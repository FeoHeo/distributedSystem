package byzant.testing;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.TreeMap;

public class Ping {
    private static int port = 5000;
    private static int otherPort = 5001;
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            
            // First, establish connection with proposer
            Socket proposerSocket = serverSocket.accept();
            PrintWriter proposerOut = new PrintWriter(proposerSocket.getOutputStream(), true);
            BufferedReader proposerIn = new BufferedReader(new InputStreamReader(proposerSocket.getInputStream()));
            
            // Create a connection to other acceptor
            Socket outSocket = null;
            boolean connected = false;
            while (!connected) {
                try {
                    outSocket = new Socket("localhost", otherPort);
                    connected = true;
                } catch (ConnectException e) {
                    Thread.sleep(1000);
                }
            }
            
            // Accept connection from other acceptor
            Socket inSocket = serverSocket.accept();
            
            final PrintWriter acptOut = new PrintWriter(outSocket.getOutputStream(), true);
            final BufferedReader acptIn = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
            
            // Start thread to handle messages from other acceptor
            Thread responseThread = new Thread(() -> {
                try {
                    String inputAcpt;
                    
                    // Send test message
                    acptOut.println("Ping from " + port);
                    
                    while ((inputAcpt = acptIn.readLine()) != null) {
                        if (inputAcpt.startsWith("Confirmed")) {
                            // Your existing confirmed message handling
                            String[] logArr = inputAcpt.split(":");
                            Double roundInfo = Double.parseDouble(logArr[1]) + (otherPort % 10);
                            Integer valInfo = Integer.parseInt(logArr[2]);
                            proposerOut.println("Confirmed:" + logArr[1] + ":" + logArr[2]);
                        }
                    }
                } catch (IOException e) {
                }
            });
            responseThread.start();
            
            // Handle proposer messages
            // Your existing proposer message handling code
        } catch (Exception e) {
        }
    }
}