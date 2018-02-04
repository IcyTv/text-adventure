import java.util.Scanner;

public class TextAdventureRunner
{
    public static void main(String args[])
    {
    	//Story Elements

    	
    	Scanner inp = new Scanner(System.in);
        TextAdventure text = new TextAdventure();
        boolean redraw = true;
        
    	System.out.println("Welcome traveler, you have come a long way!\nYour destination is on the other side of this maze.\nIf you turn around now, your travel will be for nothing.");
		boolean no1 = false;
		boolean admin = false;
		
		System.out.println("What is your name?");
		String name = inp.nextLine();
		if(name.toUpperCase().equals("STEPHEN")) {
			System.out.println("I have been waiting for you!");
			text.block();
		} else if(name.toUpperCase().equals("MICHAEL")) {
			System.out.println("Welcome admin!");
			admin = true;
			text.block();
		} else if (name.toUpperCase().equals("JOSEPH")) {
			System.out.println("Easymode enabled!");
		} else {
			System.out.println("Hello " + name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
		}
		
		if(!admin){
			System.out.println("Do you want to continue?");
		}
		
    	while(true){
    		if(admin){
    			break;
    		}
    		String ans = inp.nextLine();
	    	if(ans.toUpperCase().startsWith("NO") && !no1) {
		    		System.out.println("Very well, so you do not desire to find the treasure?");
		    		no1 = true;
	    	} else if(ans.toUpperCase().startsWith("NO") && no1) {
	    		System.out.println("Farewell then... and have safe travels back to your home!");
	    		inp.close();
	    		return;
	    	} else if(ans.toUpperCase().startsWith("YES")) {
	    		System.out.println("Very well! I will be accompanying you.\nIf you want to know anything, just ask for HELP!\nI will also explain the environment to you and tell you where you can go, to help you on your journey.\n");
	    		break;
	    	} else if(ans.toUpperCase().indexOf("TREASURE") >= 0) {
	    		System.out.println("That is why you traveled all the way out here, isn\'t it?");
	    		if(no1){
	    			System.out.println("Do you still whish to turn back?");
	    		}
	    	} else {
	    		System.out.println("I am sorry, I did not understand that!");
	    	}
    	}
    	
//    	for(int i = 0; i < 9999; i++) {
//    		System.out.println(i + ": " + (char)i);
//    	}
        
        while(true){
        	if(redraw) {
            	if(text.draw()) {
            		break;
            	}
        	}
        	redraw = true;
        	String tmp = inp.nextLine();
        	if(!text.isAttacking()){
	        	if(tmp.toUpperCase().startsWith("GO")){
	            	text.move(tmp);	
	        	} else if(tmp.toUpperCase().startsWith("UNLOCK")) {
	        		text.unlock(tmp);
	        	}
	        	else if (tmp.toUpperCase().startsWith("BREAK")) {
	        		text.breakWall(tmp);
	        		redraw = false;
	        	} else if(tmp.toUpperCase().startsWith("TAKE")) {
	        		String rtmp = tmp.toUpperCase().replace("TAKE ", "");
	    			redraw = false;
	        		if(rtmp.equals("KEY")){
	        			text.pickUpKey();
	        		} else if(rtmp.startsWith("STRENGTH")) {
	        			text.pickUpStr();
	        		} else {
	        			System.out.println("There are no " + rtmp + " to take!");
	        		}
	        	} else if(tmp.toUpperCase().startsWith("REST")) {
	        		redraw = text.rest();
	        	} else if(tmp.toUpperCase().startsWith("MAP")) {
	        		text.map();
	        		redraw = false;
	        	} else if(tmp.toUpperCase().startsWith("POS")) {
	        		text.getPos();
	        		redraw = false;
	        	} else if(tmp.toUpperCase().startsWith("SHOW")) {
	        		String rtmp = tmp.toUpperCase().replace("SHOW ", "");
	        		redraw = false;
	        		if(rtmp.equals("KEYS")){	
	        			System.out.println("From the looks of it, you have " + text.getKeys() + " key(s)!");
	        		} else if(rtmp.equals("STRENGTH")) {
	        			System.out.println("You have " + text.getStr() + " strength left!");
	        			if(text.getStr() < 10) {
	        				System.out.println("You are not very strong, we should rest for a few hours!");
	        			}
	        		} else {
	        			System.out.println("You do not have any " + rtmp);
	        		}
	        	} else if(tmp.toUpperCase().startsWith("HELP")) {
	        		redraw = false;
	        		System.out.println("Of course!\nYou can use the commands\n - GO [direction]  moves in direction\n - UNLOCK [direction]  unlocks a door in the direction\n - BREAK [wall]  breaks a wall in the direction, uses strength\n - TAKE [object]  takes object from room\n - SHOW [keys|strength]  displays amounts\n - MAP  shows a map\n - POSITION  gives current position\n - REST  restores strength, passes time\nAnd in Attack mode:\n - ATTACK  attacks enemy, based on strength\n - FLEE  flees to last room");
	        	} else if(tmp.toUpperCase().startsWith("CREDITS")) {
	        		redraw = false;
	        		System.out.println("I think this maze was built by Michael Finger, if I\'m not mistaken");
	        	} else {
	        		System.out.println("\u001B[91mCommand not supported!\u001B[0m");
	        	}
        	} else {
        		if(tmp.toUpperCase().startsWith("ATTACK")) {
        			if(text.attack()){
        				break;
        			}
        			redraw = false;
        		} else if(tmp.toUpperCase().startsWith("FLEE")){
        			if(text.flee()){
        				break;
        			}
        			redraw = false;
        		} else if(tmp.toUpperCase().startsWith("HELP")){
        			System.out.println("Of course, you can:\n - ATTACK  attacks enemy, based on strength\n - FLEE  flees to last room");
        			redraw = false;
        		} else {
        			System.out.println("\u001B[91mCommand not supported!\u001B[0m");
        		}
        	}
        }
        
        inp.close();
    }
}
