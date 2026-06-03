import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.*;
import java.io.File;

public class goalieMechanic extends JPanel implements ActionListener, KeyListener {
    //properties
    String keeperName = "Keeper Name";
    int keeperAgility = 9;
    int keeperCoverage = 2;  
    
    int intfps = 60;  

    JFrame theFrame = new JFrame("Trial Goalie Mechanic");
    Timer theTimer = new Timer(1000 / intfps, this);

    BufferedImage imgBG = null;
    BufferedImage imgGoal = null;
    BufferedImage imgGoalie = null;

    int keeperChosen;
    int keeperFileValue = 1;
    int intAgility = 4;
    int intCoverage;

    int intLeftRightLineX = 1140; 
    int intUpDownLineY = 350;    

    int intLeftRightSpeed = 4;
    int intUpDownSpeed = 5;
    
    int intStage = 1; 
    int intFinalLeftRightX = 0;
    int intFinalUpDownY = 0;

    double dblFinalLeftRightPercent = 0.0;
    double dblFinalUpDownPercent = 0.0;


    //methods
    public void actionPerformed(ActionEvent evt){
        if (intStage < 3) {
            moveSliders();
        }

        repaint();
    }

    public void keyPressed(KeyEvent evt){

    }

    public void keyReleased(KeyEvent evt){
        spacebarinput(evt);
    }

    public void keyTyped(KeyEvent evt){

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(imgBG, 0, 0, theFrame);
        g.drawImage(imgGoal, 130, 160, theFrame);
        if (imgGoalie != null) {
            g.drawImage(imgGoalie, 450, 340, theFrame);
        }
        drawMeters(g);
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
    }

    public void moveSliders() {
        int intAgilitySpeed = Math.max(1, intAgility / 2);

        if (intStage == 1) {
            if (intLeftRightSpeed > 0) {
                intLeftRightLineX += intAgilitySpeed;
            } else {
                intLeftRightLineX -= intAgilitySpeed;
            }

            if (intLeftRightLineX <= 1020 || intLeftRightLineX >= 1260) {
                intLeftRightSpeed = -intLeftRightSpeed;

                if (intLeftRightLineX < 1020) {
                    intLeftRightLineX = 1020;
                }

                if (intLeftRightLineX > 1260) {
                    intLeftRightLineX = 1260;
                }
            }
        } else if (intStage == 2) {
            if (intUpDownSpeed > 0) {
                intUpDownLineY += intAgilitySpeed;
            } else {
                intUpDownLineY -= intAgilitySpeed;
            }

            if (intUpDownLineY <= 230 || intUpDownLineY >= 470) {
                intUpDownSpeed = -intUpDownSpeed;

                if (intUpDownLineY < 230) {
                    intUpDownLineY = 230;
                }

                if (intUpDownLineY > 470) {
                    intUpDownLineY = 470;
                }
            }
        }
    }

    public void spacebarinput(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            if (intStage == 1) {
                intFinalLeftRightX = intLeftRightLineX;

                if (intFinalLeftRightX < 1020) {
                    intFinalLeftRightX = 1020;
                }

                if (intFinalLeftRightX > 1260) {
                    intFinalLeftRightX = 1260;
                }

                dblFinalLeftRightPercent = ((double) (intFinalLeftRightX - 1020) / 240.0) * 100.0;
                System.out.println("Goalie Left/Right Locked! X: " + intFinalLeftRightX + " (" + (int) dblFinalLeftRightPercent + "%)");

                intStage = 2;
            } else if (intStage == 2) {
                intFinalUpDownY = intUpDownLineY;

                if (intFinalUpDownY < 230) {
                    intFinalUpDownY = 230;
                }

                if (intFinalUpDownY > 470) {
                    intFinalUpDownY = 470;
                }

                dblFinalUpDownPercent = ((double) (intFinalUpDownY - 230) / 240.0) * 100.0;
                System.out.println("Goalie Up/Down Locked! Y: " + intFinalUpDownY + " (" + (int) dblFinalUpDownPercent + "%)");
                System.out.println("Keeper Agility: " + keeperAgility + " | Keeper Coverage: " + keeperCoverage);

                intStage = 3;
            }
        }
    }


    public void loadIMG() {
        try {
            imgBG = ImageIO.read(new File("Images/shootingBG.jpeg"));
            imgGoal = ImageIO.read(new File("Images/Goal.png"));
            loadGoalieImage();
        } catch (IOException e) {
            System.out.println("image error");
        }
    }

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
            imgGoalie = ImageIO.read(new File("Images/keepers/K" + keeperFileValue + "Stand.png"));
        } catch (IOException e) {
            System.out.println("goalie image error");
        }
    }



    //constructor
    public goalieMechanic(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1280, 720));
        this.setFocusable(true);
        this.addKeyListener(this);
        intAgility = keeperAgility;
        intCoverage = keeperCoverage;

        theTimer.start();
        repaint();
        loadIMG();

        theFrame.setContentPane(this);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.pack();
        theFrame.setVisible(true);
    }



    //main method
    public static void main (String[] args){
        new goalieMechanic();
    }


}
