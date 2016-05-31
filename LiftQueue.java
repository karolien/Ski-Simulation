//Karolien Koorts


import java.util.concurrent.*;
import java.util.*;

public class LiftQueue{
	ArrayBlockingQueue<Skier> liftQueue;
	LinkedBlockingQueue waitQueue;
	int liftSpeed;
	double probStopping;
	int seats;
	
	public LiftQueue(LinkedBlockingQueue waitQueue, int liftSpeed, int seats, double probStopping){
		this.seats = seats;
		this.liftQueue = new ArrayBlockingQueue<Skier>(seats);
		for (int i = 0; i<seats;i++){		//fill liftQueue with empty seats
			try {
				this.liftQueue.put(new Skier());
			} catch (InterruptedException e) {	}
		}
		this.waitQueue = waitQueue;
		this.liftSpeed = liftSpeed*10;	//from seconds to milliseconds
		this.probStopping = probStopping;
	}
	
	public int skiersOnLift(){
		int skiers = 0;
		Iterator iter = this.liftQueue.iterator();
		while(iter.hasNext()){
			Skier nextSkier = (Skier) iter.next();
			if (nextSkier.label == 0){
				skiers ++;
			}
		}
		return (this.liftQueue.size() - skiers);
	}

}
