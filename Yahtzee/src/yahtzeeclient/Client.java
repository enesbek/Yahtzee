package yahtzeeclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import yahtzee.Data;
import yahtzee.EndFrame;
import yahtzee.OpenFrame;
import yahtzee.YahtzeeFrame;
import static yahtzeeclient.Client.sInput;

/**
 *
 * @author enesb
 */
public class Client {

    //her clientın bir soketi olmalı
    public static Socket socket;

    //verileri almak için gerekli nesne
    public static ObjectInputStream sInput;
    //verileri göndermek için gerekli nesne
    public static ObjectOutputStream sOutput;
    //serverı dinleme thredi 
    public static Listen listenMe;

    public static void Start(String ip, int port) {
        try {
            // Client Soket nesnesi
            Client.socket = new Socket(ip, port);
            Client.Display("Servera bağlandı");
            // input stream
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            // output stream
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listenMe = new Listen();
            Client.listenMe.start();

            //ilk mesaj olarak isim gönderiyorum
            Data data = new Data(Data.DataType.Name);
            data.content = OpenFrame.thisFrame.txtName.getText() + "";
            Client.Send(data);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //client durdurma fonksiyonu
    public static void Stop() {
        try {
            if (Client.socket != null) {
                Client.listenMe.stop();
                Client.socket.close();
                Client.sOutput.flush();
                Client.sOutput.close();

                Client.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void Display(String msg) {

        System.out.println(msg);

    }

    //mesaj gönderme fonksiyonu
    public static void Send(Data data) {
        try {
            Client.sOutput.writeObject(data);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

class Listen extends Thread {

    public void run() {
        //soket bağlı olduğu sürece dön
        while (Client.socket.isConnected()) {
            try {
                //mesaj gelmesini bloking olarak dinyelen komut
                Data received = (Data) (sInput.readObject());
                //mesaj gelirse bu satıra geçer
                //mesaj tipine göre yapılacak işlemi ayır.
                switch (received.type) {
                    case Name:
                        break;
                    case RivalConnected:
                        //String name = received.content.toString() + "";
                        //YahtzeeFrame.thisYahtzeeFrame.txtRivalName.setText(name);

                        /*Game.ThisGame.btn_pick.setEnabled(true);
                        Game.ThisGame.btn_send_message.setEnabled(true);
                        Game.ThisGame.tmr_slider.start();*/
                        break;
                    case Disconnect:
                        break;
                    case Text:
                        String x = received.content.toString();
                        String[] a = x.split("-");
                        switch (a[0]) {
                            case "01":
                                YahtzeeFrame.thisYahtzeeFrame.txtAces1.setText(a[1]);
                                break;
                            case "02":
                                YahtzeeFrame.thisYahtzeeFrame.txtTwos1.setText(a[1]);
                                break;
                            case "03":
                                YahtzeeFrame.thisYahtzeeFrame.txtThrees1.setText(a[1]);
                                break;
                            case "04":
                                YahtzeeFrame.thisYahtzeeFrame.txtFours1.setText(a[1]);
                                break;
                            case "05":
                                YahtzeeFrame.thisYahtzeeFrame.txtFives1.setText(a[1]);
                                break;
                            case "06":
                                YahtzeeFrame.thisYahtzeeFrame.txtSixes1.setText(a[1]);
                                break;
                            case "07":
                                YahtzeeFrame.thisYahtzeeFrame.txt3OfAKind1.setText(a[1]);
                                break;
                            case "08":
                                YahtzeeFrame.thisYahtzeeFrame.txt4OfAKind1.setText(a[1]);
                                break;
                            case "09":
                                YahtzeeFrame.thisYahtzeeFrame.txtFullHouse1.setText(a[1]);
                                break;
                            case "10":
                                YahtzeeFrame.thisYahtzeeFrame.txtSMStraight1.setText(a[1]);
                                break;
                            case "11":
                                YahtzeeFrame.thisYahtzeeFrame.txtLGStraight1.setText(a[1]);
                                break;
                            case "12":
                                YahtzeeFrame.thisYahtzeeFrame.txtYahtzee1.setText(a[1]);
                                break;
                            case "13":
                                YahtzeeFrame.thisYahtzeeFrame.txtChance1.setText(a[1]);
                                break;
                            case "score":
                                EndFrame.rivalScore = a[1];
                        }

                        break;
                    case Score:
                        YahtzeeFrame.thisYahtzeeFrame.rivalScore = (int) received.content;
                        break;

                    case Bitis:
                        break;

                }

            } catch (IOException ex) {

                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //Client.Stop();
                break;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //Client.Stop();
                break;
            }
        }

    }

}
