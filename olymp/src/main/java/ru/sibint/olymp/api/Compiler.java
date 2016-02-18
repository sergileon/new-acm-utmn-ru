package ru.sibint.olymp.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Compiler {

	static Logger logger = Logger.getGlobal();
	
	private static void executeCommand(String command) {
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine())!= null) {
			    sb.append(line + "\n");
			}
			logger.log(Level.INFO, sb.toString());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Can not execute command " + command);
			logger.log(Level.SEVERE, e.getMessage());
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Can not execute command " + command);
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public static void compileJava(String path, String fileName) {
		executeCommand("javac " + path + fileName);
	}
	
	public static void compileCPlusPlus(String path, String fileName) {
		executeCommand("\"C:\\Program Files (x86)\\Microsoft Visual Studio 12.0\\VC\\vcvarsall.bat\"");
		executeCommand("\"C:\\Program Files (x86)\\Microsoft Visual Studio 12.0\\VC\\bin\\cl\" " + path + fileName);
	}
	
	public static void compileCSharp(String path, String fileName) {
		executeCommand("\"C:\\Windows\\Microsoft.NET\\Framework\\v4.0.30319\\csc.exe\"" + " /out:" + path + fileName.substring(0, fileName.lastIndexOf('.')) + ".exe " + path + fileName);
	}
	
}
