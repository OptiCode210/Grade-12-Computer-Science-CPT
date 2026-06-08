import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Backend for the soccer game
public class SoccerModel {
    //Properties

    //Lists
    public String[][] strikers = new String[3][3];
    public String[][] keepers = new String[3][3];

    //Striker variables
    public String strikerName;
    public int strikerAccuracy;
    public int strikerPower;

    //Keeper variables
    public String keeperName;
    public int keeperAgility;
    public int keeperCoverage;

    //Network variables
    public String strServerID;
    public String strServerIP;
    public String strIP;
    public String strNetText;
    public SuperSocketMaster connectSSM = null;
    
    //Boolean variables
    public boolean blnConnected = false;
    public boolean blnSentPicks = false;
    public boolean blnReceivedPicks = false;

    // Player 1 selections
    public String strP1K;
    public String strP1S;
    public int intP1KAgi;
    public int intP1KCvg;
    public int intP1SPwr;
    public int intP1SAcc;

    // Player 2 selections
    public String strP2K;
    public String strP2S;
    public int intP2KAgi;
    public int intP2KCvg;
    public int intP2SPwr;
    public int intP2SAcc;	

    
    public int intPicking = 1;

    //gameplay variables
    /* 0 = picking, 1 = P1 shoot, 2 = P2 Save, 3 = P2 shoots, 4 = P1 saves */
    int intGamePhase = 0;   
    int intP1Score = 0;
    public int intP2Score = 0;
    int intWinningScore = 5;

    //shooting mechanic variables
    public int intShotStage = 1;
    public int intBallX = 610;
    public int intBallY = 550;

    public int intLeftRightLineX = 1140;
    public int intUpDownLineY = 350;
    public int intPowerLineX = 1140;

    public int intLeftRightSpeed = 4;
    public int intUpDownSpeed = 5;
    public int intPowerSpeed = 6;

    public double dblFinalLeftRightPercent = 0.0;
    public double dblFinalUpDownPercent = 0.0;
    public double dblFinalPowerPercent = 0.0;

    public boolean blnShooting = false;

    // Goalie mechanic variables
    public int intGoalieStage = 1;
    public int intGoalieLeftRightLineX = 1140;
    public int intGoalieUpDownLineY = 350;

    public int intGoalieLeftRightSpeed = 4;
    public int intGoalieUpDownSpeed = 5;

    public double dblGoalieFinalLeftRightPercent = 0.0;
    public double dblGoalieFinalUpDownPercent = 0.0;



    //Completed boolean values
    public boolean blnP1S = false;
    public boolean blnP1K = false;
    public boolean blnP2S = false;
    public boolean blnP2K = false;



    // Target vectors for flight path
    public double dblBallX = 610.0;
    public double dblBallY = 550.0;
    public double dblTargetX = 0.0;
    public double dblTargetY = 0.0;
    public double dblShotStartX = 610.0;
    public double dblShotStartY = 550.0;
    public int intShotFrame = 0;
    public int intShotTotalFrames = 45;


    //methods
    //Loads striker and keeper data from two CSV files.
    public void loadCSV() {
        try {
            //opens strikers csv file
            BufferedReader playersFile = new BufferedReader(new FileReader("players.csv"));
            String strLine;
            int count = 0;

            while ((strLine = playersFile.readLine()) != null && count < strikers.length) {
                String[] split = strLine.split(",");

                //store the values in the strikers list
                strikers[count][0] = split[0].trim();
                strikers[count][1] = split[1].trim();
                strikers[count][2] = split[2].trim();
                count++;
            }
            playersFile.close();

            //open keeper csv file
            BufferedReader keepersFile = new BufferedReader(new FileReader("keepers.csv"));
            count = 0;

            while ((strLine = keepersFile.readLine()) != null && count < keepers.length) {
                String[] split = strLine.split(",");

                //store values in keepers list
                keepers[count][0] = split[0].trim();
                keepers[count][1] = split[1].trim();
                keepers[count][2] = split[2].trim();
                count++;
            }
            keepersFile.close();

        } catch (IOException e) {
            System.out.println("File error");
        }
    }

	public void resetShot(){
        intShotStage = 1;
        intBallX = 610;
        intBallY = 550;
        dblBallX = 610.0;
        dblBallY = 550.0;
        intLeftRightLineX = 1140;
        intUpDownLineY = 350;
        intPowerLineX = 1140;
        blnShooting = false;
    }

    public void moveShotSliders() {
        if (intShotStage == 1) {
            intLeftRightLineX += intLeftRightSpeed;

            if (intLeftRightLineX <= 1020 || intLeftRightLineX >= 1260) {
                intLeftRightSpeed = -intLeftRightSpeed;
            }
        } else if (intShotStage == 2) {
            intUpDownLineY += intUpDownSpeed;

            if (intUpDownLineY <= 230 || intUpDownLineY >= 470) {
                intUpDownSpeed = -intUpDownSpeed;
            }
        } else if (intShotStage == 3) {
            intPowerLineX += intPowerSpeed;

            if (intPowerLineX <= 1020 || intPowerLineX >= 1260) {
                intPowerSpeed = -intPowerSpeed;
            }
        }
    }

    public int getCurrentSavingKeeperAgility() {
        if (intGamePhase == 2) {
            return intP2KAgi;
        } else if (intGamePhase == 4) {
            return intP1KAgi;
        }
        return keeperAgility;
    }

