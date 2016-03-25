package ru.sibint.olymp.dbsync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBProxy {
	public static String getSubmissions(int count) {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		}

		String result = "[";
		try {
			Statement stm = connectionMysql.createStatement();
			stm.setFetchSize(1000);
			String qury = "SELECT Submission.Id, UserApp.Name as Name, TaskId, Verdict, TimeSpent, MemorySpent FROM Submission LEFT JOIN UserApp ON Submission.UserId = UserApp.Id ORDER BY Submission.Id DESC LIMIT 0," + String.valueOf(count);
			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
		    stm.execute(qury);
			ResultSet rs = stm.getResultSet();
			boolean bl = false;
			while(rs.next()) {
				if(bl) result += ",";
				result += 
						"{\"id\":\"" + rs.getString(1) + "\"," +  
						"\"auth\":\"" + rs.getString(2) + "\"," +
						"\"task\":\"" + rs.getString(3) + "\"," +
						"\"verd\":\"" + rs.getString(4) + "\"," +
						"\"time\":\"" + rs.getString(5) + "\"," +
						"\"mem\":\"" + rs.getString(6) + "\"}";
				bl = true;
			}
			result += "]";
			System.out.println(result);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public static void addUser(String userName, String userMail) {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		try {
			Statement stm = connectionMysql.createStatement();
			stm.setFetchSize(1000);
			String qury = "INSERT INTO UserApp (Name, Email) VALUES ('" + userName + "', '" + userMail + "')";
			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
		    stm.execute(qury);
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
			return;
		}
	}
	
	public static void addSubmission(String userEmail, String taskId, String verdict, String timeForTask, String memoryForTask) {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		String userId = "1";
		String selectIdQuery = "SELECT Id FROM UserApp WHERE email = '" + userEmail + "'";
		System.out.println(selectIdQuery);
		try {
			Statement stm = connectionMysql.createStatement();
			stm.setFetchSize(1000);
		    
			ResultSet rs1 = stm.executeQuery(selectIdQuery);
			while(rs1.next()) {
				userId = rs1.getString(1);
				System.out.println(rs1.getString(1));
			}
			String qury = "INSERT INTO Submission (TaskId, Verdict, TimeSpent, MemorySpent, UserId) VALUES ('" + taskId + "', '" + verdict+ "', '" + timeForTask + "', '" + memoryForTask + "', '" + userId + "')";

			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
			stm.execute(qury);
		    
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
			return;
		}
	}

	public static void main(String args[]) {
		System.out.println(getSubmissions(2));
	}
	
}
