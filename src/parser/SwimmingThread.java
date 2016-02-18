package parser;

public class SwimmingThread implements Runnable{
	
	private MyQueue<SmtpRequest> myQueue;
	private String name;
	
	/*
	public SwimmingThread(MyQueue<HttpRequest> queue, String name)
	{
		this.myQueue = queue;
		this.name = name;
	}*/
	
	public SwimmingThread(MyQueue<SmtpRequest> queue, String name) {
		// TODO Auto-generated constructor stub
		this.myQueue = queue;
		this.name = name;
	}

	@Override
	public void run()
	{
		while(true)
		{
			Runnable r = myQueue.dequeue();
			if(r != null)
			{
				System.out.println("Thread designado: " +this.name);		
				r.run();
				System.out.println("Thread finalizado: " +this.name);	
			}					
		}
	}

}
