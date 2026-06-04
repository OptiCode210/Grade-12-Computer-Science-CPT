import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SoccerModel {
    // Data structures
    public String[][] strikers = new String[3][3];
    public String[][] keepers = new String[3][3];

    // Selected Player Variables
    public String strikerName;
    public int strikerAccuracy;
    public int strikerPower;

    public String keeperName;
    public int keeperAgility;
    public int keeperCoverage;

    // Network Variables
    public String strServerID;
    public String strServerIP;
    public String strIP;
    public String strNetText;
    public SuperSocketMaster connectSSM = null;
    
    public boolean blnConnected = false;
    public boolean blnSentPicks = false;
    public boolean blnReceivedPicks = false;

    // Selection States
    public String strP1K;
    public String strP1S;
    public int intP1KAgi;
    public int intP1KCvg;
    public int intP1SPwr;
    public int intP1SAcc;

    public String strP2K;
    public String strP2S;
    public int intP2KAgi;
    public int intP2KCvg;
    public int intP2SPwr;
    public int intP2SAcc;	

    public int intPicking = 1;
    public boolean blnP1S = false;
    public boolean blnP1K = false;
    public boolean blnP2S = false;
    public boolean blnP2K = false;

    public SoccerModel() {
        loadCSV();
    }

    public void loadCSV() {
        try {
            // player file csv
            BufferedReader playersFile = new BufferedReader(new FileReader("players.csv"));
            String strLine;
            int count = 0;

            while ((strLine = playersFile.readLine()) != null && count < strikers.length) {
                String[] split = strLine.split(",");
                strikers[count][0] = split[0].trim();
                strikers[count][1] = split[1].trim();
                strikers[count][2] = split[2].trim();
                count++;
            }
            playersFile.close();

            // keeper file csv
            BufferedReader keepersFile = new BufferedReader(new FileReader("keepers.csv"));
            count = 0;

            while ((strLine = keepersFile.readLine()) != null && count < keepers.length) {
                String[] split = strLine.split(",");
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
}
