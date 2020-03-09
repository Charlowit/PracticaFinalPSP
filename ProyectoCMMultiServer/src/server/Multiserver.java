/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import javax.swing.SwingWorker;

/**
 *
 * @author Charlowit
 */
public class Multiserver extends SwingWorker<Void, Void>{
    
        public int puerto;
        public boolean listening;
        public static ArrayList<MultiserverThread> arrayhebras;
        
        Multiserver(int puerto){
          this.puerto = puerto;
          listening = true;
          this.arrayhebras = new ArrayList();
        }

        
        public void desconexion(){
            for (int i = 0 ; i < arrayhebras.size() ; i++){
                this.arrayhebras.get(i).stopHebra();
                
            }
        }

    @Override
    protected Void doInBackground() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            Close_Session close_session = new Close_Session();
            close_session.start();
            while (listening) {
                System.out.println("Creo el socket");
                arrayhebras.add(new MultiserverThread(serverSocket.accept()));
                System.out.println("Conexion aceptada");
                arrayhebras.get(arrayhebras.size() - 1).start();
            }
            } catch (IOException e) {
                System.err.println("Could not listen on port " + puerto);
                System.exit(-1);
            }
        return null;
    }
    
}
