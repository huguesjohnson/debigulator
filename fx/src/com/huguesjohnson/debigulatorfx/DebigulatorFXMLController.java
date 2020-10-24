/*
Debigulator - A batch compression utility
Copyright (C) 2003-2020 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.debigulatorfx;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.huguesjohnson.debigulator.BatchCompressionTask;
import com.huguesjohnson.debigulator.Enums;
import com.huguesjohnson.debigulator.BatchCompressionThreadParameters;
import com.huguesjohnson.debigulator.DebigulatorSession;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class DebigulatorFXMLController{
    private BatchCompressionTask task=null;
    private String sessionPath=null;
    private ProgressBar progressTotal;
    private Stage progressStage;
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonSaveSession;

    @FXML
    private ChoiceBox<Enums.ActionAfterArchive> choiceActionAfterArchive;

    @FXML
    private CheckBox checkboxAutomaticallySaveSession;

    @FXML
    private CheckBox checkboxAppendDate;

    @FXML
    private ChoiceBox<?> choiceFilter;

    @FXML
    private Button buttonBrowse;

    @FXML
    private Button buttonAddAll;

    @FXML
    private Button buttonHelp;

    @FXML
    private ListView<String> listviewFiles;

    @FXML
    private CheckBox checkboxEnableDebugLogs;

    @FXML
    private BorderPane paneTop;

    @FXML
    private CheckBox checkboxAppendExtension;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonAddSelected;

    @FXML
    private Button buttonNewSession;

    @FXML
    private Button buttonSaveSessionAs;

    @FXML
    private TreeView<String> treeviewFileBrowse;

    @FXML
    private Button buttonRemoveAll;

    @FXML
    private Button buttonOpenSession;

    @FXML
    private TextField textOutputDirectory;

    @FXML
    private ChoiceBox<Enums.ArchiveType> choiceArchiveType;

    @FXML
    private Button buttonCreateArchives;

    @FXML
    private Button buttonRemoveSelected;

    @FXML
    void actionBrowse(ActionEvent event){browse();}
    private void browse(){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        directoryChooser.setTitle("Select output directory");
        Stage stage=(Stage)this.paneTop.getScene().getWindow();
        File f=directoryChooser.showDialog(stage);
        if(f!=null){
            this.textOutputDirectory.setText(f.getAbsolutePath());
        }
    }

    @FXML
    void actionCancel(ActionEvent event){cancel();}
    private void cancel(){
    
    }

    @FXML
    void actionCreateArchives(ActionEvent event){createArchives();}
    private void createArchives(){ 
        if(this.listviewFiles.getItems().size()>0){
            lockUI(true);
            this.task=new BatchCompressionTask(this.getSession().getThreadParameters());
            this.task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,(WorkerStateEvent wse)->{
                onTaskSucceeded();
            });
            this.task.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED,(WorkerStateEvent wse)->{
                onTaskCancelled();
            });
            this.task.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,(WorkerStateEvent wse)->{
                onTaskFailed();
            });
            showProgressDialog();
            Thread t=new Thread(this.task);
            t.setDaemon(true);
            t.start();
        }
    }

    @FXML
    void actionAddAll(ActionEvent event){addAll();}
    private void addAll(){
    
    }

    @FXML
    void actionAddSelected(ActionEvent event){addSelected();}
    private void addSelected(){
        ObservableList<Integer> indices=this.treeviewFileBrowse.getSelectionModel().getSelectedIndices();
        for(int i=0;i<indices.size();i++){
            int index=indices.get(i);
            if(index>0){//don't add the root item
                addItem((FilePathTreeItem)this.treeviewFileBrowse.getTreeItem(index));
            }
        }
    }
    
    //sorts out if the item being added is a file or directory
    private void addItem(FilePathTreeItem item){
    	if(item.isDirectory()){
    		addDirectory(item.getFile());
    	}else{
    		addFilePath(item.getAbsolutePath());
        }
    }
    
    //add all files in a directory
    private void addDirectory(File dir){
    	for(final File f:dir.listFiles()){
    		if(f.isDirectory()){
    			addDirectory(f);
            }else{
            	addFilePath(f.getAbsolutePath());
            }
        }
    	
    }
    
    //adds a file to the list of things to archive
    private void addFilePath(String absolutePath){
        //prevent duplicate entries
    	if(!this.listviewFiles.getItems().contains(absolutePath)){
            this.listviewFiles.getItems().add(absolutePath);
        }
    	
    }

    @FXML
    void actionRemoveAll(ActionEvent event){removeAll();}
    private void removeAll(){
        this.listviewFiles.getItems().clear();
    }

    @FXML
    void actionRemoveSelected(ActionEvent event){removeSelected();}
    private void removeSelected(){
        ObservableList<Integer> indices=this.listviewFiles.getSelectionModel().getSelectedIndices();
        for(int i=indices.size()-1;i>=0;i--){
            this.listviewFiles.getItems().remove(indices.get(i).intValue());
        }
    }

    @FXML
    void actionNewSession(ActionEvent event){newSession();}
    private void newSession(){
        boolean autoSave=this.checkboxAutomaticallySaveSession.isSelected();
        if(autoSave){
            this.saveSession();
        }
        this.sessionPath=null;
        this.checkboxAutomaticallySaveSession.setDisable(true);
        //clear out the window
        this.textOutputDirectory.setText("");
        this.listviewFiles.getItems().clear();
        this.checkboxAppendDate.setSelected(false);
        this.checkboxAppendExtension.setSelected(false);
        this.checkboxAutomaticallySaveSession.setSelected(false);
        this.checkboxEnableDebugLogs.setSelected(false);
        this.choiceArchiveType.getSelectionModel().select(0);
        this.choiceActionAfterArchive.getSelectionModel().select(0);
    }

    @FXML
    void actionOpenSession(ActionEvent event){openSession();}
    private void openSession(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Open session");
        Stage stage=(Stage)this.paneTop.getScene().getWindow();
        File f=fileChooser.showOpenDialog(stage);
        if(f!=null){
            this.sessionPath=f.getAbsolutePath();
            Gson gson=new Gson();
            try{
                DebigulatorSession session=(DebigulatorSession)gson.fromJson(new FileReader(this.sessionPath),DebigulatorSession.class);
                this.setSession(session);
            }catch(FileNotFoundException|JsonSyntaxException|JsonIOException x){
                x.printStackTrace();
            }
        }
    }

    @FXML
    void actionSaveSession(ActionEvent event){saveSession();}
    public void saveSession(){
        if(this.sessionPath==null){
            showSaveAsDialog();
            //did the user cancel?
            if(this.sessionPath==null){return;}
        }
        DebigulatorSession session=getSession();
        Gson gson=new Gson();
        String json=gson.toJson(session);
        try{
            FileOutputStream out=new FileOutputStream(new File(this.sessionPath),false);
            out.write(json.getBytes());
            out.flush();
            out.close();
        }catch(Exception x){
            x.printStackTrace();
        }
    }

    @FXML
    void actionSaveSessionAs(ActionEvent event){saveSessionAs();}
    private void saveSessionAs(){
        //do we need to save the current session?
        if(sessionPath!=null){
            boolean autoSave=this.checkboxAutomaticallySaveSession.isSelected();
            if(autoSave){saveSession();}
        }
        showSaveAsDialog();
        if(sessionPath!=null){saveSession();}
    }

    private void showSaveAsDialog(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Save session");
        Stage stage=(Stage)this.paneTop.getScene().getWindow();
        File f=fileChooser.showSaveDialog(stage);
        if(f!=null){
            this.sessionPath=f.getAbsolutePath();
        }
    }
    
    @FXML
    void actionHelp(ActionEvent event){help();}
    private void help(){
        try{
            Desktop.getDesktop().browse(new URI("http://huguesjohnson.com/debigulator.html"));
        }catch(URISyntaxException|IOException x){
            x.printStackTrace();
        }
    }

    @FXML
    void initialize(){
        assert buttonSaveSession != null : "fx:id=\"buttonSaveSession\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert choiceActionAfterArchive != null : "fx:id=\"choiceActionAfterArchive\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert checkboxAutomaticallySaveSession != null : "fx:id=\"checkboxAutomaticallySaveSession\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert checkboxAppendDate != null : "fx:id=\"checkboxAppendDate\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert choiceFilter != null : "fx:id=\"choiceFilter\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonBrowse != null : "fx:id=\"buttonBrowse\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonAddAll != null : "fx:id=\"buttonAddAll\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonHelp != null : "fx:id=\"buttonHelp\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert listviewFiles != null : "fx:id=\"listviewFiles\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert checkboxEnableDebugLogs != null : "fx:id=\"checkboxEnableDebugLogs\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert paneTop != null : "fx:id=\"paneTop\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert checkboxAppendExtension != null : "fx:id=\"checkboxAppendExtension\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonCancel != null : "fx:id=\"buttonCancel\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonAddSelected != null : "fx:id=\"buttonAddSelected\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonNewSession != null : "fx:id=\"buttonNewSession\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonSaveSessionAs != null : "fx:id=\"buttonSaveSessionAs\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert treeviewFileBrowse != null : "fx:id=\"treeviewFileBrowse\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonRemoveAll != null : "fx:id=\"buttonRemoveAll\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonOpenSession != null : "fx:id=\"buttonOpenSession\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert textOutputDirectory != null : "fx:id=\"textOutputDirectory\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert choiceArchiveType != null : "fx:id=\"choiceArchiveType\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonCreateArchives != null : "fx:id=\"buttonCreateArchives\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        assert buttonRemoveSelected != null : "fx:id=\"buttonRemoveSelected\" was not injected: check your FXML file 'DebigulatorFXML.fxml'.";
        this.textOutputDirectory.setText(System.getProperty("user.home"));
        //populate file browser
        String hostName=null;
        try{hostName=InetAddress.getLocalHost().getHostName();}catch(UnknownHostException x){ System.out.println(x);} //TODO error dialog
        assert hostName != null : "Unable to get local host name.";
        TreeItem<String> rootNode=new TreeItem<>(hostName,new ImageView(new Image(ClassLoader.getSystemResourceAsStream("com/huguesjohnson/debigulatorfx/res/computer.png"))));
        Iterable<Path> rootDirectories=FileSystems.getDefault().getRootDirectories();
        for(Path name:rootDirectories){
            FilePathTreeItem treeNode=new FilePathTreeItem(new File(name.toString()));
            rootNode.getChildren().add(treeNode);
        }
        rootNode.setExpanded(true);
        this.treeviewFileBrowse.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.treeviewFileBrowse.setRoot(rootNode);
        //populate choiceboxes
        this.choiceArchiveType.setItems(FXCollections.observableArrayList(Enums.ArchiveType.values()));
        this.choiceArchiveType.getSelectionModel().select(0);
        this.choiceActionAfterArchive.setItems(FXCollections.observableArrayList(Enums.ActionAfterArchive.values()));
        this.choiceActionAfterArchive.getSelectionModel().select(0);
        //set file list to multi-select
        this.listviewFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //determine whether to show the help button
        if(!Desktop.isDesktopSupported()||(!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))){
        	this.buttonHelp.setVisible(false);
        }
    }

    public DebigulatorSession getSession(){
        DebigulatorSession session=new DebigulatorSession();
        session.setAutoSave(this.checkboxAutomaticallySaveSession.isSelected());
        BatchCompressionThreadParameters parameters=new BatchCompressionThreadParameters();
        parameters.setAction(this.choiceActionAfterArchive.getValue());
        parameters.setAppendDate(this.checkboxAppendDate.isSelected());
        parameters.setAppendExtension(this.checkboxAppendExtension.isSelected());
        parameters.setArchiveType(this.choiceArchiveType.getValue());
        String[] sourceFileList=this.listviewFiles.getItems().toArray(new String[0]);
        parameters.setInputFileList(sourceFileList);
        parameters.setOutputDirectory(this.textOutputDirectory.getText());
        parameters.setWriteDebugLog(this.checkboxEnableDebugLogs.isSelected());
        session.setThreadParameters(parameters);
        return(session);
    }
    
    private void setSession(DebigulatorSession session){
        this.checkboxAutomaticallySaveSession.setSelected(session.getAutoSave());
        BatchCompressionThreadParameters parameters=session.getThreadParameters();
        this.choiceActionAfterArchive.setValue(parameters.getAction());
        this.checkboxAppendDate.setSelected(parameters.getAppendDate());
        this.checkboxAppendExtension.setSelected(parameters.getAppendExtension());
        this.choiceArchiveType.setValue(parameters.getArchiveType());
        this.textOutputDirectory.setText(parameters.getOutputDirectory());
        this.checkboxEnableDebugLogs.setSelected(parameters.getWriteDebugLog());
        removeAll();
        this.listviewFiles.getItems().addAll(parameters.getFileList());
    }
    
    private void lockUI(boolean locked){
        this.buttonSaveSession.setDisable(locked);
        this.choiceActionAfterArchive.setDisable(locked);
        this.checkboxAutomaticallySaveSession.setDisable(locked);
        this.checkboxAppendDate.setDisable(locked);
        this.choiceFilter.setDisable(locked);
        this.buttonBrowse.setDisable(locked);
        this.buttonAddAll.setDisable(locked);
        this.buttonHelp.setDisable(locked);
        this.listviewFiles.setDisable(locked);
        this.checkboxEnableDebugLogs.setDisable(locked);
        this.paneTop.setDisable(locked);
        this.checkboxAppendExtension.setDisable(locked);
        this.buttonCancel.setDisable(locked);
        this.buttonAddSelected.setDisable(locked);
        this.buttonNewSession.setDisable(locked);
        this.buttonSaveSessionAs.setDisable(locked);
        this.treeviewFileBrowse.setDisable(locked);
        this.buttonRemoveAll.setDisable(locked);
        this.buttonOpenSession.setDisable(locked);
        this.textOutputDirectory.setDisable(locked);
        this.choiceArchiveType.setDisable(locked);
        this.buttonCreateArchives.setDisable(locked);
        this.buttonRemoveSelected.setDisable(locked);
    }

    private void showProgressDialog(){
        this.progressStage=new Stage();
        Group root=new Group(); 
        Scene scene=new Scene(root,300,80);
        this.progressStage.setScene(scene);
        this.progressStage.setTitle("Creating archives");
        Label totalProgress=new Label();
        totalProgress.setMaxWidth(Double.MAX_VALUE);
        totalProgress.setText("Total progress");
        this.progressTotal=new ProgressBar();
        this.progressTotal.setMaxWidth(Double.MAX_VALUE);
        this.progressTotal.progressProperty().bind(this.task.progressProperty());
        final VBox vbox=new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(4,4,4,4));
        vbox.getChildren().add(totalProgress);
        vbox.getChildren().add(this.progressTotal);
        scene.setRoot(vbox);
        this.progressStage.setOnCloseRequest((WindowEvent e)->{e.consume();});
        this.progressStage.initStyle(StageStyle.UTILITY);
        this.progressStage.initOwner(this.paneTop.getScene().getWindow());
        this.progressStage.show();
    }
    
    private void onTaskSucceeded(){
        this.progressStage.close();
        this.lockUI(false);
        this.task=null;
    }
    
    private void onTaskCancelled(){
        this.progressStage.close();
        this.lockUI(false);
        this.task=null;
    }

    private void onTaskFailed(){
        this.progressStage.close();
        this.lockUI(false);
        this.task=null;
    }
    
}
