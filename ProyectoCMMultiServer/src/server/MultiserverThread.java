/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Charlowit
 */
public class MultiserverThread extends Thread{
    
    private Socket socket = null;
    private Lock mutex= new ReentrantLock();
    
    PrintWriter out;
    BufferedReader in;
    public MultiserverThread(Socket socket) throws IOException {
        super("MultiserverThread");
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
     
    public void run() {
 
        try{
            ArrayList<String> salidas = new ArrayList();
            String inputLine, outputLine = null;
            Protocolo protocolo = new Protocolo();
 
            while ((inputLine = in.readLine()) != null) {
                mutex.lock();
                System.out.println(socket.getInetAddress().getHostAddress() + " " + inputLine);
                salidas = protocolo.processInput(inputLine, this);
                
                if (salidas.size()>0){
                    if(salidas.get(0).equals("OK_SEND!")){

                        for(int i=1 ; i < salidas.size() ; i++){
                            outputLine = salidas.get(i);
                            out.println(outputLine);
                            System.out.println("Server: "+ outputLine);
                            //Debug.debugServer.setText(Debug.debugServer.getText() + outputLine + "\n");
                        }
                    }
                    else if(salidas.get(0).equals("GETPHOTO!")){
                        outputLine = salidas.get(1);
                        out.println(outputLine);
                        System.out.println("Server: "+ outputLine);
                        //Debug.debugServer.setText(Debug.debugServer.getText() + outputLine + "\n");
                        for(int i=2; i < salidas.size()-1 ; i++){
                           outputLine = salidas.get(i);
                            out.println(outputLine);
                            System.out.println("Server: "+ outputLine);
                            
                        }
                        outputLine = salidas.get(salidas.size()-1);
                        System.out.println("Server: "+ outputLine);
                        out.println(outputLine);
                        //Debug.debugServer.setText(Debug.debugServer.getText() + outputLine + "\n");
                    }
                    else{
                       
                        Debug.debugServer.setText(Debug.debugServer.getText()+ socket.getInetAddress().getHostAddress() + " " + inputLine + "\n");
                        outputLine = salidas.get(0);
                        out.println(outputLine);
                        System.out.println("Server: "+ outputLine);
                        //Debug.debugServer.setText(Debug.debugServer.getText() + outputLine + "\n");
                        int wrongPackage = outputLine.indexOf("SERVER");
                        String wrongLine = outputLine.substring(wrongPackage);
                        if (wrongLine.equals("SERVER#ERROR#BAD_LOGIN"))
                            break;
                        if(wrongLine.equals("SERVER#ERROR#BAD_MSGPKG")){
                            break;
                        }     
                    }
                }
                
                mutex.unlock();
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                this.socket.close();
                this.setName("removed");
                Multiserver.arrayhebras.remove(this);
            } catch (IOException ex) {
                Logger.getLogger(MultiserverThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MultiserverThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MultiserverThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiserverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void stopHebra(){
            try {
                this.socket.close();
            } catch (IOException ex) {
                //Debug.debugServer.setText(ex + "\n" );
                Logger.getLogger(MultiserverThread.class.getName()).log(Level.SEVERE, null, ex);
            }
    
    }
    
}
