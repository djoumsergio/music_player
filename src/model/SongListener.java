package model;

import java.util.Map;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

class SongListener implements BasicPlayerListener
{
	
	private Map audioInfo = null;
	private long secondsAmount = 0;
	
	@Override
	public void opened(Object arg0, Map properties) {
		
		audioInfo = properties;
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
		
		System.out.println("Seconds : "+secondsAmount);
		
	}

	@Override
	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateUpdated(BasicPlayerEvent arg0) {
		// TODO Auto-generated method stub
		
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
	
}