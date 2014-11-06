/*
Debigulator - A batch compression utility
DebigulatorSession.java - Used to store program settings.
Copyright  (C) 2007 Hugues Johnson
 
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

import java.io.Serializable;
import com.huguesjohnson.BatchCompressionThreadParameters;

/**
 * DebigulatorSession is used to store session information for the Debigulator application.
 * @author Hugues Johnson
 */
public class DebigulatorSession implements Serializable{
	/* Determines if a de-serialized file is compatible with this class. See <a href="http://java.sun.com/j2se/1.5.0/docs/guide/serialization/spec/class.html">Java Object Serialization Specifications</a>. */
	private static final long serialVersionUID=3288020447076429842L;
	/* the archive parameters */
	private BatchCompressionThreadParameters threadParameters;
    /* whether or not to save the current session on exit */
    private boolean autoSave;
    /* whether or not to syncronize the output directory to the source directory */
    private boolean syncToSourceDirectory;
    /* path to the current source directory */
    private String sourceDirectoryPath;
    
    /**
     * Default constructor.
     */
    public DebigulatorSession(){
    	this.threadParameters=new BatchCompressionThreadParameters();
    }
    
	/** 
	 * Returns whether or not to save the current session on exit.
	 * @return Whether or not to save the current session on exit.
	 */
	public boolean getAutoSave(){
		return(this.autoSave);
	}
	
	/**
	 * Sets whether or not to save the current session on exit.
	 * @param autoSave Whether or not to save the current session on exit.
	 */
	public void setAutoSave(boolean autoSave){
		this.autoSave=autoSave;
	}
	
	/**
	 * Sets the archive parameters.
	 * @return The archive parameters.
	 */
	public BatchCompressionThreadParameters getThreadParameters(){
		return(this.threadParameters);
	}
	
	/**
	 * Sets the archive parameters.
	 * @param threadParameters The new parameters.
	 */
	public void setThreadParameters(BatchCompressionThreadParameters threadParameters){
		this.threadParameters=threadParameters;
	}
	
	/**
	 * Returns whether or not to syncronize the output directory to the source directory.
	 * @return Returns whether or not to syncronize the output directory to the source directory.
	 */
	public boolean getSyncToSourceDirectory(){
		return(this.syncToSourceDirectory);
	}

	/**
	 * Set whether or not to syncronize the output directory to the source directory.
	 * @param syncToSourceDirectory Whether or not to syncronize the output directory to the source directory.
	 */
	public void setSyncToSourceDirectory(boolean syncToSourceDirectory){
		this.syncToSourceDirectory=syncToSourceDirectory;
	}

	/**
	 * Returns the path to the source directory.
	 * @return Returns the path to the source directory.
	 */
	public String getSourceDirectoryPath(){
		return(this.sourceDirectoryPath);
	}

	/**
	 * Sets the path to the source directory.
	 * @param sourceDirectoryPath New path to the source directory.
	 */
	public void setSourceDirectoryPath(String sourceDirectoryPath){
		this.sourceDirectoryPath=sourceDirectoryPath;
	}
}