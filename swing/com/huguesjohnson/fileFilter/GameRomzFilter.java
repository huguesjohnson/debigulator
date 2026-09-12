/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.fileFilter;

import java.lang.String;
import com.huguesjohnson.fileFilter.MultiExtensionFileFilter;

/** GameRomzFilter filter to only accept game romz
 * @author Hugues Johnson
 */
public class GameRomzFilter extends MultiExtensionFileFilter{
    /** known extensions of game romz */
    private final static String[] EXTENSIONS={"n64","bin","smd","nes","gb","gg","pce","sms"};
    
    /** Creates a new instance of GameRomzFilter
     */
    public GameRomzFilter(){
        super(EXTENSIONS);
    }
    
    /** getDescription returns the description of file accepted by the filter.
     * @return description of files accepted by the current filter
     */
    public String getDescription(){
        return("Game Romz");
    }
}
