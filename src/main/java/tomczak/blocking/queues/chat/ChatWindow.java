/**
 *  @author Zbigniew Tomczak (tomczak.zbigniew@gmail.com) 
 */
package tomczak.blocking.queues.chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;

import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


@SuppressWarnings("serial")
public class ChatWindow extends JFrame {

	private JPanel contentPane;
	private JTextPane mainChat;
	private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1);
	private Thread receiver;

	class MessageReceiver extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					final String message = queue.take();
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								if (mainChat != null) {
									mainChat.setText( mainChat.getText() + "\n" + message.toString());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				} catch (InterruptedException e) {
					System.out.println("Client end");
					return;
				}
			}
		}
	}
	
	/**
	 * Create the frame.
	 */
	public ChatWindow(final String name, final Server server, int xPosition) {
		
		server.addClient(name, queue);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				try {
					if (receiver != null) {
			        	server.removeClient(name);
			            receiver.interrupt();
			            receiver.join(500);
				    }
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					super.windowClosing(event);
					System.exit(0);
				}
			}
		});
		
		setBounds(xPosition, 100, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(5, 5));
		
		JPanel north = new JPanel();
		contentPane.add(north, BorderLayout.NORTH);
		north.setLayout(new BorderLayout(2, 2));
		
		JLabel lblCzat = new JLabel("Chat - " + name);
		lblCzat.setHorizontalAlignment(SwingConstants.CENTER);
		lblCzat.setFont(new Font("Tahoma", Font.PLAIN, 14));
		north.add(lblCzat);
		
		JPanel center = new JPanel();
		contentPane.add(center, BorderLayout.CENTER);
		center.setLayout(new BorderLayout(5, 5));
		
		mainChat = new JTextPane();
		mainChat.setEditable(false);
		center.add(mainChat);
		
		JSeparator separator = new JSeparator();
		center.add(separator, BorderLayout.NORTH);
		
		JPanel south = new JPanel();
		contentPane.add(south, BorderLayout.SOUTH);
		south.setLayout(new BorderLayout(5, 5));
		
		final JTextPane textMessage = new JTextPane();
		south.add(textMessage);
		
		textMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String message = name + ": " + textMessage.getText().trim();
					server.postMessage(message);
					textMessage.setText(null);
				}
			}
		});
		
		JSeparator separator_1 = new JSeparator();
		south.add(separator_1, BorderLayout.NORTH);
		
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = name + ": " + textMessage.getText().trim();
				server.postMessage(message);
			}
		});
		south.add(sendButton, BorderLayout.EAST);
		
		this.setVisible(true);
		textMessage.requestFocusInWindow();

		receiver = new MessageReceiver();
		receiver.start();
	}
}
