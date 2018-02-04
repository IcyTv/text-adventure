import java.util.Random;
public class Room {
	private static final String[] possTypes = new String[]{"Cave", "Desert", "Swamp", "Forest", "Woodland Area"};
	public static final String[] directions = new String[]{"NORTH", "EAST", "SOUTH", "WEST"};
	
	private boolean[] doors;
	private boolean[] dL;
	private boolean locked;
	private int[] size;
	private int x;
	private int y;
	private String type;
	private boolean key;
	private boolean strPack;
	private int wStr;
	
	private Enemy enemy;
	
	public Room(int x_, int y_, int[] size_){
		size = size_;
		if(Math.random() < 0.4){
			enemy = new Enemy().random();
		} else {
			enemy = null;
		}
		wStr = new Random().nextInt(6) + 5;
		type = getRandom(possTypes);
		key = (Math.random() < 0.05);
		strPack = (Math.random() < 0.01);
		x = x_;
		y = y_;
		doors = new boolean[] {false, false, false, false};
		dL = new boolean[] {false, false, false, false};
		locked = false;
	}
	public int[] pos() {
		return new int[]{x, y};
	}
	public void remWall(int dir) {
		remWall(dir, false);
	}
	public void remWall(int dir, boolean ignLock) {
		if(!locked || ignLock){
			doors[dir] = true;
		} else {
			System.out.println("You have been here");
		}
	}
	public int breakWall(int dir, int str){
		if((dir == 0 && y == 0) || (dir == 1 && x >= size[0]-1) || (dir == 3 && y >= size[1]-1) || (dir == 4 && x == 0)){
			System.out.println("You cannot break the border walls!");
			return str;
		}
		
		if(str > wStr && !doors[dir]){
			System.out.println("You broke the " + directions[dir] + " wall");
			doors[dir] = true;
			return str - wStr;
		}else if(doors[dir]) {
			System.out.println("There is no wall to break!");
			return str;
		}
		else {
			System.out.println("You don\'t have enough strength left");
			return str;
		}
	}
	public void lockDoor(int dir) {
		dL[dir] = true;
	}
	public void unlockDoor(int dir) {
		dL[dir] = false;
	}
	public boolean doorLocked(int dir) {
		return dL[dir];
	}
	public boolean getDoor(int dir){
		return doors[dir];
	}
	public boolean[] getDoors(){
		return doors;
	}
	public void lock() {
		locked = true;
	}
	public boolean locked() {
		return locked;
	}
	public String getType(){
		return type;
	}
	public boolean hasKey() {
		return key;
	}
	public void pickupKey() {
		key = (Math.random() < 0.01);
	}
	public boolean hasStr() {
		return strPack;
	}
	public void pickupStr() {
		strPack = false;
	}
	public Enemy getEnemy(){
		return enemy;
	}
	public void mvEnemy() {
		enemy = null;
	}
	public boolean hasEnemy(){
		return enemy != null;
	}
	public void spawnEnemy(String type){
		enemy = new Enemy().spawn(type);
	}
	private static String getRandom(String[] array) {
		if(Math.random() > 0.001) {
			int rnd = new Random().nextInt(array.length);
			return array[rnd];
		} else {
			return "Final";
		}
	}
}
