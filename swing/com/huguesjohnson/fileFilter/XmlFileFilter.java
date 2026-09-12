/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.fileFilter;

import com.huguesjohnson.fileFilter.MultiExtensionFileFilter;

/**
 * XmlFileFilter Filter to only accept xml files
 * @author Hugues Johnson
 */
public class XmlFileFilter extends MultiExtensionFileFilter{
	private final static String[] EXTENSIONS={"xml"};

	/**
	 * Creates a new instance of XmlFileFilter
	 */
	public XmlFileFilter(){
		super(EXTENSIONS);
	}

	/**
	 * Returns the description of file accepted by the filter.
	 * @return Description of files accepted by the current filter.
	 */
	public String getDescription(){
		return("XML Files");
	}
}
