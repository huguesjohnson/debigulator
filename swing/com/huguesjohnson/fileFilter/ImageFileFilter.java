/*
 ImageFileFilter.java - Filter to only accept images supported by ImageIO
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.fileFilter;

import java.io.File;
import java.lang.IllegalArgumentException;
import java.lang.String;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

/**
 * ImageFileFilter filter to only accept images supported by ImageIO uses ImageReaderWriterSpi.getFileSuffixes() to get a list of supported extensions
 * @author Hugues Johnson
 */
public class ImageFileFilter extends FileFilter{

	/**
	 * Creates a new instance of ImageFileFilter
	 */
	public ImageFileFilter(){
		super();
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
					try{
						Iterator iterator=ImageIO.getImageReadersBySuffix(extension);
						acceptFile=iterator.hasNext();
					} catch(IllegalArgumentException iax){
						acceptFile=false;
					}
				}
			}
		}
		return(acceptFile);
	}

	/**
	 * getDescription returns the description of files accepted by the filter.
	 * @return description of files accepted by the current filter
	 */
	public String getDescription(){
		return("Images");
	}
}
