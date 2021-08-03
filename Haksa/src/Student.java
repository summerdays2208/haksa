import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Student extends JPanel{
	JTextField tfId = null; // 멤버변수로 선언하면 학사클래스입장에는 전역변수(글로벌변수)
	JTextField tfName = null;
	JTextField tfDepartment = null; // null은 초기화하는 기본값(레퍼런스 변수)
	JTextField tfAddress = null;
	
	JTextArea taList = null;
	
	JButton btnInsert = null; // 등록버튼
	JButton btnSelect = null; // 검색(조회)버튼
	JButton btnUpdate = null; // 수정버튼
	JButton btnDelete = null; // 삭제버튼
	
	JButton btnSearch = null; // 검색버튼
	
	Connection conn = null; // 커넥션을 전역변수로 생성
	
	//Statement stmt = null;
	
	JTable table = null;
	DefaultTableModel model = null;
	
	public Student() { // 생성자 만들기
		
		//DB Connection
		try {
			//oracle jdbc드라이버 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");// jdbc driver load
			//Connection
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ora_user","hong");// 연결
			System.out.println("연결완료");
			//Statement stmt = conn.createStatement(); 이렇게 사용도 가능
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		setLayout(new FlowLayout());
		
		add(new JLabel("학번")); // 레이블은 이벤트 처리를 안하기때문에 인스턴스명 불필요
//		JTextField tfId = new JTextField(); // 메소드안에 선언하면 tfid는 학사생성자의 지역변수, 메소드종료시 지역변수 소멸(다른 메소드에서 사용불가능)
		this.tfId = new JTextField((14)); // JTextField((14)) 14글자가 들어간다는 의미
		add(this.tfId);
		
		this.btnSearch = new JButton("검색");
		add(this.btnSearch);
		this.btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//conn이 멤버변수를 사용한다
					Statement stmt = conn.createStatement();
					
					//목록초기화
					model.setNumRows(0);// 모델의 행의 수 0
					ResultSet rs = stmt.executeQuery("select * from student where id = '"+tfId.getText()+"'");
					
					while(rs.next()) {
						
						tfName.setText(rs.getString("name"));
						tfDepartment.setText(rs.getString("dept"));
						tfAddress.setText(rs.getString("address"));
						
						//addRow(변수로 배열을 넣어줘야함)
						String[] row = new String[4];
						row[0] = rs.getString("id");
						row[1] = rs.getString("name");
						row[2] = rs.getString("dept");
						row[3] = rs.getString("address");
						
						model.addRow(row);
					}
					rs.close();
					stmt.close();
					
				}catch(Exception e1) {
					e1.printStackTrace(); // 에러메시지 자세하게 출력	
				}
				
			}
			
		});
		
		add(new JLabel("이름"));
		this.tfName = new JTextField((20));
		add(this.tfName);
		
		add(new JLabel("학과"));
		this.tfDepartment = new JTextField((20));
		add(this.tfDepartment);
		
		add(new JLabel("주소"));
		this.tfAddress = new JTextField((20));
		add(this.tfAddress);
		
		//테이블 생성, 검색결과를 테이블에 출력
		String[] colName = {"학번", "이름", "학과", "주소"}; // 컬럼명
		this.model = new DefaultTableModel(colName, 0); // 모델
		this.table = new JTable(model); // 테이블에 모델적용
		table.setPreferredScrollableViewportSize(new Dimension(280,200)); // 테이블크기
		add(new JScrollPane(this.table)); //new JScrollPane 스크롤 생성
		//테이블의 특정행을 선택해서 TextField에 값을 입력
		this.table.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//이벤트가 발생한 컴포넌트(table)을 구한다
				table = (JTable)e.getComponent(); //컴포넌트를 테이블로 변환후 테이블변수에 넣는다
				//table의 모델을 구한다
				model = (DefaultTableModel)table.getModel();
				//현재 선택된 행의 컬럼값을 구한다
				tfId.setText((String)model.getValueAt(table.getSelectedRow(), 0)); // .getValueAt(행의인덱스, 컬럼의인덱스)
				tfName.setText((String)model.getValueAt(table.getSelectedRow(), 1)); // .getValueAt 리턴타입이 Object라서 String으로 변환
				tfDepartment.setText((String)model.getValueAt(table.getSelectedRow(), 2));
				tfAddress.setText((String)model.getValueAt(table.getSelectedRow(), 3));		
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}	
		});
		
		this.btnInsert = new JButton("등록");
		add(btnInsert);
		this.btnInsert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//createStatement는 Connection의 메소드, Connection의 메소드로 생성된 Statement 인스턴스
					Statement stmt = conn.createStatement();
					
					//insert, 여기서 this는 ActionListener의 객체
					stmt.executeUpdate("insert into student (id,name,dept,address) values ('"+tfId.getText()+"','"+tfName.getText()+"','"+tfDepartment.getText()+"','"+tfAddress.getText()+"')");
					
					//목록초기화
					model.setNumRows(0); // model의 행의 수 0
					ResultSet rs = stmt.executeQuery("select * from student");
					
					
					while(rs.next()) {
						//addRow(변수로 배열을 넣어줘야함)
						String[] row = new String[4];
						row[0] = rs.getString("id");
						row[1] = rs.getString("name");
						row[2] = rs.getString("dept");
						row[3] = rs.getString("address");
						
						model.addRow(row);
					}
					rs.close();
					stmt.close();
					
				}catch(Exception e1) {
					e1.printStackTrace(); // 에러메시지 자세하게 출력	
				}
				JOptionPane.showMessageDialog(null, "등록되었습니다");
				
			}
			
		});
		
		this.btnSelect = new JButton("목록");
		add(btnSelect);
		this.btnSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Statement stmt = conn.createStatement();
					
					//목록초기화
					model.setNumRows(0);// model의 행의 수를 0으로.
					ResultSet rs = stmt.executeQuery("select * from student");
					
					while(rs.next()) {
						//addRow(변수로 배열을 넣어줘야함)
						String[] row = new String[4];
						row[0] = rs.getString("id");
						row[1] = rs.getString("name");
						row[2] = rs.getString("dept");
						row[3] = rs.getString("address");
						
						model.addRow(row);
					}
					rs.close();
					stmt.close();
					
				}catch(Exception e1) {
					e1.printStackTrace(); // 에러메시지 자세하게 출력	
				}
			}});
		
		this.btnUpdate = new JButton("수정");
		add(btnUpdate);
		this.btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Statement stmt = conn.createStatement();
					
					//update
					stmt.executeUpdate("update student set name='"+tfName.getText()+"', dept='"+tfDepartment.getText()+"', address='"+tfAddress.getText()+"'where id='"+tfId.getText()+"'");
					
					//목록초기화, setText()는 덮어쓰기
					model.setNumRows(0); //model의 행의 수 0
					
					//검색한 학생만 출력
					ResultSet rs = stmt.executeQuery("select * from student where id = '"+tfId.getText()+"'");
					
					
					while(rs.next()) {
						//addRow(변수로 배열을 넣어줘야함)
						String[] row = new String[4];
						row[0] = rs.getString("id");
						row[1] = rs.getString("name");
						row[2] = rs.getString("dept");
						row[3] = rs.getString("address");
						
						model.addRow(row);
					}
					rs.close();
					stmt.close();
					
				}catch(Exception e1) {
					e1.printStackTrace(); // 에러메시지 자세하게 출력	
				}
				
			}});
		
		this.btnDelete = new JButton("삭제");
		add(btnDelete);
		this.btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// YES나 NO값이 result에 저장 (YES 0, NO 1, X -1)
				int result = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION); 
//				System.out.println(result);
				if(result == JOptionPane.YES_OPTION) {
					try {
						Statement stmt = conn.createStatement();
						
						//delete
						stmt.executeUpdate("delete from student where id='"+tfId.getText()+"'");
						
						//목록초기화, setText()는 덮어쓰기
						model.setNumRows(0); //model의 행의 수 0
						
						ResultSet rs = stmt.executeQuery("select * from student");
						
						
						while(rs.next()) {
							//addRow(변수로 배열을 넣어줘야함)
							String[] row = new String[4];
							row[0] = rs.getString("id");
							row[1] = rs.getString("name");
							row[2] = rs.getString("dept");
							row[3] = rs.getString("address");
							
							model.addRow(row);
						}
						rs.close();
						stmt.close();
					
					}catch(Exception e1) {
						e1.printStackTrace(); // 에러메시지 자세하게 출력	
					}
				}
			}
			
		});
		
		this.setSize(280,500);
		this.setVisible(true);
	}

	
	public static void main(String[] args) {
		new Student(); //생성자 호출해서 윈도우창 생성
	}

}