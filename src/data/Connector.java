package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class Connector {
	private static Connection conn;
	
	public Connector(){
		conn = null; 
	}
	
	public static void connect(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/smtp_redes", "root", "");
			System.out.println("Connection success");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public static void close(){
		try {
			conn.close();
			System.out.println("Connection clossed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ResultSet execute(String query){
		Statement stmt;
		ResultSet rs;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static int update(String query){
		Statement stmt;
		int rowsAffected;
		try {
			stmt = conn.createStatement();
			rowsAffected = stmt.executeUpdate(query);
			
			return rowsAffected;
		} catch (SQLException e) {
			e.printStackTrace();
			
			return -1;
		}
	}
	
}
