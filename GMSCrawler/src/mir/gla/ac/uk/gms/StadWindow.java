package mir.gla.ac.uk.gms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.JLabel;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JTextPane;

public class StadWindow {

	private JFrame frmGmsStatistics;	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StadWindow window = new StadWindow();
					window.frmGmsStatistics.setVisible(true);
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					 
					// get 2/3 of the height, and 2/3 of the width
					int height = screenSize.height * 2 / 3;
					int width = screenSize.width * 2 / 3;
					
					window.frmGmsStatistics.setSize(new Dimension(width, height));
					window.frmGmsStatistics.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StadWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		final DBUtils dbUtil = new DBUtils();
		
		frmGmsStatistics = new JFrame();
		frmGmsStatistics.setResizable(false);
		frmGmsStatistics.setTitle("GMS - Statistics");
		frmGmsStatistics.setBounds(100, 100, 1215, 405);
		frmGmsStatistics.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLocation(0, -16);
		panel.setSize(new Dimension(1280, 736));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 
		int height = screenSize.height * 2 / 3;
		int width = screenSize.width * 2 / 3;
		frmGmsStatistics.getContentPane().setLayout(null);
		panel.setSize(1280, 736);		
		frmGmsStatistics.getContentPane().add(panel);
		panel.setLayout(null);
		
		/**
		 * Each title border represents an inner panel and consists of several components.  
		 */
		//dbInnerPanel Starts here...................
		JPanel dbInnerPannel = new JPanel();
		dbInnerPannel.setBounds(8, 26, 10, 10);
		dbInnerPannel.setSize(width / 3 - 20, height / 4);
		TitledBorder dbInnerPannelTitled = new TitledBorder("DB Stat(without images):");
	    dbInnerPannel.setBorder(dbInnerPannelTitled);
		panel.add(dbInnerPannel);
		dbInnerPannel.setLayout(null);
		
		JLabel lblDbTotalSizeWithoutImage = new JLabel("Total size in DB: ");
		lblDbTotalSizeWithoutImage.setBounds(12, 24, 118, 15);
		dbInnerPannel.add(lblDbTotalSizeWithoutImage);
		
		JTextPane textDbTotalSizeWithoutImage = new JTextPane();
		textDbTotalSizeWithoutImage.setSize(new Dimension(40, 0));
		textDbTotalSizeWithoutImage.setEditable(false);
		textDbTotalSizeWithoutImage.setBounds(12, 49, 382, 21);
		dbInnerPannel.add(textDbTotalSizeWithoutImage);
		
		long totalSize = dbUtil.totalSize();
		String output = String.format("%d Bytes, %.2fKB, %.2fMB\n", totalSize, (double) totalSize / 1024, (double) totalSize / (1024 * 1024));
		textDbTotalSizeWithoutImage.setText(output);
		
		JLabel lblTotalSizeIn = new JLabel("Total size in DB of:");
		lblTotalSizeIn.setBounds(12, 99, 132, 15);
		dbInnerPannel.add(lblTotalSizeIn);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(14, 82, 380, 2);
		dbInnerPannel.add(separator_1);
		
		final JTextPane textPane = new JTextPane();
		textPane.setBounds(12, 142, 382, 26);
		dbInnerPannel.add(textPane);
		
		String[] sourceStrings = { "BBC", "DailyRecord", "The Scotsman", "Evening Times"};

