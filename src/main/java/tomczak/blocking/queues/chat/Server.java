/**
 *  @author Zbigniew Tomczak (tomczak.zbigniew@gmail.com) 
 */
package tomczak.blocking.queues.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server extends Thread {
	public final static int SERVER_CAPACITY = 5;
	private BlockingQueue<String> dispatchQueue = new ArrayBlockingQueue<String>(SERVER_CAPACITY);
	private Map<String, BlockingQueue<String>> clients = new HashMap<String, BlockingQueue<String>>();
	
	@Override
	public void run() {
		System.out.println("Server starting...");
		while (true) {
			try {
				dispatchMessage(dispatchQueue.take());
			} catch (InterruptedException e) {
				System.out.println("Server end");
				return;
			}
		}
	}
	
	public boolean postMessage(String message) {
		return dispatchQueue.offer(message);
	}
	
	public synchronized boolean addClient(String name , BlockingQueue<String> clientQueue) {
		if (clients.containsKey(name)) {
			return false;
		}
		clients.put(name, clientQueue);
		return true;
	}

	public synchronized boolean removeClient(String name) {
		if (clients.containsKey(name)) {
			clients.remove(name);
			return true;
		}
		return false;
	}

	private synchronized void dispatchMessage(String message) {
		for (BlockingQueue<String> queue : clients.values()) {
			queue.offer(message);
		}
	}
}
