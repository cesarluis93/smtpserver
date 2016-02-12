package data;

import java.util.ArrayList;

import org.json.JSONArray;

public class Tester {

	public static void main(String[] args) {
		Connector.connect();
		
		DBManager dbm = new DBManager();
		//dbm.deleteUser("Mary");
		//dbm.createUser("Mary", "hola", "12-5-1998");
		
		//ArrayList<String> destinos = new ArrayList<String>();
		//destinos.add("ale");
		//destinos.add("miguel");
		//destinos.add("Carlos");
		//destinos.add("hans");
		//destinos.add("Julio");
		//dbm.newMail("Mary", destinos, "Hola cómo están.?");
		
		JSONArray correos = dbm.retrieveJsonMails("Miguel");
		if (correos != null)
			System.out.println(Tools.convertToContentJsonView(correos.toString()));
		
		Connector.close();

	}

}
