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
		
		StartBtn.setRolloverIcon(new ImageIcon("img/startBtn_hover.png")); // hover 역할
		
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
				name.setText("이름을 입력해주세요");
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
		rankingName.setFont(new Font("나눔고딕", Font.PLAIN, 26));
		//rankingName.setBorder(new LineBorder(Color.black));
		rankingName.setBounds(460, 300, 355, 600);
		
		rankingScore = new JLabel();
		rankingScore.setFont(new Font("나눔고딕", Font.PLAIN, 26));
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
		showCont.setFont(new Font("나눔고딕", Font.PLAIN, 80));
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
		g.setFont(new java.awt.Font("나눔고딕",Font.PLAIN,60));
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
		String nara[] = { "가나", "가봉", "가이아나", "감비아", "과테말라", "그레나다", "그리스", "기니", "기니비사우"
				, "나미비아", "나우루", "나이지리아", "네덜란드", "네팔", "노르웨이", "뉴질랜드", "니제르", "니카라과"
				, "대한민국", "덴마크", "도미니카", "독일", "동티모르"
				, "라오스", "라이베리아", "라트비아", "러시아", "레바논", "레소토", "루마니아", "룩셈부르크", "르완다", "리비아", "리투아니아", "리히텐슈타인"
				, "마다가스카르", "마셜 제도", "말라위", "말레이시아", "말리", "멕시코", "모나코", "모로코"
				, "모리셔스", "모리타니", "모잠비크", "몬테네그로", "몰도바", "몰디브", "몰타", "몽골", "미국", "미얀마"
				, "바누아투", "바레인", "바베이도스", "바티칸 시국", "바하마", "방글라데시", "베냉", "베네수엘라", "베트남", "벨기에", "벨라루스", "벨리즈"
				, "보츠와나", "볼리비아", "부룬디", "부르키나파소", "부탄", "불가리아", "브라질", "브루나이"
				, "사모아", "사우디아라비아", "산마리노", "상투메 프린시페", "세네갈", "세르비아", "세이셸", "세인트루시아", "소말리아"
				, "솔로몬 제도", "수단", "수리남", "스리랑카", "스와질란드", "스웨덴", "스위스", "스페인", "슬로바키아", "슬로베니아", "시리아", "시에라리온", "싱가포르"
				, "아랍에미리트", "아르메니아", "아르헨티나", "아이슬란드", "아이티", "아일랜드", "아제르바이잔", "아프가니스탄", "안도라", "알바니아", "알제리", "앙골라", "앤티가 바부다"
				, "에리트레아", "에스토니아", "에콰도르", "에티오피아", "엘살바도르", "영국", "예멘", "오만", "오스트레일리아", "오스트리아", "온두라스", "요르단", "우간다", "우루과이"
				, "우즈베키스탄", "우크라이나", "이라크", "이란", "이스라엘", "이집트", "이탈리아", "인도", "인도네시아", "일본"
				, "자메이카", "잠비아", "적도 기니", "조지아(국가)", "중화민국", "중화인민공화국", "지부티", "짐바브웨"
				, "차드", "체코", "칠레"
				, "카메룬", "카보베르데", "카자흐스탄", "카타르", "캄보디아", "캐나다", "케냐", "코모로", "코스타리카", "코트디부아르", "콜롬비아", "콩고 공화국"
				, "쿠바", "쿠웨이트", "크로아티아", "키르기스스탄", "키리바시", "키프로스"
				, "타이", "타지키스탄", "탄자니아", "터키", "토고", "통가", "투르크메니스탄", "투발루", "튀니지", "트리니다드 토바고"
				, "파나마", "파라과이", "파키스탄", "파푸아 뉴기니", "팔라우", "페루", "포르투갈", "폴란드", "프랑스", "피지", "핀란드", "필리핀", "헝가리" };
		String sudo[] = { "아크라", "리브르빌", "조지타운", "반줄", "과테말라", "세인트조지스", "아테네", "코나클", "비사우"
				, "빈트후크", "야렌", "아부자", "암스테르담", "카트만두", "오슬로", "웰링턴", "니아메", "마나과"
				, "서울", "쾨벤하운", "산토도밍고", "베를린", "딜리"
				, "비엔티안", "몬로비아", "리가", "모스크바", "베이루트", "마세루", "부쿠레슈티", "뤽상부르", "키갈리", "트리폴리", "빌니우스", "바두츠"
				, "안타나나리보", "마주로", "릴롱궤", "쿠알라룸푸르", "바마코", "멕시코시티", "모나코", "라바트"
				, "포트루이스", "누악쇼트", "마푸투", "포드고리차", "키시나우", "말레", "발레타", "울란바토르", "워싱턴 D.C.", "네피도"
				, "포트 빌라", "마나마", "브리지타운", "바티칸 시국", "나사우", "다카", "포르토노보", "카라카스", "하노이", "브뤼셀", "민스크", "벨모판"
				, "가보로네", "라파스", "부줌부라", "와가두구", "팀부", "소피아", "브라질리아", "반다르스리브가완"
				, "아피아", "리야드", "산마리노", "상투메", "다카르", "베오그라드", "빅토리아", "캐스트리스", "모가디슈"
				, "호니아라", "카르툼", "파라마리보", "콜롬보", "음바바네", "스톡홀름", "베른", "마드리드", "브라티슬라바", "류블랴나", "다마스쿠스", "프리타운", "싱가포르"
				, "아부다비", "예레반", "부에노스아이레스", "레이캬비크", "포르토프랭스", "더블린", "바쿠", "카불", "안도라라베야", "티라나", "알제", "루안다", "세인트존스"
				, "아스마라", "탈린", "키토", "아디스아바바", "산살바도르", "런던", "사나", "무스카트", "캔버라", "빈", "테구시갈파", "암만", "캄팔라", "몬테비데오"
				, "타슈켄트", "키예프", "바그다드", "테헤란", "예루살렘", "카이로", "로마", "뉴델리", "자카르타", "도쿄"
				, "킹스턴", "루사카", "말라보", "트빌리시", "타이베이", "베이징", "지부티", "하라레"
				, "은자메나", "프라하", "산티아고"
				, "야운데", "프라이아", "누르술탄", "도하", "프놈펜", "오타와", "나이로비", "모로니", "산호세", "야무수크로", "산타페데보고타", "브라자빌"
				, "아바나", "쿠웨이트", "자그레브", "비슈케크", "타라와", "니코시와"
				, "방콕", "두샨베", "도도마", "앙카라", "로메", "누쿠알로파", "아슈하바트", "푸나푸티", "튀니스", "포트오브스페인"
				, "파나마시티", "아순시온", "이슬라마바드", "포트모르즈비", "멜레케오크", "리마", "리스본", "바르샤바", "파리", "수바", "헬싱키", "마닐라", "부다페스트"
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
