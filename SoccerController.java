import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

//connection
public class SoccerController implements ActionListener {
    //properties
    private SoccerModel model;
    private SoccerView view;

    //methods
    private void attachListeners() {
        view.playButton.addActionListener(this);
        view.helpButton.addActionListener(this);
        view.backButton.addActionListener(this);
        view.connectBackButton.addActionListener(this);
        view.serverButton.addActionListener(this);
        view.clientButton.addActionListener(this);
        view.confPickButton.addActionListener(this);

        view.K1Button.addActionListener(this);
        view.K2Button.addActionListener(this);
        view.K3Button.addActionListener(this);
        view.S1Button.addActionListener(this);
        view.S2Button.addActionListener(this);
        view.S3Button.addActionListener(this);
    }

    private void updateButtonLabels() {
        view.K1Button.setText(model.keepers[0][0]);
        view.K2Button.setText(model.keepers[1][0]);
        view.K3Button.setText(model.keepers[2][0]);

        view.S1Button.setText(model.strikers[0][0]);
        view.S2Button.setText(model.strikers[1][0]);
        view.S3Button.setText(model.strikers[2][0]);
    }

    public void selectStriker(int intIndex) {
        //loads striker from the index
        model.strikerName = model.strikers[intIndex][0];
        model.strikerAccuracy = Integer.parseInt(model.strikers[intIndex][1]);
        model.strikerPower = Integer.parseInt(model.strikers[intIndex][2]);

        if (model.intPicking == 1) {    //player 1
            model.strP1S = model.strikerName;   //striker name
            model.intP1SAcc = model.strikerAccuracy;    //striker accuracy
            model.intP1SPwr = model.strikerPower;   //striker power
            model.blnP1S = true;
            view.pickedSP1Label.setText("Striker: " + model.strikerName);
        } else if (model.intPicking == 2) { //player 2
            model.strP2S = model.strikerName;
            model.intP2SAcc = model.strikerAccuracy;
            model.intP2SPwr = model.strikerPower;
            model.blnP2S = true;
            view.pickedSP2Label.setText("Striker: " + model.strikerName);
        }

        System.out.println("Name: " + model.strikerName);
        System.out.println("Accuracy: " + model.strikerAccuracy);
        System.out.println("Power: " + model.strikerPower);

        //checks if selection works
        if (model.connectSSM != null) { 
            model.connectSSM.sendText("LIVE,Striker," + model.strikerName);
        }
    }

    public void selectKeeper(int intIndex) {
        //loads keeper variables
        model.keeperName = model.keepers[intIndex][0];
        model.keeperAgility = Integer.parseInt(model.keepers[intIndex][1]);
        model.keeperCoverage = Integer.parseInt(model.keepers[intIndex][2]);

        if (model.intPicking == 1) {    //player 1
            model.strP1K = model.keeperName;
            model.intP1KAgi = model.keeperAgility;
            model.intP1KCvg = model.keeperCoverage;
            model.blnP1K = true;
            view.pickedKP1Label.setText("Keeper: " + model.keeperName);
        } else if (model.intPicking == 2) { //player 2
            model.strP2K = model.keeperName;
            model.intP2KAgi = model.keeperAgility;
            model.intP2KCvg = model.keeperCoverage;
            model.blnP2K = true;
            view.pickedKP2Label.setText("Keeper: " + model.keeperName);
        }

        System.out.println("Name: " + model.keeperName);
        System.out.println("Agility: " + model.keeperAgility);
        System.out.println("Coverage: " + model.keeperCoverage);

        //checks if selection works
        if (model.connectSSM != null) {
            model.connectSSM.sendText("LIVE,Keeper," + model.keeperName);
        }
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == view.playButton) {   //play button
            view.setMainVisible(false);
            view.setConnectVisible(true);
        }

        if (evt.getSource() == view.connectBackButton) {    //join button
            view.setConnectVisible(false);
            view.setHelpVisible(false);
            view.setSelectionVisible(false);
            view.setMainVisible(true);
            view.thePanel.revalidate();
            view.thePanel.repaint();
            view.IPLabel.setText("IP: ");
        }

        if (evt.getSource() == view.serverButton) {     //host button
            model.connectSSM = new SuperSocketMaster(6112, this);
            model.strIP = model.connectSSM.getMyAddress();
            System.out.println(model.strIP);
            view.IPLabel.setText("IP: " + model.strIP);
            model.strServerIP = model.connectSSM.getMyAddress();
            model.connectSSM.connect();
            System.out.println("Waiting for connection");
        }

        if (evt.getSource() == view.clientButton) {     //connect button (Joptionpane)
            model.strServerID = JOptionPane.showInputDialog(view.theFrame, "Enter IP: ", "Connect", JOptionPane.PLAIN_MESSAGE);

            if (model.strServerID == null || model.strServerID.equals("")) {
                //if no IP address is provided
                JOptionPane.showMessageDialog(view.theFrame, "Please enter an IP address.");    
            } else {
                //if IP address is provided
                System.out.println("Entered IP: " + model.strServerID);
                model.connectSSM = new SuperSocketMaster(model.strServerID, 6112, this);

                if (model.connectSSM.connect()) {   //connection successful
                    System.out.println("CONNECTED");
                    model.connectSSM.sendText("Joined");
                } else {    //connection failed
                    model.blnConnected = false;
                    model.connectSSM = null;
                    JOptionPane.showMessageDialog(view.theFrame, "Could not connect. Try the IP again.");
                }
            }
        }

