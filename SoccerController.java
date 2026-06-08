import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.*;
import java.io.File;

//connection
public class SoccerController implements ActionListener, KeyListener {
    //properties
    private SoccerModel model;
    private SoccerView view;

    int fps = 60;
    private Timer theTimer = new Timer(1000 / fps, this);


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

    private String displayPick(String strPick) {
        if (strPick == null || strPick.equals("")) {
            return "";
        }
        return strPick;
    }

    private void refreshSelectionLabels() {
        view.pickedKP1Label.setText("Keeper: " + displayPick(model.strP1K));
        view.pickedSP1Label.setText("Striker: " + displayPick(model.strP1S));
        view.pickedKP2Label.setText("Keeper: " + displayPick(model.strP2K));
        view.pickedSP2Label.setText("Striker: " + displayPick(model.strP2S));
    }

    private void updatePickStatus() {
        if (model.blnSentPicks && model.blnReceivedPicks) {
            view.pickLabel.setText("Both locked in!");

            startGameplay();    //starts main game

        } else if (model.blnSentPicks) {
            view.pickLabel.setText("Waiting for other player...");
        }
    }

    private void startPlayAnimation(boolean blnShotSaved) {
        model.blnShotSaved = blnShotSaved;
        model.blnResultReady = true;
        boolean blnGoalScoredThisTurn = false;

        if (!model.blnShotSaved && !model.blnResultScored) {
            if (model.intGamePhase == 2) {
                model.intP1Score++;
            } else if (model.intGamePhase == 4) {
                model.intP2Score++;
            }

            model.blnResultScored = true;
            blnGoalScoredThisTurn = true;
        }

        if (model.blnShotSaved) {
            System.out.println("Shot saved");
        } else {
            System.out.println("Goal scored");
        }

        if (blnGoalScoredThisTurn &&
            !model.blnWinningAnimationPrinted &&
            (model.intP1Score >= model.intWinningScore || model.intP2Score >= model.intWinningScore)) {
            System.out.println("winning animation here");
            model.blnWinningAnimationPrinted = true;
        }

        model.startPlayAnimation();
        view.thePanel.repaint();
    }

    private void finishPlayAnimation() {
        if (model.intGamePhase == 2) {
            model.intGamePhase = 3;
        } else if (model.intGamePhase == 4) {
            model.intGamePhase = 1;
        }

        model.blnResultReady = false;
        model.blnResultScored = false;
        model.resetShot();
        model.resetGoalie();
        view.lblLeftRight.setVisible(true);
        view.lblUpDown.setVisible(true);
        view.lblPower.setVisible(model.shouldLocalViewShowShooting());
    }

    private void sendShotData() {
        if (model.connectSSM != null) {
            model.connectSSM.sendText("SHOT," + model.dblFinalLeftRightPercent + "," + model.dblFinalUpDownPercent + "," + model.dblFinalPowerPercent);
        }
    }

    private void sendAnimationData() {
        if (model.connectSSM != null) {
            // Send one full animation packet after both players have finished input.
            // This keeps host and client using the same shot, save, and result values.
            String strAnimMessage = "ANIM," + model.blnShotSaved + "," +
                model.dblFinalLeftRightPercent + "," +
                model.dblFinalUpDownPercent + "," +
                model.dblFinalPowerPercent + "," +
                model.dblGoalieFinalLeftRightPercent + "," +
                model.dblGoalieFinalUpDownPercent;

            model.connectSSM.sendText(strAnimMessage);
        }
    }

    private void advanceToGoaliePhase() {
        if (model.intGamePhase == 1) {
            model.intGamePhase = 2;
        } else if (model.intGamePhase == 3) {
            model.intGamePhase = 4;
        }

        model.resetGoalie();
        view.lblPower.setVisible(false);

        if (model.connectSSM != null) {
            model.connectSSM.sendText("PHASE," + model.intGamePhase);
        }
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
        } else if (model.intPicking == 2) { //player 2
            model.strP2S = model.strikerName;
            model.intP2SAcc = model.strikerAccuracy;
            model.intP2SPwr = model.strikerPower;
            model.blnP2S = true;
        }

        refreshSelectionLabels();

        //checks if selection works
        if (model.connectSSM != null) { 
            model.connectSSM.sendText("LIVE,S," + model.strikerName);
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
        } else if (model.intPicking == 2) { //player 2
            model.strP2K = model.keeperName;
            model.intP2KAgi = model.keeperAgility;
            model.intP2KCvg = model.keeperCoverage;
            model.blnP2K = true;
        }

        refreshSelectionLabels();

