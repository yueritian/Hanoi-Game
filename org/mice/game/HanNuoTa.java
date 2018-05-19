package org.mice.game;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

/**
 * 汉诺塔游戏，java初学练习
 * @author chenph
 * @date 2004-12-16
 */

class HanNuoTa1 extends Frame implements ActionListener, Runnable {

	// 最小塔长 width = 10;
	// 最小塔宽 hight = 10;
	// 最大x坐标 X = 70;
	// 最大y坐标 Y = 207;

	// 声明各变量
	private Canvas x, y, z; // 声明3块画布
	Panel title, content; // title:主界面按钮；content：画布
	Button a, b, c, plus, minus, instroduce, sumary, combtn, ok, clear, go;
	// a,b,c:分别为三个移动按钮；plus,minus:增加或减少塔数；
	// instroduce,sumary:菜单栏-help-游戏说明中 弹出游戏介绍及我的一些个人说明
	Dialog Error1, Error2, Error3, Error4, Error5, Nice, Instroduce, Timeout, Stepout, revise, herolist, Go;
	Label meg1, meg2, meg3, meg4, meg5, meg6, entername;
	TextField num;
	TextArea message, T, Step, HeroList1, HeroList2, HeroList3;
	Thread time;
	MenuItem start, restart, quit, help, Hero;
	TextField name;
	int S = 0;
	int M = 0;
	int Stepnum = 0;
	String UseTime, UseStep;
	Statement stmt;
	ResultSet rs;
	Connection con;
	String FL;
	Image icon;

	// 静态变量static值可变 且只有设定为static时才可被其它类调用

	// 控制画布壹的2种功能 1：画出所有塔 2：移动塔
	static int w = 0;

	// 储存塔长
	static int save = 0;

	// 储存塔横轴坐标
	static int X = 0;

	// 塔高度
	static int y1 = 0;
	static int y2 = 0;
	static int y3 = 0;

	// 数组内变量
	static int v1 = 0;
	static int v2 = 0;
	static int v3 = 0;

	// 控制切换提取塔释放塔
	static int button = 0;
	static int button1 = 0;
	static int button2 = 0;
	static int button3 = 0;

	// 储存横轴坐标元素和塔长元素
	static int x1[], x2[], x3[];
	static int n1[], n2[], n3[];
	static int digit = 0; // 设置层数

