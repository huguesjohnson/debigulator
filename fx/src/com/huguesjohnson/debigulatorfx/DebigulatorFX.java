/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.debigulatorfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DebigulatorFX extends Application{
    
    @Override
    public void start(Stage stage) throws Exception{
        Parent root=FXMLLoader.load(getClass().getResource("DebigulatorFXML.fxml"));
        Scene scene=new Scene(root);
        stage.setTitle("Debigulator FX 1.1");
        stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("com/huguesjohnson/debigulatorfx/res/application-x-executable.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
    
}
