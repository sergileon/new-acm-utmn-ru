package ru.sibint.olymp.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.stream.StreamResult;

public class Checker {

	static Logger logger = Logger.getGlobal();

	public static boolean compareAnswers(String referenceAns, String programAns)
	{
		String refAns = ""; 
		for(int i = 0; i < programAns.length(); i++)
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
			OutputStream os = Runtime.getRuntime().exec(path + fileName + " < " + testName).getOutputStream();
			return os.toString();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Can not execute command");
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}
	
	public static CheckingResult checkProgram(String path, String fileName, int taskId) {
		Compiler.compileCSharp(path, fileName);
		String newFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".exe";
		//TODO Change hardcoded constants
		String taskPath = "C:\\Users\\Андрей\\Downloads\\acm\\" + taskId;
		//GetNumber of tests
		int n = 5;
		for(int i = 1; i <= n; i++) {
			String result = getProgramResult(path, newFileName, taskPath + "\\" + i + ".in");
			if(!compareAnswers(getFileContents(taskPath + "\\" + i + ".out"), result)) {
				return CheckingResult.WA;
			}
		}
		return CheckingResult.AC;
	}
	
}
