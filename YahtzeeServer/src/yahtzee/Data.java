package yahtzee;

/**
 *
 * @author enesb
 */
public class Data implements java.io.Serializable {
    //mesaj tipleri enum 
    public static enum DataType {None, Name, Disconnect,RivalConnected, Text, Score, Bitis,Start,}
    //mesajın tipi
    public DataType type;
    //mesajın içeriği obje tipinde ki istenilen tip içerik yüklenebilsin
    public Object content;
    public Data(DataType d)
    {
        this.type=d;
    }
}
