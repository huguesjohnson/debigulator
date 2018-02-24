/*
    Debigulator - A batch compression utility
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.debigulatorfx;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FilePathTreeItem extends TreeItem<String>{
    public static Image folderCollapseImage=new Image(ClassLoader.getSystemResourceAsStream("com/huguesjohnson/debigulatorfx/res/folder.png"));
    public static Image folderExpandImage=new Image(ClassLoader.getSystemResourceAsStream("com/huguesjohnson/debigulatorfx/res/folder-open.png"));
    public static Image fileImage=new Image(ClassLoader.getSystemResourceAsStream("com/huguesjohnson/debigulatorfx/res/text-x-generic.png"));
    private boolean isLeaf;
    private boolean isFirstTimeChildren=true;
    private boolean isFirstTimeLeaf=true;
    private final File file;
    public File getFile(){return(this.file);}
    private final String absolutePath;
    public String getAbsolutePath(){return(this.absolutePath);}
    private final boolean isDirectory;
    public boolean isDirectory(){return(this.isDirectory);}
    
    public FilePathTreeItem(File file){
        super(file.toString());
        this.file=file;
        this.absolutePath=file.getAbsolutePath();
        this.isDirectory=file.isDirectory();
        if(this.isDirectory){
            this.setGraphic(new ImageView(folderCollapseImage));
            //add event handlers
            this.addEventHandler(TreeItem.branchCollapsedEvent(),new EventHandler(){
                @Override
                public void handle(Event e){
                    FilePathTreeItem source=(FilePathTreeItem)e.getSource();
                    if(!source.isExpanded()){
                        ImageView iv=(ImageView)source.getGraphic();
                        iv.setImage(folderCollapseImage);
                    }
                }
            } );
            this.addEventHandler(TreeItem.branchExpandedEvent(),new EventHandler(){
                @Override
                public void handle(Event e){
                    FilePathTreeItem source=(FilePathTreeItem)e.getSource();
                    if(source.isExpanded()){
                        ImageView iv=(ImageView)source.getGraphic();
                        iv.setImage(folderExpandImage);
                    }               
                }
            } );            
    }else{
            this.setGraphic(new ImageView(fileImage));
        }
        //set the value (which is what is displayed in the tree)
        String fullPath=file.getAbsolutePath();
        if(!fullPath.endsWith(File.separator)){
            String value=file.toString();
            int indexOf=value.lastIndexOf(File.separator);
            if(indexOf>0){
                this.setValue(value.substring(indexOf+1));
            }else{
                this.setValue(value);
            }
        }
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren(){
        if(isFirstTimeChildren){
            isFirstTimeChildren=false;
            super.getChildren().setAll(buildChildren(this));
        }
        return(super.getChildren());
    }

    @Override
    public boolean isLeaf(){
        if(isFirstTimeLeaf){
            isFirstTimeLeaf=false;
            isLeaf=this.file.isFile();
        }
        return(isLeaf);
    }
 
    private ObservableList<FilePathTreeItem> buildChildren(FilePathTreeItem treeItem){
        File f=treeItem.getFile();
        if((f!=null)&&(f.isDirectory())){
            File[] files=f.listFiles();
            if (files!=null){
                ObservableList<FilePathTreeItem> children=FXCollections.observableArrayList();
                for(File childFile:files){
                    children.add(new FilePathTreeItem(childFile));
                }
                return(children);
            }
        }
        return FXCollections.emptyObservableList();
    }

}
