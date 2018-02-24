/*
 MultiExtensionFileFilter.java - Filter to accept a list of file extensions
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
