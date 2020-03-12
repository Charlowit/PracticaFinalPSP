package server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class User_Model extends Generic_Model {
  
    private final String tableName;

    User_Model() throws SQLException{
        super();
        tableName="user";
    }
    
    public String getTableName() {
        return tableName;
    }
    public void getAllUsers(ArrayList<User>usuarios)
    throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        String query = "Select * from " + this.getDbName() + "." + this.getTableName();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id_user = rs.getString("id_user");
                String name = rs.getString("name");
                String pass = rs.getString("password");
                String surname1 = rs.getString("surname1");
                String surname2 = rs.getString("surname2");
                String photo = rs.getString("photo");
                int state = rs.getInt("state");
                User auxiliar = new User();
                auxiliar.setId_user(id_user);
                auxiliar.setName(name);
                auxiliar.setPassword(pass);
                auxiliar.setSurname1(surname1);
                auxiliar.setSurname2(surname2);
                auxiliar.setPhoto(photo);
                if(state == 1){
                    auxiliar.setState(true);
                }
                else{
                    auxiliar.setState(false);
                }
                
                usuarios.add(auxiliar);

            }
            stmt.close(); 
            con.close();
        } catch (SQLException e ) {
            //vista.debug.setText(vista.debug.getText()+ e.toString()+"\n");
            System.err.println(e);   
        } finally {
            
            if (stmt != null) {
                //vista.debug.setText(vista.debug.getText()+ "Todos los usuarios obtenidos correctamente\n");
                stmt.close(); 
                con.close();
            }
            
        }
       
    }
    public boolean userExits(String user, String pass)
    throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        String query = "Select * from " + this.getDbName() + "." + this.getTableName();
        boolean exito = false;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id_user = rs.getString("id_user");
                String contra = rs.getString("password");
                if(id_user.equals(user) && contra.equals(pass)){
                    exito = true;  
                }                               
            }
            stmt.close(); 
            con.close();
        } catch (SQLException e ) {
            //vista.debug.setText(vista.debug.getText()+ e.toString()+"\n");
            System.err.println(e);   
        } finally {
            if (stmt != null) { stmt.close(); con.close(); } 
        }
        return exito;
    }
    public User getUser(String user) throws SQLException{
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        String query = "Select * from " + this.getDbName() + "." + this.getTableName();
        User auxiliar = new User();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String id_user = rs.getString("id_user");
                if(id_user.equals(user)){
                    String name = rs.getString("name");
                    String pass = rs.getString("password");
                    String surname1 = rs.getString("surname1");
                    String surname2 = rs.getString("surname2");
                    String photo = rs.getString("photo");
                    int state = rs.getInt("state");
                    auxiliar.setId_user(id_user);
                    auxiliar.setName(name);
                    auxiliar.setPassword(pass);
                    auxiliar.setSurname1(surname1);
                    auxiliar.setSurname2(surname2);
                    auxiliar.setPhoto(photo);
                    if(state == 1){
                        auxiliar.setState(true);
                    }
                    else{
                        auxiliar.setState(false);
                    }
                }

            }
            stmt.close(); 
            con.close();
        } catch (SQLException e ) {
            
           // vista.debug.setText(vista.debug.getText()+ e.toString()+"\n");
            System.err.println(e);   
        } finally {
           // vista.debug.setText(vista.debug.getText()+ "Usuario obetnido correctamente \n");
            if (stmt != null) { stmt.close(); con.close();}
           
        }
        return auxiliar;
    }
    
    public void insertUserOnDatabase(User usuario) throws SQLException{
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        String query="INSERT INTO "+this.getDbName()+"."+this.tableName +" VALUES ('"+usuario.getId_user() +"', '"+usuario.getName()+"', '"+usuario.getPassword()+"', '"+usuario.getSurname1() + "', '"+usuario.getSurname2()+ "', '"+" "+ "', "+ 1 +")";
        try {
            stmt = con.createStatement();
            int rs = stmt.executeUpdate(query);
            stmt.close(); 
            con.close();
        }
        catch (SQLException e){
            //vista.debug.setText(vista.debug.getText()+ e.toString()+"\n");
            System.err.println(e); 
        }
        finally {
            //vista.debug.setText(vista.debug.getText()+ "Usuario insertardo en la base de datos correctamente \n");
            if (stmt != null) { stmt.close(); con.close();}
            
        }
        
    }
    
    public boolean userConnected(String id) throws SQLException{
        boolean exito = false;
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        Statement stmt = null;
        String query = "Select * from " + this.getDbName() + "." + this.getTableName()+ " WHERE id_user ='"+id+"'";
        System.out.println("-----------LA QUERY ES " + query);
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                
                    int state = rs.getInt("state");
                    System.out.println("------ESTADO DE LA CONEXION DE:  " + id + " "+ state);
                    //Debug.debugServer.setText(Debug.debugServer.getText()+ " " + state + "\n");
                    if(state == 1){
                        exito = true;
                    }
                }
            stmt.close();
            con.close();
            
        } catch (SQLException e ) {
            
           // vista.debug.setText(vista.debug.getText()+ e.toString()+"\n");
            System.err.println(e);   
        } finally {
           // vista.debug.setText(vista.debug.getText()+ "Usuario obetnido correctamente \n");
            if (stmt != null) { stmt.close(); con.close();}
        }
       
       return exito;
    }
    
    public void changeStateFromUser(String id) throws SQLException{
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        try{
            stmt=con.createStatement();
            String query = "update " + this.getDbName() + "." + this.getTableName() + " set state = ? where id_user = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1,1);
            preparedStmt.setString(2,id);
            preparedStmt.executeUpdate();
            System.out.println("");
//            Debug.debugServer.setText(Debug.debugServer.getText()+ " He cambiado el estado \n");
            stmt.close(); 
            con.close();
        } catch (SQLException e ) {
            
           // vista.debug.setText(vista.debug.getText()+ e.toString()+"\n");
            System.err.println(e);   
        } finally {
           // vista.debug.setText(vista.debug.getText()+ "Usuario obetnido correctamente \n");
            if (stmt != null) { stmt.close(); con.close();}
            
        }
        
    }
        public void changeCloseSession(String id) throws SQLException{
            Statement stmt = null;
            Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
            try{
                stmt=con.createStatement();
                String query = "update " + this.getDbName() + "." + this.getTableName() + " set state = ? where id_user = ?";
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.setInt(1,0);
                preparedStmt.setString(2,id);
                preparedStmt.executeUpdate();
                System.out.println("He cambiado el estado de " + id);
                //Debug.debugServer.setText(Debug.debugServer.getText()+ " He cambiado el estado \n");
                stmt.close(); 
                con.close();
                System.out.println("He cerrado sesion");
            } catch (SQLException e ) {

               // vista.debug.setText(vista.debug.getText()+ e.toString()+"\n");
                System.err.println(e);   
            } finally {
               // vista.debug.setText(vista.debug.getText()+ "Usuario obetnido correctamente \n");
                if (stmt != null) { stmt.close(); con.close();}
               
            }
        
    }

    public ArrayList<String> getAllUsersConnected() throws SQLException {
        ArrayList<String> arrayamigos = new ArrayList();
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        Statement stmt = null;
        String query = "Select id_user from " + this.getDbName() + "." + this.getTableName() + " WHERE state='1'";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String id_user = rs.getString("id_user");
                arrayamigos.add(id_user);
            }
            stmt.close();
            con.close();
        } catch (SQLException e ) {
            System.err.println(e);   
        } finally {
            if (stmt != null) { stmt.close(); con.close();}
            
        }
        
        return arrayamigos;
    }
    public String getPhoto(String id_user) throws SQLException{
        Statement stmt = null;
        String photo = "";
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        String query = "Select photo from " + this.getDbName() + "." + this.getTableName() + " WHERE id_user='"+id_user+"'";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                photo = rs.getString("photo");
            }
            stmt.close(); 
            con.close();
        } catch (SQLException e ) {
            System.err.println(e);   
        } finally {
            if (stmt != null) { stmt.close(); con.close();}
            
        }
        return photo;
   }
}
