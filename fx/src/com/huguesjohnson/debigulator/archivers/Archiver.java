/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.debigulator.archivers;

public interface Archiver{
    final static int BUFFER_LENGTH=1024;    
    
    public boolean archive(String sourceFileName,String archiveFileName) throws Exception;
    
    public boolean extract(String archiveFileName,String destinationFileName) throws Exception;
    
}
