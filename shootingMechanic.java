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
    // --- JLABEL PROPERTIES ---
    JLabel lblLeftRight = new JLabel("Left / Right");
    JLabel lblUpDown = new JLabel("Up / Down");
    JLabel lblPower = new JLabel("Power");
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
    // Left/Right line moves horizontally between X: 1020 and 1260
    int intLeftRightLineX = 1140; 
    // Up/Down line moves vertically between Y: 230 and 470
    int intUpDownLineY = 350;    
    // Power line moves horizontally between X: 1020 and 1260
	int intPowerLineX = 1140;     

    // --- DEFINED ANIMATION SPEED VARIABLES ---
    int intLeftRightSpeed = 4;
    int intUpDownSpeed = 5;
    int intPowerSpeed = 6;

    //images
    BufferedImage imgBG = null;
    BufferedImage imgGoal = null;
    BufferedImage imgBall = null;

    //methods
    public void loadCSV(){

    }

    public void actionPerformed(ActionEvent evt){
		// Call the move method every time the timer ticks (60 times a second)
        moveSliders();
        // Force the screen to refresh and redraw the white bars at their new positions
        repaint();
    }

    public void keyPressed(KeyEvent evt){

    }

    public void keyReleased(KeyEvent evt){

    }

    public void keyTyped(KeyEvent evt){

    }
	public void drawMeters(Graphics g) {
		//Method to draw the 3 slider
        // Base background for the meter
        Color darkTrackBg = new Color(30, 30, 35);
		// METER 1: LEFT / RIGHT
        g.setColor(Color.BLACK);
        g.fillRect(1016, 136, 248, 48);
        g.setColor(darkTrackBg);
        g.fillRect(1020, 140, 240, 40);   
        g.setColor(new Color(0, 180, 216)); 
        g.fillRect(1020, 140, 240, 4);
        g.fillRect(1020, 176, 240, 4);
        g.fillRect(1020, 140, 4, 40);
        g.fillRect(1256, 140, 4, 40);
        g.setColor(Color.WHITE); 
        g.fillRect(intLeftRightLineX - 2, 140, 4, 40); 
        // METER 2: UP / DOWN 
        g.setColor(Color.BLACK);
        g.fillRect(1116, 226, 48, 248);
        g.setColor(darkTrackBg);
        g.fillRect(1120, 230, 40, 240);  
        g.setColor(new Color(247, 127, 0)); 
        g.fillRect(1120, 230, 40, 4);
        g.fillRect(1120, 466, 40, 4);
        g.fillRect(1120, 230, 4, 240);
        g.fillRect(1156, 230, 4, 240);
        g.setColor(Color.WHITE); 
        g.fillRect(1120, intUpDownLineY - 2, 40, 4); 
		// METER 3: POWER (Horizontal, Dynamic Segmented Blocks)
        int intPowerX = 1020;
        int intPowerY = 520;
        int intPowerWidth = 240;
        int intPowerHeight = 40;
        // Draw the background behind blocks
        g.setColor(Color.BLACK);
        g.fillRect(intPowerX - 4, intPowerY - 4, intPowerWidth + 8, intPowerHeight + 8);
        g.setColor(darkTrackBg);
        g.fillRect(intPowerX, intPowerY, intPowerWidth, intPowerHeight);
        // Professional color-gradient segmented blocks (12 segments total)
        int intSegments = 12;
        int intSegmentWidth = intPowerWidth / intSegments;
        for (int i = 0; i < intSegments; i++) {
            // Calculate how far along we are using dblProgress (0.0 at start, 1.0 at end)
            double dblProgress = (double) i / (intSegments - 1);
            // Linear interpolation using dblProgress: Fade from Green to Red
            int intRed = (int) (0 + dblProgress * (255 - 0));
            int intGreen = (int) (255 + dblProgress * (0 - 255));
            g.setColor(new Color(intRed, intGreen, 30));
            // 3. Draw each individual color block perfectly spaced
            g.fillRect(intPowerX + (i * intSegmentWidth) + 2, intPowerY + 2, intSegmentWidth - 4, intPowerHeight - 4);
        }
        // Heavy dark framework border to lock it in
        g.setColor(Color.BLACK);
        g.fillRect(intPowerX, intPowerY, intPowerWidth, 4);
        g.fillRect(intPowerX, intPowerY + intPowerHeight - 4, intPowerWidth, 4);
        g.fillRect(intPowerX, intPowerY, 4, intPowerHeight);
        g.fillRect(intPowerX + intPowerWidth - 4, intPowerY, 4, intPowerHeight);
        // Universal Crisp White Indicator over the colors
        g.setColor(Color.WHITE);
        g.fillRect(intPowerLineX - 2, intPowerY, 4, intPowerHeight); 
    }
    public void moveSliders() {
        //Move and Bounce Meter 1: Left/Right
        intLeftRightLineX += intLeftRightSpeed;
        if (intLeftRightLineX <= 1020 || intLeftRightLineX >= 1260) {
            intLeftRightSpeed = -intLeftRightSpeed; // Reverse direction
        }

        //Move and Bounce Meter 2: Up/Down 
        intUpDownLineY += intUpDownSpeed;
        if (intUpDownLineY <= 230 || intUpDownLineY >= 470) {
            intUpDownSpeed = -intUpDownSpeed; // Reverse direction
        }

        //Move and Bounce Meter 3: Power 
        intPowerLineX += intPowerSpeed;
        if (intPowerLineX <= 1020 || intPowerLineX >= 1260) {
            intPowerSpeed = -intPowerSpeed; // Reverse direction
        }
    }
    public void paintComponent(Graphics g){
		super.paintComponent(g);
        //draw BG and goal
        g.drawImage(imgBG, 0, 0, theFrame);
        g.drawImage(imgGoal, 130, 160, theFrame);
        g.drawImage(imgBall,intBallX,intBallY,theFrame);
        drawMeters(g);
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

        // SETUP LABELS MANUALLY
        Font labelFont = new Font("Arial Black", Font.PLAIN, 18);
        int labelWidth = 200;
        int labelHeight = 30;

        lblLeftRight.setBounds(1020, 105, labelWidth, labelHeight);
        lblLeftRight.setForeground(Color.WHITE); 
        lblLeftRight.setFont(labelFont);
        this.add(lblLeftRight); 

        lblUpDown.setBounds(1085, 195, labelWidth, labelHeight);
        lblUpDown.setForeground(Color.WHITE);
        lblUpDown.setFont(labelFont);
        this.add(lblUpDown);

        lblPower.setBounds(1020, 485, labelWidth, labelHeight);
        lblPower.setForeground(Color.WHITE);
        lblPower.setFont(labelFont);
        this.add(lblPower);

        //start timer
        theTimer.start();

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
