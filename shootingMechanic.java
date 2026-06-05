import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.*;
import java.io.File;

public class shootingMechanic extends JPanel implements ActionListener, KeyListener {
    // Properties
    int intfps = 60;  
    // temp Jcomponents
    JFrame theFrame = new JFrame("Trial Shooting Mechanic");
    Timer theTimer = new Timer(1000 / intfps, this);
    
    // --- JLABEL PROPERTIES ---
    JLabel lblLeftRight = new JLabel("Left / Right");
    JLabel lblUpDown = new JLabel("Up / Down");
    JLabel lblPower = new JLabel("Power");
    
    // variables
    String strikerName = "Striker Name";
    int strikerAccuracy = 9;
    int strikerPower = 2;
  
    
    // --- FIXED BALL VARIABLES (Centered with the goal at X: 130) ---
    int intBallX = 610; 
    int intBallY = 550;
    
    // Left/Right line moves horizontally between X: 1020 and 1260
    int intLeftRightLineX = 1140; 
    // Up/Down line moves vertically between Y: 230 and 470
    int intUpDownLineY = 350;    
    // Power line moves horizontally between X: 1020 and 1260
    int intPowerLineX = 1140;     

    // ANIMATION SPEED VARIABLES
    int intLeftRightSpeed = 4;
    int intUpDownSpeed = 5;
    int intPowerSpeed = 6;
    
    // --- NEW MECHANICAL & CONVERSION TRACKERS ---
    int intStage = 1; 
    // 1 = Left/Right, 2 = Up/Down, 3 = Power, 4 = Shot Locked / Complete
    int intFinalLeftRightX = 0;
    int intFinalUpDownY = 0;
    int intFinalPowerX = 0;

    double dblFinalLeftRightPercent = 0.0;
    double dblFinalUpDownPercent = 0.0;
    double dblFinalPowerPercent = 0.0;

    // images
    BufferedImage imgBG = null;
    BufferedImage imgGoal = null;
    BufferedImage imgBall = null;
    
    // --- NEW SHOOTING ANIMATION VARIABLES ---
    boolean isShooting = false;
    double dblBallX = 610.0; 
    double dblBallY = 550.0;
    double dblTargetX = 0.0;
    double dblTargetY = 0.0;
    double dblShotStartX = 610.0;
    double dblShotStartY = 550.0;
    int intShotFrame = 0;
    int intShotTotalFrames = 45;

    // methods
    public void loadCSV() {
    }

    public void actionPerformed(ActionEvent evt) {
        // Move sliders only according to our current active stage
        if (intStage < 4) {
            moveSliders();
        }
        if (isShooting) {
            animateBall();
        }

        // Refresh the screen
        repaint();
    }

    public void keyPressed(KeyEvent evt) {
    }

    public void keyReleased(KeyEvent evt) {
        // Pass the key event data into your custom method
        spacebarinput(evt);
    }

    public void keyTyped(KeyEvent evt) {
    }

    public void drawMeters(Graphics g) {
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
        
        // METER 3: POWER
        int intPowerX = 1020;
        int intPowerY = 520;
        int intPowerWidth = 240;
        int intPowerHeight = 40;
        
        g.setColor(Color.BLACK);
        g.fillRect(intPowerX - 4, intPowerY - 4, intPowerWidth + 8, intPowerHeight + 8);
        g.setColor(darkTrackBg);
        g.fillRect(intPowerX, intPowerY, intPowerWidth, intPowerHeight);
        
        int intSegments = 12;
        int intSegmentWidth = intPowerWidth / intSegments;
        for (int i = 0; i < intSegments; i++) {
            double dblProgress = (double) i / (intSegments - 1);
            int intRed = (int) (0 + dblProgress * (255 - 0));
            int intGreen = (int) (255 + dblProgress * (0 - 255));
            g.setColor(new Color(intRed, intGreen, 30));
            g.fillRect(intPowerX + (i * intSegmentWidth) + 2, intPowerY + 2, intSegmentWidth - 4, intPowerHeight - 4);
        }
        
        g.setColor(Color.BLACK);
        g.fillRect(intPowerX, intPowerY, intPowerWidth, 4);
        g.fillRect(intPowerX, intPowerY + intPowerHeight - 4, intPowerWidth, 4);
        g.fillRect(intPowerX, intPowerY, 4, intPowerHeight);
        g.fillRect(intPowerX + intPowerWidth - 4, intPowerY, 4, intPowerHeight);
        
        g.setColor(Color.WHITE);
        g.fillRect(intPowerLineX - 2, intPowerY, 4, intPowerHeight); 
    }

