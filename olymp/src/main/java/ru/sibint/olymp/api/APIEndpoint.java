package ru.sibint.olymp.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ru.sibint.olymp.checker.Checker;
import ru.sibint.olymp.checker.CheckingInfo;

@Path("/rest/")
public class APIEndpoint {

	final static String propertiesFile = "config.properties";
	static String tempDir = "C:\\temp\\";
	Properties properties = null; 
	
	static Logger logger = Logger.getGlobal();
	
	{
		properties = new Properties();
		InputStream configStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
		if(configStream != null) {
			try {
				properties.load(configStream);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Can not load properties file");
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}
	
	@Path("/submit")
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String processSubmission(
			@QueryParam("ext") String ext, 
			@QueryParam("taskId") Integer taskId, 
			@QueryParam("userId") Integer userId, 
			InputStream data) {
		StringBuilder stringData = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = "";
			while((line = in.readLine()) != null) {
				stringData.append("\n");
				stringData.append(line);
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}
		
		String fileName = "source" + System.currentTimeMillis() + "." + ext;
		File F = new File(tempDir + fileName);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(F);
			pw.printf("%s", stringData.toString());
			pw.flush();
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Can not create temp file");
			logger.log(Level.SEVERE, e.getMessage());
		} finally {
			pw.close();
		}
		
		//System.out.println(ext + " " + taskId + " " + userId);
		
		CheckingInfo result = Checker.checkProgram(tempDir, fileName, taskId);
		
		return "SUCCESS: " + result.toString();
	}
	
	@Path("/adduser/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addUser(InputStream data) {
		StringBuilder stringData = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = "";
			while((line = in.readLine()) != null)
				stringData.append(line);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}
		return "{\"Status\":\"SUCCESS\"}";
	}
	
	@Path("/addtask/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addTask(InputStream data) {
		StringBuilder stringData = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = "";
			while((line = in.readLine()) != null)
				stringData.append(line);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}
		return "{\"Status\":\"SUCCESS\"}";
	}
	
	
	@Path("/addtasktest")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addTaskTest(@QueryParam("taskId") Integer taskId, InputStream data) {
		StringBuilder stringData = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = "";
			while((line = in.readLine()) != null)
				stringData.append(line);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}
		return "{\"Status\":\"SUCCESS\"}";
	}
	
	@Path("/tasklist/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getTaskList() {
		return "{\"Status\":\"SUCCESS\"}";
	}
	
	@Path("/taskinfo")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getTaskInfo(@QueryParam("taskId") Integer taskId) {
		return "{\"Status\":\"SUCCESS\"}";
	}
	
	@Path("/usertasklist")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserTaskList(@QueryParam("userId") Integer userId) {
		return "{\"Status\":\"SUCCESS\"}";
	}
	
	@Path("/serverstatus/")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getServerStatus() {
		return "Server is up and runs application of version " + properties.getProperty("version") + ".\n" + "Operation system is " + System.getProperty("os.name");
	}
	
	@Path("/submitstatus/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getSubmissions() {
		return "[{\"id\":\"2\",\"auth\":\"107th\",\"verd\":\"AC\",\"time\":\"0.001\",\"mem\":\"765 K\"}, {\"id\":\"3\",\"auth\":\"some\",\"verd\":\"AC\",\"time\":\"0.001\",\"mem\":\"765 K\"}]";
	}
	
}
