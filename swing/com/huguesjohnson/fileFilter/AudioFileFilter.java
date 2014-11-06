/*
 AudioFileFilter.java - Filter to only accept audio files
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

/**
 * AudioFileFilter Filter to only accept audio files
 * @author Hugues Johnson
 */
public class AudioFileFilter extends MultiExtensionFileFilter{
	/** known extensions of audio files */
	private final static String[] EXTENSIONS={"mid","midi","mp3","wma","wav","mod","s3m","it"};

	/**
	 * Creates a new instance of AudioFileFilter
	 */
	public AudioFileFilter(){
		super(EXTENSIONS);
	}

	/**
	 * Returns the description of files accepted by the filter.
	 * @return description of files accepted by the current filter
	 */
	public String getDescription(){
		return("Audio Files");
	}
}