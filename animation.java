import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.*;
import java.io.File;

public class animation{
	//Properties
    JFrame theFrame = new JFrame("PENALTY!");
	
	int intFPS = 60;
	
	String keeperName = "Gianluigi DONNARUMA";
	int keeperFileValue;
		
	String strikerName = "Erling HAALAND";
	int strikerFileValue;
	
    int intBallX = 610;
    int intBallY = 550;
    
	BufferedImage imgGoalie = null;
	BufferedImage imgStriker = null;
	BufferedImage imgBall = null;
	BufferedImage imgGoal = null;
	BufferedImage imgBG = null;
			
	//Methods	
	//Getting keeper values and loading in image
	public void setKeeperFileValue() {
        if (keeperName.equalsIgnoreCase("Gianluigi DONNARUMA")) {
            keeperFileValue = 1;
        } else if (keeperName.equalsIgnoreCase("David DE GEA")) {
            keeperFileValue = 2;
        } else if (keeperName.equalsIgnoreCase("James Trafford")) {
            keeperFileValue = 3;
        }
    }

    public void loadGoalieImage() {
        try {
            setKeeperFileValue();
            imgGoalie = ImageIO.read(new File("images/keepers/K" + keeperFileValue + "Stand.png"));
        } catch (IOException e) {
            System.out.println("goalie image error");
        }
    }
    
    //Getting striker value and loading in images
    public void setStrikerFileValue(){
		if(strikerName.equalsIgnoreCase("Erling HAALAND")){
			strikerFileValue = 1;
		}else if(strikerName.equalsIgnoreCase("Cristiano RONALDO")){
			strikerFileValue = 2;
		}else if(strikerName.equalsIgnoreCase("Kylian MBAPPE")){
			strikerFileValue = 3;
		}
	}
	
	public void loadStrikerImage() {
        try {
            setStrikerFileValue();
            imgStriker = ImageIO.read(new File("images/Strikers/S" + strikerFileValue + "Stand.gif"));
            System.out.println(imgStriker);
        } catch (IOException e) {
            System.out.println("striker image error");
        }
    }
	
	//Loading in images
	public void loadImages(){
		try{
			setKeeperFileValue();
			setStrikerFileValue();
			
			imgBG = ImageIO.read(new File("images/shootingBG.jpeg"));
			imgGoal = ImageIO.read(new File("images/Goal.png"));
			imgBall = ImageIO.read(new File("images/ball.png"));
		}catch(IOException e){
			System.out.println("image error");
			System.out.println(e);
		}
	}
	
	//Drawing the panel
	JPanel thePanel = new JPanel(){
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			// Draw background full screen
			if(imgBG != null){
				g.drawImage(imgBG, 0, 0, 1280, 720, null);
			}else{
				g.setColor(new Color(40, 140, 70));
				g.fillRect(0, 0, 1280, 720);
			}
			
			// Draw goal - centered horizontally in background backdrop
			if(imgGoal != null){	
				g.drawImage(imgGoal, 130, 160, theFrame);
			}
			
			// Draw keeper centered standing directly in front of the goal line
			if (imgGoalie != null) {
				g.drawImage(imgGoalie, 450, 340, theFrame);
			}
			
			// Draw ball positioned closer to the foreground penalty marker spot
			if(imgBall != null){	
				g.drawImage(imgBall, intBallX, intBallY, theFrame);
			}
			
			// Draw striker placed to the left or offset right behind the ball
			if(imgStriker != null){
				g.drawImage(imgStriker, 480, 410, theFrame);
			}
		}
	};
	
	//Constructor
	public animation(){	
		//Start window
		super();
		loadImages();
		loadGoalieImage();
		loadStrikerImage();
		
		theFrame.setLayout(null);
		theFrame.setPreferredSize(new Dimension(1280, 720)); 
		
		thePanel.setLayout(null);
		thePanel.setPreferredSize(new Dimension(1280, 720));
		
		//Finish window
		theFrame.setContentPane(thePanel);
		theFrame.setSize(1280, 720);
		theFrame.setDefaultCloseOperation(3);
		theFrame.pack();
		theFrame.setResizable(false);
		theFrame.setVisible(true);
	}
	
	
	//Main method
	public static void main(String[] args){			
		new animation();
	}
}
	
