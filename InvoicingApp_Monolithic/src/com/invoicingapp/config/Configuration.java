/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.invoicingapp.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author frede
 */
public class Configuration {
    
    private static String path="src/com/invoicingapp/config/config.txt";
    @Expose private boolean reminder=false;
    @Expose private int idUser=0;
    @Expose private String logoPath;
    
    public static Configuration getConfiguration(){
        String json="";
        ArrayList<Configuration> configList=new ArrayList();
        Configuration config=new Configuration();
        
        File configFile=new File(path);
        
        json=Configuration.readFile(configFile);
        configList = new Gson().fromJson(json, new TypeToken<ArrayList<Configuration>>() {}.getType());
        for (int i=0;i<configList.size();i++){
            config=configList.get(i);
        }
        
        return config;
    }
    
    public void save(){
        Gson gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        File configFile=new File(path);
        String json=gson.toJson(this);
        
        System.out.println(json);
        
        Configuration.writeFile(configFile, json);
    }
    
    private static String readFile(File file){
        String buffered;
        String data="";
	FileReader fr=null;
	BufferedReader br=null; 
		
	if(!file.exists()) {
            try {
                file.createNewFile();
                writeFile(file,"");			
                } catch (IOException e1) {
                    e1.printStackTrace();
            }
        }
			
        try {
            fr=new FileReader(file);
            br=new BufferedReader(fr);
            
            while((buffered = br.readLine())!=null) {
		data=data+buffered;
            }
	} catch (IOException e1) {
            e1.printStackTrace();
	}finally {
            try {
		if (fr!=null) {
                    fr.close();
		}
            } catch (IOException e) {
		e.printStackTrace();
            }
	}
		
	return data;
    }

    private static void writeFile(File file, String data){
        FileWriter fw=null;
	PrintWriter pw=null;
		
	file.delete();
		
	try {
            file.createNewFile();
	} catch (IOException e1) {
            e1.printStackTrace();
	}
		
	try {
            fw = new FileWriter(file,true);
            pw=new PrintWriter(fw);
            pw.print("["+data+"]");		
	} catch (IOException e1) {
            e1.printStackTrace();
	}finally {
            try {
		if (null != fw) {
                    fw.close();
                }
	    } catch (Exception e2) {
	       	e2.printStackTrace();
	    }
	}
    }
    
    /**
     * @return the reminder
     */
    public boolean isReminder() {
        return reminder;
    }

    /**
     * @param reminder the reminder to set
     */
    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    /**
     * @return the user
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     * @param idUser the user to set
     */
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    
    public void setLogoPath(String logoPath){
        this.logoPath=logoPath;
    }
    
    public String getLogoPath(){
        return logoPath;
    }
    
}
