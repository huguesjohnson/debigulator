/* https://github.com/huguesjohnson/debigulator/blob/main/LICENSE */

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
