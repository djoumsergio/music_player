/**
 * 
 */
package model;

import java.io.File;

/**
 * @author Djoum
 *
 */
public interface ISong {

	/**
	 * Stop this song
	 */
	public void stop();
	
	/**
	 * Play this song
	 */
	public void play();
	
	/**
	 * Pause this song 
	 * @return true if it was successful otherwise false
	 */
	public boolean pause();
	
	/**
	 * Load the song related to the given file path
	 * @param path of the song to be loaded
	 */
	public void load(String path);
	
	/**
	 * Get basic properties of a song
	 * @param file to load 
	 */
	public void getProperties(File file);
	
	/**
	 * Get basic properties of a song
	 * @param path of the song 
	 */
	public void getProperties(String path);
	
	/**
	 * skip this song for a specific value
	 * @param value
	 * @return true if it was successful otherwise false
	 */
	public boolean skip(long value);
	
	
}
