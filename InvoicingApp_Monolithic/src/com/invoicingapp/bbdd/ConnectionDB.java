/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.invoicingapp.bbdd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
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
    
    public void noReturnQuery(String query){
        PreparedStatement stmnt=null;
                
        try{
            stmnt=con.prepareStatement(query);
            stmnt.executeUpdate();
        
        }catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*public void runScript(String scriptPath){
        ScriptRunner scriptRunner = new ScriptRunner(con);
        Reader reader=null; 
       
        // Optionnal: configure ScriptRunner
        scriptRunner.setLogWriter(new PrintWriter(System.out)); // Para no mostrar logs por consola
        scriptRunner.setErrorLogWriter(new PrintWriter(System.err)); // Para no mostrar errores por consola
        try{
            // Read the file of the script
            reader = new BufferedReader(new FileReader(scriptPath));
        }catch(FileNotFoundException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Execute the script
        scriptRunner.runScript(reader);    
        System.out.println("Script executed successfully.");
    }*/
    
    /*public ObservableList getData(String query, String attribute){
        
        ResultSet results=getResultSet(query);         
        ObservableList<String> resultsList=FXCollections.observableArrayList();
   
        try {
            while(results.next()){
                resultsList.add(results.getString(attribute));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        return resultsList;
        
    }*/

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
            statement = con.createStatement();
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