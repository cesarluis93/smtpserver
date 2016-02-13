package data;

import java.util.ArrayList;

import org.json.JSONArray;

public class Tester {

	public static void main(String[] args) {
		Connector.connect();
		
		DBManager dbm = new DBManager();
		//dbm.deleteUser("Mary");
		//dbm.createUser("Mary", "hola", "12-5-1998");
		
//		ArrayList<String> destinos = new ArrayList<String>();
//		destinos.add("ale");
//		destinos.add("miguel");
//		destinos.add("Carlitos");
//		destinos.add("Mariela");
//		destinos.add("Julio");
//		System.out.println(
//			"Rechazados: \n" +
//			dbm.newMail("Cesar", destinos, "Hola cómo están.?").toString()
//		);
		
		JSONArray correos = dbm.retrieveJsonMails("Miguel");
		if (correos != null)
			System.out.println(
				"Correos de Miguel: \n" + 
				Tools.convertToContentJsonView(correos.toString())
			);
		
		Connector.close();

	}

}
