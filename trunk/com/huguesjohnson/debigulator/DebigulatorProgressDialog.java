/*
Debigulator - A batch compression utility
DebigulatorProgressDialog.java - Dialog to display compression progress
Copyright  (C) 2000-2007 Hugues Johnson
 
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
 
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
the GNU General Public License for more details.
 
You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.huguesjohnson.debigulator;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.String;
import java.lang.StringBuffer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.huguesjohnson.debigulator.DebigulatorConstants;

/** 
 * Modal dialog used to show the progess of an archiving job in Debigulator, probably could be used for something else but not sure why you'd want to do that.
 */
public class DebigulatorProgressDialog extends JDialog implements ActionListener{
	/* Determines if a de-serialized file is compatible with this class. See <a href="http://java.sun.com/j2se/1.5.0/docs/guide/serialization/spec/class.html">Java Object Serialization Specifications</a>. */
	private static final long serialVersionUID=-2460495834679225442L;
	/* indicates dialog was closed with a cancel status */
    private boolean cancelStatus;
    /* ok button */
    private JButton okButton;
    /* cancel button */
    private JButton cancelButton;
    /* total progress */
    private JProgressBar totalProgressBar;
    /* current file progress */
    private JProgressBar currentFileProgressBar;
    
    /** 
     * Creates a new modal DebigulatorProgressDialog.
     * @param owner The owner (parent) for this dialog.
     */
    public DebigulatorProgressDialog(Frame owner){
        super(owner,"Running, please wait",true);
        this.setResizable(false);
        this.setUndecorated(true);
        //create panel to hold stuff
        JPanel contentPane=new JPanel();
        GridLayout layout=new GridLayout(6,1);
        contentPane.setLayout(layout);
        contentPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        //add stuff
        contentPane.add(new JLabel("Current file progress",SwingConstants.LEFT));
        this.currentFileProgressBar=new JProgressBar(SwingConstants.HORIZONTAL,0,100);
        this.currentFileProgressBar.setStringPainted(true);
        contentPane.add(this.currentFileProgressBar);
        contentPane.add(new JLabel("Total progress",SwingConstants.LEFT));
        this.totalProgressBar=new JProgressBar(SwingConstants.HORIZONTAL,0,100);
        this.totalProgressBar.setStringPainted(true);
        contentPane.add(this.totalProgressBar);
        /* add blank panel for spacing */
        contentPane.add(new JPanel());
        /* add ok and cancel buttons */
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridLayout(1,3));
        /* add blank panel for spacing */
        buttonPanel.add(new JPanel());
        this.okButton=new JButton(DebigulatorConstants.ACTION_OK);
        this.okButton.setMnemonic(KeyEvent.VK_O);
        this.okButton.setActionCommand(DebigulatorConstants.ACTION_OK);
        this.okButton.addActionListener(this);
        this.okButton.setEnabled(false);
        buttonPanel.add(this.okButton);
        this.cancelButton=new JButton(DebigulatorConstants.ACTION_CANCEL);
        this.cancelButton.setMnemonic(KeyEvent.VK_C);
        this.cancelButton.setActionCommand(DebigulatorConstants.ACTION_CANCEL);
        this.cancelButton.addActionListener(this);
        this.cancelButton.setEnabled(true);
        buttonPanel.add(this.cancelButton);
        contentPane.add(buttonPanel);
        this.setContentPane(contentPane);
        //pack and center to the parent window
        this.pack();
        Rectangle parentDimension=this.getOwner().getBounds();
        Rectangle windowDimension=this.getBounds();
        this.setLocation(((parentDimension.width-windowDimension.width)/2)+(int)this.getOwner().getLocationOnScreen().getX(),((parentDimension.height-windowDimension.height)/2)+(int)this.getOwner().getLocationOnScreen().getY());
    }
    
    /** 
     * Show or hide the dialog, automatically centers to parent frame.
     * @param visible Set to true to display dialog, false to hide.
     */
    public void setVisible(boolean visible){
        Rectangle parentDimension=this.getOwner().getBounds();
        Rectangle windowDimension=this.getBounds();
        this.setLocation(((parentDimension.width-windowDimension.width)/2)+(int)this.getOwner().getLocationOnScreen().getX(),((parentDimension.height-windowDimension.height)/2)+(int)this.getOwner().getLocationOnScreen().getY());
        super.setVisible(visible);
    }
    
    /** 
     * Updates the total progress of the archive operation.
     * If percent complete reaches 100 then the "OK" button is enabled and the "Cancel" button is disabled.
     * @param progress A text description of the current action, i.e. "working on something"
     * @param percentComplete The percent complete.
     */
    public void setTotalProgress(String progress,int percentComplete){
        this.totalProgressBar.setString(progress);
        this.totalProgressBar.setValue(percentComplete);
        if(percentComplete>=100){
            this.okButton.setEnabled(true);
            this.cancelButton.setEnabled(false);
        }
    }
    
    /** 
     * Updates the progress of the current file compression.
     * @param progress A text description of the current action, i.e. "compressing file X"
     * @param percentComplete The percent complete.
     */
    public void setCurrentFileProgress(String progress,int percentComplete){
        this.currentFileProgressBar.setString(progress);
        this.currentFileProgressBar.setValue(percentComplete);
    }
    
    /** 
     * Returns wheter or not the dialog was closed with the cancel button.
     * @return True if cancel button was selected.
     */
    public boolean getCancelStatus(){
        return(this.cancelStatus);
    }
    
    /** 
     * Listener for action events (just the two button clicks).
     * @param actionEvent The action that was performed.
     */
    public void actionPerformed(ActionEvent actionEvent){
        String actionCommand=actionEvent.getActionCommand();
        if(actionCommand.equals(DebigulatorConstants.ACTION_OK)){
            this.cancelStatus=false;
            this.setVisible(false);
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_CANCEL)){
            this.cancelStatus=true;
            this.setVisible(false);
        }
    }
    
    /** returns a string representation of the object
     * @return string representation of the object
     */
    public String toString(){
        StringBuffer tostring=new StringBuffer(super.toString());
        tostring.append("\nthis.totalProgressBar.getString():");
        tostring.append(this.totalProgressBar.getString());
        tostring.append("\nthis.totalProgressBar.getValue():");
        tostring.append(this.totalProgressBar.getValue());
        tostring.append("\nthis.currentFileProgressBar.getString():");
        tostring.append(this.currentFileProgressBar.getString());
        tostring.append("\nthis.currentFileProgressBar.getValue():");
        tostring.append(this.currentFileProgressBar.getValue());
        return(tostring.toString());
    }
}