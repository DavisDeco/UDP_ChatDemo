/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_chatdemo.Dao;

import daviscahtech_chatdemo.Preferences.LocalServerPreference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;
/**
 *
 * @author davis
 */
public  class DatabaseConnection {
    
    public static Connection connectDb(){
        LocalServerPreference preference = LocalServerPreference.getPreferences();
        Connection con = null;
        try {
           String url= "jdbc:mysql://localhost:3306/com.daviscahtech.chatdemo";
           String username = preference.getE();
           String password =preference.getD();

            Class.forName("com.mysql.jdbc.Driver");// load the mysql driver
            con = DriverManager.getConnection(url,username,password);
            return con;
            
        } catch (ClassNotFoundException | SQLException e) {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database-system Connetion Error!");
            alert.setHeaderText(null);
            alert.setContentText("Connection to the database Failed. \n Activate Database  \n");
            alert.showAndWait();
            return null;
        }
        
    }
    
    
}
