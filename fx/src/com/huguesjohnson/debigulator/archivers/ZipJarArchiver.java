/*
Debigulator - A batch compression utility
Copyright (C) 2003-2020 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

package com.huguesjohnson.debigulator.archivers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipJarArchiver implements Archiver{
    public enum ZipOrJar{ZIP,JAR};
    private final ZipOrJar archiveType;

    public ZipJarArchiver(ZipOrJar archiveType){
        this.archiveType=archiveType;
    }

    @Override
    public boolean archive(String sourceFileName,String archiveFileName) throws Exception{
        boolean success=false;
        ZipOutputStream zout=null;
        FileInputStream fin=null;
        try{
            if(this.archiveType==ZipOrJar.JAR){
                zout=new JarOutputStream(new FileOutputStream(new File(archiveFileName)));
            }else{
                zout=new ZipOutputStream(new FileOutputStream(new File(archiveFileName)));
            }
            zout.setMethod(ZipOutputStream.DEFLATED);
            File inputFile=new File(sourceFileName);
            byte buffer[]=new byte[BUFFER_LENGTH];
            /* generate CRC */
            CRC32 crc=new CRC32();
            fin=new FileInputStream(inputFile);
            int length;
            while((length=fin.read(buffer))>-1){
                crc.update(buffer,0,length);
            }
            fin.close();
            //create zip entry
            ZipEntry entry=new ZipEntry(sourceFileName.substring(sourceFileName.lastIndexOf(File.separator)+File.separator.length()));
            entry.setSize(inputFile.length());
            entry.setTime(inputFile.lastModified());
            entry.setCrc(crc.getValue());
            zout.putNextEntry(entry);
            fin=new FileInputStream(inputFile);
            /* write entry to zip file */
            while((length=fin.read(buffer))>-1){
                zout.write(buffer,0,length);
            }
            success=true;
        }catch(Exception x){
            throw(x);
        }finally{
            try{if(zout!=null){zout.closeEntry();zout.close();}}catch(Exception x){}
            try{if(fin!=null){fin.close();}}catch(Exception x){}
        } 
        return(success);
    }

    @Override
    public boolean extract(String archiveFileName,String destinationFileName) throws Exception{
        boolean success=false;
        ZipInputStream zin=null;
        FileOutputStream fout=null;
        try{
            if(this.archiveType==ZipOrJar.JAR){
                zin=new JarInputStream(new FileInputStream(new File(archiveFileName)));
            }else{
                zin=new ZipInputStream(new FileInputStream(new File(archiveFileName)));
            }
            ZipEntry entry=zin.getNextEntry();
            if(entry==null){
                throw(new Exception("No entry in archive "+archiveFileName));
            }else{
                byte inBuffer[]=new byte[BUFFER_LENGTH];
                int inLength;
                fout=new FileOutputStream(new File(destinationFileName));
                while((inLength=zin.read(inBuffer))>-1){
                    fout.write(inBuffer,0,inLength);
                }
            }
            success=true;
        }catch(Exception x){
            throw(x);
        }finally{
            try{if(fout!=null){fout.close();}}catch(Exception x){}
            try{if(zin!=null){zin.closeEntry();zin.close();}}catch(Exception x){}
        } 
        return(success);
    }
    
}
