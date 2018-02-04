import java.util.Random;

public class Enemy {
	private static String[] types = new String[]{"Warg","Bokbo"};
	protected int attack;
	protected int defense;
	protected int HP;
	
	private boolean attacked;
	
	private String type;
	
	public Enemy(){
		attacked = false;
	}
	public Enemy(String type_){
		type = type_;
		attacked = false;
	}
	public Enemy random(){
		return retType(types[new Random().nextInt(types.length)]);
	}
	public Enemy spawn(String type){
		return retType(type);
	}
	private Enemy retType(String type){
		switch(type){
			case "Warg":
				return new Warg();
			case "Bokbo":
				return new Bokbo();
			case "Ghost":
				return new Ghost();
			case "Sphynx":
				return new Sphynx();
			default:
				return null;
		}
	}
	//Important inheritance methods
	public void setStats(int a, int def, int hp){
		attack = a;
		defense = def;
		HP = hp;
	}
	public String getType() {
		return type;
	}
	public void draw() {
		if(!attacked){
			System.out.println("A wild " + type + " appeared!");
		}
		if(getStats() == null && !attacked){
			System.out.println("Its stats are hidden!");
		} else if(getStats() == null){
			//Stub
		} else {
			int[] tmp = getStats();
			System.out.println("Its attack power is " + tmp[0] + ".");
			System.out.println("Its defense is " + tmp[1] + ".");
			System.out.println("Its health is " + tmp[2] + ".");
		}
	}
	public boolean isDead() {
		return HP <= 0;
	}	
	public int attack(){
		attacked = true;
		return attack;
	}
	public int defend(int att){
		attacked = true;
		if(att-defense >= 0){
			HP -= (att-defense);
		}
		return att-defense;
	}
	public int[] getStats(){
		return new int[]{attack, defense, HP};
	}
	public boolean moves(){
		return false;
	}
}

class Warg extends Enemy{	
	public Warg() {
		super("Warg");
		setStats(new Random().nextInt(9) + 10,new Random().nextInt(3) + 3, new Random().nextInt(7) + 20);
	}
	@Override
	public boolean moves(){
		return (Math.random() < 0.1) && HP < 3;
	}
 }
class Bokbo extends Enemy{
	public Bokbo() {
		super("Bokbo");
		setStats(new Random().nextInt(7) + 6, new Random().nextInt(10) + 10, new Random().nextInt(3) + 5);
	}
	@Override
	public int[] getStats(){
		return null;
	}
}
class Ghost extends Enemy{
	public Ghost() {
		super("Ghost");
		setStats(new Random().nextInt(7) + 6, new Random().nextInt(10) + 3, new Random().nextInt(4) + 15);
	}
	@Override
	public boolean moves(){
		return (Math.random() < 0.2);
	}
}
class Sphynx extends Enemy{
	public Sphynx(){
		super("Sphynx");
		setStats(15, 1, 50);
	}
}
