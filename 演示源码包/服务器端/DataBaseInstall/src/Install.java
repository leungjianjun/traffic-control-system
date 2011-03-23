import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.*;

class Install extends JFrame implements ActionListener {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private JFileChooser fileChooser = new JFileChooser(".");

	private JButton button = new JButton("打开文件");
    
	private JTextArea t;
	public Install() {
		t = new JTextArea();
		//JLabel l = new JLabel("正在安装数据库，初始化数据库中的文件.....");
		JScrollPane scroll = new JScrollPane(t); 
		//把定义的JTextArea放到JScrollPane里面去 
		//分别设置水平和垂直滚动条自动出现 
		scroll.setHorizontalScrollBarPolicy( 
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		scroll.setVerticalScrollBarPolicy( 
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
	this.setTitle("文件夹选择");
	this.setPreferredSize(new Dimension(200, 100));
	this.getContentPane().add(button, BorderLayout.NORTH);
	this.getContentPane().add(scroll, BorderLayout.CENTER);
	button.addActionListener(this);
	this.pack();
	this.setLocationRelativeTo(null);
	this.setIgnoreRepaint(true);
	t.append("打开DB.sql文件");
	}

	public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();
	// 触发JButton(此例仅设置有一个按钮，多按钮请自行更改)
	if (source instanceof JButton) {
	this.exeTask(openFile());
	}

	}
	public  void exeTask(String filename){
		Statement stmt;
		t.append("正在安装数据库，初始化数据库中的文件.....\n");
		t.append("\n在安装前请确保mysql数据库已经打开，同时数据库中有名为yogng的数据库，里面不要间表单！\n");
		t.append("确保DB.sql文件的存在\n\n");
		try {
			stmt = connect2sql().createStatement();
			File file =new File(filename);
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line =null;
			String cmd="";
			while((line=bf.readLine())!=null){
				t.append(line+"\n");
				if((line.length()==0) || (line.substring(0, 2).equals("--"))){
					
				}else{
					if(line.substring(line.length()-1, line.length()).equals(";")){
						stmt.execute(cmd+line.substring(0,line.length()-1));
						cmd="";
					}else{
						cmd=cmd+line;
					}
				}
			}
			bf.close();
			t.append("数据库安装成功，可以启动服务器。/n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			t.append("发生错误，数据库安装失败！错误原因为：\n");
			t.append(e.getLocalizedMessage()+"\n");
			
		}
	}

	public String openFile() {
	fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	fileChooser.setDialogTitle("打开文件夹");
	int ret = fileChooser.showOpenDialog(null);
	if (ret == JFileChooser.APPROVE_OPTION) {
	//文件夹路径
	return fileChooser.getSelectedFile().getAbsolutePath();
	}else {
		return null;
	}
	}

	public static void main(String args[]) {
	Frame frame = new Install();
	frame.setSize(800,620);
	frame.setVisible(true);
	}
	public static Connection connect2sql(){
		Connection conn=null;
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = 
				DriverManager.getConnection("jdbc:mysql://localhost/yogng?seUnicode=true&characterEncoding=utf8","root","");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return conn;
	}


}


