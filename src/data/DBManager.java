package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DBManager {
	
	public DBManager(){};
	
	/**
	 * Crea un usuario y lo registra en la BD si no existe.
	 * @param user Nombre de usuario
	 * @param pass Contraseña de usuario
	 * @param birthdate Fecha de nacimiento
	 */
	public void createUser(String user, String pass, String birthdate) {
		User usuario = this.existUser(user);
		if (usuario != null){
			System.out.println("Ya existe un usuario registrado como '" + user +"'.");
			return;
		}
		
		usuario = new User(user, pass, birthdate);
		if (usuario.save() > 0){
			System.out.println("Usuario '" + user + "' ingresado con éxito.");
		} else {
			System.out.println("No se pudo guardar el usuario '" + user + "'.");
		}
	}
	
	/**
	 * Eliminar un usuario si existe.
	 * @param user El usuario a eliminar
	 */
	public void deleteUser(String user){
		User usuario = this.existUser(user);
		if (usuario == null){
			System.out.println("El nombre de usuario '" + user + "' no existe.");
			return;
		}
		
		if (usuario.delete() > 0){
			System.out.println("Usuario '" + user + "' eliminado con éxito.");
		} else {
			System.out.println("No se pudo eliminar el usuario '" + user + "'.");
		}
	}
	
	/**
	 * Verifica si un usuario existe o no.
	 * @param user El nombre del usuario a examinar
	 * @return	El objeto que mapea al usuario si existe. De otro modo, devuelve null
	 */
	public User existUser(String user){
		String query = "SELECT * FROM usuarios WHERE username='" + user +"';";
		User usuario = null;
		
		try {
			ResultSet rs = Connector.execute(query);	
			while (rs.next()) {
				usuario = new User(
					rs.getInt("id"),
					rs.getString("username"),
					rs.getString("password"),
					rs.getString("birthdate")
				);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return usuario;
	}
	
	/**
	 * Crea y registro un correo a la BD.
	 * @param from Usuario que envía el correo
	 * @param rcpt_to	Listado de usuarios destinatarios
	 * @param data	Mensaje del correo
	 * @return Lista de usuarios que no existen en el servidor
	 */
	public ArrayList<String> newMail(String from, ArrayList<String> rcpt_to, String subject, String data) {
		ArrayList<String> refused = new ArrayList<String>();
		
		User fromUser = this.existUser(from);
		if (fromUser == null){
			System.out.println("El nombre de usuario '" + from + "' no existe.");
			return null;
		}
		
		// Registrar el correo para cada destinatario.
		for (String rcpt: rcpt_to){
			// Si el destinatario no existe en la BD, no lo registra y da un aviso.
			User toUser = this.existUser(rcpt);
			if (toUser == null){
				System.out.println("El nombre de usuario '" + rcpt + "' no existe.");
				refused.add(rcpt);
				continue;
			}
			
			Mail mail = new Mail(fromUser, toUser, subject, data);
			if (mail.save() > 0) {
				System.out.println("Correo de '" + from + "' a '" + rcpt + "' ingresado con éxito.");
			} else {
				System.out.println("Correo de '" + from + "' a '" + rcpt + "' no fue ingresado.");
			}
		}
		
		return (refused.size() == 0)? null: refused;
	}
	
	public JSONArray retrieveJsonMails(String user){
		User usuario = this.existUser(user);
		if (usuario == null){
			System.out.println("El nombre de usuario '" + user + "' no existe.");
			return null;
		}
		
		ArrayList<Mail> mails = usuario.retrievedMails();
		if (mails == null){
			System.out.println("Ningún correo encontrado para el usuario '" + user + "'.");
			return null;
		}
		
		JSONArray jsonMails = new JSONArray();
		for (Mail mail: mails){
			JSONObject jsonMail = new JSONObject();
			
			try {
				jsonMail.put("from", mail.getFrom().getUsername());
				jsonMail.put("date", mail.getDateReceived());
				jsonMail.put("message", mail.getBody());
			} catch (JSONException e) {
				System.out.println("Ocurrió un error al construir el JSON.");
				System.out.println(e.getMessage());
			}
			
			jsonMails.put(jsonMail);
		}
		
		return jsonMails;
	}
	
	/**
	 * Login de un usuario dentro del sistema
	 * @param username	Nombre de usuario
	 * @param password	Contraseña de usuario
	 * @return	Si las credenciales son correctas devuelve el objeto que mapea al usuario. De otro modo, devuelve null.
	 */
	public User login(String username, String password){
		User usuario = this.existUser(username);
		// Verificar que usuario existe
		if (usuario == null){
			System.out.println("El nombre de usuario '" + username + "' no existe.");
			return null;
		}
		// Verificar que la contraseña es correcta
		if (usuario.getPassword().equals(password))
			return usuario;
		
		return null;
	}
}
