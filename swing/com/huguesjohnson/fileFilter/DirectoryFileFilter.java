/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

package com.huguesjohnson.fileFilter;

import java.io.File;
import java.lang.String;
import javax.swing.filechooser.FileFilter;

/**
 * DirectoryFileFilter - Filter to only accept directories in a JFileChooser JFileChooser has an option to only accept directories but it does not populate the description combo box
 * @author Hugues Johnson
 */
public class DirectoryFileFilter extends FileFilter{

	/**
	 * Creates a new instance of DirectoryFileFilter.
	 */
	public DirectoryFileFilter(){
		super();
	}

	/**
	 * Tests if a file fits the filter defined in the constructor.
	 * @param file The file to check.
	 * @return True if the file is a directory, false if it is not.
	 */
	public boolean accept(File file){
		return(file.isDirectory());
	}

	/**
	 * Returns the description of file accepted by the filter.
	 * @return Description of files accepted by the current filter.
	 */
	public String getDescription(){
		return("Directories");
	}
}
