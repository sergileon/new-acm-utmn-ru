package ru.sibint.olymp.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Compiler {

	static Logger logger = Logger.getGlobal();

	private static String cpPath = "";
	private static String csPath = "";
	static Properties properties = null; 
	final static String propertiesFile = "config.properties";

	static {
		properties = new Properties();
		InputStream configStream = Compiler.class.getClassLoader().getResourceAsStream(propertiesFile);
		if(configStream != null) {
			try {
				properties.load(configStream);
				cpPath = properties.getProperty("vcppath");
				csPath = properties.getProperty("vcspath");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Can not load properties file");
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	public static void executeCommand(String command) {
		try {
			System.out.println(command);
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine())!= null) {
			    sb.append(line + "\n");
			}
			logger.log(Level.INFO, sb.toString());
			System.out.println(sb.toString());
		} catch (IOException e) {
			System.out.println("Can not execute command " + command);
			System.out.println(e.getMessage());
			logger.log(Level.SEVERE, "Can not execute command " + command);
			logger.log(Level.SEVERE, e.getMessage());
		} catch (InterruptedException e) {
			System.out.println("Can not execute command " + command);
			System.out.println(e.getMessage());
			logger.log(Level.SEVERE, "Can not execute command " + command);
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public static void compileJava(String path, String fileName) {
		executeCommand("javac " + path + fileName);
	}
	
	public static void compileCPlusPlus(String path, String fileName) {
		executeCommand("\"" + cpPath + "gcc\" " + " -o " + path +  fileName.substring(0, fileName.lastIndexOf('.')) + ".exe " + path + fileName);
	}
	
	public static void compileGCC(String path, String fileName) {
		executeCommand("gcc " + path + fileName + " -o " + path + fileName + "exec");
	}
	
	public static void compileCSharp(String path, String fileName) {
		executeCommand("\"" + csPath + "csc.exe\"" + " /out:" + path + fileName.substring(0, fileName.lastIndexOf('.')) + ".exe " + path + fileName);
	}
	
}
