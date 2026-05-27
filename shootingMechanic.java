import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class shootingMechanic extends JPanel implements ActionListener, KeyListener{
    // Properties

    int fps = 60;

    JFrame theFrame = new JFrame("trial");
    Timer theTimer = new Timer(1000/fps, this);

    //methods
    public void actionPerformed(ActionEvent evt){

    }

    public void keyPressed(KeyEvent evt){

    }

    public void keyReleased(KeyEvent evt){

    }

    public void keyTyped(KeyEvent evt){

    }

    // Constructor
    public shootingMechanic(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1280, 720));
        this.setFocusable(true);
        this.addKeyListener(this);

        theFrame.setContentPane(this);
        theFrame.setDefaultCloseOperation(3);
        theFrame.pack();
        theFrame.setVisible(true);

        theTimer.start();
        this.requestFocusInWindow();
    }

    // Main program
    public static void main(String[] args){
        new shootingMechanic();
    }
}
