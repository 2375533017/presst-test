package org.sunny.stresstool.panel;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.commons.codec.binary.StringUtils;
import org.sunny.stresstool.BizService;

import com.busap.stresstool.util.Constant;
import com.busap.stresstool.util.Utils;

public class MainFrame {
	private static final long serialVersionUID = 1L;

	private JButton b_start;
	private JButton b_end;
	
	private JLabel l_status_text;
	private JLabel l_status_show;
	
	private JLabel l_url;
	private JLabel l_user_count;
	private JLabel l_interval_time;
	private JLabel l_total_num;
	private JLabel l_over_count;
	private JLabel l_over_count_show;
	private JLabel l_failure_count;
	private JLabel l_failure_count_show;
	
	private JTextField t_url;
	private JTextField t_user_count;
	private JTextField t_interval_time;
	private JTextField t_total_num;
	
	
	private JFrame frame;
	
	private BizService service = new BizService();
	
	public MainFrame(){
		    frame = new JFrame();    
		    frame.setTitle("AB Press Test");
	        frame.setSize(380, 292);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        centered(frame);
	        initFrame(frame);
	        initListener();
	}
	/**
	 * if the program is running , refresh the complete thread number 
	 */
	private void refreshOverNum() {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int total = Utils.isEmpty(t_total_num.getText()) ? Constant.DEFAULT_TOTAL_COUNT :  Integer.parseInt(t_total_num.getText());
				int userNum = Utils.isEmpty(t_user_count.getText()) ? Constant.DEFAULT_USER_COUNT :  Integer.parseInt(t_user_count.getText());
						
				while(service.isRun()){
					l_over_count_show.setText(service.getOverNum()+" /  " + total * userNum );
					l_failure_count_show.setText(service.getFailNum() + " / " + total * userNum);
				}
				l_status_show.setText(" STOP ");
			}
		}).start();
		
	}
	/**
	 * add listener 
	 */
	private void initListener() {
		b_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				l_status_show.setText("RUNNING");
				checkParam();
				service.start();
				refreshOverNum();
				service.dealBiz(t_url.getText(),t_user_count.getText(),t_total_num.getText(),t_interval_time.getText());
			
			}
		});
		b_end.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				l_status_show.setText("STOP");
				service.stop();
			}
		});
	}
	/**
	 * validate the param before running the program
	 */
	private void checkParam() {
		
		
	}
	/**
	 * show frame
	 */
	public void show(){
		frame.setVisible(true);
	}
	/**
	 * show the frame in the center
	 * @param mainFrame
	 */
	private void centered(JFrame mainFrame) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int w = mainFrame.getWidth();
        int h = mainFrame.getHeight();
        mainFrame.setBounds((screenSize.width - w) / 2,
                (screenSize.height - h) / 2, w, h);
	}
	
	/**
	 * init element in the frame
	 * @param mainFrame
	 */
	private void initFrame(JFrame mainFrame){
		
		 l_url = new JLabel();
		l_url.setText("URL : ");
		l_url.setBounds(new Rectangle(20, 20, 150, 20));//x，y，宽，高
		frame.add(l_url);
		t_url = new JTextField();
		t_url.setText("");
		t_url.setBounds(new Rectangle(180,20,150,20));
		frame.add(t_url);
		
		l_user_count = new JLabel();
		l_user_count.setText("USER COUNT : ");
		l_user_count.setBounds(new Rectangle(20,50,150,20));
		mainFrame.add(l_user_count);
		t_user_count = new JTextField();
		t_user_count.setText("");
		t_user_count.setBounds(new Rectangle(180,50,150,20));
		mainFrame.add(t_user_count);
		
		l_interval_time = new JLabel();
		l_interval_time.setText("INTERVAL TIME : ");
		l_interval_time.setBounds(new Rectangle(20,80,150,20));
		mainFrame.add(l_interval_time);
		t_interval_time = new JTextField();
		t_interval_time.setBounds(new Rectangle(180,80,150,20));
		mainFrame.add(t_interval_time);
		
		l_total_num = new JLabel();
		l_total_num.setText("TOTAL COUNT : ");
		l_total_num.setBounds(new Rectangle(20,110,150,20));
		mainFrame.add(l_total_num);
		t_total_num = new JTextField();
		t_total_num.setBounds(new Rectangle(180,110,150,20));
		mainFrame.add(t_total_num);
		
		l_status_text = new JLabel();
		l_status_text.setText("STATUS : ");
		l_status_text.setBounds(new Rectangle(20,140,150,20));
		mainFrame.add(l_status_text);
		l_status_show = new JLabel();
		l_status_show.setText(" STOP ");
		l_status_show.setBounds(new Rectangle(180,140,150,20));
		mainFrame.add(l_status_show);
		
		l_over_count = new JLabel();
		l_over_count.setText("OVER COUNT : ");
		l_over_count.setBounds(new Rectangle(20,170,150,20));
		mainFrame.add(l_over_count);
		l_over_count_show = new JLabel();
		l_over_count_show.setText(service.getOverNum()+" /  " + t_total_num.getText());
		l_over_count_show.setBounds(new Rectangle(180,170,150,20));
		mainFrame.add(l_over_count_show);
		
		l_failure_count = new JLabel();
		l_failure_count.setText("FAILURE COUNT : ");
		l_failure_count.setBounds(new Rectangle(20,200,150,20));
		mainFrame.add(l_failure_count);
		l_failure_count_show = new JLabel();
		l_failure_count_show.setText(service.getFailNum()+" /  " + t_total_num.getText());
		l_failure_count_show.setBounds(new Rectangle(180,200,150,20));
		mainFrame.add(l_failure_count_show);
		
		b_start = new JButton();
		b_start.setText("START");
		b_start.setBounds(new Rectangle(20,230,150,20));
		mainFrame.add(b_start);
		
		b_end = new JButton();
		b_end.setText("END");
		b_end.setBounds(new Rectangle(180,230,150,20));
		mainFrame.add(b_end);
		JLabel jButton = new JLabel();
		mainFrame.add(jButton);
	}
	

}
