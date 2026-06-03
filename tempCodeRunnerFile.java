int intLeftRightLineX = 1140; 
    // Up/Down line moves vertically between Y: 230 and 470
    int intUpDownLineY = 350;    
    // Power line moves horizontally between X: 1020 and 1260
    int intPowerLineX = 1140;     

    // ANIMATION SPEED VARIABLES
    int intLeftRightSpeed = 4;
    int intUpDownSpeed = 5;
    int intPowerSpeed = 6;
    
    // --- NEW MECHANICAL & CONVERSION TRACKERS ---
    int intStage = 1; 
    // 1 = Left/Right, 2 = Up/Down, 3 = Power, 4 = Shot Locked / Complete
    int intFinalLeftRightX = 0;
    int intFinalUpDownY = 0;
    int intFinalPowerX = 0;

    double dblFinalLeftRightPercent = 0.0;
    double dblFinalUpDownPercent = 0.0;
    double dblFinalPowerPercent = 0.0;