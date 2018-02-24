/*
NoFrillsJFileChooser.java - minimal JFileChooser
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.JTextComponent;

/** 
 * This is a minimal JFileChooser that only displays the file list and none of the other functions.
 * <i>Why was this thing written?</i> 
 * This is intended to work like a file list box and directory list box in Visual Basic, there's nothing like them in Swing (or .NET either).
 * This breaks on pretty much every JDK/Swing update so I should scrap this stupid idea and port the Visual Basic controls.
 * <p>
 * Overrides all constructors in JFileChooser.
 * Intended to be displayed in a panel, not as a stand-alone dialog.
 */
public class NoFrillsJFileChooser extends JFileChooser{
	/* Determines if a de-serialized file is compatible with this class. See <a href="http://java.sun.com/j2se/1.5.0/docs/guide/serialization/spec/class.html">Java Object Serialization Specifications</a>. */
	private static final long serialVersionUID=-3910303439474569206L;

	/** 
	 * Constructs a NoFrillsJFileChooser pointing to the user's default directory.
     */
    public NoFrillsJFileChooser(){
        super();
        this.trimComponents();
    }
    
    /** 
     * Constructs a NoFrillsJFileChooser using the given path. Passing in a null string causes the file chooser to point to the user's default directory. This default depends on the operating system.
     * @param currentDirectoryPath A String giving the path to a file or directory.
     */
    public NoFrillsJFileChooser(String currentDirectoryPath){
        super(currentDirectoryPath);
        this.trimComponents();
    }
    
    /** 
     * Constructs a NoFrillsJFileChooser using the given File as the path. Passing in a null file causes the file chooser to point to the user's default directory. This default depends on the operating system.
     * @param currentDirectory A File object specifying the path to a file or directory.
     */
    public NoFrillsJFileChooser(File currentDirectory){
        super(currentDirectory);
        this.trimComponents();
    }
    
    /** 
     * Constructs a NoFrillsJFileChooser using the given FileSystemView.
     * @param fsv The FileSystemView.
     */
    public NoFrillsJFileChooser(FileSystemView fsv){
        super(fsv);
        this.trimComponents();
    }
    
    /** 
     * Constructs a NoFrillsJFileChooser using the given current directory and FileSystemView.
     * @param currentDirectory A File object specifying the path to a file or directory.
     * @param fsv The FileSystemView.
     */
    public NoFrillsJFileChooser(File currentDirectory,FileSystemView fsv){
        super(currentDirectory,fsv);
        this.trimComponents();
    }
    
    
    /** 
     * Constructs a NoFrillsJFileChooser using the given current directory path and FileSystemView.
     * @param currentDirectoryPath A String giving the path to a file or directory.
     * @param fsv The FileSystemView.
     */
    public NoFrillsJFileChooser(String currentDirectoryPath,FileSystemView fsv){
        super(currentDirectoryPath,fsv);
        this.trimComponents();
    }
    
    /* trims out components that aren't needed for display */
    private void trimComponents(){
        try{
            /* trim excess space, default Vgap for JFileChooser is 11 */
            LayoutManager layoutManager=this.getLayout();
            if(layoutManager instanceof BorderLayout){
                ((BorderLayout)layoutManager).setVgap(1);
            } else{
                System.err.println(new Exception("Condition Failed: (layoutManager instanceof BorderLayout)\nLayoutManager for JFileChooser is now: "+layoutManager.getClass().getName()));
            }
            /* JFileChooser (as of right now) is comprised of 3 JPanels */
            Component[] components=this.getComponents();
            if(components.length==4){
                /* the first panel holds three components
                 * 1) A JLabel that says "Look In" - leave
                 * 2) A combo box that lists drives a system folders - leave
                 * 3) Another JPanel with navigation buttons - want to eliminate "New Folder" and "Desktop" buttons from this last panel
                 */
                if(components[0] instanceof JPanel){
                    this.handleJPanel((JPanel)components[0]);
                } else{
                	System.err.println(new Exception("Condition Failed: (if(components[0] instanceof JPanel))\ncomponents[0] is now: "+components[0].getClass().getName()));
                }
                /* the second panel is apparently empty
                 */
                if(components[1] instanceof JPanel){
                	int componentCount=((JPanel)components[1]).getComponentCount();
                	if(componentCount>0){
                        throw (new Exception("Condition Failed: (if(componentCount>0))\ncomponentCount is now: "+componentCount));
                	}
                } else{
                	System.err.println(new Exception("Condition Failed: (if(components[1] instanceof JPanel))\ncomponents[1] is now: "+components[1].getClass().getName()));
                }
                /* the third panel holds the actual file list
                 * leave it alone for now */
                /* the fourth panel holds the file name box and filter drop down
                 * want the file filter drop down and nothing else */
                /* getting rid of the approve and cancel buttons are the easy part */
                this.setControlButtonsAreShown(false);
                if(components[3] instanceof JPanel){
                    this.handleJPanel((JPanel)components[3]);
                } else{
                	System.err.println(new Exception("Condition Failed: (if(components[3] instanceof JPanel))\ncomponents[3] is now: "+components[3].getClass().getName()));
                }
            } else{
            	System.err.println(new Exception("Condition Failed: (if(components.length==4))\nJFileChooser is now comprised of "+components.length+" components"));
            }
        } catch(Exception x){
            x.printStackTrace();
        }
    }
    
