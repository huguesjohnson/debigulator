/*
LogFile.java - implementation of a simple log file mechanism 
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

package com.huguesjohnson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/** 
 * Extremely barebone implementation of a log file.
 * It's not really pretty because it suppresses errors but also incredibly simple to use.
 */
public class LogFile{
    private PrintStream logFile;

    /** 
     * Creates a new LogFile, dumps errors to the error console.
     * @param fileName Name & path of file to use. If the file directory doesn't exist it will be created.
     */
    public LogFile(String fileName){
        try{
            /* does the directory this file should reside in exist? */
            File targetDirectory=new File(fileName.substring(0,fileName.lastIndexOf(File.separator)));
            if(!targetDirectory.exists()){
                targetDirectory.mkdir();
            }
            /* initialize logFile for append */
            this.logFile=new PrintStream(new FileOutputStream(fileName,true));
        } catch(FileNotFoundException x){ 
            x.printStackTrace();
        }
    }
    
    /** 
     * Flushes buffer and closes the log.
     */
    public void close(){
        if(this.logFile!=null){
            this.log("----------------------------------------");
            /* force a flush to ensure everything prints before closing */
            this.logFile.flush();
            /* close the file */
            this.logFile.close();
        }
    }
    
    /** 
     * Appends a line to the log.
     * @param line The line to append.
     */
    public void log(String line){
        if(this.logFile!=null){
            this.logFile.println(line);
        }
    }
}