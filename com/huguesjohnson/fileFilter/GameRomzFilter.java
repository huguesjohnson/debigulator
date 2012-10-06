/*
GameRomzFilter.java - Filter to only accept game romz
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