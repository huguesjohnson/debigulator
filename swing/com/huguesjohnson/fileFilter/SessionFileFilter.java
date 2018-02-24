/*
SessionFileFilter.java - Filter to only accept session files in a JFileChooser
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.fileFilter;

import java.io.File;
import java.lang.String;
import javax.swing.filechooser.FileFilter;

/** SessionFileFilter Filter to only accept session files in a JFileChooser
 * @author  Hugues Johnson
 */
public class SessionFileFilter extends FileFilter{
    /* extension for session files */
    public final static String EXTENSION=".session";
    
    /** Creates a new instance of SessionFileFilter 
     */
    public SessionFileFilter(){
        super();
    }
    
     /** accept tests if a file fits the filter defined in the constructor.
     * @param file file to check
     * @return true if the file is a session, false if it is not
     */
     public boolean accept(File file){
        return(file.getName().toLowerCase().endsWith(SessionFileFilter.EXTENSION));
     }
     
     /** getDescription returns the description of file accepted by the filter.
     * @return description of files accepted by the current filter
     */
     public String getDescription(){
          return("Program Sessions (*.session)");
     }        
}
