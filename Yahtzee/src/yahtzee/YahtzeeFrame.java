package yahtzee;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import yahtzeeclient.Client;

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

    public static YahtzeeFrame thisYahtzeeFrame;
    public static int a;

    public Thread tmr_slider;
    
    public static int rivalScore;
    EndFrame thisEndFrame;

    public YahtzeeFrame() {
        initComponents();
        this.getContentPane().setBackground(new java.awt.Color(0, 0, 0));
        this.setTitle("YAHTZEE");
        myDices = new FiveDices();
        a = 0;

        this.txtPlayerName.setText(OpenFrame.txtName.getText());
        thisYahtzeeFrame = this;
        this.txtPlayerName.setEnabled(false);
        this.txtRivalName.setEnabled(false);

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

    public void manageUIState(int nextState) {

        switch (nextState) {
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
                EndFrame frm = new EndFrame();
                frm.setVisible(true);
                this.setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Something went wrong");
                break;
        }
        currentUIState = nextState;
    }

    private void clearAndDisableKeepingButtons() {
        for (int i = 0; i < DICE_NUMBER; i++) {
            diceBtns[i].setText("");
            diceBtns[i].setEnabled(false);
            diceBtns[i].setSelected(false);
        }
    }

    private void clearKeepingArray() {
        for (int i = 0; i < DICE_NUMBER; i++) {
            keepingArray[i] = false;
        }
    }

    private void enableKeepingButtons() {
        for (int i = 0; i < diceBtns.length; i++) {
            diceBtns[i].setEnabled(true);
        }
    }

    private void disableAllScoreButtons() {

        for (int i = 1; i <= GameModel.NUM_UPPER_SCORE_CATS; i++) {
            this.upperScoreBtnArray[i].setEnabled(false);
        }

        for (int i = 0; i < GameModel.NUM_LOWER_SCORE_CATS; i++) {
            this.lowerScoreBtnArray[i].setEnabled(false);
        }

    }

    private void clearAllTextBox() {
        for (int i = 1; i <= GameModel.NUM_UPPER_SCORE_CATS; i++) {
            upperScoreTextArray[i].setText("");
        }

        txtBonus.setText("");
        txtTotalUpperScore.setText("");
        for (int i = 0; i < GameModel.NUM_LOWER_SCORE_CATS; i++) {
            lowerScoreTextArray[i].setText("");
        }

        txtLowerScore.setText("");
    }

    private void enableAllUnusedScoreButtons() {

        for (int i = 1; i <= GameModel.NUM_UPPER_SCORE_CATS; i++) {
            if (!game.getUsedUpperScoreCatState(i)) {
                this.upperScoreBtnArray[i].setEnabled(true);
            }
        }

        for (int i = 0; i < GameModel.NUM_LOWER_SCORE_CATS; i++) {
            if (!game.getUsedLowerScoreCatState(i)) {
                this.lowerScoreBtnArray[i].setEnabled(true);
            }
        }

    }

    public void resetTglBtns() {
        for (int i = 0; i < GameModel.NUM_LOWER_SCORE_CATS; i++) {
            lowerScoreBtnArray[i].setEnabled(false);
        }

        for (int i = 1; i <= GameModel.NUM_UPPER_SCORE_CATS; i++) {
            upperScoreBtnArray[i].setEnabled(false);
        }
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
        txtTwos1 = new javax.swing.JTextField();
        txtAces1 = new javax.swing.JTextField();
        txtThrees1 = new javax.swing.JTextField();
        txtFours1 = new javax.swing.JTextField();
        txtSixes1 = new javax.swing.JTextField();
        txtFives1 = new javax.swing.JTextField();
        txt3OfAKind1 = new javax.swing.JTextField();
        txt4OfAKind1 = new javax.swing.JTextField();
        txtFullHouse1 = new javax.swing.JTextField();
        txtSMStraight1 = new javax.swing.JTextField();
        txtLGStraight1 = new javax.swing.JTextField();
        txtYahtzee1 = new javax.swing.JTextField();
        txtBonus1 = new javax.swing.JTextField();
        txtTotalUpperScore1 = new javax.swing.JTextField();
        txtChance1 = new javax.swing.JTextField();
        txtLowerScore1 = new javax.swing.JTextField();
        txtGrandTotal1 = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 600), new java.awt.Dimension(0, 600), new java.awt.Dimension(32767, 600));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 600), new java.awt.Dimension(0, 600), new java.awt.Dimension(32767, 600));
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtRivalName = new javax.swing.JTextField();
        txtPlayerName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 255, 0));
        setPreferredSize(new java.awt.Dimension(700, 700));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnRoll.setFont(new java.awt.Font("Goudy Stout", 0, 18)); // NOI18N
        btnRoll.setText("ROLL DICES");
        btnRoll.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRoll.setBorderPainted(false);
        btnRoll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRollActionPerformed(evt);
            }
        });
        getContentPane().add(btnRoll, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 400, 240, 60));

        tglbtnDice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnDice1ActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnDice1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 210, 93, 78));

        tglbtnDice3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnDice3ActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnDice3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 210, 93, 78));

        tglbtnDice4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnDice4ActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnDice4, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 300, 93, 78));

        tglbtnDice5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnDice5ActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnDice5, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, 93, 78));

        tglbtnDice2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnDice2ActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnDice2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, 93, 78));

        tglbtnTwos.setText("Twos");
        tglbtnTwos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnTwosActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnTwos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 90, 25));

        tglbtnAces.setFont(tglbtnAces.getFont());
        tglbtnAces.setText("Aces");
        tglbtnAces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnAcesActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnAces, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 90, 25));

        tglbtnSixes.setText("Sixes");
        tglbtnSixes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnSixesActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnSixes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 90, 25));

        tglbtnFives.setText("Fives");
        tglbtnFives.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnFivesActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnFives, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 90, 25));

        tglbtnThrees.setText("Threes");
        tglbtnThrees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnThreesActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnThrees, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 90, 25));

        tglbtnFours.setText("Fours");
        tglbtnFours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnFoursActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnFours, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 90, 25));

        tglbtn3OfAKind.setText("3 of a kind");
        tglbtn3OfAKind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtn3OfAKindActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtn3OfAKind, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 90, 25));

        tglbtn4OfAKind.setText("4 of a kind");
        tglbtn4OfAKind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtn4OfAKindActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtn4OfAKind, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 90, 25));

        tglbtnFullHouse.setText("Full House");
        tglbtnFullHouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnFullHouseActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnFullHouse, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 90, 25));

        tglbtnSMStraight.setText(" SM Straight");
        tglbtnSMStraight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnSMStraightActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnSMStraight, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 90, 25));

        tglbtnLGStraight.setText("LG Straight");
        tglbtnLGStraight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnLGStraightActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnLGStraight, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 90, 25));

        tglbtnYahtzee.setText("YAHTZEE");
        tglbtnYahtzee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnYahtzeeActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnYahtzee, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 90, 25));

        tglbtnChance.setText("Chance");
        tglbtnChance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglbtnChanceActionPerformed(evt);
            }
        });
        getContentPane().add(tglbtnChance, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 90, 25));

        txtTwos.setEditable(false);
        txtTwos.setBackground(new java.awt.Color(255, 255, 255));
        txtTwos.setEnabled(false);
        getContentPane().add(txtTwos, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 70, 25));

        txtAces.setEditable(false);
        txtAces.setBackground(new java.awt.Color(255, 255, 255));
        txtAces.setEnabled(false);
        getContentPane().add(txtAces, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 70, 25));

        txtThrees.setEditable(false);
        txtThrees.setBackground(new java.awt.Color(255, 255, 255));
        txtThrees.setEnabled(false);
        getContentPane().add(txtThrees, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 70, 25));

        txtFours.setEditable(false);
        txtFours.setBackground(new java.awt.Color(255, 255, 255));
        txtFours.setEnabled(false);
        getContentPane().add(txtFours, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 70, 25));

        txtSixes.setEditable(false);
        txtSixes.setBackground(new java.awt.Color(255, 255, 255));
        txtSixes.setEnabled(false);
        getContentPane().add(txtSixes, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 70, 25));

        txtFives.setEditable(false);
        txtFives.setBackground(new java.awt.Color(255, 255, 255));
        txtFives.setEnabled(false);
        getContentPane().add(txtFives, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 70, 25));

        txt3OfAKind.setEditable(false);
        txt3OfAKind.setBackground(new java.awt.Color(255, 255, 255));
        txt3OfAKind.setEnabled(false);
        getContentPane().add(txt3OfAKind, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 70, 25));

        txt4OfAKind.setEditable(false);
        txt4OfAKind.setBackground(new java.awt.Color(255, 255, 255));
        txt4OfAKind.setEnabled(false);
        getContentPane().add(txt4OfAKind, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 390, 70, 25));

        txtFullHouse.setEditable(false);
        txtFullHouse.setBackground(new java.awt.Color(255, 255, 255));
        txtFullHouse.setEnabled(false);
        getContentPane().add(txtFullHouse, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 420, 70, 25));

        txtSMStraight.setEditable(false);
        txtSMStraight.setBackground(new java.awt.Color(255, 255, 255));
        txtSMStraight.setEnabled(false);
        getContentPane().add(txtSMStraight, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 450, 70, 25));

        txtLGStraight.setEditable(false);
        txtLGStraight.setBackground(new java.awt.Color(255, 255, 255));
        txtLGStraight.setEnabled(false);
        getContentPane().add(txtLGStraight, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 480, 70, 25));

        txtYahtzee.setEditable(false);
        txtYahtzee.setBackground(new java.awt.Color(255, 255, 255));
        txtYahtzee.setEnabled(false);
        getContentPane().add(txtYahtzee, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 510, 70, 25));

        txtBonus.setEditable(false);
        txtBonus.setBackground(new java.awt.Color(255, 255, 255));
        txtBonus.setEnabled(false);
        txtBonus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBonusActionPerformed(evt);
            }
        });
        getContentPane().add(txtBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 70, 25));

        txtTotalUpperScore.setEditable(false);
        txtTotalUpperScore.setBackground(new java.awt.Color(255, 255, 255));
        txtTotalUpperScore.setEnabled(false);
        getContentPane().add(txtTotalUpperScore, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 320, 70, 25));

        jLabel2.setFont(jLabel2.getFont());
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Bonus (Upper > 63)");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, 25));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total Upper Score");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, 25));

        txtChance.setEditable(false);
        txtChance.setBackground(new java.awt.Color(255, 255, 255));
        txtChance.setEnabled(false);
        getContentPane().add(txtChance, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 540, 70, 25));

        txtLowerScore.setEditable(false);
        txtLowerScore.setBackground(new java.awt.Color(255, 255, 255));
        txtLowerScore.setEnabled(false);
        getContentPane().add(txtLowerScore, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 570, 70, 25));

        txtGrandTotal.setEditable(false);
        txtGrandTotal.setBackground(new java.awt.Color(255, 255, 255));
        txtGrandTotal.setEnabled(false);
        getContentPane().add(txtGrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 600, 70, 25));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total Lower Score");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, -1, 25));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Grand Total");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, -1, 25));

        btnNewGame.setFont(new java.awt.Font("Goudy Stout", 0, 11)); // NOI18N
        btnNewGame.setText("FINISH");
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGameActionPerformed(evt);
            }
        });
        getContentPane().add(btnNewGame, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 530, 160, 40));

        txtTwos1.setEnabled(false);
        getContentPane().add(txtTwos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 70, 25));

        txtAces1.setEditable(false);
        txtAces1.setBackground(new java.awt.Color(255, 255, 255));
        txtAces1.setEnabled(false);
        getContentPane().add(txtAces1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 110, 70, 25));

        txtThrees1.setEditable(false);
        txtThrees1.setEnabled(false);
        getContentPane().add(txtThrees1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 170, 70, 25));

        txtFours1.setEditable(false);
        txtFours1.setEnabled(false);
        getContentPane().add(txtFours1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 200, 70, 25));

        txtSixes1.setEditable(false);
        txtSixes1.setEnabled(false);
        getContentPane().add(txtSixes1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 260, 70, 25));

        txtFives1.setEditable(false);
        txtFives1.setEnabled(false);
        getContentPane().add(txtFives1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 230, 70, 25));

        txt3OfAKind1.setEditable(false);
        txt3OfAKind1.setEnabled(false);
        getContentPane().add(txt3OfAKind1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 360, 70, 25));

        txt4OfAKind1.setEditable(false);
        txt4OfAKind1.setEnabled(false);
        getContentPane().add(txt4OfAKind1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 390, 70, 25));

        txtFullHouse1.setEditable(false);
        txtFullHouse1.setEnabled(false);
        getContentPane().add(txtFullHouse1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 420, 70, 25));

        txtSMStraight1.setEditable(false);
        txtSMStraight1.setEnabled(false);
        getContentPane().add(txtSMStraight1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 450, 70, 25));

        txtLGStraight1.setEditable(false);
        txtLGStraight1.setEnabled(false);
        getContentPane().add(txtLGStraight1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 480, 70, 25));

        txtYahtzee1.setEditable(false);
        txtYahtzee1.setEnabled(false);
        getContentPane().add(txtYahtzee1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 510, 70, 25));

        txtBonus1.setEditable(false);
        txtBonus1.setEnabled(false);
        txtBonus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBonus1ActionPerformed(evt);
            }
        });
        getContentPane().add(txtBonus1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 290, 70, 25));

        txtTotalUpperScore1.setEditable(false);
        txtTotalUpperScore1.setEnabled(false);
        getContentPane().add(txtTotalUpperScore1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 320, 70, 25));

        txtChance1.setEditable(false);
        txtChance1.setEnabled(false);
        getContentPane().add(txtChance1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 540, 70, 25));

        txtLowerScore1.setEditable(false);
        txtLowerScore1.setEnabled(false);
        getContentPane().add(txtLowerScore1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 570, 70, 25));

        txtGrandTotal1.setEditable(false);
        txtGrandTotal1.setEnabled(false);
        getContentPane().add(txtGrandTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 600, 70, 25));

        filler1.setAutoscrolls(true);
        filler1.setBackground(new java.awt.Color(255, 0, 0));
        filler1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(filler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 200, 540));

        filler2.setAutoscrolls(true);
        filler2.setBackground(new java.awt.Color(255, 0, 0));
        filler2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(filler2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 90, 540));

        jLabel1.setFont(new java.awt.Font("Goudy Stout", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("YAHTZEE");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("ENES BEK");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 610, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("1721221009");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 630, -1, -1));

        txtRivalName.setText("Rival");
        txtRivalName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRivalNameActionPerformed(evt);
            }
        });
        getContentPane().add(txtRivalName, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 80, -1));

        txtPlayerName.setText("Player");
        getContentPane().add(txtPlayerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 70, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRollActionPerformed

        int rollNumber;

        for (int i = 0; i < this.DICE_NUMBER; i++) {
            if (!keepingArray[i]) {
                rollNumber = myDices.rollSingleDice(i);
                diceBtns[i].setText(rollNumber + "");
            }
        }

        if (currentUIState == BEFORE_1st_ROLL) {
            manageUIState(BEFORE_2nd_ROLL);
        } else if (currentUIState == BEFORE_2nd_ROLL) {
            manageUIState(BEFORE_3rd_ROLL);
        } else if (currentUIState == BEFORE_3rd_ROLL) {
            manageUIState(AFTER_3rd_ROLL);
        }


    }//GEN-LAST:event_btnRollActionPerformed

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
        Data data = new Data(Data.DataType.Text);
        data.content = "01-" + txtAces.getText();
        Client.Send(data);

    }//GEN-LAST:event_tglbtnAcesActionPerformed

    private void tglbtnChanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnChanceActionPerformed
        int score = 0;

        game.setUsedLowerScoreCat(CHANCE, true);
        manageUIState(SCORING);

        score = game.addEmUp(myDices);

        game.setLowerScoreCat(CHANCE, score);
        this.lowerScoreTextArray[CHANCE].setText("" + score);
        this.showTotal();

        Data data = new Data(Data.DataType.Text);
        data.content = "13-" + txtChance.getText();
        Client.Send(data);
        game.nextTurn();
        if (game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS) {
            manageUIState(BEFORE_1st_ROLL);
        } else {
            manageUIState(GAME_OVER);
        }
    }//GEN-LAST:event_tglbtnChanceActionPerformed

    private void tglbtn3OfAKindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtn3OfAKindActionPerformed
        int score = 0;

        game.setUsedLowerScoreCat(THREES_OF_A_KIND, true);
        manageUIState(SCORING);

        if (game.is3OfAKind(myDices)) {
            score = game.addEmUp(myDices);
        }

        game.setLowerScoreCat(THREES_OF_A_KIND, score);

        this.lowerScoreTextArray[THREES_OF_A_KIND].setText("" + score);
        this.showTotal();

        Data data = new Data(Data.DataType.Text);
        data.content = "07-" + txt3OfAKind.getText();
        Client.Send(data);
        game.nextTurn();
        if (game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS) {
            manageUIState(BEFORE_1st_ROLL);
        } else {
            manageUIState(GAME_OVER);
        }
    }//GEN-LAST:event_tglbtn3OfAKindActionPerformed

    private void tglbtnTwosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnTwosActionPerformed
        scoreUpperCat(2);
        Data data = new Data(Data.DataType.Text);
        data.content = "02-" + txtTwos.getText();
        Client.Send(data);

    }//GEN-LAST:event_tglbtnTwosActionPerformed

    private void tglbtn4OfAKindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtn4OfAKindActionPerformed
        int score = 0;

        game.setUsedLowerScoreCat(FOURS_OF_A_KIND, true);
        manageUIState(SCORING);

        if (game.is4OfAKind(myDices)) {
            score = game.addEmUp(myDices);
        }

        game.setLowerScoreCat(FOURS_OF_A_KIND, score);

        this.lowerScoreTextArray[FOURS_OF_A_KIND].setText("" + score);
        this.showTotal();

        Data data = new Data(Data.DataType.Text);
        data.content = "08-" + txt4OfAKind.getText();
        Client.Send(data);
        game.nextTurn();
        if (game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS) {
            manageUIState(BEFORE_1st_ROLL);
        } else {
            manageUIState(GAME_OVER);
        }
    }//GEN-LAST:event_tglbtn4OfAKindActionPerformed

    private void txtBonusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBonusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBonusActionPerformed

    private void tglbtnSixesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnSixesActionPerformed
        scoreUpperCat(6);
        Data data = new Data(Data.DataType.Text);
        data.content = "06-" + txtSixes.getText();
        Client.Send(data);

    }//GEN-LAST:event_tglbtnSixesActionPerformed

    private void tglbtnThreesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnThreesActionPerformed
        scoreUpperCat(3);
        Data data = new Data(Data.DataType.Text);
        data.content = "03-" + txtThrees.getText();
        Client.Send(data);

    }//GEN-LAST:event_tglbtnThreesActionPerformed

    private void tglbtnFoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnFoursActionPerformed
        scoreUpperCat(4);
        Data data = new Data(Data.DataType.Text);
        data.content = "04-" + txtFours.getText();
        Client.Send(data);

    }//GEN-LAST:event_tglbtnFoursActionPerformed

    private void tglbtnFivesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnFivesActionPerformed
        scoreUpperCat(5);
        Data data = new Data(Data.DataType.Text);
        data.content = "05-" + txtFives.getText();
        Client.Send(data);

    }//GEN-LAST:event_tglbtnFivesActionPerformed

    private void tglbtnYahtzeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnYahtzeeActionPerformed
        int score = 0;

        game.setUsedLowerScoreCat(YAHTZEE, true);
        manageUIState(SCORING);

        if (game.isOfAKind(myDices, 5)) {
            score = 50;
        }

        game.setLowerScoreCat(YAHTZEE, score);
        this.lowerScoreTextArray[YAHTZEE].setText("" + score);
        this.showTotal();

        Data data = new Data(Data.DataType.Text);
        data.content = "012-" + txtYahtzee.getText();
        Client.Send(data);
        game.nextTurn();
        if (game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS) {
            manageUIState(BEFORE_1st_ROLL);
        } else {
            manageUIState(GAME_OVER);
        }
    }//GEN-LAST:event_tglbtnYahtzeeActionPerformed

    private void tglbtnFullHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnFullHouseActionPerformed
        int score = 0;

        game.setUsedLowerScoreCat(FULL_HOUSE, true);
        manageUIState(SCORING);

        if (game.isFullHouse(myDices)) {
            score = 25;
        }

        game.setLowerScoreCat(FULL_HOUSE, score);
        this.lowerScoreTextArray[FULL_HOUSE].setText("" + score);
        this.showTotal();

        Data data = new Data(Data.DataType.Text);
        data.content = "09-" + txtFullHouse.getText();
        Client.Send(data);
        game.nextTurn();
        if (game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS) {
            manageUIState(BEFORE_1st_ROLL);
        } else {
            manageUIState(GAME_OVER);
        }
    }//GEN-LAST:event_tglbtnFullHouseActionPerformed

    private void tglbtnLGStraightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnLGStraightActionPerformed
        int score = 0;

        game.setUsedLowerScoreCat(LARGE_STRAIGHT, true);
        manageUIState(SCORING);

        if (game.isLargeStraight(myDices)) {
            score = 40;
        }

        game.setLowerScoreCat(LARGE_STRAIGHT, score);
        this.lowerScoreTextArray[LARGE_STRAIGHT].setText("" + score);
        this.showTotal();

        Data data = new Data(Data.DataType.Text);
        data.content = "11-" + txtLGStraight.getText();
        Client.Send(data);
        game.nextTurn();
        if (game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS) {
            manageUIState(BEFORE_1st_ROLL);
        } else {
            manageUIState(GAME_OVER);
        }
    }//GEN-LAST:event_tglbtnLGStraightActionPerformed

    private void tglbtnSMStraightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglbtnSMStraightActionPerformed
        int score = 0;

        game.setUsedLowerScoreCat(SMALL_STRAIGHT, true);
        manageUIState(SCORING);

        if (game.isSmallStraight(myDices)) {
            score = 30;
        }

        game.setLowerScoreCat(SMALL_STRAIGHT, score);
        this.lowerScoreTextArray[SMALL_STRAIGHT].setText("" + score);
        this.showTotal();

        Data data = new Data(Data.DataType.Text);
        data.content = "10-" + txtSMStraight.getText();
        Client.Send(data);
        game.nextTurn();
        if (game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS) {
            manageUIState(BEFORE_1st_ROLL);
        } else {
            manageUIState(GAME_OVER);
        }
    }//GEN-LAST:event_tglbtnSMStraightActionPerformed

    private void txtBonus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBonus1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBonus1ActionPerformed

    private void txtRivalNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRivalNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRivalNameActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        Client.Stop();
    }//GEN-LAST:event_formWindowClosed

    private void btnNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGameActionPerformed
        manageUIState(GAME_OVER);
        //manageUIState(BEFORE_1st_ROLL);
    }//GEN-LAST:event_btnNewGameActionPerformed

    public void scoreUpperCat(int category) {
        int score = 0;

        game.setUsedUpperScoreCat(category, true);

        manageUIState(SCORING);

        for (int i = 0; i < DICE_NUMBER; i++) {
            if (myDices.getDiceValue(i) == category) {
                score += category;
            }
        }

        game.setUpperScoreCat(category, score);
        this.upperScoreTextArray[category].setText("" + score);
        this.showTotal();
        game.nextTurn();
        if (game.getCurrentTurnNum() < GameModel.MAX_NUM_TURNS) {
            manageUIState(BEFORE_1st_ROLL);
        } else {
            manageUIState(GAME_OVER);
        }
    }

    public void showTotal() {

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
            java.util.logging.Logger.getLogger(YahtzeeFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(YahtzeeFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(YahtzeeFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(YahtzeeFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    public javax.swing.JTextField txt3OfAKind1;
    private javax.swing.JTextField txt4OfAKind;
    public javax.swing.JTextField txt4OfAKind1;
    private javax.swing.JTextField txtAces;
    public javax.swing.JTextField txtAces1;
    private javax.swing.JTextField txtBonus;
    public javax.swing.JTextField txtBonus1;
    private javax.swing.JTextField txtChance;
    public javax.swing.JTextField txtChance1;
    private javax.swing.JTextField txtFives;
    public javax.swing.JTextField txtFives1;
    private javax.swing.JTextField txtFours;
    public javax.swing.JTextField txtFours1;
    private javax.swing.JTextField txtFullHouse;
    public javax.swing.JTextField txtFullHouse1;
    public javax.swing.JTextField txtGrandTotal;
    public javax.swing.JTextField txtGrandTotal1;
    private javax.swing.JTextField txtLGStraight;
    public javax.swing.JTextField txtLGStraight1;
    private javax.swing.JTextField txtLowerScore;
    public javax.swing.JTextField txtLowerScore1;
    private javax.swing.JTextField txtPlayerName;
    public javax.swing.JTextField txtRivalName;
    private javax.swing.JTextField txtSMStraight;
    public javax.swing.JTextField txtSMStraight1;
    private javax.swing.JTextField txtSixes;
    public javax.swing.JTextField txtSixes1;
    private javax.swing.JTextField txtThrees;
    public javax.swing.JTextField txtThrees1;
    private javax.swing.JTextField txtTotalUpperScore;
    public javax.swing.JTextField txtTotalUpperScore1;
    private javax.swing.JTextField txtTwos;
    public javax.swing.JTextField txtTwos1;
    private javax.swing.JTextField txtYahtzee;
    public javax.swing.JTextField txtYahtzee1;
    // End of variables declaration//GEN-END:variables

}
