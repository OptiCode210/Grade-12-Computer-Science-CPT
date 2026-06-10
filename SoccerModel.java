import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * <h1>SoccerModel</h1>
 * The backend data model for the multiplayer network soccer game.
 * Following the Model-View-Controller (MVC) architectural pattern, this class
 * manages game state data, processes vector mathematics for ball physics, 
 * tracks player scoring attributes, and handles coordinate states for 
 * real-time slider and dive animations.
 * @author Cassian
 * @version 1.0
 * @since 2026-06-09
 */

// Backend for the soccer game
public class SoccerModel {
    //Properties
    //Lists
    /**A 2D array structure storing text attributed to the striker data file*/
    public String[][] strikers = new String[3][3];
    /**A 2D array structure storing text attributed to the keeper data file*/
    public String[][] keepers = new String[3][3];

    //Striker variables
    /**Holds the name of the selected striker*/
    public String strikerName;
    /**Holds the accuracy value of the selected striker*/
    public int strikerAccuracy;
    /**Holds the power value of the selected striker*/
    public int strikerPower;

    //Keeper variables
    /**Holds the name of the selected keeper*/
    public String keeperName;
    /**Holds the agility value of the selected keeper*/
    public int keeperAgility;
    /**Holds the coverage value of the selected keeper*/
    public int keeperCoverage;

    //Network variables
    /**Holds the idenity of the game*/
    public String strServerID;
    /**Holds the IP adress of the designated host*/
    public String strServerIP;
    /**Store the local IP value adress data*/
    public String strIP;
    /**The buffer input or output messaging text transmitted across the network*/
    public String strNetText;
    /**Manages the netowrk socket connection*/
    public SuperSocketMaster connectSSM = null;
    
    //Boolean variables
    /**Tracks whether a network connection has been established*/
    public boolean blnConnected = false;
    /**Tracks whether the player's character selections have been sent*/
    public boolean blnSentPicks = false;
    /**Tracks whether the player's character selections have been recieved*/
    public boolean blnReceivedPicks = false;

    // Player 1 selections
    /**Stores the character name of P1's keeper*/
    public String strP1K;
    /**Stores the character name of P1's striker*/
    public String strP1S;
    /**Stores the character agility of P1's keeper*/
    public int intP1KAgi;
    /**Stores the character agility of P1's coverage*/
    public int intP1KCvg;
    /**Stores the character power of P1's striker*/
    public int intP1SPwr;
    /**Stores the character accuracy of P1's striker*/
    public int intP1SAcc;

    // Player 2 selections
    /**Stores the character name of P2's keeper*/
    public String strP2K;
    /**Stores the character name of P2's striker*/
    public String strP2S;
    /**Stores the character agility of P2's keeper*/
    public int intP2KAgi;
    /**Stores the character coverage of P2's keeper*/
    public int intP2KCvg;
    /**Stores the character power of P2's strker*/
    public int intP2SPwr;
    /**Stores the character accurady of P2's striker*/
    public int intP2SAcc;	

    /**Determins what player is currently picking*/
    public int intPicking = 1;

    //gameplay variables
    //0 = picking, 1 = P1 shoot, 2 = P2 Save, 3 = P2 shoots, 4 = P1 saves
    int intGamePhase = 0;   
    int intP1Score = 0;
    public int intP2Score = 0;
    int intWinningScore = 3;
    /**Stores which player won the game. 0 means no winner yet, 1 means P1, 2 means P2.*/
    public int intWinningPlayer = 0;
    // True when the final goal happens, but the final shot animation still needs to play.
    public boolean blnPendingWinningAnimation = false;
    /**Tracks when the final game-over win text is printed*/
    public boolean blnWinningAnimationPrinted = false;
    /**Tracks when a goal should be added after the shot animation finishes*/
    public boolean blnScoreAfterAnimation = false;

    //shooting mechanic variables
    /**Tracks which slider the striker is on*/
    public int intShotStage = 1;
    /**The x-coordinate of the ball*/
    public int intBallX = 610;
    /**The y-coordinate of the ball*/
    public int intBallY = 550;
	/**The current coordinate of the striker's left/right slider*/
    public int intLeftRightLineX = 1140;
    /**The current coordinate of the striker's up/down slider*/
    public int intUpDownLineY = 350;
    /**The current coordinate of the striker's power slider*/
    public int intPowerLineX = 1140;

    // Faster default striker slider speeds.
    /**The current speed of the striker's left/right slider*/
    public int intLeftRightSpeed = 10;
    /**The current speed of the striker's up/down slider*/
    public int intUpDownSpeed = 11;
    /**The current speed of the striker's power slider*/
    public int intPowerSpeed = 12;

