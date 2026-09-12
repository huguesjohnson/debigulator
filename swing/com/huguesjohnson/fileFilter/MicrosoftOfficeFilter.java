/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.fileFilter;

import java.lang.String;
import com.huguesjohnson.fileFilter.MultiExtensionFileFilter;

/** MicrosoftOfficeFilter Filter to only accept Microsoft Office files 
 * @author Hugues Johnson
 */
public class MicrosoftOfficeFilter extends MultiExtensionFileFilter{
    /** known extensions of Microsoft Office files */
    private final static String[] EXTENSIONS={"doc","xls","mdb","ppt","vsd"};
    
    /** Creates a new instance of MicrosoftOfficeFilter
     */
    public MicrosoftOfficeFilter(){
        super(EXTENSIONS);
    }
    
    /** getDescription returns the description of file accepted by the filter.
     * @return description of files accepted by the current filter
     */
    public String getDescription(){
        return("Microsoft Office Files");
    }
}
