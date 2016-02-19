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
	final static String archivePath = "C:\\Users\\Андрей\\Downloads\\acm\\";
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

	private static CheckingInfo check(String path, String fileName, int taskId, String progType) {
		String newFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".exe";
		String taskPath = archivePath + taskId;
		int n = new File(taskPath + "\\tests\\").listFiles().length / 2;
		
		CheckingInfo cInfo = new CheckingInfo();
		cInfo.setVerdict(CheckingResult.AC);
		for(int i = 1; i <= n; i++) {
			String result = getProgramResult(path, newFileName, taskPath + "\\tests\\" + i + ".in", progType);
			if(result == null) {
				cInfo.setTestNumber(i);
				cInfo.setVerdict(CheckingResult.TLE);
				cInfo.setTime(lastTime);
				break;
			}
			if(!compareAnswers(getFileContents(taskPath + "\\tests\\" + i + ".out"), result)) {
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
	
	public static CheckingInfo checkProgram(String path, String fileName, int taskId) {
		if(fileName.endsWith(".cpp")) {
			Compiler.compileCPlusPlus(path, fileName);
			return check(path, fileName, taskId, "EXE");
		} else
		if(fileName.endsWith(".cs")) {
			Compiler.compileCSharp(path, fileName);
			return check(path, fileName, taskId, "EXE");
		} else
		if(fileName.endsWith(".java")) {
			return check(path, fileName, taskId, "JAVA");
		}
		return null;
	}
	
}
