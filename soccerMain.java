import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class soccerMain{
    //Properties
    JPanel thePanel = new JPanel();
    JFrame theFrame = new JFrame("PENALTY!");
	JButton playButton = new JButton("Play");
	JLabel serverLabel = new JLabel("Server");
	JLabel clientLabel = new JLabel("Client");
	JTextField serverField = new JTextField();
	JTextField clientField = new JTextField();
	JMenuBar theMenuBar = new JMenuBar();
	JMenuItem theHelpItem = new JMenuItem("Help");
	JButton serverButton = new JButton("Connect");
	JButton clientButton = new JButton("Connect");

    //Methods



    //Constructor
    public soccerMain(){
		//Start window
		theFrame.setLayout(null);
		theFrame.setPreferredSize(new Dimension(1280, 720)); 

        //Finish window
		theFrame.setDefaultCloseOperation(3);
		theFrame.pack();
		theFrame.setVisible(true);

    }

    //Main method
	public static void main(String[] args){
		new soccerMain();
	}

    
}
