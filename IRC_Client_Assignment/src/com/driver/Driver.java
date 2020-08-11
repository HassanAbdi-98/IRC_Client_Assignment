package com.driver;

public class Driver {
	
	public static void main(String args[]) {
		//Our codes entry point i.e. starts here
		
		IRCClient myClient=null;
		try {
			// Get a Random Public server form this website https://www.mirc.com/servers.html
			// I choose https://freenode.net/kb/answer/chat
			// command help https://www.mirc.com/help/html/index.html?basic_irc_commands.html
			myClient=new IRCClient("chat.freenode.net",8000);
			myClient.setNick("iftekhar-dasdasdasda");
			myClient.addWordsToBlockedWordsList("Hack","Crack","Password"); // Added for the creative part of the assignment to if any one uses this works on the channel all are notified 
			//myClient.listChannels(); Thread.sleep(5000); // waiting to see the channel list
			myClient.joinChannel("#alb-linux"); // change this with any channel listed by the findChannel command
			Thread.sleep(5000); 
			myClient.getMemberOfChannel(); // change this with any channel listed by the findChannel command
			Thread.sleep(5000);
			myClient.sendMessageToAllInChannel("First Message On Join");
			System.out.print("");
			
			
			/*while(true) { // Uncomment this to periodilly get a prompt to send to the channel.
			
				String m = JOptionPane.showInputDialog("Type some text [this will be send to every one]");
				if(m!=null && !m.trim().isEmpty()) { // no need to send blank messsage
					myClient.sendMessageToAllInChannel(m);
				}
			}*/
			
			
		} catch (Exception exception) {
			System.err.println("Unable to connect to server you are having a bad day!!!");
			return;
		}
		
		
		
		
	    
		
	}
}
