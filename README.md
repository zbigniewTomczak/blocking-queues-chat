BlockingQueues chat
====================

Demo application: Java Swing chat that shows the java.util.concurrent.BlockingQueue interface in multithreaded environment.

This application is meant to be build with Maven. It can be build and run with the following command:

    mvn package exec:java

Alternatively, if you're not using Maven, you can build and run the application by executing following set of java commands:

    javac -d src/main/java src/main/java/tomczak/blocking/queues/chat/*.java
    jar cfe blocking-queues-chat.jar tomczak.blocking.queues.chat.Starter -C src/main/java tomczak
    java -jar blocking-queues-chat.jar
    
When the program starts, two windows open, each representing one chat person. Message typed by one person will be broadcasted to all registered clients.

---

Chat is implemented in the client-server architecture. Server is started in the separate thread and uses concurrent enabled BlockingQueue to receive and broadcast messages to registered clients. Clients are registered and unregistered on the server to join or leave the chat. Each client has its own thread that listens for an incoming messages.
