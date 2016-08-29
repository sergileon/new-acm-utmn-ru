package ru.sibint.olymp.checker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.sibint.olymp.compiler.Compiler;

class TestChecker implements Runnable {

	static Logger logger = Logger.getGlobal();
	
	private String path;
	private String fileName;
	private String testName;
	private String programType;
	private String answer = "";
	private Process currentProcess;
	private long memoryUsage = 0;
	
	public TestChecker(String _path, String _fileName, String _testName, String _programType) {
		path = _path;
		fileName = _fileName;
		testName = _testName;
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
			pb.redirectInput(new File(testName));
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
			logger.log(Level.SEVERE, "Can not execute command: " + path + fileName + " < " + testName);
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
	
	public static String getFileContents(String path) {
		File F = new File(path);
		try {
			Scanner S = new Scanner(F);
			String answer = "";
			while(S.hasNext()) {
				answer += S.next();
			}
			S.close();
			return answer;
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Cant read file contents from given path: " + path);
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}
	
	public static String getProgramResult(String path, String fileName, String testName, String programType) {
		if(programType.equals("JAVA")) fileName = "Solution";
		TestChecker testChecker = new TestChecker(path, fileName, testName, programType);
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
			logger.log(Level.SEVERE, "Can't check test " + testName);
			logger.log(Level.SEVERE, e.getMessage());
		}
		return testChecker.getAnswer();
	}

	private static CheckingInfo check(String taskPath, String path, String fileName, int taskId, String progType, String environment) {
		String newFileName = ""; 
		if(progType.equals("EXE")) {
			newFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".exe";
			if(environment.equals("linux")) newFileName = fileName + "exec";
		} else {
			newFileName = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		System.out.println("Checking task on the path " + taskPath);
		
		CheckingInfo cInfo = new CheckingInfo();
		cInfo.setVerdict(CheckingResult.AC);
		int i = 0;
		for(File F: new File(taskPath).listFiles()) {
			i++;
			if(F.getAbsolutePath().endsWith(".out")) continue;
			String filePath = F.getAbsolutePath();
			String result = getProgramResult(path, newFileName, filePath, progType);
			if(result == null) {
				cInfo.setTestNumber(i);
				cInfo.setVerdict(CheckingResult.TLE);
				cInfo.setTime(lastTime);
				break;
			}
			String outFile = filePath.substring(0, filePath.length() - 3) + ".out";
			System.out.println(outFile);
			if(!compareAnswers(getFileContents(outFile), result)) {
				logger.log(Level.INFO, "Answer of the task is not equals to program output");
				logger.log(Level.INFO, "Reference answer is: " + outFile);
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
	
	public static CheckingInfo checkProgram(String archivePath, String path, String fileName, int taskId, String environment) {
		if(fileName.endsWith(".cpp")) {
			String result = Compiler.compileCPlusPlus(path, fileName);
			if(result != null) {
				CheckingInfo ci = new CheckingInfo();
				ci.setVerdict(CheckingResult.CE);
				ci.setMessage(result);
				return ci;
			}
			return check(archivePath, path, fileName, taskId, "EXE", environment);
		} else
		if(fileName.endsWith(".cs")) {
			String result = Compiler.compileCSharp(path, fileName);
			if(result != null) {
				CheckingInfo ci = new CheckingInfo();
				ci.setVerdict(CheckingResult.CE);
				ci.setMessage(result);
				return ci;
			}
			return check(archivePath, path, fileName, taskId, "EXE", environment);
		} else
		if(fileName.endsWith(".java")) {
			String result = Compiler.compileJava(path, fileName);
			if(result != null) {
				CheckingInfo ci = new CheckingInfo();
				ci.setVerdict(CheckingResult.CE);
				ci.setMessage(result);
				return ci;
			}
			return check(archivePath, path, fileName, taskId, "JAVA", environment);
		}
		return null;
	}
	
}
