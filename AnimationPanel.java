//Karolien Koorts


import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.event.*;

public class AnimationPanel extends JComponent implements Runnable {
	private Thread animationThread = null;	// the thread for animation
	int delay; 		// the current animation speed
	int noSkiersInwaitQueue;
	ArrayBlockingQueue<Skier> liftQueue;
	int noSkiers;
	int liftSize;
	boolean alive = true;
	int skiersOnSlope;
	int skiersOnLift;
	LinkedBlockingQueue waitQueue; 
    	int skiDotSize;

	public AnimationPanel() {
	}
	public void update(Graphics g){
		paint(g);
	}

	
	public void paintComponent(Graphics g) {
		if (noSkiers < 75 && liftSize < 75){	//changes size of dots depending on no skiers/lift size
			skiDotSize = 10;
		}
		else if (noSkiers <200 && liftSize <200){
			skiDotSize = 5;
		}
		else{
			skiDotSize = 3;
		}
			
	        g.setColor(Color.BLACK);    // set the drawing color
	        g.drawLine(4*skiDotSize, (skiDotSize*liftSize + skiDotSize), (skiDotSize*noSkiers + 4*skiDotSize), (skiDotSize*liftSize + skiDotSize));	//wait queue
	        g.drawString("Wait Queue" ,(skiDotSize*noSkiers)/2 , (skiDotSize*liftSize + 4*skiDotSize));
	        g.drawLine((skiDotSize*noSkiers + 4*skiDotSize),(skiDotSize*liftSize + skiDotSize),(skiDotSize*noSkiers + skiDotSize*4), skiDotSize);	//lift queue
	        g.drawString("Lift" ,(skiDotSize*noSkiers) + 7*skiDotSize , (skiDotSize*liftSize + skiDotSize)/2);
	        g.setColor(Color.RED); 
	
	        if (waitQueue!=null){	//animate wait queue
		        int counter = 1;
		        Iterator itera = waitQueue.iterator();
				while(itera.hasNext()){
					Skier nextSkier = (Skier) itera.next();
					g.fillOval((skiDotSize*noSkiers + 4*skiDotSize) - skiDotSize*counter, (skiDotSize*liftSize + skiDotSize), skiDotSize, skiDotSize);
					counter++;
				} 
	        }   
	        if (liftQueue!=null){	//animate lift queue
		        int counter = 0;
		        Iterator iter = liftQueue.iterator();
		        int skiersOnLift = 0;
				while(iter.hasNext()){
					Skier nextSkier = (Skier) iter.next();
					if (!(nextSkier.label == 0)){
						skiersOnLift++;
						g.fillOval((skiDotSize*noSkiers + skiDotSize*4), (skiDotSize + skiDotSize*counter), skiDotSize, skiDotSize);
					}
					counter++;
				} 
				
				//add dots for skiers on slope
		        skiersOnSlope = noSkiers - skiersOnLift - waitQueue.size();
		        for(int y = 0; y < skiersOnSlope;y++){
					g.fillOval(skiDotSize,(y+1)*skiDotSize, skiDotSize, skiDotSize);
				}
	        }   

	}

	
	/**	When the "start" button is pressed, start the thread
	 */
	public void start() {
		animationThread = new Thread(this);
		animationThread.start();
	}


	public void cancel(){
		alive = false;
		
	}
	public void begin(){
		alive = true;
	}
	
	/** run the animation
	 */
	public void run() {
		while(alive) {
			repaint();	
		}
	}

}
