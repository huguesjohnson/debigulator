/*
NonArchivedFileFilter.java - Filter to only accept files that are not an archive or part of an archive
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

package com.huguesjohnson.fileFilter;

import java.io.File;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.filechooser.FileFilter;

/** NonArchivedFileFilter Filter to only accept files that are not an archive or part of an archive
 * @author Hugues Johnson
 */
public class NonArchivedFileFilter extends FileFilter{
    /** known extensions of archive files */
    private final static String[] ARCHIVE_EXTENSIONS={".zip",".jar",".ace",".rar",".par",".sit",".tar",".gz",".tgz",".lzh",".lha",".arj",".cab",".arc"};
    /** known patterns of archive files.. yeah, this could probably be with one regular expression but I'm not good at writing them */
    private final static String[] ARCHIVE_PATTERNS={".z[0-9][0-9]",".c[0-9][0-9]",".r[0-9][0-9]",".p[0-9][0-9]"};
    
    /** Creates a new instance of NonArchivedFileFilter
     */
    public NonArchivedFileFilter(){
        super();
    }
    
    /** accept tests if a file has an extension of an known archive type.
     * @param file file to check
     * @return false if the extension of a file matches a known archive type
     */
    public boolean accept(File file){
        boolean reject=false;
        if(file.isFile()){
            String fileName=file.getName().toLowerCase();
            int indexOf=fileName.lastIndexOf(".");
            if(indexOf>=0){
                String extension=fileName.substring(indexOf);
                if(extension.length()>0){
                    /* check if the file name ends with a known archive extension */
                    int length=ARCHIVE_EXTENSIONS.length;
                    int index=0;
                    while((index<length)&&(reject==false)){
                        reject=extension.equals(ARCHIVE_EXTENSIONS[index]);
                        index++;
                    }
                    /* now check if the file name ends with a known archive extension pattern */
                    length=ARCHIVE_PATTERNS.length;
                    index=0;
                    while((index<length)&&(reject==false)){
                        Pattern pattern=Pattern.compile(ARCHIVE_PATTERNS[index]);
                        Matcher matcher=pattern.matcher(extension);
                        reject=matcher.matches();
                        index++;
                    }
                }
            }
        }
        return(!reject);
    }
    
    /** getDescription returns the description of file accepted by the filter.
     * @return description of files accepted by the current filter
     */
    public String getDescription(){
        return("Non-archived Files");
    }
}