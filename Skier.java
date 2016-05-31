//Karolien Koorts


import java.util.concurrent.*;

public class Skier implements Runnable{
	private int skiSpeed;
	private LinkedBlockingQueue waitQueue;
	static int count = 0;
	int label;
	
	public Skier(LinkedBlockingQueue waitQueue, int maxRuntime){
		skiSpeed = ThreadLocalRandom.current().nextInt(2000, maxRuntime + 1);
		this.waitQueue = waitQueue;
		count++;
		this.label = count;
	}
	
	public Skier(){
		this.label = 0;
	}
	@Override
	public void run() {
		try{
			Thread.sleep(skiSpeed);
		}
		catch(Exception e){
			
		}
		finally{
			waitQueue.add(this);
		}
	}
	
	public String toString(){
		if (label == 0){
			return "EMPTY";
		}
		return Integer.toString(label);
	}
	

}
