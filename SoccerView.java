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
    public JLabel playerLabel = new JLabel("");
    public JLabel KAgiLabel = new JLabel("");
    public JLabel KCvgLabel = new JLabel("");
    public JLabel SPwrLabel = new JLabel("");
    public JLabel SAccLabel = new JLabel("");
    public JComponent[] selectionMenu;

    //Images
    public BufferedImage S1Front, S1Run, S1Shoot, S1Stand, S1Card;
    public BufferedImage S2Front, S2Run, S2Shoot, S2Stand, S2Card;
    public BufferedImage S3Front, S3Run, S3Shoot, S3Stand, S3Card;
    public BufferedImage K1Left, K1Right, K1Stand, K1Card;
    public BufferedImage K2Left, K2Right, K2Stand, K2Card;
    public BufferedImage K3Left, K3Right, K3Stand, K3Card;
    public BufferedImage menuBG;

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
            playerLabel, KAgiLabel, KCvgLabel, SPwrLabel, SAccLabel, pickLabel,
            confPickButton, pickedKP1Label, pickedSP1Label, pickedKP2Label, pickedSP2Label
        };

        //Set all the booleans to true 
        setMainVisible(true);
        setConnectVisible(false);
        setSelectionVisible(false);
        setHelpVisible(false);
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

                // Once the players are connected, draw the available player cards
                if (SoccerView.this.model.blnConnected) {
                    if (S1Card != null) g.drawImage(S1Card, 24, 100, 190, 240, null);
                    if (S2Card != null) g.drawImage(S2Card, 224, 100, 190, 240, null);
                    if (S3Card != null) g.drawImage(S3Card, 424, 100, 190, 240, null);
                    if (K1Card != null) g.drawImage(K1Card, 656, 100, 190, 240, null);
                    if (K2Card != null) g.drawImage(K2Card, 856, 100, 190, 240, null);
                    if (K3Card != null) g.drawImage(K3Card, 1056, 100, 190, 240, null);
                }
            }
        };
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
