package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.ButtonController;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea statusField;
	private JTable table;

	/**
	 * Create the frame.
	 */
	public MainWindow() 
	{
		ButtonController control = new ButtonController();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);

		statusField = new JTextArea();
		statusField.setForeground(Color.BLACK);
		statusField.setEnabled(false);
		statusField.setEditable(false);
		statusField.setFont(new Font("Segoe UI", Font.BOLD, 15));
		statusField.setRows(5);
		contentPane.add(statusField);

		JSlider seekSlider = new JSlider();
		seekSlider.setValue(0);
		contentPane.add(seekSlider);

		JPanel panel = new JPanel();
		contentPane.add(panel);

		JButton btnPlay = new JButton("Play");
		panel.add(btnPlay);

		btnPlay.setActionCommand("play");
		btnPlay.addActionListener(control);
		control.btnPlay = btnPlay;

		JButton btnPrev = new JButton("Previous");
		panel.add(btnPrev);

		btnPrev.setActionCommand("prev");

		JButton btnNext = new JButton("Next");
		panel.add(btnNext);
		btnNext.setActionCommand("next");

		JButton btnStop = new JButton("Stop");
		panel.add(btnStop);
		btnStop.setActionCommand("stop");
		
		JSlider gainSlider = new JSlider();
		panel.add(gainSlider);
		control.gainSlider = gainSlider;
		
		btnStop.addActionListener(control);
		btnNext.addActionListener(control);
		btnPrev.addActionListener(control);
		gainSlider.addChangeListener(control);
		
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{380, 51, 0};
		gbl_panel_2.rowHeights = new int[]{83, 23, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
				
		DefaultTableModel dm = new DefaultTableModel(0, 0);
	    String header[] = new String[] { "Title","Artist", "File"  };
	    dm.setColumnIdentifiers(header);
	    
	    
				table = new JTable();
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(dm);
				
				
				GridBagConstraints gbc_table = new GridBagConstraints();
				gbc_table.insets = new Insets(0, 0, 5, 5);
				gbc_table.fill = GridBagConstraints.BOTH;
				gbc_table.gridx = 0;
				gbc_table.gridy = 0;
				
				JScrollPane scrollPane = new JScrollPane(table);
				panel_2.add(scrollPane, gbc_table);
				
						JButton btnLoad = new JButton("Add");
						GridBagConstraints gbc_btnLoad = new GridBagConstraints();
						gbc_btnLoad.insets = new Insets(0, 0, 0, 5);
						gbc_btnLoad.anchor = GridBagConstraints.WEST;
						gbc_btnLoad.gridx = 0;
						gbc_btnLoad.gridy = 1;
						panel_2.add(btnLoad, gbc_btnLoad);
						btnLoad.setActionCommand("load");
		btnLoad.addActionListener(control);
		control.playlistTable = table;
		table.addMouseListener(control);
		table.getSelectionModel().addListSelectionListener(control);
		control.seekSlider = seekSlider;
		seekSlider.addMouseListener(control);
		seekSlider.addChangeListener(control);
		control.setStatusField(statusField);
		gainSlider.setValue(62);

	}
}