	HanNuoTa1() {
		// 初始化窗口标题，标题栏图标，大小，禁止更改窗口大小
		super("The towers of Hanoi-<2.0>");
		icon = Toolkit.getDefaultToolkit().getImage("开心.gif");
		this.setIconImage(icon);
		setSize(450, 322);
		setResizable(false);

		// 使窗口居中显示
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		// 创建Panel
		title = new Panel();
		content = new Panel();

		// 创建菜单栏
		MenuBar bar = new MenuBar();
		this.setMenuBar(bar);
		Menu file = new Menu("Game");
		Menu about = new Menu("Help");

		// 创建菜单
		start = new MenuItem("开始游戏", new MenuShortcut('s'));
		restart = new MenuItem("重新游戏", new MenuShortcut('r'));
		Hero = new MenuItem("英雄榜", new MenuShortcut('a'));
		quit = new MenuItem("退出", new MenuShortcut('q'));
		help = new MenuItem("游戏说明", new MenuShortcut('i'));

		// 重新开始为不可选
		restart.setEnabled(false);

		// 创建文本框
		num = new TextField("0", 6);
		num.setEditable(false);

		// 创建按钮
		minus = new Button("-");
		plus = new Button("+");
		a = new Button("a移动");
		b = new Button("b移动");
		c = new Button("c移动");
		a.setEnabled(false);
		b.setEnabled(false);
		c.setEnabled(false);
		instroduce = new Button("游戏规则");
		sumary = new Button("总结");

		// 创建画布
		x = new Circlex();
		y = new Circlex1();
		z = new Circlex2();

		// 创建存储长度数组
		n1 = new int[8];
		n2 = new int[8];
		n3 = new int[8];

		// 创建存储x坐标数组
		x1 = new int[8];
		x2 = new int[8];
		x3 = new int[8];

		// 创建线程
		time = new Thread(this, "计时器");

		// 画布颜色
		x.setBackground(Color.red);
		y.setBackground(Color.yellow);
		z.setBackground(Color.green);

		// 添加画布
		content.add(x);
		content.add(y);
		content.add(z);

		// 添加菜单
		bar.add(file);
		bar.add(about);
		file.add(start);
		file.add(restart);
		file.add(Hero);
		file.addSeparator();
		file.add(quit);
		about.add(help);

		// 添加Panel
		add("North", title);
		add("Center", content);

		// 添加Button
		title.add(minus);
		title.add(num);
		title.add(plus);
		title.add(a);
		title.add(b);
		title.add(c);

		// 设置布局
		content.setLayout(new GridLayout(1, 3, 3, 3));
		title.setLayout(new GridLayout(2, 3, 3, 3));

		// 添加监听事件
		quit.addActionListener(this);
		plus.addActionListener(this);
		minus.addActionListener(this);
		start.addActionListener(this);
		restart.addActionListener(this);
		Hero.addActionListener(this);
		help.addActionListener(this);
		a.addActionListener(this);
		b.addActionListener(this);
		c.addActionListener(this);
		instroduce.addActionListener(this);
		sumary.addActionListener(this);

		// 对话框设置

		Error1 = new Dialog(this, "Error", true);
		Error1.setSize(200, 60);
		Error1.setLocation((screenSize.width - Error1.getSize().width) / 2,
				(screenSize.height - Error1.getSize().height) / 2);
		meg1 = new Label(" 按‘+,-’，层数只能介于1~7之间");
		Error1.add(meg1);
		Error1.setResizable(false);

		Error2 = new Dialog(this, "Error", true);
		Error2.setSize(200, 60);
		Error2.setLocation((screenSize.width - Error2.getSize().width) / 2,
				(screenSize.height - Error2.getSize().height) / 2);
		meg2 = new Label("    高手！ 0层咋玩啊？！");
		Error2.add(meg2);
		Error2.setResizable(false);

		Error3 = new Dialog(this, "Error", true);
		Error3.setSize(200, 60);
		Error3.setLocation((screenSize.width - Error3.getSize().width) / 2,
				(screenSize.height - Error3.getSize().height) / 2);
		meg3 = new Label("    啊！托盘也要端走啊？！");
		Error3.add(meg3);
		Error3.setResizable(false);

		Error4 = new Dialog(this, "Error", true);
		Error4.setSize(200, 60);
		Error4.setLocation((screenSize.width - Error4.getSize().width) / 2,
				(screenSize.height - Error4.getSize().height) / 2);
		meg4 = new Label("  我那么小还拿那么大的压我啊！");
		Error4.add(meg4);
		Error4.setResizable(false);

		Error5 = new Dialog(this, "Error", true);
		Error5.setSize(200, 60);
		Error5.setLocation((screenSize.width - Error4.getSize().width) / 2,
				(screenSize.height - Error4.getSize().height) / 2);
		meg4 = new Label("  1层?! 您就甭费劲啦！");
		Error5.add(meg4);
		Error5.setResizable(false);

		Timeout = new Dialog(this, "计时器");
		Timeout.setSize(100, 100);
		Timeout.setLocation((screenSize.width - this.getSize().width) / 2 + 450,
				(screenSize.height - this.getSize().height) / 2);
		T = new TextArea(3, 4);
		Timeout.add(T);
		Timeout.setResizable(false);
		T.setEditable(false);

		Stepout = new Dialog(this, "计步器");
		Stepout.setSize(100, 100);
		Stepout.setLocation((screenSize.width - this.getSize().width) / 2 + 450,
				(screenSize.height - this.getSize().height) / 2 + 100);
		Step = new TextArea(3, 4);
		Stepout.add(Step);
		Stepout.setResizable(false);
		Step.setEditable(false);

		Nice = new Dialog(this, "好样的！", true);
		Nice.setSize(350, 70);
		Nice.setLocation((screenSize.width - Nice.getSize().width) / 2,
				(screenSize.height - Nice.getSize().height) / 2);
		meg5 = new Label();
		Nice.add(meg5);
		combtn = new Button("确定");
		Nice.add("South", combtn);
		Nice.setResizable(false);
		combtn.addActionListener(this);

		Instroduce = new Dialog(this, "游戏说明", true);
		Instroduce.setSize(300, 200);
		Instroduce.setLocation((screenSize.width - Instroduce.getSize().width) / 2,
				(screenSize.height - Instroduce.getSize().height) / 2);
		Instroduce.setLayout(null);
		message = new TextArea(4, 18);
		message.setEditable(false);
		message.setBounds(25, 45, 250, 100);
		Instroduce.add(message);
		instroduce.setBounds(75, 160, 60, 30);
		sumary.setBounds(160, 160, 60, 30);
		Instroduce.add(instroduce);
		Instroduce.add(sumary);
		Instroduce.setResizable(false);
		try {
			FileReader fr = new FileReader("游戏规则.txt");
			BufferedReader br = new BufferedReader(fr);
			String str = br.readLine();
			while (str != null) {
				message.append(str + '\n');
				str = br.readLine();
			}
		} catch (Exception b) {
		}

		revise = new Dialog(this, "恭喜进入英雄榜", true);
		revise.setSize(100, 100);
		revise.setLocation((screenSize.width - revise.getSize().width) / 2,
				(screenSize.height - revise.getSize().height) / 2);
		name = new TextField(10);
		entername = new Label("高手您地大名：");
		revise.add("Center", name);
		revise.add("North", entername);
		ok = new Button("OK!");
		revise.add("South", ok);
		ok.addActionListener(this);
		revise.setResizable(false);

		herolist = new Dialog(this, "英雄榜", true);
		herolist.setSize(230, 200);
		herolist.setLocation((screenSize.width - herolist.getSize().width) / 2,
				(screenSize.height - herolist.getSize().height) / 2);
		// ScrollPane scroller = new ScrollPane(ScrollPane.SCROLLABRS_NONE);
		Panel P = new Panel();
		Panel P1 = new Panel();
		HeroList1 = new TextArea(4, 50);
		HeroList2 = new TextArea(4, 50);
		HeroList3 = new TextArea(4, 50);
		herolist.add("Center", P);
		P.add(HeroList1);
		P.add(HeroList2);
		P.add(HeroList3);
		clear = new Button("恢复默认");
		P.setLayout(new GridLayout(1, 3, 3, 3));
		P1.setLayout(new GridLayout(1, 3, 3, 3));
		Label F = new Label("层数");
		Label N = new Label("姓名");
		Label T = new Label("时间");
		P1.add(F);
		P1.add(N);
		P1.add(T);
		herolist.add("North", P1);
		herolist.add("South", clear);
		clear.addActionListener(this);
		herolist.setResizable(false);

		Go = new Dialog(this, "Go!", true);
		Go.setSize(100, 100);
		Go.setLocation((screenSize.width - Go.getSize().width) / 2, (screenSize.height - Go.getSize().height) / 2);
		go = new Button("继续GO!GO!GO!");
		Go.add("Center", go);
		go.addActionListener(this);
		Go.setResizable(false);

		// 主窗口关闭事件
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Frame frm = (Frame) e.getWindow();
				frm.dispose();
				System.exit(0);
			}
		});

		// 对话框关闭事件
		// 编程中错误 关闭事件时需要注意当前的窗口是什么‘Error.addWindowListener’

		Error1.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Error1.setVisible(false);
				Error1.dispose();
			}
		});
		Error2.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Error2.setVisible(false);
				Error2.dispose();
			}
		});
		Error3.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Error3.setVisible(false);
				Error3.dispose();
			}
		});
		Error4.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Error4.setVisible(false);
				Error4.dispose();
			}
		});
		Error5.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Error5.setVisible(false);
				Error5.dispose();
			}
		});
		Timeout.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Timeout.setVisible(false);
				Timeout.dispose();
			}
		});
		Stepout.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Stepout.setVisible(false);
				Stepout.dispose();
			}
		});
		Nice.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Nice.setVisible(false);
				Nice.dispose();
			}
		});
		Instroduce.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Instroduce.setVisible(false);
				Instroduce.dispose();
			}
		});
		revise.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				revise.setVisible(false);
				revise.dispose();
			}
		});
		herolist.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				herolist.setVisible(false);
				herolist.dispose();
			}
		});
		Go.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Go.setVisible(false);
				Go.dispose();
			}
		});
	}

	// 计时器线程
	public void run() {
		while (v2 != digit || v3 != digit) {
			String c;
			try {
				time.sleep(1000);
			} catch (InterruptedException l) {
			}
			c = Thread.currentThread().getName();
			if (c.equals("计时器")) {
				if (S == 60) {
					M++;
					S = 0;
				} else {
					S++;
				}
				T.setText(M + " 分 " + S + " 秒");
			}
		}
	}

	//////////// 导出数据库全部元素并写在"英雄榜"上///////////////////////
	private void processRequest() {
		String stringResult1 = "";
		String stringResult2 = "";
		String stringResult3 = "";
		String result1, result2, result3;

		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String url = "jdbc:odbc:hero";
			con = DriverManager.getConnection(url);
			stmt = con.createStatement();

			String sqlselect = "select * from 表1";
			rs = stmt.executeQuery(sqlselect);

			while (rs.next()) {
				result1 = rs.getString("层数") + "层" + "\n";
				result2 = rs.getString("姓名") + "\n";
				result3 = rs.getString("所用时间") + "秒" + "\n";
				stringResult1 += result1;
				stringResult2 += result2;
				stringResult3 += result3;
			}
			HeroList1.setText(stringResult1);
			HeroList2.setText(stringResult2);
			HeroList3.setText(stringResult3);

		} catch (Exception e) {

		}
	}

	/////////////////////// 响//应//事//件/////////////////////////////

	public void actionPerformed(ActionEvent e) {

		// 初始化游戏
		if (e.getActionCommand() == "开始游戏") {
			restart.setEnabled(true);
			start.setEnabled(false);
			plus.setEnabled(false);
			minus.setEnabled(false);
			a.setEnabled(true);
			b.setEnabled(true);
			c.setEnabled(true);
			/*
			 * //重新初始化各数据 v1 = 0; v2 = 0; v3 = 0; for(int i = 0;i<8;i++) { n2[i] = 0; n3[i]
			 * = 0; } save = 0; w = 0; //壹画布两种方式转换 S = 0; M = 0; Stepnum = 0;
			 */

			time.start();

			name.setText("");// 解决输入英雄榜姓名重新打开后名称还滞留问题

			Timeout.show();
			Stepout.show();

			// 获得TextField内字符，并转换成int型，重新赋值给digit
			digit = Integer.parseInt(num.getText());

			if (digit == 0) {
				Error2.show();
				a.setEnabled(false);
				b.setEnabled(false);
				c.setEnabled(false);
				plus.setEnabled(true);
				minus.setEnabled(true);
			} else if (digit == 1) {
				Error5.show();
				a.setEnabled(false);
				b.setEnabled(false);
				c.setEnabled(false);
				plus.setEnabled(true);
				minus.setEnabled(true);
			} else if (digit != 0 && digit != 1) {
				for (int i = digit; i > 0; i--) {
					n1[v1] = (2 * i - 1) * 10;
					x1[v1] = 70 - (i - 1) * 10;
					v1++;
				}
			}
			// 重画‘x’画布
			x.repaint();
			y.repaint();
			z.repaint();

		}

		// 初始化游戏，为了解决程序中所遇到的线程问题
		if (e.getActionCommand() == "重新游戏") {
			a.setEnabled(true);
			b.setEnabled(true);
			c.setEnabled(true);
			plus.setEnabled(false);
			minus.setEnabled(false);
			if (digit != v2 || digit != v3) {
				time.suspend();
			}

			// 重新初始化各数据
			v1 = 0;
			v2 = 0;
			v3 = 0;
			for (int i = 0; i < 8; i++) {
				n1[i] = 0;
				n2[i] = 0;
				n3[i] = 0;
			}
			save = 0;
			w = 0; // 壹画布两种方式转换
			S = 0;
			M = 0;
			Stepnum = 0;

			T.setText("");
			name.setText("");
			time.resume();// 重启线程

			// 获得TextField内字符，并转换成int型，重新赋值给digit
			digit = Integer.parseInt(num.getText());

			if (digit == 0) {
				Error2.show();
				a.setEnabled(false);
				b.setEnabled(false);
				c.setEnabled(false);
				plus.setEnabled(true);
				minus.setEnabled(true);

			} else if (digit == 1) {
				Error5.show();
				a.setEnabled(false);
				b.setEnabled(false);
				c.setEnabled(false);
				plus.setEnabled(true);
				minus.setEnabled(true);
			} else if (digit != 0 && digit != 1) {
				for (int i = digit; i > 0; i--) {
					n1[v1] = (2 * i - 1) * 10;
					x1[v1] = 70 - (i - 1) * 10;
					v1++;
				}
			}
			// 重画‘x’画布
			x.repaint();
			y.repaint();
			z.repaint();

		}

		// 导出数据库
		if (e.getActionCommand() == "英雄榜") {
			processRequest();
			herolist.show();
		}

		// 退出主程序
		if (e.getActionCommand() == "退出") {
			dispose();
			System.exit(0);
		}

		// 弹出游戏说明窗口
		if (e.getActionCommand() == "游戏说明") {
			Instroduce.show();
		}

		// 显示游戏玩法
		if (e.getActionCommand() == "游戏规则") {
			try {
				message.setText("");
				FileReader fr = new FileReader("游戏规则.txt");
				BufferedReader br = new BufferedReader(fr);
				String str = br.readLine();
				while (str != null) {
					message.append(str + '\n');
					str = br.readLine();
				}
			} catch (Exception b) {
			}
		}

		// 我的一些个人说明
		if (e.getActionCommand() == "总结") {
			try {
				message.setText("");
				FileReader fr = new FileReader("总结.txt");
				BufferedReader br = new BufferedReader(fr);
				String str = br.readLine();
				while (str != null) {
					message.append(str + '\n');
					str = br.readLine();
				}
			} catch (Exception b) {
			}
		}

		// 增加层数
		if (e.getActionCommand() == "+") {
			if (digit == 7 || digit > 7) {
				Error1.show();
			} else {
				digit++;
				num.setText("" + digit);
			}
		}

		// 减少层数
		if (e.getActionCommand() == "-") {
			if (digit < 0 || digit == 0) {
				Error1.show();
			} else {
				digit--;
				num.setText("" + digit);
			}
		}

		if (e.getActionCommand() == "继续GO!GO!GO!") {
			Go.dispose();
		}

		// 更改数据库，以及恢复默认值等
		////////////////////////////////////////////////////////////////////////////////////////
		// 更改数据库资料
		if (e.getActionCommand() == "确定") {
			a.setEnabled(false);
			b.setEnabled(false);
			c.setEnabled(false);
			plus.setEnabled(true);
			minus.setEnabled(true);

			try {

				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				String url = "jdbc:odbc:hero";
				con = DriverManager.getConnection(url);
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select 层数,所用时间 from 表1");
				while (rs.next()) {

					FL = rs.getString("层数");
					int timcompare = Integer.parseInt(rs.getString("所用时间"));

					if (FL.equals(num.getText().trim())) {
						if (60 * M + S < timcompare) {
							revise.show();
						} else {
							Nice.dispose();
							Go.show();
						}
					}

				}

			} catch (Exception e1) {
				System.out.println(e1.toString());
			}
		}

		if (e.getActionCommand() == "OK!") {
			try {
				String str1 = name.getText().trim();
				String str2 = String.valueOf(M * 60 + S);
				String strupd = "update 表1 set 所用时间='" + str2 + "',姓名='" + str1 + "'where 层数='" + FL + "'";// !!
				stmt.executeUpdate(strupd);

				revise.dispose();
				Nice.dispose();
				Go.show();
			} catch (Exception e1) {
			}
		}

		// 恢复默认值
		if (e.getActionCommand() == "恢复默认") {
			try {
				//////////////////////// 导入默认数据/////////////////////////////
				String strupd = "update 表1 set 所用时间='" + "9999" + "',姓名='" + "yueritian" + "'";
				stmt.executeUpdate(strupd);
				herolist.dispose();
			} catch (Exception e1) {
			}

		}

		/////////////////////////////////////////////////////////////////////////////////////////
		/////////////
		//////////// 游戏主体部分！////////////////////////////////
		// 移动‘壹’中的盘子
		if (e.getActionCommand() == "a移动") {

			if (v1 != 0) // 塔数不为‘0’时
			{
				if (n1[v1 - 1] < save && button == 1)
					Error4.show(); // 解决大压小问题
				else // 放塔
				{
					if (button == 1) {
						n1[v1] = save;
						x1[v1] = X;
						v1++;
						y1 = 207 + (v1 - 1) * (-12);
						button = 0;
						button1 = 1;
						x.repaint();
						Stepnum++;
						Step.setText("" + Stepnum + "  步");
					} else // 取塔
					{
						v1--;
						save = n1[v1];
						X = x1[v1];
						n1[v1] = 0;
						y1 = 207 - (v1 - 1) * (-12);
						button = 1;
						button1 = 0;
						x.repaint();
					}
				}
			} else // 塔数为‘0’
			{
				if (button == 1) // 放塔
				{
					n1[v1] = save;
					x1[v1] = X;
					v1++;
					y1 = 207 + (v1 - 1) * (-12);
					button = 0;
					button1 = 1;
					x.repaint();
					Stepnum++;
					Step.setText("" + Stepnum + "  步");
				} else {
					Error3.show(); // 解决没塔还拿问题
				}
			}
		}

		// 移动‘貳’中的盘子
		if (e.getActionCommand() == "b移动") {
			if (v2 != 0) {
				if (n2[v2 - 1] < save && button == 1)
					Error4.show();
				else {
					if (button == 1) {
						n2[v2] = save;
						x2[v2] = X;
						v2++;
						y2 = 207 + (v2 - 1) * (-12);
						button = 0;
						button2 = 1;
						y.repaint();
						Stepnum++;
						Step.setText("" + Stepnum + "  步");
						if (v2 == digit) // 按规则移动完成后启动
						{
							time.suspend();// 线程挂起
							UseStep = Step.getText();
							UseTime = T.getText();
							meg5.setText("    成功啦！还会更快的！" + "共用时：" + UseTime + " 共移动：" + UseStep);
							Nice.show();
						}
					} else {
						v2--;
						save = n2[v2];
						X = x2[v2];
						n2[v2] = 0;
						y2 = 207 - (v2 - 1) * (-12);
						button = 1;
						button2 = 0;
						y.repaint();
					}
				}
			} else {
				if (button == 1) {
					n2[v2] = save;
					x2[v2] = X;
					v2++;
					y2 = 207 + (v2 - 1) * (-12);
					button = 0;
					button2 = 1;
					y.repaint();
					Stepnum++;
					Step.setText("" + Stepnum + "  步");
				} else {
					Error3.show();
				}
			}
		}

		// 移动‘叁’中的盘子
		if (e.getActionCommand() == "c移动") {
			if (v3 != 0) {
				if (n3[v3 - 1] < save && button == 1)
					Error4.show();
				else {
					if (button == 1) {
						n3[v3] = save;
						x3[v3] = X;
						v3++;
						y3 = 207 + (v3 - 1) * (-12);
						button = 0;
						button3 = 1;
						z.repaint();
						Stepnum++;
						Step.setText("" + Stepnum + "  步");
						if (v3 == digit) {
							time.suspend();// 线程挂起
							UseStep = Step.getText();
							UseTime = T.getText();
							meg5.setText("    成功啦！还会更快的！" + "共用时：" + UseTime + " 共移动：" + UseStep);
							Nice.show();
						}
					} else {
						v3--;
						save = n3[v3];
						X = x3[v3];
						n3[v3] = 0;
						y3 = 207 - (v3 - 1) * (-12);
						button = 1;
						button3 = 0;
						z.repaint();
					}
				}
			} else {
				if (button == 1) {
					n3[v3] = save;
					x3[v3] = X;
					v3++;
					y3 = 207 + (v3 - 1) * (-12);
					button = 0;
					button3 = 1;
					z.repaint();
					Stepnum++;
					Step.setText("" + Stepnum + "  步");
				} else {
					Error3.show();
				}
			}
		}
	}

}

