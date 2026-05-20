import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
//For Paint Compnent

public class soccerPenalty extends JPanel{
    //properties
    int intBallX, intBallY;


    //methods
    public void paintComponent(Graphics g){
        g.setColor(Color.WHITE);
        g.drawOval(intBallX, intBallY, 10, 10);
    }



    //constructor
    public soccerPenalty(){
        super();

        intBallX = 550;
        intBallY = 650;

        repaint();
    }



    
}
