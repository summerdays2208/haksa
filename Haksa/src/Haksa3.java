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
	
	JPanel panel;  // �޴��� ȭ���� ��µǴ� �г�
	
	public Haksa3() {
		this.setTitle("�л����");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//DB Connection
		try {
			//oracle jdbc����̹� �ε�
			Class.forName("oracle.jdbc.driver.OracleDriver");// jdbc driver load
			//Connection
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ora_user","hong");// ����
			System.out.println("����Ϸ�");
			stmt=conn.createStatement();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//DB Close. �����찡 ����� �� ����.
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
		  
		JMenu m_student=new JMenu("�л�����");//File�޴�
		bar.add(m_student);
		JMenu m_book=new JMenu("��������");//Edit�޴�
		bar.add(m_book);
		
		JMenuItem mi_list=new JMenuItem("�л�����");
		m_student.add(mi_list);
		mi_list.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.removeAll(); //���������Ʈ ����
				panel.revalidate(); //�ٽ� Ȱ��ȭ
				panel.repaint();    //�ٽ� �׸���
				panel.add(new Student()); //ȭ�� ����.
				panel.setLayout(null);//���̾ƿ��������
				
			}});
		
		JMenuItem mi_bookRent=new JMenuItem("������");
		m_book.add(mi_bookRent);
		mi_bookRent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.removeAll(); //���������Ʈ ����
				panel.revalidate(); //�ٽ� Ȱ��ȭ
				panel.repaint();    //�ٽ� �׸���
				panel.add(new BookRent()); //ȭ�� ����.
				panel.setLayout(null);//���̾ƿ��������
				
			}});
		
//		JMenuItem mi_rentRate=new JMenuItem("������Ȳ");
//		m_book.add(mi_rentRate);
		
		
		
		
		
		this.setJMenuBar(bar);
		
		panel=new JPanel();
		add(panel);//�����ӿ� �г� �߰�
		

		
		this.setSize(700, 500);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		new Haksa3();
	}

}
