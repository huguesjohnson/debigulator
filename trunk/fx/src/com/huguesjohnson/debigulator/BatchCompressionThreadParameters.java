/*
    Debigulator - A batch compression utility
    Copyright (C) 2003-2014 Hugues Johnson

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.huguesjohnson.debigulator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class BatchCompressionThreadParameters implements Serializable{
    /* the list of files to archive */
    private ArrayList<String> fileList;
    /* the type of archive to write */
    private Enums.ArchiveType archiveType;
    /* the path to the destination directory */
    private String outputDirectory;
    /* action to perform on files after archiving */
    private Enums.ActionAfterArchive action;
    /* whether or not to append the date to archive names */
    private boolean appendDate;
    /* whether or not to append the file extension to archive names */
    private boolean appendExtension;
    /* whether or not to write a debug log */
    private boolean writeDebugLog;
    /* path to the temporary folder used for file validation */
    private String tempDirectory;

    /** 
     * Sets the list of files to archive.
     * @param sourceFileList The list of files to archive.
     */
	public void setInputFileList(String[] sourceFileList){
        int size=sourceFileList.length;
        this.fileList=new ArrayList<String>(size);
        for(int index=0;index<size;index++){
            this.fileList.add(sourceFileList[index]);
        }
    }    
    
    /** 
     * Returns the list of files to archive.
     * @return A string array of all files set to be archived, null if no files are in the list.
     */
    public String[] getFileList(){
        int size=this.fileList.size();
        if(size<=0){
            return(null);
        }
        return(this.fileList.toArray(new String[0]));
    }
    
    /** 
     * Sets the path to the destination directory.
     * @param outputDirectory The new path to the output directory.
     */
    public void setOutputDirectory(String outputDirectory){
        if(outputDirectory.endsWith(File.separator)){
            this.outputDirectory=outputDirectory;
        } else{
            this.outputDirectory=outputDirectory+File.separator;
        }
    }
    
    /** 
     * Returns the path to the destination directory.
     * @return The path to the destination directory.
     */
    public String getOutputDirectory(){
        return(this.outputDirectory);
    }

    /** 
     * Sets the path to the temporary directory.
     * @param tempDirectory The new path to the temporary directory.
     */
    public void setTempDirectory(String tempDirectory){
        if(tempDirectory.endsWith(File.separator)){
            this.tempDirectory=tempDirectory;
        } else{
            this.tempDirectory=tempDirectory+File.separator;
        }
    }
    
    /** 
     * Returns the path to the temporary directory.
     * @return path to the temporary directory.
     */
    public String getTempDirectory(){
        return(this.tempDirectory);
    }    
    
    /** 
     * Set the type of archives to create.
     * @param archiveType The type of archive to create.
     */
    public void setArchiveType(Enums.ArchiveType archiveType){
    	this.archiveType=archiveType;
    }

    /** 
     * Returns type of archives to create.
     * @return The type of archive to create.
     */
    public Enums.ArchiveType getArchiveType(){
        return(this.archiveType);
    }
    
    /** 
     * Adds a file to the list of files to archive.
     * @param inputFile The full path of the file to add.
     */
	public void addInputFile(String inputFile){
        this.fileList.add(inputFile);
    }
    
    /** 
     * Removes a file from the list of files to archive.
     * @param removeFile The full path of the file to remove.
     */
    public void removeInputFile(String removeFile){
        this.fileList.remove(removeFile);
    }
    
    /** 
     * Clears the list of files to archive.
     */
    public void clearFileList(){
        this.fileList=new ArrayList<String>();
    }
    
    /** 
     * Sets the action to perform on files after archiving.
     * @param action The action to perform.
     */
    public void setAction(Enums.ActionAfterArchive action){
    	this.action=action;
    }

    /**
     *  Returns the action to perform on files after archiving.
     * @return The action to perform.
     */ 
    public Enums.ActionAfterArchive getAction(){
        return(this.action);
    }
    
    /** 
     * Sets whether or not to append the date to output file names. For example filename.exe->2003-07-25_filename.zip.
     * @param appendDate Set to true to append date to output file names.
     */
    public void setAppendDate(boolean appendDate){
        this.appendDate=appendDate;
    }

    /** 
     * Return whether or not to append the date to output file names.
     * @return true If dates are appended to output file names.
     */
    public boolean getAppendDate(){
        return(this.appendDate);
    }

    /** 
     * Sets whether or not to append the extension to output file names. For example filename.exe->filename_exe.zip.
     * @param appendExtension Set to true to append extension to output file names.
     */
    public void setAppendExtension(boolean appendExtension){
        this.appendExtension=appendExtension;
    }
    
    /** 
     * Return whether or not to append the extension to output file names.
     * @return true If extension is appened to output file names.
     */
    public boolean getAppendExtension(){
        return(this.appendExtension);
    }
    
    /** 
     * Set whether or not to write a debug log.
     * @param writeDebugLog Set to true if a debug log should be created.
     */
    public void setWriteDebugLog(boolean writeDebugLog){
    	this.writeDebugLog=writeDebugLog;        
    }
    
    /** 
     * Returns whether or not to write a debug log.
     * @return True if a debug log should be created.
     */
    public boolean getWriteDebugLog(){
        return(this.writeDebugLog);
    }
    
    /** 
     * Returns a string representation of the object.
     * @return A string representation of the object.
     */
    public String toString(){
        StringBuffer tostring=new StringBuffer(super.toString());
        tostring.append("\narchiveType=").append(this.archiveType.toString());
        tostring.append("\naction=").append(this.action.toString());
        tostring.append("\nappendDate=").append(this.appendDate);
        tostring.append("\nappendExtension=").append(this.appendExtension);
        tostring.append("\nwriteDebugLog=").append(this.writeDebugLog);
        tostring.append("\noutputDirectory=").append(this.outputDirectory);
        tostring.append("\ntempDirectory=").append(this.tempDirectory);
        int size=this.fileList.size();
        tostring.append("\nfileList.size()=").append(size);
        for(int index=0;index<size;index++){
            tostring.append("\n  fileList[").append(index).append("]=").append(this.fileList.get(index).toString());
        }
        return(tostring.toString());
    }
}