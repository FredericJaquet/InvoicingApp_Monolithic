/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.invoicingapp.bbdd;

import com.invoicingapp.config.PathNames;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author frede
 */
public class Database {
    
    protected static final String CONNECTIONURL="jdbc:mysql://localhost:3306/";
    protected static final String ADMIN_USER="root";
    protected static final String ADMIN_PASSWORD="root";
    protected static final String DATABASE_NAME="invoicingApp";
    
    protected static final String DB_USER="Frederic";
    protected static final String DB_PASSWORD="Frederic";
    
    public static void createUser(){

        Connection connection=null;
        Statement statement=null;
        
        try {
            connection=DriverManager.getConnection(CONNECTIONURL, ADMIN_USER, ADMIN_PASSWORD);
            statement=connection.createStatement();
            
             // Create new User
            statement.executeUpdate("CREATE USER IF NOT EXISTS '" + DB_USER + "'@'localhost' IDENTIFIED BY '" + DB_PASSWORD + "';");

            // Grant privileges to the new User
            statement.executeUpdate("GRANT ALL PRIVILEGES ON " + DATABASE_NAME + ".* TO '" + DB_USER + "'@'localhost';");            

            System.out.println("Usuario creado exitosamente.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creando el usuario: " + e.getMessage());
        }
    }
    
    public static void createDataBase(){

        Connection connection=null;
        Statement statement=null;
        
        try {
            connection=DriverManager.getConnection(CONNECTIONURL, ADMIN_USER, ADMIN_PASSWORD);
            statement=connection.createStatement();
            
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
            statement.executeUpdate("USE " + DATABASE_NAME);
            
            System.out.println("Base de datos creadas exitosamente.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creando la base de datos: " + e.getMessage());
        }
    }
    
    public static void createTables() {
        ConnectionDB con = new ConnectionDB();
        ArrayList<String> blocks = new ArrayList<>();
        BufferedReader br = null;
        StringBuilder content = new StringBuilder();
        String line;

        try {
            br = new BufferedReader(new FileReader(PathNames.CREATETABLEFILEPATH));
        
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        
            // Split the SQL script by semicolons
            String[] sqlStatements = content.toString().split(";");
            for (String statement : sqlStatements) {
                // Trim and add to blocks if it's not empty
                if (!statement.trim().isEmpty()) {
                    blocks.add(statement.trim() + ";");
                }
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        con.openConnection();
        for (String block : blocks) {
            con.executeUpdate(block);
        }
        con.closeConnection();
    }
    
}
