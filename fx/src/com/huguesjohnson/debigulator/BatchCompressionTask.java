/*
    Debigulator - A batch compression utility
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.debigulator;

import com.huguesjohnson.debigulator.archivers.Archiver;
import com.huguesjohnson.debigulator.archivers.GZipArchiver;
import com.huguesjohnson.debigulator.archivers.SevenZipArchiver;
import com.huguesjohnson.debigulator.archivers.ZipJarArchiver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.concurrent.Task;

public class BatchCompressionTask extends Task<Void>{
    private final BatchCompressionThreadParameters parameters;
    private boolean debug;
    private static final Logger logger=Logger.getLogger(BatchCompressionTask.class.getName());

    public BatchCompressionTask(BatchCompressionThreadParameters parameters){
        this.parameters=parameters;
    }
    
    @Override
    protected Void call() throws Exception{
        boolean doVerify=((this.parameters.getAction().equals(Enums.ActionAfterArchive.Verify))||(this.parameters.getAction().equals(Enums.ActionAfterArchive.VerifyDelete)));
        //setup the log file
        this.debug=this.parameters.getWriteDebugLog();
        if(this.debug){
            try{
                FileHandler fh=new FileHandler(this.getClass().getSimpleName()+"_debug.log", false);
                fh.setFormatter(new SimpleFormatter());
                logger.addHandler(fh);
                logger.setLevel(Level.ALL);
            }catch(SecurityException|IOException x){
                x.printStackTrace();
            }
        }
        String[] fileList=this.parameters.getFileList();
        int size=fileList.length;
        for(int index=0;index<size;index++){
            this.updateProgress(index,size);
            this.log("Compressing Files ("+(index+1)+"/"+size+")");
            String sourceFileName=fileList[index];
            this.log("fileList element #"+index+"="+sourceFileName);
            //build the output file name
            String outputFileName=buildOutputFileName(sourceFileName,parameters);
            int lastFileSeparatorPosition=sourceFileName.lastIndexOf(File.separator);
            if((lastFileSeparatorPosition>=0)&&(lastFileSeparatorPosition<sourceFileName.length())){
                lastFileSeparatorPosition+=File.separator.length();
            }
            String shortSourceFileName=new String(sourceFileName.substring(lastFileSeparatorPosition));
            //now let's create the archive
            boolean verified=false;
            try{ 
                //create archiver
                Archiver archiver=null;
                if(this.parameters.getArchiveType().equals(Enums.ArchiveType.Jar)){
                    archiver=new ZipJarArchiver(ZipJarArchiver.ZipOrJar.JAR);
                }else if(this.parameters.getArchiveType().equals(Enums.ArchiveType.Zip)){
                    archiver=new ZipJarArchiver(ZipJarArchiver.ZipOrJar.ZIP);
                }else if(this.parameters.getArchiveType().equals(Enums.ArchiveType.GZip)){
                    archiver=new GZipArchiver();
                }else if(this.parameters.getArchiveType().equals(Enums.ArchiveType.SevenZip)){
                    archiver=new SevenZipArchiver();
                }
                if(archiver==null){throw(new Exception("Unsupported archive type"));}
                //archive
                archiver.archive(sourceFileName,outputFileName);
                //verify
                if(doVerify){
                    String tempFileName=outputFileName+"_TEMP_";
                    if(archiver.extract(outputFileName,tempFileName)){
                        verified=this.compareFiles(new File(sourceFileName),new File(tempFileName));
                        if(!verified){
                            //TODO - log error
                        }
                        //delete the temp file
                        this.log("Deleting temp file: "+tempFileName);
                        boolean deleteSuccess=(new File(tempFileName)).delete();
                        //check success of delete
                        if(deleteSuccess){
                            this.log("  deleted temp file");
                        } else{ //throw an error?
                            this.log("  deletion of temp file failed");
                        }
                        this.log("Deleted temp file: "+tempFileName);
                    }else{
                        //TODO - log error
                    }
                }
                //delete
                boolean doDelete=false;
                if(this.parameters.getAction().equals(Enums.ActionAfterArchive.Delete)){
                    doDelete=true;
                }else if(this.parameters.getAction().equals(Enums.ActionAfterArchive.VerifyDelete)){
                    doDelete=verified;
                }
                if(doDelete){
                    //to do send files to recycle bin instead of totally deleting (if this is even possible)
                    this.log("Deleting "+shortSourceFileName);
                    boolean deleteSuccess=(new File(sourceFileName)).delete();
                    //check success of delete
                    if(deleteSuccess){
                        this.log("  deleted input file");
                    }else{ //throw an error?
                        this.log("  deletion of input file failed");
                    }
                    this.log("Deleted "+shortSourceFileName);
                }
             }catch(Exception x){
                x.printStackTrace();
                this.log(x.toString());
            }            
        }
        this.updateProgress(size,size);
        return(null);
    }

    private void log(String line){
        if(this.debug){
            logger.log(Level.INFO,line);
        }
    }    
    
    private String buildOutputFileName(String sourceFileName,BatchCompressionThreadParameters parameters){
        StringBuffer outputFileName=new StringBuffer();
        outputFileName.append(this.parameters.getOutputDirectory());
        String date=new String();
        if(this.parameters.getAppendDate()){
            date=(new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        }
        int lastFileSeparatorPosition=sourceFileName.lastIndexOf(File.separator);
        if((lastFileSeparatorPosition>=0)&&(lastFileSeparatorPosition<sourceFileName.length())){
            lastFileSeparatorPosition+=File.separator.length();
        }
        String shortSourceFileName=new String(sourceFileName.substring(lastFileSeparatorPosition));
        this.log("  shortSourceFileName="+shortSourceFileName);
        /* append date */
        if(this.parameters.getAppendDate()){
            outputFileName.append(date);
            outputFileName.append("_");
            outputFileName.append(shortSourceFileName);
        }else{
            outputFileName.append(shortSourceFileName);
        }
        this.log("  outputFileName after appendDate test="+outputFileName.toString());
        //find exisiting file extension
        int lastDotPosition=outputFileName.lastIndexOf(".");
        if((lastDotPosition>=0)&&(lastDotPosition<outputFileName.length())){
            //append original file extension
            if(this.parameters.getAppendExtension()){
                String extension=outputFileName.substring(lastDotPosition+1);
                outputFileName.delete(lastDotPosition,outputFileName.length());
                outputFileName.append("_");
                outputFileName.append(extension);
            }else{
                outputFileName.delete(lastDotPosition,outputFileName.length());
            }
        }
        this.log("  outputFileName after appendExtension test="+outputFileName);
        //append archive extension
        if(this.parameters.getArchiveType().equals(Enums.ArchiveType.Jar)){
            outputFileName.append(".jar");
        }else if(this.parameters.getArchiveType().equals(Enums.ArchiveType.Zip)){
            outputFileName.append(".zip");
        }else if(this.parameters.getArchiveType().equals(Enums.ArchiveType.GZip)){
            outputFileName.append(".gz");
        }else if(this.parameters.getArchiveType().equals(Enums.ArchiveType.SevenZip)){
            outputFileName.append(".7z");
        }
        //make sure the file name doesn't already exist
        int fileNamePad=0;
        String testName=outputFileName.toString();
        lastDotPosition=outputFileName.lastIndexOf(".");
        while((new File(testName)).exists()){
            testName=outputFileName.substring(0,lastDotPosition)+"["+fileNamePad+"]"+outputFileName.substring(lastDotPosition);
            fileNamePad++;
        }
        return(testName);
    }
    
    private boolean compareFiles(File originalFile,File compareFile){
        /* compare file lengths first */
        String shortName=originalFile.getName();
        long fileLength=originalFile.length();
        if(fileLength!=compareFile.length()){
            this.log("  (fileLength!=extractedFile.length())");
            return(false);
        }else{
            boolean verified=true;
            FileInputStream fInOriginal=null;
            FileInputStream fInCompare=null;
            try{
                fInOriginal=new FileInputStream(originalFile);
                fInCompare=new FileInputStream(compareFile);
                int originalByte=fInOriginal.read();
                int compareByte=fInCompare.read();
                int bytesRead=0;
                int lastPercentComplete=0;
                while((originalByte==compareByte)&&(originalByte!=-1)&&verified){
                    originalByte=fInOriginal.read();
                    compareByte=fInCompare.read();
                    verified=originalByte==compareByte;
                    bytesRead+=1;
                    /* update progress */
                    int percentComplete=(int)(((double)bytesRead/(double)fileLength)*100.0D);
                    if(percentComplete!=lastPercentComplete){
                        lastPercentComplete=percentComplete;
                    }
                }
                this.log("  verified after byte compare="+verified);    
            }catch(Exception x){
                this.log(x.getMessage());
            }finally{
                try{if(fInOriginal!=null){fInOriginal.close();}}catch(Exception x){this.log(x.getMessage());}
                try{if(fInCompare!=null){fInCompare.close();}}catch(Exception x){this.log(x.getMessage());}
            }
            return(verified);
        }
    }
    
}
