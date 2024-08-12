package Calculator;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {

    Stack<String> STACK = new Stack<>(); //Create new stack

    protected CalculatorImplementation() throws RemoteException {
        super();
    }

    public String sayHello() {
	    return "Hello, world!";
	}

    public void pushValue(int val) {
        String valStr = String.valueOf(val);
        STACK.push(valStr);
    };


    public int pop() {
        String poppedStr = STACK.pop();

        return Integer.parseInt(poppedStr);
    };

    public void pushOperation(String operator) { //This is quite long, take care of it later
        int pushVal = pop();
        int buffer;
        if(operator == "min") {
            while(!isEmpty()) {
                buffer = pop();
                if(pushVal < buffer) {
                    pushVal = buffer;
                }
            }

        } else if(operator == "max") {
            while(!isEmpty()) {
                buffer = pop();
                if(pushVal > buffer) {
                    pushVal = buffer;
                }
            }

        } else if(operator == "gcd") {
            while(!isEmpty()) {
                buffer = pop();
                pushVal = gcd(pushVal , buffer);
            }

        } else if(operator == "lcm") {
            while(!isEmpty()) {
                buffer = pop();
                pushVal = lcm(pushVal, buffer);
            }
        }
        pushValue(pushVal);
    };

    public boolean isEmpty() {

        return STACK.isEmpty();
    };

    public int delayPop(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            System.out.println("Thread sleep failed");
        }


        return 0;
    };

    public String displayStack() {
        return "CurrentStack: " + STACK;
    };

    public int gcd(int firstNum , int secondNum) {
        if(secondNum == 0) {
            return firstNum;
        }

        return gcd(firstNum , firstNum % secondNum);
    }

    public int lcm(int firstNum, int secondNum) {
        return (firstNum / gcd(firstNum, secondNum)) * secondNum;
    }
}