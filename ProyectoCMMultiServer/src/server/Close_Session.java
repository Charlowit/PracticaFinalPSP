/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Charlowit
 */
public class Close_Session extends Thread {
    
    
    Close_Session(){
        
    }
    @Override
    public void run(){
        try {
            ArrayList<String> friends_connected = new ArrayList();
            User_Controller user_controller = new User_Controller();
            while(true){
                sleep(15000);
                friends_connected = user_controller.obtenerListaUsuariosConectados();
     
//                System.out.println("**************Amigos conectados: " + friends_connected.size());
//                System.out.println("**************Tamanio de hebras servidoras: " + Multiserver.arrayhebras.size());
                if(friends_connected.size() > 0 && Multiserver.arrayhebras.size() > 0){
                    boolean usuarioNoencontrado=false;
                    
                    if (Multiserver.arrayhebras.size()>friends_connected.size()){

                        for(int i=0; i<Multiserver.arrayhebras.size(); i++){
                            usuarioNoencontrado=false;
                            for(int j=0; j<friends_connected.size()&&usuarioNoencontrado==false; j++){
//                                System.out.println("*********************Amigo: "+j+" "+friends_connected.get(j));
//                                System.out.println("*********************Hebra id_user: "+ Multiserver.arrayhebras.get(i).getName());
                                if (friends_connected.get(j).equals(Multiserver.arrayhebras.get(i).getName())){
                                    usuarioNoencontrado=true;
                                }
//                                System.out.println("*******************************usuarioNoencontrado: "+ usuarioNoencontrado);
                            }
                            if (usuarioNoencontrado==false){
                                    user_controller.changeCloseSession(Multiserver.arrayhebras.get(i).getName());
                                    System.out.println("he cambiado el estado");
                            }
                        }
                    }else if (Multiserver.arrayhebras.size()<friends_connected.size()){
                        for(int i=0; i<friends_connected.size(); i++){
                            usuarioNoencontrado=false;
                            for(int j=0; j<Multiserver.arrayhebras.size() && usuarioNoencontrado==false; j++){
//                                System.out.println("*********************Amigo: "+j+" "+friends_connected.get(i));
//                                System.out.println("*********************Hebra id_user: "+ Multiserver.arrayhebras.get(j).getName());
                                if (friends_connected.get(i).equals(Multiserver.arrayhebras.get(j).getName())){
                                    usuarioNoencontrado=true;  
                                }
                            }
//                            System.out.println("*******************************usuarioNoencontrado: "+ usuarioNoencontrado);
                                if (usuarioNoencontrado==false){
                                    user_controller.changeCloseSession(friends_connected.get(i));
                                    System.out.println("he cambiado el estado");
                                }
                        }   
                    }    
                }
                else if(friends_connected.size() == 0 && Multiserver.arrayhebras.size() > 0){
                          for(int h = 0 ; h < Multiserver.arrayhebras.size() ; h++){
                              user_controller.changeCloseSession(Multiserver.arrayhebras.get(h).getName());
                          }
                }
                else{
                    
                    for(int k = 0 ; k < friends_connected.size() ; k++ ){
                        user_controller.changeCloseSession(friends_connected.get(k));
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Close_Session.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Close_Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
