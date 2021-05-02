package yahtzee;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 *
 * @author enesb
 */
public class YahtzeeFrame extends javax.swing.JFrame {

    private FiveDices myDices;
    
    private final int DICE_NUMBER = 5;
    
    private boolean[] keepingArray;
    
    private int currentUIState;
    
    private GameModel game;
    
    private JToggleButton[] diceBtns;
    private JToggleButton[] upperScoreBtnArray;
    private JToggleButton[] lowerScoreBtnArray;
    private JTextField[] upperScoreTextArray;
    private JTextField[] lowerScoreTextArray;
    
    public final static int RESET_GAME = 1;
    public final static int BEFORE_1st_ROLL = 2;
    public final static int BEFORE_2nd_ROLL = 3;
    public final static int BEFORE_3rd_ROLL = 4;
    public final static int AFTER_3rd_ROLL = 5;
    public final static int SCORING = 6;
    public final static int GAME_OVER = 7;
    
    public final static int THREES_OF_A_KIND = 0;
    public final static int FOURS_OF_A_KIND = 1;
    public final static int FULL_HOUSE = 2;
    public final static int SMALL_STRAIGHT = 3;
    public final static int LARGE_STRAIGHT = 4;
    public final static int YAHTZEE = 5;
    public final static int CHANCE = 6;
    
    public YahtzeeFrame() {
        initComponents();
        
        myDices = new FiveDices();
        
        //Array that keeps Dice buttons
        diceBtns = new JToggleButton[this.DICE_NUMBER];
        diceBtns[0] = tglbtnDice1;
        diceBtns[1] = tglbtnDice2;
        diceBtns[2] = tglbtnDice3;
        diceBtns[3] = tglbtnDice4;
        diceBtns[4] = tglbtnDice5;
        
        upperScoreBtnArray = new JToggleButton[GameModel.NUM_UPPER_SCORE_CATS + 1];
        upperScoreBtnArray[1] = this.tglbtnAces;
        upperScoreBtnArray[2] = this.tglbtnTwos;
        upperScoreBtnArray[3] = this.tglbtnThrees;
        upperScoreBtnArray[4] = this.tglbtnFours;
        upperScoreBtnArray[5] = this.tglbtnFives;
        upperScoreBtnArray[6] = this.tglbtnSixes;
        
        upperScoreTextArray = new JTextField[GameModel.NUM_UPPER_SCORE_CATS + 1];
        upperScoreTextArray[1] = this.txtAces;
        upperScoreTextArray[2] = this.txtTwos;
        upperScoreTextArray[3] = this.txtThrees;
        upperScoreTextArray[4] = this.txtFours;
        upperScoreTextArray[5] = this.txtFives;
        upperScoreTextArray[6] = this.txtSixes;
        
        lowerScoreBtnArray = new JToggleButton[GameModel.NUM_LOWER_SCORE_CATS];
        lowerScoreBtnArray[THREES_OF_A_KIND] = this.tglbtn3OfAKind;
        lowerScoreBtnArray[FOURS_OF_A_KIND] = this.tglbtn4OfAKind;
        lowerScoreBtnArray[FULL_HOUSE] = this.tglbtnFullHouse;
        lowerScoreBtnArray[SMALL_STRAIGHT] = this.tglbtnSMStraight;
        lowerScoreBtnArray[LARGE_STRAIGHT] = this.tglbtnLGStraight;
        lowerScoreBtnArray[YAHTZEE] = this.tglbtnYahtzee;
        lowerScoreBtnArray[CHANCE] = this.tglbtnChance;
        
        lowerScoreTextArray = new JTextField[GameModel.NUM_LOWER_SCORE_CATS];
        lowerScoreTextArray[THREES_OF_A_KIND] = this.txt3OfAKind;
        lowerScoreTextArray[FOURS_OF_A_KIND] = this.txt4OfAKind;
        lowerScoreTextArray[FULL_HOUSE] = this.txtFullHouse;
        lowerScoreTextArray[SMALL_STRAIGHT] = this.txtSMStraight;
        lowerScoreTextArray[LARGE_STRAIGHT] = this.txtLGStraight;
        lowerScoreTextArray[YAHTZEE] = this.txtYahtzee;
        lowerScoreTextArray[CHANCE] = this.txtChance;
        
        // Array that which we want to don't roll 
        keepingArray = new boolean[this.DICE_NUMBER];
        
        game = new GameModel();
        
        // Temp - should be reset game
        manageUIState(RESET_GAME);
        manageUIState(BEFORE_1st_ROLL);
    }
    
