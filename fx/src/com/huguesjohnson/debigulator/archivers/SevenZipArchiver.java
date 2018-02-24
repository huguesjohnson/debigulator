/*
    Debigulator - A batch compression utility

    7-Zip compression & decompression is derived from LzmaAlone.java (7-Zip.org/sdk.html)

Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.debigulator.archivers;

import SevenZip.Compression.LZMA.Decoder;
import SevenZip.Compression.LZMA.Encoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SevenZipArchiver implements Archiver{

    @Override
    public boolean archive(String sourceFileName,String archiveFileName) throws Exception{
        boolean success=false;
        File inputFile=new File(sourceFileName);
        FileInputStream fin=null;
        FileOutputStream szout=null;
        try{
            fin=new FileInputStream(new File(sourceFileName));
            szout=new FileOutputStream(new File(archiveFileName));
            Encoder encoder=new Encoder();
            encoder.WriteCoderProperties(szout);
            long fileSize;
            boolean endOfStream=false;
            if(endOfStream){
                fileSize=-1;
            }else{
                fileSize=inputFile.length();
            }
            for(int i=0;i<8;i++){
                szout.write((int)(fileSize >>>(8*i))&0xFF);
            }
            encoder.Code(fin,szout,-1,-1,null);       
            szout.flush();
            success=true;                   
        }catch(Exception x){
            throw(x);
        }finally{
            try{if(szout!=null){szout.close();}}catch(Exception x){}
            try{if(fin!=null){fin.close();}}catch(Exception x){} 
        }
        return(success);
    }

    @Override
    public boolean extract(String archiveFileName,String destinationFileName) throws Exception{
        boolean success=false;
        FileInputStream szin=null;
        FileOutputStream fout=null;
        try{
            int propertiesSize=5;
            byte[] properties=new byte[propertiesSize];
            szin=new FileInputStream(new File(archiveFileName));
            fout=new FileOutputStream(new File(destinationFileName));
            if(szin.read(properties,0,propertiesSize)!=propertiesSize){
                throw(new Exception("input .lzma file is too short"));
            }
            Decoder decoder=new Decoder();
            if(!decoder.SetDecoderProperties(properties)){
                throw(new Exception("Incorrect stream properties"));
            }
            long outSize=0;
                for(int i=0;i<8;i++){
                    int v=szin.read();
                    if(v<0){
                        throw(new Exception("Can't read stream size"));
                    }
                    outSize|=((long)v)<<(8*i);
                }
                if(!decoder.Code(szin,fout,outSize)){
                    throw new Exception("Error in data stream");
                }
            success=true;
        }catch(Exception x){
            throw(x);
        }finally{
            try{if(fout!=null){fout.close();}}catch(Exception x){}
            try{if(szin!=null){szin.close();}}catch(Exception x){}
        }
        return(success);
    }
    
}
