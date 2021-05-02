package yahtzee;

public class Test {
    
    public static void main(String[] args) {
        
       Dice myDice = new Dice();
       System.out.println(myDice.rollDice());
        
       FiveDices myFiveDices = new FiveDices();
       for(int i = 0; i < 100; i++)
       System.out.println("Sum of dices: " + myFiveDices.rollFiveDices());
    }
}
