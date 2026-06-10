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
    int intWinningScore = 3;
    // True when the final goal happens, but the final shot animation still needs to play.
    public boolean blnPendingWinningAnimation = false;
    public boolean blnWinningAnimationPrinted = false;

    //shooting mechanic variables
    public int intShotStage = 1;
    public int intBallX = 610;
    public int intBallY = 550;

    public int intLeftRightLineX = 1140;
    public int intUpDownLineY = 350;
    public int intPowerLineX = 1140;

    // Faster default striker slider speeds.
    public int intLeftRightSpeed = 10;
    public int intUpDownSpeed = 11;
    public int intPowerSpeed = 12;

    public double dblFinalLeftRightPercent = 0.0;
    public double dblFinalUpDownPercent = 0.0;
    public double dblFinalPowerPercent = 0.0;

    public boolean blnShooting = false;

    // Goalie mechanic variables
    public int intGoalieStage = 1;
    public int intGoalieLeftRightLineX = 1140;
    public int intGoalieUpDownLineY = 350;

    // Faster default goalie slider speeds.
    public int intGoalieLeftRightSpeed = 10;
    public int intGoalieUpDownSpeed = 11;

    public double dblGoalieFinalLeftRightPercent = 0.0;
    public double dblGoalieFinalUpDownPercent = 0.0;
    
    //Indicator for goalie
    public int intCircleBlurX;
	public int intCircleBlurY;

    // Result variables for the save/goal check.
    // blnResultReady becomes true once the goalie finishes both sliders.
    public boolean blnShotSaved = false;
    public boolean blnResultReady = false;
    public boolean blnResultScored = false;
    public boolean blnShowHitbox = true;
    // Controls if the red shooting hint circle should be shown to the goalie.
    public boolean blnShowShootingHint = true;

    // MVC animation state:
    // The model stores the numbers for the play animation so animation.java is not needed.
    public boolean blnPlayAnimationRunning = false;
    public int intPlayAnimationFrame = 0;
    public int intPlayAnimationTotalFrames = 130;

    public int intAnimStrikerX = 340;
    public int intAnimStrikerY = 440;
    public int intAnimGoalieX = 450;
    public int intAnimGoalieY = 340;
    public int intAnimBallX = 610;
    public int intAnimBallY = 550;

    public boolean blnAnimShowRun = false;
    public boolean blnAnimShowShoot = false;
    public boolean blnAnimShowDive = false;

    private double dblAnimBallX = 610.0;
    private double dblAnimBallY = 550.0;
    private int intAnimBallFrame = 0;
    private int intAnimBallTotalFrames = 45;
    private boolean blnAnimBallFinished = false;



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
        int intCurrentPower = getCurrentShootingStrikerPower();
        intPowerSpeed = Math.max(4, 14 - intCurrentPower);
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

    public int getCurrentShootingStrikerPower() {
        if (intGamePhase == 1) {
            return intP1SPwr;
        } else if (intGamePhase == 3) {
            return intP2SPwr;
        }
        return strikerPower;
    }

    public void resetGoalie() {
        intGoalieStage = 1;
        intGoalieLeftRightLineX = 1140;
        intGoalieUpDownLineY = 350;
        // Reset goalie sliders with the faster default speeds.
        intGoalieLeftRightSpeed = 10;
        intGoalieUpDownSpeed = 11;
        dblGoalieFinalLeftRightPercent = 0.0;
        dblGoalieFinalUpDownPercent = 0.0;
    }

    public void moveGoalieSliders() {
        // Boost goalie movement so the goalie sliders feel faster.
        int intAgilitySpeed = Math.max(6, getCurrentSavingKeeperAgility() * 2);

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
        int goalTop = 275;
        int goalBottom = 500;

        double targetCenterX = goalLeft + ((dblFinalLeftRightPercent / 100.0) * (goalRight - goalLeft));
        double targetCenterY = goalTop + ((dblFinalUpDownPercent / 100.0) * (goalBottom - goalTop));
        
        // Compensate for center alignment
        dblTargetX = targetCenterX - 15; // Roughly half of ball thickness
        dblTargetY = targetCenterY - 15;

        // Dynamic speed based on power selection
        intShotTotalFrames = (int) (62 - (dblFinalPowerPercent * 0.40)); 
        if (intShotTotalFrames < 18){
			 intShotTotalFrames = 18;
		}

        dblBallX = intBallX;
        dblBallY = intBallY;
        dblShotStartX = dblBallX;
        dblShotStartY = dblBallY;
        intShotFrame = 0;
    }

    public boolean animateBall() {
        intShotFrame++;
        
        double progress = (double) intShotFrame / intShotTotalFrames;
        if (progress > 1.0) {
			progress = 1.0;
		}

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

    public void startPlayAnimation() {
        // Starts the MVC play animation after both players finish input.
        // The controller calls this; the view only draws these stored values.
        blnPlayAnimationRunning = true;
        blnShooting = true;

        intPlayAnimationFrame = 0;
        intPlayAnimationTotalFrames = 130;

        intAnimStrikerX = 340;
        intAnimStrikerY = 440;
        intAnimGoalieX = 450;
        intAnimGoalieY = 340;
        intAnimBallX = 610;
        intAnimBallY = 550;

        blnAnimShowRun = true;
        blnAnimShowShoot = false;
        blnAnimShowDive = false;
        blnAnimBallFinished = false;

        dblAnimBallX = 610.0;
        dblAnimBallY = 550.0;
        intAnimBallFrame = 0;
        intAnimBallTotalFrames = intShotTotalFrames;

        if (intAnimBallTotalFrames < 1) {
            intAnimBallTotalFrames = 1;
        }
    }

    public boolean updatePlayAnimation() {
        // Advances the animation by one timer frame.
        // Returns true when the scene is finished so the controller can switch turns.
        if (!blnPlayAnimationRunning) {
            return false;
        }

        intPlayAnimationFrame++;

        if (intPlayAnimationFrame < 35) {
            // First part: striker runs toward the ball.
            blnAnimShowRun = true;
            blnAnimShowShoot = false;
            blnAnimShowDive = false;
            intAnimStrikerX = 340 + (intPlayAnimationFrame * 5);
        } else {
            // Second part: striker switches to shooting image.
            blnAnimShowRun = false;
            blnAnimShowShoot = true;
            intAnimStrikerX = 520;
        }

        if (intPlayAnimationFrame >= 42 && !blnAnimBallFinished) {
            // Ball starts moving a little after the shoot image appears.
            blnAnimBallFinished = moveAnimationBall();
        }

        if (intPlayAnimationFrame >= 52) {
            // Goalie starts diving after the ball leaves the striker.
            blnAnimShowDive = true;
            moveAnimationGoalie();
        }

        if (intPlayAnimationFrame >= intPlayAnimationTotalFrames) {
            stopPlayAnimation();
            return true;
        }

        return false;
    }

    private boolean moveAnimationBall() {
        // Moves the ball from the penalty spot to the target calculated by the shot sliders.
        intAnimBallFrame++;

        double dblProgress = (double) intAnimBallFrame / (double) intAnimBallTotalFrames;
        if (dblProgress > 1.0) {
            dblProgress = 1.0;
        }

        double dblEasedProgress = 1.0 - Math.pow(1.0 - dblProgress, 3);
        dblAnimBallX = 610.0 + ((dblTargetX - 610.0) * dblEasedProgress);
        dblAnimBallY = 550.0 + ((dblTargetY - 550.0) * dblEasedProgress);

        intAnimBallX = (int) Math.round(dblAnimBallX);
        intAnimBallY = (int) Math.round(dblAnimBallY);

        if (dblProgress >= 1.0) {
            intAnimBallX = (int) Math.round(dblTargetX);
            intAnimBallY = (int) Math.round(dblTargetY);
            return true;
        }

        return false;
    }

    private void moveAnimationGoalie() {
        // Moves the goalie toward the same goal point used by drawGoalieHitbox.
        // This keeps the dive image aligned with the actual save area.
        int intDiveFrame = intPlayAnimationFrame - 52;

        if (intDiveFrame > 30) {
            intDiveFrame = 30;
        }

        double dblProgress = (double) intDiveFrame / 30.0;

        int goalLeft = 315;
        int goalRight = 940;
        int goalTop = 275;
        int goalBottom = 500;

        int intGoalieCenterX = goalLeft + (int)(dblGoalieFinalLeftRightPercent / 100.0 * (goalRight - goalLeft));
        int intGoalieCenterY = goalTop + (int)(dblGoalieFinalUpDownPercent / 100.0 * (goalBottom - goalTop));

        int intTargetGoalieX = intGoalieCenterX - 75;
        int intTargetGoalieY = intGoalieCenterY - 80;

        intAnimGoalieX = 450 + (int)((intTargetGoalieX - 450) * dblProgress);
        intAnimGoalieY = 340 + (int)((intTargetGoalieY - 340) * dblProgress);
    }

    public void stopPlayAnimation() {
        // Clears the animation state so normal shooter/goalie screens can draw again.
        blnPlayAnimationRunning = false;
        blnShooting = false;
        intPlayAnimationFrame = 0;
        blnAnimShowRun = false;
        blnAnimShowShoot = false;
        blnAnimShowDive = false;
        blnAnimBallFinished = false;
    }

    public boolean isPlayAnimationRunning() {
        return blnPlayAnimationRunning;
    }

    public int getCurrentKeeperCoverage(){
        if (intGamePhase == 2){
            return intP2KCvg;
        }else if (intGamePhase == 4){
            return intP1KCvg;
        }

        return keeperCoverage;
    }

    public boolean isShotSaved(){
        int intCoverage = getCurrentKeeperCoverage();

        double dblHitboxWidth = 18 + (intCoverage * 2);
        double dblHitboxHeight = 28 + (intCoverage * 2);

        //get the difference in the x axis (saved or not)
        double dblLeftRightDifference = Math.abs(dblFinalLeftRightPercent - dblGoalieFinalLeftRightPercent);

        //get the difference in the y axis (saved or not)
        double dblUpDownDifference = Math.abs(dblFinalUpDownPercent - dblGoalieFinalUpDownPercent);

        //if left right is < hitbox -> true
        //if up down is smaller than hitbox
        return dblLeftRightDifference <= dblHitboxWidth && dblUpDownDifference <= dblHitboxHeight;
    }

    // Constructor.
    public SoccerModel() {
        loadCSV();
        this.intCircleBlurX = (int)(Math.random() * 101) - 50;
		this.intCircleBlurY = (int)(Math.random() * 101) - 50;
    }
}
