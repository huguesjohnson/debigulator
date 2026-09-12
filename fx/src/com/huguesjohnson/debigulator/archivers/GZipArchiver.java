/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.debigulator.archivers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipArchiver implements Archiver{

    @Override
    public boolean archive(String sourceFileName,String archiveFileName) throws Exception{
        boolean success=true;
        GZIPOutputStream gzout=null;
        FileInputStream fin=null;
        try{
            gzout=new GZIPOutputStream(new FileOutputStream(new File(archiveFileName)));
            File inputFile=new File(sourceFileName);
            byte buffer[]=new byte[BUFFER_LENGTH];
            fin=new FileInputStream(inputFile);
            int length;
            while((length=fin.read(buffer))>0){
                gzout.write(buffer,0,length);
            }
            success=true;
        }catch(Exception x){
            throw(x);
        }finally{
            try{if(gzout!=null){gzout.close();}}catch(Exception x){}
            try{if(fin!=null){fin.close();}}catch(Exception x){}
        } 
        return(success);
    }

    @Override
    public boolean extract(String archiveFileName,String destinationFileName) throws Exception{
        boolean success=false;
        GZIPInputStream gzin=null;
        FileOutputStream fout=null;
        try{
            gzin=new GZIPInputStream(new FileInputStream(new File(archiveFileName)));
            byte inBuffer[]=new byte[BUFFER_LENGTH];
            fout=new FileOutputStream(new File(destinationFileName));
            int length;
            while((length=gzin.read(inBuffer))>-1){
                fout.write(inBuffer,0,length);
            }
            success=true;
        }catch(Exception x){
            throw(x);
        }finally{
            try{if(fout!=null){fout.close();}}catch(Exception x){}
            try{if(gzin!=null){gzin.close();}}catch(Exception x){}
        } 
        return(success);
    }
    
}
