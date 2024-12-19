package byzant.testing;

import byzant.SimpleAcceptor;

public class Manager {

    public static void main(String[] args) {
        // Start SimpleAcceptor with port 5000
        Thread acceptorThread = new Thread(() -> {
            SimpleAcceptor.main(new String[]{"5000"});
        } , "Acceptor-5000");
        Thread acceptorThread2 = new Thread(() -> {
        	SimpleAcceptor.main(new String[]{"5001"});
        } , "Acceptor-5001");
        
        acceptorThread2.start();
        acceptorThread.start();
        try {
			Thread.sleep(1000);	// Quick fix to timing issue
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Start SimpleAcceptor with port 5001

        // Wait for both threads to complete
        try {
            acceptorThread.join();
            acceptorThread2.join();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Both SimpleAcceptors have completed.");
    }
}

