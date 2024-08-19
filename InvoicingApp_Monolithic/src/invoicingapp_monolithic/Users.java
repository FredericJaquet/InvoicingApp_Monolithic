/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.ConnectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class Users extends Company {

    private String userName;
    private Password passwd=new Password();
    private int idUsers;
    
    public Users(){}
    
    public Users(String street, String stNumber, String apt, String cp, String city, String state, String country, String vatNumber, String comName, String legalName, String firstName, String middleName, String lastname, String email, String web, String userName, String password){
        super(street, stNumber, apt, cp, city, state, country, vatNumber, comName, legalName, email, web);
        this.userName=userName;
        this.passwd=new Password(password);
    }
    
    /**
    * Adds this users instance to the Database.
    * Sets the idUsers from the new register in the DB
    */
    @Override
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert;
        String queryGetId="SELECT MAX(idUsers) FROM Users";
        ResultSet result=null;
        
        super.addToDB();
        
        queryInsert="INSERT INTO Users (userName, passwd, idCompany) values('"+userName+"','"+passwd.getHashed()+"',"+getIdCompany()+")";
        
        con.openConnection();
        con.executeUpdate(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idUsers=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Gets the users from the Database and create an instance of that user.
    * @param id The idUsers of the user to get from DB
    */
    @Override
    public void getFromDB (int id){
        
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Users WHERE idUsers="+id;
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                idUsers=result.getInt(1);
                userName=result.getString(2);
                passwd.setHashed(result.getString(3));
                super.getFromDB(result.getInt(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
     /**
    * Cleans the table Users in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Users";
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this user instance from the DB. 
    */
    @Override
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Users WHERE idUsers="+idUsers;
                
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Updates this user instance in the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    @Override
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Users SET "+field+"= '"+newValue+"' WHERE idUsers="+idUsers;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idUsers);
    }
    
    @Override
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Users SET "+field+"= "+newValue+" WHERE idUsers="+idUsers;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idUsers);
    }
    
    public void updatePasswdInDB(String newPasswd){
        ConnectionDB con=new ConnectionDB();
        String hashed=passwd.getHashed();
        String query=null;
        
        try {
            hashed=Password.hashPassword(newPasswd);
        } catch (Exception ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(hashed.equals(passwd.getHashed())){
            System.out.println("No se ha podido cambiar la contraseña");
        }
        
        query="UPDATE Users SET passwd= '"+hashed+"' WHERE idUsers="+idUsers;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idUsers);
    }

    public static Map<String,Integer> getMap(){
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        String query="SELECT userName, idUsers FROM Users;";
        Map<String,Integer> mapUsers=new HashMap();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try{
            while(result.next()){
                mapUsers.put(result.getString(1),result.getInt(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return mapUsers;
    }
    
    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the passwd
     */
    public Password getPasswd() {
        return passwd;
    }

    /**
     * @param passwd the passwd to set
     */
    public void setPasswd(String passwd) {
        this.passwd = new Password(passwd);
    }

    /**
     * @return the idUsers
     */
    public int getIdUsers() {
        return idUsers;
    }

    /**
     * @param id the id to set
     */
    public void setIdUsers(int id) {
        this.idUsers = id;
    }

}
