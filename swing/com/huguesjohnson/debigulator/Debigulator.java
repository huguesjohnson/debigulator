/*
Debigulator - A batch compression utility
Debigulator.java - Main program
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.debigulator;

import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import com.huguesjohnson.BatchCompressionThread;
import com.huguesjohnson.BatchCompressionThreadParameters;
import com.huguesjohnson.LaunchBrowser;
import com.huguesjohnson.fileFilter.SessionFileFilter;
import com.huguesjohnson.ui.SplashScreen;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/** 
 * Main class for the Debigulator application. 
 * Manages the frame, user actions, and batch compression thread.
 */
public class Debigulator{
    private final static boolean PROMPT_SAVE_CANCEL=true;
	/* the path of the current session */
    private String sessionPath;
    /* path to the current working directory */
    private String workingDirectory;
    /* path to the error log */
    private String errorLogPath;
    /* reference to the main window */
    private DebigulatorWindow window;
    /* reference to the progress dialog*/
    private DebigulatorProgressDialog progressDialog;
    /* reference to batch compression thread */
    private BatchCompressionThread batchCompressionThread;
    /* session */
    private DebigulatorSession session;
    
    /** 
     * Creates a new application with default settings.
     */
    public Debigulator(){
        /* set the working directory */
        this.setWorkingDirectory();
        /* create error log */
        this.setErrorStream();
        /* load the window */
        this.loadWindow();
        /* create a new session */
        this.newSession();
    }
    
    /** 
     * Creates a new application based on the session name passed.
     * @param sessionPath The path to the session to load.
     */
    public Debigulator(String sessionPath){
        /* set the working directory */
        this.setWorkingDirectory();
        /* load the window */
        this.loadWindow();
        /* create error log */
        this.setErrorStream();
        /* load the session */
        this.loadSession(sessionPath);
    }
    
     /* creates a text file to log run-time errors */
     private void setErrorStream(){
          try{
              File logDirectory=new File(this.workingDirectory+File.separator+"logs"); 
              if(!logDirectory.exists()){
                logDirectory.mkdir();
              }
              this.errorLogPath=logDirectory+File.separator+"error.log";
              System.setErr(new PrintStream(new FileOutputStream(this.errorLogPath,true)));
          } catch(Exception x){
               x.printStackTrace();
          }
     }    
    
    /* sets the working directory to the default value */
    private void setWorkingDirectory(){
    	this.workingDirectory=new String(System.getProperty("user.dir"));
    	if(!this.workingDirectory.endsWith(File.separator)){
    		this.workingDirectory=this.workingDirectory+File.separator;    		
    	}
    }
    
    /* saves the current session */
    private void saveSession(){
    	if(this.sessionPath==null){
    		this.saveSessionAs();
    	} else{
	        this.window.updateStatus(DebigulatorConstants.STATUS_SAVING);
	        this.window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    	this.updateSession();
	        try{
	        	XStream xstream=new XStream(new DomDriver());
				String xml=xstream.toXML(this.session);
				FileOutputStream out=new FileOutputStream(new File(this.sessionPath));
				out.write(xml.getBytes());
				out.flush();
				out.close();
	            this.window.updateStatus(DebigulatorConstants.STATUS_SAVED);
	        } catch(Exception x){
	        	errorHandler(x);
	            this.window.updateStatus(DebigulatorConstants.ERROR_SAVE+" "+this.sessionPath);
	        }
	        this.window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	}
    }
    
    /* opens a file chooser to browse for a session to save */
    private void saveSessionAs(){
        /* browse for session */
        JFileChooser fileChooser=new JFileChooser(this.sessionPath);
        fileChooser.setDialogTitle("Save Session");
        /* add SessionFileFilter */
        fileChooser.setFileFilter(new SessionFileFilter());
        if(fileChooser.showSaveDialog(this.window)==JFileChooser.APPROVE_OPTION){
            String fileName=fileChooser.getSelectedFile().getPath();
            if(!fileName.toLowerCase().endsWith(SessionFileFilter.EXTENSION.toLowerCase())){
                fileName=fileName.concat(SessionFileFilter.EXTENSION);
            }
            this.saveSession(fileName);
        }
    }
    
