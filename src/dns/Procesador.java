package dns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.naming.NamingException;

import data.Connector;
import data.DBManager;
import parser.SMTPServer;

public class Procesador {

    private DNS Dns;
    

    public Procesador() {
        try {
            Dns = new DNS();
        } catch (NamingException ex) {
            Logger.getLogger(Procesador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public static void main(String[] args) {
		
    	
		
		String[] test = {"yahoo.com", "gcc.org", "google.com", "chechita.com"};
		
		SMTPServer domainName = new SMTPServer();
		
		DNSlookup dns = new DNSlookup();
		
		dns.getNames(test);
		
	}
    
	public void processHelo(String data)
	{
		
		boolean temp;
		
			//extraer dominio
			String[] correoUsuario = data.split("\\s+");
			//extraer el segundo elemento
			
			if(correoUsuario.length > 1)
			{
				String nombreDominio = correoUsuario[1]; //nombre del dominio que escribe
				System.out.println(nombreDominio);
				
			}
			else
			{
				//error o poner comodin?
				String nombreDominio = "dummy";
				System.out.println(nombreDominio);

			}				
	}
    
    
}