    public void moveSliders() {
        // Stage 1 active: Move Left/Right bar
        if (intStage == 1) {
            intLeftRightLineX += intLeftRightSpeed;
            if (intLeftRightLineX <= 1020 || intLeftRightLineX >= 1260) {
                intLeftRightSpeed = -intLeftRightSpeed;
                if (intLeftRightLineX < 1020) intLeftRightLineX = 1020;
                if (intLeftRightLineX > 1260) intLeftRightLineX = 1260;
            }
        }
        // Stage 2 active: Move Up/Down bar
        else if (intStage == 2) {
            intUpDownLineY += intUpDownSpeed;
            if (intUpDownLineY <= 230 || intUpDownLineY >= 470) {
                intUpDownSpeed = -intUpDownSpeed;
                if (intUpDownLineY < 230) intUpDownLineY = 230;
                if (intUpDownLineY > 470) intUpDownLineY = 470;
            }
        }
        // Stage 3 active: Move Power bar
        else if (intStage == 3) {
            intPowerLineX += intPowerSpeed;
            if (intPowerLineX <= 1020 || intPowerLineX >= 1260) {
                intPowerSpeed = -intPowerSpeed;
                if (intPowerLineX < 1020) intPowerLineX = 1020;
                if (intPowerLineX > 1260) intPowerLineX = 1260;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imgBG, 0, 0, theFrame);
        g.drawImage(imgGoal, 130, 160, theFrame);
        g.drawImage(imgBall, intBallX, intBallY, theFrame);
        drawMeters(g);
    }

    public void loadIMG() {
        try {
            imgBG = ImageIO.read(new File("Images/shootingBG.jpeg"));
            imgGoal = ImageIO.read(new File("Images/Goal.png"));
            imgBall = ImageIO.read(new File("Images/ball.png"));
        } catch (IOException e) {
            System.out.println("image error");
        }
    }

    public void spacebarinput(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            // STAGE 1: Lock Left/Right
            if (intStage == 1) {
                intFinalLeftRightX = intLeftRightLineX;
                if (intFinalLeftRightX < 1020) intFinalLeftRightX = 1020;
                if (intFinalLeftRightX > 1260) intFinalLeftRightX = 1260;

                dblFinalLeftRightPercent = ((double) (intFinalLeftRightX - 1020) / 240.0) * 100.0;
                System.out.println("Left/Right Locked! X: " + intFinalLeftRightX + " (" + (int) dblFinalLeftRightPercent + "%)");             
                intStage = 2;
            }
            // STAGE 2: Lock Up/Down
            else if (intStage == 2) {
                intFinalUpDownY = intUpDownLineY;              
                if (intFinalUpDownY < 230) intFinalUpDownY = 230;
                if (intFinalUpDownY > 470) intFinalUpDownY = 470;

                dblFinalUpDownPercent = ((double) (intFinalUpDownY - 230) / 240.0) * 100.0;
                System.out.println("Up/Down Locked! Y: " + intFinalUpDownY + " (" + (int) dblFinalUpDownPercent + "%)");               
                intStage = 3;
            }
            // STAGE 3: Lock Power & Calculate Vectors
            else if (intStage == 3) {
                intFinalPowerX = intPowerLineX;              
                if (intFinalPowerX < 1020) intFinalPowerX = 1020;
                if (intFinalPowerX > 1260) intFinalPowerX = 1260;

                dblFinalPowerPercent = ((double) (intFinalPowerX - 1020) / 240.0) * 100.0;
                System.out.println("Power Locked! X: " + intFinalPowerX + " (" + (int) dblFinalPowerPercent + "%)");                
                
                int ballWidth = imgBall.getWidth();
                int ballHeight = imgBall.getHeight();

                // Target the open net area inside the goal image.
                int goalLeft = 315;
                int goalRight = 940;
                int goalTop = 330;
                int goalBottom = 450;

                double targetCenterX = goalLeft + ((dblFinalLeftRightPercent / 100.0) * (goalRight - goalLeft));
                double targetCenterY = goalTop + ((dblFinalUpDownPercent / 100.0) * (goalBottom - goalTop));
                dblTargetX = targetCenterX - (ballWidth / 2.0);
                dblTargetY = targetCenterY - (ballHeight / 2.0);

                intShotTotalFrames = (int) (62 - (dblFinalPowerPercent * 0.40)); 
                if (intShotTotalFrames < 18) {
                    intShotTotalFrames = 18;
                }

                // Sync doubles to current ball coordinates
                dblBallX = intBallX;
                dblBallY = intBallY;
                dblShotStartX = dblBallX;
                dblShotStartY = dblBallY;
                intShotFrame = 0;

                isShooting = true;
                intStage = 4; 
                System.out.println("--- BALL KICKED --- Target X: " + (int) dblTargetX + " Y: " + (int) dblTargetY);
            }
        }
    }

    public void animateBall() {
        intShotFrame++;
        
        double progress = (double) intShotFrame / intShotTotalFrames;
        if (progress > 1.0) {
            progress = 1.0;
        }

        double easedProgress = 1.0 - Math.pow(1.0 - progress, 3);
        dblBallX = dblShotStartX + ((dblTargetX - dblShotStartX) * easedProgress);
        dblBallY = dblShotStartY + ((dblTargetY - dblShotStartY) * easedProgress);
        
        intBallX = (int) Math.round(dblBallX);
        intBallY = (int) Math.round(dblBallY);

        if (progress >= 1.0) {
            isShooting = false;
            dblBallX = dblTargetX;
            dblBallY = dblTargetY;
            intBallX = (int) Math.round(dblTargetX);
            intBallY = (int) Math.round(dblTargetY);
            System.out.println("Shot finished at net!");
        }
    }

    // Constructor
    public shootingMechanic() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1280, 720));
        this.setFocusable(true);
        this.addKeyListener(this);

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

        theTimer.start();
        repaint();
        loadIMG();

        theFrame.setContentPane(this);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    // Main
    public static void main(String[] args) {
        new shootingMechanic();
    }
}
