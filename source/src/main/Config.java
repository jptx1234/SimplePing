package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class Config extends JDialog implements ActionListener,ChangeListener,FocusListener,KeyListener,MouseListener,WindowListener{
	public static Config thisWindow = null;
	JLabel info;
	JWindow win;
	JPanel saveTipPanel = new JPanel();
	JTextField processName = new JTextField(Win.processName,20);
	JTextField ipString;
	JLabel foreColor = new JLabel("      ");
	JLabel backColor = new JLabel("      ");
	JSlider foreAlpha;
	JSlider backAlpha;
	JSpinner winSize;
	JPanel processNamePanel; 
	JPanel ipPanel;
	JRadioButton moniProcess;
	JRadioButton moniIP;
	
	private Config(JLabel info,JWindow w) {
		this.info = info;
		this.win = w;
		Container container = this.getContentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		setTitle("设置 -- 网络延迟监测工具");
		
		
		
		//TODO 设置保存提示
		saveTipPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel saveTip = new JLabel("更改后自动保存");
		saveTipPanel.add(saveTip);
		
		//TODO 更改游戏进程
		processNamePanel = new JPanel(new BorderLayout(5,0));
		JLabel processNameTips = new JLabel("游戏进程：");
		processName.addFocusListener(this);
		processName.addKeyListener(this);
		processNamePanel.add(processNameTips,"West");
		processNamePanel.add(processName,"Center");
		processNamePanel.setVisible(false);
		processNamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		//TODO 更改监测IP
		ipPanel = new JPanel(new BorderLayout(5, 0));
		JLabel ipTips = new JLabel("要监测IP：");
		String firIP = "127.0.0.1";
		if (Win.IP.size() != 0) {
			firIP = Win.IP.firstElement();
		}
		ipString = new JTextField(firIP);
		ipString.addFocusListener(this);
		ipString.addKeyListener(this);
		ipPanel.add(ipTips,"West");
		ipPanel.add(ipString,"Center");
		ipPanel.setVisible(false);
		ipPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		//TODO 监测模式
		JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
		ButtonGroup radioGroup = new ButtonGroup();
		moniProcess = new JRadioButton("监测指定进程");
		moniIP = new JRadioButton("监测指定IP");
		moniProcess.addActionListener(this);
		moniIP.addActionListener(this);
		radioGroup.add(moniProcess);
		radioGroup.add(moniIP);
		radioPanel.add(moniProcess);
		radioPanel.add(moniIP);
		
		if (Win.moniProcess) {
			moniProcess.setSelected(true);
			processNamePanel.setVisible(true);
		}else {
			moniIP.setSelected(true);
			ipPanel.setVisible(true);
		}
		
		JPanel chooseMonPanel = new JPanel();
		chooseMonPanel.setLayout(new BoxLayout(chooseMonPanel, BoxLayout.Y_AXIS));
		chooseMonPanel.add(radioPanel);
		chooseMonPanel.add(ipPanel);
		chooseMonPanel.add(processNamePanel);
		chooseMonPanel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1),"监测设置",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION));
		
		
		//TODO 更改颜色
		JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel foreColorTips = new JLabel("文字颜色：");
		foreColor.setFocusable(true);
		foreColor.setOpaque(true);
		Color oldForeColor = info.getForeground();
		foreColor.setBackground(new Color(oldForeColor.getRed(), oldForeColor.getGreen(), oldForeColor.getBlue()));
		foreColor.addFocusListener(this);
		foreColor.addMouseListener(this);
		
		JLabel backColorTips = new JLabel("      背景颜色：");
		backColor.setFocusable(true);
		backColor.setOpaque(true);
		Color oldBackColor = w.getBackground();
		backColor.setBackground(new Color(oldBackColor.getRed(), oldBackColor.getGreen(), oldBackColor.getBlue()));
		backColor.addFocusListener(this);
		backColor.addMouseListener(this);
		
		colorPanel.add(foreColorTips);
		colorPanel.add(foreColor);
		colorPanel.add(backColorTips);
		colorPanel.add(backColor);
		colorPanel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1),"颜色设置",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION));
		
		
		//TODO 透明度
		JPanel alphaPanel = new JPanel();
		alphaPanel.setLayout(new BoxLayout(alphaPanel, BoxLayout.Y_AXIS));
		
		JPanel foreAlphaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel foreAlphaTips = new JLabel("文字透明度：");
		foreAlpha = new JSlider(SwingConstants.HORIZONTAL, 0, 100, (int)(oldForeColor.getAlpha()/255.0*100));
		foreAlphaPanel.add(foreAlphaTips);
		foreAlphaPanel.add(foreAlpha);
		foreAlpha.addChangeListener(this);
		foreAlpha.addFocusListener(this);
		
		JPanel backAlphaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel backAlphaTips = new JLabel("背景透明度：");
		backAlpha = new JSlider(SwingConstants.HORIZONTAL, 0, 100, (int)(oldBackColor.getAlpha()/255.0*100));
		backAlphaPanel.add(backAlphaTips);
		backAlphaPanel.add(backAlpha);
		backAlpha.addChangeListener(this);
		backAlpha.addFocusListener(this);
		
		alphaPanel.add(foreAlphaPanel);
		alphaPanel.add(backAlphaPanel);
		
		alphaPanel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 1),"透明度设置",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION));
		
		//TODO 文字大小
		JPanel winSizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel winSizeTips = new JLabel("文字大小：");
		winSize = new JSpinner(new SpinnerNumberModel(info.getFont().getSize(), 1, 255, 1));
		winSize.addChangeListener(this);
		winSize.addFocusListener(this);
		winSizePanel.add(winSizeTips);
		winSizePanel.add(winSize);
		winSizePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		
		container.add(saveTipPanel);
		container.add(chooseMonPanel);
		container.add(winSizePanel);
		container.add(colorPanel);
		container.add(alphaPanel);
		
		addWindowListener(this);
	}

	// TODO 显示窗口
	public static void showon(JLabel info,JWindow win){
		if (thisWindow == null) {
			thisWindow=new Config(info,win);
			thisWindow.pack();
			thisWindow.setLocationRelativeTo(null);
			thisWindow.setVisible(true);
		}else {
			thisWindow.setVisible(true);
		}
	}
	
	
	
	@Override
	public void focusGained(FocusEvent e) {
		JComponent component = (JComponent) e.getSource();
		if (component.equals(foreColor) || component.equals(backColor)) {
			component.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		JComponent component = (JComponent) e.getSource();
		if (component.equals(foreColor) || component.equals(backColor)) {
			component.setBorder(null);
		}else if (component.equals(processName)) {
			Win.processName = ((JTextField)component).getText();
			info.setText("已保存监测的进程");
		}else if (component.equals(ipString)) {
			String ip_toMon = ipString.getText();
			try {
				InetAddress.getByName(ip_toMon);
				ipString.setBackground(processName.getBackground());
				ipString.setToolTipText(null);
				Vector<String> IP = new Vector<>(1,1);
				IP.add(ip_toMon);
				Win.IP = IP;
				info.setText("已保存监测的IP");
			} catch (UnknownHostException e1) {
				ipString.setBackground(Color.ORANGE);
				ipString.setToolTipText("IP格式有误");
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10) {
			e.consume();
			JComponent component = (JComponent) e.getSource();
			if (component.equals(processName)) {
				Win.processName = ((JTextField)e.getSource()).getText();
				info.setText("已保存监测的进程");
			}else if (component.equals(ipString)) {
				String ip_toMon = ipString.getText();
				try {
					InetAddress.getByName(ip_toMon);
					ipString.setBackground(processName.getBackground());
					ipString.setToolTipText(null);
					Vector<String> IP = new Vector<>(1,1);
					IP.add(ip_toMon);
					Win.IP = IP;
					info.setText("已保存监测的IP");
				} catch (UnknownHostException e1) {
					ipString.setBackground(Color.ORANGE);
					ipString.setToolTipText("IP格式有误");
				}
				
			}else if (component.equals(foreColor) || component.equals(backColor)) {
				Color c =  JColorChooser.showDialog(component.getParent(), "选择颜色", component.getBackground());
				component.setBackground(c);
				info.setForeground(c);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		JComponent component = (JComponent) e.getSource();
		component.requestFocus();
		if (component.equals(foreColor)) {
			Color c =  JColorChooser.showDialog(component.getParent(), "选择颜色", component.getBackground());
			if (c!=null) {
				component.setBackground(c);
				info.setForeground(c);
				foreAlpha.setValue((int)(c.getAlpha()/255.0*100));
			}
		}else if (component.equals(backColor)) {
			Color c =  JColorChooser.showDialog(component.getParent(), "选择颜色", component.getBackground());
			if (c!=null) {
				component.setBackground(c);
				win.setBackground(c);
				backAlpha.setValue((int)(c.getAlpha()/255.0*100));
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		thisWindow.dispose();
		thisWindow = null;
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
		case "监测指定进程":
			Win.moniProcess = true;
			if (ipPanel.isVisible()) {
				info.setText("已切换监测模式");
			}
			ipPanel.setVisible(false);
			processNamePanel.setVisible(true);
			pack();
			break;
			
		case "监测指定IP":
			Win.moniProcess = false;
			if (processNamePanel.isVisible()) {
				info.setText("已切换监测模式");
			}
			processNamePanel.setVisible(false);
			ipPanel.setVisible(true);
			pack();
			break;
		default:
			break;
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JComponent component = (JComponent) e.getSource();
		if (component.equals(foreAlpha)) {
			int v = foreAlpha.getValue();
			int a = (int) (v/100.0*255);
			if (a > 254) a = 254;
			Color oldC = info.getForeground();
			info.setForeground(new Color(oldC.getRed(), oldC.getGreen(), oldC.getBlue(), a));
		}else if (component.equals(backAlpha)) {
			int v = backAlpha.getValue();
			int a = (int) (v/100.0*255);
			if (a > 254) a = 254;
			Color oldC = win.getBackground();
			win.setBackground(new Color(oldC.getRed(), oldC.getGreen(), oldC.getBlue(), a));
		}else if (component.equals(winSize)) {
			Font newFont = info.getFont();
			info.setFont(new Font(newFont.getName(), newFont.getStyle(), (int) winSize.getValue()));
			win.pack();
		}
	}

}