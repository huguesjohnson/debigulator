/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.debigulator;

import java.io.Serializable;
import com.huguesjohnson.BatchCompressionThreadParameters;

/**
 * DebigulatorSession is used to store session information for the Debigulator application.
 * @author Hugues Johnson
 */
public class DebigulatorSession{
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
