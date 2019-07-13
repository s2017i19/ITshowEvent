import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

class JPanel01 extends JPanel {
	private JButton StartBtn, StartBtn_hover;
	private JLabel bg;
	private Main one;
	
	public JPanel01(Main one) { // gameMain page
		this.one = one;
		setLayout(null);
		
		bg = new JLabel(new ImageIcon("img/main.png"));
		bg.setBounds(0, 0, 1920, 1080);
		
		StartBtn = new JButton(new ImageIcon("img/startBtn.png"));
		StartBtn.setBounds(750, 540, 450, 450);
		StartBtn.setBorderPainted(false);
		StartBtn.setFocusPainted(false);
		StartBtn.setContentAreaFilled(false);
		
		StartBtn.setRolloverIcon(new ImageIcon("img/startBtn_hover.png")); // hover ����
		
		add(StartBtn);
		add(bg);
		
		StartBtn.addActionListener(new MyActionListener());
	}
	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			one.change("panel02");
		}
	}
}

class JPanel02 extends JPanel { // gameExplain page
	private JButton gameStartBtn;
	private JLabel bg;
	private Main one;
	private JTextField name;
	String userName;
	
	public JPanel02(Main one) {
		this.one = one;
		setLayout(null);
		
		bg = new JLabel(new ImageIcon("img/explain.png"));
		bg.setBounds(0, 0, 1920, 1080);
		
		name = new JTextField(10) {
			public void setBorder(Border border) { }
		};
		name.setFont(name.getFont().deriveFont(Font.PLAIN, 24f));
		name.setBounds(630, 915, 300, 70);
		name.setHorizontalAlignment(name.CENTER);
		
		gameStartBtn = new JButton(new ImageIcon("img/gameStartBtn.png"));
		gameStartBtn.setBounds(1150, 910, 200, 100);
		gameStartBtn.setBorderPainted(false);
		gameStartBtn.setFocusPainted(false);
		gameStartBtn.setContentAreaFilled(false);
		
		gameStartBtn.setRolloverIcon(new ImageIcon("img/gameStartBtn_hover.png"));
		
		bg.add(name);
		bg.add(gameStartBtn);
		add(bg);
		
		gameStartBtn.addActionListener(new MyActionListener());
	}
	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			userName = name.getText();
			if(userName.equals("")) {
				name.setText("�̸��� �Է����ּ���");
				name.requestFocus();
				name.selectAll();
			} else {
				try {
					currentUser(userName);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				one.jpanel05 = new JPanel05(one);
				add(one.jpanel05);
				one.change("panel05");
			}
		}
	}
	public void currentUser(String name) throws IOException {
		PrintWriter pw = new PrintWriter("C:/java_study/ITshow/file/userName.txt");
		pw.println(name);
		
		pw.close();
	}
}

class JPanel04 extends JPanel { // gameRanking page
	public Connection connection;
	private JButton mainBtn;
	private JLabel bg, rankingName, rankingScore;
	private Main one;
	
	public JPanel04(Main one) {
		this.one = one;
		setLayout(null);
		
		bg = new JLabel(new ImageIcon("img/ranking.png"));
		bg.setBounds(0, 0, 1920, 1080);
	
		mainBtn = new JButton(new ImageIcon("img/startBtn.png"));
		mainBtn.setBounds(1455, 300, 450, 450);
		mainBtn.setBorderPainted(false);
		mainBtn.setFocusPainted(false);
		mainBtn.setContentAreaFilled(false);
		
		rankingName = new JLabel();
		rankingName.setFont(new Font("�������", Font.PLAIN, 26));
		//rankingName.setBorder(new LineBorder(Color.black));
		rankingName.setBounds(460, 300, 355, 600);
		
		rankingScore = new JLabel();
		rankingScore.setFont(new Font("�������", Font.PLAIN, 26));
		rankingScore.setBounds(870, 300, 355, 600);
		
		mainBtn.setRolloverIcon(new ImageIcon("img/mainBtn_hover.png"));
		
		bg.add(rankingName);
		bg.add(rankingScore);
		bg.add(mainBtn);
		add(bg);
		
		try {
			connectionDB();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		mainBtn.addActionListener(new MyActionListener());
	}
	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			one.change("panel01");
		}
	}
	public void connectionDB() throws ClassNotFoundException, SQLException {
		int i = 0;
		String userName, userScore;
		String beforeName = "<html>";
		String beforeScore = "<html>";
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:itshow.db");
		Statement s = connection.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT * FROM userTbl ORDER BY userScore DESC LIMIT 10");
		
		while(rs.next()) {
			i++;
			userName = rs.getString("userName");
			userScore = rs.getString("userScore");
			
			if(i<=2) {
				beforeName += "<font size=16><b>" + userName + "</b></font><br><font size=4><p></p></font>";
				beforeScore += "<font size=16><b>" + userScore + "</b></font><br><font size=4><p></p></font>";
			} else {
				beforeName += userName + "<br><font size=4><p></p></font>";
				beforeScore += userScore + "<br><font size=4><p></p></font>";
			}
			
		}
		
		beforeName += "</html>";
		beforeScore += "</html>";
		
		rankingName.setText(beforeName);
		rankingScore.setText(beforeScore);
		
		s.close();
		rs.close();
		connection.close();
	}
}

