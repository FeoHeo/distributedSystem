package Calculator;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    
    String sayHello() throws RemoteException;

    void pushValue(int val) throws RemoteException; //Push value to stack

    void pushOperation(String operator) throws RemoteException; //Push a string that contains {min,max,lcm,gcd} to stack, which will cause server to pop all stack vals and
    //min: push min val of all popped val
    //max: push max val of all popped val
    //lcm: push least common multiple
    //gcd: push greatest common divisor

    int pop() throws RemoteException; //pop top of stack and return to client

    boolean isEmpty() throws RemoteException; //Return true if stack is empty, false otherwise

    int delayPop(int millis) throws RemoteException; //Delay for milisec before popping

    String displayStack() throws RemoteException; //For testing purposes

    int gcd(int firstNum , int secondNum) throws RemoteException;

    int lcm(int firstNum , int secondNum) throws RemoteException;
}