package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mail {
	private int id;
	private User from;
	private User to;
	private String subject;
	private String dateReceived;
	private String body;

	public Mail() {
		this.body = "";
	}

	public Mail(User from, User to, String subject, String body) {
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	public Mail(int id, User from, User to, String subject, String dateReceived, String body) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.dateReceived = dateReceived;
		this.body = body;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public User getTo() {
		return to;
	}

	public void setTo(User to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int save() {        
		// Insertar correo a la BD.
		String query = String.format(
			"INSERT INTO mails (user_from, user_to, subject, body) VALUES ('%s', %d, '%s', '%s');",
			this.from.getUsername(), this.to.getId(), this.subject, this.body
		);

		// Insertar en la BD
		return Connector.update(query);
	}
}
