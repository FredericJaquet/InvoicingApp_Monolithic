/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.invoicingapp.bbdd;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;

/**
 *
 * @author frede
 */
public class ConnectionDB {
    
    private Connection con=null; 
    
    public void openConnection(){
        try{
            con=DriverManager.getConnection(Database.CONNECTIONURL+Database.DATABASE_NAME, Database.DB_USER, Database.DB_PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closeConnection() {
        
        try {
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }   
    }
    
    public ResultSet getResultSet(String query){
        
        Statement stmnt=null;
        ResultSet results=null;
            
        try{
            stmnt=con.createStatement();
            results=stmnt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return results;
    }

    /**
     * @return the con
     */
    public Connection getConnection() {
        return con;
    }

    /**
     * @param con the con to set
     */
    public void setConnection(Connection con) {
        this.con = con;
    }
    
    public void executeUpdate(String sql) {
        Statement statement = null;
        try {
            statement=con.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}