package smtpserver.src.parser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

//definicion de la cola de requests
public class MyQueue<E> implements baseQueue {
	
	//la lista
	private Queue<E> queue = new LinkedList<E>();
	private final int MAX_REQUESTS = 10;
	
	@Override
	public synchronized void enqueue(Object e) {
		if(queue.size() < MAX_REQUESTS)
		{
			queue.add((E) e); //cast a lo "coche"
			//notificar a threads
			notifyAll();
		}
		else{
			//Queue llena
			//cerrar conexion
			HttpRequest r = (HttpRequest) e;
			System.out.println("Conexion Rechazada");
			
			try {
				r.conexion.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		
	}
	
	@Override
	public synchronized E dequeue() {
		
		E e = null;
		
		//mientras no hay requests esperar
		while(queue.isEmpty())
		{
			try{
				wait();
			} catch(InterruptedException e1)
			{
				e1.printStackTrace();
			}
		}
		e = queue.remove(); //extrae al primero de la queue
				
		return e;
	}
}
