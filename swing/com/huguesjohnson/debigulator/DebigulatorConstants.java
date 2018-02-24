/*
Debigulator - A batch compression utility
DebigulatorConstants.java - Constants for the application
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.debigulator;

/** 
 * Contains constants used by the Debigulator application.
 */
public abstract class DebigulatorConstants{
/* pop-up dialog messages */
	/** 
	 * Message that displays on the about menu.<p>
	 * <code>MESSAGE_ABOUT="Debigulator, a batch compression program\n(c) 2003-2018 Hugues Johnson\nhttp://www.huguesjohnson.com/";</code>
	 */
	public final static String MESSAGE_ABOUT="Debigulator, a batch compression program\n(c) 2003-2018 Hugues Johnson\nhttp://www.huguesjohnson.com/";
    
/* status messages */
	/**
	 * Status mesage indicating session is being saved.
	 * <code>public final static String STATUS_SAVING="Saving session..";</code>
	 */
    public final static String STATUS_SAVING="Saving session..";
	/**
	 * Status mesage indicating session has been saved.
	 * <code>public final static String STATUS_SAVED="Saved session";</code>
	 */
    public final static String STATUS_SAVED="Saved session";
	/**
	 * Status mesage indicating session is being loaded.
	 * <code>public final static String STATUS_LOADING="Loading session..";</code>
	 */
    public final static String STATUS_LOADING="Loading session..";
	/**
	 * Status mesage indicating session has been loaded.
	 * <code>public final static String STATUS_LOADED="Loaded session";</code>
	 */
    public final static String STATUS_LOADED="Loaded session";
	/**
	 * Status mesage indicating archiving is complete.
	 * <code>public final static String STATUS_ARCHIVE_COMPLETE="Archive operation completed";</code>
	 */
    public final static String STATUS_ARCHIVE_COMPLETE="Archive operation completed";
    
/* error messages */
	/**
	 * Message indicating error has occured saving the session.
	 * <code>public final static String ERROR_SAVE="Error saving session";</code>
	 */
    public final static String ERROR_SAVE="Error saving session";
	/**
	 * Message indicating error has occured loading the session.
	 * <code>public final static String ERROR_LOAD="Error loading session";</code>
	 */
    public final static String ERROR_LOAD="Error loading session";
    
/* captions */
	/**
	 * Title for the main program frame.
	 * <code>public final static String PROGRAM_TITLE="Debigulator 1.1";</code>
	 */
    public final static String PROGRAM_TITLE="Debigulator 1.1";
    
/* component sizes */
	/**
	 * Default height of the output directory component.
	 * <code>public final static int OUTPUT_DIRECTORY_BROWSER_HEIGHT=140;</code>
	 */
    public final static int OUTPUT_DIRECTORY_BROWSER_HEIGHT=140;
	/**
	 * Default width of the output directory component.
	 * <code>public final static int OUTPUT_DIRECTORY_BROWSER_WIDTH=400;</code>
	 */
    public final static int OUTPUT_DIRECTORY_BROWSER_WIDTH=400;
	/**
	 * Default height of the file browser component.
	 * <code>FILE_BROWSER_HEIGHT=200;</code>
	 */
    public final static int FILE_BROWSER_HEIGHT=200;
	/**
	 * Default width of the file browser component.
	 * <code>public final static int FILE_BROWSER_WIDTH=240;</code>
	 */
    public final static int FILE_BROWSER_WIDTH=240;
    
/* action commands */
	/**
	 * Action to create a new session.
	 * <code>public final static String ACTION_NEWSESSION="New Session";</code>
	 */
    public final static String ACTION_NEWSESSION="New Session";
	/**
	 *
	 * <code>public final static String ACTION_OPENSESSION="Open Session..";</code>
	 */
    public final static String ACTION_OPENSESSION="Open Session..";
	/**
	 * Action to save the current session.
	 * <code>public final static String ACTION_SAVESESSION="Save Session";</code>
	 */
    public final static String ACTION_SAVESESSION="Save Session";
	/**
	 * Action to save the session as.
	 * <code>public final static String ACTION_SAVESESSIONAS="Save Session As..";</code>
	 */
    public final static String ACTION_SAVESESSIONAS="Save Session As..";
	/**
	 * Action to begin the archive operation.
	 * <code>public final static String ACTION_CREATEARCHIVES="Create Archives..";</code>
	 */
    public final static String ACTION_CREATEARCHIVES="Create Archives..";
	/**
	 * Action to display the advanced settings dialog.
	 * <code>public final static String ACTION_ADVANCEDSETTINGS="Advanced Settings..";</code>
	 */
    public final static String ACTION_ADVANCEDSETTINGS="Advanced Settings..";
	/**
	 * Action to exit the application.
	 * <code>public final static String ACTION_EXIT="Exit";</code>
	 */
    public final static String ACTION_EXIT="Exit";
	/**
	 * Action to open the help index.
	 * <code>public final static String ACTION_HELPINDEX="Index..";</code>
	 */
    public final static String ACTION_HELPINDEX="Index..";
	/**
	 * Action to open the API documentation.
	 * <code>public final static String ACTION_HELPAPI="API Reference..";</code>
	 */
    public final static String ACTION_HELPAPI="API Reference..";
	/**
	 * Action to display the about dialog.
	 * <code>public final static String ACTION_HELPABOUT="About..";</code>
	 */
    public final static String ACTION_HELPABOUT="About..";
	/**
	 * Action to add selected files to the list to archive.
	 * <code>public final static String ACTION_ADDSELECTED="Add Selected";</code>
	 */
    public final static String ACTION_ADDSELECTED="Add Selected";
	/**
	 * Action to add all files to the list to archive.
	 * <code>public final static String ACTION_ADDALL="Add All";</code>
	 */
    public final static String ACTION_ADDALL="Add All";
	/**
	 * Action to remove selected files from the list to archive.
	 * <code>public final static String ACTION_REMOVESELECTED="Remove Selected";</code>
	 */
    public final static String ACTION_REMOVESELECTED="Remove Selected";
	/**
	 * Action to remove all files from the list to archive.
	 * <code>public final static String ACTION_REMOVEALL="Remove All";</code>
	 */
    public final static String ACTION_REMOVEALL="Remove All";
	/**
	 * Action when OK button is pressed on a dialog.
	 * <code>public final static String ACTION_OK="OK";</code>
	 */
    public final static String ACTION_OK="OK";
	/**
	 * Action when cancel button is pressed on a dialog.
	 * <code>public final static String ACTION_CANCEL="Cancel";</code>
	 */
    public final static String ACTION_CANCEL="Cancel";
//	/**
//	 * Action when cancel button is pressed on a dialog.
//	 * <code>public final static String ACTION_CHANGESTYLE="Change Style";</code>
//	 */
//    public final static String ACTION_CHANGESTYLE="Change Style";
    
/* default settings */
	/**
	 * Default session name.
	 * <code>public final static String DEFAULT_SESSION_NAME="New.session";</code>
	 */
    public final static String DEFAULT_SESSION_NAME="New.session";
	/**
	 * Default autosave value.
	 * <code>public final static boolean DEFAULT_AUTOSAVE=true;</code>
	 */
    public final static boolean DEFAULT_AUTOSAVE=true;
	/**
	 * Default syncToSourceDir value.
	 * <code>public final static boolean DEFAULT_SYNC_TO_SOURCE_DIR=true;</code>
	 */
    public final static boolean DEFAULT_SYNC_TO_SOURCE_DIR=true;
}
