/*
LogFile.java - implementation of a simple log file mechanism 
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
