package parser;

public class ThreadPoolManager {
	
	private final int MAX_THREADS;
	private MyQueue<HttpRequest> queue = new MyQueue<HttpRequest>();
	
	public ThreadPoolManager(int capacity)
	{
		this.MAX_THREADS = capacity; //establecer max threads
		initAllConsumers(); //inicializar threads
	}
	
	private void initAllConsumers()
	{
		for(Integer i = 0; i < MAX_THREADS; i++)
		{
			//iniciar nuevo thread
			//utilizar Runnable pro para poder tener Threads "crudos"
			//para agregar el HttpRequest luego (on demand)
			Thread thread = new Thread(new SwimmingThread(queue, i.toString()));
			thread.start();
		}
	}
	
	public void submitTask(Runnable r)
	{
		queue.enqueue(r);
	}

}
