import java.io.*;
import java.net.*;
import java.util.*;
public class ServeurF {
  
 // Client part
 private int res=1;
 private int nb;
 ArrayList<ArrayList<Integer>> Cache = new ArrayList<ArrayList<Integer>>();
 ArrayList<Integer> singleCache = new ArrayList<Integer>();
 
 
 
 
 class ClientThread extends Thread {
 
     Socket socket;
     InputStream sInput;
     OutputStream sOutput;
     String name;
	private Scanner sc;
 
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
         sc = new Scanner(sInput);
         while (true)
             if (sc.hasNext()) {
                 nb = sc.nextInt();
                 int nombre = nb ; 
                 nb = Factoriel(nb);
                 ArrayList<Integer> singleCache = new ArrayList<Integer>();	
                 singleCache.add(nombre);
                 singleCache.add(nb);
                 Cache.add(singleCache);
                 
                 System.out.println(name + ": " + nb);
               
                 broadcast(nb);
                 res=1;
             }//if 
     
     }//run
      synchronized int Fibonacci2(int nb){
         try { boolean verif=true;
             while(verif) {
            	 int position = Exist(nb,Cache);
            	 if (position>=0)
            	 {		System.out.println("Resultat issu du cache");
            	 int newres = res *Cache.get(position).get(1) ; 
            	 return newres;}
            	 else {
		             if (nb > 0){
		                 
		                     ClientThread fakeClient = new ClientThread(socket, name);             
		                     fakeClient.start();
		                   
		                     res = nb * fakeClient.getValue();
		                   
		     
		                     nb-- ;
		                   
		                     
		                          
		                     if(nb==0) {
		                         verif=false;
		                         return res; 
		                     }
		                 }
		                 else if (nb == 0){
		                	 			;
		                     res= 1;
		                     return res;
		                 }
		                 else{
		                     System.out.println("Attention nombre négatif !" );
		                     //Erreur(name);
		                     return res;
		                 }
		             }
		                 
		             } }
		             catch (Exception e) { }
		              return 0;   
		     }  
     private int Exist(int nb, ArrayList<ArrayList<Integer>> cache) {
    	// System.out.println(Cache.size());
    	 for (int i =0 ; i < Cache.size(); i++) {
    		 	
    		       if (Cache.get(i).get(0)==nb) 
    		       return  i ; 
    		    
    		}
		return -1;
	}
	public int getValue() {
     return res;
     }
 }//ClientThread
 // Server part
 
 ArrayList<ClientThread> socks = new ArrayList<ClientThread>();
 int port;
 int num = 0;
private ServerSocket sServer;
 ServeurF(int port) {
	this.port = port;
	this.Cache =new ArrayList<ArrayList<Integer>>()  ; 
 }//ServeurF
 synchronized void broadcast(int nb) {
	for (ClientThread e : socks) {
         PrintStream output = new PrintStream(e.sOutput);
         output.println(nb);
     }//for
 }//broadcast
 
 void run(){
	try {
		  
		ServerSocket sServer = new ServerSocket(port);
	  
	    while(true) {
	    	
		Socket s = sServer.accept();
		ClientThread c = new ClientThread(s, "Thread " + num);
             System.out.println("Connection of client " + num);
             num++;
		socks.add(c);
		c.start();
	    }//while
	}//try
	catch (Exception e) {   System.out.println(e);}
 }//run
 public static void main(String [] argv) {
	try {
		ServeurF c = new ServeurF(5000);
		c.run();
		
	} catch (Exception e) {
		
	}
 }//main
}//ServeurF
