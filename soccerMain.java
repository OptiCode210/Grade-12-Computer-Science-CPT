import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class soccerMain implements ActionListener{
    //Properties
    JPanel thePanel = new JPanel();
    JFrame theFrame = new JFrame("PENALTY!");
    
	JButton helpButton = new JButton("Help");
	JButton playButton = new JButton("Play");
	JComponent[] mainMenu;
	
	JLabel serverLabel = new JLabel("Server");
	JLabel clientLabel = new JLabel("Client");
	JTextField serverField = new JTextField();
	JTextField clientField = new JTextField();
	JButton serverButton = new JButton("Host");
	JButton clientButton = new JButton("Join");
	String strServerID;
	JComponent[] connectMenu;
	
	SuperSocketMaster connectSSM = null;
	
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
			connectSSM.connect();
		}
		if(evt.getSource() == clientButton){
			strServerID = JOptionPane.showInputDialog(theFrame, "Enter IP: ", "YIPPE", JOptionPane.PLAIN_MESSAGE);
			connectSSM = new SuperSocketMaster(strServerID, 6112, this);
		
			//Access the connect method
			connectSSM.connect();
			serverButton.setEnabled(false);
			clientButton.setEnabled(false);
			System.out.println("CONNECTED");
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


    //Constructor
    public soccerMain(){
        //Start window
		theFrame.setLayout(null);
		theFrame.setPreferredSize(new Dimension(1280, 720)); 

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
		
		//Drawing main screen
		setMainVisible(true);
		setConnectVisible(false);
								
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
