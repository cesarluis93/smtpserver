package parser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import data.Connector;
import data.DBManager;
import data.Mail;
import data.User;

public class SMTPServer {
	
	public static int PUERTO = 25; 
	public static int CAPACIDAD = 10;
	
	public static void main(String args[]) throws Exception
	{
		//Probando ahora 
		//server socket con tcp
		//while true
			//escuchar tcp connection
			//new HttpRequest(??);
			//crear thread(soketPorAtender);
			//thread.start();
		
		//ingresar cantidad de threads como args
		
		System.out.println("Iniciando Web Server - Protocolo SMTP");
		System.out.println("Puerto: "+PUERTO);
		ServerSocket s = new ServerSocket(PUERTO); //inicializa un nuevo socket
		
		while(true)
		{	
			System.out.println("Esperando conexion...");			
			Socket conexion = s.accept(); //espera a una peticion del cliente			
			System.out.println(">Conexion Entrante..!");	
			
			SmtpRequest request = new SmtpRequest(conexion);
			Thread thread = new Thread(request);
			thread.start();
			
		}					
	}
}

final class SmtpRequest implements Runnable{

	public String recibido;
	public String enviado;
	public Socket conexion;
	public BufferedReader input; //deprecated?
	public DataOutputStream output;
	public DataInputStream in;
	public ArrayList<Mail> MAILSPROPIOS = new ArrayList<Mail>();
	public ArrayList<Mail> MAILSAJENOS = new ArrayList<Mail>();
	
	boolean heloOK = false, fromOK = false, rcptOK = false,
			dataOK = false, breakOK = false, quitOK = false;
	
	ArrayList<String> usuariosPropios = new ArrayList<String>();
	ArrayList<String> usuariosAjenos = new ArrayList<String>();
	String usuarioEmisor, dominioEmisor, dataDeCorreo;
	
	public SmtpRequest(){}
	
	public SmtpRequest(Socket conexion) throws IOException
	{
		recibido = "";
		enviado = "";
		this.conexion = conexion;
		input = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		output = new DataOutputStream(conexion.getOutputStream());
		in = new DataInputStream(conexion.getInputStream());	
		sendResponse("200 - LabSMTP holi \n");
	}	
	
