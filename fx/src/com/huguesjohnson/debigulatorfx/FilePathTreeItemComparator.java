/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

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