package com.driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

public class IRCClient extends Thread {

	private Socket socket;

	BufferedWriter objSender;

	private String myNickName="";
	private String myChannel="";
	
	private Vector<String> blockWordNotifier=new Vector<String>();

	public IRCClient(String serverName,int serverPort) throws Exception {
		socket = new Socket(serverName,serverPort);	//throws an exception if the server is not available and the client quits
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
		objSender= new BufferedWriter(outputStreamWriter);
		this.start(); // call the run method in this class the run runs in a separate thread 
	}


	public void addWordsToBlockedWordsList(String...words) {
		for(String i:words) {
			blockWordNotifier.add(i);
		}
		
	}
	
	public void getMemberOfChannel() throws IOException {
		writeToStream("NAMES "+myChannel);
		
	}
	
	


	public void setNick(String nick) throws InterruptedException, IOException {
		Thread.sleep(500);
		myNickName=nick;
		writeToStream("USER "+nick+" . . :"+nick);
		writeToStream("NICK "+nick);

	}

	public void listChannels(int minumumUser,int maxusers) throws InterruptedException, IOException {
		Thread.sleep(500);
		writeToStream("LIST -min "+minumumUser+" -max "+maxusers);

	}

	public void listChannels() throws InterruptedException, IOException {
		Thread.sleep(500);
		writeToStream("LIST");

	}

	private void msg(String to, String text) throws IOException, InterruptedException {
		Thread.sleep(500);
		String msg = "PRIVMSG " + to + " :" + text;
		writeToStream(msg);
	}

	public void sendMessageToAllInChannel(String msg) throws InterruptedException, IOException {
		Thread.sleep(500);
		msg(myChannel,msg);


	}
	
	public void sendMessageToSpecificPerson(String to,String msg) throws InterruptedException, IOException {
		Thread.sleep(500);
		msg(to,msg);


	}

	public void joinChannel(String channelName) throws InterruptedException, IOException {
		Thread.sleep(500);
		myChannel=channelName;
		writeToStream("JOIN "+channelName+"");

	}
	
	public void privateMessageHandler(String line) throws InterruptedException, IOException {
		checkForBlockedWords(line);
		String splitedText[] = line.split(" ");
		String chatMessage = line.split(":")[2];
		if(splitedText[2].equals(myChannel)) {
			System.out.println("A message on my chanel : "+chatMessage); 
		} else if(splitedText[2].equals(myNickName)) {
			System.out.println("A private message for me : "+chatMessage); 
		} else {
			System.out.println(">>> "+line);
		}

	}
	
	public void handleResponceForChanelListCommand(String line) throws InterruptedException, IOException {
		String splitedText[] = line.split(" ");
		System.out.println("Channel Name "+splitedText[3]); 

	}
	
	private void checkForBlockedWords(String toCheckIn) throws InterruptedException, IOException {
		boolean containsBlockedWord=false;
		for(String i : blockWordNotifier) {
			if(toCheckIn.contains(i)) {
				containsBlockedWord=true;
				break;
			}
		}
		if(containsBlockedWord) {
			sendMessageToAllInChannel("A Blocked Word Was Used");
		}
		
	}

	@Override
	public void run() {
		// this method is responsible of reading any new content from the server
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader breader = new BufferedReader(inputStreamReader);
			String line = null;
			while (true) {
				Thread.sleep(50);
				while ((line = breader.readLine()) != null) {

					String splitedText[] = line.split(" ");

					String code = splitedText[1];  // Define any listener here
					if (line.contains("PING")) { // The server send a PING message to the connected client periodically . this listener has been placed to respond the the server . it acts as a keep-alive so the client is not disconnected 
						String pongserver = line.split(":")[1].trim();
						System.out.println("Recived A Ping message"); 
						writeToStream("PONG "+pongserver);
					} else if (code.equals("322")) {
						handleResponceForChanelListCommand(line);
						
					} else if (code.equals("PRIVMSG")) {
						privateMessageHandler(line);
						

					} else {
						System.out.println(">>> "+line); // Output the test to console for a code that i have not handled
					}

				}
			}

			
		} catch (Exception ex) {
			// an exception occurred while reading data from the server basically this point should not be reached.
			System.err.println("Error in new content listener!!!");
			ex.printStackTrace();
			System.exit(0);
		}



	}

	private void writeToStream( String str) throws IOException {

		objSender.write(str + "\r\n"); // send the formated command to server ends with breakline character
		objSender.flush();

	}


}
