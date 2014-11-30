package main;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Win implements MouseListener,MouseMotionListener{
	public static String processName= "Client.exe";
	public static volatile Vector<String> IP = new Vector<>(1,1);
	public static volatile boolean moniProcess = true;
	public static volatile CheckProcess cp;
	public static JWindow w;
	private Point origin=new Point();
	public JLabelWithFont info;
	public boolean unlock = true;
	private JPopupMenu popupMenu;
	
	public static void test(){
		try { 
			Process p = Runtime.getRuntime().exec("cmd /c tasklist /fo csv /fi \"imagename eq "+"cmd.exe"+" \" /NH");
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while((line = reader.readLine()) != null){
				System.out.println(line);
			} 
			p.getInputStream().close(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
	}
	
	public Win(){
		w = new JWindow();
		info=new JLabelWithFont("查找进程中",w);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				initUI();
			}
		});
		cp = new CheckProcess(info);
		new Thread(cp).start();
		initMenu();
	}
	
	public void initUI(){
		//TODO 初始化界面
//		w.setBounds(new Rectangle(170, 50));
		w.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		w.setBackground(new Color(0,255,255,0));
		info.setForeground(new Color(255, 255, 0));
		
		w.add(info);
		
		w.pack();
		w.setLocationRelativeTo(null);
		w.setVisible(true);
		w.setAlwaysOnTop(true);
		
		w.addMouseListener(this);
		w.addMouseMotionListener(this);
		info.addMouseListener(this);
		info.addMouseMotionListener(this);
	}
	
	public void initMenu(){
		//TODO 右键菜单
		popupMenu = new JPopupMenu();
		final JCheckBoxMenuItem lockLocation = new JCheckBoxMenuItem("锁定位置", false);
		JMenuItem config = new JMenuItem("设置");
		JMenuItem exit = new JMenuItem("退出");
		popupMenu.add(lockLocation);
		popupMenu.add(config);
		popupMenu.add(exit);
		
		lockLocation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				unlock = !lockLocation.isSelected();
			}
		});
		config.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						Config.showon(info, w);
					}
				});
			}
		});
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		info.setComponentPopupMenu(popupMenu);
	}

	
	public static void main(String[] args) {
//		test();
		new Win();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO 拖放
		if (unlock) {
			Point p = w.getLocation();
			w.setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
			mouseEntered(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3){
			Point p = e.getPoint();
			popupMenu.show(e.getComponent(), p.x, p.y);
		}else{
			popupMenu.setVisible(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 鼠标按下
		origin.x = e.getX();
		origin.y = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Point wPoint = w.getLocationOnScreen();
		info.showToolTip(new Point(wPoint.x+(w.getWidth()/2), wPoint.y+w.getHeight()));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		info.hideToolTip();
	}

}
