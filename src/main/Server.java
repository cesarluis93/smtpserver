package main;

import dns.DNSlookup;

public class Server {

	public static void main(String[] args) {
		System.out.println("Hola chechita!");
		
		String[] test = {"yahoo.com", "gcc.org", "google.com", "chechita.com"};
		
		DNSlookup dns = new DNSlookup();
		dns.getNames(test);
		
	}
	
	/*
	 * Tips: Recuerde las pruebas realizadas en clase. 
	 * Haga pruebas contra un servidor SMTP y observe la secuencia de comandos. 
	 * Puede hacer pruebas para analizar el flujo de datos utilizando el siguiente servidor:
	 * postini.com.s200a1.psmtp.com
	 */

}