    public void manageUIState(int nextState){
        
        switch(nextState)
        {
            case RESET_GAME:
                game.clearAllUpperScoreCat();
                game.clearAllLowerScoreCat();
                game.clearUsedScoreCat();
                this.clearAllTextBox();
                this.resetTglBtns();
                game.resetTurn();
                break;
            case BEFORE_1st_ROLL:
                //Disable the hold buttons
                clearAndDisableKeepingButtons();
                //Enable roll button
                btnRoll.setEnabled(true);
                //Clear the keepingArray
                clearKeepingArray();
                disableAllScoreButtons();
                break;
            case BEFORE_2nd_ROLL:
                enableKeepingButtons();
                enableAllUnusedScoreButtons();
                break;
            case BEFORE_3rd_ROLL:
                break;
            case AFTER_3rd_ROLL:
                //Disable roll Button
                btnRoll.setEnabled(false);
            case SCORING:
                break;
            case GAME_OVER:
                JOptionPane.showMessageDialog(null, "Your Score is: " + game.getGrandTotal());
                break;
            default:
                JOptionPane.showMessageDialog(this, "Something went wrong");
                break;
        }
        currentUIState = nextState;
    }
    
    private void clearAndDisableKeepingButtons(){
        for(int i = 0; i < diceBtns.length; i++){
            diceBtns[i].setText("");
            diceBtns[i].setEnabled(false);
        }
    }
    
    private void clearKeepingArray(){
        for(int i = 0; i < keepingArray.length; i++)
            keepingArray[i] = false;
    }

    private void enableKeepingButtons(){
        for(int i = 0; i < diceBtns.length; i++)
            diceBtns[i].setEnabled(true);
    }
    
    private void disableAllScoreButtons(){
        
        for(int i = 1; i <= GameModel.NUM_UPPER_SCORE_CATS; i++)
            this.upperScoreBtnArray[i].setEnabled(false);
        
        for(int i = 0; i < GameModel.NUM_LOWER_SCORE_CATS; i++)
            this.lowerScoreBtnArray[i].setEnabled(false);
        
    }
    
    private void clearAllTextBox(){
        for(int i = 1; i <= GameModel.NUM_UPPER_SCORE_CATS; i++)
            upperScoreTextArray[i].setText("");
        
        txtBonus.setText("");
        txtTotalUpperScore.setText("");
        for(int i = 0; i < GameModel.NUM_LOWER_SCORE_CATS; i++)
            lowerScoreTextArray[i].setText("");
        
        txtLowerScore.setText("");
    }
    
    private void enableAllUnusedScoreButtons(){
        
        for(int i = 1; i <= GameModel.NUM_UPPER_SCORE_CATS; i++)
            if(!game.getUsedUpperScoreCatState(i))
                this.upperScoreBtnArray[i].setEnabled(true);
        
        for(int i = 0; i < GameModel.NUM_LOWER_SCORE_CATS; i++)
            if(!game.getUsedLowerScoreCatState(i))
                this.lowerScoreBtnArray[i].setEnabled(true);
        
    }
    
