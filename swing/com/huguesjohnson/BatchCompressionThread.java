/*
BatchCompressionThread.java - Compresses a list of files into individual archives
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/** 
 * BatchCompressionThread compresses a list of files into individual archives.
 * @author Hugues Johnson
 */
public abstract class BatchCompressionThread implements Runnable{
    /* parameters */
	private BatchCompressionThreadParameters parameters;
	/* thread for this class */
    private Thread myThread;
    /* whether or not this is running */
    private boolean running;
    /* log file */
    private LogFile logFile;
    /* default buffer length to use */
    private final static int BUFFER_LENGTH=1024;    
    
    /** 
     * Creates a new instance of BatchCompressionThread.
     * @param parameters The parameters for the thread.
     */
	public BatchCompressionThread(BatchCompressionThreadParameters parameters){
        this.parameters=parameters;
    }

    /** 
     * Begins thread execution.
     */
    public void start(){
        if(this.myThread==null||(!this.running)){
        	this.myThread=new Thread(this);
        }
        this.myThread.start();
    }

    /** 
     * Starts the batch compression thread, archives each file in the file list and performs the action specified in the parameters.
     * <p>
     * Override <code>updateTotalProgress</code> & <code>updateCurrentFileProgress</code> to monitor the progress of the thread.
     */
    public void run(){
        this.running=true;
        this.updateTotalProgress("Starting..",0);
        //setup the log file
        if(this.parameters.getWriteDebugLog()){
        	String logFilePath=System.getProperty("user.dir");
        	if(!logFilePath.endsWith(File.separator)){
        		logFilePath=logFilePath+File.separator;
        	}
        	this.logFile=new LogFile(logFilePath);
        }
        String date=new String();
        if(this.parameters.getAppendDate()){
            date=new String((new SimpleDateFormat("yyyy-MM-dd")).format(new Date()));
        }
        String[] fileList=this.parameters.getFileList();
        int size=fileList.length;
        for(int index=0;index<size;index++){
            /* update total progress */
        	String statusMessage="Compressing Files ("+(index+1)+"/"+size+")";
            this.updateTotalProgress(statusMessage,(index*(100/size)));
            String sourceFileName=fileList[index];
            this.log("fileList element #"+index+"="+sourceFileName);
            String outputFileName=this.parameters.getOutputDirectory();
            /* build the output file name */
            int lastFileSeparatorPosition=sourceFileName.lastIndexOf(File.separator);
            if((lastFileSeparatorPosition>=0)&&(lastFileSeparatorPosition<sourceFileName.length())){
                lastFileSeparatorPosition++;
            }
            String shortSourceFileName=new String(sourceFileName.substring(lastFileSeparatorPosition));
            this.log("  shortSourceFileName="+shortSourceFileName);
            /* append date */
            if(this.parameters.getAppendDate()){
                outputFileName+=date+"_"+sourceFileName.substring(lastFileSeparatorPosition);
            } else{
                outputFileName+=sourceFileName.substring(lastFileSeparatorPosition);
            }
            this.log("  outputFileName after appendDate test="+outputFileName);
            /* find exisiting file extension */
            int lastDotPosition=outputFileName.lastIndexOf(".");
            if((lastDotPosition>=0)&&(lastDotPosition<outputFileName.length())){
                /* append original file extension */
                if(this.parameters.getAppendExtension()){
                    String extension=outputFileName.substring(lastDotPosition+1);
                    outputFileName=outputFileName.substring(0,lastDotPosition)+"_"+extension;
                } else{
                    outputFileName=outputFileName.substring(0,lastDotPosition);
                }
            }
            this.log("  outputFileName after appendExtension test="+outputFileName);
            //append archive extension
            if(this.parameters.getArchiveType().equals(BatchCompressionThreadParameters.ArchiveTypes.jar)){
            	outputFileName=outputFileName+".jar";
            } else{
            	outputFileName=outputFileName+".zip";
            }
            //make sure the file name doesn't already exist
            int fileNamePad=0;
            String testName=outputFileName;
            lastDotPosition=outputFileName.lastIndexOf(".");
            while((new File(testName)).exists()){
                testName=outputFileName.substring(0,lastDotPosition)+"["+fileNamePad+"]"+outputFileName.substring(lastDotPosition);
            	fileNamePad++;
            }
            outputFileName=testName;
            //now let's create the archive
            try{
                ZipOutputStream zout;
                if(this.parameters.getArchiveType().equals(BatchCompressionThreadParameters.ArchiveTypes.jar)){
                	zout=new JarOutputStream(new FileOutputStream(new File(outputFileName)));
                } else{
                	zout=new ZipOutputStream(new FileOutputStream(new File(outputFileName)));
                }
                this.log("  opened output stream");
                zout.setMethod(ZipOutputStream.DEFLATED);
                File inputFile=new File(sourceFileName);
                byte buffer[]=new byte[BUFFER_LENGTH];
                /* generate CRC */
                CRC32 crc=new CRC32();
                FileInputStream fin=new FileInputStream(inputFile);
                this.log("  opened input stream to generate CRC");
                int availableBytes=fin.available();
                int length;
                int bytesRead=0;
                int lastPercentComplete=-1;
                while((length=fin.read(buffer))>-1){
                    crc.update(buffer,0,length);
                    bytesRead+=length;
                    /* update progress */
                    int percentComplete=(int)(((double)bytesRead/(double)availableBytes)*100.0D);
                    if(percentComplete!=lastPercentComplete){
                        lastPercentComplete=percentComplete;
                        this.updateCurrentFileProgress("Generating CRC for "+shortSourceFileName+"..",percentComplete);
                    }
                }
                fin.close();
                this.log("  closed input stream to generate CRC");
                /* create zip entry */
                ZipEntry entry=new ZipEntry(shortSourceFileName);
                entry.setSize(inputFile.length());
                entry.setTime(inputFile.lastModified());
                entry.setCrc(crc.getValue());
                zout.putNextEntry(entry);
                fin=new FileInputStream(inputFile);
                this.log("  opened input stream to read input file");
                bytesRead=0;
                /* write entry to zip file */
                while((length=fin.read(buffer))>-1){
                    zout.write(buffer,0,length);
                    bytesRead+=length;
                    /* update progress */
                    int percentComplete=(int)(((double)bytesRead/(double)availableBytes)*100.0D);
                    if(percentComplete!=lastPercentComplete){
                        lastPercentComplete=percentComplete;
                        this.updateCurrentFileProgress("Compressing "+shortSourceFileName+"..",percentComplete);
                    }
                }
                fin.close();
                this.log("  closed input stream to read input file");
                zout.closeEntry();
                zout.close();
                this.log("  closed output stream");
                /* process exit actions */
                boolean verified=true;
                if((this.parameters.getAction().equals(BatchCompressionThreadParameters.ArchiveCompleteActions.verify))||(this.parameters.getAction().equals(BatchCompressionThreadParameters.ArchiveCompleteActions.verifyAndDelete))){
                    String tempFile=this.parameters.getTempDirectory()+shortSourceFileName;
                    /* extract the file from the archive */
                    ZipInputStream zin;
                    if(this.parameters.getArchiveType().equals(BatchCompressionThreadParameters.ArchiveTypes.jar)){
                    	zin=new JarInputStream(new FileInputStream(new File(outputFileName)));
                    } else{
                    	zin=new ZipInputStream(new FileInputStream(new File(outputFileName)));
                    }
                    ZipEntry inEntry=zin.getNextEntry();
                    if(entry==null){
                        /* no entry in archive */
                        verified=false; //throw an error?
                    } else{
                        byte inBuffer[]=new byte[BUFFER_LENGTH];
                        long uncompressedSize=inEntry.getSize();
                        int inLength;
                        int bytesWritten=0;
                        lastPercentComplete=-1;
                        /* write file */
                        FileOutputStream fTempOut=new FileOutputStream(new File(tempFile));
                        while((inLength=zin.read(inBuffer))>-1){
                            fTempOut.write(inBuffer,0,inLength);
                            bytesWritten+=length;
                            /* update progress */
                            int percentComplete=(int)(((double)bytesWritten/(double)uncompressedSize)*100.0D);
                            if(percentComplete!=lastPercentComplete){
                                lastPercentComplete=percentComplete;
                                this.updateCurrentFileProgress("Extracting "+entry.getName()+" for validation..",percentComplete);
                            }
                        }
                        zin.closeEntry();
                        fTempOut.close();
                    }
                    /* are the files the same */
                    File originalFile=new File(sourceFileName);
                    File extractedFile=new File(tempFile);
                    /* compare file lengths first */
                    long fileLength=originalFile.length();
                    if(fileLength!=extractedFile.length()){
                        this.log("  (fileLength!=extractedFile.length())");
                        verified=false; //throw an error?
                    } else{
                        /* binary compare the files */
                        FileInputStream fInOriginal=new FileInputStream(originalFile);
                        FileInputStream fInExtracted=new FileInputStream(extractedFile);
                        int originalByte=fInOriginal.read();
                        int extractedByte=fInExtracted.read();
                        bytesRead=0;
                        lastPercentComplete=0;
                        while((originalByte==extractedByte)&&(originalByte!=-1)){
                            originalByte=fInOriginal.read();
                            extractedByte=fInExtracted.read();
                            bytesRead+=1;
                            /* update progress */
                            int percentComplete=(int)(((double)bytesRead/(double)fileLength)*100.0D);
                            if(percentComplete!=lastPercentComplete){
                                lastPercentComplete=percentComplete;
                                this.updateCurrentFileProgress("Comparing files..",percentComplete);
                            }
                        }
                        fInOriginal.close();
                        fInExtracted.close();
                        /* if the end of the original file was reached then the two files are equal */
                        verified=(originalByte==-1);
                        this.log("  verified after byte compare="+verified);
                    }
                    /* delete the temp file */
                    this.updateCurrentFileProgress("Deleting temp file..",0);
                    boolean deleteSuccess=(new File(tempFile)).delete();
                    /* check success of delete */
                    if(deleteSuccess){
                        this.log("  deleted temp file");
                    } else{ //throw an error?
                        this.log("  deletion of temp file failed");
                    }
                    this.updateCurrentFileProgress("Deleted temp file",100);
                }
                if((this.parameters.getAction().equals(BatchCompressionThreadParameters.ArchiveCompleteActions.delete))||(this.parameters.getAction().equals(BatchCompressionThreadParameters.ArchiveCompleteActions.verifyAndDelete))){
                    if(verified){
                    	//to do send files to recycle bin instead of totally deleting (if this is even possible)
                        this.updateCurrentFileProgress("Deleting "+shortSourceFileName+"..",0);
                        boolean deleteSuccess=(new File(sourceFileName)).delete();
                        /* check success of delete */
                        if(deleteSuccess){
                            this.log("  deleted input file");
                        } else{ //throw an error?
                            this.log("  deletion of input file failed");
                        }
                        this.updateCurrentFileProgress("Deleted "+shortSourceFileName+"..",100);
                    }
                }
             } catch(Exception x){
                x.printStackTrace();
                this.log(x.toString());
            }            
        }
        this.updateTotalProgress("Done",100);
        this.running=false;
    }
    

    /** 
     * Writes a line to the log file.
     * @param line The line to log.
     */
    private void log(String line){
        if(this.parameters.getWriteDebugLog()){
            this.logFile.log(line);
        }
    }    
    
    /** 
     * Updates status and percent complete for the overall progress.
     * Override this when implementing this class to receive status update.
     * @param status a brief description.
     * @param percentComplete The percent complete for the current operation.
     */
    public abstract void updateTotalProgress(String status,int percentComplete);    

    /** 
     * Updates status and percent complete for the current file.
     * Override this when implementing this class to receive status update.
     * @param status A brief description.
     * @param percentComplete The percent complete for the current operation.
     */
    public abstract void updateCurrentFileProgress(String status,int percentComplete);
}
