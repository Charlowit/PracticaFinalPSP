/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Charlowit
 */
public class Protocolo {
    
    private static final int TRYCONNECT = 0;
    private static final int REFRESHFRIENDS = 1;
    private static final int GETALLMESSAGE = 2;
    private static final int OKSEND = 3;
    private static final int NOTHING = 4;
    private static final int GIVEDATESFROMUSER = 5;
    private static final int INSERTMESSAGEONDB = 6;
    private static final int GETPHOTO = 7;

    private int estado = NOTHING;

    public static String login = "";
    public static String password = "";
    public String user_orig = "";
    public String user_destino = "";
    public static User userconnected = new User();
    public ArrayList<String> recorrer = new ArrayList();
    public ArrayList<Friend> friendlist = new ArrayList();
    public ArrayList<User> userFriends = new ArrayList();
    public ArrayList<Message> message = new ArrayList();
    String dateformessage  = "";
    int tamanio = 0;
    String textmessage;
    
    private int contador=0;
    
    public void splitString(String entrada) throws SQLException, IOException {

        String nuevaCadena;
        if (entrada.startsWith("PROTOCOLCRISTOMESSENGER1.0#")){
            int posicion= entrada.indexOf("CLIENT");
            if (posicion>=0){
                nuevaCadena = entrada.substring(posicion);
                String [] usuariofinal = nuevaCadena.split("#");
                if(usuariofinal[1].equals("LOGIN")){
                    login=usuariofinal[2];
                    password=usuariofinal[3];
                    this.estado=TRYCONNECT;
                }
                else if(usuariofinal[1].equals("MSGS")){
                    if(usuariofinal[usuariofinal.length-1].equals("OK_SEND!")){
                        this.estado = OKSEND; 
                    }
                    else if(usuariofinal[usuariofinal.length-1].equals("ALL_RECEIVED")){
                        Debug.debugServer.setText(Debug.debugServer.getText() + " ALL_MESSAGE_RECIVED \n");
                        this.estado = NOTHING; 
                    }
                    else{
                        this.estado = GETALLMESSAGE;
                        user_orig=usuariofinal[2];
                        user_destino=usuariofinal[3];
                        dateformessage = usuariofinal[4];  
                    }
                }
                else if(usuariofinal[1].equals("STATUS")){
                    this.estado = REFRESHFRIENDS;
                    user_destino = usuariofinal[3];

                }
                else if(usuariofinal[1].equals("ALLDATA_USER")){
                    this.estado = GIVEDATESFROMUSER;
                    user_destino = usuariofinal[2];


                }
                else if(usuariofinal[1].equals("CHAT")){
                    this.estado = INSERTMESSAGEONDB;
                    user_orig = usuariofinal[2];
                    user_destino = usuariofinal[3];
                    textmessage = usuariofinal[4];
                }
                else if (usuariofinal[1].equals("GET_PHOTO")){
                    this.estado = GETPHOTO;
                    this.getPhoto(nuevaCadena);

                }
                else if(usuariofinal[1].equals("PHOTO_RECEIVED")){
                    this.estado = NOTHING;
                    
                }
            }
        }
    }
    
