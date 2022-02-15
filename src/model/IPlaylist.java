/**
 * 
 */
package model;

/**
 * @author Djoum
 *
 */
public interface IPlaylist {
	
	/**
	 * Delete a song from the list
	 * @param index
	 */
	public void deleteSong(int index);
	
	/**
	 * Shift a song from one position to another
	 * @param from
	 * @param to
	 */
	public void shiftSong(int from, int to);
	
	/**
	 * Play the song indexed by the parameter
	 * @param index
	 */
	public void play(int index);
	
	
	/**
	 * Play all song of the list starting from the first song
	 */
	public void playAll();
	
	/**
	 * Pause the currently playing song
	 * @return
	 */
	public boolean pause();
	
	/**
	 * Stop the currently playing song and reinitialize the instance
	 */
	public void stop();
	
	/**
	 * Play the next song. The logic of this is a bit tricky since if a song is currently playing, then the worker should be switched to the 
	 * next song. inversely, if there is no song playing, then the index is incremented in such a way that next time the first song to play 
	 * will be the song with this index.
	 */
	public void next();
	
	
	/**
	 * Play the previous song. The logic is the same as next();
	 */
	public void previous();
	
	
	
}
