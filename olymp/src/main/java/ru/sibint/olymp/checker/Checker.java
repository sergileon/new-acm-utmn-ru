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

class TestChecker implements Runnable {

	static Logger logger = Logger.getGlobal();
	
	private String path;
	private String fileName;
	private String testInputData;
	private String programType;
	private String answer = "";
	private Process currentProcess;
	private long memoryUsage = 0;
	
	public TestChecker(String _path, String _fileName, String _testInputData, String _programType) {
		path = _path;
		fileName = _fileName;
		testInputData = _testInputData;
		programType = _programType;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void stopProcess() {
		currentProcess.destroy();
	}
	
	public void run() {
		try {
			String command = "";
			if(programType.equals("EXE"))
				command = path + fileName;
			if(programType.equals("JAVA")) 
				command = "java -classpath " + path + " " + fileName;
			command = "java -classpath " + "\"" + path + "\"" + " " + fileName;
			System.out.println(command);
			ProcessBuilder pb = new ProcessBuilder(command);

			File f = File.createTempFile("temp" + String.valueOf(System.nanoTime()), ".tmp");
			PrintWriter pw = new PrintWriter(f);
			pw.print(testInputData);
			pw.flush();
			pw.close();

			pb.redirectInput(f);

			long startMemory = Runtime.getRuntime().freeMemory();
			currentProcess = pb.start();
			long endMemory = Runtime.getRuntime().freeMemory();
			
			memoryUsage = startMemory - endMemory;
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine())!= null) {
			    sb.append(line + "\n");
			}
			answer = sb.toString();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Can not execute command: " + path + fileName);
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public long getMemoryUsage() {
		return memoryUsage;
	}

}

public class Checker {

	static Logger logger = Logger.getGlobal();
	//TODO Change hardcoded constants
	private static long lastTime = 0;
	private static long lastMem = 0;

	public static boolean compareAnswers(String referenceAns, String programAns)
	{
		String refAns = ""; 
		for(int i = 0; i < referenceAns.length(); i++)
		{
			if(referenceAns.charAt(i) >= 33) {
				refAns += referenceAns.charAt(i);
			}
		}
		String prgAns = ""; 
		for(int i = 0; i < programAns.length(); i++)
		{
			if(programAns.charAt(i) >= 33) {
				prgAns += programAns.charAt(i);
			}
		}
		return prgAns.equals(refAns);
	}
	
	public static String getProgramResult(String path, String fileName, String testInputData, String programType) {
		if(programType.equals("JAVA")) fileName = "Solution";
		TestChecker testChecker = new TestChecker(path, fileName, testInputData, programType);
		Thread t = new Thread(testChecker);
		ThreadMXBean tBean = ManagementFactory.getThreadMXBean();
		t.start();
		try {
			t.join(2000);
			long time = tBean.getThreadCpuTime(t.getId());
			lastTime = time;
			lastMem = testChecker.getMemoryUsage();
			if(t.isAlive()) {
				testChecker.stopProcess();
				return null;
			}
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Can't check test");
			logger.log(Level.SEVERE, e.getMessage());
		}
		return testChecker.getAnswer();
	}

	private static CheckingInfo check(String path, String fileName, int taskId, String progType, String environment) {
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
			String result = getProgramResult(path, newFileName, tests.get(i).get("in"), progType);
			if(result == null) {
				cInfo.setTestNumber(i + 1);
				cInfo.setVerdict(CheckingResult.TLE);
				cInfo.setTime(lastTime);
				break;
			}
			if(!compareAnswers(tests.get(i).get("out"), result)) {
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
	
	public static CheckingInfo checkProgram(String path, String fileName, int taskId, String environment) {
		if(fileName.endsWith(".cpp")) {
			String result = Compiler.compileCPlusPlus(path, fileName);
			if(result != null) {
				CheckingInfo ci = new CheckingInfo();
				ci.setVerdict(CheckingResult.CE);
				ci.setMessage(result);
				return ci;
			}
			return check(path, fileName, taskId, "EXE", environment);
		} else
		if(fileName.endsWith(".cs")) {
			String result = Compiler.compileCSharp(path, fileName);
			if(result != null) {
				CheckingInfo ci = new CheckingInfo();
				ci.setVerdict(CheckingResult.CE);
				ci.setMessage(result);
				return ci;
			}
			return check(path, fileName, taskId, "EXE", environment);
		} else
		if(fileName.endsWith(".java")) {
			String result = Compiler.compileJava(path, fileName);
			if(result != null) {
				CheckingInfo ci = new CheckingInfo();
				ci.setVerdict(CheckingResult.CE);
				ci.setMessage(result);
				return ci;
			}
			return check(path, fileName, taskId, "JAVA", environment);
		}
		return null;
	}
	
}
