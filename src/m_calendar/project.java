package m_calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class project extends CalendarDataManager implements ActionListener{
	//캘린더를 출력하기 위한 요소들
	JPanel calOpPanel;
	JButton todayBut;
	JLabel todayLab;
	JButton lYearBut;
	JButton lMonBut;
	JLabel curMMYYYYLab;
	JButton nMonBut;
	JButton nYearBut;
	ListenForCalOpButtons lForCalOpButtons = new ListenForCalOpButtons();

	JPanel calPanel;
	JButton weekDaysName[];
	JButton dateButs[][] = new JButton[6][7];
	listenForDateButs lForDateButs = new listenForDateButs(); 

	JPanel infoPanel;
	JLabel infoClock;


	JLabel selectedDate;

//상수, 메세지
	final String WEEK_DAY_NAME[] = { "일", "월", "화", "수", "목", "금", "토" };
	public JPanel p;
	public JTable outtable; //지출내역테이블
	public JComboBox comboBox; //수입 내역 콤보박스
	public JComboBox comboBoxx; //지출 내역 콤보박스
	public JRadioButton rbin; // 수입 라디오 버튼
	public JRadioButton rbout; // 지출 라디오 버튼
	public int indexx; // 수입 콤보박스에서 선택한 요소를 저장하는 변수
	public int outxx;//지출 콤보박스에서 선택한 요소를 저장하는 변수
	public JTextField memotext; // 메모를 입력하는 텍스트 필드
	public JTextField cashtext; // 금액을 입력하는 텍스트필드
	public JTable moneytable; // 전체내역 테이블
	public JTable inputtable;//수입내역테이블
	public int[][] in = {{0,0}};
	public int[][] out = {{0,0}};
	int total_m = 0;//잔액
	int inz = 0; //수입 num
	int outz = 0; //지출 num
	int tz = 0; //종합 num
	
	String num; //수입횟수 저장
	String nn; //지출횟수 저장
	String tt; //총 입력 횟수 저장
	String moneyy; //잔액
	String[][][][] datai = new String[50][50][50][50]; // 수입 입력 내용을 저장하는 배열
	String[][][][] datao = new String[50][50][50][50]; // 지출 입력 내용을 저장하는 배열
	String[][][][] datat = new String[50][50][50][50]; // 총 입력 내용을 저장하는 배열
	
	String header[] = {"NO", "항목", "MEMO", "결제수단", "금액"}; //테이블 헤더-수입,지출
	DefaultTableModel model = new DefaultTableModel(header, 0); //수입내역 테이블
	DefaultTableModel modelo = new DefaultTableModel(header, 0); // 지출 내역 테이블
	String headerr[] = {"  N  O  ", "  날  짜  ", "  항  목  ", "  M E M O  ", "  결 제 수 단  ", "  수  입  ", "  지  출  ", "  잔  액  "}; //테이블 헤더-총 내역
	DefaultTableModel modell = new DefaultTableModel(headerr, 0);//총 내역 테이블
	
	public String datee; // 선택한 날짜 저장하는 변수
	public JFrame f = new JFrame(); // 메인 프레임
	
	public project() {
		f.setSize(1250,598);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setTitle("-ㅁ- 가계부입니다");
	    
	    p = new JPanel();
	    p.setLayout(null);
	    /*
		JButton chart = new JButton("\uCC28\uD2B8"); //차트버튼
		p.add(chart);
		chart.setBounds(43, 10, 114, 32);
		*/
		JButton exit = new JButton("\uC885\uB8CC"); //종료버튼
		exit.setFont(new Font("HY나무M", Font.PLAIN, 11));
		p.add(exit);
		exit.setBounds(750, 6, 60, 22);
		
		JLabel lblNewLabel = new JLabel("< \uAC00\uACC4\uBD80 \uC785\uB825 >"); //<가계부입력>
		p.add(lblNewLabel);
		lblNewLabel.setFont(new Font("HY나무M", Font.BOLD, 17));
		lblNewLabel.setBounds(120, 52, 180, 32);
		
		JLabel date = new JLabel("* \uB0A0         \uC9DC  : ");//날짜
		p.add(date);
		date.setFont(new Font("HY나무L", Font.BOLD, 13));
		date.setBounds(23, 86, 108, 32);
		
		JLabel choice = new JLabel("* \uAD6C         \uBD84  : "); //수입,지출 구분
		choice.setFont(new Font("HY나무L", Font.BOLD, 13));
		choice.setBounds(23, 118, 108, 32);
		p.add(choice);
		
		JLabel index = new JLabel("* \uD56D         \uBAA9  : "); //항목선택
		index.setFont(new Font("HY나무L", Font.BOLD, 13));
		index.setBounds(23, 153, 108, 32);
		p.add(index);
		
		JLabel memo = new JLabel("* \uD56D \uBAA9 \uC138 \uBD80  : "); //메모
		memo.setFont(new Font("HY나무L", Font.BOLD, 13));
		memo.setBounds(23, 186, 108, 32);
		p.add(memo);
		
		JLabel way = new JLabel("* \uACB0 \uC81C \uC218 \uB2E8  : "); //결제수단(현금,카드)
		way.setFont(new Font("HY나무L", Font.BOLD, 13));
		way.setBounds(23, 219, 108, 32);
		p.add(way);
		
		JLabel cash = new JLabel("* \uAE08         \uC561  : "); // 금액
		cash.setFont(new Font("HY나무L", Font.BOLD, 13));
		cash.setBounds(23, 250, 108, 32);
		p.add(cash);
		
		rbin = new JRadioButton("\uC218\uC785"); //수입 라디오버튼
		rbin.setFont(new Font("HY나무L", Font.BOLD, 11));
		rbin.setBounds(139, 123, 50, 23);
		p.add(rbin);
		rbin.addActionListener(this);
		
		rbout = new JRadioButton("\uC9C0\uCD9C"); //지출 라디오버튼
		rbout.setFont(new Font("HY나무L", Font.BOLD, 11));
		rbout.setBounds(222, 123, 50, 23);
		p.add(rbout);
		rbout.addActionListener(this);
		
		JRadioButton rbcash = new JRadioButton("\uD604\uAE08"); //현금 라디오버튼
		rbcash.setFont(new Font("HY나무L", Font.BOLD, 11));
		rbcash.setBounds(139, 224, 50, 23);
		p.add(rbcash);
		rbcash.addActionListener(this);
		
		JRadioButton rbcard = new JRadioButton("\uCE74\uB4DC"); //카드 라디오버튼
		rbcard.setFont(new Font("HY나무L", Font.BOLD, 11));
		rbcard.setBounds(232, 224, 50, 23);
		p.add(rbcard);
		rbcard.addActionListener(this);
		
		JButton save = new JButton("\uC785    \uB825"); // 입력 버튼
		save.setFont(new Font("HY나무L", Font.BOLD, 13));
		save.setBounds(74, 292, 88, 32);
		p.add(save);
		
		JButton cancel = new JButton("\uCD08 \uAE30 \uD654"); // 초기화 버튼
		cancel.setFont(new Font("HY나무L", Font.BOLD, 13));
		cancel.setBounds(189, 292, 88, 32);
		p.add(cancel);
		
		JLabel label = new JLabel("* \uC218\uC785\uB0B4\uC5ED"); //수입 내역 테이블 라벨
		label.setFont(new Font("HY나무M", Font.BOLD, 17));
		label.setBounds(348, 20, 123, 32);
		p.add(label);
		
		JLabel label_1 = new JLabel("* \uC9C0\uCD9C\uB0B4\uC5ED"); //지출 내역 테이블 라벨
		label_1.setFont(new Font("HY나무M", Font.BOLD, 17));
		label_1.setBounds(348, 185, 123, 32);
		p.add(label_1);
		
		ButtonGroup bg = new ButtonGroup(); //수입,지출 라디오 버튼 그룹화
	    ButtonGroup ag = new ButtonGroup(); //현금,카드 라디오 버튼 그룹화
	    
	    bg.add(rbin);
	    bg.add(rbout);
	      
	    ag.add(rbcash);
	    ag.add(rbcard);
	    
		String[] input_index = {"용 돈", "월 급", "장학금", "기 타"}; //combobox 내역-수입
	    String[] output_index = {"식사 ", "교 통", "여 가", "공과금", "저 축", "기 타"}; //지출-
	      
		comboBox = new JComboBox(input_index); // 항목 콤보박스
		comboBox.setBounds(124, 159, 90, 20);
		p.add(comboBox);
		
		//콤보박스 이벤트 (항목 선택)
	    comboBox.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            JComboBox jcb = (JComboBox)e.getSource();
	            indexx = jcb.getSelectedIndex(); //콤보박스에서 선택 값을 불러옴
	         }
	      });
	      
		String header[] = {"NO" ,"항목", "MEMO", "결제수단", "금액"}; //테이블 헤더
		DefaultTableModel model = new DefaultTableModel(header, 0); //테이블에 행단위로 쓰거나 삭제가 용이하도록 DefaultTableModel 사용
		inputtable = new JTable(model);
		inputtable.setFillsViewportHeight(true);//컨테이너의 전체 높이를 테이블이 전부 사용하도록 
		inputtable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
	      //한번에 하나의 항목을 선택
		inputtable.getTableHeader().setReorderingAllowed(false);//테이블의 헤더를 고정
		JScrollPane scrollPane_1 = new JScrollPane(inputtable); //JScrollPane 클래스로 객체를 생성 - 입력 값에 따른 스크롤 사용이 가능하도록 
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);// 수직 스크롤 항상 표시
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS); //수평 스크롤 항상 표시
		scrollPane_1.setViewportView(inputtable);
		scrollPane_1.setBounds(348, 62, 459, 115);
		p.add(scrollPane_1);
		
		DefaultTableModel modelo = new DefaultTableModel(header, 0);
		outtable = new JTable(modelo); // 지출 내역 입력 테이블
		outtable.setFillsViewportHeight(true);
		outtable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane_2 = new JScrollPane(outtable); //JScrollPane 클래스로 객체를 생성 
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_2.setViewportView(outtable);
		scrollPane_2.setBounds(346, 228, 461, 115);
		//scrollPane_2.setWheelScrollingEnabled( true );
		p.add(scrollPane_2);

		memotext = new JTextField(); //메모 입력 텍스트 필드 생성
		memotext.setBounds(123, 192, 148, 21);
		p.add(memotext);
		memotext.setColumns(10);
		
		cashtext = new JTextField(); //금액 입력 텍스트 필드 생성
		cashtext.setBounds(123, 256, 148, 21);
		p.add(cashtext);
		cashtext.setColumns(10);
		
		JLabel label_2 = new JLabel("* \uAC00\uACC4\uBD80\uB0B4\uC5ED"); //'*가계부 내역' 표시 라벨
		label_2.setFont(new Font("HY나무M", Font.BOLD, 17));
		label_2.setBounds(23, 348, 123, 32);
		p.add(label_2);
		
		JButton delete = new JButton("\uC0AD\uC81C"); //삭제 버튼
		delete.setFont(new Font("HY나무L", Font.BOLD, 12));
		delete.setBounds(137, 354, 91, 23);
		p.add(delete);
		
		JButton button = new JButton("내역확인"); //내역 확인 버튼
		button.setFont(new Font("HY나무L", Font.BOLD, 12));
		button.setBounds(476, 354, 91, 23);
		p.add(button);
		
		JButton deleteall = new JButton("\uC804\uCCB4\uC0AD\uC81C"); //전체 삭제 버튼
		deleteall.setFont(new Font("HY나무L", Font.BOLD, 12));
		deleteall.setBounds(252, 354, 91, 23);
		p.add(deleteall);
		
		JButton savef = new JButton("\uC800\uC7A5"); //저장버튼
		savef.setFont(new Font("HY나무L", Font.BOLD, 12));
		savef.setBounds(366, 354, 91, 23);
		p.add(savef);
		
		String headerr[] = {"  N  O  ", "  날  짜  ", "  항  목  ", "  M E M O  ", "  결 제 수 단  ", "  수  입  ", "  지  출  ", "  잔  액  "}; //테이블 헤더
		DefaultTableModel modell = new DefaultTableModel(headerr, 0); //가계부 전체 내역 테이블
		moneytable = new JTable(modell);
		moneytable.setFillsViewportHeight(true);
		moneytable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane_ = new JScrollPane(moneytable); //JScrollPane 클래스로 객체를 생성 
		scrollPane_.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_.setViewportView(moneytable);
		
		scrollPane_.setBounds(23, 390, 784, 147);
		p.add(scrollPane_);
		
		// 캘린더 구성
		// 캘린더의 년도, 월 선택 버튼
		calOpPanel = new JPanel();
		todayBut = new JButton("Today");
		todayBut.setToolTipText("Today");
		todayBut.addActionListener(lForCalOpButtons);
		todayLab = new JLabel("      " + (today.get(Calendar.MONTH) + 1) + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR));
		lYearBut = new JButton("<<");
		lYearBut.setToolTipText("Previous Year");
		lYearBut.addActionListener(lForCalOpButtons);
		lMonBut = new JButton("<");
		lMonBut.setToolTipText("Previous Month");
		lMonBut.addActionListener(lForCalOpButtons);
		curMMYYYYLab = new JLabel("<html><table width=100><tr><th><font size=5>" + ((calMonth + 1) < 10 ? "&nbsp;" : "") + (calMonth + 1)  + " / " + calYear + "</th></tr></table></html>");
		nMonBut = new JButton(">");
		nMonBut.setToolTipText("Next Month");
		nMonBut.addActionListener(lForCalOpButtons);
		nYearBut = new JButton(">>");
		nYearBut.setToolTipText("Next Year");
		nYearBut.addActionListener(lForCalOpButtons);
		calOpPanel.setLayout(new GridBagLayout());
		GridBagConstraints calOpGC = new GridBagConstraints(); //gridbaglayou으로 설정된 컨네이너에 부착할 컨포넌트의 속성을 정의
		calOpGC.gridx = 1; //컴포넌트가 위치할 격자의 위치 중 행
		calOpGC.gridy = 1; //컴포넌트가 위치할 격자의 위치 중 열
		calOpGC.gridwidth = 2; //컴포넌트가 가로로 몇 개의 셀을 차지할 것인가
		calOpGC.gridheight = 1; //컴포넌트가 세로로 몇 개의 셀을 차지할 것인가
		calOpGC.weightx = 1; //다른 행들과 비교하여 행의 비를 결정함
		calOpGC.weighty = 1; //다른 열들과 비교하여 열의 비를 결정함
		calOpGC.insets = new Insets(5,5,0,0); //여백 설정
		calOpGC.anchor = GridBagConstraints.WEST; // 컴포넌트 여백의 위치 지정
		calOpGC.fill = GridBagConstraints.NONE; //남는 공간은 채우지 않고 기본 크기를 유지
		calOpPanel.add(todayBut,calOpGC);
		calOpGC.gridwidth = 3;
		calOpGC.gridx = 2;
		calOpGC.gridy = 1;
		calOpPanel.add(todayLab,calOpGC);
		calOpGC.anchor = GridBagConstraints.CENTER;
		calOpGC.gridwidth = 1;
		calOpGC.gridx = 1;
		calOpGC.gridy = 2;
		calOpPanel.add(lYearBut,calOpGC);
		calOpGC.gridwidth = 1;
		calOpGC.gridx = 2;
		calOpGC.gridy = 2;
		calOpPanel.add(lMonBut,calOpGC);
		calOpGC.gridwidth = 2;
		calOpGC.gridx = 3;
		calOpGC.gridy = 2;
		calOpPanel.add(curMMYYYYLab,calOpGC);
		calOpGC.gridwidth = 1;
		calOpGC.gridx = 5;
		calOpGC.gridy = 2;
		calOpPanel.add(nMonBut,calOpGC);
		calOpGC.gridwidth = 1;
		calOpGC.gridx = 6;
		calOpGC.gridy = 2;
		calOpPanel.add(nYearBut,calOpGC);
		//요일 출력 패널
		calPanel = new JPanel();
		weekDaysName = new JButton[7];
		for(int i = 0 ; i < CAL_WIDTH ; i++){
			weekDaysName[i] = new JButton(WEEK_DAY_NAME[i]);
			weekDaysName[i].setBorderPainted(false);
			weekDaysName[i].setContentAreaFilled(true);
			weekDaysName[i].setForeground(Color.WHITE);
			if(i == 0) weekDaysName[i].setBackground(new Color(200, 50, 50));
			else if (i == 6) weekDaysName[i].setBackground(new Color(50, 100, 200));
			else weekDaysName[i].setBackground(new Color(150, 150, 150));
			weekDaysName[i].setOpaque(true);
			weekDaysName[i].setFocusPainted(false);
			calPanel.add(weekDaysName[i]);
		}
		for(int i = 0 ; i < CAL_HEIGHT; i++){
			for(int j = 0; j < CAL_WIDTH; j++){
				dateButs[i][j] = new JButton();
				dateButs[i][j].setBorderPainted(false);
				dateButs[i][j].setContentAreaFilled(false);
				dateButs[i][j].setBackground(Color.WHITE);
				dateButs[i][j].setOpaque(true);
				dateButs[i][j].addActionListener(lForDateButs);
				calPanel.add(dateButs[i][j]);
			}
		}
		calPanel.setLayout(new GridLayout(0,7,1,1));
		calPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		showCal(); // 달력을 표시
					

		// 현재 시간 출력
		infoClock = new JLabel("", SwingConstants.RIGHT); 
		infoClock.setBounds(1000,19,100,50);
		// 선택한 날짜 표시
		selectedDate = new JLabel("<Html><font size=3>" + (today.get(Calendar.YEAR) + "/" + (today.get(Calendar.MONTH) + 1) + "/" + today.get(Calendar.DAY_OF_MONTH) + "&nbsp;(Today)</html>"), SwingConstants.LEFT);
		selectedDate.setLayout(null);
		selectedDate.setBounds(490, 87, 110, 23);
					
		f.add(selectedDate);
		f.add(infoClock);


		//calOpPanel, calPanel을  frameSubPanelWest에 배치
		JPanel frameSubPanelWest = new JPanel();
		Dimension calOpPanelSize = calOpPanel.getPreferredSize();
		calOpPanelSize.height = 60;
		calOpPanel.setPreferredSize(calOpPanelSize);
		frameSubPanelWest.setLayout(new BorderLayout());
		frameSubPanelWest.add(calOpPanel,BorderLayout.NORTH);
		frameSubPanelWest.add(calPanel,BorderLayout.CENTER);
	
		
		//frame에 전부 배치
		f.setLayout(new BorderLayout());
		f.add(frameSubPanelWest, BorderLayout.WEST);
	
		focusToday(); //현재 날짜에 focus를 줌 (mainFrame.setVisible(true) 이후에 배치해야함)
		
		//Thread 작동(시계, bottomMsg 일정시간후 삭제)
		ThreadConrol threadCnl = new ThreadConrol();
		threadCnl.start();	
	
		f.add(p);
		// 초기화 이벤트
		cancel.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent e) {
		    memotext.setText("");
		    cashtext.setText("");
		    rbin.setSelected(false);
		    rbout.setSelected(false);
		    rbcard.setSelected(false);
		    rbcash.setSelected(false);
		 }
		});
		// 종료 이벤트
		exit.addActionListener(new ActionListener() {
	     public void actionPerformed(ActionEvent e) {
	    	 	f.dispose();
	         }
	      });
		// 입력 이벤트
		save.addActionListener(new ActionListener() {
			@Override
	         public void actionPerformed(ActionEvent e) {
				//입력된 값 테이블에 추가하기
	            //입력된 값들을 한줄 데이터 덩어리(배열)로 만들어줘야 함    ←←←
	            String[] rows = new String[6]; //입,출력 내용 저장 배열   
	            String[] rrow = new String[8]; // 총 가계부 내역 저장 배열
	            String SelectDay = calYear + "/" + (calMonth + 1) + "/" + calDayOfMon;
	            // 수입 버튼을 클릭하고 현금 버튼을 클릭했을 때
	            if(rbin.isSelected() && rbcash.isSelected()) {
	            	num = Integer.toString(inz);
	                rows[0] = num; //순서
	                rows[1] = input_index[indexx]; //콤보박스 선택 내용
	                rows[2] =  memotext.getText(); //메모입력내용
	                rows[3] = "현 금";
	                rows[4] = cashtext.getText(); //금액입력내용
	                model.addRow(rows);  
	                for(int z = 0; z < 6; z++) {
	                	datai[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][inz][z] = rows[z]; //입력내용을 배열에 저장
	                }
	                
	                total_m += Integer.parseInt(rows[4]); // 잔액 계산
	                moneyy = Integer.toString(total_m); //잔액
	                tt = Integer.toString(tz); //총 가계부 입력 횟수 계산
	                rrow[0] = tt;
	                rrow[1] = SelectDay; //날짜
	                rrow[2] = input_index[indexx];
	                rrow[3] = memotext.getText();
	                rrow[4] = "현 금";
	                rrow[5] = "수 입";
	                rrow[6] = ".";
	                rrow[7] = moneyy; //잔액
	                modell.addRow(rrow);//전체 테이블에도 입력
	                
	                for(int z = 0; z < 5; z++) {
	                	datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][tz][z] = rrow[z];
	                }
	                
	                inz += 1;
	                tz += 1;
	             }
	            // 수입 버튼을 클릭하고 카드 버튼을 클릭했을 때
	             else if(rbin.isSelected() && rbcard.isSelected()) {
	            	 num = Integer.toString(inz);
		             rows[0] = num; //순서
		             rows[1] = input_index[indexx];
		             rows[2] = memotext.getText();
		             rows[3] = "체크카드";
		             rows[4] = cashtext.getText();
		             model.addRow(rows);
		            
		             for(int z = 0; z < 5; z++) {
		                	datai[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][inz][z]= rows[z];
		                }
		             inz += 1;
		             total_m += Integer.parseInt(rows[4]);
		             moneyy = Integer.toString(total_m);
		             tt = Integer.toString(tz);
		             rrow[0] = tt;
		             rrow[1] = SelectDay; //날짜
		             rrow[2] = input_index[indexx];
		             rrow[3] = memotext.getText();
		             rrow[4] = "카 드";
		             rrow[5] = "수 입";
		             rrow[6] = ".";
		             rrow[7] = moneyy; //잔액
		             modell.addRow(rrow);//전체 테이블에도 입력
		             
		             
		             for(int z = 0; z < 5; z++) {
		            	 datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][tz][z] = rrow[z];
		             }
		             tz += 1;
	             }
	            // 지출 버튼을 클릭하고 현금 버튼을 클릭했을 때
	             else if(rbout.isSelected() && rbcash.isSelected()) {
	            	nn = Integer.toString(outz);
	            	rows[0] = nn;
	                rows[1] = output_index[indexx];
	                rows[2] = memotext.getText();
	                rows[3] = "현 금";
	                rows[4] = cashtext.getText();
	                modelo.addRow(rows);
	               
	                for(int z = 0; z < 5; z++) {
	                	datao[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][outz][z] = rows[z];
	                }
	                outz += 1;
		             total_m -= Integer.parseInt(rows[4]);
		             moneyy = Integer.toString(total_m);
		             tt = Integer.toString(tz);
		             rrow[0] = tt;
		             rrow[1] = SelectDay; //날짜
		             rrow[2] = output_index[indexx];
		             rrow[3] = memotext.getText();
		             rrow[4] = "현 금";
		             rrow[5] = ".";
		             rrow[6] = "지 출";
		             rrow[7] = moneyy; //잔액
		             modell.addRow(rrow);//전체 테이블에도 입력
		             
		             for(int z = 0; z < 5; z++) {
		                	datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][tz][z] = rrow[z];
		                }
		             tz += 1;
	             }
	            // 지출 버튼을 클릭하고 카드 버튼을 클릭했을 때
	             else if(rbout.isSelected() && rbcard.isSelected()){
	            	 nn = Integer.toString(outz);
		             rows[0] = nn;
		             rows[1] = output_index[indexx];
		             rows[2] = memotext.getText();
		             rows[3] = "카 드";
		             rows[4] = cashtext.getText();
	                modelo.addRow(rows);
	                
	                for(int z = 0;z < 5; z++) {
	                	datao[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][outz][z] = rows[z];
	                }
	                outz += 1;
	                total_m -= Integer.parseInt(rows[4]);
		             moneyy = Integer.toString(total_m);
		             tt = Integer.toString(tz);
		             rrow[0] = tt;
		             rrow[1] = SelectDay; //날짜
		             rrow[2] = output_index[indexx];
		             rrow[3] = memotext.getText();
		             rrow[4] = "체크카드";
		             rrow[5] = ".";
		             rrow[6] = "지 출";
		             rrow[7] = moneyy; //잔액
		             modell.addRow(rrow);//전체 테이블에도 입력
		            
		             for(int z = 0; z < 5; z++) {
		                	datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][tz][z] = rrow[z];
		             }
		             tz += 1;
	             }
	             //입력후 텍스트 필드 값 제거
	             memotext.setText("");
	             cashtext.setText("");
	             rbin.setSelected(false); //라디오 초기화
	             rbout.setSelected(false);
	             
	          }
	      });   
		// 삭제 이벤트
		delete.addActionListener(new ActionListener() {
		@Override //moneytable 내역 삭제
         public void actionPerformed(ActionEvent e) {
	           // 전체 가계부 내역에서 선택한 내용과 그에 해당하는 수입내역에서 선택한 행을 삭제 
            if(moneytable.getSelectedRow() != 1 && inputtable.getSelectedRow() != 1) {
            	int row = moneytable.getSelectedRow();
                int col = moneytable.getSelectedColumn();
                Object value = moneytable.getValueAt(row, col);
                modell.removeRow(moneytable.getSelectedRow());
                
            	int rowi = inputtable.getSelectedRow();
                int coli = inputtable.getSelectedColumn();
                Object valuei = inputtable.getValueAt(rowi, coli);
                model.removeRow(inputtable.getSelectedRow());
            }
         // 전체 가계부 내역에서 선택한 내용과 그에 해당하는 지출내역에서 선택한 행을 삭제 
            if(moneytable.getSelectedRow() != 1 && outtable.getSelectedRow() != 1) {
            	int row = moneytable.getSelectedRow();
                int col = moneytable.getSelectedColumn();
                Object value = moneytable.getValueAt(row, col);
                modell.removeRow(moneytable.getSelectedRow());
                
            	int rowo = outtable.getSelectedRow();
                int colo = outtable.getSelectedColumn();
                Object valueo = outtable.getValueAt(rowo, colo);
                modelo.removeRow(outtable.getSelectedRow());
            }
		}
		});
		// 전체삭제 이벤트
		// 수입, 지출, 전체 내역의 모든 내용을 삭제
		deleteall.addActionListener(new ActionListener() {
		@Override
         public void actionPerformed(ActionEvent e) {
			DefaultTableModel remove = (DefaultTableModel)moneytable.getModel();
			remove.setNumRows(0);
			DefaultTableModel removein = (DefaultTableModel)inputtable.getModel();
			removein.setNumRows(0);
			DefaultTableModel removeout = (DefaultTableModel)outtable.getModel();
			removeout.setNumRows(0);
	         }
	      });
		//내역확인이벤트
		// 확인을 원하는 날짜를 누르면 그에 저장되었던 내역을 출력
		button.addActionListener(new ActionListener() {
			@Override
	         public void actionPerformed(ActionEvent e) {
				String[] iii = new String[6];   // 수입 테이블 내역
				String[] oo = new String[6];  //지출 테이블 내역
		        String[] too = new String[8]; //종합 테이블 내역
		     // 해당 날짜에 입력되었던 수입 내역들을 배열로 묶어 수입 테이블에 출력 
			        for(int z = 0; z < datai[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)].length; z++) {
			        	iii[0] = datai[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][0]; //날짜
			            iii[1] = datai[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][1]; //항목
			            iii[2] = datai[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][2];//메모
			            iii[3] = datai[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][3];//결제수단
			            iii[4] = datai[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][4];//금액
			            model.addRow(iii); // 배열의 내용을 수입 테이블에 입력
			        }
			        // 해당 날짜에 입력되었던 지출 내역들을 배열로 묶어 지출 테이블에 출력 
			        for(int z = 0; z < datao[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)].length; z++) {
			        	oo[0] = datao[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][0]; //날짜
			            oo[1] = datao[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][1]; //항목
			            oo[2] = datao[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][2]; //메모
			            oo[3] = datao[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][3]; //결제수단
			            oo[4] = datao[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][4]; //금액
			            modelo.addRow(oo); //배열의 내용을 지출 테이블에 입력
			        }
			     // 해당 날짜에 입력되었던 전체 내역들을 배열로 묶어 전체 테이블에 출력 
			        for(int z = 0; z < 10; z++) {
			        	too[0] = datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][0];
			            too[1] = datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][1];
			            too[2] = datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][2];
			            too[3] = datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][3];
			            too[4] = datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][4];
			            too[5] = datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][5];
			            too[6] = datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][6];
			            too[7] = datat[cal.get(Calendar.MONTH)][cal.get(Calendar.DAY_OF_MONTH)][z][7];
			            modell.addRow(too);
			        }
			       }
		         
		      });
		// 저장 이벤트
		savef.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 File file_i = new File("input"); //파일명-수입내역
	        	 File file_o = new File("output"); //파일명-지출내역
	        	 File file_t = new File("total"); //파일명-종합내역
	        	 if(!file_i.isDirectory()) file_i.mkdir();//해당 파일이 존재하는지 확인하고 존재하지 않으면 파일 생성
	        	 if(!file_o.isDirectory()) file_o.mkdir();
	        	 if(!file_t.isDirectory()) file_t.mkdir();
	        	 BufferedWriter writeri;
	        	 BufferedWriter writero;
	        	 BufferedWriter writert;
	        	 
	        	 int readLinei = 0;
	        	 int readLineo = 0;
	        	 int readLinet = 0;
	        	 
				try {
					writeri = new BufferedWriter(new FileWriter("input/" + calYear + ((calMonth + 1) < 10 ? "0" : "") + (calMonth + 1) + (calDayOfMon < 10 ? "0" : "") + calDayOfMon + ".txt",true));
					writero = new BufferedWriter(new FileWriter("output/" + calYear + ((calMonth + 1) < 10 ? "0" : "") + (calMonth + 1) + (calDayOfMon < 10 ? "0" : "") + calDayOfMon + ".txt",true));
					writert = new BufferedWriter(new FileWriter("total/" + calYear + ((calMonth + 1) < 10 ? "0" : "") + (calMonth + 1) + (calDayOfMon < 10 ? "0" : "") + calDayOfMon + ".txt",true));
					PrintWriter pi = new PrintWriter(writeri,true);
					PrintWriter po = new PrintWriter(writero,true);
					PrintWriter pt = new PrintWriter(writert,true);
					// 각 테이블의 헤더를 파일에 먼저 입력해줌
					
					BufferedReader readeri = new BufferedReader(new FileReader("input/" + calYear + ((calMonth + 1) < 10 ? "0" : "") + (calMonth + 1) + (calDayOfMon < 10 ? "0" : "") + calDayOfMon + ".txt"));
					BufferedReader readero = new BufferedReader(new FileReader("output/" + calYear + ((calMonth + 1) < 10 ? "0" : "") + (calMonth + 1) + (calDayOfMon < 10 ? "0" : "") + calDayOfMon + ".txt"));
					BufferedReader readert = new BufferedReader(new FileReader("total/" + calYear + ((calMonth + 1) < 10 ? "0" : "") + (calMonth + 1) + (calDayOfMon < 10 ? "0" : "") + calDayOfMon + ".txt"));
					
					while ((readeri.readLine()) != null) {
						readLinei++;
					}
					while ((readero.readLine()) != null) {
						readLineo++;
					}
					while ((readert.readLine()) != null) {
						readLinet++;
					}
					readeri.close();
					readero.close();
					readert.close();
					
					if(readLinei == 0) {
						writeri.write(header[0] + "\t\t" + header[1] + "\t\t" + header[2] + "\t\t" + header[3] + "\t\t" + header[4] + "\t\t" + "\n");
					}
					if(readLineo == 0) {
						writero.write(header[0] + "\t\t" + header[1] + "\t\t" + header[2] + "\t\t" + header[3] + "\t\t" + header[4] + "\t\t" + "\n");
					}
					if(readLinet == 0) {
						writert.write(headerr[0] + "\t" + headerr[1] + "\t" + headerr[2] + "\t" + headerr[3] + "\t" + headerr[4] + "\t" + headerr[5] + "\t" + headerr[6] +
								"\t" + headerr[7] + "\t" + "\n");
					}
					
					//수입 테이블에 저장된 내용을 읽어들여 파일에 저장
					for(int i = 0; i < inputtable.getRowCount(); i++)
	        		  {
	        			 //writeri.flush(); //버퍼를 비움
	        			 for(int j = 0; j < inputtable.getColumnCount(); j++)
	        			 {
	        				// writeri.write((String)(inputtable.getValueAt(i,j)));
	        				 //writeri.write("\t\t");
	        				 if(j == 0) {
	        					 int line = readLinei;
	        					 pi.write(Integer.toString(line));
	        					 pi.write("\t\t");
	        				 } else {
	        					 pi.write((String)(inputtable.getValueAt(i,j)));
		        				 pi.write("\t\t"); 
	        				 }
	        			 }
	        			 readLinei++;
	        			 pi.write("\n");
	        		  }
					pi.flush();
					pi.close();
	        		  
	        		//지출 테이블에 저장된 내용을 읽어들여 파일에 저장
	        		  for(int i = 0; i < outtable.getRowCount(); i++)
	        		  {
	        			 //writero.flush(); //버퍼를 비움
	        			 for(int j = 0; j < outtable.getColumnCount(); j++)
	        			 {
	        				 if(j == 0) {
	        					 int line = readLineo;
	        					 po.write(Integer.toString(line));
	        					 po.write("\t\t");
	        				 } else {
	        					 po.write((String)(outtable.getValueAt(i,j)));
		        				 po.write("\t\t"); 
	        				 }
	        			 }
	        			 po.write("\n");
	        			 readLineo++;
	        		  }
	        		  po.flush();
	        		  po.close();
	        		//전체 내역 테이블에 저장된 내용을 읽어들여 파일에 저장
	        		  for(int i = 0; i < moneytable.getRowCount(); i++)
	        		  {
	        			 for(int j = 0; j < moneytable.getColumnCount(); j++)
	        			 {
	        				 pt.write((String)(moneytable.getValueAt(i,j)));
	        				 pt.write("\t");
	        			 }
	        			 pt.write("\n");
	        		  }
	        		  pt.flush();
	        		  pt.close();
	        		  //파일에 저장한 후 각 테이블의 내용을 모두 지워줌
	        		DefaultTableModel remove = (DefaultTableModel)moneytable.getModel();
	  				remove.setNumRows(0);
	  				DefaultTableModel removein = (DefaultTableModel)inputtable.getModel();
	  				removein.setNumRows(0);
	  				DefaultTableModel removeout = (DefaultTableModel)outtable.getModel();
	  				removeout.setNumRows(0);

				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	         }
	         
		});
		/*// 차트 이벤트
		chart.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 new chart();
	         }
		});*/
		f.setVisible(true);
	}
	// 오늘 날짜에 포커스를 맞춰줌
	private void focusToday(){
		if(today.get(Calendar.DAY_OF_WEEK) == 1)
			dateButs[today.get(Calendar.WEEK_OF_MONTH)][today.get(Calendar.DAY_OF_WEEK) - 1].requestFocusInWindow();
		else
			dateButs[today.get(Calendar.WEEK_OF_MONTH) - 1][today.get(Calendar.DAY_OF_WEEK) - 1].requestFocusInWindow();
	}
	// 캘린더를 보여준다
	private void showCal(){
		for(int i = 0; i < CAL_HEIGHT; i++){
			for(int j = 0; j < CAL_WIDTH; j++){
				String fontColor = "black";
				if(j == 0) fontColor = "red";
				else if(j == 6) fontColor = "blue";
				
				File f = new File("MemoData/" + calYear + ((calMonth + 1) < 10 ? "0" : "") + (calMonth + 1) + (calDates[i][j] < 10 ? "0" : "") + calDates[i][j] + ".txt");
				if(f.exists()){
					dateButs[i][j].setText("<html><b><font color=" + fontColor + ">" + calDates[i][j] + "</font></b></html>");
				}
				else dateButs[i][j].setText("<html><font color=" + fontColor + ">" + calDates[i][j] + "</font></html>");
				JLabel todayMark = new JLabel("<html><font color=green>*</html>");
				dateButs[i][j].removeAll();
				if(calMonth == today.get(Calendar.MONTH) &&
						calYear == today.get(Calendar.YEAR) &&
						calDates[i][j] == today.get(Calendar.DAY_OF_MONTH)){
					dateButs[i][j].add(todayMark);
					dateButs[i][j].setToolTipText("Today");
				}
				
				if(calDates[i][j] == 0) dateButs[i][j].setVisible(false);
				else dateButs[i][j].setVisible(true);
			}
		}
	}
	//<<,<,>,>> 년, 월 선택 버튼 이벤트
	//today 버튼 이벤트
	private class ListenForCalOpButtons implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == todayBut){
				setToday();
				lForDateButs.actionPerformed(e);
				focusToday();
			}
			else if(e.getSource() == lYearBut) moveMonth(-12);
			else if(e.getSource() == lMonBut) moveMonth(-1);
			else if(e.getSource() == nMonBut) moveMonth(1);
			else if(e.getSource() == nYearBut) moveMonth(12);
			
			curMMYYYYLab.setText("<html><table width=100><tr><th><font size=5>" + calYear + " / " + ((calMonth + 1) < 10 ? "&nbsp;" : "") + (calMonth + 1) + "</th></tr></table></html>");
			showCal();
		}
	}
	//캘린더에서의 날짜 선택 이벤트
	private class listenForDateButs implements ActionListener{
		public void actionPerformed(ActionEvent e) {
				
			int k = 0, l = 0;
			for(int i = 0 ; i < CAL_HEIGHT ; i++){
				for(int j = 0 ; j < CAL_WIDTH ; j++){
					if(e.getSource() == dateButs[i][j]){ 
						k = i;
						l = j;
					}
				}
			}
	
			if(!(k == 0 && l == 0)) calDayOfMon = calDates[k][l]; //today버튼을 눌렀을때도 이 actionPerformed함수가 실행되기 때문에 넣은 부분

			cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
			
			String dDayString = new String();
			//오늘 날짜와 선택 날짜의 d-day 계산
			int dDay = ((int)((cal.getTimeInMillis() - today.getTimeInMillis())/1000/60/60/24));
			if(dDay == 0 && (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)) 
					&& (cal.get(Calendar.MONTH) == today.get(Calendar.MONTH))
					&& (cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))) dDayString = "Today"; 
			else if(dDay >= 0) { dDayString = "D-" + (dDay + 1);
					datee = new String(calYear + "/" + (calMonth + 1) + "/" + (today.get(Calendar.DAY_OF_MONTH) + (dDay + 1))); //날짜
			}
			else if(dDay < 0) { dDayString = "D+" + (dDay * -1);
					datee = new String(calYear + "/" + (calMonth + 1) + "/" + (today.get(Calendar.DAY_OF_MONTH) + (dDay * -1) + 1)); //날짜
			}
			//선택한 날짜를 selectedDate 변수에 저장
			selectedDate.setText("<Html><font size=3>" + calYear + "/" + (calMonth + 1) + "/" + calDayOfMon + "&nbsp;(" + dDayString + ")</html>");
	        
		}
	}
	// 현재 시간을 읽어들임
	private class ThreadConrol extends Thread{
		public void run(){
			boolean msgCntFlag = false;
			int num = 0;
			String curStr = new String();
			while(true){
					today = Calendar.getInstance();
					String amPm = (today.get(Calendar.AM_PM)==0?"AM":"PM");
					String hour;
							if(today.get(Calendar.HOUR) == 0) hour = "12"; 
							else if(today.get(Calendar.HOUR) == 12) hour = " 0";
							else hour = (today.get(Calendar.HOUR) < 10 ? " " : "") + today.get(Calendar.HOUR);
					String min = (today.get(Calendar.MINUTE) < 10 ? "0" : "") + today.get(Calendar.MINUTE);
					String sec = (today.get(Calendar.SECOND) < 10 ? "0" : "") + today.get(Calendar.SECOND);
					infoClock.setText(amPm + " " + hour + ":" + min + ":" + sec);
				
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new project();
			}
		});
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String[] input_index = {"용 돈", "월 급", "장학금", "기 타"};
	    String[] output_index = {"식 사 ", "교 통", "여 가", "공과금", "저 축", "기 타"};
	    // 수입 라디오 버튼을 선택했을 때 콤보박스 설정
	    if(e.getSource() == rbin) {
	        DefaultComboBoxModel mode1 = new DefaultComboBoxModel(input_index);
	        comboBox.setModel(mode1);
	     }
	     // 지출 라디오 버튼을 선택했을 때 콤버박스 설정
	     else if(e.getSource() == rbout) {
	        DefaultComboBoxModel mode2 = new DefaultComboBoxModel(output_index);
	        comboBox.setModel(mode2);
	     }
	}
}