		JComboBox jComboDbTotalSizeWithoutImage = new JComboBox(sourceStrings);
		jComboDbTotalSizeWithoutImage.setBounds(150, 96, 244, 20);
		jComboDbTotalSizeWithoutImage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox)e.getSource();
		        String source = (String)cb.getSelectedItem();
		        String sourceQueryString = "";
		        if(source.equals("BBC"))sourceQueryString = "http://www.bbc.co.uk";
		        else if(source.equals("DailyRecord"))sourceQueryString = "http://www.dailyrecord.co.uk";
		        else if(source.equals("The Scotsman"))sourceQueryString = "http://www.scotsman.com";
		        else if(source.equals("Evening Times"))sourceQueryString = "Evening Times";
		        long size = dbUtil.totalSize(sourceQueryString);
		        String output = String.format("%d Bytes, %.2fKB, %.2fMB\n", size, (double) size / 1024, (double) size / (1024 * 1024));
		        textPane.setText(output);
			}
		});
		
		dbInnerPannel.add(jComboDbTotalSizeWithoutImage);
		//dbInnerPannel ends here.......................
		
		//dbImagePannel starts here.......................
		JPanel imageInnerPannel = new JPanel();
		imageInnerPannel.setBounds(426 , 26, 10, 10);
		imageInnerPannel.setSize(width / 3 - 20, height / 4);
		TitledBorder imageInnerPannelTitled = new TitledBorder("Image Stat:");
		imageInnerPannel.setBorder(imageInnerPannelTitled);
		imageInnerPannel.setLayout(null);
		panel.add(imageInnerPannel);
		
		//Components..........
		JLabel lblDbTotalSizeImageOnly = new JLabel("Total size of all images: ");
		lblDbTotalSizeImageOnly.setBounds(12, 18, 182, 21);
		imageInnerPannel.add(lblDbTotalSizeImageOnly);
		
		JTextPane textDbTotalSizeImageOnly = new JTextPane();
		textDbTotalSizeImageOnly.setSize(new Dimension(40, 0));
		textDbTotalSizeImageOnly.setEditable(false);
		textDbTotalSizeImageOnly.setBounds(12, 51, 380, 21);
		imageInnerPannel.add(textDbTotalSizeImageOnly);
		
		final long bbcImageSize = folderSize(new File("/home/ripul/images/bbc/"));
		final long drImageSize = folderSize(new File("/home/ripul/images/dr/"));
		final long etImageSize = folderSize(new File("/home/ripul/images/et/"));
		final long scotImageSize = folderSize(new File("/home/ripul/images/scotsman/"));
		long totalImageSize = bbcImageSize + drImageSize + etImageSize + scotImageSize;
		
		output = String.format("%d Bytes, %.2fKB, %.2fMB\n", totalImageSize, (double) totalImageSize / 1024, (double) totalImageSize / (1024 * 1024));
		textDbTotalSizeImageOnly.setText(output);
		
		JLabel lblTotalSizeImageOnly = new JLabel("Total size of images in:");
		lblTotalSizeImageOnly.setBounds(12, 101, 182, 15);
		imageInnerPannel.add(lblTotalSizeImageOnly);
		
		JSeparator separatorImageOnly = new JSeparator();
		separatorImageOnly.setBounds(12, 84, 380, 2);
		imageInnerPannel.add(separatorImageOnly);
		
		final JTextPane textPaneImageOnly = new JTextPane();
		textPaneImageOnly.setBounds(12, 142, 382, 26);
		imageInnerPannel.add(textPaneImageOnly);
		

		JComboBox jComboDbTotalSizeImageOnly = new JComboBox(sourceStrings);
		jComboDbTotalSizeImageOnly.setBounds(185, 98, 199, 20);
		jComboDbTotalSizeImageOnly.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox)e.getSource();
		        String source = (String)cb.getSelectedItem();
		        String output = "";
		        if(source.equals("BBC")){
		        	output =  String.format("%d Bytes, %.2fKB, %.2fMB\n", bbcImageSize, (double) bbcImageSize / 1024, (double) bbcImageSize / (1024 * 1024));
		        }
		        else if(source.equals("DailyRecord")){
		        	output =  String.format("%d Bytes, %.2fKB, %.2fMB\n", drImageSize, (double) drImageSize / 1024, (double) drImageSize / (1024 * 1024));
		        }
		        else if(source.equals("The Scotsman")){
		        	output =  String.format("%d Bytes, %.2fKB, %.2fMB\n", scotImageSize, (double) scotImageSize / 1024, (double) scotImageSize / (1024 * 1024));
		        }
		        else if(source.equals("Evening Times")){
		        	output =  String.format("%d Bytes, %.2fKB, %.2fMB\n", etImageSize, (double) etImageSize / 1024, (double) etImageSize / (1024 * 1024));
		        }
		        
		        textPaneImageOnly.setText(output);
			}
		});
		
		imageInnerPannel.add(jComboDbTotalSizeImageOnly);
		///////////////////////////////////
		//dbImagePannel ends here.......................
		
		//totalInnerPannel starts here.......................
		JPanel totalInnerPannel = new JPanel();
		totalInnerPannel.setBounds(850, 26, 10, 10);
		totalInnerPannel.setSize(width / 3 - 20, height / 4);
		TitledBorder totalInnerPannelTitled = new TitledBorder("Total Stat (DB + Images):");
		totalInnerPannel.setBorder(totalInnerPannelTitled);
		totalInnerPannel.setLayout(null);
		panel.add(totalInnerPannel);
		
		//Components..........
		JLabel lblDbTotalSizeTotal = new JLabel("Total size of all data: ");
		lblDbTotalSizeImageOnly.setBounds(12, 18, 182, 21);
		totalInnerPannel.add(lblDbTotalSizeImageOnly);

		JTextPane textDbTotalSizeTotal = new JTextPane();
		textDbTotalSizeTotal.setSize(new Dimension(40, 0));
		textDbTotalSizeTotal.setEditable(false);
		textDbTotalSizeTotal.setBounds(12, 51, 380, 21);
		totalInnerPannel.add(textDbTotalSizeTotal);

		output = String.format("%d Bytes, %.2fKB, %.2fMB\n", (totalImageSize + totalSize),(double) (totalImageSize + totalSize) / 1024, (double) (totalImageSize + totalSize) / (1024 * 1024));
		textDbTotalSizeTotal.setText(output);

		JLabel lblTotalSizeTotal = new JLabel("Total size of data in:");
		lblTotalSizeTotal.setBounds(12, 101, 182, 15);
		totalInnerPannel.add(lblTotalSizeTotal);

		JSeparator separatorTotal = new JSeparator();
		separatorTotal.setBounds(12, 84, 380, 2);
		totalInnerPannel.add(separatorTotal);

		final JTextPane textPaneTotal = new JTextPane();
		textPaneTotal.setBounds(12, 142, 382, 26);
		totalInnerPannel.add(textPaneTotal);

		JComboBox jComboDbTotalSizeTotal = new JComboBox(sourceStrings);
		jComboDbTotalSizeTotal.setBounds(185, 98, 199, 20);
		jComboDbTotalSizeTotal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox) e.getSource();
				String source = (String) cb.getSelectedItem();
				String output = "";
				if (source.equals("BBC")) {
			        long size = dbUtil.totalSize("http://www.bbc.co.uk");
					output = String.format("%d Bytes, %.2fKB, %.2fMB\n", (size + bbcImageSize), (double) (size + bbcImageSize) / 1024,(double) (size + bbcImageSize) / (1024 * 1024));
				} else if (source.equals("DailyRecord")) {
					long size = dbUtil.totalSize("http://www.dailyrecord.co.uk");
					output = String.format("%d Bytes, %.2fKB, %.2fMB\n", (size + drImageSize), (double) (size + drImageSize) / 1024, (double) (size + drImageSize) / (1024 * 1024));
				} else if (source.equals("The Scotsman")) {
					long size = dbUtil.totalSize("http://www.scotsman.com");
					output = String.format("%d Bytes, %.2fKB, %.2fMB\n", (size + scotImageSize), (double) (size + scotImageSize) / 1024, (double) (size + scotImageSize) / (1024 * 1024));
				} else if (source.equals("Evening Times")) {
					long size = dbUtil.totalSize("Evening Times");
					output = String.format("%d Bytes, %.2fKB, %.2fMB\n", (size + etImageSize), (double) (size + etImageSize) / 1024,(double) (size + etImageSize) / (1024 * 1024));
				}

				textPaneTotal.setText(output);
			}
		});

		totalInnerPannel.add(jComboDbTotalSizeTotal);
		// /////////////////////////////////
		//totalInnerPannel ends here.......................
		
		JSeparator separator = new JSeparator();
		separator.setBounds(8, 216, width - 20, 2);
		panel.add(separator);
		
	}
	
	public static long folderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += folderSize(file);
	    }
	    return length;
	}
}
