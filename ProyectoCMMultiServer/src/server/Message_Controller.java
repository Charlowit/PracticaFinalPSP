package server;


import java.sql.SQLException;
import java.text.ParseException;
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
public class Message_Controller {
    protected Message_Model message_model;
    
    Message_Controller() throws SQLException{
        message_model = new Message_Model();
    }

    public void getMessagesFromUser(ArrayList<Message> mensajes, String user_orig, String user_dest) throws SQLException{
        message_model.getMessages(mensajes,user_orig, user_dest);
    }
    
    public void getMessageBydate(ArrayList<Message> mensajes, String user_orig, String user_dest, String date ) throws SQLException, ParseException{
        message_model.getMessageByDate(mensajes, user_orig, user_dest, date);
    }
    
    public void insertMessageIntoDB(String id_orig, String id_dest, String dateTime, String textmessage) throws SQLException{
        message_model.insertMessageIntoDB(id_orig, id_dest, dateTime, textmessage);
    }
    public void changeReadMessage(String id_orig, String id_dest, String dateTime) throws SQLException, ParseException{
        message_model.changeReadMessage(id_orig, id_dest, dateTime);
    }
}