	@Override
	public void run() {
		//Recibir y responder paso por paso			
				
		boolean exit = false;
		System.out.println("Iniciando run");
		
		
		while(!exit)
		{			
			
//			try {
				//System.out.print("1");
				
				try {
					readData(); //obtener un nuevo input reader
				} catch(Exception e)
				{
					exit=true;
				}				
				
				//System.out.print("2");
				
				String dataToProcess = "";
				
				try{
					dataToProcess = input.readLine();
				}catch(Exception e)
				{
					e.printStackTrace();
					exit = true;
				}
				
				
//				byte packetData[] = new byte[in.readInt()];
//				System.out.print("3");
//			    in.readFully(packetData);
//			    System.out.print("4");
//			    String dataToProcess = new String(packetData);
			    System.out.println("Data: "+dataToProcess);
			    
			    
			    //procesar paquete
			    //primero verificar si ya ha saludado, si no hacerlo
			    boolean valido = false;
			    
			    //verificar primero si es un quit
			    
			    valido = Pattern.matches("(QUIT)|(quit)", dataToProcess);
			    if(valido)
			    {
			    	//salir
			    	exit = true;
			    	System.out.println("Terminando sesion SMTP");
			    	sendResponse("221 - So long and thanks for the Fish \n");
			    }
			    else
			    {
			    	//continuar SMTPeando jaja
			    	
			    	if(!heloOK)
				    {
			    		System.out.println("Procesando Helo");
				    	valido = processHelo(dataToProcess);				    	
				    	//verifiar valido?
				    }
				    else
				    {
				    	//ya se ha saludado adecaudamente
				    	//proceder a verificar si es un posible Mail From
				    	//o si ya se tiene origen
				    	if(!fromOK)
				    	{
				    		System.out.println("Procesando mail from");
				    		valido = processMailFrom(dataToProcess);
				    	}
				    	else
				    	{
				    		//ya se ha ingresado el mail from 
				    		//procede a verificar recipientes
				    		//rcptOK == false dataOK == false --> Debe ser un recipiente
				    		//rcptOK == true dataOK == false --> Puede ser otro rcpt o DATA
				    		//ambos true --> ya se esta leyendo data
				    		if(rcptOK == false && dataOK == false)
				    		{
				    			//debe ser un recipiente
				    			System.out.println("Procesando recipiente 1");
				    			valido = processRcptTo(dataToProcess);	
				    			
				    			/*--VERIFICAR---valido*/
				    		}
				    		else if(rcptOK == true && dataOK == false)
				    		{
				    			//otro recipiente o DATA
				    			System.out.println("Procesando recipiente 2,3..");
				    			valido = processRcptTo(dataToProcess);
				    			if(!valido)
				    			{
				    				//verificar si es un data
				    				System.out.println("Procesando data");
				    				valido = processData(dataToProcess);
				    				if(!valido)
				    				{
				    					//error
				    				}
				    			}
				    		}
				    		else if(rcptOK == dataOK == true)
				    		{
				    			//ya se esta leyendo data
				    			//leerla... hasta encontrar el punto final
				    			
			    				//si no se ha ingresado
			    				//.
			    				//entonces leer mail
			    				//verificar aca si es un 
			    				//.			    				
			    							    				
			    				boolean temp = Pattern.matches("\\.", dataToProcess);
			    				if(temp)
			    				{
			    					//se termino la data
			    					breakOK = true;
			    					//responder OK
			    					System.out.println("Mail terminado");
			    					createAndSendMail();
			    					
			    					/*-----------------*/
			    					//ahora CORREOSAJENOS ya tiene todo lo necesario
			    					//enviar para reenvio de correos!!!!!!!!!!!!!
			    					/*-----------------*/
			    					
			    					sendResponse("250 - OK, mail terminado \n");
			    					
			    					//reset lo necesario
			    					//resetear las booleanas salvo helo
			    					fromOK = rcptOK = dataOK = breakOK = false;
			    					
			    					//limpiar arraylists de correos
			    					MAILSPROPIOS.clear();
			    					MAILSAJENOS.clear();
			    					
			    					//limpiar nombres
			    					usuariosPropios.clear();
			    					usuariosAjenos.clear();
			    					
			    				}
			    				else
			    				{
			    					//sigue siendo data
			    					System.out.println("Leyendo Data");
			    					valido = processDataContent(dataToProcess); //!
			    				}							    			
				    		}
				    		else
				    		{
				    			//error inesperado !!!!!!
				    			System.out.println("ERROR INESPERADO");
				    		}			    		
				    	}			    	
				    }
			    }				    				
//			} catch(IOException e)
//			{
//				System.out.print("+");
//				e.printStackTrace();
//			}		    
		}
		System.out.println("Terminando run");
		
		//closeConnection(); //terminar
		
		//RESPONDER 220!!!!!!!!!!!!!!!!!!!??????????????????????		
		
		//iterar el request recibido para revisar protocolo		
		
		//una idea es....
		//while todo esos no esten true
		//hacer el for desde el inicio
		//marcar variables, sifaltaba una seguir y obtenr todas en orden 
		
		//creo que no ya que el SMTP request es "efimero" en el sentido que caa vez que 
		//el SMTP User Agent hace un request, sea una linea o sea todo el request
		//y lo procesa independientemente
		//las conexiones tienen que tener las banderas globales
		//para que mientras exista la conexion se tenga "memoria" de lo ya parseado
		
	}	//ojo
	
