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
			String qury = "SELECT Id, UserId, TaskId, Verdict, TimeSpent, MemorySpent FROM Submission LIMIT 0," + String.valueOf(count);
			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
		    stm.execute(qury);
			ResultSet rs = stm.getResultSet();
			boolean bl = false;
			while(rs.next()) {
				if(bl) result += ",";
				result += 
						"{\"id\":\"" + rs.getString(1) + "\"," +  
						"\"auth\":" + rs.getString(2) + "\"," +
						"\"verd\":" + rs.getString(3) + "\"," +
						"\"time\":" + rs.getString(4) + "\"," +
						"\"mem\":" + rs.getString(5) + "\"}";
				bl = true;
			}
			result += "]";
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
			return result;
		}
	}
	
	public static void main(String args[]) {
		System.out.println(getSubmissions(2));
	}
	
}
