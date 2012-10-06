/*
Debigulator - A batch compression utility
DebigulatorAdvancedSettingsDialog.java - Dialog to display/update advanced program settings
Copyright  (C) 2003-2007 Hugues Johnson
 
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

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** 
 * Dialog to edit advanced settings for Debigulator.
 * @author Hugues Johnson
 */
public class DebigulatorAdvancedSettingsDialog extends JDialog implements ActionListener{
	/* Determines if a de-serialized file is compatible with this class. See <a href="http://java.sun.com/j2se/1.5.0/docs/guide/serialization/spec/class.html">Java Object Serialization Specifications</a>. */
	private static final long serialVersionUID=5985647814403320920L;
	/* the data being manipulated by this dialog */
	private DebigulatorSession sessionData;
	/* indicates dialog was closed with a cancel status */
    private boolean cancelStatus;
    /* checkbox for append date */
    private JCheckBox appendDateCheckBox;
    /* checkbox for append extension */
    private JCheckBox appendExtensionCheckBox;
    /* checkbox for save session on exit */
    private JCheckBox saveSessionOnExitCheckBox;
    /* checkbox for debug logs */
    private JCheckBox enableDebugLogsCheckBox;
    
    /** 
     * Creates a new DebigulatorAdvancedSettingsDialog, dialog is modal.
     * @param owner The owner for this dialog.
     * @param sessionData The session data being manipulated by this dialog.
     */
    public DebigulatorAdvancedSettingsDialog(Frame owner,DebigulatorSession sessionData){
        super(owner,"Advanced Settings",true);
        this.setResizable(false);
        this.sessionData=sessionData;
        /* set cancelStatus to true by default, if "X" on title bar is pressed the dialog should return cancel status */
        this.cancelStatus=true; 
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        JPanel contentPane=new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.Y_AXIS));
        //this.getContentPane().setLayout(new GridLayout(9,1));
        /* add checkboxes and labels */
        this.saveSessionOnExitCheckBox=new JCheckBox("Automatically save current session on program exit",false);
        this.saveSessionOnExitCheckBox.setSelected(this.sessionData.getAutoSave());
        contentPane.add(this.saveSessionOnExitCheckBox);
        this.appendDateCheckBox=new JCheckBox("Append archive names with date (yyyy-mm-dd)",false);
        this.appendDateCheckBox.setSelected(this.sessionData.getThreadParameters().getAppendDate());
        contentPane.add(this.appendDateCheckBox);
        contentPane.add(new JLabel("<html><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Example: Filename.exe => 2003-07-29_Filename.zip</i></html>"));
        this.appendExtensionCheckBox=new JCheckBox("Append archive names with original file extension",false);
        this.appendExtensionCheckBox.setSelected(this.sessionData.getThreadParameters().getAppendExtension());
        contentPane.add(this.appendExtensionCheckBox);
        contentPane.add(new JLabel("<html><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Example: Filename.exe => Filename_exe.zip</i></html>"));
        this.enableDebugLogsCheckBox=new JCheckBox("Enable debug logs",false);
        this.enableDebugLogsCheckBox.setSelected(this.sessionData.getThreadParameters().getWriteDebugLog());
        contentPane.add(this.enableDebugLogsCheckBox);
        contentPane.add(new JLabel("<html><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Logs will reside in $runtime_path\\logs</i></html>"));
        /* add blank panel for spacing */
        contentPane.add(new JPanel());
        this.getContentPane().add(contentPane);
        /* add ok and cancel buttons */
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton=new JButton(DebigulatorConstants.ACTION_OK);
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.setActionCommand(DebigulatorConstants.ACTION_OK);
        okButton.addActionListener(this);
        okButton.setEnabled(true);
        buttonPanel.add(okButton);
        JButton cancelButton=new JButton(DebigulatorConstants.ACTION_CANCEL);
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setActionCommand(DebigulatorConstants.ACTION_CANCEL);
        cancelButton.addActionListener(this);
        cancelButton.setEnabled(true);
        buttonPanel.add(cancelButton);
        this.getContentPane().add(buttonPanel);
        //pack and center to parent
        this.pack();
        Rectangle parentDimension=this.getOwner().getBounds();
        Rectangle windowDimension=this.getBounds();
        this.setLocation(((parentDimension.width-windowDimension.width)/2)+(int)this.getOwner().getLocationOnScreen().getX(),((parentDimension.height-windowDimension.height)/2)+(int)this.getOwner().getLocationOnScreen().getY());
    }
    
    /**
     * Returns the data updated by this dialog.
     * @return The data updated by this dialog.
     */
    public DebigulatorSession getSessionData(){
    	this.sessionData.getThreadParameters().setAppendDate(this.appendDateCheckBox.isSelected());
    	this.sessionData.getThreadParameters().setAppendExtension(this.appendExtensionCheckBox.isSelected());
    	this.sessionData.getThreadParameters().setWriteDebugLog(this.enableDebugLogsCheckBox.isSelected());
    	this.sessionData.setAutoSave(this.saveSessionOnExitCheckBox.isSelected());
    	return(this.sessionData);
    }
    
    /** 
     * Listener for action events.
     * @param actionEvent The action performed.
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
    
    /** 
     * Returns whether or not the dialog was closed with a cancel status.
     * @return True if cancel button was selected or dialog was closed by hitting "X" on the title bar.
     */
    public boolean getCancelStatus(){
        return(this.cancelStatus);
    }
}