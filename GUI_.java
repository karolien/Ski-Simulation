//Karolien Koorts



//Use this class to test program without GUI interaction. 
//The initial values determine what is run without user input

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.*;

import javax.swing.*;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.PUBLIC_MEMBER;

public class GUI_ extends JFrame {
    
    int liftSeats, liftSpeed, noSkiers, maxRuntime;
    double probStopping;
  //~~~~~~~~~~~~~~~~~~CHANGE THESE INITIAL VALUES IF YOU WISH TO TEST WITHOUT GUI INTERACTION
    String liftSeatsS = "10", liftSpeedS = "1000", noSkiersS = "30", maxRuntimeS = "12000", probStoppingS = "0.05";
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  
    
    SkiSimulate skiSim;
    AnimationPanel picture;
    Thread thread;
    Container cp = getContentPane();
    
	public GUI_() {
        setTitle("Skifield Simulation");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        final JTextArea textArea = new JTextArea();
        final JButton initialiseButton = new JButton("Initialise Ski Field");
        initialiseButton.setToolTipText("Click to save entered values");
        final JButton startButton = new JButton("Start");
        startButton.setToolTipText("Click to start new simulation");
        final JButton stopButton = new JButton("Stop");
        stopButton.setToolTipText("Click to end simulation");


        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 600));
        cp.add(scrollPane, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        JPanel topPanel = new JPanel();

        picture = new AnimationPanel();
        picture.setPreferredSize(new Dimension(50000, 50000));
        JScrollPane scrollPane2 = new JScrollPane(picture);
        scrollPane2.setPreferredSize(new Dimension(600, 600));
        cp.add(scrollPane2, BorderLayout.WEST);
        
  
        final JTextField liftSeatEntry = new JTextField(liftSeatsS);
        liftSeatEntry.setToolTipText("Must be an integer");
        
        final JTextField liftSpeedEntry = new JTextField(liftSpeedS);
        liftSpeedEntry.setToolTipText("Must be an integer");
        
        final JTextField noSkiersEntry = new JTextField(noSkiersS);
        noSkiersEntry.setToolTipText("Must be an integer");
        
        final JTextField maxRunTimeEntry = new JTextField(maxRuntimeS);
        maxRunTimeEntry.setToolTipText("Must be an integer > 2000");
        
        final JTextField probStoppingEntry = new JTextField(probStoppingS);
        probStoppingEntry.setToolTipText("Must be an double between 0 and 1");
        
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(new JLabel(" Seats on Lift: ", JLabel.RIGHT));
        topPanel.add(liftSeatEntry);
        topPanel.add(new JLabel(" Lift Speed: ", JLabel.RIGHT));
        topPanel.add(liftSpeedEntry);
        topPanel.add(new JLabel(" No. of Skiers: ", JLabel.RIGHT));
        topPanel.add(noSkiersEntry);
        topPanel.add(new JLabel(" Max Runtime: ", JLabel.RIGHT));
        topPanel.add(maxRunTimeEntry);
        topPanel.add(new JLabel(" Lift Stopping Probability: ", JLabel.RIGHT));
        topPanel.add(probStoppingEntry);
        topPanel.add(initialiseButton);
        
        //~~~~~~~~~~~~~~~~~~~~~~~INITIALISE BUTTON~~~~~~~~~~~~~~
        initialiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	try{
            		liftSeats = Integer.parseInt(liftSeatEntry.getText());
            		liftSeatsS = liftSeatEntry.getText();
            		liftSeatEntry.setBackground(Color.white);
            	}catch(Exception ex){
            		liftSeatEntry.setBackground(Color.red);
            	}
            	try{
            		liftSpeed = Integer.parseInt(liftSpeedEntry.getText());
            		liftSpeedS = liftSpeedEntry.getText();
            		liftSpeedEntry.setBackground(Color.white);
            	}catch(Exception ex){
            		liftSpeedEntry.setBackground(Color.red);
            	}
            	try{
            		noSkiers = Integer.parseInt(noSkiersEntry.getText());
            		noSkiersS = noSkiersEntry.getText();
            		noSkiersEntry.setBackground(Color.white);
            	}catch(Exception ex){
            		noSkiersEntry.setBackground(Color.red);
            	}
            	try{
            		maxRuntime = Integer.parseInt(maxRunTimeEntry.getText());            		
            		if (maxRuntime > 2000){
            			maxRuntimeS = maxRunTimeEntry.getText();
            			maxRunTimeEntry.setBackground(Color.white);
            		}
            		else{
            			maxRunTimeEntry.setBackground(Color.red);
            		}
            	}catch(Exception ex){
            		maxRunTimeEntry.setBackground(Color.red);
            	}
            	try{
            		probStopping = Double.parseDouble(probStoppingEntry.getText());
            		if (probStopping >=0 && probStopping <= 1){
            			probStoppingS = probStoppingEntry.getText();
            			probStoppingEntry.setBackground(Color.white);
            		}
            		else{
            			probStoppingEntry.setBackground(Color.red);
            		}
            	}catch(Exception ex){
            		probStoppingEntry.setBackground(Color.red);
            	}
            }
        });
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        

        //~~~~~~~~~~~~~~~~~~~~~~START BUTTON~~~~~~~~~~~~~~
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	textArea.setText(null);
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                liftSeatEntry.setText(liftSeatsS); 
                liftSeatEntry.setBackground(Color.white);
                liftSpeedEntry.setText(liftSpeedS);
                liftSpeedEntry.setBackground(Color.white);
                noSkiersEntry.setText(noSkiersS);
                noSkiersEntry.setBackground(Color.white);
                maxRunTimeEntry.setText(maxRuntimeS);
                maxRunTimeEntry.setBackground(Color.white);
                probStoppingEntry.setText(probStoppingS);
                probStoppingEntry.setBackground(Color.white);
                skiSim = new SkiSimulate(textArea, startButton, picture, liftSeats, liftSpeed, noSkiers, maxRuntime, probStopping);
              
                picture.begin();
                thread = new Thread(picture);
                thread.start();
                skiSim.execute();
            }
        });
        bottomPanel.add(startButton);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        
        //~~~~~~~~~~~~~~~~~~~~~~STOP BUTTON~~~~~~~~~~~~~~
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                skiSim.cancel(true);
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                initialiseButton.setEnabled(true);
                picture.cancel();
            }
        });
        bottomPanel.add(stopButton);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        
        cp.add(topPanel, BorderLayout.NORTH);
        cp.add(bottomPanel, BorderLayout.SOUTH);

        pack();
        
        initialiseButton.doClick();
        startButton.doClick();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI_().setVisible(true);
            }
        });
    }
	
}
