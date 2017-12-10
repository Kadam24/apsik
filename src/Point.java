import java.util.ArrayList;
import java.io.*;
import java.util.Random;


public class Point {
	private ArrayList<Point> neighbors;
	private ArrayList<Point> friendDistant;
	private ArrayList<Point> friendClose;
	private int limitDistantFriends = 1;
	private int limitCloseFriend = 2;
	private int countCloseFriends;
	private int countDistantFriends;
	private int currentState;
	private int nextState;
	private int numStates = 3;
	private int counterInfected;
	private int counterProtected;
	private int age;

	public Point() {
		age = randomAge();
		countCloseFriends = 0;
		counterProtected = 0;
		counterInfected = 0;
		currentState = 0;
		nextState = 0;
		countDistantFriends = 0;
		neighbors = new ArrayList<Point>();
		friendDistant = new ArrayList<Point>();
		friendClose = new ArrayList<Point>();
	}
	public Point (int age){
		this();
		this.age = age;
	}

	int randomAge(){
		int random = losuj(85);
		if (random<10)
			return 1;
		else if (random<40)
			return 2;
		else return 3;
	}

	public int getAge(){return age;}

	public void clicked() {
		currentState=(++currentState)%numStates;
	}

	public int losuj(int limit){
		Random random = new Random();
		return random.nextInt(limit);
	}

	public int getState() {
		return currentState;
	}

	public void setState(int s) {
		currentState = s;
	}

	public void calculateNewState() {
		int tmp = 15000;
		int tmp2 = 300;
		int tmp3 = 1000;
		int neighbors = countInfectedNeighbors();
		int distantFriends = countInfectedDistantFriends();
		int closeFriends = countInfectedCloseFriends();

		int prob = tmp * neighbors + tmp2 * distantFriends + tmp3*closeFriends * (1+ 100*(age % 2));
		Random random = new Random();
		int num = random.nextInt(3000000);
		//TODO: insert logic which updates according to currentState and
		if ((num < prob)&&(currentState==0) )
			nextState = 1;
		else
			nextState = currentState;
        //System.out.print("elo");
		if(currentState==1)
			counterInfected++;

		if(counterInfected > 140) {
			nextState = 2;
			counterInfected = 0;
		}

		if(currentState == 2)
			counterProtected++;

		if(counterProtected > 1000) {
			nextState = 0;
			counterProtected = 0;
		}

	}

	public int getCountDistant() { return countDistantFriends;}

	public int getCountClose() { return countCloseFriends;}

	public void changeState() {
		currentState = nextState;
	}

	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}

	public boolean addDistantFriend(Point friend) {

		if( countDistantFriends == limitDistantFriends) {
			//System.out.println("PRZEPRASZAM CZY SZYMEK WYJDZIE NA DWÓR?");
			return false;
		}
		else {
			//System.out.printf("kierwa dzialaj \n");
			countDistantFriends++;
			friendDistant.add(friend);
			//System.out.println(countDistant);
			return true;
		}
	}

	public boolean addCloseFriend(Point friend) {

		if (countCloseFriends == limitCloseFriend) {
			return false;
		}
		else{
			countCloseFriends++;;
			friendClose.add(friend);
			return true;
		}
	}

	private int countInfectedNeighbors(){
		int count = 0;
		for (Point n:neighbors) {
			if (n.currentState == 1) {
				count += 1;
			}
		}
		return count;
	}

	private int countInfectedDistantFriends(){
		int count = 0;
		for (Point n:friendDistant) {
			if(n.currentState == 1) {
				count ++;
			}
		}
		return count;
	}

	private int countInfectedCloseFriends(){
		int count = 0;
		for (Point n:friendClose) {
			if(n.currentState == 1) {
				count ++;
			}
		}
		return count;
	}
	}
	//TODO: write method counting all active neighbors of THIS point