        // Network Data Interception
        if (model.connectSSM != null && evt.getSource() == model.connectSSM) {
            //if network is connected
            model.strNetText = model.connectSSM.readText();
            System.out.println("Recieved: " + model.strNetText);

            String[] strSplit = model.strNetText.split(",");

            if (strSplit[0].equals("Joined")) {
                model.blnConnected = true;
                model.connectSSM.sendText("Start");
                System.out.println("Client joined");

                view.setMainVisible(false);
                view.setConnectVisible(false);
                view.setSelectionVisible(true);
                view.thePanel.revalidate();
                view.thePanel.repaint();
            } else if (strSplit[0].equals("Start")) {
                model.blnConnected = true;
                System.out.println("Server started game");
            } else if (strSplit[0].equals("LIVE")) {
                String strType = strSplit[1];
                String strName = strSplit[2];

                if (strType.equals("K")) {
                    if (model.intPicking == 1) model.strP2K = strName;
                    else model.strP1K = strName;
                    view.pickedKP2Label.setText("Opponent Keeper: " + strName);
                } else if (strType.equals("S")) {
                    if (model.intPicking == 1) model.strP2S = strName;
                    else model.strP1S = strName;
                    view.pickedSP2Label.setText("Opponent Striker: " + strName);
                }
            } else if (strSplit[0].equals("PICKS")) {
                model.strP2K = strSplit[1];
                model.intP2KAgi = Integer.parseInt(strSplit[2]);
                model.intP2KCvg = Integer.parseInt(strSplit[3]);

                model.strP2S = strSplit[4];
                model.intP2SAcc = Integer.parseInt(strSplit[5]);
                model.intP2SPwr = Integer.parseInt(strSplit[6]);

                model.blnP2K = true;
                model.blnP2S = true;
                model.blnReceivedPicks = true;

                System.out.println("Opponent picked");
            }
        }

        if (model.blnConnected && !view.K1Button.isVisible() && !view.confPickButton.isVisible()) {
            view.setMainVisible(false);
            view.setConnectVisible(false);
            view.setSelectionVisible(true);
            view.thePanel.revalidate();
            view.thePanel.repaint();
        }

        // Selection Handlers
        if (evt.getSource() == view.K1Button) selectKeeper(0);
        else if (evt.getSource() == view.K2Button) selectKeeper(1);
        else if (evt.getSource() == view.K3Button) selectKeeper(2);

        if (evt.getSource() == view.S1Button) selectStriker(0);
        else if (evt.getSource() == view.S2Button) selectStriker(1);
        else if (evt.getSource() == view.S3Button) selectStriker(2);

        // Confirmation lock-in
        if (evt.getSource() == view.confPickButton) {

            boolean blnReady = (model.intPicking == 1 && model.blnP1K && model.blnP1S) || (model.intPicking == 2 && model.blnP2K && model.blnP2S);

            if (blnReady) {
                String strMessage;
                if (model.intPicking == 1) {
                    strMessage = ("PICKS," + model.strP1K + "," + model.intP1KAgi + "," + model.intP1KCvg + "," + model.strP1S + "," + model.intP1SAcc + "," + model.intP1SPwr);
                } else if (model.intPicking == 2) {
                    strMessage = ("PICKS," + model.strP2K + "," + model.intP2KAgi + "," + model.intP2KCvg + "," + model.strP2S + "," + model.intP2SAcc + "," + model.intP2SPwr);
                } else {
                    strMessage = ("invalid");
                }

                if (model.blnReceivedPicks) {
                    view.pickLabel.setText("Both locked in! Starting game...");
                } else {
                    view.pickLabel.setText("Waiting for other player...");
                }

                model.connectSSM.sendText(strMessage);
                model.blnSentPicks = true;
            }
        }

        if (model.blnSentPicks && model.blnReceivedPicks) {
            System.out.println("----------------------------------------");
            System.out.println("Both ready! Proceed to comparison gameplay logic.");
        }

        if (evt.getSource() == view.helpButton) {
            view.setMainVisible(false);
            view.setConnectVisible(false);
            view.setSelectionVisible(false);
            view.setHelpVisible(true);
            view.thePanel.repaint();
        }

        if (evt.getSource() == view.backButton) {
            view.setHelpVisible(false);
            view.setConnectVisible(false);
            view.setSelectionVisible(false);
            view.setMainVisible(true);
            view.thePanel.repaint();
        }
    }

    //constructor
    public SoccerController(SoccerModel model, SoccerView view) {
        this.model = model;
        this.view = view;
        attachListeners();
        updateButtonLabels();
    }

    //main method
    public static void main(String[] args) {
        SoccerModel model = new SoccerModel();
        SoccerView view = new SoccerView(model);
        new SoccerController(model, view);  //implements the frontend and backend
    }
}