//////////////////////////////////////////////////////////////
/////////////////
///////////// 主体部分---画塔//////////////////////
// ‘壹’画布
class Circlex extends Canvas {
	public void paint(Graphics g) {
		// 初始化画布
		g.setColor(Color.black);
		g.drawLine(75, 60, 75, 300);
		g.drawString("壹", 10, 15);
		g.fillRoundRect(0, 218, 146, 10, 5, 5);

		int j = HanNuoTa1.digit;

		if (HanNuoTa1.w == 0) // 产生游戏界面
		{
			for (int i = 1; i <= HanNuoTa1.digit; i++) {
				g.fillRoundRect(70 - (j - 1) * 10, 207 + (i - 1) * (-12), (2 * j - 1) * 10, 10, 5, 5);
				j--;
			}
			HanNuoTa1.w = 1;
		} else // 运行游戏时
		{
			for (int i = 0; i <= HanNuoTa1.v1; i++) {
				if (HanNuoTa1.n1[i] == 0) // 取塔
				{
					if (HanNuoTa1.button == 1) // 标明状态无实际意义
						;
					else {
						if (HanNuoTa1.button1 == 1) // 放最上边的塔 （解决按2次或更多次相同’移动‘按钮所出现的问题）
							g.fillRoundRect(HanNuoTa1.X, HanNuoTa1.y1, HanNuoTa1.save, 10, 5, 5);
					}
				} else // 放塔
				{
					g.fillRoundRect(HanNuoTa1.x1[i], 207 - 12 * i, HanNuoTa1.n1[i], 10, 5, 5);
				}
			}
		}
	}
}