    /* handle JPanels */
    private void handleJPanel(JPanel jpanel){
        Component[] panelComponents=jpanel.getComponents();
        int panelComponentCount=panelComponents.length;
        for(int panelComponentIndex=0;panelComponentIndex<panelComponentCount;panelComponentIndex++){
            if(panelComponents[panelComponentIndex] instanceof JPanel){
                this.handleJPanel((JPanel)panelComponents[panelComponentIndex]);
            } else if(panelComponents[panelComponentIndex] instanceof AbstractButton){
                this.handleAbstractButton((AbstractButton)panelComponents[panelComponentIndex]);
            } else if(panelComponents[panelComponentIndex] instanceof JLabel){
                this.handleJLabel((JLabel)panelComponents[panelComponentIndex]);
            } else if(panelComponents[panelComponentIndex] instanceof javax.swing.Box.Filler){
                this.handleBoxFiller((javax.swing.Box.Filler)panelComponents[panelComponentIndex]);
            } else if(panelComponents[panelComponentIndex] instanceof JComboBox){
                this.handleJComboBox((JComboBox)panelComponents[panelComponentIndex]);
            } else if(panelComponents[panelComponentIndex] instanceof JTextComponent){
                this.handleJTextComponent((JTextComponent)panelComponents[panelComponentIndex]);
            } else{
                /* some unexpected component was added.. can't think if anything should be done here or not */
            }
        }
    }
    
    /* handle AbstractButtons */
    private void handleAbstractButton(AbstractButton abstractButton){
        String toolTipText=abstractButton.getToolTipText().toLowerCase();
        if(toolTipText.indexOf("create")>=0){ //in case they add more "create" buttons than just "Create New Folder"
            abstractButton.setVisible(false);
        } else if(toolTipText.indexOf("desktop")>=0){ //desktop is also accessible from the drop down combo so there's no need for this button too
            abstractButton.setVisible(false);
        }
    }
    
    /* handle BoxFiller */
    private void handleBoxFiller(javax.swing.Box.Filler boxFiller){
        boxFiller.setVisible(false);
    }
    
    /* handle JLabel */
    private void handleJLabel(JLabel jlabel){
        String text=jlabel.getText().toLowerCase();
        if(text.indexOf("look in")>=0){ //look in label is just taking up space.. I hope everyone can figure out what the corresponding combo box next to it does
            jlabel.setVisible(false);
        } else if(text.indexOf("file name")>=0){
            jlabel.setVisible(false);
        } else if(text.indexOf("files of type")>=0){
            /* for some reason when the text is changed the label doen't resize correctly
             * I'd like to change this to "Filter" instead of hiding it, oh well, people should be able to figure out what the corresponding combo box does
             */
            //jlabel.setText("Filter: ");
            jlabel.setVisible(false);
        }
    }
    
    /* handle JComboBox */
    private void handleJComboBox(JComboBox jcombobox){
        /* not doing anything to the combo boxes, yet..  */
    }
    
    /* handle JTextComponent */
    private void handleJTextComponent(JTextComponent jtextcomponent){
        /* this is slightly risky, as of right now there is only one JTextComponet which is the "File Name" box */
        jtextcomponent.setVisible(false);
    }
}