	public void readData()
	{
		try {
			InputStreamReader input = new InputStreamReader(this.conexion.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			this.input = reader;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//input new inputstreamreader this.socket.getinputstream
	//buffreader reader = new bufferedreader input
	//this,input = reader
	
	public void createAndSendMail()
	{		
		//for Mail m : MAILS
		//m.setData(dataDeCorreo)
		//otros datos
		//fecha
		//enciar
		Date fechaRecibido = new Date();
		
		for(Mail m : MAILSPROPIOS)
		{
			//setear data
			m.setBody(dataDeCorreo);			
			//setear fecha
			m.setDateReceived(fechaRecibido.toString());
			//almacenar en BD
			m.save();
		}
		
		//ahora setear data para los mails ajenos
		for(Mail m : MAILSAJENOS)
		{
			//setear data
			m.setBody(dataDeCorreo);
			//setear fecha
			m.setDateReceived(fechaRecibido.toString());			
		}
	}
	
	public boolean processDataContent(String data)
	{
		//leer y agregar a la data
		try{
			dataDeCorreo += data;
			return true;
		} catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}		
	}
	
	public boolean processData(String data)
	{
		String response = "550 - ¡Error en la Data! \n";
		boolean temp;			
		
		temp = Pattern.matches("(DATA)", data);
		
		if(temp)
		{		
			sendResponse("354 - Inicie su Correo, terminelo con <CRLF>.<CRLF>: \n");
			dataOK = true;
			return true;
		}
		else
		{
			//error
			sendResponse(response);
			return false;
		}		
	}
	
	public boolean processRcptTo(String data)
	{
		String response = "550 - ¡No existe tal usuario aca \n!";
		boolean temp;			
		
		temp = Pattern.matches("(RCPT)\\s+(TO:?)\\s+(.)+", data);
		
		if(temp)
		{
			//extraer el correo del recipiente
			String[] correoRecipiente = data.split("\\s+");
			//el tercer elemento deberia ser el correo que envia
			String nombreRecipiente = correoRecipiente[2];
			
			//si coincide con formato, extraer usuario y dominio
			String usuario, dominio;
			String[] formatoTemporal = nombreRecipiente.split("@");
			usuario = formatoTemporal[0];
			dominio = formatoTemporal[1];
			System.out.println(usuario+" @ "+dominio);
			
			//dominio debe ser nuestro o sea @LabSMTP
			if(dominio.equals("LabSMTP"))
			{
				//es nuestro
				System.out.println("Correo Local");
				//verificar si existe en BD
				
				Connector.connect();		
				DBManager dbm = new DBManager();
				User usuarioVerificar = dbm.existUser(usuario);
				
				
				if(usuarioVerificar != null)
				{
					usuariosPropios.add(nombreRecipiente);
					Connector.close();
					rcptOK = true; //validar que ya se tiene un usuario ok
					
					//agregar un nuevo mail a la lista de mails por enviar
					Mail nuevoMail = new Mail();
					
					User sender = new User(usuarioEmisor+"@"+dominioEmisor);
					
					nuevoMail.setFrom(sender);					
					nuevoMail.setTo(usuarioVerificar);
					
					MAILSPROPIOS.add(nuevoMail);					
					
					sendResponse("250 - Ok \n");
					
					return true;
				}
				else
				{
					//error ya que no existe el usuario
					sendResponse(response);	
					Connector.close();
					return false;
				}					
			}
			else
			{
				//no es nuestro
				System.out.println("Correo ajeno");
				//almacenar para reenvio
				usuariosAjenos.add(nombreRecipiente);
				
				Mail nuevoMail = new Mail();
				
				User sender = new User(usuarioEmisor+"@"+dominioEmisor);
				
				nuevoMail.setFrom(sender);
				nuevoMail.setTo(new User(nombreRecipiente));
				
				MAILSAJENOS.add(nuevoMail);
				
				rcptOK = true; //validar que ya se tiene un usuario ok aunque sea ajeno
				sendResponse("250 - Ok \n");
				return true;
			}							
		}
		else
		{
			//error ya que no tiene le formato adecuado
			sendResponse("402 - ¡Correo malformado! \n");				
			return false;
		}		
	}
	
	public boolean processMailFrom(String data)
	{
		String response = "401 - ¡Origen Invalido! \n";
		boolean temp;		
		temp = Pattern.matches("(MAIL)\\s+(FROM:?)\\s+(.)+", data);
		
		if(temp)
		{
			String[] correoUsuario = data.split("\\s+");
			//el tercer elemento deberia ser el correo que envia
			String nombreAutor = correoUsuario[2];
			//verificar cumpla con usuario@dominio
			temp = Pattern.matches("(.)+@(.)+", nombreAutor);
			
			if(temp)
			{
				//si coincide con formato, extraer usuario y dominio
				//String usuario, dominio;
				String[] formatoTemporal = nombreAutor.split("@");
				usuarioEmisor = formatoTemporal[0];
				dominioEmisor = formatoTemporal[1];
				System.out.println(usuarioEmisor+" @ "+dominioEmisor);
				
				/*--------VERIFICAR DOMINIO VALIDO------------------!*/
				//DNS verify¡¡
								
				sendResponse("250 - Ok \n");					
				fromOK = true;
				return true;
			}
			else
			{
				//error en formato de correo
				sendResponse("402 - ¡Correo de Origen malformado! \n");				
				return false;
			}			
		}
		else
		{
			//error
			sendResponse(response);
			return false;
		}
	}
		
	
	public boolean processHelo(String data)
	{
		String response = "400 - ¡Las personas educadas saludan primero! \n";
		boolean temp;
		temp = Pattern.matches("(HELO|EHLO)(\\s)+(\\w)+", data);
		
		if(temp)
		{
			//extraer dominio
			String[] correoUsuario = data.split("\\s+");
			//extraer el segundo elemento
			
			if(correoUsuario.length > 1)
			{
				String nombreDominio = correoUsuario[1]; //nombre usuario
				System.out.println(nombreDominio);
				sendResponse("220 - Saludos "+nombreDominio+", estamos a las ordenes...\n");
			}
			else
			{
				//error o poner comodin?
				String nombreDominio = "dummy";
				System.out.println(nombreDominio);
				sendResponse("220 - Saludos anonimo, estamos a las ordenes...\n");
			}			
			heloOK = true;	
			return true;
		}	
		else
		{
			//error
			sendResponse(response);
			return false;
		}		
	}
	
	public void closeConnection()
	{
		try {
			input.close();
			output.close();
			conexion.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendResponse(String resp)
	{
		try {
//			byte[] b = resp.getBytes();
//			output.write(b);			
			output.writeBytes(resp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Cagada: "+e);
			closeConnection();
		}
	}	
}