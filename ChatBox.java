import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatBox implements IChatBox, ActionListener, WindowListener {

	public void actionPerformed(ActionEvent arg0) { 
		if(jf.getTitle() == "ChatBox") {
			cp.subscribe( WinNum,this);
			screenName = jtf.getText();
			jtf.setText("");
			if(screenName.length() < 1) {screenName = "NULL";}
			jf.setTitle(screenName+"'s ChatBox");
		}
		else {
			cp.update(WinNum, this, jtf.getText(), screenName);
			jtf.setText("");
			jtf.requestFocusInWindow();
		}
	}
	public void notify(String now) { jta.setText(now); } //append to set testing
	
	public ChatBox(int winNum, ChatPublisher cp) {
		this.cp = cp;
		this.WinNum = winNum;
		jf = new JFrame();
        jf.setSize(605,350);
        jf.setLocation(0,(winNum-1)*150);
        
        jtf = new JTextField(20);
        jtf.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        jta = new JTextArea(10,30);
        jta.setEditable(false);
        jta.setFont(new Font("SansSerif", Font.PLAIN, 22));
        jta.setBackground(Color.LIGHT_GRAY);
        jta.setText("ENTER SCREEN NAME BELOW:\n\n\n\n\n\n\n\n\nPRESS ENTER TO SUBMIT:");
        
        scroll = new JScrollPane(jta);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getViewport ().setView (jta);
        
        jf.setTitle("ChatBox");
        jf.setLayout(new BorderLayout());
        jf.add(jtf, BorderLayout.PAGE_END);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jb = new JButton("Enter");
        jb.addActionListener(this);
        jtf.addActionListener(this);
        jf.addWindowListener(this);
        
        jf.add(scroll, BorderLayout.CENTER);
        
        jf.getRootPane().setDefaultButton(jb);

        jf.setVisible(true);
        jtf.requestFocusInWindow();
	}

	private JFrame jf;
    private JTextField jtf;
    private JTextArea jta;
    private JScrollPane scroll;
    private JButton jb;
	private ChatPublisher cp;
	private int WinNum;
	private String screenName;

    public void windowActivated(WindowEvent e) {jtf.requestFocusInWindow();}
    public void windowClosed(WindowEvent e) {}
    public void windowClosing(WindowEvent e) { 
    	cp.update(WinNum, this, "leaving the chat... \n"+screenName+" disconnected:", screenName);
    	cp.unsubscribe(WinNum, this);
    }
    public void windowDeactivated(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
}