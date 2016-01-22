// ServeurF.java

import java.io.*;
import java.net.*;
import java.util.*;

public class ServeurF {

    // Client part
    private int res=1;
    private int nb;

    class ClientThread extends Thread {
    
        Socket socket;
        InputStream sInput;
        OutputStream sOutput;
        String name;
    
        ClientThread(Socket socket, String name) {
	    try {
	        this.socket = socket;
	        sOutput = socket.getOutputStream();
	        sInput  = socket.getInputStream();
	        this.name = name;
	    }//try
	    catch (Exception e) {}
        }//ClientThread

        public void run() {
            Scanner sc = new Scanner(sInput);
            while (true)
                if (sc.hasNext()) {
                    nb = sc.nextInt();
                    System.out.println(nb);
                    nb = Factoriel(nb);
                    System.out.println(name + ": " + nb);
                    broadcast(nb);
                }//if
        }//run

         synchronized int Factoriel(int nb){
            try { boolean verif=true;
                while(verif) 
                if (nb > 0){
                    
                        ClientThread fakeClient = new ClientThread(socket, name); 
                        PrintWriter p = new PrintWriter(socket.getOutputStream());
                        p.println(nb-1);
                        p.flush();
                        fakeClient.run();
                        res = nb * fakeClient.getValue();
                             
                        if(nb==0) {
                            verif=false;
                            return res; 
                        }
                    }
                    else if (nb == 0){
                         PrintWriter p = new PrintWriter(socket.getOutputStream());
                        p.println(1);
                        p.flush();
                        res= 1;
                        return res;
                    }
                    else{
                        System.out.println("Attention nombre négatif !" );
                        //Erreur(name);
                        return res;
                    }
                    
                }
                catch (Exception e) { return 0;}
                    
        }



        public int getValue() {
        return res;
        }

    }//ClientThread

    // Server part
    
    ArrayList<ClientThread> socks = new ArrayList<ClientThread>();
    int port;
    int num = 0;

    ServeurF(int port) {
	this.port = port;
    }//ServeurF

    synchronized void broadcast(int nb) {
	for (ClientThread e : socks) {
            PrintStream output = new PrintStream(e.sOutput);
            output.println(nb);
        }//for
    }//broadcast
    
    void run() {
	try {
	    ServerSocket sServer = new ServerSocket(port);
	    while (true) {
		Socket s = sServer.accept();
		ClientThread c = new ClientThread(s, "Thread " + num);;
                System.out.println("Connection of client " + num);
                num++;
		socks.add(c);
		c.start();
	    }//while
	}//try
	catch (Exception e) {}
    }//run

    public static void main(String [] argv) {
	ServeurF c = new ServeurF (50000);
	c.run();
    }//main

}//ServeurF
	    
