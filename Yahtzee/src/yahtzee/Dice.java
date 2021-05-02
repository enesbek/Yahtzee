package yahtzee;

import java.util.Random;
/**
 *
 * @author enesb
 */
public class Dice {
    
    private final int sideNumber = 6;
    private int valueOfDice;
    
    public Dice(){
        this.valueOfDice = 0;
    }
    
    public int rollDice(){
        Random rand = new Random();
        this.valueOfDice = rand.nextInt(this.getSideNumber()) + 1;
        return this.getValueOfDice();
    } 
    
    /**
     * @return the sideNumber
     */
    public int getSideNumber() {
        return this.sideNumber;
    }

    /**
     * @return the valueOfDice
     */
    public int getValueOfDice() {
        return this.valueOfDice;
    }

    
    
}
