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
	
	BufferedImage S2Front = null;
	BufferedImage S2Run = null;
	BufferedImage S2Shoot = null;
	BufferedImage S2Stand = null;
	
	BufferedImage S3Front = null;
	BufferedImage S3Run = null;
	BufferedImage S3Shoot = null;
	BufferedImage S3Stand = null;
	
	BufferedImage K1Left = null;
	BufferedImage K1Right = null;
	BufferedImage K1Stand = null;
	
	BufferedImage K2Left = null;
	BufferedImage K2Right = null;
	BufferedImage K2Stand = null;
	
	BufferedImage K3Left = null;
	BufferedImage K3Right = null;
	BufferedImage K3Stand = null;
	
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

	JButton confKButton = new JButton("Confirm");
	JButton confSButton = new JButton("Confirm");
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
			
			if(blnConnected == true){				
				if(S1Front != null){
					g.drawImage(S1Front, 10, 100, 150, 200, null);
				}
		
				if(blnConnected == true){				
					if(S2Front != null){
						g.drawImage(S2Front, 170, 100, 150, 200, null);
					}
				}
				if(blnConnected == true){				
					if(S3Front != null){
						g.drawImage(S3Front, 330, 100, 150, 200, null);
					}
				}
				if(blnConnected == true){				
					if(K1Stand != null){
						g.drawImage(K1Stand, 650, 100, 200, 200, null);
					}
				}
				if(blnConnected == true){				
					if(K2Stand != null){
						g.drawImage(K2Stand, 860, 100, 200, 200, null);
					}
				}
				if(blnConnected == true){				
					if(K3Stand != null){
						g.drawImage(K3Stand, 1070, 100, 200, 200, null);
					}
				}
				
				g.setColor(Color.BLACK);
				g.fillRect(0, 378, 1280, 4);
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
		
		connectMenu = new JComponent[]{
			serverButton, clientButton
		};
						
		//Player selection images
		try{
			S1Front = ImageIO.read(new File("images/Strikers/S1Front.gif"));	
			S1Run = ImageIO.read(new File("images/Strikers/S1Run.gif"));	
			S1Shoot = ImageIO.read(new File("images/Strikers/S1Shoot.gif"));	
			S1Stand = ImageIO.read(new File("images/Strikers/S1Stand.gif"));	
			
			S2Front = ImageIO.read(new File("images/Strikers/S2Front.gif"));	
			S2Run = ImageIO.read(new File("images/Strikers/S2Run.gif"));	
			S2Shoot = ImageIO.read(new File("images/Strikers/S2Shoot.gif"));	
			S2Stand = ImageIO.read(new File("images/Strikers/S2Stand.gif"));
			
			S3Front = ImageIO.read(new File("images/Strikers/S3Front.gif"));	
			S3Run = ImageIO.read(new File("images/Strikers/S3Run.gif"));	
			S3Shoot = ImageIO.read(new File("images/Strikers/S3Shoot.gif"));	
			S3Stand = ImageIO.read(new File("images/Strikers/S3Stand.gif"));
			
			K1Left = ImageIO.read(new File("images/keepers/K1Left.gif"));	
			K1Right = ImageIO.read(new File("images/keepers/K1Right.gif"));	
			K1Stand = ImageIO.read(new File("images/keepers/K1Stand.gif"));		
			
			K2Left = ImageIO.read(new File("images/keepers/K2Left.gif"));	
			K2Right = ImageIO.read(new File("images/keepers/K2Right.gif"));	
			K2Stand = ImageIO.read(new File("images/keepers/K2Stand.gif"));	
			
			K3Left = ImageIO.read(new File("images/keepers/K3Left.gif"));	
			K3Right = ImageIO.read(new File("images/keepers/K3Right.gif"));	
			K3Stand = ImageIO.read(new File("images/keepers/K3Stand.gif"));			
		}catch(IOException e){
			System.out.println("Image unable to be loaded");
			System.out.println(e);
		}
		
		selectionMenu = new JComponent[]{
			confKButton, confSButton, playerLabel, KAgiLabel, KCvgLabel, SPwrLabel, SAccLabel
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
