package com.xcorpio.calc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.Utilities;

public class MainWindow extends JFrame{
	
	private static final long serialVersionUID = 3921653147362628777L;
	public int WINDOW_WIDTH=600;	//窗体宽度
	public int WINDOW_HEIGHT=450;	//窗体高度
	JMenuBar mMenuBar;	//菜单
	CustomTextArea mTextPane;
	JScrollPane mScrollPane;
	//Image textAreaBackground;		//TextArea的背景图，用于实现半透明效果
	//Robot robot;
	
	public MainWindow(){
		super();
		
		initMenuBar();									//创建菜单
		initTextPane(); 								//初始化编辑框
		this.setTitle("SmartCalculator");
		this.setSize(this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//设置窗体在屏幕正中间
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-this.WINDOW_WIDTH)/2, (Toolkit.getDefaultToolkit().getScreenSize().height-this.WINDOW_HEIGHT)/2);
		//this.setResizable(false);
		//this.setUndecorated(true);
		//this.setOpacity((float) 0.8);
		this.setVisible(true);
		
	}
	
//	@Override
//	public void paintComponents(Graphics g) {
//		// TODO Auto-generated method stub
//		super.paintComponents(g);
//		textAreaBackground=robot.createScreenCapture(new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight()));
//		g.drawImage(textAreaBackground, this.getX(), this.getY(), null);
//		System.out.println("getX()"+this.getX()+"  getY()"+this.getY());
//	}

	/**
	 * 初始化编辑框
	 */
	private void initTextPane(){
		// try {
		// robot=new Robot();
		// } catch (AWTException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		mTextPane=new CustomTextArea(this);
		//mTextArea.setLineWrap(true);
		mTextPane.setFont(new Font("微软雅黑",Font.PLAIN,14));
		mTextPane.setBackground(new Color(232, 242, 254));
		mScrollPane=new JScrollPane(mTextPane);
		mScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(mScrollPane);
	}
	
	/**
	 * 初始化菜单
	 */
	private void initMenuBar(){
		mMenuBar=new JMenuBar();
		//菜单
		JMenu menuFile=new JMenu("File");
		JMenuItem menuItemExit=new JMenuItem("Exit");
		menuItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		menuFile.add(menuItemExit);
		mMenuBar.add(menuFile);
		//关于菜单
		JMenu menuAbout=new JMenu("Help");
		JMenuItem menuItemAbout=new JMenuItem("About");
		menuItemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null, "Author:  郑少冬\nE-mail: xcorpio@qq.com \nPhone: 15109221400", "About", JOptionPane.PLAIN_MESSAGE);
			}
		});
		menuAbout.add(menuItemAbout);
		mMenuBar.add(menuAbout);
		this.setJMenuBar(mMenuBar);
	}
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new MainWindow();
			}
		});
	}
}
