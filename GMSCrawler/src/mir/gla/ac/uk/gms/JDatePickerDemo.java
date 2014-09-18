package mir.gla.ac.uk.gms;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 * This program demonstrates how to use JDatePicker to display a calendar 
 * component in a Swing program.
 * @author www.codejava.net
 *
 */
public class JDatePickerDemo extends JFrame implements ActionListener {
	
	private JDatePickerImpl datePicker;
	
	public JDatePickerDemo() {
		super("Calendar Component Demo");
		setLayout(new FlowLayout());
		
		add(new JLabel("Birthday: "));
		
		UtilDateModel model = new UtilDateModel();
		model.setDate(1990, 8, 24);
		model.setSelected(true);
		
//		UtilCalendarModel model = new UtilCalendarModel();
//		SqlDateModel model = new SqlDateModel();
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		
//		datePicker = new JDatePickerImpl(datePanel);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		
		add(datePicker);
		
		JButton buttonOK = new JButton("OK");
		buttonOK.addActionListener(this);
		
		add(buttonOK);
		
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new JDatePickerDemo().setVisible(true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// for UtilDateModel, the value returned is of type java.util.Date
		Date selectedDate = (Date) datePicker.getModel().getValue();
		
		// for UtilCalendarModel, the value returned is of type java.util.Calendar
//		Calendar selectedValue = (Calendar) datePicker.getModel().getValue();
//		Date selectedDate = selectedValue.getTime();

		// for SqlDateModel, the value returned is of type java.sql.Date
//		java.sql.Date selectedDate = (java.sql.Date) datePicker.getModel().getValue();
		
		JOptionPane.showMessageDialog(this, "The selected date is " + selectedDate);
	}

}