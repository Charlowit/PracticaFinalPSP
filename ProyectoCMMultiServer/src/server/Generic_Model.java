package server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Charlowit
 */
public class Generic_Model {
    protected final String user;
    protected final String password;
    protected final String dbms;
    protected final String ip;
    protected final String port;
    protected final String dbName;
    protected final String url;
    protected Connection con;
    
    Generic_Model() throws SQLException{
        user="clasedam2";
        password="root";
        dbms="mysql";
        ip="clasedam2.ddns.net";
        port="3306";
        dbName="cristomessenger";
        url= "jdbc:"+ this.getDbms()+"://"+this.getIp()+":"+ this.getPort()+ "/" + this.getDbName();
        con = DriverManager.getConnection(this.getUrl(),this.getUserName(),this.getPassword());
    }   

    public String getUserName() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDbms() {
        return dbms;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }

    public String getUrl() {
        return url;
    }
}
