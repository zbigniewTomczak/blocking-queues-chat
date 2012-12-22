/**
 *  @author Zbigniew Tomczak (tomczak.zbigniew@gmail.com) 
 */
package tomczak.blocking.queues.chat;

import java.awt.EventQueue;

public class Starter {
	public static void main(String[] args) throws InterruptedException {
		//start server
		final Server server = new Server();
		server.start();
		
		//start clients
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ChatWindow("Mark", server, 50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ChatWindow("John", server, 600);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
}
