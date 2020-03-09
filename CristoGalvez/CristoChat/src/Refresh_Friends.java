
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Charlowit
 */
public class Refresh_Friends extends Thread {
    
    public static ArrayList<Friend> friendlist;
    private Controladora controladora;
    private DefaultListModel modelo;
    private String user;
    
    Refresh_Friends(ArrayList<Friend> friendlist, Controladora controladora, String user){
        this.friendlist = friendlist;
        this.controladora = controladora;
        this.user= user;
        
    }
    @Override
    public void run(){
        
        while(true){
            modelo = new DefaultListModel();
            try {
                sleep(20000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Refresh_Friends.class.getName()).log(Level.SEVERE, null, ex);
            }
            for(Friend amigo: friendlist){
                String [] splitfriend = amigo.getUser_amigo().split(" ");
                controladora.generateStringForStatus(user,splitfriend[0]);
                    
            }
        }
        
    }
}
