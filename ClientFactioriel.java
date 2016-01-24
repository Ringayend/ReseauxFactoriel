//ClientF.java

import java.util.*;
import java.net.*;
import java.io.*;

class ClientF {

 

 class Listen extends Thread {

     Socket socket;
     InputStream sInput;
 
     Listen(Socket socket) {
	    try {
	        this.socket = socket;
	        sInput  = socket.getInputStream();
	    }//try
	    catch (Exception e) {}
     }//Listen

     public void run() {
         Scanner sc = new Scanner(sInput);
         while (true) {
             if (sc.hasNext()) {
                 int nb = sc.nextInt();
                 System.out.println(nb);
             }//if
         }//while
     }//run

 }//Listen

 void run() throws SocketException, IOException, UnknownHostException {
     InetAddress address = InetAddress.getLocalHost();
     Socket socket = new Socket(address, 5000);
     Listen l = new Listen(socket);
     l.start();
     
     PrintStream output = new PrintStream(socket.getOutputStream());
     Scanner sc = new Scanner(System.in);
     int result;
     while (true) {
         result = sc.nextInt();
         output.println(result);
        	 break;
         }
         
         
     
     
     
 }//run

 public static void main(String argv[]) {
     try {
         ClientF c = new ClientF();
         c.run();
     }//try
     catch (Exception e) {}//catch
 }//main

}//ClientF
