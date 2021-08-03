import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Haksa3 extends JFrame{

	JTextField tfId=null;
	JTextField tfName=null;
	JTextField tfDepartment=null;
	JTextField tfAddress=null;
	
	JTextArea taList=null;
	
	JButton btnInsert=null;
	JButton btnSelect=null;
	JButton btnUpdate=null;
	JButton btnDelete=null;
	
	JButton btnSearch=null;
	
	Connection conn=null;
	
	Statement stmt=null;
	
	JTable table=null;
	DefaultTableModel model=null;
	
	JPanel panel;  // 메뉴별 화면이 출력되는 패널
	
	public Haksa3() {
		this.setTitle("학사관리");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//DB Connection
		try {
			//oracle jdbc드라이버 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");// jdbc driver load
			//Connection
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ora_user","hong");// 연결
			System.out.println("연결완료");
			stmt=conn.createStatement();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//DB Close. 윈도우가 종료될 때 실행.
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if(conn!=null) {
						conn.close();
					} 
				}catch(Exception e1) {
					e1.printStackTrace();
				}
				
			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}
		});
		
		
		JMenuBar bar=new JMenuBar();
		  
		JMenu m_student=new JMenu("학생관리");//File메뉴
		bar.add(m_student);
		JMenu m_book=new JMenu("도서관리");//Edit메뉴
		bar.add(m_book);
		
		JMenuItem mi_list=new JMenuItem("학생정보");
		m_student.add(mi_list);
		mi_list.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.removeAll(); //모든컴포넌트 삭제
				panel.revalidate(); //다시 활성화
				panel.repaint();    //다시 그리기
				panel.add(new Student()); //화면 생성.
				panel.setLayout(null);//레이아웃적용안함
				
			}});
		
		JMenuItem mi_bookRent=new JMenuItem("대출목록");
		m_book.add(mi_bookRent);
		mi_bookRent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.removeAll(); //모든컴포넌트 삭제
				panel.revalidate(); //다시 활성화
				panel.repaint();    //다시 그리기
				panel.add(new BookRent()); //화면 생성.
				panel.setLayout(null);//레이아웃적용안함
				
			}});
		
//		JMenuItem mi_rentRate=new JMenuItem("대출현황");
//		m_book.add(mi_rentRate);
		
		
		
		
		
		this.setJMenuBar(bar);
		
		panel=new JPanel();
		add(panel);//프레임에 패널 추가
		

		
		this.setSize(700, 500);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		new Haksa3();
	}

}
