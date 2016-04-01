package ru.sibint.olymp.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Compiler {

	static Logger logger = Logger.getGlobal();
	//TODO Change hardcoded constants
	//static String VCPPPath = "\"C:\\Program Files (x86)\\Microsoft Visual Studio 12.0\\VC\\";
	//static String VCSPath = "\"C:\\Windows\\Microsoft.NET\\Framework\\v4.0.30319\\";
	static String VCPPPath = "\"C:\\Program Files (x86)\\Microsoft Visual Studio 12.0\\VC\\";
	static String VCSPath = "\"C:\\Windows\\Microsoft.NET\\Framework\\v4.0.30319\\";
	
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
		//executeCommand(VCPPPath + "vcvarsall.bat\" & " + VCPPPath + "bin\\cl\" " + "/Fe" + path +  fileName.substring(0, fileName.lastIndexOf('.')) + ".exe " + path + fileName);
		executeCommand("gcc " + path + fileName + " -o " + path + fileName + "exec");
	}
	
	public static void compileCSharp(String path, String fileName) {
		//executeCommand(VCSPath + "csc.exe\"" + " /out:" + path + fileName.substring(0, fileName.lastIndexOf('.')) + ".exe " + path + fileName);
	}
	
}
