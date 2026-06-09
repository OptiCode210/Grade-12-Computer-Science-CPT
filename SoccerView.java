import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

// Frontend view for the soccer game (Jcomponents)
public class SoccerView extends JPanel {  
    //properties  
    // Window variables
    public JFrame theFrame = new JFrame("PENALTY!");
    public JPanel thePanel;

    // Main menu variables
    public JButton helpButton = new JButton("Help");
    public JButton playButton = new JButton("Play");
    public JLabel titleLabel = new JLabel("Penalty Shootout");
    public JComponent[] mainMenu;

    // Help menu variables
    public JLabel helpTitleLabel = new JLabel("How to Play");
    public JLabel helpLabel = new JLabel("<html> Striker: <br>"+
        "There are 3 sliders seen on the right side of the screen. <br>"+
        "Left/Right: Determins how far left or right the ball goes. Press space locking in your decision <br>"+
        "Up/Down: Determins how high or low the ball goes. Press space locking in your decision <br>"+
        "Power: Determins how hard the shot is. Press space locking in your decision"+
        "There are 2 abilities for each striker. <br>"+
        "Accuracy: Slows down the Left/Right and Up/Down sliders for better accuracy <br>"+
        "Power: Slows down the Power slider for better power selection"+
        "<br><br> Keeper: <br>"+
        "There are 2 sliders seen on the right side of the screen. <br>"+
        "Left/Right: Determins how far left or right the goalie jumps. Press space locking in your decision <br>"+
        "Power: Determins how high or low the goalie jumps. Press space locking in your decision"+
        "There are 2 abilities for each striker. <br>"+
        "Agility: Slows down the Left/Right and Up/Down sliders for better placment of your goalie <br>"+
        "Coverage: Expands the hitbox on your goalie <br>");
    public JButton backButton = new JButton("Back");
    public JComponent[] helpMenu;

    // Connection menu variables
    public JLabel serverLabel = new JLabel("Server");
    public JLabel clientLabel = new JLabel("Client");
    public JLabel connectTitleLabel = new JLabel("Connect");
    public JLabel IPLabel = new JLabel("IP: ");
    public JTextField serverField = new JTextField();
    public JTextField clientField = new JTextField();
    public JButton serverButton = new JButton("Host");
    public JButton clientButton = new JButton("Join");
    public JButton connectBackButton = new JButton("Back");
    public JComponent[] connectMenu;

    // Player selection variables
    public JButton K1Button = new JButton("Gianluigi DONNARUMA");
    public JButton K2Button = new JButton("James TRAFFORD");
    public JButton K3Button = new JButton("David DE GEA");
    public JButton S1Button = new JButton("Erling HAALAND ");
    public JButton S2Button = new JButton("Cristiano RONALDO");
    public JButton S3Button = new JButton("Lionel MESSI");
    public JButton confPickButton = new JButton("Confirm");

    public JLabel pickedKP1Label = new JLabel("Keeper: ");
    public JLabel pickedSP1Label = new JLabel("Striker: ");
    public JLabel pickedKP2Label = new JLabel("Keeper: ");
    public JLabel pickedSP2Label = new JLabel("Striker: ");


    public JLabel pickLabel = new JLabel("");
    public JComponent[] selectionMenu;

    //shooting mechanic
    public JLabel turnLabel = new JLabel("");
    public JLabel scoreLabel = new JLabel("");
    public JLabel lblLeftRight = new JLabel("Left / Right");
    public JLabel lblUpDown = new JLabel("Up / Down");
    public JLabel lblPower = new JLabel("Power");
    public JComponent[] gameMenu;

    //Images
    public BufferedImage S1Front, S1Run, S1Shoot, S1Stand, S1Card;
    public BufferedImage S2Front, S2Run, S2Shoot, S2Stand, S2Card;
    public BufferedImage S3Front, S3Run, S3Shoot, S3Stand, S3Card;
    public BufferedImage K1Left, K1Right, K1Stand, K1Card;
    public BufferedImage K2Left, K2Right, K2Stand, K2Card;
    public BufferedImage K3Left, K3Right, K3Stand, K3Card;
    public BufferedImage menuBG;
    public BufferedImage shootingBG;
    public BufferedImage goalImg;
    public BufferedImage ballImg;
    
