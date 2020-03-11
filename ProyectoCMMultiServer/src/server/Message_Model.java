package server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Charlowit
 */
public class Message_Model extends Generic_Model {

    private final String tableName;
    private ArrayList<Message> mensajes;

    Message_Model() throws SQLException{
        super();
        tableName="message";
        mensajes = new ArrayList<>();
    }
    
    public String getTableName() {
        return tableName;
    }
    public void getMessages(ArrayList<Message> mensajes, String user_orig, String user_dest)
    throws SQLException {
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        String query = "Select * from " + this.getDbName() + "." + this.getTableName();
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id_user_orig = rs.getString("id_user_orig");
                String id_user_dest = rs.getString("id_user_dest");
                if( (id_user_orig.equals(user_orig) && id_user_dest.equals(user_dest)) || (id_user_orig.equals(user_dest) && id_user_dest.equals(user_orig))  ) {
                    String date = rs.getString("datetime");
                    int read = rs.getInt("read_msg");
                    int sent = rs.getInt("sent");
                    String text = rs.getString("text");
                    Message auxiliar = new Message();
                    auxiliar.setId_user_orig(id_user_orig);
                    auxiliar.setId_user_dest(id_user_dest);
                    auxiliar.setDatetime(date);
                    auxiliar.setText(text);
                    if(read == 1){
                        auxiliar.setRead(true);
                    }
                    else if(read == 0){
                        auxiliar.setRead(false);
                    }
                    else if(sent == 1){
                        auxiliar.setSent(true);
                    }
                    else if(sent == 0){
                        auxiliar.setSent(false);
                    }
                    System.out.println(auxiliar.getText());
                mensajes.add(auxiliar);
                }
            }
            stmt.close();
            con.close();
        } catch (SQLException e ) {
            //vista.debug.setText(vista.debug.getText()+ e.toString()+"\n");
            System.err.println(e);   
        } finally {
            //vista.debug.setText(vista.debug.getText()+ "Mensajes obtenidos correctamente \n");
            if (stmt != null) { stmt.close(); con.close();}
            
        }
    }
    
    public void getMessageByDate(ArrayList<Message> mensajes, String user_orig, String user_dest, String date) throws SQLException, ParseException{
        Statement stmt = null;
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        String [] init_date = date.split(" ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String limit=sdf.format(Timestamp.valueOf(date).getTime() + (246060*1000));
        String [] limit_date = limit.split(" ");
        
        
        String query = "Select * from " + this.getDbName() + "." + this.getTableName()+" WHERE (((id_user_orig='" + user_orig+"' AND id_user_dest='"+ user_dest +"') OR (id_user_orig='" + user_dest+"' AND id_user_dest='"+ user_orig +"')) AND dateTime between '"+init_date[0] +"' and '"+limit_date[0] +"')";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id_user_orig = rs.getString("id_user_orig");
                String id_user_dest = rs.getString("id_user_dest");
                String datetime = rs.getString("datetime");
                //if( (id_user_orig.equals(user_orig) && id_user_dest.equals(user_dest)) || (id_user_orig.equals(user_dest) && id_user_dest.equals(user_orig)) && limit_date[0].equals(fecha[0])) {
                    int read = rs.getInt("read_msg");
                    int sent = rs.getInt("sent");
                    String text = rs.getString("text");
                    Message auxiliar = new Message();
                    auxiliar.setId_user_orig(id_user_orig);
                    auxiliar.setId_user_dest(id_user_dest);
                    auxiliar.setDatetime(datetime);
                    auxiliar.setText(text);
//                    if(read == 1){
                        auxiliar.setRead(true);
//                    }
//                    else if(read == 0){
//                        auxiliar.setRead(false);
//                    }
//                    else if(sent == 1){
                        auxiliar.setSent(true);
//                    }
//                    else if(sent == 0){
//                        auxiliar.setSent(false);
//                    }
                    System.out.println("Texto del mensaje"+ auxiliar.getText()+ " la fecha del mensaje "+ auxiliar.getDatetime());
                    this.changeReadMessage(id_user_orig, id_user_dest, datetime);
                mensajes.add(auxiliar);
                //}
            }
            stmt.close();
            con.close();
        } catch (SQLException e ) {
            System.err.println(e);   
        } finally {
            
            if (stmt != null) { stmt.close(); con.close();}
            
        }
    }
    
    public void insertMessageIntoDB(String id_orig, String id_dest, String dateTime, String textmessage) throws SQLException{
        String query="INSERT INTO "+ this.getDbName() + "." + this.getTableName()+ " VALUES ('"+id_orig+"', '"+id_dest+"', '"+dateTime+"', 0, 1, '"+textmessage+"')";
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            int rs = stmt.executeUpdate(query);
            stmt.close(); 
            con.close();
        } catch (SQLException e ) {
            System.out.println(e);
        } finally {
            if (stmt != null) {stmt.close(); con.close();}
           
        }
    }
    public void changeReadMessage(String id_orig, String id_dest, String dateTime) throws SQLException, ParseException{
        String query= "UPDATE "+ this.getDbName() + "." + this.getTableName()+ " SET read_msg = ? WHERE id_user_orig = ? AND id_user_dest = ? AND datetime=?";
        Connection con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
//      System.out.println(" Voy a actualizar "+ id_orig + " ----- " + id_dest + " ------ " + dateTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedDate = dateFormat.parse(dateTime);
        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

//        Connection con=super.Conector();
        Statement stmt = null;
        try {
            stmt = con.createStatement();
           PreparedStatement preparedStmt = con.prepareStatement(query); 
           preparedStmt.setInt(1, 1);
           preparedStmt.setString(2, id_orig);
           preparedStmt.setString(3, id_dest);
           preparedStmt.setTimestamp(4, timestamp);
            System.out.println("-------------------------query: "+ preparedStmt.toString());
           
           preparedStmt.executeUpdate();
           stmt.close(); 
           con.close();
        } catch (SQLException e ) {
            System.out.println(e);
        } finally {
            if (stmt != null) {
                //System.out.println("Desconnected to database"); 
                stmt.close(); 
                con.close();
            }
        }

    }
}
