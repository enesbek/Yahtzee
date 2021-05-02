package yahtzee;
/**
 *
 * @author enesb
 */
public class GameModel {

    private int[] upperScoreCategories;
    private int[] lowerScoreCategories;
    private boolean[] usedUpperScoreCategories;
    private boolean[] usedLowerScoreCategories;
    private int currentTurnNum;    
    
    public static final int BONUS = 35;
    public static final int NUM_UPPER_SCORE_CATS = 6;
    public static final int NUM_LOWER_SCORE_CATS = 7;
    public static final int MAX_NUM_TURNS = NUM_UPPER_SCORE_CATS + NUM_LOWER_SCORE_CATS;
    
    private int sumUpperScore;
    private int sumLowerScore;
    private int grandTotal;
    private int bonus;
    
    public GameModel(){
        
        upperScoreCategories = new int[NUM_UPPER_SCORE_CATS + 1];
        lowerScoreCategories = new int[NUM_LOWER_SCORE_CATS];
        
        usedUpperScoreCategories = new boolean[NUM_UPPER_SCORE_CATS + 1];
        usedLowerScoreCategories = new boolean[NUM_LOWER_SCORE_CATS];
        
        currentTurnNum = 1;
    }
    
    public boolean is3OfAKind(FiveDices myDice){
        int[] freqTable;
        freqTable = myDice.buildFreqTable();
        
        boolean found3OfAKind = false;
        
        for(int i = 0; i < myDice.getNumberOfDices(); i++)
            if(freqTable[i] >= 3)
                found3OfAKind = true;
        
        return found3OfAKind;
        
    }
    
    public boolean is4OfAKind(FiveDices myDice){
        int[] freqTable;
        freqTable = myDice.buildFreqTable();
        
        boolean found4OfAKind = false;
        
        for(int i = 0; i < myDice.getNumberOfDices(); i++)
            if(freqTable[i] >= 4)
                found4OfAKind = true;
        
        return found4OfAKind;
    }
    
    public boolean isOfAKind(FiveDices myDice, int kind){
        int[] freqTable;
        freqTable = myDice.buildFreqTable();
        
        boolean foundOfAKind = false;
        
        for(int i = 0; i < myDice.getNumberOfDices(); i++)
            if(freqTable[i] >= kind)
                foundOfAKind = true;
        
        return foundOfAKind;
    }
    
    public boolean isFullHouse(FiveDices myDice){
        int[] freqTable;
        freqTable = myDice.buildFreqTable();
        
        boolean foundThree = false;
        boolean foundTwo = false;
        
        for(int i = 0; i < myDice.getNumberOfDices(); i++)
            if(freqTable[i] == 3)
                foundThree = true;
            else if(freqTable[i] == 2)
                foundTwo = true;
        
        return foundThree && foundTwo;
    }
    
    public boolean isLargeStraight(FiveDices myDice){
        int[] freqTable;
        freqTable = myDice.buildFreqTable();
        
        return freqTable[2] == 1 && freqTable[3] == 1 && freqTable[4] == 1 &&
                freqTable[5] == 1;        
    }
    
    public boolean isSmallStraight(FiveDices myDice){
        int[] freqTable;
        freqTable = myDice.buildFreqTable();
        boolean returnValue;
        
        for(int i = 0; i < myDice.getNumberOfDices(); i++)
            if(freqTable[i] > 1)
                freqTable[i] = 1;
        
        if(freqTable[3] == 1 && freqTable[4] == 1)
            if(freqTable[1] == 1 && freqTable[2] == 1)
                returnValue = true;
            else if(freqTable[2] == 1 && freqTable[5] == 1)
                returnValue = true;
            else if(freqTable[5] == 1 && freqTable[6] == 1)
                returnValue = true;
            else 
                returnValue = false;
        else
            returnValue = false;
        
        return returnValue;
    }
    
    public void setUsedLowerScoreCat(int index, boolean used){
        usedLowerScoreCategories[index] = used;
    }
    
    public void setLowerScoreCat(int index, int score){
        lowerScoreCategories[index] = score;
    }
    
    public void setUpperScoreCat(int index, int score){
        upperScoreCategories[index] = score;
    }
    
    public void setUsedUpperScoreCat(int index, boolean used){
        usedUpperScoreCategories[index] = used;
    }
    
    public boolean getUsedLowerScoreCatState(int i){
        return usedLowerScoreCategories[i];
    }
    
    public boolean getUsedUpperScoreCatState(int i){
        return usedUpperScoreCategories[i];
    }
    
    public int getCurrentTurnNum(){
        return currentTurnNum; 
    }
    
    public void nextTurn(){
        currentTurnNum++;
    }
    
    public int addEmUp(FiveDices myDice){
        int sum = 0;
        
        for(int i = 0; i < myDice.getNumberOfDices(); i++)
            sum += myDice.getDiceValue(i);
        
        return sum;
    }
    
    public void UpdateTotals(){
        
        setSumLowerScore(0); 
        setSumUpperScore(0);
        setGrandTotal(0);
        
        for(int i = 1; i <= NUM_UPPER_SCORE_CATS; i++)
            setSumUpperScore(getSumUpperScore() + upperScoreCategories[i]);
        
        if(getSumUpperScore() >= 63)
            setSumUpperScore(getSumUpperScore() + getBonus());
        
        for(int i = 0; i < NUM_LOWER_SCORE_CATS; i++)
            setSumLowerScore(getSumLowerScore() + lowerScoreCategories[i]);
        
        setGrandTotal(getSumUpperScore() + getSumLowerScore());
    }
    
    public int getSumUpperScore() {
        return sumUpperScore;
    }

    public void setSumUpperScore(int sumUpperScore) {
        this.sumUpperScore = sumUpperScore;
    }

    public int getSumLowerScore() {
        return sumLowerScore;
    }

    public void setSumLowerScore(int sumLowerScore) {
        this.sumLowerScore = sumLowerScore;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
    
    public void clearAllUpperScoreCat(){
        for(int i = 1; i <= NUM_UPPER_SCORE_CATS; i++)
            upperScoreCategories[i] = 0;
    }
    
    public void clearAllLowerScoreCat(){
        for(int i = 0; i < NUM_LOWER_SCORE_CATS; i++)
            lowerScoreCategories[i] = 0;
    }
    
    public void clearUsedScoreCat(){
        for(int i = 0; i < NUM_LOWER_SCORE_CATS; i++)
            usedLowerScoreCategories[i] = false;
        
        for(int i = 1; i <= NUM_UPPER_SCORE_CATS; i++)
            usedUpperScoreCategories[i] = false;
    }
    
    public void resetTurn(){
        currentTurnNum = 1;
    }
}
