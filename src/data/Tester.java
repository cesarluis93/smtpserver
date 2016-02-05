package data;

public class Tester {

	public static void main(String[] args) {
		Connector con = new Connector();
		con.connect();
		con.execute("SELECT * FROM usuario");
	}

}
