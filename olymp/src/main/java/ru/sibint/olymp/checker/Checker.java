package ru.sibint.olymp.checker;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.sibint.olymp.compiler.Compiler;
import ru.sibint.olymp.dbsync.DBProxy;

class TestChecker {

	static Logger logger = Logger.getGlobal();

	private String path;
	private String runPath;
	private String fileName;
	private String testInputData;
	private String programType;
	private String answer = "";
	private String info = "";
	private Process currentProcess;
	private long memoryUsage = 0;
	
	public TestChecker(String _path, String _fileName, String _testInputData, String _programType, String _runPath) {
		path = _path;
		fileName = _fileName;
		testInputData = _testInputData;
		programType = _programType;
		runPath = _runPath;
	}

	public String getAnswer() {
		return answer;
	}

	public String getInfo() {
		return info;
	}

	public void run() {
		try {
			File f = File.createTempFile("temp" + String.valueOf(System.nanoTime()), ".tmp");
			File f_out = File.createTempFile("temp" + String.valueOf(System.nanoTime()), ".out");
			PrintWriter pw = new PrintWriter(f);
			pw.print(testInputData);
			pw.flush();
			pw.close();

			String command = runPath + "run -m 64M -t 3s -i \"" + f.getAbsolutePath() + "\" ";
			ProcessBuilder pb = null;
			if(programType.equals("EXE")) {
				command += "\"" + path + fileName + "\"";
				pb = new ProcessBuilder(runPath + "run", "-m", "64M", "-t", "3s", "-i", f.getAbsolutePath(), "-o", f_out.getAbsolutePath(), path + fileName);
			}
			if(programType.equals("JAVA")) {
				command += "java -classpath " + path + " " + fileName;
				pb = new ProcessBuilder("java", "-classpath", path, fileName);
			}

			System.out.println(command);

			long startMemory = Runtime.getRuntime().freeMemory();
			currentProcess = pb.start();
			currentProcess.waitFor();
			long endMemory = Runtime.getRuntime().freeMemory();
			
			memoryUsage = startMemory - endMemory;

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f_out)));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine())!= null) {
			    sb.append(line + "\n");
			}
			answer = sb.toString();

			reader = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()));
			line = "";
			sb = new StringBuilder();
			while ((line = reader.readLine())!= null) {
				sb.append(line + "\n");
			}
			info = sb.toString();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Can not execute command: " + path + fileName);
			logger.log(Level.SEVERE, e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public long getMemoryUsage() {
		String value = info.split("peak memory:   ")[1].split(" of")[0];
		return Long.parseLong(value);
	}

	public Double getTimeUsage() {
		String value = info.split("time consumed: ")[1].split(" of")[0];
		return Double.parseDouble(value);
	}

}

public class Checker {

	static Logger logger = Logger.getGlobal();
	//TODO Change hardcoded constants
	private static Double lastTime = 0.0;
	private static Long lastMem = 0L;
	private ResultChecker resultChecker;

	public Checker(String checkerCode, String checkerLanguage) {
		resultChecker = new ResultChecker(checkerCode, checkerLanguage);
	}

	public boolean compareAnswers(String referenceAns, String programAns, String originalInput)
	{
		return resultChecker.check(originalInput, programAns);
	}
	
	public static String getProgramResult(String path, String fileName, String testInputData, String programType, String runPath) {
		if(programType.equals("JAVA")) fileName = "Solution";
		TestChecker testChecker = new TestChecker(path, fileName, testInputData, programType, runPath);
		testChecker.run();
		lastTime = testChecker.getTimeUsage();
		lastMem = testChecker.getMemoryUsage();
		return testChecker.getAnswer();
	}

	private CheckingInfo check(String path, String fileName, int taskId, String progType, String environment, String runPath) {
		String newFileName = ""; 
		if(progType.equals("EXE")) {
			newFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".exe";
			if(environment.equals("linux")) newFileName = fileName + "exec";
		} else {
			newFileName = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		System.out.println("Checking task on the path with fileName " + fileName);
		
		CheckingInfo cInfo = new CheckingInfo();
		cInfo.setVerdict(CheckingResult.AC);
		List<Map<String, String>> tests = DBProxy.getAllTestData(taskId);

		for(int i = 0; i < tests.size(); i++) {
			String result = getProgramResult(path, newFileName, tests.get(i).get("in"), progType, runPath);
			if(result == null) {
				cInfo.setTestNumber(i + 1);
				cInfo.setVerdict(CheckingResult.TLE);
				cInfo.setTime(lastTime);
				break;
			}
			if(!compareAnswers(tests.get(i).get("out"), result, tests.get(i).get("in"))) {
				logger.log(Level.INFO, "Answer of the task is not equals to program output");
				logger.log(Level.INFO, "Reference answer is: " + tests.get(i).get("out"));
				logger.log(Level.INFO, "Programs answer is: " + result);
				cInfo.setTestNumber(i);
				cInfo.setVerdict(CheckingResult.WA);
				cInfo.setTime(lastTime);
				break;
			}
			if(cInfo.getTime() < lastTime) {
				cInfo.setTime(lastTime);
			}
			if(cInfo.getMemory() < lastMem) {
				cInfo.setMemory(lastMem);
			}
		}
		return cInfo;
	}
	
	public CheckingInfo checkProgram(String path, String fileName, int taskId, String environment, String runPath) {
		if(fileName.endsWith(".cpp")) {
			String result = Compiler.compileCPlusPlus(path, fileName);
			if(result != null) {
				CheckingInfo ci = new CheckingInfo();
				ci.setVerdict(CheckingResult.CE);
				ci.setMessage(result);
				return ci;
			}
			return check(path, fileName, taskId, "EXE", environment, runPath);
		} else
		if(fileName.endsWith(".cs")) {
			String result = Compiler.compileCSharp(path, fileName);
			if(result != null) {
				CheckingInfo ci = new CheckingInfo();
				ci.setVerdict(CheckingResult.CE);
				ci.setMessage(result);
				return ci;
			}
			return check(path, fileName, taskId, "EXE", environment, runPath);
		} else
		if(fileName.endsWith(".java")) {
			String result = Compiler.compileJava(path, fileName);
			if(result != null) {
				CheckingInfo ci = new CheckingInfo();
				ci.setVerdict(CheckingResult.CE);
				ci.setMessage(result);
				return ci;
			}
			return check(path, fileName, taskId, "JAVA", environment, runPath);
		}
		return null;
	}
	
}
