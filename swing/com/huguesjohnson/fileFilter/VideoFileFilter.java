/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.fileFilter;

import java.lang.String;
import com.huguesjohnson.fileFilter.MultiExtensionFileFilter;

/** VideoFileFilter Filter to only accept video files 
 * @author Hugues Johnson
 */
public class VideoFileFilter extends MultiExtensionFileFilter{
    /** known extensions of video files */
    private final static String[] EXTENSIONS={"avi","mpg","mov","wmv","mpeg","mp2","asf","wma"};
    
    /** Creates a new instance of VideoFileFilter
     */
    public VideoFileFilter(){
        super(EXTENSIONS);
    }
    
    /** getDescription returns the description of file accepted by the filter.
     * @return description of files accepted by the current filter
     */
    public String getDescription(){
        return("Video Files");
    }
}
