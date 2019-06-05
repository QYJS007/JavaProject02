package cg.model23.state.state1;


import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//״̬�ı�ʱִ�еĶ�����
public class SafeFrame extends Frame implements ActionListener,Context {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField textClock = new TextField(60);
	private TextArea textScreen = new TextArea(10,60);
	private Button buttonUse = new Button("ʹ�ý��");
	private Button buttonAlarm = new Button("���¾���");
	private Button buttonPhone = new Button("����ͨ��");
	private Button buttonExit = new Button("����");

	private State state  = DayState.getInstance();

	public SafeFrame(String title) {
		super(title);
		setBackground(Color.lightGray);
		setLayout(new BorderLayout());
		add(textClock, BorderLayout.NORTH);
		textClock.setEditable(false);
		add(textScreen, BorderLayout.CENTER);
		textScreen.setEditable(false);
		Panel panel = new Panel();
		panel.add(buttonUse);
		panel.add(buttonAlarm);
		panel.add(buttonPhone);
		panel.add(buttonExit);
		add(panel, BorderLayout.SOUTH);

		pack();
		show();
		//���ʱ�����
		buttonUse.addActionListener(this);
		buttonAlarm.addActionListener(this);
		buttonPhone.addActionListener(this);
		buttonExit.addActionListener(this);

	}
	@Override
	public void setClock(int hour) {
		String clockString = "���ڵ�ʱ����";
		if(hour<10){
			clockString += "0"+hour+":00";
		}else {
			clockString += hour+":00";
		}
		System.out.println(clockString);
		textClock.setText(clockString);
		state.doClock(this, hour);
	}

	@Override
	public void changeState(State state) {
		System.out.println("��"+this.state+"״̬��Ϊ��"+state+"״̬");
		this.state = state;

	}

	@Override
	public void callSecurityCenter(String msg) {
		textScreen.append("call!"+msg+"\n");

	}

	@Override
	public void recordLog(String msg) {
		textScreen.append("record..."+msg+"\n");

	}
	//�¼�������ʱ�ж����ĸ� �¼����������ݲ�ͬ���¼�ִ�в�ͬ�Ķ���
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.toString());
		if(e.getSource()==buttonUse){
			state.doUse(this);
		}else if (e.getSource()==buttonAlarm) {
			state.doAlarm(this);
		}else if (e.getSource()==buttonPhone) {
			state.doPone(this);
		}else {
			System.out.println("?");
		}
	}

}
