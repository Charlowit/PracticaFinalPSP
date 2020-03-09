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
public class User_Controller {
    protected User_Model user_model;
    
    User_Controller() throws SQLException{
        user_model = new User_Model();
    }
    
    public void obtenerListaUsuarios(ArrayList<User> usuarios) throws SQLException{
        user_model.getAllUsers(usuarios);
    }
    
    public ArrayList<String> obtenerListaUsuariosConectados() throws SQLException{
        ArrayList<String> arrayamigos = new ArrayList();
        arrayamigos = user_model.getAllUsersConnected();
        return arrayamigos;
    }
    public boolean comprobarUser(String id_user, String pass) throws SQLException{
        return user_model.userExits(id_user, pass);
    }
    public User obtenerUsuario(String user) throws SQLException{
        User usuario_obtenido = user_model.getUser(user);
        return usuario_obtenido;
    }
    public void registrarUsuario(User usuario) throws SQLException{
        user_model.insertUserOnDatabase(usuario);
    }
    public boolean estaConnectado(String id) throws SQLException{
        boolean exito = false;
        exito = user_model.userConnected(id);   
        return exito;     
    }
    
    public void changeStateFromUser(String id) throws SQLException{
        user_model.changeStateFromUser(id);
    }
    public void changeCloseSession(String id) throws SQLException{
        user_model.changeCloseSession(id);
    }
    public String getPhoto(String id_user) throws SQLException{   
        return user_model.getPhoto(id_user);    
    }
    
}
