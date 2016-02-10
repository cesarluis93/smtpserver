package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Statement;

public class DBManager {
	Connector conn;
	ResultSet rs;

	public DBManager() {
		conn = new Connector();
		conn.connect();
	}
	
	public int createUser(String user, String pass) {
		if (this.existUser(user) == 1){
			System.out.println("Ya existe un usuario con el mismo nombre de usuario.");
			return 0;
		}
		
		String query = String.format("INSERT INTO `usuario` (`username`, `password`) VALUES ('%1$s', '%2$s');",
				user, pass);
		
		int result = conn.update(query);
		
		if (result > 0){
			System.out.println("Usuario ingresado con éxito");
			return 1;
		}
		System.out.println("No se pudo ingresar el usuario");
		
		return -1;

	}
	
	public int deleteUser(String user){
		if (this.existUser(user) == 0){
			System.out.println("El nombre de usuario '" + user + "' no existe.");
			return 0;
		}
		
		String query = "DELETE FROM `usuario` WHERE `username` = '" + user + "';";
		
		int result = conn.update(query);
		
		if (result > 0){
			System.out.println("Usuario eliminado con éxito");
			return 1;
		}
		System.out.println("No se pudo eliminar el usuario");
		
		return -1;
	}
	
	public int existUser(String user){
		String querie = "SELECT count(*) AS c FROM usuario WHERE username='" + user +"';";
		rs = conn.execute(querie);
		
		try {
			rs.next();
			return (rs.getInt("c") == 0) ? 0 : 1;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return -1;
	}
	
    
    

}
