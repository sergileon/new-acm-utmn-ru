package ru.sibint.olymp.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checker {

	static Logger logger = Logger.getGlobal();
	//TODO Change hardcoded constants
	static String archivePath = "C:\\Users\\Андрей\\Downloads\\acm\\";

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
		try {
			ProcessBuilder pb = new ProcessBuilder(path + fileName);
			pb.redirectInput(new File(testName));
			BufferedReader reader = new BufferedReader(new InputStreamReader(pb.start().getInputStream()));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine())!= null) {
			    sb.append(line + "\n");
			}
			return sb.toString();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Can not execute command: " + path + fileName + " < " + testName);
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	private static CheckingResult check(String path, String fileName, int taskId, String progType) {
		String newFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".exe";
		String taskPath = archivePath + taskId;
		int n = new File(taskPath + "\\tests\\").listFiles().length;
		for(int i = 1; i <= n; i++) {
			String result = getProgramResult(path, newFileName, taskPath + "\\tests\\" + i + ".in", progType);
			if(!compareAnswers(getFileContents(taskPath + "\\tests\\" + i + ".out"), result)) {
				return CheckingResult.WA;
			}
		}
		return CheckingResult.AC;
	}
	
	public static CheckingResult checkProgram(String path, String fileName, int taskId) {
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
