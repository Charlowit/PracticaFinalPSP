/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Charlowit
 */
public class Message {
    private String id_user_orig;
    private String id_user_dest;
    private String datetime;
    private boolean read;
    private boolean sent;
    private String text;

    public Message() {
        this.id_user_orig = "";
        this.id_user_dest = "";
        this.datetime = "";
        this.read = false;
        this.sent = false;
        this.text = "";
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setId_user_orig(String id_user_orig) {
        this.id_user_orig = id_user_orig;
    }

    public void setId_user_dest(String id_user_dest) {
        this.id_user_dest = id_user_dest;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getId_user_orig() {
        return id_user_orig;
    }

    public String getId_user_dest() {
        return id_user_dest;
    }

    public boolean isRead() {
        return read;
    }

    public boolean isSent() {
        return sent;
    }


}
