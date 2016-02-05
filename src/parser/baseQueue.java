package smtpserver.src.parser;


//implementacion de cola

public interface baseQueue<E> {
	
	public void enqueue(E e);
	
	public E dequeue();

}
