package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import model.Playlist;
import model.Song;
import view.ObserverSong;
import view.PlaylistObserver;

public class ButtonController implements ActionListener, ChangeListener, ListSelectionListener, MouseListener{
	
	public JButton btnPlay;
	public JButton btnStop;
	public JButton btnNext;
	public JButton btnPrev;
	
	public JTextField pathField;
	private JTextArea statusField;
	public JSlider gainSlider;
	public JSlider seekSlider;
	
	public JTable playlistTable;
	
	public PlaylistObserver observer;
	
	private static BasicPlayer musicPlayer = new BasicPlayer();
	private static Playlist playList = new Playlist(null, null, null, musicPlayer);
	
	public ButtonController() 
	{
		observer = new PlaylistObserver();
		playList.addObserver(observer);
	}
	
	public ButtonController(JButton btnPlay, JButton btnStop, JButton btnNext, JButton btnPrev) 
	{
		super();
		this.btnPlay = btnPlay;
		this.btnStop = btnStop;
		this.btnNext = btnNext;
		this.btnPrev = btnPrev;
	}



	@Override
	public void actionPerformed(ActionEvent e) {

		String action = e.getActionCommand();

		switch (action) {

		case "prev":
			playList.previous();
			btnPlay.setText("Pause");
			btnPlay.setActionCommand("pause");
			break;
		

		case "next": 
			playList.next();
			btnPlay.setText("Pause");
			btnPlay.setActionCommand("pause");
			break;
		
		case "play":
			if (musicPlayer.getStatus() != BasicPlayerEvent.UNKNOWN || playList.list.size() != 0)
			{
				((JButton)e.getSource()).setText("Pause");
				((JButton)e.getSource()).setActionCommand("pause");
				playList.playAll();
			}
			
			break;

		case "pause":
			((JButton)e.getSource()).setActionCommand("play");
			((JButton)e.getSource()).setText("Play");
			playList.pause();
			break;

		case "stop": 
			playList.stop();
			btnPlay.setActionCommand("play");
			btnPlay.setText("Play");
			break;
		
		case "load": 
			load();
			break;
		
		default: 
		{
			
		}

		}

	}
	
	public void load() 
	{
		FileFilter filter = new FileNameExtensionFilter("MP3 File", "mp3");
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			
			if (f.isDirectory())
			{
				playList.addFiles(f.listFiles());
			}
			else
			{
				Song s = new Song(f.getAbsolutePath());
				playList.addSong(s);
			}
		
			for (Song s : playList.list)
				System.out.println(s.toString());
		} 
		else 
		{}
	}
	
	
	public void prev() 
	{
		
	}

	public void next() 
	{
	
	}

	public void play(ActionEvent e) 
	{
		
	}
	
	public void stop()
	{
	
	}
	
	public void pause(ActionEvent e)
	{
	
	}

	public JTextArea getStatusField() {
		return statusField;
	}

	public void setStatusField(JTextArea statusField) {
		this.statusField = statusField;
		observer.table = playlistTable;
		
		ObserverSong os = new ObserverSong();
		os.textArea = statusField;
		os.seekSlider = seekSlider;
		
		musicPlayer.addBasicPlayerListener(os);
	}

	@Override
	public void stateChanged(ChangeEvent ce) 
	{
		if ((JSlider)ce.getSource() == gainSlider)
		{
			double value = (double)gainSlider.getValue();
			double d_value = value/100;
			
			System.out.println("Got "+d_value+" -- Max gain: "+musicPlayer.getMaximumGain());
			
			try 
			{
				playList.setCurrentGain(d_value);
				if (musicPlayer.hasGainControl())
					musicPlayer.setGain(d_value);
				
				System.out.println("Got gain in playlist "+playList.getCurrentGain());
			} 
			catch (BasicPlayerException e) 
			{
				// TODO Auto-generated catch block
				System.out.println("error "+e.toString());
			}
		}
		else if ((JSlider)ce.getSource() == seekSlider)
		{
			
			
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		System.out.println("Row number "+playlistTable.getSelectedRow());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		//Nothing
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//Nothing
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Nothing
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		if (e.getSource().getClass() == JTable.class )
		{
			JTable table = (JTable) e.getSource();
			Point p = e.getPoint();
			int row = table.rowAtPoint(p);
			if (e.getClickCount() == 2) 
			{
				System.out.println("Row number " + row + " double clicked");
				playList.play(row);
			}
		}
		else if ( e.getSource().getClass() == JSlider.class)
		{
			
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if (e.getSource().getClass() == JTable.class)
		{
			
		}
		else if (e.getSource().getClass() == JSlider.class)
		{
			if (musicPlayer.getStatus() != BasicPlayerEvent.UNKNOWN)
			{
				double value = (double)seekSlider.getValue();
				seekSlider.setValue((int)value);
				
				try {
					musicPlayer.seek((long)((value *playList.c_byteslength) / 100));
					if (musicPlayer.hasGainControl())
						musicPlayer.setGain(playList.getCurrentGain());
				} catch (BasicPlayerException bpe) {
					// TODO Auto-generated catch block
					bpe.printStackTrace();
				}
				System.out.println("Got "+(long)((value *playList.c_byteslength) / 100)+" bytes length: "+playList.c_byteslength);
			}
			else
			{
				seekSlider.setValue(0);
			}
		}
		
	}
	

}
