import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class soccerMain extends JPanel implements ActionListener {
    //Properties
    JFrame theFrame = new JFrame("PENALTY!");
    
    //lists
	String[][] strikers = new String[3][3];
	String[][] keepers = new String[3][3];

	//variables
	String strikerName;
	int strikerAccuracy;
	int strikerPower;

	String keeperName;
	int keeperAgility;
	int keeperCoverage;
    
    //Main menu
	JButton helpButton = new JButton("Help");
	JButton playButton = new JButton("Play");
	JLabel titleLabel = new JLabel("Penalty Shootout");
	JComponent[] mainMenu;
	
	//Help menu
	JLabel helpTitleLabel  = new JLabel("How to Play");
	JButton backButton = new JButton("Back");
	JComponent[] helpMenu;
	
	//Connect
	JLabel serverLabel = new JLabel("Server");
	JLabel clientLabel = new JLabel("Client");
	JLabel connectTitleLabel = new JLabel("Connect");
	JLabel IPLabel = new JLabel("IP: ");
	JTextField serverField = new JTextField();
	JTextField clientField = new JTextField();
	JButton serverButton = new JButton("Host");
	JButton clientButton = new JButton("Join");
	JButton connectBackButton = new JButton("Back");
	String strServerID;
	String strServerIP;
	JComponent[] connectMenu;
	boolean blnConnected = false;
	boolean blnSentPicks= false;
	boolean blnReceivedPicks  = false;
	String strIP;
	String strNetText;
	SuperSocketMaster connectSSM = null;
	
	//Picking player
	BufferedImage S1Front = null;
	BufferedImage S1Run = null;
	BufferedImage S1Shoot = null;
	BufferedImage S1Stand = null;
	BufferedImage S1Card = null;
	
	BufferedImage S2Front = null;
	BufferedImage S2Run = null;
	BufferedImage S2Shoot = null;
	BufferedImage S2Stand = null;
	BufferedImage S2Card = null;
	
	BufferedImage S3Front = null;
	BufferedImage S3Run = null;
	BufferedImage S3Shoot = null;
	BufferedImage S3Stand = null;
	BufferedImage S3Card = null;
	
	BufferedImage K1Left = null;
	BufferedImage K1Right = null;
	BufferedImage K1Stand = null;
	BufferedImage K1Card = null;
	
	BufferedImage K2Left = null;
	BufferedImage K2Right = null;
	BufferedImage K2Stand = null;
	BufferedImage K2Card = null;
	
	BufferedImage K3Left = null;
	BufferedImage K3Right = null;
	BufferedImage K3Stand = null;
	BufferedImage K3Card = null;
	
	BufferedImage menuBG = null;
	
	String strP1K;
	String strP1S;
	int intP1KAgi;
	int intP1KCvg;
	int intP1SPwr;
	int intP1SAcc;
	
	String strP2K;
	String strP2S;
	int intP2KAgi;
	int intP2KCvg;
	int intP2SPwr;
	int intP2SAcc;	
	
	int intPicking = 1;
	int intCount = 0;
	boolean blnP1S = false;
	boolean blnP1K = false;
	boolean blnP2S = false;
	boolean blnP2K = false;
	
	JButton K1Button = new JButton("Gianluigi DONNARUMA");
	JButton K2Button = new JButton("James TRAFFORD");
	JButton K3Button = new JButton("David DE GEA");
	JButton S1Button = new JButton("Erling HAALAND ");
	JButton S2Button = new JButton("Cristiano RONALDO");
	JButton S3Button = new JButton("Lionel MESSI");
	JButton confPickButton = new JButton("Confirm");
	
	JLabel pickedKP1Label = new JLabel("Keeper: ");
	JLabel pickedSP1Label = new JLabel("Striker: ");
	JLabel pickedKP2Label = new JLabel("Keeper: ");
	JLabel pickedSP2Label = new JLabel("Striker: ");
	
	JLabel pickLabel = new JLabel("");
	JLabel playerLabel = new JLabel("");
	JLabel KAgiLabel = new JLabel("");
	JLabel KCvgLabel = new JLabel("");
	JLabel SPwrLabel = new JLabel("");
	JLabel SAccLabel = new JLabel("");
	JComponent[] selectionMenu;

    //Methods
    	public void loadCSV(){
        try{
            //player file csv
            BufferedReader playersFile = new BufferedReader(new FileReader("players.csv"));
            String strLine;
            int count = 0;

            while((strLine = playersFile.readLine()) != null && count < strikers.length){
                String[] split = strLine.split(",");    //splis data with ","

                strikers[count][0] = split[0].trim();
                strikers[count][1] = split[1].trim();
                strikers[count][2] = split[2].trim();

                count++;
            }

            playersFile.close();

            //keeper file csv
            BufferedReader keepersFile = new BufferedReader(new FileReader("keepers.csv"));
            count = 0;

            while((strLine = keepersFile.readLine()) != null && count < keepers.length){
                String[] split = strLine.split(",");

                keepers[count][0] = split[0].trim();
                keepers[count][1] = split[1].trim();
                keepers[count][2] = split[2].trim();

                count++;
            }

            keepersFile.close();

        }catch(IOException e){
            System.out.println("File error");
        }
    }

	public void selectStriker(int intIndex){
		strikerName = strikers[intIndex][0];
		strikerAccuracy = Integer.parseInt(strikers[intIndex][1]);
		strikerPower = Integer.parseInt(strikers[intIndex][2]);

		if(intPicking == 1){
			strP1S = strikerName;
			intP1SAcc = strikerAccuracy;
			intP1SPwr = strikerPower;
			blnP1S = true;
			pickedSP1Label.setText("Striker: "+strikerName);
		}else if(intPicking == 2){
			strP2S = strikerName;
			intP2SAcc = strikerAccuracy;
			intP2SPwr = strikerPower;
			blnP2S = true;
			pickedSP2Label.setText("Striker: "+strikerName);
		}

		System.out.println("Name: " + strikerName);
		System.out.println("Accuracy: " + strikerAccuracy);
		System.out.println("Power: " + strikerPower);
		
		if(connectSSM != null){
			connectSSM.sendText("LIVE,S," + strikerName);
		}
	}

	public void selectKeeper(int intIndex){
		keeperName = keepers[intIndex][0];
		keeperAgility = Integer.parseInt(keepers[intIndex][1]);
		keeperCoverage = Integer.parseInt(keepers[intIndex][2]);

		if(intPicking == 1){
			strP1K = keeperName;
			intP1KAgi = keeperAgility;
			intP1KCvg = keeperCoverage;
			blnP1K = true;
			pickedKP1Label.setText("Keeper: "+keeperName);
		}else if(intPicking == 2){
			strP2K = keeperName;
			intP2KAgi = keeperAgility;
			intP2KCvg = keeperCoverage;
			blnP2K = true;
			pickedKP2Label.setText("Keeper: "+keeperName);
		}

		System.out.println("Name: " + keeperName);
		System.out.println("Agility: " + keeperAgility);
		System.out.println("Coverage: " + keeperCoverage);
		
		if(connectSSM != null){
			connectSSM.sendText("LIVE,K," + keeperName);
		}
	}
    
    //For action listener
	public void actionPerformed(ActionEvent evt){
		//Going into the play menu to connect to server
		if(evt.getSource() == playButton){
			setMainVisible(false);
			setConnectVisible(true);
		}
		
		//Back button from connect menu
		if(evt.getSource() == connectBackButton){
			setConnectVisible(false);
			setHelpVisible(false);
			setSelectionVisible(false);
			setMainVisible(true);
			thePanel.repaint();
			IPLabel.setText("IP: ");
		}
		
		//Connecting to server
		if(evt.getSource() == serverButton){
			connectSSM = new SuperSocketMaster(6112, this); 
			strIP = connectSSM.getMyAddress();
			System.out.println(strIP);
			IPLabel.setText("IP: "+strIP);
			strServerIP = connectSSM.getMyAddress();
			connectSSM.connect();
			System.out.println("Waiting for connection");
		}
		
		if(evt.getSource() == clientButton){	
			strServerID = JOptionPane.showInputDialog(theFrame, "Enter IP: ", "Connect", JOptionPane.PLAIN_MESSAGE);
			
			if(strServerID == null || strServerID.equals("")){
				JOptionPane.showMessageDialog(theFrame, "Please enter an IP address.");
			}else{
				connectSSM = new SuperSocketMaster(strServerID, 6112, this);
				System.out.println("Entered IP: "+strServerID);
				
				if(connectSSM.connect() == true){
					System.out.println("CONNECTED");
					
					connectSSM.sendText("Joined");
				}else{
					blnConnected = false;
					connectSSM = null;
					JOptionPane.showMessageDialog(theFrame, "Could not connect. Try the IP again.");
				}
			}
		}
		
		//Recieving network data
		if(evt.getSource() == connectSSM){
			strNetText = connectSSM.readText();
			System.out.println("Recieved: "+strNetText);
			
			String strSplit[] = strNetText.split(",");
			
			if(strSplit[0].equals("Joined")){
				blnConnected = true;
				connectSSM.sendText("Start");
				System.out.println("Client joined");
				
				//Loading selection screen for client 
				setMainVisible(false);
				setConnectVisible(false);
				setSelectionVisible(true);
				thePanel.repaint();
			}else if(strSplit[0].equals("Start")){
				blnConnected = true;
				System.out.println("Server started game");
			}else if(strSplit[0].equals("LIVE")) {
				String strType = strSplit[1];   
				String strName = strSplit[2];  
				
				if(strType.equals("K")) {
					if(intPicking == 1) strP2K = strName; 
					else strP1K = strName;
					pickedKP2Label.setText("Opponent Keeper: " + strName);
				} else if(strType.equals("S")) {
					if(intPicking == 1) strP2S = strName;
					else strP1S = strName;
					pickedSP2Label.setText("Opponent Striker: " + strName);
				}
			}else if(strSplit[0].equals("PICKS")){
				strP2K = strSplit[1];
				intP2KAgi = Integer.parseInt(strSplit[2]);
				intP2KCvg = Integer.parseInt(strSplit[3]);
				
				strP2S = strSplit[4];
				intP2SAcc = Integer.parseInt(strSplit[5]);
				intP2SPwr = Integer.parseInt(strSplit[6]);
				
				blnP2K = true;
				blnP2S = true;
				blnReceivedPicks = true;
				
				System.out.println("Opponent picked");
				System.out.println("Keeper: "+strP2K+"| Agility: "+intP2KAgi+"| Coverage"+intP2KCvg);
				System.out.println("Striker: "+strP2S+"| Accuracy: "+intP2SAcc+"| Power: "+intP2SPwr);
			}
		}
		
		//Going to player selection
		if(blnConnected == true){
			setMainVisible(false);
			setConnectVisible(false);
			setSelectionVisible(true);
			thePanel.repaint();
		}
		
		//Selections
		if(evt.getSource() == K1Button){
			selectKeeper(0);
		}else if(evt.getSource() == K2Button){
			selectKeeper(1);
		}else if(evt.getSource() == K3Button){
			selectKeeper(2);
		}
		
		if(evt.getSource() == S1Button){
			selectStriker(0);
		}else if(evt.getSource() == S2Button){
			selectStriker(1);
		}else if(evt.getSource() == S3Button){
			selectStriker(2);
		}
		
		//Finalizing and locking in picks
		if(evt.getSource() == confPickButton){
			boolean blnReady = (intPicking == 1 && blnP1K && blnP1S) || (intPicking == 2 && blnP2K && blnP2S);
			
			if(blnReady){
				String strMessage;
				if(intPicking == 1){
					strMessage = ("PICKS,"+strP1K+","+intP1KAgi+","+intP1KCvg+","+strP1S+","+intP1SAcc+","+intP1SPwr);
				}else if(intPicking == 2){
					strMessage = ("PICKS,"+strP2K+","+intP2KAgi+","+intP2KCvg+","+strP2S+","+intP2SAcc+","+intP2SPwr);
				}else{
					strMessage = ("invalid");
				}
				
				if(blnReceivedPicks == true){
					pickLabel.setText("Both locked in! Starting game...");
				}else{
					pickLabel.setText("Waiting for other player...");
				}
				
				System.out.println("Sent: "+strMessage);
				
				connectSSM.sendText(strMessage);
				blnSentPicks = true;
				pickLabel.setText("Waiting for opponent to lock in...");
				System.out.println("Sent Final Confirmation: "+strMessage);
			}
		}
		
		//When both sides have locked in
		if(blnSentPicks == true && blnReceivedPicks == true){
			System.out.println("----------------------------------------");
			System.out.println("Both ready! Proceed to comparison gameplay logic.");
		}
		
		//Going to help screen
		if(evt.getSource() == helpButton){
			setMainVisible(false);
			setConnectVisible(false);
			setSelectionVisible(false);
			setHelpVisible(true);
			thePanel.repaint();
		}
		
		//Back button from help screen
		if(evt.getSource() == backButton){
			setHelpVisible(false);
			setConnectVisible(false);
			setSelectionVisible(false);
			setMainVisible(true);
			thePanel.repaint();
		}
	}
		
	//Drawing the images
	JPanel thePanel = new JPanel(){
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			//Drawing images
			if(menuBG != null){
				g.drawImage(menuBG, 0, 0, 1280, 720, null);
			}	
			
			if(blnConnected == true){		
				if(blnConnected == true){
					if(menuBG != null){
						g.drawImage(menuBG, 0, 0, 1280, 720, null);
					}
				}						
				if(S1Card != null){
					g.drawImage(S1Card, 24, 100, 190, 240, null);
				}
		
				if(blnConnected == true){				
					if(S2Card != null){
						g.drawImage(S2Card, 224, 100, 190, 240, null);
					}
				}
				if(blnConnected == true){				
					if(S3Card != null){
						g.drawImage(S3Card, 424, 100, 190, 240, null);
					}
				}
				if(blnConnected == true){				
					if(K1Card != null){
						g.drawImage(K1Card, 656, 100, 190, 240, null);
					}
				}
				if(blnConnected == true){				
					if(K2Card != null){
						g.drawImage(K2Card, 856, 100, 190, 240, null);
					}
				}
				if(blnConnected == true){				
					if(K3Card != null){
						g.drawImage(K3Card, 1056, 100, 190, 240, null);
					}
				}		
			}
		}
	};
		
	//Setting menus to visible or invisible
	public void setMainVisible(boolean blnVisible){
		for(JComponent c:mainMenu){
			c.setVisible(blnVisible);
		}
	}
	
	public void setConnectVisible(boolean blnVisible){
		for(JComponent c:connectMenu){
			c.setVisible(blnVisible);
		}
	}

	public void setSelectionVisible(boolean blnVisible){
		for(JComponent c:selectionMenu){
			c.setVisible(blnVisible);
		}
	}
	
	public void setHelpVisible(boolean blnVisible){
		for(JComponent c:helpMenu){
			c.setVisible(blnVisible);
		}
	}

    //Constructor
    public soccerMain(){
        //Start window
        super();
		theFrame.setLayout(null);
		theFrame.setPreferredSize(new Dimension(1280, 720)); 
		thePanel.setLayout(null);
		thePanel.setPreferredSize(new Dimension(1280, 720));
		
		//Main menu
		playButton.setBounds(490, 260, 300, 70);
		playButton.setFont(new Font("Arial", Font.BOLD, 28));
		playButton.setBackground(new Color(30, 120, 60));
		playButton.setForeground(Color.WHITE);
		playButton.setFocusPainted(false);
		playButton.addActionListener(this);
		thePanel.add(playButton);
		
		helpButton.setBounds(490, 350, 300, 70);
		helpButton.setFont(new Font("Arial", Font.BOLD, 28));
		helpButton.setBackground(new Color(40, 70, 140));
		helpButton.setForeground(Color.WHITE);
		helpButton.setFocusPainted(false);
		helpButton.addActionListener(this);
		thePanel.add(helpButton);
		
		titleLabel.setBounds(0, 110, 1280, 100);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		thePanel.add(titleLabel);
		
		//Main menu pannel into a list
		mainMenu = new JComponent[]{
			playButton, helpButton, titleLabel
		};
				
		//Connect menu
		connectTitleLabel.setBounds(0, 110, 1280, 80);
		connectTitleLabel.setFont(new Font("Arial", Font.BOLD, 64));
		connectTitleLabel.setForeground(Color.WHITE);
		connectTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		thePanel.add(connectTitleLabel);
		
		serverButton.setBounds(440, 280, 400, 70);
		serverButton.setFont(new Font("Arial", Font.BOLD, 28));
		serverButton.setBackground(new Color(30, 120, 60));
		serverButton.setForeground(Color.WHITE);
		serverButton.setFocusPainted(false);
		serverButton.addActionListener(this);
		thePanel.add(serverButton);

		clientButton.setBounds(440, 380, 400, 70);
		clientButton.setFont(new Font("Arial", Font.BOLD, 28));
		clientButton.setBackground(new Color(40, 70, 140));
		clientButton.setForeground(Color.WHITE);
		clientButton.setFocusPainted(false);
		clientButton.addActionListener(this);
		thePanel.add(clientButton);
		
		IPLabel.setBounds(465, 505, 350, 36);
		IPLabel.setForeground(Color.WHITE);
		thePanel.add(IPLabel);
		
		serverButton.setBounds(330,260,300,100);
		serverButton.addActionListener(this);
		thePanel.add(serverButton);
		
		clientButton.setBounds(650,260,300,100);
		clientButton.addActionListener(this);
		thePanel.add(clientButton);
		
		IPLabel.setBounds(780, 10, 500, 30);
		IPLabel.setForeground(Color.WHITE);
		IPLabel.setFont(new Font("Arial", Font.BOLD, 20));
		IPLabel.setHorizontalAlignment(SwingConstants.CENTER);
		thePanel.add(IPLabel);
		
		connectBackButton.setBounds(40, 40, 180, 50);
		connectBackButton.setFont(new Font("Arial", Font.BOLD, 22));
		connectBackButton.setBackground(new Color(40, 70, 140));
		connectBackButton.setForeground(Color.WHITE);
		connectBackButton.setFocusPainted(false);
		connectBackButton.addActionListener(this);
		thePanel.add(connectBackButton);
	
		
		//Conect menu pannel into a list
		connectMenu = new JComponent[]{
			serverButton, clientButton, IPLabel, connectTitleLabel, connectBackButton
		};
		
		//Help menu
		helpTitleLabel.setBounds(0, 120, 1280, 80);
		helpTitleLabel.setFont(new Font("Arial", Font.BOLD, 64));
		helpTitleLabel.setForeground(Color.WHITE);
		helpTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		thePanel.add(helpTitleLabel);
		
		backButton.setBounds(40, 40, 180, 50);
		backButton.setFont(new Font("Arial", Font.BOLD, 22));
		backButton.setBackground(new Color(40, 70, 140));
		backButton.setForeground(Color.WHITE);
		backButton.setFocusPainted(false);
		backButton.addActionListener(this);
		thePanel.add(backButton);
		
		//Hel[p menu panel into a list
		helpMenu = new JComponent[]{
			helpTitleLabel, backButton
		};
		
		//Player stats
		loadCSV();
		
		K1Button.setText(keepers[0][0]);
		K2Button.setText(keepers[1][0]);
		K3Button.setText(keepers[2][0]);
		
		S1Button.setText(strikers[0][0]);
		S2Button.setText(strikers[1][0]);
		S3Button.setText(strikers[2][0]);	
		
		K1Button.addActionListener(this);
		K2Button.addActionListener(this);
		K3Button.addActionListener(this);
		
		S1Button.addActionListener(this);
		S2Button.addActionListener(this);
		S3Button.addActionListener(this);
						
		//Player selection images
		try{
			S1Front = ImageIO.read(new File("images/Strikers/S1Front.gif"));	
			S1Run = ImageIO.read(new File("images/Strikers/S1Run.gif"));	
			S1Shoot = ImageIO.read(new File("images/Strikers/S1Shoot.gif"));	
			S1Stand = ImageIO.read(new File("images/Strikers/S1Stand.gif"));	
			S1Card = ImageIO.read(new File("images/player cards/S1Card.png"));	
			
			S2Front = ImageIO.read(new File("images/Strikers/S2Front.gif"));	
			S2Run = ImageIO.read(new File("images/Strikers/S2Run.gif"));	
			S2Shoot = ImageIO.read(new File("images/Strikers/S2Shoot.gif"));	
			S2Stand = ImageIO.read(new File("images/Strikers/S2Stand.gif"));
			S2Card = ImageIO.read(new File("images/player cards/S2Card.png"));	
			
			S3Front = ImageIO.read(new File("images/Strikers/S3Front.gif"));	
			S3Run = ImageIO.read(new File("images/Strikers/S3Run.gif"));	
			S3Shoot = ImageIO.read(new File("images/Strikers/S3Shoot.gif"));	
			S3Stand = ImageIO.read(new File("images/Strikers/S3Stand.gif"));
			S3Card = ImageIO.read(new File("images/player cards/S3Card.png"));	
			
			K1Left = ImageIO.read(new File("images/keepers/K1Left.gif"));	
			K1Right = ImageIO.read(new File("images/keepers/K1Right.gif"));	
			K1Stand = ImageIO.read(new File("images/keepers/K1Stand.gif"));		
			K1Card = ImageIO.read(new File("images/player cards/K1Card.png"));	
			
			K2Left = ImageIO.read(new File("images/keepers/K2Left.gif"));	
			K2Right = ImageIO.read(new File("images/keepers/K2Right.gif"));	
			K2Stand = ImageIO.read(new File("images/keepers/K2Stand.gif"));	
			K2Card = ImageIO.read(new File("images/player cards/K2Card.png"));	
			
			K3Left = ImageIO.read(new File("images/keepers/K3Left.gif"));	
			K3Right = ImageIO.read(new File("images/keepers/K3Right.gif"));	
			K3Stand = ImageIO.read(new File("images/keepers/K3Stand.gif"));	
			K3Card = ImageIO.read(new File("images/player cards/K3Card.png"));	
			
			menuBG = ImageIO.read(new File("images/menuBG.jpg"));		
		}catch(IOException e){
			System.out.println("Image unable to be loaded");
			System.out.println(e);
		}
				
		//The UI
		S1Button.setBounds(24,400,190,25);
		thePanel.add(S1Button);
		S2Button.setBounds(224,400,190,25);
		thePanel.add(S2Button);
		S3Button.setBounds(424,400,190,25);
		thePanel.add(S3Button);
		
		K1Button.setBounds(656,400,190,25);
		thePanel.add(K1Button);
		K2Button.setBounds(856,400,190,25);
		thePanel.add(K2Button);
		K3Button.setBounds(1056,400,190,25);
		thePanel.add(K3Button);
		
		pickLabel.setBounds(10, 10, 400, 40);
		pickLabel.setForeground(Color.WHITE);
		pickLabel.setFont(new Font("Arial", Font.BOLD, 28));
		thePanel.add(pickLabel);
		
		pickedKP1Label.setBounds(20, 610, 500, 30);
		pickedKP1Label.setForeground(Color.WHITE);
		pickedKP1Label.setFont(new Font("Arial", Font.BOLD, 20));
		thePanel.add(pickedKP1Label);
		
		pickedSP1Label.setBounds(20, 645, 500, 30);
		pickedSP1Label.setForeground(Color.WHITE);
		pickedSP1Label.setFont(new Font("Arial", Font.BOLD, 20));
		thePanel.add(pickedSP1Label);
		
		pickedKP2Label.setBounds(500, 600, 450, 30);
		pickedKP2Label.setForeground(Color.WHITE);
		pickedKP2Label.setFont(new Font("Arial", Font.BOLD, 20));
		thePanel.add(pickedKP2Label);
		
		pickedSP2Label.setBounds(500, 635, 450, 30);
		pickedSP2Label.setForeground(Color.WHITE);
		pickedSP2Label.setFont(new Font("Arial", Font.BOLD, 20));
		thePanel.add(pickedSP2Label);
		
		confPickButton.setBounds(1050, 615, 180, 40);
		confPickButton.addActionListener(this);
		thePanel.add(confPickButton);
		
		//Selection menu pannel into a list
		selectionMenu = new JComponent[]{
			K1Button, K2Button, K3Button, S1Button, S2Button, S3Button,
			playerLabel, KAgiLabel, KCvgLabel, SPwrLabel, SAccLabel, pickLabel,
			confPickButton, pickedKP1Label, pickedSP1Label, pickedKP2Label, pickedSP2Label
		};
		
		//Drawing main screen
		setMainVisible(true);
		setConnectVisible(false);
		setSelectionVisible(false);
		setHelpVisible(false);
								
        //Finish window
        theFrame.setContentPane(thePanel);
        theFrame.setSize(1280, 720);
		theFrame.setDefaultCloseOperation(3);
		theFrame.pack();
		theFrame.setResizable(false);
		theFrame.setVisible(true);
    }

    //Main method
	public static void main(String[] args){			
		new soccerMain();
	}
}
