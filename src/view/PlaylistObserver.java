package view;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Playlist;
import model.Song;

public class PlaylistObserver implements Observer {

	public JTable table;
	public DefaultTableModel data_model;
	public String table_header[];
	
	public PlaylistObserver() 
	{
		data_model = new DefaultTableModel(0, 0)
				{
					
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isCellEditable(int row, int column) {
						// TODO Auto-generated method stub
						return false;
					}
				};
		table_header = new String[] { "Title","Artist", "File"  };
		data_model.setColumnIdentifiers(table_header);
		
	}
	
	@Override
	public void update(Observable arg0, Object o) {
	    table.setModel(data_model);
	    
	    Playlist play = (Playlist)o;
	    //remove all rows and update the data model
	    for (int i= 0; i<data_model.getRowCount(); i++)
	    	data_model.removeRow(i);
	    
	    for (Song s : play.list) {
	        Vector<Object> data = new Vector<Object>();
	        data.addElement(s.getTitle());
	        data.addElement(s.getArtist());
	        data.addElement(s.getFile());
	        data_model.addRow(data);
	        System.out.println("test :- ");
	    }
	}
	
	

}