    /* saves the session with the specified name - resets application title */
    private void saveSession(String sessionPath){
        this.sessionPath=sessionPath;
        /* set caption for window */
        this.window.setTitle(DebigulatorConstants.PROGRAM_TITLE+" - "+sessionPath.substring(sessionPath.lastIndexOf(File.separator)+1));
        /* save the session */
        this.saveSession();
    }
    
    /* opens a file chooser to browse for a session to open */
    private void loadSession(){
        /* browse for session */
        JFileChooser fileChooser=new JFileChooser(this.sessionPath);
        fileChooser.setDialogTitle("Load Session");
        /* add SessionFileFilter */
        fileChooser.setFileFilter(new SessionFileFilter());
        if(fileChooser.showOpenDialog(this.window)==JFileChooser.APPROVE_OPTION){
            /* set the path */
            this.loadSession(fileChooser.getSelectedFile().getPath());
        }
    }
    
    /* loads the session with the specified name */
    private void loadSession(String sessionPath){
        this.window.updateStatus(DebigulatorConstants.STATUS_LOADING);
        this.window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        /* set caption for window */
        this.window.setTitle(DebigulatorConstants.PROGRAM_TITLE+" - "+sessionPath.substring(sessionPath.lastIndexOf(File.separator)+1));
        /* set session path */
        this.sessionPath=sessionPath;
        /* open the session */
        try{
        	XStream xstream=new XStream(new DomDriver());
        	this.session=(DebigulatorSession)xstream.fromXML(new FileReader(this.sessionPath));
            //update the window
            this.window.setSyncToSourceDir(this.session.getSyncToSourceDirectory());
            this.window.setOutputDirectory(this.session.getThreadParameters().getOutputDirectory());
            this.window.setBrowseDirectory(this.session.getSourceDirectoryPath());
            this.window.setFileList(this.session.getThreadParameters().getFileList());
            this.window.setComboBoxValues(this.session.getThreadParameters().getArchiveType(),this.session.getThreadParameters().getAction());
            this.window.setTitle(DebigulatorConstants.PROGRAM_TITLE+" - "+this.sessionPath.substring(this.sessionPath.lastIndexOf(File.separator)+1));
        	this.window.updateStatus(DebigulatorConstants.STATUS_LOADED);
        } catch(Exception x){
        	errorHandler(x);
            this.window.updateStatus(DebigulatorConstants.ERROR_LOAD+" "+this.sessionPath);
        }
        this.window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    /* prompt the user to save the current session and save if they click yes */
    private boolean promptSave(){
   		String message="Save changes to "+this.sessionPath+"?";
   		int response=JOptionPane.showOptionDialog(this.window,message,"Save?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new String[]{"Yes","No","Cancel"},"Yes");
   		if(response==JOptionPane.CANCEL_OPTION){
   			return(PROMPT_SAVE_CANCEL);
   		} 
   		if(response==JOptionPane.YES_OPTION){
   			this.saveSession();    			
   		}
   		return(!PROMPT_SAVE_CANCEL);
    }
    
    /* creates a new session with default settings */
    private void newSession(){
    	//nag to save the previous session and check for cancel selection
    	if(this.sessionPath!=null){
        	if(this.promptSave()==PROMPT_SAVE_CANCEL){
        		return;
        	}
    	}
    	//create the new session
        this.setWorkingDirectory();
        this.session=new DebigulatorSession();
        this.session.setAutoSave(DebigulatorConstants.DEFAULT_AUTOSAVE);
        this.session.setSyncToSourceDirectory(DebigulatorConstants.DEFAULT_SYNC_TO_SOURCE_DIR);
        File sessionDirectory=new File(this.workingDirectory+File.separator+"sessions");
        if(!sessionDirectory.exists()){
            sessionDirectory.mkdir();
        }
        this.sessionPath=sessionDirectory+File.separator+DebigulatorConstants.DEFAULT_SESSION_NAME;
        //clear out the window
        this.window.setOutputDirectory(this.workingDirectory);
        this.window.setBrowseDirectory(this.workingDirectory);
        this.window.setSyncToSourceDir(this.session.getAutoSave());
        this.window.clearFileList();
        this.window.setComboBoxValues(0,0);
        this.window.setTitle(DebigulatorConstants.PROGRAM_TITLE+" - "+this.sessionPath.substring(this.sessionPath.lastIndexOf(File.separator)+1));
    }

    /* initializes batchCompressionThread with default values */
    private void createBatchCompressionThread(){
        this.batchCompressionThread=new BatchCompressionThread(this.session.getThreadParameters()){
			@SuppressWarnings("synthetic-access")
			public void updateTotalProgress(String status,int percentComplete){
                receiveTotalProgressUpdate(status,percentComplete);
            }
			@SuppressWarnings("synthetic-access")
			public void updateCurrentFileProgress(String status,int percentComplete){
                receiveCurrentFileProgressUpdate(status,percentComplete);
            }
        };
    }
    
    /* updates the total progress */
    private void receiveTotalProgressUpdate(String progress,int percentComplete){
        this.progressDialog.setTotalProgress(progress,percentComplete);
    }
    
    /* updates status and percent complete for the current file */
    private void receiveCurrentFileProgressUpdate(String progress,int percentComplete){
        this.progressDialog.setCurrentFileProgress(progress,percentComplete);
    }    
    
    /* loads the main window */
    @SuppressWarnings("serial")
	private void loadWindow(){
        this.window=new DebigulatorWindow(){
            @SuppressWarnings("synthetic-access")
			public void sendAction(String actionCommand){
                receiveAction(actionCommand);
            }
        };
        ImageIcon splashImage=new ImageIcon(this.getClass().getResource("/com/huguesjohnson/debigulator/images/splash_debigulator.gif"));
        SplashScreen splash=new SplashScreen(this.window,splashImage.getImage());
        this.window.initialize();
        /* show the window */
        this.window.setVisible(true);
        /* create reference to progress dialog */
        this.progressDialog=new DebigulatorProgressDialog(this.window);
        /* unload the splash screen */
        splash.dispose();
    }
    
    /* receives an action from the window */
    private void receiveAction(String actionCommand){
        if(actionCommand.equals(DebigulatorConstants.ACTION_NEWSESSION)){
            this.newSession();
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_OPENSESSION)){
            this.loadSession();
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_SAVESESSION)){
            this.saveSession();
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_SAVESESSIONAS)){
            this.saveSessionAs();
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_CREATEARCHIVES)){
            this.createArchives();
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_ADVANCEDSETTINGS)){
            this.showAdvancedSettingsDialog();
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_HELPINDEX)){
            String result=LaunchBrowser.launch("http://www.huguesjohnson.com/debigulator.html");
            //TODO - send result to status bar of window
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_HELPAPI)){
            String result=LaunchBrowser.launch("http://www.huguesjohnson.com/javadoc/debigulator/index.html");
            //TODO - send result to status bar of window
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_HELPABOUT)){
            JOptionPane.showMessageDialog(this.window,DebigulatorConstants.MESSAGE_ABOUT,"About",JOptionPane.INFORMATION_MESSAGE);
        } else if(actionCommand.equals(DebigulatorConstants.ACTION_EXIT)){
            /* check autoSave */
            if(this.session.getAutoSave()){
                this.saveSession();
            } else{
            	if(this.promptSave()==PROMPT_SAVE_CANCEL){
            		return;
            	}
            }
            System.exit(0);
        } else{
            //error
            System.err.println("Unknown command: "+actionCommand);
        }
    }
    
    /* display the advanced settings dialog */
    private void showAdvancedSettingsDialog(){
        DebigulatorAdvancedSettingsDialog advancedSettingsDialog=new DebigulatorAdvancedSettingsDialog(this.window,this.session);
        advancedSettingsDialog.setVisible(true);
        if(!advancedSettingsDialog.getCancelStatus()){
        	this.session=advancedSettingsDialog.getSessionData();
        }
    }
    
    /* display the progress dialog */
    private void showProgressDialog(){
        DebigulatorProgressDialog progressDialog=new DebigulatorProgressDialog(this.window);
        if(progressDialog.getCancelStatus()){
            //TODO anything else to do on cancel?
            System.out.println("progress dialog cancelled");
        }
        /* update the GUI here */
        this.window.refreshFiles();
    }
    
    private void updateSession(){
    	/* first update thread parameters */
    	BatchCompressionThreadParameters parameters=this.session.getThreadParameters();
        /* copy the list of files */
    	parameters.setInputFileList(this.window.getFileList());
        /* set the output directory */
    	parameters.setOutputDirectory(this.window.getOutputDirectory());
        /* set the archive type */
    	parameters.setArchiveType(this.window.getSelectedArchiveType());
        /* set the action after archiving */
    	parameters.setAction(this.window.getSelectedAction());
    	/* update session */
    	this.session.setThreadParameters(parameters);
    	this.session.setSourceDirectoryPath(this.window.getBrowseDirectory());
    	this.session.setSyncToSourceDirectory(this.window.getSyncToSourceDir());
    }
    
    /* starts the create archive process */
    private void createArchives(){
    	this.updateSession();
    	/* create & start the thread */
    	this.createBatchCompressionThread();
        this.batchCompressionThread.start();
        /* show the progress dialog */
        this.progressDialog.setModal(true);
        this.progressDialog.setVisible(true);
        /* did the user hit the cancel button? */
        if(this.progressDialog.getCancelStatus()){
            //TODO - any other cancel conditions that need to be handled?
            System.out.println("progress dialog cancelled");
        }
        /* update the UI here */
        this.window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.window.refreshFiles();
        this.window.updateStatus(DebigulatorConstants.STATUS_ARCHIVE_COMPLETE);
        this.window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /* log errors and show a popup dialog */
    private void errorHandler(Exception x){
    	//log the error
    	System.err.println(Calendar.getInstance().getTime().toString());
    	x.printStackTrace();
    	StringBuffer message=new StringBuffer();
    	message.append("An error done occur:\n");
    	message.append(x.getMessage());
    	if(this.errorLogPath!=null){
    		message.append("\n\nError stack trace logged to:\n");
    		message.append(this.errorLogPath);
    	}
    	//show an error dialog
    	JOptionPane.showMessageDialog(this.window,message,DebigulatorConstants.PROGRAM_TITLE,JOptionPane.ERROR_MESSAGE);
    }
    
    /** 
     * Returns a string representation of the object.
     * @return A string representation of the object.
     */
    public String toString(){
        StringBuffer tostring=new StringBuffer(super.toString());
        tostring.append("\nsessionPath=").append(this.sessionPath);
        tostring.append("\nworkingDirectory=").append(this.workingDirectory);
        tostring.append("\nsession=").append(this.session.toString());
        return(tostring.toString());
    }    
    
    //TODO - get rid of this when file choosers are fixed-up
    private static void checkVersion(){
    	String version=System.getProperty("java.version");
    	String vendor=System.getProperty("java.vendor");
    	if((version.indexOf("1.5")<0)||(vendor.indexOf("Sun")<0)){
        	StringBuffer message=new StringBuffer();
        	message.append("Debigulator is designed for the Sun Microsystems 1.5 JVM\n\n");
        	message.append("You are running: ");
        	message.append(vendor);
        	message.append(" ");
        	message.append(version);
        	message.append(" JVM\n\n");
        	message.append("You may experience some oddness with the user interface under this JVM.");
        	JOptionPane.showMessageDialog(null,message.toString(),"Warning",JOptionPane.WARNING_MESSAGE);
    	}
    }
    
    /** 
     * Entry point for the application.
     * @param arguments The command line arguments for the program.
     */
    public static void main(String[] arguments){
    	checkVersion();
    	//TODO - command line support to accept session path
        new Debigulator();
    }
}
