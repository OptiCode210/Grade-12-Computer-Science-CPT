import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class soccerMain{
    //properties
    JPanel thePanel = new JPanel();
    JFrame theFrame = new JFrame("PENALTY!");



    //methods



    //constructor
    public soccerMain(){
        //start window
            theFrame.setLayout(null);
            theFrame.setPreferredSize(new Dimension(1280, 720)); 

        //finish window
            theFrame.setDefaultCloseOperation(3);
            theFrame.pack();
            theFrame.setVisible(true);

    }

    //Main method
	public static void main(String[] args){
		new soccerMain();
	}

    
}
