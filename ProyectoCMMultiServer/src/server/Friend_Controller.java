package server;


import java.sql.SQLException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Charlowit
 */
public class Friend_Controller {
    protected Friend_Model friend_model;
    
    Friend_Controller() throws SQLException{
        friend_model = new Friend_Model();
    }
    
    public void obtenerAmigos(ArrayList<Friend> amigos, String user) throws SQLException{
        friend_model.getAllAmigos(amigos, user);
    }
}
