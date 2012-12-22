/**
 *  @author Zbigniew Tomczak (tomczak.zbigniew@gmail.com) 
 */
package tomczak.blocking.queues.chat;

import static org.junit.Assert.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerTest {

	private static Server server;
	
	@BeforeClass
	public static void startServer() {
		server = new Server();
		server.start();
	}
	
	@AfterClass
	public static void shutdownServer() {
		server.interrupt();
		try {
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPostMessage() {
		String testMessage = "Sea shels she sells";
		String name = "Martin";
		BlockingQueue<String> clientQueue = new ArrayBlockingQueue<String>(1);
		assertTrue(server.addClient(name , clientQueue));
		server.postMessage(testMessage);
		try {
			assertEquals(clientQueue.take(), testMessage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(server.removeClient(name));
	}
}
