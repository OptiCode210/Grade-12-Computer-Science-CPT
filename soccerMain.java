import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.IOException;
import java.io.File;

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
	JTextField serverField = new JTextField();
	JTextField clientField = new JTextField();
	JButton serverButton = new JButton("Host");
	JButton clientButton = new JButton("Join");
	String strServerID;
	String strServerIP;
	JComponent[] connectMenu;
	boolean blnConnected = false;
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
	int intP1Pwr;
	int intP1Acc;
	
	String strP2K;
	String strP2S;
	int intP2KAgi;
	int intP2KCvg;
	int intP2Pwr;
	int intP2Acc;	

	JButton K1Button = new JButton("Gianluigi DONNARUMA");
	JButton K2Button = new JButton("James TRAFFORD ");
	JButton K3Button = new JButton("David DE GEA");
	JButton S1Button = new JButton("Erling HAALAND ");
	JButton S2Button = new JButton("Cristiano RONALDO");
	JButton S3Button = new JButton("Lionel MESSI");
	JLabel playerLabel = new JLabel("");
	JLabel KAgiLabel = new JLabel("");
	JLabel KCvgLabel = new JLabel("");
	JLabel SPwrLabel = new JLabel("");
	JLabel SAccLabel = new JLabel("");
	int intRow = 0;
	int intCol = 0;
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
			System.out.println(connectSSM.getMyAddress());
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
		}	
		
		//Going to player selection
		if(blnConnected == true){
			setMainVisible(false);
			setConnectVisible(false);
			setSelectionVisible(true);
			thePanel.repaint();
		}
	}
		
	//Drawing the images
	JPanel thePanel = new JPanel(){
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			//Drawing images
			if(blnConnected == true){				
				if(S1Card != null){
					g.drawImage(S1Card, 10, 100, 150, 200, null);
				}
		
				if(blnConnected == true){				
					if(S2Card != null){
						g.drawImage(S2Card, 170, 100, 150, 200, null);
					}
				}
				if(blnConnected == true){				
					if(S3Card != null){
						g.drawImage(S3Card, 330, 100, 150, 200, null);
					}
				}
				if(blnConnected == true){				
					if(K1Card != null){
						g.drawImage(K1Card, 650, 100, 200, 200, null);
					}
				}
				if(blnConnected == true){				
					if(K2Card != null){
						g.drawImage(K2Card, 860, 100, 200, 200, null);
					}
				}
				if(blnConnected == true){				
					if(K3Card != null){
						g.drawImage(K3Card, 1070, 100, 200, 200, null);
					}
				}
				if(blnConnected == true){
					if(menuBG != null){
						g.drawImage(menuBG, 0, 0, 1280, 720, null);
					}
				}
				
				//UI and Player stats
				g.setColor(Color.BLACK);
				g.fillRect(638, 0, 4, 720);
				
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
		
		//Conect menu pannel into a list
		connectMenu = new JComponent[]{
			serverButton, clientButton
		};
						
		//Player selection images
		try{
			S1Front = ImageIO.read(new File("images/Strikers/S1Front.gif"));	
			S1Run = ImageIO.read(new File("images/Strikers/S1Run.gif"));	
			S1Shoot = ImageIO.read(new File("images/Strikers/S1Shoot.gif"));	
			S1Stand = ImageIO.read(new File("images/Strikers/S1Stand.gif"));	
			S1Card = ImageIO.read(new File("images/player cards/S1Card.gif"));	
			
			S2Front = ImageIO.read(new File("images/Strikers/S2Front.gif"));	
			S2Run = ImageIO.read(new File("images/Strikers/S2Run.gif"));	
			S2Shoot = ImageIO.read(new File("images/Strikers/S2Shoot.gif"));	
			S2Stand = ImageIO.read(new File("images/Strikers/S2Stand.gif"));
			S2Card = ImageIO.read(new File("images/player cards/S2Card.gif"));	
			
			S3Front = ImageIO.read(new File("images/Strikers/S3Front.gif"));	
			S3Run = ImageIO.read(new File("images/Strikers/S3Run.gif"));	
			S3Shoot = ImageIO.read(new File("images/Strikers/S3Shoot.gif"));	
			S3Stand = ImageIO.read(new File("images/Strikers/S3Stand.gif"));
			S3Card = ImageIO.read(new File("images/player cards/S3Card.gif"));	
			
			K1Left = ImageIO.read(new File("images/keepers/K1Left.gif"));	
			K1Right = ImageIO.read(new File("images/keepers/K1Right.gif"));	
			K1Stand = ImageIO.read(new File("images/keepers/K1Stand.gif"));		
			K1Card = ImageIO.read(new File("images/player cards/K1Card.gif"));	
			
			K2Left = ImageIO.read(new File("images/keepers/K2Left.gif"));	
			K2Right = ImageIO.read(new File("images/keepers/K2Right.gif"));	
			K2Stand = ImageIO.read(new File("images/keepers/K2Stand.gif"));	
			K2Card = ImageIO.read(new File("images/player cards/K2Card.gif"));	
			
			K3Left = ImageIO.read(new File("images/keepers/K3Left.gif"));	
			K3Right = ImageIO.read(new File("images/keepers/K3Right.gif"));	
			K3Stand = ImageIO.read(new File("images/keepers/K3Stand.gif"));	
			K3Card = ImageIO.read(new File("images/player cards/K3Card.gif"));	
			
			menuBG = ImageIO.read(new File("images/menuBG.jpg"));		
		}catch(IOException e){
			System.out.println("Image unable to be loaded");
			System.out.println(e);
		}
		
		//The UI
		K1Button.setBounds(33,400,200,25);
		thePanel.add(K1Button);
		K2Button.setBounds(233,400,200,25);
		thePanel.add(K2Button);
		K3Button.setBounds(433,400,200,25);
		thePanel.add(K3Button);
		
		S1Button.setBounds(647,400,200,25);
		thePanel.add(S1Button);
		S2Button.setBounds(847,400,200,25);
		thePanel.add(S2Button);
		S3Button.setBounds(1047,400,200,25);
		thePanel.add(S3Button);
		
		//Selection menu pannel into a list
		selectionMenu = new JComponent[]{
			K1Button, K2Button, K3Button, S1Button, S2Button, S3Button,
			playerLabel, KAgiLabel, KCvgLabel, SPwrLabel, SAccLabel
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
