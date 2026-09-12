/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

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