class JPanel05 extends JPanel { // gameMain page
	public Connection connection;
	private JButton check, pass, ranking, restart;
	private JLabel bg, overBg, showCont, content, p1, p2, p3, warning, correct, incorrect;
	private JTextField input;
	private Main one;
	private int score = 0, passChk = 0, rand_index = 0, i = 0, time = 70;
	private String inputText;
	private String nara, sudo, name;

	public JPanel05(Main one) {
		this.one = one;
		setLayout(null);
		
		bg = new JLabel(new ImageIcon("img/board.png"));
		bg.setBounds(0, 0, 1920, 1080);
		
		overBg = new JLabel(new ImageIcon("img/gameover.png"));
		overBg.setBounds(0, 0, 1920, 1080);
		overBg.setVisible(false);
		
		ranking = new JButton(new ImageIcon("img/rankingBtn.png"));
		ranking.setBounds(790, 600, 270, 50);
		ranking.setBorderPainted(false);
		ranking.setFocusPainted(false);
		ranking.setContentAreaFilled(false);
		ranking.setRolloverIcon(new ImageIcon("img/rankingBtn_hover.png"));
		ranking.setVisible(false);
		
		restart = new JButton(new ImageIcon("img/restartBtn.png"));
		restart.setBounds(790, 710, 270, 50);
		restart.setBorderPainted(false);
		restart.setFocusPainted(false);
		restart.setContentAreaFilled(false);
		restart.setRolloverIcon(new ImageIcon("img/restartBtn_hover.png"));
		restart.setVisible(false);
		
		warning = new JLabel(new ImageIcon("img/passWarning.png"));
		warning.setBounds(0, 0, 1920, 1080);
		warning.setVisible(false);
		
		correct = new JLabel(new ImageIcon("img/correct.png"));
		correct.setBounds(700, 300, 618, 517);
		correct.setVisible(false);
		
		incorrect = new JLabel(new ImageIcon("img/incorrect.png"));
		incorrect.setBounds(300, 200, 516, 397);
		incorrect.setVisible(false);
		
		p1 = new JLabel(new ImageIcon("img/passIcon.png"));
		p1.setBounds(730, 30, 100, 75);
		p2 = new JLabel(new ImageIcon("img/passIcon.png"));
		p2.setBounds(880, 30, 100, 75);
		p3 = new JLabel(new ImageIcon("img/passIcon.png"));
		p3.setBounds(1030, 30, 100, 75);
		content = new JLabel(new ImageIcon("img/content.png"));
		content.setBounds(890, 340, 700, 100);
		
		showCont = new JLabel("");
		showCont.setFont(new Font("�������", Font.PLAIN, 80));
		showCont.setHorizontalAlignment(showCont.CENTER);
		showCont.setBounds(385, 340, 700, 100);
		
		input = new JTextField(16) {
			public void setBorder(Border border) { }
		};
		input.setFont(input.getFont().deriveFont(Font.PLAIN, 60f));
		input.setBounds(460, 475, 1000, 130);
		input.setHorizontalAlignment(input.CENTER);
		
		check = new JButton(new ImageIcon("img/checkBtn.png"));
		check.setBounds(730, 640, 210, 210);
		check.setBorderPainted(false);
		check.setFocusPainted(false);
		check.setContentAreaFilled(false);
		check.setRolloverIcon(new ImageIcon("img/checkBtn_hover.png"));
		
		pass = new JButton(new ImageIcon("img/passBtn.png"));
		pass.setBounds(960, 640, 210, 210);
		pass.setBorderPainted(false);
		pass.setFocusPainted(false);
		pass.setContentAreaFilled(false);
		pass.setRolloverIcon(new ImageIcon("img/passBtn_hover.png"));
		
		add(warning);
		add(ranking);
		add(restart);
		add(overBg);
		add(correct);
		add(incorrect);
		add(p1); add(p2); add(p3);
		add(check);
		add(pass);
		add(content);
		add(input);
		add(showCont);
		add(bg);
		
		ranking.addActionListener(new MyActionListener());
		restart.addActionListener(new MyActionListener());
		check.addActionListener(new MyActionListener());
		pass.addActionListener(new MyActionListener());
		
		Test();
		timer.scheduleAtFixedRate(task, 1500, 1000);
		
		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					check();
				}
			}
		 });
		   
	}
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new java.awt.Font("�������",Font.PLAIN,60));
		g.drawString(score+"", 450, 93);
		g.drawString(time+"", 1580, 94);
	}
	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == ranking) {
				one.change("panel04");
			} else if(e.getSource() == restart) {
				one.change("panel01");
			} else if(e.getSource() == check) {
				check();
			} else if(e.getSource() == pass) {
				passChk();
			}
		}
	}
	Timer timer = new Timer();
	TimerTask task = new TimerTask( ) {
		@Override
		public void run() {
			if(time >= 1) {
				time--;
				repaint();
			} else {
				timer.cancel();
				overBg.setVisible(true);
				ranking.setVisible(true);
				restart.setVisible(true);
				check.setEnabled(false);
				pass.setEnabled(false);
				input.setEnabled(false);
				try {
					connectionDB();
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	public void Test() {
		String nara[] = { "����", "����", "���̾Ƴ�", "�����", "���׸���", "�׷�����", "�׸���", "���", "��Ϻ���"
				, "���̺��", "�����", "����������", "�״�����", "����", "�븣����", "��������", "������", "��ī���"
				, "���ѹα�", "����ũ", "���̴�ī", "����", "��Ƽ��"
				, "�����", "���̺�����", "��Ʈ���", "���þ�", "���ٳ�", "������", "�縶�Ͼ�", "����θ�ũ", "���ϴ�", "�����", "�����ƴϾ�", "�����ٽ�Ÿ��"
				, "���ٰ���ī��", "���� ����", "������", "�����̽þ�", "����", "�߽���", "����", "�����"
				, "�𸮼Ž�", "��Ÿ��", "�����ũ", "���׳ױ׷�", "������", "�����", "��Ÿ", "����", "�̱�", "�̾Ḷ"
				, "�ٴ�����", "�ٷ���", "�ٺ��̵���", "��Ƽĭ �ñ�", "���ϸ�", "��۶󵥽�", "����", "���׼�����", "��Ʈ��", "���⿡", "����罺", "������"
				, "�����ͳ�", "�������", "�η��", "�θ�Ű���ļ�", "��ź", "�Ұ�����", "�����", "��糪��"
				, "����", "����ƶ���", "�긶����", "������ ��������", "���װ�", "�������", "���̼�", "����Ʈ��þ�", "�Ҹ�����"
				, "�ַθ� ����", "����", "������", "������ī", "����������", "������", "������", "������", "���ι�Ű��", "���κ��Ͼ�", "�ø���", "�ÿ��󸮿�", "�̰�����"
				, "�ƶ����̸�Ʈ", "�Ƹ��޴Ͼ�", "�Ƹ���Ƽ��", "���̽�����", "����Ƽ", "���Ϸ���", "������������", "�������Ͻ�ź", "�ȵ���", "�˹ٴϾ�", "������", "�Ӱ��", "��Ƽ�� �ٺδ�"
				, "����Ʈ����", "������Ͼ�", "���⵵��", "��Ƽ���Ǿ�", "����ٵ���", "����", "����", "����", "����Ʈ���ϸ���", "����Ʈ����", "�µζ�", "�丣��", "�찣��", "������"
				, "���Ű��ź", "��ũ���̳�", "�̶�ũ", "�̶�", "�̽���", "����Ʈ", "��Ż����", "�ε�", "�ε��׽þ�", "�Ϻ�"
				, "�ڸ���ī", "����", "���� ���", "������(����)", "��ȭ�α�", "��ȭ�ιΰ�ȭ��", "����Ƽ", "���ٺ��"
				, "����", "ü��", "ĥ��"
				, "ī�޷�", "ī��������", "ī���彺ź", "īŸ��", "į�����", "ĳ����", "�ɳ�", "�ڸ��", "�ڽ�Ÿ��ī", "��Ʈ��ξƸ�", "�ݷҺ��", "��� ��ȭ��"
				, "���", "�����Ʈ", "ũ�ξ�Ƽ��", "Ű���⽺��ź", "Ű���ٽ�", "Ű���ν�"
				, "Ÿ��", "Ÿ��Ű��ź", "ź�ڴϾ�", "��Ű", "���", "�밡", "����ũ�޴Ͻ�ź", "���߷�", "Ƣ����", "Ʈ���ϴٵ� ��ٰ�"
				, "�ĳ���", "�Ķ����", "��Ű��ź", "��Ǫ�� �����", "�ȶ��", "���", "��������", "������", "������", "����", "�ɶ���", "�ʸ���", "�밡��" };
		String sudo[] = { "��ũ��", "���긣��", "����Ÿ��", "����", "���׸���", "����Ʈ������", "���׳�", "�ڳ�Ŭ", "����"
				, "��Ʈ��ũ", "�߷�", "�ƺ���", "�Ͻ��׸���", "īƮ����", "������", "������", "�ϾƸ�", "������"
				, "����", "�꺥�Ͽ�", "���䵵�ְ�", "������", "����"
				, "��Ƽ��", "��κ��", "����", "��ũ��", "���̷�Ʈ", "������", "������Ƽ", "���θ�", "Ű����", "Ʈ������", "���Ͽ콺", "�ٵ���"
				, "��Ÿ��������", "���ַ�", "���ձ�", "��˶��Ǫ��", "�ٸ���", "�߽��ڽ�Ƽ", "����", "���Ʈ"
				, "��Ʈ���̽�", "���Ǽ�Ʈ", "��Ǫ��", "�������", "Ű�ó���", "����", "�߷�Ÿ", "������丣", "������ D.C.", "���ǵ�"
				, "��Ʈ ����", "������", "�긮��Ÿ��", "��Ƽĭ �ñ�", "�����", "��ī", "������뺸", "ī��ī��", "�ϳ���", "���", "�ν�ũ", "������"
				, "�����γ�", "���Ľ�", "���ܺζ�", "�Ͱ��α�", "����", "���Ǿ�", "���������", "�ݴٸ������갡��"
				, "���Ǿ�", "���ߵ�", "�긶����", "������", "��ī��", "�����׶��", "���丮��", "ĳ��Ʈ����", "�𰡵�"
				, "ȣ�Ͼƶ�", "ī����", "�Ķ󸶸���", "�ݷҺ�", "���ٹٳ�", "����Ȧ��", "����", "���帮��", "���Ƽ�����", "������", "�ٸ�����", "����Ÿ��", "�̰�����"
				, "�ƺδٺ�", "������", "�ο��뽺���̷���", "����ļ��ũ", "������������", "����", "����", "ī��", "�ȵ���󺣾�", "Ƽ��", "����", "��ȴ�", "����Ʈ����"
				, "�ƽ�����", "Ż��", "Ű��", "�Ƶ𽺾ƹٹ�", "���ٵ���", "����", "�糪", "����īƮ", "ĵ����", "��", "�ױ��ð���", "�ϸ�", "į�ȶ�", "���׺񵥿�"
				, "Ÿ����Ʈ", "Ű����", "�ٱ״ٵ�", "�����", "����췽", "ī�̷�", "�θ�", "������", "��ī��Ÿ", "����"
				, "ŷ����", "���ī", "����", "Ʈ������", "Ÿ�̺���", "����¡", "����Ƽ", "�϶�"
				, "���ڸ޳�", "������", "��Ƽ�ư�"
				, "�߿", "�����̾�", "������ź", "����", "������", "��Ÿ��", "���̷κ�", "��δ�", "��ȣ��", "�߹���ũ��", "��Ÿ�䵥����Ÿ", "����ں�"
				, "�ƹٳ�", "�����Ʈ", "�ڱ׷���", "����ũ", "Ÿ���", "���ڽÿ�"
				, "����", "�μ���", "������", "��ī��", "�θ�", "����˷���", "�ƽ��Ϲ�Ʈ", "Ǫ��ǪƼ", "Ƣ�Ͻ�", "��Ʈ���꽺����"
				, "�ĳ�����Ƽ", "�Ƽ��ÿ�", "�̽��󸶹ٵ�", "��Ʈ�����", "�᷹�ɿ�ũ", "����", "������", "�ٸ�����", "�ĸ�", "����", "���Ű", "���Ҷ�", "�δ��佺Ʈ"
				};
		List<Object> check = new ArrayList<Object>();
		
		rand_index = (int) (Math.random()*184);
		check.add(rand_index);
		
		rand_index = (int) (Math.random()*184);
		if(!check.contains(rand_index)) {
			check.add(rand_index);
		}
		
		showCont.setText(nara[rand_index]);
		this.nara = nara[rand_index];
		this.sudo = sudo[rand_index];
	}
	public void check() {
		inputText = input.getText();
		inputText = inputText.replaceAll(" ", "");
		sudo = sudo.replaceAll(" ", "");
		if(inputText.equals(sudo)) {
			correct.setVisible(true);
			score += 10;
			repaint();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					correct.setVisible(false);
				}
			}, 600);
		} else {
			incorrect.setVisible(true);
			time -= 3;
			repaint();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					incorrect.setVisible(false);
				}
			}, 600);
		}
		
		input.setText("");
		input.requestFocus();
		
		Test();
	}
	public void passChk() {
		passChk++;
		if(passChk == 1) {
			p3.setVisible(false);
			Test();
		} else if(passChk == 2) {
			p2.setVisible(false);
			Test();
		} else if(passChk == 3) {
			p1.setVisible(false);
			Test();
		} else {
			timer.cancel();
			warning.setVisible(true);
			
			timer = new Timer();
			TimerTask task = new TimerTask( ) {
				@Override
				public void run() {
					warning.setVisible(false);
					pass.setEnabled(false);
					if(time >= 1) {
						time--;
						repaint();
					} else {
						timer.cancel();
						overBg.setVisible(true);
						ranking.setVisible(true);
						restart.setVisible(true);
						check.setEnabled(false);
						pass.setEnabled(false);
						input.setEnabled(false);
						try {
							connectionDB();
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
						}
					}
				}
			};
			timer.schedule(task, 2000, 1000);
		}
	}
	public void connectionDB() throws ClassNotFoundException, SQLException {
		try {
			name = currentUser();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:itshow.db");
		
		PreparedStatement ps = connection.prepareStatement("INSERT INTO userTbl VALUES(?, ?)");
		ps.setString(1, name);
		ps.setInt(2, score);
		
		int res = ps.executeUpdate();
		if(res == 1) System.out.println(String.format("%d row insert success", res));
		
		ps.close();
		connection.close();
	}
	public String currentUser() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("C:/java_study/ITshow/file/userName.txt"));
		String name = br.readLine();
		
		return name;
	}
}

public class Main extends JFrame {
	public JPanel01 jpanel01 = null;
	public JPanel02 jpanel02 = null;
	public JPanel04 jpanel04 = null;
	public JPanel05 jpanel05 = null;
	
	public void change(String panelName) {
		getContentPane().removeAll();
		
		if(panelName.equals("panel01")) {
			getContentPane().add(jpanel01);
		} else if(panelName.equals("panel02")) {
			getContentPane().add(jpanel02);
		} else if(panelName.equals("panel04")) {
			getContentPane().add(jpanel04);
		} else if(panelName.equals("panel05")) {
			getContentPane().add(jpanel05);
		}
		
		revalidate();
		repaint();
	}

	public static void main(String[] args) {
		Main one = new Main();
		
		one.setTitle("2019 IT SHOW EVENT");
		one.jpanel01 = new JPanel01(one); // main
		one.jpanel02 = new JPanel02(one); // explain
		one.jpanel04 = new JPanel04(one); // ranking
		one.jpanel05 = new JPanel05(one); // game
		
		one.add(one.jpanel04);
		one.setDefaultCloseOperation(EXIT_ON_CLOSE);
		one.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		one.setVisible(true);
	}

}
