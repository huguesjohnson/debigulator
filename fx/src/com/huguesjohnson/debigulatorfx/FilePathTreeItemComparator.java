/*
Debigulator - A batch compression utility
Copyright (C) 2003-2020 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.debigulatorfx;

import java.util.Comparator;

public class FilePathTreeItemComparator implements Comparator<FilePathTreeItem>{
	@Override
	public int compare(FilePathTreeItem fpti0,FilePathTreeItem fpti1){
		//sort directories ahead of files
		if(fpti0.isDirectory()){
			if(!fpti1.isDirectory()){
				//fpti0 is a directory and fpti1 is a file
				return(-1);
			}
		}else{//fpti0 is a file
			//fpti0 is a file and fpti1 is a directory
			if(fpti1.isDirectory()){
				return(1);
			}
		}
		//fpti0 and fpti1 are both directories or both files 
		return(fpti0.getValue().compareToIgnoreCase(fpti1.getValue()));
	}
}