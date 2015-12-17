package Editor.view;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;


public class View extends JPanel{
	
	JTextArea textArea;
	JButton btnEval;

	public View() {
		setLayout(null);

		textArea = new JTextArea();
		textArea.setRows(10);
		textArea.setColumns(20);
		textArea.setBounds(30, 25, 384, 184);
		textArea.setText("greetings DB 5\r\n" + 
				"MOV EAX, [EDX]\r\n" + 
				"mov cx, 0x4567\r\n" + 
				"mov edx, [eax]");
		add(textArea);
		
		btnEval = new JButton("Eval");
		btnEval.setBounds(30, 240, 89, 23);
		add(btnEval);

		JFrame f = new JFrame();
		f.setSize(500, 500);
		f.setContentPane(this);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public String getCode(){
		String formattedText = this.textArea.getText();		
		return formattedText.replace("\r", "");
	}

	public void addActionListener(ActionListener a){
		btnEval.addActionListener(a);
	}
}
