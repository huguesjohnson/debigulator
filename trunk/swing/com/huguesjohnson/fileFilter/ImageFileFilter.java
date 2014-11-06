/*
 ImageFileFilter.java - Filter to only accept images supported by ImageIO
 Copyright  (C) 2000-2004 Hugues Johnson
 
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