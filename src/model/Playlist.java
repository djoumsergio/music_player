package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

/**
 * This is the description of a playlist
 * @author Djoum
 *
 */
public class Playlist extends Observable implements IPlaylist, BasicPlayerListener
{
	public String name;
	public String username;
	public int length;
	public long duration;
	public int currentIndex;
	private Map<String, Object> audioInfo = null;
	private long secondsAmount = 0;
	public int c_byteslength;
	
	private double currentGain;
	
	public static final int INIT = 0;
	public static final int OPEN = 1;
	public static final int PLAY = 2;
	public static final int PAUSE = 3;
	public static final int STOP = 4;
	private int playerState = INIT;
	
	private BasicController player;
	
	public List<Song> list;
	
	/**
	 * 
	 * 
	 * @param list
	 * @param name
	 * @param username
	 * @param player TODO
	 */
	public Playlist(List<Song> list, String name, String username, BasicController player)
	{
		if (list == null)
			this.list = new ArrayList<Song>();
		else
			this.list = list;
		
		if (name == null || name == "")
			this.name = "Playlist"; //we should look into the database to generate new Playlist name
		else 
			this.name = name;
		
		if (username == null || username == "")
			this.username = "local"; //Local user
		else 
			this.username = username;
		
		this.player = player;
		((BasicPlayer) player).addBasicPlayerListener(this);
	}
	
	public Playlist(List<Song> list, String name, String username)
	{
		if (list == null)
			this.list = new ArrayList<Song>();
		else
			this.list = list;
		
		if (name == null || name == "")
			this.name = "Playlist"; //we should look into the database to generate new Playlist name
		else 
			this.name = name;
		
		if (username == null || username == "")
			this.username = "local"; //Local user
		else 
			this.username = username;
		
		this.player = new BasicPlayer();
		((BasicPlayer) player).addBasicPlayerListener(this);
	}

	@Override
	public void deleteSong(int index) {
		this.list.remove(index);
		
	}

	@Override
	public void shiftSong(int from, int to) {
		Song toSong = this.list.get(to);
		Song fromSong = this.list.get(to);
		if (toSong == null) {
			this.list.add(to, fromSong);
			this.list.remove(from);
		} else {
			this.list.add(to, fromSong);
			this.list.add(from, toSong);
		}
		
	}

	@Override
	public void play(int index) {
		if (this.list == null)
			return;
		currentIndex = index;
		Song song = this.list.get(index);
		try {
			if (playerState == PLAY)
				player.stop();
			player.open(song.getFile());
			player.play();
			playerState = PLAY;
			player.setGain(currentGain);
		} catch (BasicPlayerException e) {
			
		}
		
	}

