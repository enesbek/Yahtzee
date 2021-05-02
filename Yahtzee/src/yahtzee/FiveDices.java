package yahtzee;

/**
 *
 * @author enesb
 */
public class FiveDices {
    
    private final int numberOfDices = 5;
    private Dice[] diceArray;
    
    public FiveDices(){
        this.diceArray = new Dice[this.numberOfDices];
        for(int i = 0; i < this.numberOfDices; i++)
            diceArray[i] = new Dice();
    }
    
    public int getDiceValue(int diceNumber){
        //Get value from Dice class
        return diceArray[diceNumber].getValueOfDice();
    }
    
    public int rollSingleDice(int diceNumber){
        //rollDice method comes from Dice class
        return diceArray[diceNumber].rollDice();
    }
    
    public int rollFiveDices(){
        //rolling 5 dices.
        int sum = 0; 
        for(int i = 0; i < this.getNumberOfDices(); i++)
            sum += rollSingleDice(i);
        
        return sum;
    }
    
    public int addEmUp(){
        int sum = 0;
        for(int i = 0; i < this.getNumberOfDices(); i++)
            sum += getDiceValue(i);
        
        return sum;
    }
    
    public int[] buildFreqTable(){
        
        // 6 is side number. It is constant
        int[] freqTable = new int[6 + 1];
        
        for(int i = 0; i < getNumberOfDices(); i++)
            freqTable[diceArray[i].getValueOfDice()]++;
        
        return freqTable;
    }

    public int getNumberOfDices() {
        return numberOfDices;
    }
}