    public void getPhoto(String cadena) throws SQLException, IOException{
        User_Controller user_controller = new User_Controller();
        FileInputStream input = null;
        Date date = new Date();
        DateFormat fechayhora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String [] id_user = cadena.split("#");
        String photo = user_controller.getPhoto(id_user[2]);
        
        String line = "";
        int cont = 0;
        int totalByte=0;
        ArrayList<String> arrayLines = new ArrayList<String>();
        ArrayList<String> encodeLines = new ArrayList<String>();
        
        try { 
            input = new FileInputStream(photo);
            int valor = input.read();
            while(valor!=-1){
                if (cont > 511) {
                    arrayLines.add(line);
                    line = "";
                    cont = 0;
                }
                line += (char)valor;
                valor=input.read();
                cont++;
                totalByte++;
            }
            if (cont > 0) {
                arrayLines.add(line);
                cont = 0;
            }
            this.recorrer.clear();
            this.recorrer.add("GETPHOTO!");
            this.recorrer.add("PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#SERVER#STARTING_MULTIMEDIA_TRANSMISSION_TO#"+id_user[2]);
            for (String s : arrayLines) {
                encodeLines.add(Base64.getEncoder().encodeToString(s.getBytes()));
                this.recorrer.add("PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#SERVER#RESPONSE_MULTIMEDIA#"+id_user[2]+"#"+totalByte+"#"+s.length()+"#"+encodeLines.get(encodeLines.size()-1).toString());
            }
            this.recorrer.add("PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#SERVER#ENDING_MULTIMEDIA_TRANSMISSION#"+id_user[2]);
        } catch (FileNotFoundException ex) {
            
                input = new FileInputStream("data/default.jpg");
                int valor = input.read();
                while(valor!=-1){
                    if (cont > 511) {
                        arrayLines.add(line);
                        line = "";
                        cont = 0;
                    }
                    line += (char)valor;
                    valor=input.read();
                    cont++;
                    totalByte++;
                }
                if (cont > 0) {
                    arrayLines.add(line);
                    cont = 0;
                }
                this.recorrer.clear();
                this.recorrer.add("GETPHOTO!");
                this.recorrer.add("PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#SERVER#STARTING_MULTIMEDIA_TRANSMISSION_TO#"+id_user[2]);
                for (String s : arrayLines) {
                    encodeLines.add(Base64.getEncoder().encodeToString(s.getBytes()));
                    this.recorrer.add("PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#SERVER#RESPONSE_MULTIMEDIA#"+id_user[2]+"#"+totalByte+"#"+s.length()+"#"+encodeLines.get(encodeLines.size()-1).toString());
                }
                this.recorrer.add("PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#SERVER#ENDING_MULTIMEDIA_TRANSMISSION#"+id_user[2]);
        
            
//            Logger.getLogger(Protocolo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(Protocolo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public String getAllDataUser() throws SQLException{
        Date date = new Date();
        DateFormat fechayhora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User_Controller user_controller = new User_Controller();
        User usuario;
        usuario = user_controller.obtenerUsuario(user_destino);
        String exit = "PROTOCOLCRISTOMESSENGER1.0#" + fechayhora.format(date) + "#SERVER#ALLDATA_USER#" + usuario.getId_user() + "#" + usuario.getName()+"#"+usuario.getSurname1()+"#"+usuario.getSurname2();
        System.out.println(exit);
        return exit;
       
       
    }
    
    public void getAllMessageFromUser(String theOutput, String user_orig, String user_destino) throws SQLException{
    
        Message_Controller message_controller = new Message_Controller();
        message_controller.getMessagesFromUser(message, user_orig, user_destino);
        tamanio = message.size();    
        
    }
    
    public String getStatusFriends(String user_amigo) throws SQLException{
        User_Controller user_controller = new User_Controller();
        String theOutput = "";
        boolean exito = user_controller.estaConnectado(user_amigo);
        if(exito == true){ 
            theOutput += user_amigo+ "#CONNECTED";
        }
        else{        
            theOutput += user_amigo+ "#NOT_CONNECTED";
        }
        
        return theOutput;
    
    }
    
    public String getFriendsConnected(String theOutput, User_Controller user_controller) throws SQLException{
            Friend_Controller getAllFriends = new Friend_Controller();
            getAllFriends.obtenerAmigos(friendlist, login);
            theOutput= this.login +  "#FRIENDS#" + friendlist.size()+"#";
            for(int i = 0 ; i < friendlist.size(); i++){
                        boolean exito = user_controller.estaConnectado(friendlist.get(i).getUser_amigo());
                        if(exito == true){
                            if (i == friendlist.size()-1)
                            theOutput += friendlist.get(i).getUser_amigo()+ "#CONNECTED";
                            else{
                            theOutput += friendlist.get(i).getUser_amigo()+ "#CONNECTED#";
                            }
                        }
                        else{
                            if (i == friendlist.size()-1)
                            theOutput += friendlist.get(i).getUser_amigo()+ "#NOT_CONNECTED";
                            else{
                            theOutput += friendlist.get(i).getUser_amigo()+ "#NOT_CONNECTED#";
                            }
                        }
                        
            } 
            return theOutput;
    }

    public synchronized ArrayList<String> processInput(String theInput, MultiserverThread hebra) throws SQLException, IOException, ParseException, InterruptedException {
        
        while(contador==1){
            wait();
        }
        contador=contador+1;
        
        String salida = "";
        ArrayList <String> theOutput = new ArrayList();
        Date date = new Date();
        DateFormat fechayhora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        
        
        this.splitString(theInput);
        switch(estado){
            case TRYCONNECT:
           
                User_Controller user_controller = new User_Controller();
                boolean checkuser = false;
                checkuser = user_controller.comprobarUser(login, password);

                if(checkuser!=false){
                    hebra.setName(login);
                    userconnected = user_controller.obtenerUsuario(login);
                    salida = "PROTOCOLCRISTOMESSENGER1.0#"+ fechayhora.format(date) + "#SERVER#LOGIN_CORRECT#";
                    salida += getFriendsConnected(salida, user_controller);
                    theOutput.add(salida);
                    boolean connected=user_controller.estaConnectado(login);
                    if (connected==false){
                        user_controller.changeStateFromUser(login);
                    }else{
                        theOutput.add("PROTOCOLCRISTOMESSENGER1.0#" + fechayhora.format(date) + "#SERVER#ERROR#BAD_LOGIN");
                    }
                    
                }
                else{
                    theOutput.add("PROTOCOLCRISTOMESSENGER1.0#" + fechayhora.format(date) + "#SERVER#ERROR#BAD_LOGIN");
                }
                break; 
                
            case REFRESHFRIENDS:
                String aux = this.getStatusFriends(user_destino);
                theOutput.clear();
                theOutput.add("PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#SERVER#STATUS#"+aux);
                break;
                
            case GETALLMESSAGE:
               recorrer.clear();
               this.getAllMessageFromUser(theInput, user_orig, user_destino);
               if(tamanio > 0){
                    getMessageByDate();
                    recorrer.add("PROTOCOLCRISTOMESSENGER1.0#"+ fechayhora.format(date) + "#SERVER#MSGS#" + user_orig+ "#"+ user_destino + "#" + tamanio + "#" + message.size());
                    for(Message mensaje : message){
                        recorrer.add("PROTOCOLCRISTOMESSENGER1.0#"+ fechayhora.format(date) + "#SERVER#MSGS#" + mensaje.getId_user_orig() + "#" + mensaje.getId_user_dest() +"#" + mensaje.getDatetime() + "#" + mensaje.getText());
                    }
                   theOutput.clear();
                   theOutput=recorrer; 
                   for(int i = 0 ; i< theOutput.size() ; i++){
                        System.out.println(theOutput.get(i));
                        Debug.debugServer.setText(Debug.debugServer.getText() + theOutput.get(i) + "\n");
                   }
                   
               }              
               else{
                   recorrer.add("PROTOCOLCRISTOMESSENGER1.0#"+ fechayhora.format(date) + "#SERVER#MSGS#" + user_orig+ "#"+ user_destino + "#" + tamanio + "#" + message.size());  
                   theOutput.clear();
                   theOutput=recorrer; 
               }
               
               break;
               
            case OKSEND:
                
                if(recorrer.size()>1){
                    recorrer.remove(0);
                    recorrer.add(0, "OK_SEND!");
                    theOutput.clear();
                    theOutput = recorrer;
                }
                break;
            case NOTHING:
                theOutput.clear();
//                theOutput.add("");
                break;
                
            case GIVEDATESFROMUSER:
                theOutput.clear();
                theOutput.add(getAllDataUser());
                break;
            
            case INSERTMESSAGEONDB:
                theOutput.clear();
                String [] getdate = theInput.split("#");
                String dateMessage = getdate[1];
                Message_Controller message_controller = new Message_Controller();
                message_controller.insertMessageIntoDB(user_orig, user_destino, dateMessage, textmessage);
                for (MultiserverThread hebras: Multiserver.arrayhebras){
                   
                    if (hebras.getName().equals(user_destino)){
                        hebras.out.println("PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#SERVER#CHAT#"+user_orig+"#"+user_destino+"#"+textmessage+"#"+dateMessage);
                        theOutput.add("PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#SERVER#CHAT#"+user_orig+"#"+user_destino+"#MESSAGE_SUCCESFULLY_PROCESSED#"+dateMessage); 
                    }
                }
                
                break;
                
            case GETPHOTO:
                theOutput.clear();
                theOutput = recorrer;
                break;
                
                    
        }
        contador=contador-1;
        notifyAll();
    
        return theOutput;
    }
    
    public void getMessageByDate() throws SQLException, ParseException{
        
        Message_Controller message_controller = new Message_Controller();
        message.clear();
        message_controller.getMessageBydate(message, user_orig, user_destino, dateformessage);
    }
    
}
