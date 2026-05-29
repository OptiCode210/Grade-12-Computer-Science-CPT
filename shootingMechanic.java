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

        // --- NEW SLIDER LINE VARIABLES ---
        // Left/Right line moves horizontally between X: 1020 and 1260
        int intLeftRightLineX = 1140; 
        
        // Up/Down line moves vertically between Y: 230 and 470
        int intUpDownLineY = 350;     
        
        // Power line moves horizontally between X: 1020 and 1260
        int intPowerLineX = 1140;     

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
	public void drawMeters(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(4f));

        // Smooth rendering hints for a professional look
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Base sleek background for empty slots (transparent dark charcoal)
        Color darkTrackBg = new Color(30, 30, 35, 220);

		// --- METER 1: LEFT / RIGHT (Horizontal, Muted Cyan Accent) ---
        g2d.setColor(darkTrackBg);
        g2d.fillRect(1020, 140, 240, 40);   
        
        g2d.setColor(new Color(0, 180, 216)); // Sleek Cyan Accent Border
        g2d.drawRect(1020, 140, 240, 40);
        
        g2d.setColor(Color.WHITE); // Uses variable coordinate
        g2d.drawLine(intLeftRightLineX, 140, intLeftRightLineX, 180); 


        // --- METER 2: UP / DOWN (Vertical, Muted Orange/Gold Accent) ---
        g2d.setColor(darkTrackBg);
        g2d.fillRect(1120, 230, 40, 240);  
        
        g2d.setColor(new Color(247, 127, 0)); // Sleek Orange Accent Border
        g2d.drawRect(1120, 230, 40, 240);
        
        g2d.setColor(Color.WHITE); // Uses variable coordinate
        g2d.drawLine(1120, intUpDownLineY, 1160, intUpDownLineY); 


        // --- METER 3: POWER (Horizontal, Dynamic Segmented Blocks) ---
        int powerX = 1020;
        int powerY = 520;
        int powerWidth = 240;
        int powerHeight = 40;
        
        // Draw the background behind blocks
        g2d.setColor(darkTrackBg);
        g2d.fillRect(powerX, powerY, powerWidth, powerHeight);

        // Professional color-gradient segmented blocks (12 segments total)
        int segments = 12;
        int segmentWidth = powerWidth / segments;
        
        for (int i = 0; i < segments; i++) {
            // Calculate a smooth color transition from Green (0) -> Yellow (6) -> Red (11)
            int red = (int) (255 * Math.min(1.0, (i * 2.0) / segments));
            int green = (int) (255 * Math.min(1.0, 2.0 * (segments - i) / segments));
            g2d.setColor(new Color(red, green, 30));
            
            // Draw each individual color block slightly inset for a modern segmented grid look
            g2d.fillRect(powerX + (i * segmentWidth) + 2, powerY + 2, segmentWidth - 4, powerHeight - 4);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawRect(powerX, powerY, powerWidth, powerHeight);
        
        g2d.setColor(Color.WHITE);
        g2d.drawLine(intPowerLineX, powerY, intPowerLineX, powerY + powerHeight); 
        
        g2d.setStroke(oldStroke);
    }
    public void paintComponent(Graphics g){
		super.paintComponent(g);
        //draw BG and goal
        //if(imgBall != null){
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

        // --- SETUP LABELS MANUALLY ---
        Font labelFont = new Font("Arial Black", Font.PLAIN, 18);
        int labelWidth = 200;
        int labelHeight = 30;

        lblLeftRight.setBounds(1020, 105, labelWidth, labelHeight);
        lblLeftRight.setForeground(Color.WHITE); 
        lblLeftRight.setFont(labelFont);
        this.add(lblLeftRight); 

        // Shifted layout variables to frame the tall block beautifully
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
