/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daviscahtech_chatdemo.Controllers;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import daviscahtech_chatdemo.Dao.DatabaseConnection;
import daviscahtech_chatdemo.Server.Server;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/** \n\n
 * FXML Controller class
 *
 * @author davis
 */
public class ClientController implements Initializable,Runnable {
    
    //###############################For DB link##########################
    Connection con = null;
    PreparedStatement pstmt;
    ResultSet rs;
    Statement st; 
    
    //variables to hold database data/info
    String dbIdHolder;
    String dbusername;
    String dbIPAddress;
    int dbPort;
    
    //connection variables
    private  DatagramSocket socket;
    private InetAddress ip;
    
    private  Thread send, listen, run;
    private Server server;
    private int id = -1;
    private boolean running = false;    

    @FXML
    private TextArea txtHistory;
    @FXML
    private TextField txtMessage; 
    @FXML
    private Text connectionStatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        con = DatabaseConnection.connectDb();        
        loadInfo();
        
        //start connection
        connectToServer();
        
        running = true;
        run = new Thread(this,"Running");
        run.start();
        
        //closeFromXtoolbar();
        
        //start the server
        //server = new  Server(dbPort);
    } 
    
    //connect to the server
    private void connectToServer(){
        String conInfo = "/c/"+ dbusername;
        if (openConnection(dbIPAddress)) {
            sendDataToSocket(conInfo.getBytes());
        } else {
        }
    
    }
    
    //open connection
    private boolean openConnection(String address){
    
        try {
            socket = new DatagramSocket();
            ip = InetAddress.getByName(address);
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            return  false;
        }
        return true;
    }
    
    //receive massega freom socket
    private String receiveMessageFromSocket(){
        byte[] data = new  byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        
        try {
            socket.receive(packet);
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String message = new String(packet.getData());        
        return message;
    }
    
    //listen for messages from the server
    public void listen(){
        listen = new Thread("listenForMessages"){
            @Override
            public  void run(){                
                
                while (running) {
                   String message = receiveMessageFromSocket();
                   if (message.startsWith("/c/")) {
                        id = Integer.parseInt(message.split("/c/|/e/")[1]);
                        connectionStatus.setText("You are now online ");
                    }else if (message.startsWith("/m/")) {
                        String text = message.split("/m/|/e/")[1];
                        consoleToTextArea(text);
                    } else if (message.startsWith("/i/")) {
                        String text =  "/i/"+id+"/e/";
                        sendDataToSocket(text.getBytes());
                        
                    }
                    
                }
            }
        };
        listen.start();
    }
    
    //method to send data to a socket
    private void sendDataToSocket(byte[] data){
    
        send = new Thread("send"){
            @Override
            public void run(){
                DatagramPacket packet = new DatagramPacket(data, data.length,ip,dbPort);
                
                try {
                    socket.send(packet);
                } catch (IOException ex) {
                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        };
        send.start();
    }
    
    //LOAD DATABASE INFO
    private void loadInfo(){
        
            try {
                String sql = "SELECT * FROM setting";
                pstmt = con.prepareStatement(sql);

                rs=pstmt.executeQuery();
                if (rs.next()) {
                    dbIdHolder = rs.getString("id"); 
                    dbusername = rs.getString("username");
                    dbIPAddress = rs.getString("ipAddress");
                    dbPort = rs.getInt("port");
                                          
                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Invalid class");
                    alert.setHeaderText(null);
                    alert.setContentText("Once the system starts, register your settings");
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
    private void openServerSettingWindow(ActionEvent event) throws IOException {
        loadWindow("/daviscahtech_chatdemo/Fxml/mainPageSetting.fxml", "Server Configuration");        
    }

    @FXML
    private void sendMessageOperation(ActionEvent event) {        
        sendMessage();       
    }
    
    @FXML
    private void txtMessageOnKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }    
    
     //Method to take and open any window
     private void loadWindow(String loc, String title) throws IOException{
        //cretate stage with specified owner and modality                
        Parent root = FXMLLoader.load(getClass().getResource(loc));
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();   
    }
     
    private void consoleToTextArea(String message){    
        txtHistory.appendText(message + "\n\r");
    } 
    
    private void sendMessage(){ 
        txtMessage.setPromptText(null);
        if (txtMessage.getText().isEmpty()) {
            txtMessage.setPromptText("You must write a message");
        } else {
            String messsage = dbusername +" : "+txtMessage.getText();            
            messsage = "/m/" + messsage;
            sendDataToSocket(messsage.getBytes());
        }
        txtMessage.clear();
    }

    @Override
    public void run() {
        listen();
    }

    @FXML
    private void closeChatOperation(ActionEvent event) {
        String disconnect = "/d/"+id+"/e/";
        sendDataToSocket(disconnect.getBytes());
        running = false;
        close();
        System.exit(0);
        
    }
    
    //method to close socket
    public void close(){
        new Thread(){
            @Override
            public void run(){
                
                synchronized(socket){
                    socket.close();
                }
            }  
        }.start();
    }
    
    //method to close from window X toolbar
    private void closeFromXtoolbar(){
        
        addWindowListener(new WindowAdapter() {
            
            public void windowClosing(WindowEvent e){
            
                 String disconnect = "/d/"+id+"/e/";
                sendDataToSocket(disconnect.getBytes());
                running = false;
                System.exit(0);           
            
            }
        });
    
    
    }



    
    
}//end of class
