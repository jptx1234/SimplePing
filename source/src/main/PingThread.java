package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PingThread implements Runnable {
	public boolean runFlag=false;
	public JLabelWithFont info;
	public Boolean isCn;
	public StringBuilder tootip = new StringBuilder("正在等待游戏正式开始");
	public boolean finishFlag=true;
	private byte runtimes = 0;
	private boolean lastMonProcessState = true;
	
	public PingThread(JLabelWithFont info){
		this.info = info;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (Win.IP) {
				if (runFlag && Win.IP.size() != 0) {
					String mes=ping(Win.IP);
					if (runFlag) {
						info.setText(mes);
						info.setToolTipText(tootip.toString());
						finishFlag = true;
					}
				}else {
					info.setToolTipText("正在等待游戏正式开始");
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (++runtimes == 10) {
				System.gc();
				runtimes = 0;
			}
			if (!lastMonProcessState && Win.moniProcess) {
				new Thread(new CheckProcess(info)).start();
				break;
			}else {
				lastMonProcessState = Win.moniProcess;
			}
		}
	}
	
	private String ping(Vector<String> IP){
		int maxTime=0;
		String maxtimeString = "暂无数据";
		try {
			for (String string : IP) {
				Process p=Runtime.getRuntime().exec("cmd /c ping "+string+" -n 1");
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				StringBuilder sb=new StringBuilder();
				while((line = reader.readLine()) != null){
					sb.append(line);
				} 
				p.getInputStream().close(); 
				String[] mes=sb.toString().split(" +");
				String timeString = "响应超时";
				for (int i = 0; i < mes.length; i++) {
					if (mes[i].startsWith("TTL=")) {
						timeString = mes[i-1].replaceAll(mes[i-1].split("(?:(<*+\\d+ms))")[0], "");
						Pattern pat = Pattern.compile("\\D+");
						Matcher m = pat.matcher(timeString);
//						timeString = mes[i-1].split("=|<|(ms)")[1];
						int time = Integer.valueOf(m.replaceAll("").trim());
						if (time > maxTime) {
							maxTime = time;
							maxtimeString = timeString;
						}
					}
				}
				buildTooltip("\r\n"+string+"\t"+timeString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (maxTime == 0) {
			return "响应超时";
		}else {
			return maxtimeString;
		}
	}
	
	public void buildTooltip(String str){
		if (finishFlag) {
			tootip = new StringBuilder("      连接IP\t  延迟");
			finishFlag = false;
		}
		tootip.append(str);
	}

}
