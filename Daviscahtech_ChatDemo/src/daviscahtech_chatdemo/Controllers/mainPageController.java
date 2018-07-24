/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_chatdemo.Controllers;

import daviscahtech_chatdemo.Dao.DatabaseConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author davis
 */
public class mainPageController implements Initializable {
    
    //###############################For DB link##########################
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st;    
    

    @FXML
    private TextField serverName;
    @FXML
    private TextField ipAddress;
    @FXML
    private TextField port;
    @FXML
    private PasswordField userPassword;
    
    //id holder from db
    private String dbIdHolder;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();
        loadInfo();
    } 
    
    //LOAD DATABASE INFO
    private void loadInfo(){
        
            try {
                String sql = "SELECT * FROM setting";
                pstmt = con.prepareStatement(sql);

                rs=pstmt.executeQuery();
                if (rs.next()) {
                    dbIdHolder = rs.getString("id"); 
                    serverName.setText(rs.getString("username"));
                    userPassword.setText(rs.getString("userpass"));
                    ipAddress.setText(rs.getString("ipAddress"));
                    port.setText(String.valueOf(rs.getInt("port")));
                                          
                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Invalid class");
                    alert.setHeaderText(null);
                    alert.setContentText("Register you details or configuration correctly.");
                    alert.showAndWait(); 
                }
            } catch (SQLException e) {
            } finally {
                    try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {
                    }        
               }          
    
    }    
    
    
    

    @FXML
    private void logInOperation(ActionEvent event) {
        
         if (serverName.getText().isEmpty() || ipAddress.getText().isEmpty() 
                 || port.getText().isEmpty() || userPassword.getText().isEmpty() ) {             
                         
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("Registration Failed");
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Some Information was not enterered.\n");
            errorIssue.showAndWait(); 
            
        }else {              
             //show confirm alert before registering data of issued
            String ms = " Are you sure you want to register info";           
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm registration");
            alert.setHeaderText(null);
            alert.setContentText(ms);
           
            Optional<ButtonType> response = alert.showAndWait();            
            if (response.get()==ButtonType.OK) {
                
               try {
                    String sql = "INSERT INTO setting (username,userpass,ipAddress,port) VALUES(?,?,?,?)";
                    pstmt= con.prepareStatement(sql);

                    pstmt.setString(1, serverName.getText());
                    pstmt.setString(2,DigestUtils.sha1Hex(userPassword.getText()));
                    pstmt.setString(3, ipAddress.getText());
                    pstmt.setInt(4, Integer.parseInt(port.getText()));

                    int i = pstmt.executeUpdate(); // load data into the database             
                    if (i>0) {
                        //show alert before registering data of employee
                    String msg = " Infomation has been registered.";
                    Alert al = new Alert(Alert.AlertType.INFORMATION);
                    al.setTitle("Registration Information Dialog");
                    al.setHeaderText(null);
                    al.setContentText(msg);
                    al.showAndWait();
                    
                    clearFields();
                    
                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Failed");
                        error.setContentText("OOPS! Could not register ");
                        error.showAndWait();
                    } 
                    
                    pstmt.close();
                    
                    
                } catch (Exception e) {
                    Alert at = new Alert(Alert.AlertType.ERROR);
                    at.setTitle("Error in registration of class");
                    at.setHeaderText(null);
                    at.setContentText("OOPS! Could not register class due  to internal error. " +e);
                    at.showAndWait();                         
                }finally {
                   try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {}               
               }
               
            }   
        }        
        

    }

    private void clearFields() {
        serverName.clear();
        port.clear();
        ipAddress.clear();
        userPassword.clear();

    }

    @FXML
    private void updateSettingOperation(ActionEvent event) {
        
         if (serverName.getText().isEmpty() || ipAddress.getText().isEmpty() || port.getText().isEmpty() ) {             
                         
            Alert errorIssue = new Alert(Alert.AlertType.ERROR);
            errorIssue.setTitle("UPDATE Failed");
            errorIssue.setHeaderText(null);
            errorIssue.setContentText("Some Information was not UPDATE.\n");
            errorIssue.showAndWait(); 
            
        }else {              
             //show confirm alert before registering data of issued
            String ms = " Are you sure you want to UPDATE info";           
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm UPDATE");
            alert.setHeaderText(null);
            alert.setContentText(ms);
           
            Optional<ButtonType> response = alert.showAndWait();            
            if (response.get()==ButtonType.OK) {
                
               try {
                    String sql = "UPDATE setting SET username = ?,userpass = ?,ipAddress = ?,port = ? WHERE id = '"+dbIdHolder+"' ";
                    pstmt= con.prepareStatement(sql);

                    pstmt.setString(1, serverName.getText());
                    pstmt.setString(2,DigestUtils.sha1Hex(userPassword.getText()));
                    pstmt.setString(3, ipAddress.getText());
                    pstmt.setInt(4, Integer.parseInt(port.getText()));

                    int i = pstmt.executeUpdate(); // load data into the database             
                    if (i>0) {
                        //show alert before registering data of employee
                    String msg = " Infomation has been UPDATE.";
                    Alert al = new Alert(Alert.AlertType.INFORMATION);
                    al.setTitle("UPDATE Information Dialog");
                    al.setHeaderText(null);
                    al.setContentText(msg);
                    al.showAndWait();
                    
                    clearFields();
                    
                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Failed");
                        error.setContentText("OOPS! Could not UPDATE ");
                        error.showAndWait();
                    } 
                    
                    pstmt.close();
                    
                    
                } catch (Exception e) {
                    Alert at = new Alert(Alert.AlertType.ERROR);
                    at.setTitle("Error in UPDATE of class");
                    at.setHeaderText(null);
                    at.setContentText("OOPS! Could not UPDATE class due  to internal error. " +e);
                    at.showAndWait();                         
                }finally {
                   try {
                        rs.close();
                        pstmt.close();
                    } catch (Exception e) {}               
               }
               
            }   
        }          
        
    }

    
}// end of class
