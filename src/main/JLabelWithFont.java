package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JWindow;

@SuppressWarnings("serial")
public class JLabelWithFont extends JLabel {
	public boolean isCn;
	private JWindow tooltipWindow = new JWindow();
	private JTextArea ip = new JTextArea();
	private JTextArea time = new JTextArea();
	public static Font enFont=new Font("Impact", Font.BOLD, 25);
	public static Font cnFont=new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 25);
	
	public JLabelWithFont(String text,JWindow w){
		super(text);
		isCn = true;
		setFont(cnFont);
		setText(text);
		Container container = tooltipWindow.getContentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		tooltipWindow.add(ip);
		tooltipWindow.add(time);
		tooltipWindow.setBackground(new Color(0, 0, 200, 100));
		tooltipWindow.setVisible(false);
		tooltipWindow.setAlwaysOnTop(true);
		ip.setBackground(new Color(0,0,0,0));
		ip.setEditable(false);
		ip.setForeground(Color.YELLOW);
		ip.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		ip.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
		time.setBackground(new Color(0,0,0,0));
		time.setEditable(false);
		time.setForeground(Color.YELLOW);
		time.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		time.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		if (text.matches("(\\d+)ms")) {
			if (isCn) {
				setFont(enFont);
				isCn = false;
			}
		}else {
			if (!isCn) {
				setFont(cnFont);
				isCn = true;
			}
		}
		int oldWidth = Win.w.getWidth();
		Point p = Win.w.getLocation();
		Win.w.pack();
		Win.w.setLocation(p.x-(Win.w.getWidth()-oldWidth)/2,p.y);
		Win.w.pack();
	}
	
	@Override
	public void setToolTipText(String text) {
		if (tooltipWindow.isVisible()) {
			tooltipWindow.setVisible(false);
			packText(text);
			tooltipWindow.pack();
			tooltipWindow.setVisible(true);
		}else {
			packText(text);
			tooltipWindow.pack();
		}
	}
	
	public void packText(String text){
		ip.setText("");
		time.setText("");
		boolean findHead = true;
		for (String line : text.split("\r\n")) {
			String[] ipInfo = line.split("\t");
			if (ipInfo.length != 2) continue;
			if (findHead) {
				ip.append(ipInfo[0]+"  ");
				time.append(" "+ipInfo[1]);
				findHead = false;
			}else {
				ip.append("\r\n"+ipInfo[0]+"  ");
				time.append("\r\n "+ipInfo[1]);
			}
		}
	}
	
	public void showToolTip(Point location){
		tooltipWindow.setLocation(location.x-(tooltipWindow.getWidth()/2),location.y);
		tooltipWindow.setVisible(true);
	}
	
	public void hideToolTip(){
		tooltipWindow.setVisible(false);
	}
}