    //Chat variable
	public JTextArea chatArea = new JTextArea();
	public JScrollPane chatScroll = new JScrollPane(chatArea);
	public JTextField chatField = new JTextField();

    //Takes the properties from the Soccer model class
    private SoccerModel model;

    

    //methods
    private void loadArtAssets() {
        //load images
        try {
            S1Front = ImageIO.read(new File("Images/Strikers/S1Front.gif"));
            S1Run   = ImageIO.read(new File("Images/Strikers/S1Run.gif"));
            S1Shoot = ImageIO.read(new File("Images/Strikers/S1Shoot.gif"));
            S1Stand = ImageIO.read(new File("Images/Strikers/S1Stand.gif"));
            S1Card  = ImageIO.read(new File("Images/player cards/S1Card.png"));

            S2Front = ImageIO.read(new File("Images/Strikers/S2Front.gif"));
            S2Run   = ImageIO.read(new File("Images/Strikers/S2Run.gif"));
            S2Shoot = ImageIO.read(new File("Images/Strikers/S2Shoot.gif"));
            S2Stand = ImageIO.read(new File("Images/Strikers/S2Stand.gif"));
            S2Card  = ImageIO.read(new File("Images/player cards/S2Card.png"));

            S3Front = ImageIO.read(new File("Images/Strikers/S3Front.gif"));
            S3Run   = ImageIO.read(new File("Images/Strikers/S3Run.gif"));
            S3Shoot = ImageIO.read(new File("Images/Strikers/S3Shoot.gif"));
            S3Stand = ImageIO.read(new File("Images/Strikers/S3Stand.gif"));
            S3Card  = ImageIO.read(new File("Images/player cards/S3Card.png"));

            K1Left  = ImageIO.read(new File("Images/keepers/K1Left.png"));
            K1Right = ImageIO.read(new File("Images/keepers/K1Right.png"));
            K1Stand = ImageIO.read(new File("Images/keepers/K1Stand.png"));
            K1Card  = ImageIO.read(new File("Images/player cards/K1Card.png"));

            K2Left  = ImageIO.read(new File("Images/keepers/K2Left.png"));
            K2Right = ImageIO.read(new File("Images/keepers/K2Right.png"));
            K2Stand = ImageIO.read(new File("Images/keepers/K2Stand.png"));
            K2Card  = ImageIO.read(new File("Images/player cards/K2Card.png"));

            K3Left  = ImageIO.read(new File("Images/keepers/K3Left.png"));
            K3Right = ImageIO.read(new File("Images/keepers/K3Right.png"));
            K3Stand = ImageIO.read(new File("Images/keepers/K3Stand.png"));
            K3Card  = ImageIO.read(new File("Images/player cards/K3Card.png"));

            shootingBG = ImageIO.read(new File("Images/shootingBG.jpeg"));
            goalImg = ImageIO.read(new File("Images/Goal.png"));
            ballImg = ImageIO.read(new File("Images/ball.png"));

            menuBG  = ImageIO.read(new File("Images/menuBG.jpg"));
        } catch (IOException e) {
            System.out.println("Image unable to be loaded: " + e);
        }
    }

