import java.util.ArrayList;
import java.io.*;
import java.util.Random;


public class Point {
	private ArrayList<Point> neighbors;
	private int currentState;
	private int nextState;
	private int numStates = 3;
	private int counterInfected;
	private int counterProtected;

	// age parameter ( 1 - child, 2- adult, 3 - eldery);
	private int age;

	public Point() {

		age = randomAge();
		counterProtected = 0;
		counterInfected = 0;
		currentState = 0;
		nextState = 0;
		neighbors = new ArrayList<Point>();
	}

	public Point (int age){
		this();
		this.age = age;
	}

	int randomAge(){
		int random = losuj(85);
		if (random<10)
			return 1;
		else if (random<70)
			return 2;
		else return 3;
	}

	public void clicked() {
		currentState=(++currentState)%numStates;
	}

	public int getState() {
		return currentState;
	}

	public int getAge(){return age;}

	public void setState(int s) {
		currentState = s;
	}

	public int losuj(int limit){
		Random random = new Random();
		return random.nextInt(limit);
	}

	public void calculateNewState() {
		int tmp = 15000;
		int prob = tmp * countInfectedNeighbours() * (1+ 10*(age % 2));

		int num = losuj(3000000);
		//TODO: insert logic which updates according to currentState and
		if ((num - 1< prob)&&(currentState==0) )
			nextState = 1;
		else
			nextState = currentState;
		//
		if(currentState==1)
			counterInfected++;

		if(counterInfected > 140) {
			nextState = 2;
			counterInfected = 0;
		}

		if(currentState == 2)
			counterProtected++;

		if(counterProtected > 2500) {
			nextState = 0;
			counterProtected = 0;
		}

	}

	public void changeState() {
		currentState = nextState;
	}

	public void addNeighbor(Point nei) {
		neighbors.add(nei);
	}

	private int countInfectedNeighbours(){
		int count = 0;
		for (Point n:neighbors) {
			if (n.currentState == 1)
				count += 1;
		}return count;
	}
	//TODO: write method counting all active neighbors of THIS point
}