	/**The final target percentages based on the left/right slider*/
    public double dblFinalLeftRightPercent = 0.0;
    /**The final target percentages based on the up/down slider*/
    public double dblFinalUpDownPercent = 0.0;
    /**The final target percentages based on the power slider*/
    public double dblFinalPowerPercent = 0.0;

	/**Tracks whether a shot is in progress*/
    public boolean blnShooting = false;

    // Goalie mechanic variables
    /**Tracks which slider the goalie is on*/
    public int intGoalieStage = 1;
    /**Tracks the current coordinate of the keeper's left/right slider*/
    public int intGoalieLeftRightLineX = 1140;
    /**Tracks the current coordinate of the keeper's up/down slider*/
    public int intGoalieUpDownLineY = 350;

    // Faster default goalie slider speeds.
    /**The current speed of the keeper's left/right slider*/
    public int intGoalieLeftRightSpeed = 10;
    /**The current speed of the keeper's up/down slider*/
    public int intGoalieUpDownSpeed = 11;

	/**The final target percentages based on the left/right slider*/
    public double dblGoalieFinalLeftRightPercent = 0.0;
    /**The final target percentages based on the up/down slider*/
    public double dblGoalieFinalUpDownPercent = 0.0;
    
    //Indicator for goalie
    /**The x-coordinate applied for the hint*/
    public int intCircleBlurX;
    /**The y-coordinate applied for the hint*/
	public int intCircleBlurY;

    // Result variables for the save/goal check.
    // blnResultReady becomes true once the goalie finishes both sliders.
    /**Stores the final result of whether the shot was saved or not*/
    public boolean blnShotSaved = false;
    /**Stores the signal that the all slider's have recieved an input*/
    public boolean blnResultReady = false;
    /**Tracks whether or not a goal was scored during the current cycle*/
    public boolean blnResultScored = false;
    /**Controls whether the visible save hitbox bounds are drawn*/
    public boolean blnShowHitbox = true;
    /**Controls if the red shooting hint circle should be shown to the goalie*/
    public boolean blnShowShootingHint = true;

    // MVC animation state:
    // The model stores the numbers for the play animation so animation.java is not needed.
    /**Tracks whether the main multi-frame visual play sequence is active*/
    public boolean blnPlayAnimationRunning = false;
    /**The current frame step counter within the multi-stage animation*/
    public int intPlayAnimationFrame = 0;
    /**The fixed duration length assigned to run the animation*/
    public int intPlayAnimationTotalFrames = 130;
	
	/**The x-coordinate of the striker during the amnimation*/
    public int intAnimStrikerX = 340;
    /**The y-coordinate of the striker during the animation*/
    public int intAnimStrikerY = 440;
    /**The x-coordinate of the keeper during the animation*/
    public int intAnimGoalieX = 450;
    /**The y-coordinate of the keeper during the animation*/
    public int intAnimGoalieY = 340;
    /**The x-coordinate of the ball during the animation*/
    public int intAnimBallX = 610;
    /**The y-coordinate of the ball during the animation*/
    public int intAnimBallY = 550;

	/**Indicates if the striker's running animation frame should be shown*/
    public boolean blnAnimShowRun = false;
    /**Indicates if the striker's shooting animation frame should be shown*/
    public boolean blnAnimShowShoot = false;
    /**Indicates if the goalie's dive animation frame should be down*/
    public boolean blnAnimShowDive = false;

    private double dblAnimBallX = 610.0;
    private double dblAnimBallY = 550.0;
    private int intAnimBallFrame = 0;
    private int intAnimBallTotalFrames = 45;
    private boolean blnAnimBallFinished = false;

    //Completed boolean values
    /**Tracks whether P1 finished selecting their striker*/
    public boolean blnP1S = false;
    /**Tracks whether P1 finished selecting their keeper*/
    public boolean blnP1K = false;
    /**Tracks whether P2 finished selecting their striker*/
    public boolean blnP2S = false;
    /**Tracks whether P2 finished selecting thier keeper*/
    public boolean blnP2K = false;



    // Target vectors for flight path
    /**Vector flight path of the ball's x-coordinate*/
    public double dblBallX = 610.0;
    /**Vector flight path of the ball's y-coordinate*/
    public double dblBallY = 550.0;
    /**Vector flight path of the ball's target x-coordinate*/
    public double dblTargetX = 0.0;
    /**Vector flight path of the ball's target y-coordiante*/
    public double dblTargetY = 0.0;
    /**The initial x-coordinate of the ball*/
    public double dblShotStartX = 610.0;
    /**The initial y-coordinate of the ball*/
    public double dblShotStartY = 550.0;
    /**The current frame step counter*/
    public int intShotFrame = 0;
    /**The total frame duration length for the ball's flight*/
    public int intShotTotalFrames = 45;


