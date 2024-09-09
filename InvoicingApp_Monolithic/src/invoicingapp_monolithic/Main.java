/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.Database;
import com.invoicingapp.config.PathNames;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 *
 * @author frederic
 */
public class Main extends Application{

    private static Scene scene;

    @Override
    public void init(){
        Database.createUser();
        Database.createDataBase();
        Database.createTables();
        ChangeRate.setDefaultChangeRate();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root= FXMLLoader.load(getClass().getResource(PathNames.LOGINVIEW));
        scene=new Scene(root);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(PathNames.ICON))); 
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop(){
        
    }
    
    public static void setRoot(String fxml)throws IOException{
        scene.setRoot(loadFXML(fxml));
    }
    
    public static Parent loadFXML(String fxml)throws IOException{
        Parent root= FXMLLoader.load(Main.class.getResource(fxml));
        
        return root;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {   
        launch();
    }
}
