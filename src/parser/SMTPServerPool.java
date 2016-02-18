package parser;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

import data.Connector;
import data.DBManager;
import data.Mail;
import data.User;


public class SMTPServerPool {
	
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
		
		System.out.println("Iniciando Web Server - Protocolo TCP/SMTP");
		System.out.println("Puerto: "+PUERTO);
		ServerSocket s = new ServerSocket(PUERTO); //inicializa un nuevo socket
		ThreadPoolManager pool = new ThreadPoolManager(CAPACIDAD);
		System.out.println("Capaidad maxima: "+CAPACIDAD+"\n");
		try
		{
			while(true)
			{	
				//System.out.println("Esperando conexion...");			
				Socket conexion = s.accept(); //espera a una peticion del cliente			
				System.out.println(">Conexion Entrante..!");	
				
				SmtpRequest request = new SmtpRequest(conexion);
				
				pool.submitTask(request);
				
				//basado en lab de multithreaded
				
			}	
		}finally
		{
			s.close();
		}		
	}
}

final class HttpRequest implements Runnable{

	public String recibido;
	public String enviado;
	public Socket conexion;
	public BufferedReader input;
	public DataOutputStream output;
	
	public HttpRequest(){}
	
	public HttpRequest(Socket conexion) throws IOException
	{
		recibido = "";
		enviado = "";
		this.conexion = conexion;
		input = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		output = new DataOutputStream(conexion.getOutputStream());
	}
	
	@Override
	public void run() {		
			
		//obtener data entrante en inicializacion	
		
		//esperar 7 segundos para probar multithread
//		try {
//			Thread.sleep(7000);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		//parsear y obtener datos (primera linea)
		try {
			
			//iterar y leer todo el HTTP request
			
			String fragmento = input.readLine();
			while(!fragmento.isEmpty())
			{
				recibido += fragmento+"\n";
				try{
					fragmento = input.readLine();						
				}catch(Exception e)
				{
					e.printStackTrace();
				}							
			}
			
			//System.out.println("Request: \n"+recibido);
			
			StringTokenizer tokenizer = new StringTokenizer(recibido, "\n");
			String encabezado = tokenizer.nextToken();
			String[] request = encabezado.split(" ");
			String metodo = request[0];
			String recurso = "N/A";
			if(encabezado.length() > 1)
			{
				recurso = request[1];
			}			
			
			System.out.println("Metodo Recibido: "+metodo);
			System.out.println("Recurso Solicitado: "+recurso);
			
			switch(metodo){
				case "GET":
					//revisar el recurso
					//Retornar 200 o bien 404
					if(recurso.contains("index.html")){
						//Ok
						Date fecha = new Date();
						enviado = "HTTP/1.1 200 OK\n"
								+ "Connection close\n"
								+ "Date: "+fecha.toString()+"\n"
								+ "Server: Troncomovil/1.2.3 (Unix)\n"
								+ "Last-Modified: FRI, 22 Jan 2016 12:00:01 GMT\n"
								+ "Content-Length: 5555\n"
								+ "Content-Type: text/html \n\n"
								+ "<HTML><head><title>Index - Lab 2</title></head><body>Saludos</body></HTML>";
						
						output.writeBytes(enviado); //escribe en output 
					}
					else{
						//404						
						Date fecha = new Date();
						enviado = "HTTP/1.1 404 Not Found\n"
								+ "Connection close\n"
								+ "Date: "+fecha.toString()+"\n"
								+ "Server: Troncomovil/1.2.3 (Unix)\n"
								+ "Last-Modified: FRI, 22 Jan 2016 12:00:01 GMT\n"
								+ "Content-Length: 5555\n"
								+ "Content-Type: text/html\n\n"
								+ "<HTML><head><title>Oops!</title></head><body>Parece que no existe el Recurso...</body></HTML>";
						
						output.writeBytes(enviado); //escribe en output
					}	
					
					conexion.close();
					break;
					
				case "HEAD":
					//retornar el Head del servidor				
					Date fecha = new Date();
					enviado = "HTTP/1.1 200 Ok\n"
							+ "Connection close\n"
							+ "Date: "+fecha.toString()+"\n"
							+ "Server: Troncomovil/1.2.3 (Unix)\n"
							+ "Last-Modified: FRI, 22 Jan 2016 12:00:01 GMT\n"
							+ "Content-Length: 5555\n"
							+ "Content-Type: text/html\n\n";
					
					output.writeBytes(enviado); //escribe en output
					conexion.close();
					break;	
					
//				case "POST":
//					break;					
			
				default:
					conexion.close();
					break;
			}
					
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
	}	
}