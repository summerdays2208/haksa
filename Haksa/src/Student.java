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
	JTextField tfId = null; // ��������� �����ϸ� �л�Ŭ�������忡�� ��������(�۷ι�����)
	JTextField tfName = null;
	JTextField tfDepartment = null; // null�� �ʱ�ȭ�ϴ� �⺻��(���۷��� ����)
	JTextField tfAddress = null;
	
	JTextArea taList = null;
	
	JButton btnInsert = null; // ��Ϲ�ư
	JButton btnSelect = null; // �˻�(��ȸ)��ư
	JButton btnUpdate = null; // ������ư
	JButton btnDelete = null; // ������ư
	
	JButton btnSearch = null; // �˻���ư
	
	Connection conn = null; // Ŀ�ؼ��� ���������� ����
	
	//Statement stmt = null;
	
	JTable table = null;
	DefaultTableModel model = null;
	
	public Student() { // ������ �����
		
		//DB Connection
		try {
			//oracle jdbc����̹� �ε�
			Class.forName("oracle.jdbc.driver.OracleDriver");// jdbc driver load
			//Connection
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ora_user","hong");// ����
			System.out.println("����Ϸ�");
			//Statement stmt = conn.createStatement(); �̷��� ��뵵 ����
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		setLayout(new FlowLayout());
		
		add(new JLabel("�й�")); // ���̺��� �̺�Ʈ ó���� ���ϱ⶧���� �ν��Ͻ��� ���ʿ�
//		JTextField tfId = new JTextField(); // �޼ҵ�ȿ� �����ϸ� tfid�� �л�������� ��������, �޼ҵ������ �������� �Ҹ�(�ٸ� �޼ҵ忡�� ���Ұ���)
		this.tfId = new JTextField((14)); // JTextField((14)) 14���ڰ� ���ٴ� �ǹ�
		add(this.tfId);
		
		this.btnSearch = new JButton("�˻�");
		add(this.btnSearch);
		this.btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//conn�� ��������� ����Ѵ�
					Statement stmt = conn.createStatement();
					
					//����ʱ�ȭ
					model.setNumRows(0);// ���� ���� �� 0
					ResultSet rs = stmt.executeQuery("select * from student where id = '"+tfId.getText()+"'");
					
					while(rs.next()) {
						
						tfName.setText(rs.getString("name"));
						tfDepartment.setText(rs.getString("dept"));
						tfAddress.setText(rs.getString("address"));
						
						//addRow(������ �迭�� �־������)
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
					e1.printStackTrace(); // �����޽��� �ڼ��ϰ� ���	
				}
				
			}
			
		});
		
		add(new JLabel("�̸�"));
		this.tfName = new JTextField((20));
		add(this.tfName);
		
		add(new JLabel("�а�"));
		this.tfDepartment = new JTextField((20));
		add(this.tfDepartment);
		
		add(new JLabel("�ּ�"));
		this.tfAddress = new JTextField((20));
		add(this.tfAddress);
		
		//���̺� ����, �˻������ ���̺� ���
		String[] colName = {"�й�", "�̸�", "�а�", "�ּ�"}; // �÷���
		this.model = new DefaultTableModel(colName, 0); // ��
		this.table = new JTable(model); // ���̺� ������
		table.setPreferredScrollableViewportSize(new Dimension(280,200)); // ���̺�ũ��
		add(new JScrollPane(this.table)); //new JScrollPane ��ũ�� ����
		//���̺��� Ư������ �����ؼ� TextField�� ���� �Է�
		this.table.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//�̺�Ʈ�� �߻��� ������Ʈ(table)�� ���Ѵ�
				table = (JTable)e.getComponent(); //������Ʈ�� ���̺�� ��ȯ�� ���̺����� �ִ´�
				//table�� ���� ���Ѵ�
				model = (DefaultTableModel)table.getModel();
				//���� ���õ� ���� �÷����� ���Ѵ�
				tfId.setText((String)model.getValueAt(table.getSelectedRow(), 0)); // .getValueAt(�����ε���, �÷����ε���)
				tfName.setText((String)model.getValueAt(table.getSelectedRow(), 1)); // .getValueAt ����Ÿ���� Object�� String���� ��ȯ
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
		
		this.btnInsert = new JButton("���");
		add(btnInsert);
		this.btnInsert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//createStatement�� Connection�� �޼ҵ�, Connection�� �޼ҵ�� ������ Statement �ν��Ͻ�
					Statement stmt = conn.createStatement();
					
					//insert, ���⼭ this�� ActionListener�� ��ü
					stmt.executeUpdate("insert into student (id,name,dept,address) values ('"+tfId.getText()+"','"+tfName.getText()+"','"+tfDepartment.getText()+"','"+tfAddress.getText()+"')");
					
					//����ʱ�ȭ
					model.setNumRows(0); // model�� ���� �� 0
					ResultSet rs = stmt.executeQuery("select * from student");
					
					
					while(rs.next()) {
						//addRow(������ �迭�� �־������)
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
					e1.printStackTrace(); // �����޽��� �ڼ��ϰ� ���	
				}
				JOptionPane.showMessageDialog(null, "��ϵǾ����ϴ�");
				
			}
			
		});
		
		this.btnSelect = new JButton("���");
		add(btnSelect);
		this.btnSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Statement stmt = conn.createStatement();
					
					//����ʱ�ȭ
					model.setNumRows(0);// model�� ���� ���� 0����.
					ResultSet rs = stmt.executeQuery("select * from student");
					
					while(rs.next()) {
						//addRow(������ �迭�� �־������)
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
					e1.printStackTrace(); // �����޽��� �ڼ��ϰ� ���	
				}
			}});
		
		this.btnUpdate = new JButton("����");
		add(btnUpdate);
		this.btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Statement stmt = conn.createStatement();
					
					//update
					stmt.executeUpdate("update student set name='"+tfName.getText()+"', dept='"+tfDepartment.getText()+"', address='"+tfAddress.getText()+"'where id='"+tfId.getText()+"'");
					
					//����ʱ�ȭ, setText()�� �����
					model.setNumRows(0); //model�� ���� �� 0
					
					//�˻��� �л��� ���
					ResultSet rs = stmt.executeQuery("select * from student where id = '"+tfId.getText()+"'");
					
					
					while(rs.next()) {
						//addRow(������ �迭�� �־������)
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
					e1.printStackTrace(); // �����޽��� �ڼ��ϰ� ���	
				}
				
			}});
		
		this.btnDelete = new JButton("����");
		add(btnDelete);
		this.btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// YES�� NO���� result�� ���� (YES 0, NO 1, X -1)
				int result = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "�˸�", JOptionPane.YES_NO_OPTION); 
//				System.out.println(result);
				if(result == JOptionPane.YES_OPTION) {
					try {
						Statement stmt = conn.createStatement();
						
						//delete
						stmt.executeUpdate("delete from student where id='"+tfId.getText()+"'");
						
						//����ʱ�ȭ, setText()�� �����
						model.setNumRows(0); //model�� ���� �� 0
						
						ResultSet rs = stmt.executeQuery("select * from student");
						
						
						while(rs.next()) {
							//addRow(������ �迭�� �־������)
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
						e1.printStackTrace(); // �����޽��� �ڼ��ϰ� ���	
					}
				}
			}
			
		});
		
		this.setSize(280,500);
		this.setVisible(true);
	}

	
	public static void main(String[] args) {
		new Student(); //������ ȣ���ؼ� ������â ����
	}

}