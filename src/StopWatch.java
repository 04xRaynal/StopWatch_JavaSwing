/*
 * Creates a Panel using Swing Components consisting of a StopWatch.
 * Displays the timer counter, and buttons to start, pause and reset the watch.
 * Also has a button of Lap Timer for counting laps (a timed session), each lap is calculated and displayed on the panel.
 * @author 04xRaynal
 */

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class StopWatch extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	JLabel label, timerLabel;
	JButton reset, start, pause, timer;
	JScrollPane scrollPane;
	DefaultListModel<String> listModel;
	JList<String> timerList;
	
	int hour, minute, second, millisecond;
	int i = 1;
	int lapsedHour, lapsedMinute, lapsedSecond, lapsedMillisecond;
	
	Timer t;
	
	public StopWatch() {
		try {   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());   }
		catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}        //Refines the look of the ui
		
		hour = minute = second = millisecond = 0;
		lapsedHour = lapsedMinute = lapsedSecond = lapsedMillisecond = 0;
		
		setTitle("StopWatch");
		setLayout(null);
		setSize(280, 400);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				//Frame exit when close button is pressed
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	public static void main(String args[]) {
		new StopWatch();
	}
	
	public void createAndShowGUI() {
		
		label = new JLabel();
		changeLabel();
		label.setBounds(40, 5, 250, 50);
		label.setFont(new Font("Arial", Font.PLAIN, 25));
		add(label);
		
		
		Image resetImage = Toolkit.getDefaultToolkit().getImage("reset-icon.png").getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		reset = new JButton(new ImageIcon(resetImage));
		reset.addActionListener(this);
		reset.setBounds(20, 265, resetImage.getWidth(getParent()), resetImage.getHeight(getParent()));
		reset.setEnabled(false);
		add(reset);
		
		Image startImage = Toolkit.getDefaultToolkit().getImage("play-icon.png").getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		start = new JButton(new ImageIcon(startImage));
		start.addActionListener(this);
		start.setBounds(80, 265, startImage.getWidth(getParent()), startImage.getHeight(getParent()));
		add(start);
		
		Image pauseImage = Toolkit.getDefaultToolkit().getImage("pause-icon.jpg").getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		pause = new JButton(new ImageIcon(pauseImage));
		pause.addActionListener(this);
		pause.setBounds(140, 265, pauseImage.getWidth(getParent()), pauseImage.getHeight(getParent()));
		pause.setEnabled(false);
		add(pause);
		
		Image timerImage = Toolkit.getDefaultToolkit().getImage("timer-icon.png").getScaledInstance(50, 60, Image.SCALE_SMOOTH);
		timer = new JButton(new ImageIcon(timerImage));
		timer.addActionListener(this);
		timer.setBounds(200, 294, timerImage.getWidth(getParent()), timerImage.getHeight(getParent()));
		add(timer);
		
		timerLabel = new JLabel("Lap Timer:");
		timerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		timerLabel.setBounds(135, 330, 150, 30);
		add(timerLabel);
		
		listModel = new DefaultListModel<>();
		timerList = new JList<>(listModel);
		scrollPane = new JScrollPane(timerList);
		scrollPane.setBounds(13, 55, 238, 200);
		add(scrollPane);
	}
	
	
	public void update() {
		millisecond++;
		if(millisecond == 1000) {
			millisecond = 0;
			second++;
			
			if(second == 60) {
				second = 0;
				minute++;
				
				if(minute == 60) {
					minute = 0;
					hour++;
				}
			}
		}
	}
	
	
	public void reset() {
		try {
			Thread.sleep(1);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		hour = minute = second = millisecond = 0;
		
		changeLabel();
		listModel.removeAllElements();
		i = 1;
	}
	
	
	public void changeLabel() {
		DecimalFormat formatter = new DecimalFormat("00");		
		label.setText(formatter.format(hour) + " : " + formatter.format(minute) + " : " + formatter.format(second) + " : " + formatter.format(millisecond));
	}
	
	
	public void setLaps() {
		timerList.setFont(new Font("Arial", Font.ITALIC, 15));
		DecimalFormat formatter = new DecimalFormat("00");
		lapsedHour = Math.abs(hour - lapsedHour) ;
		lapsedMinute = Math.abs(minute - lapsedMinute);
		lapsedSecond = Math.abs(second - lapsedSecond);
		
		listModel.addElement("Lap " + i++ + ":   " + formatter.format(lapsedHour) + " : " + formatter.format(lapsedMinute) + " : " + formatter.format(lapsedSecond) + "        ||    Elapsed Time:  " + formatter.format(hour) + " : " + formatter.format(minute) + " : " + formatter.format(second));
		lapsedHour = hour;
		lapsedMinute = minute;
		lapsedSecond = second;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == start) {
			start.setEnabled(false);
			reset.setEnabled(true);  pause.setEnabled(true);
			
			t = new Timer(1, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					update();
					changeLabel();
				}
			});
			t.start();
		}
		
		if(e.getSource() == reset) {
			if(t != null) {					//if reset button is pressed to reset the laps without pressing the start button, it throws runtime error if null is not checked as Timer t doesn't start until start button is pressed
				t.stop();
			}
			reset();
			reset.setEnabled(false); pause.setEnabled(false);
			start.setEnabled(true);
		}
		
		if(e.getSource() == pause) {
			t.stop();
			pause.setEnabled(false);
			start.setEnabled(true); reset.setEnabled(true);
		}
		
		if(e.getSource() == timer) {
			setLaps();
			reset.setEnabled(true);
		}
	}

}

