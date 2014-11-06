/*
    Debigulator - A batch compression utility
    Copyright (C) 2003-2014 Hugues Johnson

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        stage.setTitle("Debigulator FX 1.0");
        stage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("com/huguesjohnson/debigulatorfx/res/application-x-executable.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
    
}