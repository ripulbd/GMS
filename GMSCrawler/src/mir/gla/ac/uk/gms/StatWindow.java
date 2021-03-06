package mir.gla.ac.uk.gms;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.text.DateFormatter;

import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.JLabel;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JTextPane;

import com.michaelbaranov.microba.calendar.DatePicker;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class StatWindow {

	private JFrame frmGmsStatistics;	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StatWindow window = new StatWindow();
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
	public StatWindow() {
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
		frmGmsStatistics.setBounds(100, 100, 1260, 747);
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
		
		String[] sourceStrings = { "BBC", "DailyRecord", "The Scotsman", "Evening Times"};
		
		final JComboBox jComboDbTotalSizeWithoutImage = new JComboBox(sourceStrings);
		final JComboBox jComboDbTotalSizeImageOnly = new JComboBox(sourceStrings);
		final JComboBox jComboDbTotalSizeTotal = new JComboBox(sourceStrings);
		
		
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
		
		long size = dbUtil.totalSize("http://www.bbc.co.uk");
        String outputToTextPane = String.format("%d Bytes, %.2fKB, %.2fMB\n", size, (double) size / 1024, (double) size / (1024 * 1024));
        textPane.setText(outputToTextPane);
		
		
		jComboDbTotalSizeWithoutImage.setBounds(150, 96, 244, 20);
		jComboDbTotalSizeWithoutImage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox)e.getSource();
		        String source = (String)cb.getSelectedItem();
		        String sourceQueryString = "";
		        if(source.equals("BBC")){
		        	jComboDbTotalSizeImageOnly.setSelectedIndex(0);
		        	jComboDbTotalSizeTotal.setSelectedIndex(0);
		        	sourceQueryString = "http://www.bbc.co.uk";
		        }
		        else if(source.equals("DailyRecord")){
		        	jComboDbTotalSizeImageOnly.setSelectedIndex(1);
		        	jComboDbTotalSizeTotal.setSelectedIndex(1);
		        	sourceQueryString = "http://www.dailyrecord.co.uk";
		        }
		        else if(source.equals("The Scotsman")){
		        	jComboDbTotalSizeImageOnly.setSelectedIndex(2);
		        	jComboDbTotalSizeTotal.setSelectedIndex(2);
		        	sourceQueryString = "http://www.scotsman.com";
		        }
		        else if(source.equals("Evening Times")){
		        	jComboDbTotalSizeImageOnly.setSelectedIndex(3);
		        	jComboDbTotalSizeTotal.setSelectedIndex(3);
		        	sourceQueryString = "Evening Times";
		        }
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
		textPaneImageOnly.setText(String.format("%d Bytes, %.2fKB, %.2fMB\n", bbcImageSize, (double) bbcImageSize / 1024, (double) bbcImageSize / (1024 * 1024)));
		

		jComboDbTotalSizeImageOnly.setBounds(185, 98, 199, 20);
		jComboDbTotalSizeImageOnly.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox)e.getSource();
		        String source = (String)cb.getSelectedItem();
		        String output = "";
		        if(source.equals("BBC")){
		        	jComboDbTotalSizeWithoutImage.setSelectedIndex(0);
		        	jComboDbTotalSizeTotal.setSelectedIndex(0);
		        	output =  String.format("%d Bytes, %.2fKB, %.2fMB\n", bbcImageSize, (double) bbcImageSize / 1024, (double) bbcImageSize / (1024 * 1024));
		        }
		        else if(source.equals("DailyRecord")){
		        	jComboDbTotalSizeWithoutImage.setSelectedIndex(1);
		        	jComboDbTotalSizeTotal.setSelectedIndex(1);
		        	output =  String.format("%d Bytes, %.2fKB, %.2fMB\n", drImageSize, (double) drImageSize / 1024, (double) drImageSize / (1024 * 1024));
		        }
		        else if(source.equals("The Scotsman")){
		        	jComboDbTotalSizeWithoutImage.setSelectedIndex(2);
		        	jComboDbTotalSizeTotal.setSelectedIndex(2);
		        	output =  String.format("%d Bytes, %.2fKB, %.2fMB\n", scotImageSize, (double) scotImageSize / 1024, (double) scotImageSize / (1024 * 1024));
		        }
		        else if(source.equals("Evening Times")){
		        	jComboDbTotalSizeWithoutImage.setSelectedIndex(3);
		        	jComboDbTotalSizeTotal.setSelectedIndex(3);
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
		lblDbTotalSizeTotal.setBounds(12, 18, 182, 21);
		totalInnerPannel.add(lblDbTotalSizeTotal);

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
		
		long bbcTotalSize = dbUtil.totalSize("http://www.bbc.co.uk");
		String outputTotal = String.format("%d Bytes, %.2fKB, %.2fMB\n", (bbcTotalSize + bbcImageSize), (double) (bbcTotalSize + bbcImageSize) / 1024,(double) (bbcTotalSize + bbcImageSize) / (1024 * 1024));
		textPaneTotal.setText(outputTotal);

		jComboDbTotalSizeTotal.setBounds(185, 98, 199, 20);
		jComboDbTotalSizeTotal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox) e.getSource();
				String source = (String) cb.getSelectedItem();
				String output = "";
				if (source.equals("BBC")) {
					jComboDbTotalSizeImageOnly.setSelectedIndex(0);
					jComboDbTotalSizeImageOnly.setSelectedIndex(0);
			        long size = dbUtil.totalSize("http://www.bbc.co.uk");
					output = String.format("%d Bytes, %.2fKB, %.2fMB\n", (size + bbcImageSize), (double) (size + bbcImageSize) / 1024,(double) (size + bbcImageSize) / (1024 * 1024));
				} else if (source.equals("DailyRecord")) {
					jComboDbTotalSizeImageOnly.setSelectedIndex(1);
					jComboDbTotalSizeImageOnly.setSelectedIndex(1);
					long size = dbUtil.totalSize("http://www.dailyrecord.co.uk");
					output = String.format("%d Bytes, %.2fKB, %.2fMB\n", (size + drImageSize), (double) (size + drImageSize) / 1024, (double) (size + drImageSize) / (1024 * 1024));
				} else if (source.equals("The Scotsman")) {
					jComboDbTotalSizeImageOnly.setSelectedIndex(2);
					jComboDbTotalSizeImageOnly.setSelectedIndex(2);
					long size = dbUtil.totalSize("http://www.scotsman.com");
					output = String.format("%d Bytes, %.2fKB, %.2fMB\n", (size + scotImageSize), (double) (size + scotImageSize) / 1024, (double) (size + scotImageSize) / (1024 * 1024));
				} else if (source.equals("Evening Times")) {
					long size = dbUtil.totalSize("Evening Times");
					jComboDbTotalSizeImageOnly.setSelectedIndex(3);
					jComboDbTotalSizeImageOnly.setSelectedIndex(3);
					output = String.format("%d Bytes, %.2fKB, %.2fMB\n", (size + etImageSize), (double) (size + etImageSize) / 1024,(double) (size + etImageSize) / (1024 * 1024));
				}
				
				textPaneTotal.setText(output);
			}
		});

		totalInnerPannel.add(jComboDbTotalSizeTotal);
		// /////////////////////////////////
		//totalInnerPannel ends here.......................
		
		JSeparator separator = new JSeparator();
		separator.setBounds(8, 216, width - 32, 2);
		panel.add(separator);
		
		/**
		 * Total Number Panel Starts here....
		 */
		JPanel panelTotalNumber = new JPanel();
		panelTotalNumber.setLayout(null);
		panelTotalNumber.setBounds(8, 226, 406, 165);
		TitledBorder titledBorderTotalNumber = new TitledBorder("Total Number Stat:");
		panelTotalNumber.setBorder(titledBorderTotalNumber);
		panel.add(panelTotalNumber);
		
		JLabel labelTotalNumber = new JLabel("Total number of news: ");
		labelTotalNumber.setBounds(12, 24, 163, 15);
		panelTotalNumber.add(labelTotalNumber);
		
		JTextPane textPaneTotalNumber = new JTextPane();
		textPaneTotalNumber.setText("");
		textPaneTotalNumber.setSize(new Dimension(40, 0));
		textPaneTotalNumber.setEditable(false);
		textPaneTotalNumber.setBounds(180, 18, 214, 21);
		panelTotalNumber.add(textPaneTotalNumber);
		
		long totalNumber = dbUtil.totalNumber();
		output = String.format("%d", totalNumber);
		textPaneTotalNumber.setText(output);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(12, 61, 380, 2);
		panelTotalNumber.add(separator_2);
		
		JLabel labelTotalNumberSource = new JLabel("Total number of news for each source:");
		labelTotalNumberSource.setBounds(12, 75, 282, 21);
		panelTotalNumber.add(labelTotalNumberSource);
		
		final JTextPane textPaneTotalNumberSource = new JTextPane();
		textPaneTotalNumberSource.setText("");
		textPaneTotalNumberSource.setBounds(208, 108, 185, 26);
		panelTotalNumber.add(textPaneTotalNumberSource);
		
		long number= dbUtil.totalNumber("http://www.bbc.co.uk");
		//textPaneTotalNumberSource.setText(String.format("%d", number));
			
		JComboBox comboBoxTotalNumberSource = new JComboBox(sourceStrings);
		comboBoxTotalNumberSource.setBounds(12, 108, 184, 26);
		
		comboBoxTotalNumberSource.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox) e.getSource();
				String source = (String) cb.getSelectedItem();
				String output = "";
				if (source.equals("BBC")) {
			        long number= dbUtil.totalNumber("http://www.bbc.co.uk");
					output = String.format("%d", number);
				} else if (source.equals("DailyRecord")) {
					long number = dbUtil.totalNumber("http://www.dailyrecord.co.uk");
					output = String.format("%d", number);
				} else if (source.equals("The Scotsman")) {
					long number = dbUtil.totalNumber("http://www.scotsman.com");
					output = String.format("%d", number);
				} else if (source.equals("Evening Times")) {
					long number = dbUtil.totalNumber("Evening Times");
					output = String.format("%d", number);
				}
				
				textPaneTotalNumberSource.setText(output);
			}
		});
		panelTotalNumber.add(comboBoxTotalNumberSource);
		
		JPanel panelIndividualNews = new JPanel();
		panelIndividualNews.setLayout(null);
		panelIndividualNews.setBounds(425, 226, 830, 165);
		TitledBorder titledBorderIndividualNews = new TitledBorder("Stat for individual news:");
		panelIndividualNews.setBorder(titledBorderIndividualNews);
		panel.add(panelIndividualNews);
		
		JLabel lblSource = new JLabel("Source:");
		lblSource.setBounds(12, 23, 70, 15);
		panelIndividualNews.add(lblSource);
		final ArrayList<GMSNewsDocument> bbcDocList = dbUtil.returnNewsForSource("http://www.bbc.co.uk");
		final ArrayList<GMSNewsDocument> drDocList = dbUtil.returnNewsForSource("http://www.dailyrecord.co.uk");
		final ArrayList<GMSNewsDocument> etDocList = dbUtil.returnNewsForSource("http://www.scotsman.com");
		final ArrayList<GMSNewsDocument> scotDocList = dbUtil.returnNewsForSource("Evening Times");
		
		
		JComboBox comboBoxSource = new JComboBox(sourceStrings);
		final JComboBox comboBoxEachNews = new JComboBox();
		comboBoxSource.setBounds(73, 18, 147, 24);
		panelIndividualNews.add(comboBoxSource);
		comboBoxSource.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox) e.getSource();
				String source = (String) cb.getSelectedItem();
				String output = "";
								
				if (source.equals("BBC")) {
					String[] array = new String[bbcDocList.size()];
					int count = 0;
					for(GMSNewsDocument tmpDoc : bbcDocList){
						//comboBoxEachNews.addItem(tmpDoc.getTitle());
						array[count++] = tmpDoc.getTitle();
					}
					DefaultComboBoxModel model = new DefaultComboBoxModel( array);
					comboBoxEachNews.setModel( model );
				} else if (source.equals("DailyRecord")) {
					String[] array = new String[drDocList.size()];
					int count = 0;
					for(GMSNewsDocument tmpDoc : drDocList){
						//comboBoxEachNews.addItem(tmpDoc.getTitle());
						array[count++] = tmpDoc.getTitle();
					}
					DefaultComboBoxModel model = new DefaultComboBoxModel( array);
					comboBoxEachNews.setModel( model );
				} else if (source.equals("The Scotsman")) {
					String[] array = new String[scotDocList.size()];
					int count = 0;
					for(GMSNewsDocument tmpDoc : scotDocList){
						//comboBoxEachNews.addItem(tmpDoc.getTitle());
						array[count++] = tmpDoc.getTitle();
					}
					DefaultComboBoxModel model = new DefaultComboBoxModel( array);
					comboBoxEachNews.setModel( model );
				} else if (source.equals("Evening Times")) {
					String[] array = new String[etDocList.size()];
					int count = 0;
					for(GMSNewsDocument tmpDoc : etDocList){
						//comboBoxEachNews.addItem(tmpDoc.getTitle());
						array[count++] = tmpDoc.getTitle();
					}
					DefaultComboBoxModel model = new DefaultComboBoxModel( array);
					comboBoxEachNews.setModel( model );
				}
			}
		});
		
		JLabel lblNews = new JLabel("News:");
		lblNews.setBounds(238, 23, 70, 15);
		panelIndividualNews.add(lblNews);
		
		
		comboBoxEachNews.setBounds(287, 18, 531, 24);
		panelIndividualNews.add(comboBoxEachNews);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(12, 61, 806, 2);
		panelIndividualNews.add(separator_3);
		
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setBounds(12, 75, 70, 26);
		panelIndividualNews.add(lblTitle);
		
		final JTextPane textPaneTitle = new JTextPane();
		textPaneTitle.setText("");
		textPaneTitle.setBounds(58, 75, 493, 26);
		panelIndividualNews.add(textPaneTitle);
		
		JLabel lblDate = new JLabel("Date:");
		lblDate.setBounds(565, 75, 70, 26);
		panelIndividualNews.add(lblDate);
		
		final JTextPane textPaneDate = new JTextPane();
		textPaneDate.setText("");
		textPaneDate.setBounds(615, 75, 203, 26);
		panelIndividualNews.add(textPaneDate);
		
		JLabel lblNewsSizewithout = new JLabel("News Size:");
		lblNewsSizewithout.setBounds(12, 120, 91, 26);
		panelIndividualNews.add(lblNewsSizewithout);
		
		final JTextPane textPaneNewsSize = new JTextPane();
		textPaneNewsSize.setText("");
		textPaneNewsSize.setBounds(92, 113, 190, 40);
		panelIndividualNews.add(textPaneNewsSize);
		
		JLabel lblImageSizes = new JLabel("Image Size:");
		lblImageSizes.setBounds(284, 120, 82, 26);
		panelIndividualNews.add(lblImageSizes);
		
		final JTextPane textPaneImageSize = new JTextPane();
		textPaneImageSize.setText("");
		textPaneImageSize.setBounds(365, 113, 188, 40);
		panelIndividualNews.add(textPaneImageSize);
		
		JLabel lblTotalSize = new JLabel("Total Size:");
		lblTotalSize.setBounds(553, 120, 82, 26);
		panelIndividualNews.add(lblTotalSize);
		
		final JTextPane textPaneTotalSize = new JTextPane();
		textPaneTotalSize.setText("");
		textPaneTotalSize.setBounds(630, 113, 190, 40);
		panelIndividualNews.add(textPaneTotalSize);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(8, 403, 1248, 2);
		panel.add(separator_4);
		
		comboBoxEachNews.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox) e.getSource();
				String source = (String) cb.getSelectedItem();
				HashMap<String, String> tmpMap = dbUtil.returnURL(source);
				String timeStamp = tmpMap.get("timeStamp");
				String URL = tmpMap.get("url");
				ArrayList<String> imageNameList = dbUtil.imageName(URL);
				
				long elementSize = dbUtil.totalSizeEachElement(URL);
				
				long imageSize = imageSize(imageNameList);
				
				textPaneDate.setText(timeStamp);
				textPaneTitle.setText(source);
				textPaneNewsSize.setText(String.format("%dBytes, %.2fKB, %.2fMB\n", elementSize , (double) elementSize  / 1024,(double) elementSize / (1024 * 1024)));
				textPaneImageSize.setText(String.format("%dBytes, %.2fKB, %.2fMB\n", imageSize, (double) imageSize / 1024,(double) imageSize / (1024 * 1024)));
				textPaneTotalSize.setText(String.format("%dBytes, %.2fKB, %.2fMB\n", (imageSize + elementSize), (double) (imageSize + elementSize) / 1024,(double) (imageSize + elementSize) / (1024 * 1024)));
				
			}
			
			
		});
		
		/**
		 * Calendar Stat Panel Starts here....
		 */
		JPanel panelCalendarStat = new JPanel();
		panelCalendarStat.setLayout(null);
		panelCalendarStat.setBounds(8, 410, 1245, 300);
		TitledBorder titledBorderCalenderPanel = new TitledBorder("Stat using Calendar:");
		panelCalendarStat.setBorder(titledBorderCalenderPanel);
		
		/*UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
		panelCalendarStat.add(datePicker);*/
		
		/*UtilDateModel model = new UtilDateModel();
		model.setDate(1990, 8, 24);
		model.setSelected(true);
		
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());*/
		
		panel.add(panelCalendarStat);
		final DatePicker  datePicker = new DatePicker(new Date());
		
		datePicker.setBounds(129, 25, 180, 22);
		panelCalendarStat.add(datePicker);
		datePicker.setVisible(true);
		datePicker.setEnabled(true);
		
		
		JLabel lblChooseADate = new JLabel("Choose a date:");
		lblChooseADate.setBounds(10, 25, 116, 22);
		panelCalendarStat.add(lblChooseADate);
		
		JLabel lblTotalNewsOn = new JLabel("Total News on this date:");
		lblTotalNewsOn.setBounds(10, 63, 174, 22);
		panelCalendarStat.add(lblTotalNewsOn);
		
		final JTextPane textPaneDateTotal = new JTextPane();
		textPaneDateTotal.setBounds(187, 64, 122, 21);
		panelCalendarStat.add(textPaneDateTotal);
		
		JLabel lblTotalNewsOf = new JLabel("Total News of ");
		lblTotalNewsOf.setBounds(10, 112, 107, 22);
		panelCalendarStat.add(lblTotalNewsOf);
		
		final JComboBox comboBoxDateFile = new JComboBox(sourceStrings);
		comboBoxDateFile.setBounds(118, 111, 116, 24);
		panelCalendarStat.add(comboBoxDateFile);
		comboBoxDateFile.setEnabled(false);
		
		final JTextPane textPaneDateSource = new JTextPane();
		textPaneDateSource.setBounds(246, 113, 64, 21);
		panelCalendarStat.add(textPaneDateSource);
		
		datePicker.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String date = datePicker.getDate().toGMTString();
				String[] dateArray = date.split(" ");
				String day = "" + (Integer.parseInt(dateArray[0]) + 1);
				String month = returnNumber(dateArray[1]);
				String year = dateArray[2];
				String total = day + "/" + month + "/" + year;
				File file = new File("tmp");
				
				FileOutputStream fop = null;
		 
				try {
		 
					fop = new FileOutputStream(file);
		 
					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}
		 
					// get the content in bytes
					byte[] contentInBytes = total.getBytes();
		 
					fop.write(contentInBytes);
					fop.flush();
					fop.close();
		 
					//System.out.println("Done");		 
				} catch (IOException ee) {
					ee.printStackTrace();
				} finally {
					try {
						if (fop != null) {
							fop.close();
						}
					} catch (IOException ee) {
						ee.printStackTrace();
					}
				}
				//System.out.println(total);
				textPaneDateTotal.setText("" + dbUtil.totalNumberEachDate(total));
				comboBoxDateFile.setEnabled(true);
			}
		});
		
		comboBoxDateFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JComboBox cb = (JComboBox) e.getSource();
				String source = (String) cb.getSelectedItem();
				String line = "";
				
				File file = new File("tmp");
				BufferedReader reader;
				try {
					reader = new BufferedReader(new FileReader("tmp"));
					line = reader.readLine();					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    
				if (source.equals("BBC")) {
					textPaneDateSource.setText("" + dbUtil.totalNumberEachDate(line, "http://www.bbc.co.uk"));
				} else if (source.equals("DailyRecord")) {
					textPaneDateSource.setText("" + dbUtil.totalNumberEachDate(line, "http://www.dailyrecord.co.uk"));
				} else if (source.equals("The Scotsman")) {
					textPaneDateSource.setText("" + dbUtil.totalNumberEachDate(line, "http://www.scotsman.com"));
				} else if (source.equals("Evening Times")) {
					textPaneDateSource.setText("" + dbUtil.totalNumberEachDate(line, "Evening Times"));
				}
			}
				
		});
		
		/*final DatePicker  datepicker = new DatePicker(new Date());
		panelCalendarStat.add(datepicker);
		
		panel.add(panelCalendarStat);
		datepicker.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(datepicker.getDate());//print the current date
                
            }
        });*/
		
		
	}
	
	/*private class OperatorAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}*/
	
	public static String returnNumber(String month){
		String number = "";
		switch(month){
			case "Jan":
			case "January":
				number = "01";
				break;
			case "Feb":
			case "February":
				number = "02";
				break;
			case "Mar":
			case "March":
				number = "03";
				break;
			case "Apr":
			case "April":
				number = "04";
				break;
			case "May":
				number = "05";
				break;
			case "Jun":
			case "June":
				number = "06";
				break;
			case "Jul":
			case "July":
				number = "07";
				break;
			case "Aug":
			case "August":
				number = "08";
				break;
			case "Sep":
			case "September":
				number = "09";
				break;
			case "Oct":
			case "October":
				number = "10";
				break;
			case "Nov":
			case "November":
				number = "11";
				break;
			case "Dec":
			case "December":
				number = "12";
				break;			
		}
		return number;
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
	
	public static long imageSize(ArrayList<String> imageList) {
	    long length = 0;
	    for(String image : imageList){
	    	File file = new File("/home/ripul/resources/images/" + image);
	    	length += file.length();
	    }
	    return length;
	}
}
