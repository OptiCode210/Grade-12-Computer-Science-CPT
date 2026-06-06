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



    //Completed boolean values
    public boolean blnP1S = false;
    public boolean blnP1K = false;
    public boolean blnP2S = false;
    public boolean blnP2K = false;



    //shooting mechanic variables


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
        System.out.println("Resetting shot");

        intShotStage = 1;
        intBallX = 610;
        intBallY = 550;

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


    // Constructor.
    public SoccerModel() {
        loadCSV();
    }
}