    private void initializePositions() {
        //creates the starting menu

        // Main menu setup.
        // These components are visible when the program first opens.
        playButton.setSize(300, 70);
        playButton.setLocation(490, 260);
        playButton.setFont(new Font("Arial", Font.BOLD, 28));
        playButton.setBackground(new Color(30, 120, 60));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setOpaque(true);
        playButton.setBorderPainted(false);
        thePanel.add(playButton);

        helpButton.setSize(300, 70);
        helpButton.setLocation(490, 350);
        helpButton.setFont(new Font("Arial", Font.BOLD, 28));
        helpButton.setBackground(new Color(40, 70, 140));
        helpButton.setForeground(Color.WHITE);
        helpButton.setFocusPainted(false);
        helpButton.setOpaque(true);
        helpButton.setBorderPainted(false);
        thePanel.add(helpButton);

        titleLabel.setSize(1280, 100);
        titleLabel.setLocation(0, 110);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thePanel.add(titleLabel);

        mainMenu = new JComponent[]{playButton, helpButton, titleLabel};

        // Connect menu setup
        connectTitleLabel.setSize(1280, 80);
        connectTitleLabel.setLocation(0, 110);
        connectTitleLabel.setFont(new Font("Arial", Font.BOLD, 64));
        connectTitleLabel.setForeground(Color.WHITE);
        connectTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thePanel.add(connectTitleLabel);

        serverButton.setSize(400, 70);
        serverButton.setLocation(440, 280);
        serverButton.setFont(new Font("Arial", Font.BOLD, 28));
        serverButton.setBackground(new Color(30, 120, 60));
        serverButton.setForeground(Color.WHITE);
        serverButton.setFocusPainted(false);
        serverButton.setOpaque(true);
        serverButton.setBorderPainted(false);
        thePanel.add(serverButton);

        clientButton.setSize(400, 70);
        clientButton.setLocation(440, 380);
        clientButton.setFont(new Font("Arial", Font.BOLD, 28));
        clientButton.setBackground(new Color(40, 70, 140));
        clientButton.setForeground(Color.WHITE);
        clientButton.setFocusPainted(false);
        clientButton.setOpaque(true);
        clientButton.setBorderPainted(false);
        thePanel.add(clientButton);

        IPLabel.setSize(500, 30);
        IPLabel.setLocation(780, 10);
        IPLabel.setForeground(Color.WHITE);
        IPLabel.setFont(new Font("Arial", Font.BOLD, 20));
        IPLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thePanel.add(IPLabel);

        connectBackButton.setSize(180, 50);
        connectBackButton.setLocation(40, 40);
        connectBackButton.setFont(new Font("Arial", Font.BOLD, 22));
        connectBackButton.setBackground(new Color(40, 70, 140));
        connectBackButton.setForeground(Color.WHITE);
        connectBackButton.setFocusPainted(false);
        connectBackButton.setOpaque(true);
        connectBackButton.setBorderPainted(false);
        thePanel.add(connectBackButton);

        connectMenu = new JComponent[]{serverButton, clientButton, IPLabel, connectTitleLabel, connectBackButton};

        // Help menu setup
        helpTitleLabel.setSize(1280, 80);
        helpTitleLabel.setLocation(0, 120);
        helpTitleLabel.setFont(new Font("Arial", Font.BOLD, 64));
        helpTitleLabel.setForeground(Color.WHITE);
        helpTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thePanel.add(helpTitleLabel);

        helpLabel.setSize(1270, 520);
        helpLabel.setLocation(10, 200);
        helpLabel.setFont(new Font("Arial", Font.BOLD, 20));
        helpLabel.setForeground(Color.WHITE);
        thePanel.add(helpLabel);

        backButton.setSize(180, 50);
        backButton.setLocation(40, 40);
        backButton.setFont(new Font("Arial", Font.BOLD, 22));
        backButton.setBackground(new Color(40, 70, 140));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        thePanel.add(backButton);

        helpMenu = new JComponent[]{helpTitleLabel, helpLabel, backButton};

        // Selection menu setup
        S1Button.setSize(190, 25); S1Button.setLocation(24, 400); thePanel.add(S1Button);
        S2Button.setSize(190, 25); S2Button.setLocation(224, 400); thePanel.add(S2Button);
        S3Button.setSize(190, 25); S3Button.setLocation(424, 400); thePanel.add(S3Button);

        K1Button.setSize(190, 25); K1Button.setLocation(656, 400); thePanel.add(K1Button);
        K2Button.setSize(190, 25); K2Button.setLocation(856, 400); thePanel.add(K2Button);
        K3Button.setSize(190, 25); K3Button.setLocation(1056, 400); thePanel.add(K3Button);

        pickLabel.setSize(400, 40);
        pickLabel.setLocation(10, 10);
        pickLabel.setForeground(Color.WHITE);
        pickLabel.setFont(new Font("Arial", Font.BOLD, 28));
        thePanel.add(pickLabel);

        pickedKP1Label.setSize(500, 30); pickedKP1Label.setLocation(20, 610); thePanel.add(pickedKP1Label);
        pickedKP1Label.setForeground(Color.WHITE); pickedKP1Label.setFont(new Font("Arial", Font.BOLD, 20));

        pickedSP1Label.setSize(500, 30); pickedSP1Label.setLocation(20, 645); thePanel.add(pickedSP1Label);
        pickedSP1Label.setForeground(Color.WHITE); pickedSP1Label.setFont(new Font("Arial", Font.BOLD, 20));

        pickedKP2Label.setSize(450, 30); pickedKP2Label.setLocation(500, 600); thePanel.add(pickedKP2Label);
        pickedKP2Label.setForeground(Color.WHITE); pickedKP2Label.setFont(new Font("Arial", Font.BOLD, 20));

        pickedSP2Label.setSize(450, 30); pickedSP2Label.setLocation(500, 635); thePanel.add(pickedSP2Label);
        pickedSP2Label.setForeground(Color.WHITE); pickedSP2Label.setFont(new Font("Arial", Font.BOLD, 20));

        confPickButton.setSize(180, 40);
        confPickButton.setLocation(1050, 615);
        thePanel.add(confPickButton);

        selectionMenu = new JComponent[]{
            K1Button, K2Button, K3Button, S1Button, S2Button, S3Button,
            pickLabel, confPickButton, pickedKP1Label, pickedSP1Label,
            pickedKP2Label, pickedSP2Label
        };

        //shooting mechanic setup
        turnLabel.setSize(500, 40);
        turnLabel.setLocation(20, 20);
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 28));
        thePanel.add(turnLabel);

        scoreLabel.setSize(500, 40);
        scoreLabel.setLocation(20, 60);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        thePanel.add(scoreLabel);

        lblLeftRight.setBounds(1020, 105, 200, 30);
        lblLeftRight.setForeground(Color.WHITE);
        lblLeftRight.setFont(new Font("Arial Black", Font.PLAIN, 18));
        thePanel.add(lblLeftRight);

        lblUpDown.setBounds(1085, 195, 200, 30);
        lblUpDown.setForeground(Color.WHITE);
        lblUpDown.setFont(new Font("Arial Black", Font.PLAIN, 18));
        thePanel.add(lblUpDown);

        lblPower.setBounds(1020, 485, 200, 30);
        lblPower.setForeground(Color.WHITE);
        lblPower.setFont(new Font("Arial Black", Font.PLAIN, 18));
        thePanel.add(lblPower);

        gameMenu = new JComponent[]{turnLabel, scoreLabel, lblLeftRight, lblUpDown, lblPower};
        setGameVisible(false);

        //Set all the booleans to true 
        setMainVisible(true);
        setConnectVisible(false);
        setSelectionVisible(false);
        setHelpVisible(false);
    }

    public void setGameVisible(boolean b){
        for (JComponent c : gameMenu) {
            c.setVisible(b);
        }
        chatScroll.setVisible(b);
		chatField.setVisible(b);
    }

    public void setMainVisible(boolean b) { 
        //shortcut to set main menu components visibility
        for(JComponent c: mainMenu){
            c.setVisible(b);
        }
    }

    public void setConnectVisible(boolean b){
        //shortcut to set connect menu components visibility
        for(JComponent c: connectMenu){
            c.setVisible(b);
        } 
    }

    public void setSelectionVisible(boolean b){ 
        //shortcut to set select menu components visibility
        for(JComponent c: selectionMenu){
             c.setVisible(b);
        } 
    }

    public void setHelpVisible(boolean b){ 
        for(JComponent c: helpMenu){
             c.setVisible(b); 
        }
    }

    private void setupPanel() {
        thePanel = new JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw the menu background first so buttons and labels appear on top
                if (menuBG != null) {
                    g.drawImage(menuBG, 0, 0, 1280, 720, null);
                }

                if (SoccerView.this.model.isPlayAnimationRunning()) {
                    drawPlayAnimation(g);
                    return;
                }

                if (SoccerView.this.model.shouldLocalViewShowShooting()) {
                    // The local striker sees the shooting screen.
                    // After shooting, this screen can remain as a waiting/result view.
                    drawShootingGame(g);
                    return;
                } else if (SoccerView.this.model.shouldLocalViewShowGoalie()) {
                    // The opponent sees the goalie screen while the striker shoots.
                    // The goalie sliders only move when the controller says it is goalie input time.
                    drawGoalieGame(g);
                    return;
                }

                // Once the players are connected, draw the available player cards
                if (SoccerView.this.model.blnConnected) {
                    if (S1Card != null) g.drawImage(S1Card, 24, 100, 190, 240, null);
                    if (S2Card != null) g.drawImage(S2Card, 224, 100, 190, 240, null);
                    if (S3Card != null) g.drawImage(S3Card, 424, 100, 190, 240, null);
                    if (K1Card != null) g.drawImage(K1Card, 656, 100, 190, 240, null);
                    if (K2Card != null) g.drawImage(K2Card, 856, 100, 190, 240, null);
                    if (K3Card != null) g.drawImage(K3Card, 1056, 100, 190, 240, null);
                }
                // Configure Chat Components
				chatArea.setEditable(false);
				chatArea.setLineWrap(true);
				chatArea.setWrapStyleWord(true);

				// Position chat scroll pane on the left side (X: 20, Y: 150)
				chatScroll.setSize(220, 350);
				chatScroll.setLocation(20, 150);
				chatScroll.setVisible(false); // Initially hidden on menus
				thePanel.add(chatScroll);

				// Position chat text field right below the scroll pane
				chatField.setSize(220, 40);
				chatField.setLocation(20, 510);
				chatField.setVisible(false); // Initially hidden on menus
				thePanel.add(chatField);
            }
        };
    }

	public void drawShootingGame(Graphics g) {
		if (shootingBG != null) g.drawImage(shootingBG, 0, 0, 1280, 720, null);
		if (goalImg != null) g.drawImage(goalImg, 130, 160, null);
		if (ballImg != null) g.drawImage(ballImg, model.intBallX, model.intBallY, null);

		// New stylized scoreboard drawing
		drawProfessionalScoreboard(g);
		drawShotMeters(g);
	}

    public void drawGoalieGame(Graphics g) {
		BufferedImage keeperImage = getSavingKeeperImage();

		if (shootingBG != null) g.drawImage(shootingBG, 0, 0, 1280, 720, null);
		if (goalImg != null) g.drawImage(goalImg, 130, 160, null);
		if (keeperImage != null) g.drawImage(keeperImage, 450, 340, null);

		drawProfessionalScoreboard(g);
		drawGoalieMeters(g);
		if (model.dblFinalPowerPercent > 0.0) {
			
			int goalLeft = 315;
			int goalRight = 940;
			int goalTop = 275;
			int goalBottom = 500;

			// 1. Calculate base accurate coordinates
			int intBallTargetX = goalLeft + (int)((model.dblFinalLeftRightPercent / 100.0) * (goalRight - goalLeft));
			int intBallTargetY = goalTop + (int)((model.dblFinalUpDownPercent / 100.0) * (goalBottom - goalTop));

			// 2. Apply the single-generated random blur offset from the model
			intBallTargetX += model.intCircleBlurX;
			intBallTargetY += model.intCircleBlurY;

			// 3. Set the circle radius size based on opponent shot power
			int intMinRadius = 30; 
			int intCircleRadius = intMinRadius + (int)(model.dblFinalPowerPercent * 0.8);
			
			// 4. Center coordinates around the blurred position
			int intCircleX = intBallTargetX - intCircleRadius;
			int intCircleY = intBallTargetY - intCircleRadius; 
			
			// 5. Draw the blurred target area
			g.setColor(new Color(255, 0, 0, 85)); 
			g.fillOval(intCircleX, intCircleY, intCircleRadius * 2, intCircleRadius * 2);
			
			g.setColor(new Color(255, 0, 0, 220));
			g.drawOval(intCircleX, intCircleY, intCircleRadius * 2, intCircleRadius * 2);
		}

		if (model.blnShowHitbox && model.intGoalieStage == 3) {
			drawGoalieHitbox(g);
    }
}

    public void drawPlayAnimation(Graphics g) {
        BufferedImage strikerImage = getShootingStrikerImage();
        BufferedImage keeperImage = getAnimatingKeeperImage();

        if (shootingBG != null) g.drawImage(shootingBG, 0, 0, 1280, 720, null);
        if (goalImg != null) g.drawImage(goalImg, 130, 160, null);
        if (keeperImage != null) g.drawImage(keeperImage, model.intAnimGoalieX, model.intAnimGoalieY, null);
        if (ballImg != null) g.drawImage(ballImg, model.intAnimBallX, model.intAnimBallY, null);
        if (strikerImage != null) g.drawImage(strikerImage, model.intAnimStrikerX, model.intAnimStrikerY, null);

        drawProfessionalScoreboard(g);
    }

    private BufferedImage getShootingStrikerImage() {
        String strStrikerName;

        if (model.intGamePhase == 2) {
            strStrikerName = model.strP1S;
        } else {
            strStrikerName = model.strP2S;
        }

        if (strStrikerName != null && strStrikerName.equals(model.strikers[1][0])) {
            if (model.blnAnimShowShoot) {
                return S2Shoot;
            } else if (model.blnAnimShowRun) {
                return S2Run;
            }

            return S2Stand;
        } else if (strStrikerName != null && strStrikerName.equals(model.strikers[2][0])) {
            if (model.blnAnimShowShoot) {
                return S3Shoot;
            } else if (model.blnAnimShowRun) {
                return S3Run;
            }

            return S3Stand;
        }

        if (model.blnAnimShowShoot) {
            return S1Shoot;
        } else if (model.blnAnimShowRun) {
            return S1Run;
        }

        return S1Stand;
    }

    private BufferedImage getAnimatingKeeperImage() {
        String strKeeperName;
        boolean blnDiveRight = model.dblGoalieFinalLeftRightPercent >= 50.0;

        if (model.intGamePhase == 2) {
            strKeeperName = model.strP2K;
        } else {
            strKeeperName = model.strP1K;
        }

        if (strKeeperName != null && strKeeperName.equals(model.keepers[1][0])) {
            if (model.blnAnimShowDive) {
                return blnDiveRight ? K2Right : K2Left;
            }

            return K2Stand;
        } else if (strKeeperName != null && strKeeperName.equals(model.keepers[2][0])) {
            if (model.blnAnimShowDive) {
                return blnDiveRight ? K3Right : K3Left;
            }

            return K3Stand;
        }

        if (model.blnAnimShowDive) {
            return blnDiveRight ? K1Right : K1Left;
        }

        return K1Stand;
    }

    private BufferedImage getSavingKeeperImage() {
        String strKeeperName;

        if (model.intGamePhase == 2) {
            strKeeperName = model.strP2K;
        } else {
            strKeeperName = model.strP1K;
        }

        if (strKeeperName != null && strKeeperName.equals(model.keepers[1][0])) {
            return K2Stand;
        } else if (strKeeperName != null && strKeeperName.equals(model.keepers[2][0])) {
            return K3Stand;
        }

        return K1Stand;
    }

	private void drawProfessionalScoreboard(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Track who is shooting based on the game phase
		boolean isP1Active = (model.intGamePhase == 1 || model.intGamePhase == 2);

		int rowHeight = 39;
		int p1Y = 40;
		int p2Y = 81;

		// 1. Background Bar (Fixed size)
		g2.setColor(new Color(20, 20, 25, 200));
		g2.fillRect(40, 40, 260, 80);

		// 2. Team Name Blocks (The neon green accent)
		g2.setColor(new Color(180, 255, 50)); 
		g2.fillRect(40, p1Y, 100, rowHeight); // Player 1 block
		g2.fillRect(40, p2Y, 100, rowHeight); // Player 2 block

		// 3. Text & Score Styling (Fixed font size)
		g2.setFont(new Font("Arial", Font.BOLD, 24));
		
		// Player 1 Row Text
		g2.setColor(Color.BLACK);
		g2.drawString("P1", 75, 69);
		g2.setColor(Color.WHITE);
		g2.drawString("" + model.intP1Score, 185, 69);

		// Player 2 Row Text
		g2.setColor(Color.BLACK);
		g2.drawString("P2", 75, 110);
		g2.setColor(Color.WHITE);
		g2.drawString("" + model.intP2Score, 185, 110);

		// 4. Draw Indicator Arrow for the active shooter
		g2.setColor(new Color(180, 255, 50)); // Match the neon green accent
		
		// Create an arrow shape pointing left
		int[] arrowX = {245, 235, 245};
		int[] arrowY = new int[3];
		
		if (isP1Active) {
			arrowY[0] = 50;  // Top point
			arrowY[1] = 60;  // Left point (tip)
			arrowY[2] = 70;  // Bottom point
		} else {
			arrowY[0] = 91;  // Top point
			arrowY[1] = 101; // Left point (tip)
			arrowY[2] = 111; // Bottom point
		}
		
		g2.fillPolygon(arrowX, arrowY, 3);

		// 5. Header Label Above Scoreboard
		g2.setFont(new Font("Arial", Font.PLAIN, 14));
		g2.setColor(Color.LIGHT_GRAY);
		g2.drawString("PENALTY SHOOTOUT", 40, 35);
	}

    public void drawShotMeters(Graphics g){
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
        g.fillRect(model.intLeftRightLineX - 2, 140, 4, 40);

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
        g.fillRect(1120, model.intUpDownLineY - 2, 40, 4);

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
            int intRed = (int) (dblProgress * 255);
            int intGreen = (int) (255 - (dblProgress * 255));

            g.setColor(new Color(intRed, intGreen, 30));
            g.fillRect(intPowerX + (i * intSegmentWidth) + 2, intPowerY + 2, intSegmentWidth - 4, intPowerHeight - 4);
        }

        g.setColor(Color.BLACK);
        g.fillRect(intPowerX, intPowerY, intPowerWidth, 4);
        g.fillRect(intPowerX, intPowerY + intPowerHeight - 4, intPowerWidth, 4);
        g.fillRect(intPowerX, intPowerY, 4, intPowerHeight);
        g.fillRect(intPowerX + intPowerWidth - 4, intPowerY, 4, intPowerHeight);

        g.setColor(Color.WHITE);
        g.fillRect(model.intPowerLineX - 2, intPowerY, 4, intPowerHeight);
        
    }

    public void drawGoalieMeters(Graphics g) {
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
        g.fillRect(model.intGoalieLeftRightLineX - 2, 140, 4, 40);

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
        g.fillRect(1120, model.intGoalieUpDownLineY - 2, 40, 4);
    }

    public void drawGoalieHitbox(Graphics g) {
        //method for visualizing the hitbox
        int goalLeft = 315;
        int goalRight = 940;
        int goalTop = 275;
        int goalBottom = 500;

        int intCoverage = model.getCurrentKeeperCoverage();
        double dblHitboxWidthPercent = 18 + (intCoverage * 2);
        double dblHitboxHeightPercent = 28 + (intCoverage * 2);

        int intGoalieCenterX = goalLeft + (int)(model.dblGoalieFinalLeftRightPercent / 100.0 * (goalRight - goalLeft));
        int intGoalieCenterY = goalTop + (int)(model.dblGoalieFinalUpDownPercent / 100.0 * (goalBottom - goalTop));

        int intHitboxWidth = (int)((dblHitboxWidthPercent / 100.0) * (goalRight - goalLeft));
        int intHitboxHeight = (int)((dblHitboxHeightPercent / 100.0) * (goalBottom - goalTop));

        g.setColor(new Color(0, 255, 0, 90));
        g.fillRect(
            intGoalieCenterX - intHitboxWidth / 2,
            intGoalieCenterY - intHitboxHeight / 2,
            intHitboxWidth,
            intHitboxHeight
        );
    }

    public void winningAnimation(Graphics g){

    }

    // Constructor
    public SoccerView(SoccerModel model) {
        super();
        this.model = model;

        //graphics
        setupPanel();
        
        //Start window
        theFrame.setLayout(null);
        theFrame.setPreferredSize(new Dimension(1280, 720));
            
        thePanel.setLayout(null);
        thePanel.setPreferredSize(new Dimension(1280, 720));

        loadArtAssets();
        initializePositions();

        //Finish window
        theFrame.setContentPane(thePanel);
        theFrame.setSize(1280, 720);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.pack();
        theFrame.setResizable(false);
        theFrame.setVisible(true);
    }
}
