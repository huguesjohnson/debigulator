/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.fileFilter;

import java.lang.String;
import com.huguesjohnson.fileFilter.MultiExtensionFileFilter;

/** SourceCodeFileFilter Filter to only accept source code files 
 * @author Hugues Johnson
 */
public class SourceCodeFileFilter extends MultiExtensionFileFilter{
    /** known extensions of audio files */
    private final static String[] EXTENSIONS={"c","cpp","cxx","bas","java","cs","asm"};
    
    /** Creates a new instance of SourceCodeFileFilter
     */
    public SourceCodeFileFilter(){
        super(EXTENSIONS);
    }
    
    /** getDescription returns the description of file accepted by the filter.
     * @return description of files accepted by the current filter
     */
    public String getDescription(){
        return("Source Code Files");
    }
}
