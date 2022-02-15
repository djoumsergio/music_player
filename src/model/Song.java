/**
 * 
 */
package model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

/**
 * @author Djoum
 *
 */
public class Song implements ISong {

	private String title;
	private String artist;
	private double volume;

	private Player player;
	private BasicController player_;
	private File file;

	private FileInputStream fis;
	private BufferedInputStream bis;
	private long toRead;
	public long amountSeconds = 0;
	private long length;

	public Song() {

		this.title = null;
		this.artist = null;
		this.player = null;
		this.player_ = null;
		this.file = null;
		this.setCurrentPosition(0);
		this.setLength(0);

	}

	public Song(String path) {
		load(path);
		getProperties(this.file);
	}

	@Override
	public void stop() {
		if (player != null) {
			try {
				player_.stop();
			} catch (BasicPlayerException e) {
			}
		}

	}

	public void play() {

		long toSkip = this.length - this.toRead;
		System.out.println("to skip: " + toSkip + " and length: " + this.length);

		try 
		{
			this.fis = new FileInputStream(this.file);
		} 
		catch (FileNotFoundException e1) 
		{
			//TODO
		}

		this.bis = new BufferedInputStream(fis);
		this.skip(toSkip);
		try {
			player = new Player(bis);
			BasicPlayer b = new BasicPlayer();
			player_ = (BasicController) b;
			player_.open(bis);

		} catch (Exception e) {
		}

		try {
			System.out.println("Play song " + this.getArtist());
			//player.play();
			player_.play();
			System.out.println("complete song " + this.getArtist());
		} catch (BasicPlayerException e) {
			System.out.println("Error");
		}

	}

	public boolean pause() {

		try {
			this.toRead = bis.available();

			System.out.println("Song is paused: " + this.toRead);

			//player.close();
			player_.pause();
			this.bis.close();
			this.fis.close();

			return true;

		} catch (IOException e) {
			return false;
		} catch (BasicPlayerException e) {
			return false;
		}

	}

	public boolean skip(long value) {
		try {
			if (bis != null) {
				bis.skip(value);
				System.out.println("after the skip: " + bis.available());
				return true;
			} else {
				return false;
			}
		} catch (IOException ioe) {
			return false;
		}
	}

	public void load(String path) {

		try {
			File file = new File(path);
			if (file.exists()) {
				this.file = file;
				this.fis = new FileInputStream(file);
				this.bis = new BufferedInputStream(fis);
				this.player = new Player(bis);
				this.player_ = (BasicController) (new BasicPlayer());
				this.setLength(bis.available());
				this.setCurrentPosition(fis.available());
			} else {
				this.file = null;
				this.fis = null;
				this.bis = null;
				this.setLength(0);
			}

		} catch (JavaLayerException | IOException | NullPointerException e) {
			this.file = null;
			this.fis = null;
			this.bis = null;
			this.setLength(0);
		}
	}

	public void getProperties(File file) {
		try {

			InputStream input = new FileInputStream(file);
			ContentHandler handler = new DefaultHandler();
			Metadata metadata = new Metadata();
			Parser parser = new Mp3Parser();
			ParseContext parseCtx = new ParseContext();
			parser.parse(input, handler, metadata, parseCtx);
			input.close();

			this.title = metadata.get("title");
			this.artist = metadata.get("xmpDM:artist");

		} catch (IOException | SAXException | TikaException e) {
		}

	}

	public void getProperties(String path) {
		try {

			InputStream input = new FileInputStream(new File(path));
			ContentHandler handler = new DefaultHandler();
			Metadata metadata = new Metadata();
			Parser parser = new Mp3Parser();
			ParseContext parseCtx = new ParseContext();
			parser.parse(input, handler, metadata, parseCtx);
			input.close();

			this.title = metadata.get("title");
			this.artist = metadata.get("xmpDM:artist");

		} catch (IOException | SAXException | TikaException e) {
		}

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public long getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public long getCurrentPosition() {
		return toRead;
	}

	public void setCurrentPosition(Integer currentPosition) {
		this.toRead = currentPosition;
	}
	
	public File getFile()
	{
		return this.file;
	}
	

	public static void main(String[] args) throws InterruptedException {

		//final Song song = new Song("tmp/woo.mp3");

//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				song.play();
//			}
//		});
//		t.start();
		
		//song.play();
		
		List<File> m = new ArrayList<File>();
		
		File file = new File("tmp/06 Woo.mp3");
		System.out.println(file.exists());
		File file1 = new File("tmp/Runaway.mp3");
		
		//m.add(file);
		m.add(file1);
		
		BasicPlayer p = new BasicPlayer();
		
		p.addBasicPlayerListener(new SongListener());
		
		for (File f : m)
		{
			try 
			{
				p.open(f);
				p.play();
				//while (p.PLAYING == 0);
			} 
			catch (BasicPlayerException e) {}
			System.out.println("fggherdsg");
		}
		
		
//		Thread.sleep(5000);
//
//		song.pause();
//
//		Thread.sleep(5000);
//
//		song.play();

	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

}
