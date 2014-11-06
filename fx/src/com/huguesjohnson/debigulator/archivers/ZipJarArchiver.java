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
            int availableBytes=fin.available();
            int length;
            int bytesRead=0;
            int lastPercentComplete=-1;
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
