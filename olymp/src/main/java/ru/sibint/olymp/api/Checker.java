package ru.sibint.olymp.api;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.stream.StreamResult;

public class Checker {

	static Logger logger = Logger.getGlobal();

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
			return answer;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getProgramResult(String path, String fileName, String testName) {
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
		/*} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Can not execute command: " + path + fileName + " < " + testName);
			e.printStackTrace();*/
		}
		return null;
	}

	private static CheckingResult checkEXE(String path, String fileName, int taskId) {
		String newFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".exe";
		//TODO Change hardcoded constants
		String taskPath = "C:\\Users\\Андрей\\Downloads\\acm\\" + taskId;
		//GetNumber of tests
		int n = 5;
		for(int i = 1; i <= n; i++) {
			String result = getProgramResult(path, newFileName, taskPath + "\\tests\\" + i + ".in");
			if(!compareAnswers(getFileContents(taskPath + "\\tests\\" + i + ".out"), result)) {
				return CheckingResult.WA;
			}
		}
		return CheckingResult.AC;
	}
	
	private static CheckingResult checkJAVA(String path, String fileName, int taskId) {
		String newFileName = fileName.substring(0, fileName.lastIndexOf('.'));
		//TODO Change hardcoded constants
		String taskPath = "C:\\Users\\Андрей\\Downloads\\acm\\" + taskId;
		//executeCommand("java " + path + fileName);
		//GetNumber of tests
		int n = 5;
		for(int i = 1; i <= n; i++) {
			String result = getProgramResult(path, newFileName, taskPath + "\\tests\\" + i + ".in");
			if(!compareAnswers(getFileContents(taskPath + "\\tests\\" + i + ".out"), result)) {
				return CheckingResult.WA;
			}
		}
		return CheckingResult.AC;
	}
	
	public static CheckingResult checkProgram(String path, String fileName, int taskId) {
		if(fileName.endsWith(".cpp")) {
			Compiler.compileCPlusPlus(path, fileName);
			return checkEXE(path, fileName, taskId);
		} else
		if(fileName.endsWith(".cs")) {
			Compiler.compileCSharp(path, fileName);
			return checkEXE(path, fileName, taskId);
		} else
		if(fileName.endsWith(".java")) {
			return checkJAVA(path, fileName, taskId);
		}
		return null;
	}
	
}