        //checks if selection works
        if (model.connectSSM != null) {
            model.connectSSM.sendText("LIVE,K," + model.keeperName);
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
            model.intPicking = 1;
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
                model.intPicking = 2;
                model.connectSSM = new SuperSocketMaster(model.strServerID, 6112, this);

                final SuperSocketMaster attemptedConnection = model.connectSSM;

                // Run the connection  on a background thread so the window does not freeze
                new Thread(new Runnable() {
                    public void run() {
                        final boolean blnSuccess = attemptedConnection.connect();

                        // Swing components must be updated on the Swing event thread.
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                if (blnSuccess) {   //connection successful
                                    System.out.println("CONNECTED");
                                    attemptedConnection.sendText("Joined");
                                } else {    //connection failed
                                    model.blnConnected = false;
                                    model.connectSSM = null;
                                    JOptionPane.showMessageDialog(view.theFrame, "Could not connect. Try the IP again.");
                                }
                            }
                        });
                    }
                }).start();
            }
        }

        // Network Data Interception
        if (model.connectSSM != null && evt.getSource() == model.connectSSM) {
            //if network is connected
            model.strNetText = model.connectSSM.readText();
            System.out.println("Recieved: " + model.strNetText);

            String[] strSplit = model.strNetText.split(",");

            if (strSplit[0].startsWith("Chat:")) {
                String cleanMsg = model.strNetText.substring(5);
                view.chatArea.append("Opponent: " + cleanMsg + "\n");
            } else if (strSplit[0].equals("Joined")) {
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
                    if (model.intPicking == 1) {
                        model.strP2K = strName;
                    } else {
                        model.strP1K = strName;
                    }
                } else if (strType.equals("S")) {
                    if (model.intPicking == 1) {
                        model.strP2S = strName;
                    } else {
                        model.strP1S = strName;
                    }
                }
                refreshSelectionLabels();
            } else if (strSplit[0].equals("PICKS")) {
                if (model.intPicking == 1) {
                    model.strP2K = strSplit[1];
                    model.intP2KAgi = Integer.parseInt(strSplit[2]);
                    model.intP2KCvg = Integer.parseInt(strSplit[3]);

                    model.strP2S = strSplit[4];
                    model.intP2SAcc = Integer.parseInt(strSplit[5]);
                    model.intP2SPwr = Integer.parseInt(strSplit[6]);

                    model.blnP2K = true;
                    model.blnP2S = true;
                } else {
                    model.strP1K = strSplit[1];
                    model.intP1KAgi = Integer.parseInt(strSplit[2]);
                    model.intP1KCvg = Integer.parseInt(strSplit[3]);

                    model.strP1S = strSplit[4];
                    model.intP1SAcc = Integer.parseInt(strSplit[5]);
                    model.intP1SPwr = Integer.parseInt(strSplit[6]);

                    model.blnP1K = true;
                    model.blnP1S = true;
                }
                model.blnReceivedPicks = true;
                refreshSelectionLabels();
                updatePickStatus();

                System.out.println("Opponent picked");
            } else if (strSplit[0].equals("PHASE")) {
                // Sync the game phase from the other computer.
                // This lets the goalie computer start its goalie sliders after the striker shoots.
                model.intGamePhase = Integer.parseInt(strSplit[1]);

                if (model.intGamePhase == 2 || model.intGamePhase == 4) {
                    // Reset goalie sliders exactly when the goalie input phase begins.
                    model.resetGoalie();
                }

                view.thePanel.repaint();
            } else if (strSplit[0].equals("SHOT")) {
                model.dblFinalLeftRightPercent = Double.parseDouble(strSplit[1]);
                model.dblFinalUpDownPercent = Double.parseDouble(strSplit[2]);
                model.dblFinalPowerPercent = Double.parseDouble(strSplit[3]);
                model.calculateTarget();
                model.intShotStage = 4;
                view.thePanel.repaint();
            } else if (strSplit[0].equals("ANIM")) {
                // Both computers use this same animation packet so the striker,
                // ball, and goalie all animate from the same saved shot data.
                model.blnShotSaved = Boolean.parseBoolean(strSplit[1]);

                // Shot slider values from the shooter.
                model.dblFinalLeftRightPercent = Double.parseDouble(strSplit[2]);
                model.dblFinalUpDownPercent = Double.parseDouble(strSplit[3]);
                model.dblFinalPowerPercent = Double.parseDouble(strSplit[4]);

                // Goalie slider values from the saver.
                model.dblGoalieFinalLeftRightPercent = Double.parseDouble(strSplit[5]);
                model.dblGoalieFinalUpDownPercent = Double.parseDouble(strSplit[6]);

                // Rebuild the exact same ball target before starting the animation.
                model.calculateTarget();
                startPlayAnimation(model.blnShotSaved);
            }
        }

        if (model.intGamePhase == 0 && model.blnConnected && !view.K1Button.isVisible() && !view.confPickButton.isVisible()) {
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

                model.connectSSM.sendText(strMessage);
                model.blnSentPicks = true;
                updatePickStatus();
            }
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
        
        //Chat features
        if (evt.getSource() == view.chatField) {
			// Send chat text across network if connection exists
			if (model.connectSSM != null) {
				model.connectSSM.sendText("Chat:" + view.chatField.getText());
				// Append your own text locally so you can see it
				view.chatArea.append("You: " + view.chatField.getText() + "\n");
				view.chatField.setText("");
			}
		}
		if (evt.getSource() == theTimer) {
            if (model.intGamePhase > 0) {
                if (model.isPlayAnimationRunning()) {
                    view.lblLeftRight.setVisible(false);
                    view.lblUpDown.setVisible(false);
                    view.lblPower.setVisible(false);

                    if (model.updatePlayAnimation()) {
                        finishPlayAnimation();
                    }
                } else if (model.isLocalShooterInputTurn() && !model.blnShooting) {
                    view.lblLeftRight.setVisible(true);
                    view.lblUpDown.setVisible(true);
                    view.lblPower.setVisible(model.shouldLocalViewShowShooting());

                    // Only the local striker's computer moves shooting sliders.
                    // The opponent's shooting input acts like null/no input.
                    model.moveShotSliders();
                } else if (model.isLocalGoalieInputTurn()) {
                    view.lblLeftRight.setVisible(true);
                    view.lblUpDown.setVisible(true);
                    view.lblPower.setVisible(model.shouldLocalViewShowShooting());

                    // Only the local goalie's computer moves goalie sliders.
                    // The striker/opponent computer sees a frozen waiting view.
                    model.moveGoalieSliders();
                }
                view.thePanel.repaint();
            }
        }

        if (evt.getSource() == view.playButton) {
		}   //play button
        // ... rest of your buttons and network code continues untouched below ...
    }

	public void startGameplay() {
		model.intGamePhase = 1;
		model.resetShot();
		model.resetGoalie();

		view.setMainVisible(false);
		view.setConnectVisible(false);
		view.setHelpVisible(false);
		view.setSelectionVisible(false);
		view.setGameVisible(true);
		view.pickLabel.setVisible(false);
		
		// Completely hide both old label text layers
		view.scoreLabel.setVisible(false); 
		view.turnLabel.setVisible(false); 
        view.lblPower.setVisible(true);
		
		theTimer.start();
		view.thePanel.revalidate();
		view.thePanel.repaint();
		view.thePanel.requestFocusInWindow();
	}

    public void keyReleased(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_SPACE && model.intGamePhase > 0) {
            if (model.isPlayAnimationRunning()) {
                return;
            }

            if (!model.isLocalShooterInputTurn() && !model.isLocalGoalieInputTurn()) {
                // If it is not this computer's turn, ignore spacebar completely.
                // This makes opponent input act like null/no input.
                return;
            }

            if (model.isLocalGoalieInputTurn()) {
                // Goalie spacebar input:
                // Stage 1 locks Left/Right, then Stage 2 locks Up/Down.
                if (model.intGoalieStage == 1) {
                    model.dblGoalieFinalLeftRightPercent = ((double)(model.intGoalieLeftRightLineX - 1020) / 240.0) * 100.0;
                    model.intGoalieStage = 2;
                } else if (model.intGoalieStage == 2) {
                    model.dblGoalieFinalUpDownPercent = ((double)(model.intGoalieUpDownLineY - 230) / 240.0) * 100.0;
                    model.intGoalieStage = 3;

                    // Once both goalie sliders are locked, use the model's
                    // coverage-based hitbox logic to decide SAVE or GOAL.
                    model.blnShotSaved = model.isShotSaved();

                    startPlayAnimation(model.blnShotSaved);

                    sendAnimationData();
                }
            } else if (model.intShotStage == 1) {
                // Striker spacebar input:
                // Stage 1 locks Left/Right, Stage 2 locks Up/Down, Stage 3 locks Power.
                model.dblFinalLeftRightPercent = ((double)(model.intLeftRightLineX - 1020) / 240.0) * 100.0;
                model.intShotStage = 2;
            } else if (model.intShotStage == 2) {
                model.dblFinalUpDownPercent = ((double)(model.intUpDownLineY - 230) / 240.0) * 100.0;
                model.intShotStage = 3;
            } else if (model.intShotStage == 3) {
                model.dblFinalPowerPercent = ((double)(model.intPowerLineX - 1020) / 240.0) * 100.0;
                model.calculateTarget();
                model.intShotStage = 4;
                sendShotData();
                advanceToGoaliePhase();
            }
        }
    }

    public void keyPressed(KeyEvent evt) {
    }

    public void keyTyped(KeyEvent evt) {
    }

    //constructor
    public SoccerController(SoccerModel model, SoccerView view) {
        this.model = model;
        this.view = view;
        view.thePanel.setFocusable(true);
        view.thePanel.addKeyListener(this);
        view.chatField.addActionListener(this);
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
