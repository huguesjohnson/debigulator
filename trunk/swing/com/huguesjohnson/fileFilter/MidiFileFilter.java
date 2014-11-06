/*
 MidiFileFilter.java - Filter to only accept midi files
 Copyright  (C) 2006 Hugues Johnson
 
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

import com.huguesjohnson.fileFilter.MultiExtensionFileFilter;

/**
 * MidiFileFilter Filter to only accept midi files
 * @author Hugues Johnson
 */
public class MidiFileFilter extends MultiExtensionFileFilter{
	private final static String[] EXTENSIONS={"mid","midi"};

	/**
	 * Creates a new instance of MidiFileFilter
	 */
	public MidiFileFilter(){
		super(EXTENSIONS);
	}

	/**
	 * Returns the description of file accepted by the filter.
	 * @return Description of files accepted by the current filter.
	 */
	public String getDescription(){
		return("Midi Files");
	}
}