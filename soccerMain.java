import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.IOException;
import java.io.File;
import arc.*;

public class soccerMain extends JPanel implements ActionListener {
    //Properties
    JFrame theFrame = new JFrame("PENALTY!");
    
    //Main menu
	JButton helpButton = new JButton("Help");
	JButton playButton = new JButton("Play");
	JComponent[] mainMenu;
	
	//Connect
	JLabel serverLabel = new JLabel("Server");
	JLabel clientLabel = new JLabel("Client");
	JLabel IPLabel = new JLabel("");
	JTextField serverField = new JTextField();
	JTextField clientField = new JTextField();
	JButton serverButton = new JButton("Host");
	JButton clientButton = new JButton("Join");
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

	String[][] strStrikers;
	String[][] strKeepers;
	
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
    //For action listener
	public void actionPerformed(ActionEvent evt){
		//Going into the play menu to connect to server
		if(evt.getSource() == playButton){
			setMainVisible(false);
			setConnectVisible(true);
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
			connectSSM = new SuperSocketMaster(strServerID, 6112, this);
			strServerIP = connectSSM.getMyAddress();
		
			//Access the connect method
			connectSSM.connect();
			blnConnected = true;
			System.out.println("CONNECTED");
			
			//Sending data to server
			connectSSM.sendText("Joined");
			
			//Loading selection screen for client 
			setMainVisible(false);
			setConnectVisible(false);
			setSelectionVisible(true);
			thePanel.repaint();
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
			}else if(strSplit[0].equals("Picks")){
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
		
		//Getting player stats for player 1(host)
		if(intPicking == 1){
			if(evt.getSource() == K1Button){
				strP1K = strKeepers[0][0];
				intP1KAgi = Integer.parseInt(strKeepers[0][1]);
				intP1KCvg = Integer.parseInt(strKeepers[0][2]);
				blnP1K = true;
				pickedKP1Label.setText("Keeper: "+strP1K);
				connectSSM.sendText("LIVE,K," + strP1K);
			}else if(evt.getSource() == K2Button){
				strP1K = strKeepers[1][0];
				intP1KAgi = Integer.parseInt(strKeepers[1][1]);
				intP1KCvg = Integer.parseInt(strKeepers[1][2]);
				blnP1K = true;
				pickedKP1Label.setText("Keeper: "+strP1K);
				connectSSM.sendText("LIVE,K," + strP1K);
			}else if(evt.getSource() == K3Button){
				strP1K = strKeepers[2][0];
				intP1KAgi = Integer.parseInt(strKeepers[2][1]);
				intP1KCvg = Integer.parseInt(strKeepers[2][2]);
				blnP1K = true;
				pickedKP1Label.setText("Keeper: "+strP1K);
				connectSSM.sendText("LIVE,K," + strP1K);
			}
			
			if(evt.getSource() == S1Button){
				strP1S = strStrikers[0][0];
				intP1SAcc = Integer.parseInt(strStrikers[0][1]);
				intP1SPwr = Integer.parseInt(strStrikers[0][2]);
				blnP1S = true;
				pickedSP1Label.setText("Striker: "+strP1S);
				connectSSM.sendText("LIVE,S," + strP1S);
			}else if(evt.getSource() == S2Button){
				strP1S = strStrikers[1][0];
				intP1SAcc = Integer.parseInt(strStrikers[1][1]);
				intP1SPwr = Integer.parseInt(strStrikers[1][2]);
				blnP1S = true;
				pickedSP1Label.setText("Striker: "+strP1S);
				connectSSM.sendText("LIVE,S," + strP1S);
			}else if(evt.getSource() == S3Button){
				strP1S = strStrikers[2][0];
				intP1SAcc = Integer.parseInt(strStrikers[2][1]);
				intP1SPwr = Integer.parseInt(strStrikers[2][2]);
				blnP1S = true;
				pickedSP1Label.setText("Striker: "+strP1S);
				connectSSM.sendText("LIVE,S," + strP1S);
			}
		}				
				
		//Getting player stats for player 2(client)
		else if(intPicking == 2){
			if(evt.getSource() == K1Button){
				strP2K = strKeepers[0][0];
				intP2KAgi = Integer.parseInt(strKeepers[0][1]);
				intP2KCvg = Integer.parseInt(strKeepers[0][2]);
				blnP2K = true;
				pickedKP2Label.setText("Keeper: "+strP2K);
				connectSSM.sendText("LIVE,K," + strP2K);
			}else if(evt.getSource() == K2Button){
				strP2K = strKeepers[1][0];
				intP2KAgi = Integer.parseInt(strKeepers[1][1]);
				intP2KCvg = Integer.parseInt(strKeepers[1][2]);
				blnP2K = true;
				pickedKP2Label.setText("Keeper: "+strP2K);
				connectSSM.sendText("LIVE,K," + strP2K);
			}else if(evt.getSource() == K3Button){
				strP2K = strKeepers[2][0];
				intP2KAgi = Integer.parseInt(strKeepers[2][1]);
				intP2KCvg = Integer.parseInt(strKeepers[2][2]);
				blnP2K = true;
				pickedKP2Label.setText("Keeper: "+strP2K);
				connectSSM.sendText("LIVE,K," + strP2K);
			}
			
			if(evt.getSource() == S1Button){
				strP2S = strStrikers[0][0];
				intP2SAcc = Integer.parseInt(strStrikers[0][1]);
				intP2SPwr = Integer.parseInt(strStrikers[0][2]);
				blnP2S = true;
				pickedSP2Label.setText("Striker: "+strP2S);
				connectSSM.sendText("LIVE,S," + strP2S);
			}else if(evt.getSource() == S2Button){
				strP2S = strStrikers[1][0];
				intP2SAcc = Integer.parseInt(strStrikers[1][1]);
				intP2SPwr = Integer.parseInt(strStrikers[1][2]);
				blnP2S = true;
				pickedSP2Label.setText("Striker: "+strP2S);
				connectSSM.sendText("LIVE,S," + strP2S);
			}else if(evt.getSource() == S3Button){
				strP2S = strStrikers[2][0];
				intP2SAcc = Integer.parseInt(strStrikers[2][1]);
				intP2SPwr = Integer.parseInt(strStrikers[2][2]);
				blnP2S = true;
				pickedSP2Label.setText("Striker: "+strP2S);
				connectSSM.sendText("LIVE,S," + strP2S);
			}
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
	}
		
	//Drawing the images
	JPanel thePanel = new JPanel(){
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			//Drawing images
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
	
	//Reading player CSV
	public void loadCSV(){	
		strStrikers = new String[3][3];
		TextInputFile playersFile = new TextInputFile("players.csv");

		intCount = 0;
		while(playersFile.eof() == false){
			String strLine = playersFile.readLine();
			String strSplit[] = strLine.split(",");

			strStrikers[intCount][0] = strSplit[0].trim();
			strStrikers[intCount][1] = strSplit[1].trim();
			strStrikers[intCount][2] = strSplit[2].trim();

			intCount++;
		}

		strKeepers = new String[3][3];
		TextInputFile keepersFile = new TextInputFile("keepers.csv");

		intCount = 0;
		while(keepersFile.eof() == false){
			String strLine = keepersFile.readLine();
			String strSplit[] = strLine.split(",");

			strKeepers[intCount][0] = strSplit[0].trim();
			strKeepers[intCount][1] = strSplit[1].trim();
			strKeepers[intCount][2] = strSplit[2].trim();

			intCount++;
		}
	}	
	
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

    //Constructor
    public soccerMain(){
        //Start window
        super();
		theFrame.setLayout(null);
		theFrame.setPreferredSize(new Dimension(1280, 720)); 
		thePanel.setLayout(null);
		thePanel.setPreferredSize(new Dimension(1280, 720));
		
		//Main menu
		playButton.setBounds(0,0,300,100);
		playButton.addActionListener(this);
		thePanel.add(playButton);
		
		helpButton.setBounds(0,300,300,100);
		helpButton.addActionListener(this);
		thePanel.add(helpButton);
		
		//Main menu pannel into a list
		mainMenu = new JComponent[]{
			playButton, helpButton
		};
				
		//Connect menu
		serverButton.setBounds(0,0,300,100);
		serverButton.addActionListener(this);
		thePanel.add(serverButton);
		
		clientButton.setBounds(0,300,300,100);
		clientButton.addActionListener(this);
		thePanel.add(clientButton);
		
		IPLabel.setBounds(780, 10, 500, 30);
		IPLabel.setForeground(Color.BLACK);
		IPLabel.setFont(new Font("Arial", Font.BOLD, 20));
		thePanel.add(IPLabel);
	
		
		//Conect menu pannel into a list
		connectMenu = new JComponent[]{
			serverButton, clientButton, IPLabel
		};
		
		//Player stats
		loadCSV();
		
		K1Button.setText(strKeepers[0][0]);
		K2Button.setText(strKeepers[1][0]);
		K3Button.setText(strKeepers[2][0]);
		
		S1Button.setText(strStrikers[0][0]);
		S2Button.setText(strStrikers[1][0]);
		S3Button.setText(strStrikers[2][0]);
		
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
