//Karolien Koorts


import java.util.*;
import java.util.concurrent.*;

import javax.swing.*;

public class SkiSimulate extends SwingWorker<Void, String> {

    JTextArea textArea;
    JButton workerButton;
    int liftSeats;
    int liftSpeed;
    int noSkiers;
    int maxRuntime;
    double probStopping;
    AnimationPanel AP;
    int noSkiersOnLift;


    public SkiSimulate(JTextArea textArea, JButton workerButton, AnimationPanel AP, int liftSeats, int liftSpeed, int noSkiers, int maxRuntime, double probStopping) {
        this.textArea = textArea;
        this.workerButton = workerButton;
        this.liftSeats = liftSeats;
        this.liftSpeed = liftSpeed;
        this.noSkiers = noSkiers;
        this.maxRuntime = maxRuntime;
        this.probStopping = probStopping;
        this.AP = AP;
    }

    @Override
    public Void doInBackground() throws Exception {
    	
    	LinkedBlockingQueue waitQueue = new LinkedBlockingQueue();	// create and fill wait queue
    	for (int i=0; i<noSkiers;i++){
    		Skier toAdd = new Skier(waitQueue, maxRuntime);
    		waitQueue.add(toAdd);
    	}
    	
    	LiftQueue liftQueueObject = new LiftQueue(waitQueue, liftSpeed, liftSeats, probStopping);	//create lift queue
    	while(!isCancelled()){
    		
    		noSkiersOnLift = liftQueueObject.skiersOnLift();
    		
    		//ASCII OUTPUT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			textArea.append("On Lift (" + noSkiersOnLift + "): " + liftQueueObject.liftQueue + "\n");
			System.out.print("On Lift (" + noSkiersOnLift + "): " + liftQueueObject.liftQueue + "\n");
			textArea.append("In Queue (" + waitQueue.size() + "): " + waitQueue +"\n");
			System.out.println("In Queue (" + waitQueue.size() + "): " + waitQueue +"\n");
			textArea.append("\n");
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			
			//VISUAL OUTPUT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			AP.noSkiers = noSkiers;
			AP.liftSize = liftSeats;
			AP.waitQueue = waitQueue;
			AP.liftQueue = liftQueueObject.liftQueue;
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			Thread.sleep(liftSpeed);	//wait ___ milliseconds before repeating
			Skier nextInLine;
			try {
				Random random = new Random();
				if(random.nextDouble() < probStopping){	//does the lift stop?
					int stopTime = ThreadLocalRandom.current().nextInt(0, 8000 + 1);
					//ASCII OUTPUT~~~~~~~~~~~~~~~~~~~~~~~~~~~~
					textArea.append("Lift stops temporarily (for " + stopTime +" milliseconds).\n");
					System.out.print("Lift stops temporarily (for " + stopTime +" milliseconds).\n");
					
					Thread.sleep(stopTime);	//stop for random time up to 8s
					
					textArea.append("Lift continues operation.\n");
					System.out.println("Lift continues operation.\n");
					textArea.append("\n");
					//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				}
				Skier topOfLift = (Skier) liftQueueObject.liftQueue.peek();
				if(topOfLift.label !=0){	//if top of lift has Skier, let them run down the slope
					Thread skierThread = new Thread((Skier) liftQueueObject.liftQueue.take());
					skierThread.start();
					
				}
				else{
					liftQueueObject.liftQueue.take();	//otherwise, remove empty seat to make room in queue
				}
				if(waitQueue.isEmpty()){	//if wait queue is empty, add empty seat to lift
					liftQueueObject.liftQueue.put(new Skier());
				}
				else{
					nextInLine = (Skier) waitQueue.take();	//otherwise, put next in wait line onto lift
					liftQueueObject.liftQueue.put(nextInLine);
				}
				
			} catch (InterruptedException e) {
				
			}
		}
		return null;

    }
}
