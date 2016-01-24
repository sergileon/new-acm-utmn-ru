package ru.sibint.olymp.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rest/")
public class APIEndpoint {

	final static String propertiesFile = "config.properties";
	Properties properties = null; 
	
	{
		properties = new Properties();
		InputStream configStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
		if(configStream != null) {
			try {
				properties.load(configStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Path("/submit/")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public String processSubmission(InputStream data) {
		StringBuilder stringData = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = "";
			while((line = in.readLine()) != null)
				stringData.append(line);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}
		return "SUCCESS";
	}
	
	@Path("/serverstatus/")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getServerStatus() {
		return "Server is up and runs application of version " + properties.getProperty("version");
	}
	
	@Path("/adduser/")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
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
		return "SUCCESS";
	}
	
	@Path("/addtask/")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
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
		return "SUCCESS";
	}
	
	
	@Path("/addtasktest/")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addTaskTest(InputStream data) {
		StringBuilder stringData = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = "";
			while((line = in.readLine()) != null)
				stringData.append(line);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}
		return "SUCCESS";
	}
	
}
