/*
 MultiExtensionFileFilter.java - Filter to accept a list of file extensions
 Copyright  (C) 2000-2005 Hugues Johnson
 
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

import java.io.File;
import java.lang.String;
import java.lang.StringBuffer;
import java.util.TreeSet;
import javax.swing.filechooser.FileFilter;

/**
 * MultiExtensionFileFilter Filter to accept a list of file extensions
 * @author Hugues Johnson
 */
public class MultiExtensionFileFilter extends FileFilter{
	/** the description of this filter, build based on list of file extensions passed */
	private String description;

	/** TreeSet to hold the extensions, searches in log(n) which is better than using a brute-force search */
	private TreeSet<String> treeSet;

	/**
	 * Creates a new instance of MultiExtensionFileFilter
	 * @param extensionList the list of file extensions to accept NOT case sensitive directories always accepted
	 */
	public MultiExtensionFileFilter(String[] extensionList){
		super();
		/* build treeSet and description */
		int length=extensionList.length;
		this.treeSet=new TreeSet<String>();
		StringBuffer descriptionBuffer=new StringBuffer("(");
		if(length>0){
			descriptionBuffer.append(" *.");
			descriptionBuffer.append(extensionList[0]);
			this.treeSet.add(extensionList[0].toLowerCase());
			for(int index=1;index<length;index++){
				descriptionBuffer.append(", *.");
				descriptionBuffer.append(extensionList[index]);
				this.treeSet.add(extensionList[index].toLowerCase());
			}
		}
		descriptionBuffer.append(" )");
		this.description=new String(descriptionBuffer.toString());
	}

	/**
	 * Tests if a file has an extension of a known type.
	 * @param file The file to check.
	 * @return True if the extension of a file matches a known type.
	 */
	public final boolean accept(File file){
		boolean acceptFile=false;
		/* directories always accepted */
		if(file.isDirectory()){
			acceptFile=true;
		} else{
			String fileName=file.getName().toLowerCase();
			int indexOf=fileName.lastIndexOf(".");
			if((indexOf>=0)&&(indexOf<fileName.length())){
				String extension=fileName.substring(indexOf+1);
				if(extension.length()>0){
					/* check if the file name ends with a known extension */
					if(this.treeSet.contains(extension)){
						acceptFile=true;
					}
				}
			}
		}
		return(acceptFile);
	}

	/**
	 * Returns the description of file accepted by the filter.
	 * @return Description of files accepted by the current filter.
	 */
	public String getDescription(){
		return(this.description);
	}
}