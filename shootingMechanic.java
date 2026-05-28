import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.*;
import java.io.File;

public class shootingMechanic extends JPanel implements ActionListener, KeyListener{
    // Properties

        int intfps = 60;  

        //temp Jcomponents
        JFrame theFrame = new JFrame("trial");
        Timer theTimer = new Timer(1000/intfps, this);

        //variables
        String strikerName = "Striker Name";
        int strikerAccuracy = 9;
        int strikerPower = 2;

        String keeperName = "Keeper Name";
        int keeperAgility = 9;
        int keeperCoverage = 2;
        
        //Ball variables
        int intBallX = 610;
        int intBallY = 550;

        //images
        BufferedImage imgBG = null;
        BufferedImage imgGoal = null;
        BufferedImage imgBall = null;

    //methods
    public void loadCSV(){

    }

    public void actionPerformed(ActionEvent evt){

    }

    public void keyPressed(KeyEvent evt){

    }

    public void keyReleased(KeyEvent evt){

    }

    public void keyTyped(KeyEvent evt){

    }

    public void paintComponent(Graphics g){
		super.paintComponent(g);
        //draw BG and goal
            //if(imgBall != null){
            g.drawImage(imgBG, 0, 0, theFrame);
            g.drawImage(imgGoal, 130, 160, theFrame);
            g.drawImage(imgBall,intBallX,intBallY,theFrame);
    }

    public void loadIMG(){
        try{
            imgBG = ImageIO.read(new File("Images/shootingBG.jpeg"));
            imgGoal = ImageIO.read(new File("Images/Goal.png"));
            imgBall = ImageIO.read(new File("Images/ball.png"));
        }catch(IOException e){
            System.out.println("image error");
        }
    }


    // Constructor
    public shootingMechanic(){
        //start window (temp)
            this.setLayout(null);
            this.setPreferredSize(new Dimension(1280, 720));
            this.setFocusable(true);
            this.addKeyListener(this);

        //start timer
            theTimer.start();
			intBallX = 20;
			intBallY = 20;
			imgBall.setBounds(0,300,300,100);

        //repaint
            repaint();

        //load images
            loadIMG();


        //end window (temp)
            theFrame.setContentPane(this);
            theFrame.setDefaultCloseOperation(3);
            theFrame.pack();
            theFrame.setVisible(true);

    }

    // Main program
    public static void main(String[] args){
        new shootingMechanic();
    }
}
