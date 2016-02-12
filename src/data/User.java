package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class User {
	private int id;
	private String username;
	private String password;
	private String birthdate;

	public User(int id, String username, String password, String birthdate) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.birthdate = birthdate;
	}

	public User(String username, String password, String birthdate) {
		this.username = username;
		this.password = password;
		this.birthdate = birthdate;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public int save() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY");
        java.util.Date parsedDate = null;
		try {
			parsedDate = format.parse(this.birthdate);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			return -1;
		}
		
		// Convertir el string parseado a una fecha válida para SQL
        java.sql.Date sql = new java.sql.Date(parsedDate.getTime());
        
		// Preparar la query
		String query = String.format("INSERT INTO usuarios (username, password, birthdate) VALUES ('%1$s', '%2$s', '%3$s');",
			this.username, this.password, sql
		);

		// Insertar en la BD
		return Connector.update(query);
	}
	
	public int delete() {
		String query = "DELETE FROM usuarios WHERE id = '" + this.id + "';";
		
		return Connector.update(query);
	}
	
	public ArrayList<Mail> retrievedMails() {
		String query = "SELECT * FROM mails M LEFT JOIN usuarios U ON M.user_from = U.id WHERE M.user_to = '" + this.id + "' ORDER BY M.date_received DESC;";
		
		ArrayList<Mail> list = new ArrayList<Mail>();
		try {
			ResultSet mails = Connector.execute(query);
			if (mails == null) {
				return null;
			}
			while (mails.next()) {
				User from = new User(
					mails.getString("username"),
					mails.getString("password"),
					mails.getString("birthdate")
				);
				list.add(
					new Mail(
						mails.getInt("id"),
						from,
						this,
						mails.getString("date_received"),
						mails.getString("body")
					)
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
			return null;
		}
		
		return list;
	}
	
}