// ‘貳’画布
class Circlex1 extends Canvas {
	public void paint(Graphics g) {
		// 初始化画布
		g.setColor(Color.black);
		g.drawLine(75, 60, 75, 300);
		g.drawString("貳", 10, 15);
		g.fillRoundRect(0, 218, 146, 10, 5, 5);

		for (int i = 0; i <= HanNuoTa1.v2; i++) {
			if (HanNuoTa1.n2[i] == 0) // 取塔
			{
				if (HanNuoTa1.button == 1) // 标明状态无实际意义
					;
				else // 放最上边的塔 （解决按2次或更多次相同’移动‘按钮所出现的问题）
				{
					if (HanNuoTa1.button2 == 1)
						g.fillRoundRect(HanNuoTa1.X, HanNuoTa1.y2, HanNuoTa1.save, 10, 5, 5);
				}
			} else // 放塔
			{
				g.fillRoundRect(HanNuoTa1.x2[i], 207 - 12 * i, HanNuoTa1.n2[i], 10, 5, 5);
			}
		}
	}
}

// ‘叁’画布
class Circlex2 extends Canvas {
	public void paint(Graphics g) {
		// 初始化画布
		g.setColor(Color.black);
		g.drawLine(75, 60, 75, 300);
		g.drawString("叁", 10, 15);
		g.fillRoundRect(0, 218, 146, 10, 5, 5);

		int i = 0;
		int j = 1;

		for (i = 0; i < HanNuoTa1.v3; i++) {
			if (HanNuoTa1.n3[i] == 0) // 取塔
			{
				if (HanNuoTa1.button == 1) // 标明状态无实际意义
					;
				else // 放最上边的塔 （解决按2次或更多次相同’移动‘按钮所出现的问题）
				{
					if (HanNuoTa1.button3 == 1)
						g.fillRoundRect(HanNuoTa1.X, HanNuoTa1.y3, HanNuoTa1.save, 10, 5, 5);
				}
			} else // 放塔
			{

				g.fillRoundRect(HanNuoTa1.x3[i], 207 - 12 * i, HanNuoTa1.n3[i], 10, 5, 5);
			}
			j++;
		}
	}

}

// 主类
public class HanNuoTa {
	public static void main(String args[]) {
		HanNuoTa1 f = new HanNuoTa1();
		f.setVisible(true);
	}
}
