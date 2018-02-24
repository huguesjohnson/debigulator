/*
 AudioFileFilter.java - Filter to only accept audio files
Copyright (C) 2003-2018 Hugues Johnson

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.huguesjohnson.fileFilter;

import java.lang.String;
import com.huguesjohnson.fileFilter.MultiExtensionFileFilter;

/**
 * AudioFileFilter Filter to only accept audio files
 * @author Hugues Johnson
 */
public class AudioFileFilter extends MultiExtensionFileFilter{
	/** known extensions of audio files */
	private final static String[] EXTENSIONS={"mid","midi","mp3","wma","wav","mod","s3m","it"};

	/**
	 * Creates a new instance of AudioFileFilter
	 */
	public AudioFileFilter(){
		super(EXTENSIONS);
	}

	/**
	 * Returns the description of files accepted by the filter.
	 * @return description of files accepted by the current filter
	 */
	public String getDescription(){
		return("Audio Files");
	}
}
