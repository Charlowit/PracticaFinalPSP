package server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Charlowit
 */
public class Friend {
    
     protected String user_conectado;
     protected String user_amigo;
     protected boolean accept_request;
     
     Friend(){
         this.user_conectado="";
         this.user_amigo="";
         this.accept_request=false;
     }
     
     Friend(String user_conectado, String user_amigo, boolean accept_request){
         this.user_conectado = user_conectado;
         this.user_amigo = user_amigo;
         this.accept_request= accept_request;
     }

    public String getUser_conectado() {
        return user_conectado;
    }

    public String getUser_amigo() {
        return user_amigo;
    }

    public void setUser_conectado(String user_conectado) {
        this.user_conectado = user_conectado;
    }

    public void setUser_amigo(String user_amigo) {
        this.user_amigo = user_amigo;
    }

    public boolean Accept_request() {
        return accept_request;
    }
    
}