	@Override
	public void playAll() {
		
		if (this.list == null)
			return;
		if (playerState == PAUSE)
		{
			//resume the player
			try {
				player.resume();
				playerState = PLAY;
				
			} catch (BasicPlayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			int i = this.currentIndex;
			System.out.println("Player State: "+playerState);
			if (i < this.list.size() && (playerState == INIT || playerState == STOP))
			{
				play(i);
				playerState = PLAY;
			}
			else
			{
				
			}
				
		}
		
	}
	
	public void addSong(Song song)
	{
		list.add(song);
		System.out.println("Added song");
		teller();
		
	}

	@Override
	public boolean pause() {
		if (playerState == PLAY)
		{
			try {
				player.pause();
				playerState = PAUSE;
			} catch (BasicPlayerException e) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void stop() {
		try 
		{
			if (playerState != STOP)
			{
				player.stop();
			}
				
			else
			{
				this.currentIndex = 0;
			}
		} 
		catch (BasicPlayerException e) 
		{
			
		}
		
		
	}

	@Override
	public void next() {
		try 
		{
			player.stop();
			if (this.currentIndex + 1 < list.size())
			{
				this.currentIndex++;
			}
			else
			{
				this.currentIndex = 0;
			}
			player.open(list.get(currentIndex).getFile());
			player.play();
			player.setGain(currentGain);
		} 
		catch (BasicPlayerException e) 
		{
			
		}
		
		
	}

	@Override
	public void previous() 
	{
		try 
		{
			player.stop();
			if (this.currentIndex - 1 >= 0)
			{
				this.currentIndex--;
			}
			else
			{
				this.currentIndex = 0;
			}
			player.open(list.get(currentIndex).getFile());
			player.play();
			player.setGain(currentGain);
		} 
		catch (BasicPlayerException e) 
		{
			
		}
	}
	
	public void addFiles(File[] files)
	{
		for (File f1 : files)
		{
			if (f1.getName().endsWith(".mp3"))
			{
				Song s = new Song(f1.getAbsolutePath());
				list.add(s);
			}
			
		}
		teller();
	}

	@Override
	public void opened(Object arg0, Map properties) {
		
		audioInfo = properties;
		c_byteslength = ((Integer) audioInfo.get("audio.length.bytes")).intValue();	
		System.out.println(String.format("Audio Information: %s \n ", 
				audioInfo.toString()));
		
	}

	@Override
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) 
	{
		int byteslength = -1;
		long total = -1;
		
		if (total <=0) total = (long) Math.round(getTimeLengthEstimation(audioInfo)/1000);
		
		if (audioInfo.containsKey("audio.length.bytes"))
		{
			byteslength = ((Integer) audioInfo.get("audio.length.bytes")).intValue();			
		}
		if (total <=0) total = -1;
		float progress = -1.0f;
		if ((bytesread > 0) && ((byteslength > 0))) progress = bytesread*1.0f/byteslength*1.0f;	
		
		if (audioInfo.containsKey("audio.type"))
		{
			String audioformat = (String) audioInfo.get("audio.type");
			if (audioformat.equalsIgnoreCase("mp3"))
			{
				if (total > 0) secondsAmount = (long) (total*progress);
				else secondsAmount = -1;
			}
		}
		
		if (secondsAmount < 0) secondsAmount = (long) Math.round(microseconds/1000000);
		
		this.list.get(currentIndex).amountSeconds = secondsAmount;
		
	}
	@Override
	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateUpdated(BasicPlayerEvent event) {
		
		int state = event.getCode();
		if (state==BasicPlayerEvent.EOM)
		{
			
		  this.next();
		}
		else if (state == BasicPlayerEvent.STOPPED)
		{
			System.out.println("end of song");
			playerState = STOP;
		}
		else if (state == BasicPlayerEvent.PLAYING)
		{
			playerState = PLAY;
		}
		else if (state == BasicPlayerEvent.PAUSED)
		{
			playerState = PAUSE;
		}
	}
	
	 public long getTimeLengthEstimation(Map properties)
	  {
		  long milliseconds = -1;
		  int byteslength = -1;
		  if (properties != null)
		  {
			if (properties.containsKey("audio.length.bytes"))
			{
			  byteslength = ((Integer) properties.get("audio.length.bytes")).intValue();			
			}
			if (properties.containsKey("duration"))
			{
				milliseconds = (int) (((Long) properties.get("duration")).longValue())/1000;			
			}
			else
			{
				// Try to compute duration
				int bitspersample = -1;
				int channels = -1;
				float samplerate = -1.0f;
				int framesize = -1;			 
				if (properties.containsKey("audio.samplesize.bits"))
				{
					bitspersample = ((Integer) properties.get("audio.samplesize.bits")).intValue(); 
				}
				if (properties.containsKey("audio.channels"))
				{
					channels = ((Integer) properties.get("audio.channels")).intValue(); 
				}
				if (properties.containsKey("audio.samplerate.hz"))
				{
					samplerate = ((Float) properties.get("audio.samplerate.hz")).floatValue(); 
				}
				if (properties.containsKey("audio.framesize.bytes"))
				{
					framesize = ((Integer) properties.get("audio.framesize.bytes")).intValue(); 
				}
				if (bitspersample > 0)
				{
					milliseconds = (int) (1000.0f*byteslength/(samplerate * channels * (bitspersample/8))); 
				} 
				else
				{
					milliseconds = (int)(1000.0f*byteslength/(samplerate*framesize)); 
				}			
			}
		  }
		  return milliseconds;
	  }
	 
	 public void teller()
		{
			if (countObservers() > 0)
			{
				setChanged();
				notifyObservers(this);
			}
		}

	public double getCurrentGain() {
		return currentGain;
	}

	public void setCurrentGain(double currentGain) {
		this.currentGain = currentGain;
	}
	
}
