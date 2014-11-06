/*
SessionFileFilter.java - Filter to only accept session files in a JFileChooser
Copyright  (C) 2000-2003 Hugues Johnson

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