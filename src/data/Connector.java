package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector {
	Connection conn;
	
	public Connector(){
		conn = null; 
	}
	
	public void connect(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/stmp_redes", "root", "");
			System.out.println("Connection success");
		} catch (SQLException e) {
			System.out.println(e);
		}	
	}
	
	public void execute(String query){
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()){
				System.out.println("Ussername: " + rs.getString("username"));
				System.out.println("Password: " + rs.getString("password") + "\n");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