    public void resetTglBtns(){
        for(int i = 0; i < GameModel.NUM_LOWER_SCORE_CATS; i++)
            lowerScoreBtnArray[i].setEnabled(false);
        
        for(int i = 1; i <= GameModel.NUM_UPPER_SCORE_CATS; i++)
            upperScoreBtnArray[i].setEnabled(false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnRoll = new javax.swing.JButton();
        scoreButton = new javax.swing.JButton();
        tglbtnDice1 = new javax.swing.JToggleButton();
        tglbtnDice3 = new javax.swing.JToggleButton();
        tglbtnDice4 = new javax.swing.JToggleButton();
        tglbtnDice5 = new javax.swing.JToggleButton();
        tglbtnDice2 = new javax.swing.JToggleButton();
        tglbtnTwos = new javax.swing.JToggleButton();
        tglbtnAces = new javax.swing.JToggleButton();
        tglbtnSixes = new javax.swing.JToggleButton();
        tglbtnFives = new javax.swing.JToggleButton();
        tglbtnThrees = new javax.swing.JToggleButton();
        tglbtnFours = new javax.swing.JToggleButton();
        tglbtn3OfAKind = new javax.swing.JToggleButton();
        tglbtn4OfAKind = new javax.swing.JToggleButton();
        tglbtnFullHouse = new javax.swing.JToggleButton();
        tglbtnSMStraight = new javax.swing.JToggleButton();
        tglbtnLGStraight = new javax.swing.JToggleButton();
        tglbtnYahtzee = new javax.swing.JToggleButton();
        tglbtnChance = new javax.swing.JToggleButton();
        txtTwos = new javax.swing.JTextField();
        txtAces = new javax.swing.JTextField();
        txtThrees = new javax.swing.JTextField();
        txtFours = new javax.swing.JTextField();
        txtSixes = new javax.swing.JTextField();
        txtFives = new javax.swing.JTextField();
        txt3OfAKind = new javax.swing.JTextField();
        txt4OfAKind = new javax.swing.JTextField();
        txtFullHouse = new javax.swing.JTextField();
        txtSMStraight = new javax.swing.JTextField();
        txtLGStraight = new javax.swing.JTextField();
        txtYahtzee = new javax.swing.JTextField();
        txtBonus = new javax.swing.JTextField();
        txtTotalUpperScore = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtChance = new javax.swing.JTextField();
        txtLowerScore = new javax.swing.JTextField();
        txtGrandTotal = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnNewGame = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnRoll.setText("ROLL");
        btnRoll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRollActionPerformed(evt);
            }
        });

        scoreButton.setText("Score");
        scoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreButtonActionPerformed(evt);
            }
        });

        tglbtnDice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnDice1ActionPerformed(evt);
            }
        });

        tglbtnDice3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnDice3ActionPerformed(evt);
            }
        });

        tglbtnDice4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnDice4ActionPerformed(evt);
            }
        });

        tglbtnDice5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnDice5ActionPerformed(evt);
            }
        });

        tglbtnDice2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnDice2ActionPerformed(evt);
            }
        });

        tglbtnTwos.setText("Twos");
        tglbtnTwos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnTwosActionPerformed(evt);
            }
        });

        tglbtnAces.setText("Aces");
        tglbtnAces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnAcesActionPerformed(evt);
            }
        });

        tglbtnSixes.setText("Sixes");
        tglbtnSixes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnSixesActionPerformed(evt);
            }
        });

        tglbtnFives.setText("Fives");
        tglbtnFives.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnFivesActionPerformed(evt);
            }
        });

        tglbtnThrees.setText("Threes");
        tglbtnThrees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnThreesActionPerformed(evt);
            }
        });

        tglbtnFours.setText("Fours");
        tglbtnFours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnFoursActionPerformed(evt);
            }
        });

        tglbtn3OfAKind.setText("3 of a kind");
        tglbtn3OfAKind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtn3OfAKindActionPerformed(evt);
            }
        });

        tglbtn4OfAKind.setText("4 of a kind");
        tglbtn4OfAKind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtn4OfAKindActionPerformed(evt);
            }
        });

        tglbtnFullHouse.setText("Full House");
        tglbtnFullHouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnFullHouseActionPerformed(evt);
            }
        });

        tglbtnSMStraight.setText(" SM Straight");
        tglbtnSMStraight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnSMStraightActionPerformed(evt);
            }
        });

        tglbtnLGStraight.setText("LG Straight");
        tglbtnLGStraight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnLGStraightActionPerformed(evt);
            }
        });

        tglbtnYahtzee.setText("YAHTZEE");
        tglbtnYahtzee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnYahtzeeActionPerformed(evt);
            }
        });

        tglbtnChance.setText("Chance");
        tglbtnChance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnChanceActionPerformed(evt);
            }
        });

        txtTwos.setEditable(false);

        txtAces.setEditable(false);

        txtThrees.setEditable(false);

        txtFours.setEditable(false);

        txtSixes.setEditable(false);

        txtFives.setEditable(false);

        txt3OfAKind.setEditable(false);

        txt4OfAKind.setEditable(false);

        txtFullHouse.setEditable(false);

        txtSMStraight.setEditable(false);

        txtLGStraight.setEditable(false);

        txtYahtzee.setEditable(false);

        txtBonus.setEditable(false);
        txtBonus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBonusActionPerformed(evt);
            }
        });

        txtTotalUpperScore.setEditable(false);

        jLabel2.setText("Bonus (Upper > 63)");

        jLabel3.setText("Total Upper Score");

        txtChance.setEditable(false);

        txtLowerScore.setEditable(false);

        txtGrandTotal.setEditable(false);

        jLabel4.setText("Total Lower Score");

        jLabel5.setText("Grand Total");

        btnNewGame.setText("New Game");
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(tglbtnDice1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tglbtnDice2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tglbtnDice3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(tglbtnDice4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(tglbtnDice5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(245, 245, 245)
                        .addComponent(btnRoll, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(96, 96, 96)
                        .addComponent(scoreButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(tglbtnAces, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtAces, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(tglbtn3OfAKind, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt3OfAKind, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(tglbtnTwos, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtTwos, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(tglbtn4OfAKind, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt4OfAKind, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(tglbtnThrees, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtThrees, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(tglbtnFullHouse, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtFullHouse, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(tglbtnFours, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtFours, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(tglbtnSMStraight, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSMStraight, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(tglbtnFives, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtFives, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(tglbtnLGStraight, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtLGStraight, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addComponent(btnNewGame, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addComponent(txtTotalUpperScore))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(txtBonus)))
                                        .addGap(142, 142, 142)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5))
                                        .addGap(23, 23, 23))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(tglbtnChance, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtChance, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                    .addComponent(txtLowerScore)
                                    .addComponent(txtGrandTotal)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tglbtnSixes, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtSixes, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(128, 128, 128)
                                .addComponent(tglbtnYahtzee, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtYahtzee, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tglbtnDice1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tglbtnDice2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tglbtnDice3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tglbtnDice4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tglbtnDice5, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRoll, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(scoreButton)))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tglbtnAces, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAces, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tglbtn3OfAKind, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt3OfAKind, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tglbtnTwos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTwos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tglbtn4OfAKind, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt4OfAKind, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tglbtnThrees, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThrees, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tglbtnFullHouse, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFullHouse, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tglbtnFours, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFours, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tglbtnSMStraight, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSMStraight, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tglbtnFives, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFives, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tglbtnLGStraight, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLGStraight, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tglbtnYahtzee, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(txtYahtzee, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtSixes, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tglbtnSixes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(8, 8, 8)))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(tglbtnChance, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(3, 3, 3)
                                    .addComponent(jLabel2)
                                    .addGap(17, 17, 17)
                                    .addComponent(jLabel3))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtBonus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(11, 11, 11)
                                    .addComponent(txtTotalUpperScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(17, 17, 17)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtChance, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(txtLowerScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(txtGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addComponent(btnNewGame, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRollActionPerformed
        
        int rollNumber;
        
        for(int i = 0; i < this.DICE_NUMBER; i++)
            //if user dont want to keep that dice, roll it
            if(keepingArray[i] != true){
                rollNumber = myDices.rollSingleDice(i);
                diceBtns[i].setText(Integer.toString(myDices.getDiceValue(i)));
            }
        
        if(currentUIState == BEFORE_1st_ROLL)
            manageUIState(BEFORE_2nd_ROLL);
        
        else if(currentUIState == BEFORE_2nd_ROLL)
            manageUIState(BEFORE_3rd_ROLL);
        
        else if(currentUIState == BEFORE_3rd_ROLL)
            manageUIState(AFTER_3rd_ROLL);
        
    }//GEN-LAST:event_btnRollActionPerformed

    private void scoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreButtonActionPerformed
        manageUIState(BEFORE_1st_ROLL);
    }//GEN-LAST:event_scoreButtonActionPerformed

    private void tglbtnDice1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnDice1ActionPerformed
        keepingArray[0] = !keepingArray[0];
    }//GEN-LAST:event_tglbtnDice1ActionPerformed

    private void tglbtnDice2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnDice2ActionPerformed
        keepingArray[1] = !keepingArray[1];
    }//GEN-LAST:event_tglbtnDice2ActionPerformed

    private void tglbtnDice3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnDice3ActionPerformed
        keepingArray[2] = !keepingArray[2];
    }//GEN-LAST:event_tglbtnDice3ActionPerformed

    private void tglbtnDice4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnDice4ActionPerformed
        keepingArray[3] = !keepingArray[3];
    }//GEN-LAST:event_tglbtnDice4ActionPerformed

    private void tglbtnDice5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnDice5ActionPerformed
        keepingArray[4] = !keepingArray[4];
    }//GEN-LAST:event_tglbtnDice5ActionPerformed

    private void tglbtnAcesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnAcesActionPerformed
        scoreUpperCat(1);
    }//GEN-LAST:event_tglbtnAcesActionPerformed

    private void tglbtnChanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnChanceActionPerformed
        int score = 0;
        
        game.setUsedLowerScoreCat(CHANCE, true);
        manageUIState(SCORING);
        
        score = game.addEmUp(myDices);
        
        game.setLowerScoreCat(CHANCE, score);
        this.lowerScoreTextArray[CHANCE].setText("" + score);
        this.showTotal();
        
        if(game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS)
            manageUIState(BEFORE_1st_ROLL);
        else
            manageUIState(GAME_OVER);  
    }//GEN-LAST:event_tglbtnChanceActionPerformed

    private void tglbtn3OfAKindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtn3OfAKindActionPerformed
        int score = 0;
        
        game.setUsedLowerScoreCat(THREES_OF_A_KIND, true);
        manageUIState(SCORING);
        
        if(game.isOfAKind(myDices, 3))
            score = game.addEmUp(myDices);
        
        game.setLowerScoreCat(THREES_OF_A_KIND, score);
        
        this.lowerScoreTextArray[THREES_OF_A_KIND].setText("" + score);  
        this.showTotal();
        
        if(game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS)
            manageUIState(BEFORE_1st_ROLL);
        else
            manageUIState(GAME_OVER);
    }//GEN-LAST:event_tglbtn3OfAKindActionPerformed

    private void tglbtnTwosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnTwosActionPerformed
        scoreUpperCat(2);
    }//GEN-LAST:event_tglbtnTwosActionPerformed

    private void tglbtn4OfAKindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtn4OfAKindActionPerformed
        int score = 0;
        
        game.setUsedLowerScoreCat(FOURS_OF_A_KIND, true);
        manageUIState(SCORING);
        
        if(game.is4OfAKind(myDices))
            score = game.addEmUp(myDices);
        
        game.setLowerScoreCat(FOURS_OF_A_KIND, score);
        
        this.lowerScoreTextArray[FOURS_OF_A_KIND].setText("" + score);
        this.showTotal();
        
        if(game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS)
            manageUIState(BEFORE_1st_ROLL);
        else
            manageUIState(GAME_OVER);
    }//GEN-LAST:event_tglbtn4OfAKindActionPerformed

    private void txtBonusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBonusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBonusActionPerformed

    private void btnNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGameActionPerformed
        manageUIState(RESET_GAME);
        manageUIState(BEFORE_1st_ROLL);
    }//GEN-LAST:event_btnNewGameActionPerformed

    private void tglbtnSixesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnSixesActionPerformed
        scoreUpperCat(6);
    }//GEN-LAST:event_tglbtnSixesActionPerformed

    private void tglbtnThreesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnThreesActionPerformed
        scoreUpperCat(3);
    }//GEN-LAST:event_tglbtnThreesActionPerformed

    private void tglbtnFoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnFoursActionPerformed
        scoreUpperCat(4);
    }//GEN-LAST:event_tglbtnFoursActionPerformed

    private void tglbtnFivesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnFivesActionPerformed
        scoreUpperCat(5);
    }//GEN-LAST:event_tglbtnFivesActionPerformed

    private void tglbtnYahtzeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnYahtzeeActionPerformed
        int score = 0;
        
        game.setUsedLowerScoreCat(YAHTZEE, true);
        manageUIState(SCORING);
        
        if(game.isOfAKind(myDices, 5))
            score = 50;
        
        game.setLowerScoreCat(YAHTZEE, score);
        this.lowerScoreTextArray[YAHTZEE].setText("" + score);
        this.showTotal();
        
        if(game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS)
            manageUIState(BEFORE_1st_ROLL);
        else
            manageUIState(GAME_OVER);  
    }//GEN-LAST:event_tglbtnYahtzeeActionPerformed

    private void tglbtnFullHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnFullHouseActionPerformed
        int score = 0;
        
        game.setUsedLowerScoreCat(FULL_HOUSE, true);
        manageUIState(SCORING);
        
        if(game.isFullHouse(myDices))
            score = 25;
        
        game.setLowerScoreCat(FULL_HOUSE, score);
        this.lowerScoreTextArray[FULL_HOUSE].setText("" + score);
        this.showTotal();
        
        if(game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS)
            manageUIState(BEFORE_1st_ROLL);
        else
            manageUIState(GAME_OVER);  
    }//GEN-LAST:event_tglbtnFullHouseActionPerformed

    private void tglbtnLGStraightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnLGStraightActionPerformed
        int score = 0;
        
        game.setUsedLowerScoreCat(LARGE_STRAIGHT, true);
        manageUIState(SCORING);
        
        if(game.isLargeStraight(myDices))
            score = 40;
        
        game.setLowerScoreCat(LARGE_STRAIGHT, score);
        this.lowerScoreTextArray[LARGE_STRAIGHT].setText("" + score);
        this.showTotal();
        
        if(game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS)
            manageUIState(BEFORE_1st_ROLL);
        else
            manageUIState(GAME_OVER); 
    }//GEN-LAST:event_tglbtnLGStraightActionPerformed

    private void tglbtnSMStraightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnSMStraightActionPerformed
        int score = 0;
        
        game.setUsedLowerScoreCat(SMALL_STRAIGHT, true);
        manageUIState(SCORING);
        
        if(game.isSmallStraight(myDices))
            score = 30;
        
        game.setLowerScoreCat(SMALL_STRAIGHT, score);
        this.lowerScoreTextArray[SMALL_STRAIGHT].setText("" + score);
        this.showTotal();
        
        if(game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS)
            manageUIState(BEFORE_1st_ROLL);
        else
            manageUIState(GAME_OVER); 
    }//GEN-LAST:event_tglbtnSMStraightActionPerformed

    public void scoreUpperCat(int category){
        int score = 0;
        
        game.setUsedUpperScoreCat(category, true);
        
        manageUIState(SCORING);
        
        for(int i = 0; i < DICE_NUMBER; i++)
            if(myDices.getDiceValue(i) == category)
                score += category;
        
        game.setUpperScoreCat(category, score);
        this.upperScoreTextArray[category].setText("" + score);
        this.showTotal();
        
        if(game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS)
            manageUIState(BEFORE_1st_ROLL);
        else
            manageUIState(GAME_OVER);
    }
    
    public void showTotal(){
        
        game.UpdateTotals();
        txtBonus.setText("" + game.getBonus());
        txtTotalUpperScore.setText("" + game.getSumUpperScore());
        txtLowerScore.setText("" + game.getSumLowerScore());
        txtGrandTotal.setText("" + game.getGrandTotal());
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(YahtzeeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(YahtzeeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(YahtzeeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(YahtzeeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new YahtzeeFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewGame;
    private javax.swing.JButton btnRoll;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton scoreButton;
    private javax.swing.JToggleButton tglbtn3OfAKind;
    private javax.swing.JToggleButton tglbtn4OfAKind;
    private javax.swing.JToggleButton tglbtnAces;
    private javax.swing.JToggleButton tglbtnChance;
    private javax.swing.JToggleButton tglbtnDice1;
    private javax.swing.JToggleButton tglbtnDice2;
    private javax.swing.JToggleButton tglbtnDice3;
    private javax.swing.JToggleButton tglbtnDice4;
    private javax.swing.JToggleButton tglbtnDice5;
    private javax.swing.JToggleButton tglbtnFives;
    private javax.swing.JToggleButton tglbtnFours;
    private javax.swing.JToggleButton tglbtnFullHouse;
    private javax.swing.JToggleButton tglbtnLGStraight;
    private javax.swing.JToggleButton tglbtnSMStraight;
    private javax.swing.JToggleButton tglbtnSixes;
    private javax.swing.JToggleButton tglbtnThrees;
    private javax.swing.JToggleButton tglbtnTwos;
    private javax.swing.JToggleButton tglbtnYahtzee;
    private javax.swing.JTextField txt3OfAKind;
    private javax.swing.JTextField txt4OfAKind;
    private javax.swing.JTextField txtAces;
    private javax.swing.JTextField txtBonus;
    private javax.swing.JTextField txtChance;
    private javax.swing.JTextField txtFives;
    private javax.swing.JTextField txtFours;
    private javax.swing.JTextField txtFullHouse;
    private javax.swing.JTextField txtGrandTotal;
    private javax.swing.JTextField txtLGStraight;
    private javax.swing.JTextField txtLowerScore;
    private javax.swing.JTextField txtSMStraight;
    private javax.swing.JTextField txtSixes;
    private javax.swing.JTextField txtThrees;
    private javax.swing.JTextField txtTotalUpperScore;
    private javax.swing.JTextField txtTwos;
    private javax.swing.JTextField txtYahtzee;
    // End of variables declaration//GEN-END:variables

}
