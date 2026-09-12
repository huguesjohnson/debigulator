/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.fileFilter;

import com.huguesjohnson.fileFilter.MultiExtensionFileFilter;

/**
 * TrueTypeFontFileFilter Filter to only accept True Type fonts
 * @author Hugues Johnson
 */
public class TrueTypeFontFileFilter extends MultiExtensionFileFilter{
	private final static String[] EXTENSIONS={"ttf"};

	/**
	 * Creates a new instance of TrueTypeFontFileFilter
	 */
	public TrueTypeFontFileFilter(){
		super(EXTENSIONS);
	}

	/**
	 * Returns the description of file accepted by the filter.
	 * @return Description of files accepted by the current filter.
	 */
	public String getDescription(){
		return("True Type Fonts");
	}
}
