/*
    Debigulator - A batch compression utility
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.debigulator;

import java.io.Serializable;

/**
 * DebigulatorSession is used to store session information for the Debigulator application.
 * @author Hugues Johnson
 */
public class DebigulatorSession implements Serializable{
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
