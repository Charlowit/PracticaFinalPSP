/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Charlowit
 */
class ProtocoloCliente {
    private ArrayList<Friend> amigos= new ArrayList ();
    private ArrayList<Message> mensajes = new ArrayList();
    public String login;
    Date date = new Date();
    DateFormat fechayhora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String select_user;

    public ArrayList<Friend> getFriends() {
        return amigos;
    }
    
    
    public int splitCadena(String entrada) {
        int check=0;
//        System.out.println("Entrada" + entrada);
        if(!entrada.equals(null) && !entrada.equals("")){
            if (entrada.startsWith("PROTOCOLCRISTOMESSENGER1.0#")){
            int localizarComun= entrada.indexOf("SERVER");
            if(localizarComun >= 0){
                String comun= entrada.substring(localizarComun);
                String nuevaCadena = comun.replace("SERVER#","");
                String [] detalles = nuevaCadena.split("#");
                
                if(detalles[detalles.length-2].equals("MESSAGE_SUCCESFULLY_PROCESSED")){
                    check=0;
                }else if(detalles[0].equals("LOGIN_CORRECT")){
                    check=1;
                }
                else if(detalles[0].equals("MSGS")){
                    check=2;
                }
                else if(detalles[0].equals("STATUS")){
                    check=3;
                }
                else if(detalles[0].equals("ALLDATA_USER")){
                    check=4;
                }
                else if(detalles[0].equals("CHAT")){
                    check=5;
                }
                else if(detalles[0].equals("STARTING_MULTIMEDIA_TRANSMISSION_TO")){
                    check=6;   
                }
                else if(detalles[0].equals("ERROR")){
                    check=7;
                }
            }
        }
        
        }
        return check;
    }
    
    public synchronized String RefreshStateFromFriends(String login, String user){
            Date date1 = new Date();
            DateFormat fechayhora1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "PROTOCOLCRISTOMESSENGER1.0#"+fechayhora1.format(date1)+"#CLIENT#STATUS#"+login+"#"+user;
    
    }
    public synchronized String processInput(String cadenaServer) {
        String resultado="ERROR";
        
        int check = this.splitCadena(cadenaServer);
        if (check==1){
            resultado="CREATEVIEW";
            this.getFriends(cadenaServer);
        }
        else if(check == 2 ){
            resultado = "READ_MESSAGE";
        }
        else if (check == 3 ){
            resultado = "STATE_PROCESSED";
        }
        else if (check == 4){
           // System.out.println("voy a decir que es el exito 4");
            resultado = "ALLDATA_USER";
        }
        else if (check == 5){
            resultado = "CHATRECIBED";
        }
        else if (check==6){
            resultado="GET_PHOTO";
        }
        else if (check==7){
            resultado="USER_STILL_CONNECTED";
        }
        
        
        
        return resultado;
    }
    
    public int [] total_messages(String cadena){
        int [] total =new int[2];

        int localizarComun= cadena.indexOf("SERVER");
        String comun= cadena.substring(localizarComun);
        String nuevaCadena = comun.replace("SERVER#",""); 
        String [] detalles = nuevaCadena.split("#");
        total[0]=Integer.parseInt(detalles[3]);
        total[1]=Integer.parseInt(detalles[4]);
        return total;
        
    }
    private void refreshStatusFriends(String [] detalles, int totalAmigos){
            for(int i=5; i<=totalAmigos*2+4; i=i+2){
            Friend aux = new Friend();
            aux.setUser_conectado(login);
            aux.setUser_amigo(detalles[i]+" "+detalles[i+1]);
            Login.friendList.add(aux);
        } 
    }

    private void getFriends(String cadenaServer) {
     
        int posicion = cadenaServer.indexOf("SERVER");      
        String nuevaCadena = cadenaServer.substring(posicion);
        String [] detalles = nuevaCadena.split("#");
        int totalAmigos=Integer.parseInt(detalles[4]);
        refreshStatusFriends(detalles, totalAmigos);


    }
    
    public void getMessages(String cadenaServer){
        
        int posicion = cadenaServer.indexOf("MSGS");
        String nuevaCadena = cadenaServer.substring(posicion);
//        System.out.println(nuevaCadena);
        String [] detalles = nuevaCadena.split("#");
        Message aux = new Message();
        aux.setId_user_orig(detalles[1]);
        aux.setId_user_dest(detalles[2]);
        aux.setDatetime(detalles[3]);
        aux.setText(detalles[4]);
        
        Vista.mensajes.add(aux);
        
    }
    
    public User getAllDataUser(String cadenaServer){
        User aux = new User();
        int position= cadenaServer.indexOf("SERVER");
        String repeat= cadenaServer.substring(position); 
        String nuevaCadena = repeat.replace("SERVER#",""); 
        String [] detalles = nuevaCadena.split("#");

        aux.setId_user(detalles[1]);
        aux.setName(detalles[2]);
        aux.setSurname1(detalles[3]);
        aux.setSurname2(detalles[4]);
    
        return aux;
    }
    
    public  void getNewMessage(String cadena){
        int posicion = cadena.indexOf("CHAT");
        String nuevaCadena = cadena.substring(posicion);
       // System.out.println(nuevaCadena);
        String [] detalles = nuevaCadena.split("#");
        Message aux = new Message();
        aux.setId_user_orig(detalles[1]);
        aux.setId_user_dest(detalles[2]);
        aux.setDatetime(detalles[4]);
        aux.setText(detalles[3]);
        //System.out.println("select_user " + select_user);
        if(select_user.equals(aux.getId_user_orig())){    
            Vista.mensajes.add(aux);
            if (Vista.total_mensajes.getText().equals("No habéis mantenido todavia una conversación")){
                Vista.total_mensajes.setText( aux.getId_user_orig() + " " + aux.getText() + "\n" + aux.getDatetime());
            }
            else {
                Vista.total_mensajes.setText(Vista.total_mensajes.getText() + "\n" + aux.getId_user_orig() + " " + aux.getText() + "\n" + aux.getDatetime() );
            }
            
            
        }

    }
    
    public boolean ComprobarFinDeTransmisionFoto(String entrada){
        boolean fin=true;
        
        int localizarComun= entrada.indexOf("SERVER");
        String comun= entrada.substring(localizarComun); 
        String nuevaCadena = comun.replace("SERVER#","");         
        String [] detalles = nuevaCadena.split("#");
        
        if (detalles[0].equals("ENDING_MULTIMEDIA_TRANSMISSION")){
            fin=false;
        }
        
        
        return fin;
    }
    
}
