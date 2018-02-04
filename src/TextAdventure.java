import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TextAdventure{
	List<Room> rooms;
	Room current;
	Room last;
	
	private int sx;
	private int sy;
	private String lastDir;
	
	private int keys;
	private int strength;
	private static final int maxStr = 75;
	
	private int time;
	
	private boolean attacking;
	private Enemy enemy;
	private int restNoAtt;
	private int restNum;
	public boolean block;
	
	public TextAdventure(){
		block = false;
		attacking = false;
		keys = 0;
		strength = 30;
		time = 0;
		restNoAtt = 0;
		restNum = 1;
		rooms = new ArrayList<Room>();
		createRooms(15,15);
		current = rooms.get(0);
		for(int i = 0; i < new Random().nextInt(2) + 1; i++) {
			current.remWall(new Random().nextInt(2) + 1);
		}
		current.lock();
		current.mvEnemy();
		last = null;
	}
	
	public boolean draw(){
		if(current.pos()[0] == sx - 1 && current.pos()[1] == sy -1) {
			Scanner inp = new Scanner(System.in);
			while(true){
				System.out.println("There is a Sphynx guarding the exit of the maze.\n\"Who makes it, has no need of it.\nWho buys it, has no use for it. \nWho uses it can neither see nor feel it. \nWhat is it?\"");
				if(inp.nextLine().toUpperCase().indexOf("COFFIN") >= 0){
					System.out.println("\"Correct! How did you guess that?\nI bet you cheated!\nThat is why you will feel my wrath!\"");
					current.spawnEnemy("Sphynx");
					break;
				} else {
					System.out.println("That is not correct!");
				}
			}
			System.out.println("Congratulations traveler! You made it to the end in only " + time + " hours");
			if(current.getEnemy() != null){
				return false;
			} else {
				return true;
			}
		}
		
		if(strength > maxStr){
			strength = maxStr;
		}
	
		
		String out = "";
		if(Math.random() < 0.2){
			out += "We are ";
		} else if(Math.random() < 0.5){
			out += "I think we are ";
		} else {
			out += "It looks like we are ";
		}
		if(current.equals(last)){
			out += "still ";
		}
		out += "in a " + current.getType();
		System.out.println(out);
		out = "";
//		switch(current.getType()) {
//			case "Cave":
//				out += "You can see the water drip from the walls.";
//		}
		
		if(current.getEnemy() != null){
			attacking = true;
			enemy = current.getEnemy();
			enemy.draw();
			if(strength < 10){
				System.out.println("You doged the attack, but you are not very strong!\nWe should flee!");
			} else if( Math.random() < 0.18) {
				System.out.println("You doged the attack! I admire your reflexes!");
			} else {
			
				int pow = enemy.attack();
				if(strength - pow <= 0){
					//DIED
					System.out.println("You died!");
					return true;
				}
				System.out.println("It attacked!\nYou lost " + pow + " strength!");
				strength -= pow;
			}
		} else {
			out = "You can go ";
			for(int i = 0; i < 4; i++){
				if(current.getDoor(i)){
					switch(i){
						case 0:
							if(current.pos()[1] > 0) {
								out += "NORTH, ";
							}
							break;
						case 1:
							if(current.pos()[0] < sx) {
								out += "EAST, ";
							}
							break;
						case 2:
							if(current.pos()[1] < sy) {
								out += "SOUTH, ";
							}
							break;
						case 3:
							if(current.pos()[0] > 0) {
								out += "WEST, ";
							}
							break;
					}
				}
			}
			System.out.println(out.substring(0, out.length()-2));
			if(current.hasKey()) {
				System.out.println("This room has a key");
			}
			if(current.hasStr()) {
				System.out.println("This room has a strength pack");
			}
		}
		return false;
	}

	public void getPos(){
		System.out.println(current.pos()[0] + " " + current.pos()[1]);
	}
	
	public void pickUpKey() {
		if(current.hasKey()) {
    		System.out.println("You took the key!");
			current.pickupKey();
			keys++;
		} else {
			System.out.println("No key available to pick up!");
		}
	}
	
	public void pickUpStr() {
		if(current.hasStr()) {
    		System.out.println("You feel refreshed!");
			current.pickupStr();
			strength += 5;
		} else {
			System.out.println("No strength pack available to pick up!");
		}
	}
	
	public void breakWall(String dir){
		dir = dir.toUpperCase().replace("BREAK ", "");
		int d;
		switch(dir){
			case "NORTH":
				d = 0;
				break;
			case "EAST":
				d = 1;
				break;
			case "SOUTH":
				d = 2;
				break;
			case "WEST":
				d = 3;
				break;
			default:
				System.out.println("Direction not available!");
				return;
		}
		strength = current.breakWall(d, strength);
	}
	public boolean attack(){
		if(!enemy.moves()) {
			int dmg = enemy.defend(strength + new Random().nextInt(10) - 5);
			if(dmg < 0){
				System.out.println("The " + enemy.getType() + " took 0 damage");
			} else {
				System.out.println("The " + enemy.getType() + " took " + dmg + " damage!");
			}
			if(enemy.isDead()){
				System.out.println("The " + enemy.getType() + " died!");
				int r = new Random().nextInt(5) + 1;
				System.out.println("You regained " + r + " strength");
				strength += r;
				attacking = false;
				current.mvEnemy();
				draw();
				return false;
			}
			int pow = -(int) (((strength + 0.1 * (enemy.attack() * ((strength / maxStr)- strength))) * 0.4) - enemy.attack() * 0.6);
			if(strength - pow <= 0){
				//DIED
				System.out.println("You died!");
				return true;
			}
			strength -= pow;
			System.out.println("It attacked!\nYou lost " + pow + " strength!\nYou have " + strength + " strength left");
		} else {
			current.mvEnemy();
			System.out.println("The " + enemy.getType() + " fled!");
			attacking = false;
		}
		return false;
	}
	
	public boolean flee(){
		if(current.pos()[0] == 0 && current.pos()[1] == 0){
			System.out.println("You can\'t flee from here!");
			return false;
		}
		
		String dir = lastDir.toUpperCase().replace("GO ", "");
		if(dir.endsWith("NORTH")){
			dir = "GO SOUTH";
		} else if(dir.endsWith("EAST")) {
			dir = "GO WEST";
		} else if(dir.endsWith("SOUTH")) {
			dir = "GO NORTH";
		} else if(dir.endsWith("WEST")) {
			dir = "GO EAST";
		}

		if(Math.random() > 0.2){
			System.out.println("You successfully fled!");
			attacking = false;
			move(dir);
			return false;
		} else {
			System.out.println("You couldn't flee!");
			int pow = -(int) (strength + 0.1 * (enemy.attack() * ((strength / maxStr)- strength)));
			if(strength - pow <= 0){
				//DIED
				System.out.println("You died!");
				return true;
			}
			strength -= pow;
			System.out.println("It attacked!\nYou lost " + pow + " strength!\nYou have " + strength + " strength left");
			return false;
		}
	}
	
	public boolean isAttacking() {
		return attacking;
	}
	
	public void move(String dir) {
		last = current;
		lastDir = dir;
		time++;
		switch(dir.toUpperCase().replace("GO ", "")) {
			case "NORTH":
				if(current.doorLocked(0)) {
					System.out.println("This door is locked, you need a key to unlock it");
				}
				else if(current.pos()[1] > 0 && current.getDoor(0)){
					current = rooms.get((current.pos()[1] - 1) * sx + current.pos()[0]);
					current.remWall(2, true);
				}
				break;
			case "EAST":
				if(current.doorLocked(1)) {
					System.out.println("This door is locked, you need a key to unlock it");
				}
				else if(current.pos()[0] < sx && current.getDoor(1)){
					current = rooms.get(current.pos()[1] * sx + current.pos()[0] + 1);
					current.remWall(3, true);
				}
				break;
			case "SOUTH":
				if(current.doorLocked(2)) {
					System.out.println("This door is locked, you need a key to unlock it");
				}
				else if(current.pos()[1] < sy && current.getDoor(2)){
					current = rooms.get((current.pos()[1] + 1) * sx + current.pos()[0]);
					current.remWall(0, true);
				}
				break;
			case "WEST":
				if(current.doorLocked(3)) {
					System.out.println("This door is locked, you need a key to unlock it");
				}
				else if(current.pos()[0] > 0 && current.getDoor(3)){
					current = rooms.get((current.pos()[1]) * sx + current.pos()[0] -1);
					current.remWall(1, true);
				}
				break;
			default:
				System.out.println("This is not a direction!");
		}
		
		if(current.pos()[0] == sx-1 && current.pos()[1] == sy-1){
			return;
		}
		
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		if(!current.locked()) {
			for(int i = 0; i < 4; i++) {
				tmp.add(i);
			}
			if(current.pos()[1] == 0) {
				tmp.remove(tmp.indexOf(0));
			}
			if(current.pos()[0] >= sx-1) {
				tmp.remove(tmp.indexOf(1));
			}
			if(current.pos()[1] >= sy-1) {
				tmp.remove(tmp.indexOf(2));
			}
			if(current.pos()[0] == 0) {
				tmp.remove(tmp.indexOf(3));
			}
			Collections.shuffle(tmp);
		}
		for(int i = 0; i < new Random().nextInt(3) + 1; i++) {
			if(!current.locked()){
				int r = tmp.get(i);
				current.remWall(r);
				if(Math.random() < 0.03) {
					current.lockDoor(r);
				}
			}
		}
		current.lock();	
	}
	
	public void unlock(String dir) {
		dir = dir.toUpperCase().replace("UNLOCK ", "");
		if(keys > 0) {
			switch(dir) {
				case "NORTH":
					if(current.doorLocked(0)) {
						keys--;
						current.unlockDoor(0);
						break;
					} else {
						System.out.println("This door is not locked!");
						break;
					}
				case "EAST":
					if(current.doorLocked(1)) {
						keys--;
						current.unlockDoor(1);
						break;
					} else {
						System.out.println("This door is not locked!");
						break;
					}
				case "SOUTH":
					if(current.doorLocked(2)) {
						keys--;
						current.unlockDoor(2);
						break;
					} else {
						System.out.println("This door is not locked!");
						break;
					}
				case "WEST":
					if(current.doorLocked(3)) {
						keys--;
						current.unlockDoor(3);
						break;
					} else {
						System.out.println("This door is not locked!");
						break;
					}
				case "":
					boolean doorExists = false;
					for(int i = 0; i > 4; i++) {
						if(current.doorLocked(i)) {
							keys--;
							current.unlockDoor(i);
							doorExists = true;
							break;
						}
					}
					if(!doorExists) {
						System.out.println("There is no locked door!");
					}
					break;
				default:
					System.out.println("Not a valid direction!");
					break;
			}
		} else {
			System.out.println("You don't have a key!");
		}
	}
	
	public void map() {
		String out = "";
		for(int n = 0; n < sy; n++) {
			for(int i = 0; i < sx; i++) {
				Room r = rooms.get(n * sx + i);
				if(r.locked()) {
					boolean col = false;
					if(r.pos()[0] == current.pos()[0] && r.pos()[1] == current.pos()[1]){
						out += "\u001B[92m";
						col = true;
					} else if(r.hasKey()) {
						out += "\u001B[93m";
						col = true;
					} else if(r.hasStr()) {
						out += "\u001B[94m";
						col = true;
					} else if(r.hasEnemy()) {
						out += "\u001B[91m";
						col = true;
					}
					if(r.getDoor(0) && r.getDoor(1) && r.getDoor(2) && r.getDoor(3)) {
						out += (char)9547;
					} else if(r.getDoor(0) && r.getDoor(1) && r.getDoor(3)) {
						out += (char)9531;
					} else if(r.getDoor(0) && r.getDoor(1) && r.getDoor(2)) {
						out += (char)9507;
					} else if(r.getDoor(1) && r.getDoor(2) && r.getDoor(3)) {
						out += (char)9523;
					} else if(r.getDoor(0) && r.getDoor(2) && r.getDoor(3)) {
						out += (char)9514;
					} else if(r.getDoor(0) && r.getDoor(1)) {
						out += (char)9495;
					} else if(r.getDoor(1) && r.getDoor(2)) {
						out += (char)9487;
					} else if(r.getDoor(2) && r.getDoor(3)) {
						out += (char)9491;
					} else if(r.getDoor(3) && r.getDoor(0)) {
						out += (char)9499;
					} else if(r.getDoor(0) && r.getDoor(2)) {
						out += (char)9550;
					} else if(r.getDoor(1) && r.getDoor(3)) {
						out += (char)9548;
					} else if(r.getDoor(0)) {
						out += (char)9589;
					} else if(r.getDoor(1)) {
						out += (char)9590;
					} else if(r.getDoor(2)) {
						out += (char)9591;
					} else if(r.getDoor(3)) {
						out += (char)9592;
					}
					if(col){
						out += "\u001B[0m";
					}
				} else {
					out += (char)9612;
				}
			}
			out += '\n';
		}
		PrintStream stream;
		try {
			stream = new PrintStream(System.out, true, "UTF-8");
			stream.print(out);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public int getKeys() {
		return keys;
	}
	
	public int getStr(){
		if(strength > maxStr){
			strength = maxStr;
		}
		return strength;
	}
	
	public boolean rest() {
		restNoAtt++;
		if(strength >= 10 && (Math.random() < 0.1 * (restNoAtt * restNum) || block)){
			current.spawnEnemy("Ghost");
			restNoAtt = 0;
			restNum++;
			return true;
		} else if(strength + 10 < maxStr){
			System.out.println("You slept well and feel well rested!");
			strength += 10;
		} else {
			System.out.println("You slept well and feel well rested!");
			strength = maxStr;
		}
		time += 10;
		return false;
	}
	
	public void block() {
		block = true;
	}
	
	public void createRooms(int sx_, int sy_){
		sx = sx_;
		sy = sy_;
		for(int n = 0; n <= sy; n++){
			for(int i = 0; i <= sx; i++){
				try{
				rooms.set(n * sx + i, new Room(i, n, new int[]{sx, sy}));
				} catch(Exception e){
					rooms.add(new Room(i, n, new int[]{sx, sy}));
				}
			}
		}
	}

	public List<Room> getRooms(){
		return rooms;
	}
}
