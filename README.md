BlockingQueues chat
====================

Demo application: Java Swing chat with usage of java.util.concurrent.BlockingQueue.

This application is ment to be build with Maven. It can be build and run with the following command:

    mvn package exec:java

When the program starts, two windows open, each representing one chat person. Message typed by one person will be broadcasted to all registered clients.

---

Chat is implemented in the client-server architecture. Server is started in the seprate thread and uses concurrent enabled BlockingQueue to receive and broadcast messages to registered clients. Clients are registered on the server to join the chat.