    public void resetGoalie() {
        intGoalieStage = 1;
        intGoalieLeftRightLineX = 1140;
        intGoalieUpDownLineY = 350;
        intGoalieLeftRightSpeed = 4;
        intGoalieUpDownSpeed = 5;
        dblGoalieFinalLeftRightPercent = 0.0;
        dblGoalieFinalUpDownPercent = 0.0;
    }

    public void moveGoalieSliders() {
        int intAgilitySpeed = Math.max(1, getCurrentSavingKeeperAgility() / 2);

        if (intGoalieStage == 1) {
            if (intGoalieLeftRightSpeed > 0) {
                intGoalieLeftRightLineX += intAgilitySpeed;
            } else {
                intGoalieLeftRightLineX -= intAgilitySpeed;
            }

            if (intGoalieLeftRightLineX <= 1020 || intGoalieLeftRightLineX >= 1260) {
                intGoalieLeftRightSpeed = -intGoalieLeftRightSpeed;

                if (intGoalieLeftRightLineX < 1020) {
                    intGoalieLeftRightLineX = 1020;
                }

                if (intGoalieLeftRightLineX > 1260) {
                    intGoalieLeftRightLineX = 1260;
                }
            }
        } else if (intGoalieStage == 2) {
            if (intGoalieUpDownSpeed > 0) {
                intGoalieUpDownLineY += intAgilitySpeed;
            } else {
                intGoalieUpDownLineY -= intAgilitySpeed;
            }

            if (intGoalieUpDownLineY <= 230 || intGoalieUpDownLineY >= 470) {
                intGoalieUpDownSpeed = -intGoalieUpDownSpeed;

                if (intGoalieUpDownLineY < 230) {
                    intGoalieUpDownLineY = 230;
                }

                if (intGoalieUpDownLineY > 470) {
                    intGoalieUpDownLineY = 470;
                }
            }
        }
    }

    public boolean isLocalShooterInputTurn() {
        // True only when this computer is controlling the striker.
        // This prevents the goalie/opponent computer from moving shooting sliders.
        return (intGamePhase == 1 && intPicking == 1) || (intGamePhase == 3 && intPicking == 2);
    }

    public boolean isLocalGoalieInputTurn() {
        // True only when this computer is controlling the goalie save input.
        // The goalie can see the goalie screen earlier, but sliders move only here.
        return (intGamePhase == 2 && intPicking == 2) || (intGamePhase == 4 && intPicking == 1);
    }

    public boolean shouldLocalViewShowGoalie() {
        // During the striker's turn, the opponent should see the goalie screen.
        // The goalie sliders stay frozen until the phase becomes their goalie input turn.
        return (intGamePhase == 1 && intPicking == 2) ||
               (intGamePhase == 2 && intPicking == 2) ||
               (intGamePhase == 3 && intPicking == 1) ||
               (intGamePhase == 4 && intPicking == 1);
    }

    public boolean shouldLocalViewShowShooting() {
        // The striker sees the shooting screen while shooting and while waiting
        // for the goalie result after their shot.
        return (intGamePhase == 1 && intPicking == 1) ||
               (intGamePhase == 2 && intPicking == 1) ||
               (intGamePhase == 3 && intPicking == 2) ||
               (intGamePhase == 4 && intPicking == 2);
    }

    public void calculateTarget() {
        // Target coordinates based on open net area inside the view frame
        int goalLeft = 315;
        int goalRight = 940;
        int goalTop = 330;
        int goalBottom = 450;

        double targetCenterX = goalLeft + ((dblFinalLeftRightPercent / 100.0) * (goalRight - goalLeft));
        double targetCenterY = goalTop + ((dblFinalUpDownPercent / 100.0) * (goalBottom - goalTop));
        
        // Compensate for center alignment
        dblTargetX = targetCenterX - 15; // Roughly half of ball thickness
        dblTargetY = targetCenterY - 15;

        // Dynamic speed based on power selection
        intShotTotalFrames = (int) (62 - (dblFinalPowerPercent * 0.40)); 
        if (intShotTotalFrames < 18) intShotTotalFrames = 18;

        dblBallX = intBallX;
        dblBallY = intBallY;
        dblShotStartX = dblBallX;
        dblShotStartY = dblBallY;
        intShotFrame = 0;
    }

    public boolean animateBall() {
        intShotFrame++;
        
        double progress = (double) intShotFrame / intShotTotalFrames;
        if (progress > 1.0) progress = 1.0;

        // Clean cubic ease-out calculation for deceleration arc
        double easedProgress = 1.0 - Math.pow(1.0 - progress, 3);
        dblBallX = dblShotStartX + ((dblTargetX - dblShotStartX) * easedProgress);
        dblBallY = dblShotStartY + ((dblTargetY - dblShotStartY) * easedProgress);
        
        intBallX = (int) Math.round(dblBallX);
        intBallY = (int) Math.round(dblBallY);

        if (progress >= 1.0) {
            blnShooting = false;
            intBallX = (int) Math.round(dblTargetX);
            intBallY = (int) Math.round(dblTargetY);
            
            // Turn over controls / Reset back to meter phase
            intShotStage = 1; 
            return true;
        }
        return false;
    }


    // Constructor.
    public SoccerModel() {
        loadCSV();
    }
}
