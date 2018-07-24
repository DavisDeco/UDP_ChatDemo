/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_chatdemo.Preferences;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author davis
 */
public class LocalServerPreference {
    
    public static final String DB_CONFIG_FILE = "./Config/P.txt";
    
    private String e;
    private String d;

    public LocalServerPreference() {  
        
        setE("root");
        setD("root");
               
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
    
    
    
  
    
         // method to store preferences into json file
    public static  void  initConfig(){
    
        Writer writer = null;
        try {  
            LocalServerPreference preferences = new LocalServerPreference();
            Gson gson = new Gson();
            writer = new FileWriter(DB_CONFIG_FILE);
            gson.toJson(preferences,writer);
        } catch (IOException ex) {
            Logger.getLogger(LocalServerPreference.class.getName()).log(Level.SEVERE, null, ex);
        }finally{        
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(LocalServerPreference.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //method to get preferences from the json file
    public static LocalServerPreference getPreferences(){
        
        Gson gson = new Gson();
        LocalServerPreference preferences = new LocalServerPreference();
        
        try {
            preferences = gson.fromJson(new FileReader(DB_CONFIG_FILE), LocalServerPreference.class);
        } catch (FileNotFoundException ex) {
            initConfig();
            Logger.getLogger(LocalServerPreference.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return preferences;
    }
    
    //method to update preferences to the system json file
    
    public static void writePreferences(LocalServerPreference preferences){
        
         Writer writer = null;
        try { 
            Gson gson = new Gson();
            writer = new FileWriter(DB_CONFIG_FILE);
            gson.toJson(preferences,writer);
            
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);  
            successAlert.setTitle("SUCCESS");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Setting changes were successful");
            successAlert.showAndWait();
            
        } catch (IOException ex) {
            Alert bookError = new Alert(Alert.AlertType.ERROR);  
            bookError.setTitle("FAILED");
            bookError.setHeaderText(null);
            bookError.setContentText("Changes update failed!\nDefault values will still be used");
            bookError.showAndWait();
        }finally{        
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(LocalServerPreference.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
        
    } 
    
    
    
    
    
    
    
    
    
    
    
    
}// END OF CLASS
