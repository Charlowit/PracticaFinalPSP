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
public class User {
    protected String id_user;
    protected String name;
    protected String password;
    protected String surname1;
    protected String surname2;
    protected String photo;
    protected boolean state;
    
    User(){
        this.id_user = "";
        this.name = "";
        this.password = "";
        this.surname1 = "";
        this.surname2 = "";
        this.photo = "";
        this.state = false;
    }

    User(String id_user, String name, String password, String surname1, String surname2, String photo, boolean state) {
        this.id_user = id_user;
        this.name = name;
        this.password = password;
        this.surname1 = surname1;
        this.surname2 = surname2;
        this.photo = photo;
        this.state = state;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getId_user() {
        return id_user;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSurname1() {
        return surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public String getPhoto() {
        return photo;
    }

    public boolean isState() {
        return state;
    }
    
    
}
