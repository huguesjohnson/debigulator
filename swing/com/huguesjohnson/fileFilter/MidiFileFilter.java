/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

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
