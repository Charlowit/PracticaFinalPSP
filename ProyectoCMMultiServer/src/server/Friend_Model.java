package server;


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
public class Friend_Model extends Generic_Model {

    private final String tableName;

    Friend_Model() throws SQLException{
        super();
        tableName="friend";
    }
    
    public String getTableName() {
        return tableName;
    }
    public void getAllAmigos(ArrayList<Friend>amigos, String user)
    throws SQLException {
        Statement stmt = null;
        String query = "Select * from " + this.getDbName() + "." + this.getTableName();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String user_conectado = rs.getString("id_user_orig");
                String user_amigo = rs.getString("id_user_dest");
                if(user.equals(user_conectado)){
                    Friend auxiliar = new Friend();
                    auxiliar.setUser_conectado(user_conectado);
                    auxiliar.setUser_amigo(user_amigo);
                    amigos.add(auxiliar);
                }
            }
        } catch (SQLException e ) {
            //vista.debug.setText(vista.debug.getText()+ e.toString()+"\n");
            System.err.println(e);   
        } finally {
            //vista.debug.setText(vista.debug.getText()+" Lista de amigos obtenida correctamente \n");
            if (stmt != null) { stmt.close(); }
        }
    }


}
