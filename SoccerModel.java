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

    //Completed boolean values
    public boolean blnP1S = false;
    public boolean blnP1K = false;
    public boolean blnP2S = false;
    public boolean blnP2K = false;

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


    // Constructor.
    public SoccerModel() {
        loadCSV();
    }
}