    //methods
    //Loads striker and keeper data from two CSV files.
    /**Load in the CSV files
     * Parses the files by commas and stores them into 2D arrays*/
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
	/**Resets the values after a shot is taken*/
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
	
	/**Moves the sliders for the striker*/
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
	
	/**Get the agility value for the striker
	 * @return The agility integer value belonging to the current active goalie*/
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

	
	/**Reset the goalie after a shot*/
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
	
	/**Move the sliders for goalie*/
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
	
	/**Analyzes matching turn states to track if the local device has input for the striker
	 * @return If the local user is acting as the kicker*/
    public boolean isLocalShooterInputTurn() {
        // True only when this computer is controlling the striker.
        // This prevents the goalie/opponent computer from moving shooting sliders.
        return (intGamePhase == 1 && intPicking == 1) || (intGamePhase == 3 && intPicking == 2);
    }
	
	/**Analyzes matching turn states to track if the local device has input for the goalie
	 * @return If the local user is acting as the goalie*/
    public boolean isLocalGoalieInputTurn() {
        // True only when this computer is controlling the goalie save input.
        // The goalie can see the goalie screen earlier, but sliders move only here.
        return (intGamePhase == 2 && intPicking == 2) || (intGamePhase == 4 && intPicking == 1);
    }
	
	/**Determines whether the client view layout configuration needs to toggle display over onto goalie workspace view
	 * @return If the goalie defense layout view configuration should display*/
    public boolean shouldLocalViewShowGoalie() {
        // During the striker's turn, the opponent should see the goalie screen.
        // The goalie sliders stay frozen until the phase becomes their goalie input turn.
        return (intGamePhase == 1 && intPicking == 2) ||
               (intGamePhase == 2 && intPicking == 2) ||
               (intGamePhase == 3 && intPicking == 1) ||
               (intGamePhase == 4 && intPicking == 1);
    }
	
	/**Determines whether the client view layout configuration needs to toggle display over onto striker shooting workspace view
	 * @return If the kicker shooter layout view configuration should display*/
    public boolean shouldLocalViewShowShooting() {
        // The striker sees the shooting screen while shooting and while waiting
        // for the goalie result after their shot.
        return (intGamePhase == 1 && intPicking == 1) ||
               (intGamePhase == 2 && intPicking == 1) ||
               (intGamePhase == 3 && intPicking == 2) ||
               (intGamePhase == 4 && intPicking == 2);
    }
	
	/**Compiles raw locked percentages into real vector targets matching graphic coordinates for ball physics flight equation*/
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
	
	/**Progresses real-time interactive coordinate steps moving ball location across calculation curves toward destination points
	 * @return If target destination coordinates have been reached*/
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
	
	/**Initialized cinematic variables establishing framework setup conditions to launch multi-stage sequence animations*/
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
	
	/**Main animation driver processing timeline frame updates moving field assets across layered animation benchmarks
	 * @return If the entire segment framework time has ran out*/
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

	/**Translates coordinates for ball rendering track paths mapped inside cinematic playback timeline curves
	 * @return If ball coordinates fully match target variable*/
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
	
	/**Translates coordinates driving graphical goalie dive animations matching calculated lock points on screen*/
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
	
	/**Clears all rendering variables assigned to cinematic screens preparing module environment for standard view modes*/
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

	/**Monitors execution state of game loops for screen update hooks checking animation presence
	 * @return If the play animation is currently executing*/
    public boolean isPlayAnimationRunning() {
        return blnPlayAnimationRunning;
    }
	
	/**Checks current match sequence structures extracting defense coverage capability levels belonging to active keeper
	 * @return The paramter attached to the active goalie*/
    public int getCurrentKeeperCoverage(){
        if (intGamePhase == 2){
            return intP2KCvg;
        }else if (intGamePhase == 4){
            return intP1KCvg;
        }

        return keeperCoverage;
    }
	
	/**Boundary evaluation routine measuring intercept calculations cross matching spatial dimensions for striker and keeper actions
	 * @return If the differences fall within an acceptable coverage boundary*/
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
    /**Constructs instance parameters setting initial random target uncertainty margins and parsing character rosters*/
    public SoccerModel() {
        loadCSV();
        this.intCircleBlurX = (int)(Math.random() * 101) - 50;
		this.intCircleBlurY = (int)(Math.random() * 101) - 50;
    }
}
