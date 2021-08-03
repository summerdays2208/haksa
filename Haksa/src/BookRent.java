import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookRent extends JPanel{
	DefaultTableModel model=null;
	JTable table=null;
	Connection conn=null;
	
	Statement stmt;  
	String query; //sql��
	
	public BookRent() { //������ ����� ������ ȣ��
		//query = "select s.id, s.name, b.title, br.rdate ";
		//query += "from student s, books b, bookrent br ";
		//query += "where s.id = br.id and b.no = br.bookno";
		
		//query = "select s.id, s.name, b.title, br.rdate " //���α׷� �������ڸ��� ��ü ������ �������Ϸ��� �� ���� ���
				//+"from student s, books b, bookrent br "
				//+"where s.id = br.id and b.no = br.bookno";
		
		try {
			//DB����
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ora_user", "hong");
			//System.out.println("����Ϸ�");
			stmt=conn.createStatement();
			}catch(Exception e) {
			e.printStackTrace();
			}	
    
		setLayout(null);//���̾ƿ�����. ���̾ƿ� ��� ����.
    
		JLabel l_dept=new JLabel("�а�");
		l_dept.setBounds(10, 10, 30, 20); //.setBounds(x,y,����,����)
		add(l_dept);
		
		String[] dept={"��ü","��ǻ�ͽý���","��Ƽ�̵��","��ǻ�Ͱ���"};
		JComboBox cb_dept=new JComboBox(dept);
		cb_dept.setBounds(45, 10, 100, 20); //�޺��ڽ� ũ�� ����
		add(cb_dept);
		cb_dept.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				query = "select s.id, s.name, b.title, br.rdate "
						+"from student s, books b, bookrent br "
						+"where s.id = br.id and b.no = br.bookno";
				
				JComboBox cb=(JComboBox)e.getSource(); //�̺�Ʈ�� �߻��� �޺��ڽ� ���ϱ�
				//��������
				if(cb.getSelectedIndex() == 0) {
					//��ü
					query += " order by s.id"; //��ĭ����� ������ ���� ������ ������� �ٴ´�
				//��ǻ�ͽý���
				}else if(cb.getSelectedIndex() == 1) {
					query += " and s.dept = '��ǻ�ͽý���' order by br.no";
				//��Ƽ�̵��
				}else if(cb.getSelectedIndex() == 2) {
					query += " and s.dept = '��Ƽ�̵��' order by br.no";
				//��ǻ�Ͱ���
				}else if(cb.getSelectedIndex() == 3) {
					query += " and s.dept = '��ǻ�Ͱ���' order by br.no";
				}
				
				//������
				list();
			}
			
		});
		
		String colName[]={"�й�","�̸�","������","������"};
	    model=new DefaultTableModel(colName,0); // �𵨿� ������ �ֱ�
	    table = new JTable(model); //���� ���̺��� �ֱ�
	    table.setPreferredScrollableViewportSize(new Dimension(470,200)); //���̺� ������ ����
	    add(table);
	    JScrollPane sp=new JScrollPane(table);
	    sp.setBounds(10, 40, 460, 250); //��ũ�� ������ ����
	    add(sp);  
	    
	    setSize(490, 400);//ȭ��ũ��
	    setVisible(true);
	    
	    //��ü���
	    list();
	
		
		 setSize(490, 400);//ȭ��ũ��
		 setVisible(true);
	}
	
	public void list(){
	    try{
		     System.out.println("����Ǿ����ϴ�.....");
		     System.out.println(query); //�����, �������� ���      
		     // Select�� ����     
		     ResultSet rs=stmt.executeQuery(query);
		    
	     //JTable �ʱ�ȭ
	     model.setNumRows(0);
	    
	     while(rs.next()){
	      String[] row=new String[4];//�÷��� ������ 4
	      row[0]=rs.getString("id");
	      row[1]=rs.getString("name");
	      row[2]=rs.getString("title");
	      row[3]=rs.getString("rdate");
	      model.addRow(row);
	     }
	     rs.close();
	    
	    }
	    catch(Exception e1){
	     //e.getStackTrace();
	     System.out.println(e1.getMessage());
	    }							
	 }
	
	//main�޼ҵ�� �����ӿ��� ����Ǳ⶧���� ��� �ȴ�
	//public static void main(String[] args) {new BookRent();}

}