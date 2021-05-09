/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yahtzee;

/**
 *
 * @author enesb
 */
public class Data implements java.io.Serializable {
    
    public static enum DataType{
        None, Name, Disconnect,RivalConnected, Text, Score, Bitis,Start,  
    }
    
    public DataType type;
    public Object content;
    public Data(DataType t)
    {
        this.type=t;
    }
    
    
}
