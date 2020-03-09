

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Charlowit
 */
public class Controladora extends Thread {
    String ip;
    int puerto;
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    Date date;
    DateFormat fechayhora;
    String usuario;
    String password;
    ProtocoloCliente protocolocliente;
    String read_from_server1;
    String user_dest_for_message;
    String date_for_message;
    String friend_refresh_state;
    int contador;
    Controladora(String ip, int puerto, String usuario, String password) throws IOException{
            this.usuario = usuario;
            this.password = password;
            date = new Date();
            fechayhora= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.ip = ip;
            this.puerto = puerto;
            socket = new Socket(ip, puerto);
            protocolocliente = new ProtocoloCliente();
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            read_from_server1="";
            int contador = 0 ;
            
    }            

    @Override
    public void run(){
        try {
            while(true){
                    read_from_server1 = in.readLine();
                    System.out.println("Server : " + read_from_server1);
                    String resultado = protocolocliente.processInput(read_from_server1);
                    if(resultado.equals("CREATEVIEW")){
                        this.createView();
                    }
                    else if(resultado.equals("READ_MESSAGE")){
                        this.getMessageFromUser(read_from_server1);
                    }
                    else if(resultado.equals("STATE_PROCESSED")){
                        this.RefreshStatusFromUser(read_from_server1);
                    }
                    else if(resultado.equals("ALLDATA_USER")){
                        this.getAllDataUser(read_from_server1);
                    }
                    else if(resultado.equals("CHATRECIBED")){
                        protocolocliente.getNewMessage(read_from_server1);
                    }
                    else if(resultado.equals("GET_PHOTO")){
                        this.getPhotoFromUser();
                    } 
                    else if (resultado.equals("USER_STILL_CONNECTED")){
                        JOptionPane.showMessageDialog(null, "Error: usuario ya conectado");
                        break;
                    }
                
        }
        } catch (IOException ex) {
            Logger.getLogger(Controladora.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Controladora.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Controladora.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Controladora.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    public void createView() throws IOException, SQLException{
            
                Vista vistacliente = new Vista(this.usuario, this);
                vistacliente.setVisible(true);
    }
    
    public synchronized void getMessageFromUser(String cadena) throws IOException{
        Date date1 = new Date();
        DateFormat fechayhoraMensaje = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int [] total_messages;
        String send_to_server, read_from_server, aux;
        
        total_messages = protocolocliente.total_messages(cadena);
           if(total_messages[1] > 0 ){
               send_to_server = "PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date1)+"#CLIENT#MSGS#OK_SEND!";
               out.println(send_to_server);
               System.out.println("Client: " + send_to_server);
               Vista.mensajes.clear();
               Vista.total_mensajes.setText("");
               for(int i = 0 ; i < total_messages[1]; i++){
                   read_from_server = in.readLine();
                   System.out.println("Server: " + read_from_server);
                   protocolocliente.getMessages(read_from_server);   
               }
               for (int i = 0 ; i < Vista.mensajes.size() ;i++){
                   Vista.total_mensajes.setText(Vista.total_mensajes.getText()+ "\n" + Vista.mensajes.get(i).getId_user_orig()+ " " + Vista.mensajes.get(i).getText()+ "\n" + Vista.mensajes.get(i).getDatetime() );
               }
               send_to_server = "PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date1)+"#CLIENT#MSGS#ALL_RECEIVED";
               System.out.println("Client: " + send_to_server);
               out.println(send_to_server);
               
           }
           else if(total_messages[1] == 0 && total_messages[0]> 0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                date_for_message=sdf.format(Timestamp.valueOf(date_for_message).getTime() - (24*60*60*1000));
                this.generateStringForMessage(usuario, user_dest_for_message, date_for_message);
                 
           }
           else{
              Vista.total_mensajes.setText("No habéis mantenido todavia una conversación"); 
           }
         
    }
    
    public synchronized void generateStringForMessage(String user, String user_destino, String date){
                Date date1 = new Date();
                DateFormat fechayhoraMensaje = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");             
                String send_to_server;
                usuario = user;
                user_dest_for_message = user_destino;
                date_for_message = date;
                send_to_server = "PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date1)+"#CLIENT#MSGS#"+usuario+"#"+user_dest_for_message+"#"+date_for_message;
                System.out.println("Client: " +send_to_server);
                out.println(send_to_server);
    }
    
    public synchronized void generateStringForStatus(String user, String user_destino){
                Date date1 = new Date();
                DateFormat fechayhoraMensaje = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");             
                String send_to_server;
                send_to_server = "PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date1)+"#CLIENT#STATUS#"+user+"#"+user_destino;
                System.out.println("Client: " +send_to_server);
                out.println(send_to_server);
    }
    
    public synchronized void generateStringForLogin(String user, String password){
                Date date1 = new Date();
                DateFormat fechayhoraMensaje = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");             
                String send_to_server;
                send_to_server = "PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date1)+"#CLIENT#LOGIN#"+user+"#"+password;
                System.out.println("Client: " +send_to_server);
                out.println(send_to_server);
    }
    public synchronized void generateStringForDataUser(String user){
                Date date1 = new Date();
                DateFormat fechayhoraMensaje = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");             
                String send_to_server;
                send_to_server = "PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date1)+"#CLIENT#ALLDATA_USER#"+user;
                System.out.println("Client: " +send_to_server);
                out.println(send_to_server);
    }
    
    public synchronized void cadenaPedirFoto(String id_user){
        Date date = new Date();
        DateFormat fechayhora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String cadena= "PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#CLIENT#GET_PHOTO#"+id_user;
        this.out.println(cadena);
        System.out.println("Cliente: " + cadena);
    }
    
    public synchronized void RefreshStatusFromUser(String cadena) throws IOException, InterruptedException{
        String aux;
        int localizarComun= cadena.indexOf("SERVER");
        String aux2= cadena.substring(localizarComun);
        String nuevaCadena = aux2.replace("SERVER#",""); 
        String [] spliteo = nuevaCadena.split("#");
        
        int i = 0;
        boolean check = false;
        for(i = i ; i < Refresh_Friends.friendlist.size() && check == false ; i++){
            String [] id_user = Refresh_Friends.friendlist.get(i).getUser_amigo().split(" ");
            if(id_user[0].equals(spliteo[1])){
                Refresh_Friends.friendlist.get(i).setUser_amigo(id_user[0] + " " + spliteo[2] );
                check = true;
                
            }
        }
        if(i==Refresh_Friends.friendlist.size() -1){
           DefaultListModel model = new DefaultListModel();
           for(int j = 0 ; j < Refresh_Friends.friendlist.size() ; j++){
               model.addElement(Refresh_Friends.friendlist.get(j).getUser_amigo());
           } 
        Vista.lista_amigos.setModel(model);
        }
        
    }
    
    public synchronized void getAllDataUser(String cadena) throws IOException{
        int same= cadena.indexOf("SERVER");
        String same_replace= cadena.substring(same); 
        String newString = same_replace.replace("SERVER#",""); 
        String [] detalles = newString.split("#");
        
        User aux = new User();
        aux.setId_user(detalles[1]);
        aux.setName(detalles[2]);
        aux.setSurname1(detalles[3]);
        aux.setSurname2(detalles[4]);
        Vista.user_dest_connected.setText(aux.getName() + " " + aux.getSurname1() + " " + aux.getSurname2());
        
    }
    public synchronized void sendMessage(String login, String amigo, String mensaje){
        Date date = new Date();
        DateFormat fechayhora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String [] userAmigo= amigo.split(" ");
        String fromServer= "PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#CLIENT#CHAT#"+login+"#"+userAmigo[0]+"#"+mensaje;
        out.println(fromServer);
        System.out.println("Cliente: "+fromServer);
        if (Vista.total_mensajes.getText().equals("No habéis mantenido todavia una conversación")){
            Vista.total_mensajes.setText(login + " " + mensaje + "\n" + fechayhora.format(date));
        }
        else {
            Vista.total_mensajes.setText(Vista.total_mensajes.getText()+ "\n" + login + " " + mensaje + "\n" + fechayhora.format(date));
        }
    }
    
    
    private synchronized void getPhotoFromUser() throws IOException {
        String desdeServer;
        boolean continuar_leyendo=true;
        desdeServer= this.in.readLine();
        ArrayList<String> fotoCodificada= new ArrayList();
        ArrayList<String> fotoDescodificada= new ArrayList();
        String persona="";
        
        while(continuar_leyendo==true){
            continuar_leyendo= protocolocliente.ComprobarFinDeTransmisionFoto(desdeServer);
            if (continuar_leyendo==true){
                String [] trozosdeFoto= desdeServer.split("#");
                persona=trozosdeFoto[4];
                fotoCodificada.add(trozosdeFoto[trozosdeFoto.length-1]);
                desdeServer= this.in.readLine();
                System.out.println("Server :" + desdeServer);
            }
            
        }
        String cadena="PROTOCOLCRISTOMESSENGER1.0#"+fechayhora.format(date)+"#CLIENT#PHOTO_RECEIVED#"+persona;
        this.out.println(cadena);
        System.out.println("Cliente: " + cadena);
        
        for (String s : fotoCodificada) {
            fotoDescodificada.add(new String(Base64.getDecoder().decode(s)));
        }
        
        try{
            File file = new File("copia/"+persona+".jpg");
            file.createNewFile();
            contador=contador+1;
            try (FileOutputStream fos = new FileOutputStream(file)) {
                for (String s : fotoDescodificada) {
                    for (char c : s.toCharArray()) {
                        fos.write(c);
                    }
                }
                fos.flush();
                fos.close();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Controladora.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (persona.equals(this.usuario)){
            rsscalelabel.RSScaleLabel.setScaleLabel(Vista.photo_user, "copia/"+persona+".jpg");
        }else {
            rsscalelabel.RSScaleLabel.setScaleLabel(Vista.photo_user_dest, "copia/"+persona+".jpg");
        }   
    }
    
    public void Close_Comunication() throws IOException{  
        socket.close();
    }
    
}
