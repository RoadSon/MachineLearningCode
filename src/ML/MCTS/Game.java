package ML.MCTS;

import java.util.ArrayList;

public class Game {
	private State state;
	private int gameSize = 3;
	private int startPlayer ;
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public int getGameSize() {
		return gameSize;
	}
	public void setGameSize(int gameSize) {
		this.gameSize = gameSize;
	}
	public int getStartPlayer() {
		return startPlayer;
	}
	public void setStartPlayer(int startPlayer) {
		this.startPlayer = startPlayer;
	}
	public Game(State state,int startPlayer) {
		this.startPlayer = startPlayer;
		if(state == null) {
			int[][] board = new int[gameSize][gameSize];
			int player = startPlayer;
			State startState = new State(board, player);
			this.state = startState;
		}
		else {
			this.state = state;
		}
	}
	public void step(ArrayList<Integer> action) {
		if(action != null) {
			this.state = this.state.getNextState(action);
		}
	}
	public int gameResult() {
		return state.getWinner();
	}
	public void print() {
		for(int i=0;i<gameSize;i++) {
			for(int j=0;j<gameSize;j++) {
				if(state.getBoard()[i][j] == 1) {
					System.out.print(" x ");
				}
				else if(state.getBoard()[i][j] == -1) {
					System.out.print(" o ");
				}
				else {
					System.out.print(" . ");
				}
			}
			System.out.println("");
		}
	}
	public static void main(String[] args) {
//		int[][] board = {{1,1,-1},{-1,0,-1},{-1,-1,1}};
//		State state = new State(board,1);
//		ArrayList<Integer> action = new ArrayList<>();
//		action.add(1);
//		action.add(1);
//		State nextState = state.getNextState(action);
//		Game game = new Game(nextState);
//		game.print();
		int[][] b1 = {{1,1,-1},{-1,0,-1},{-1,-1,1}};
		int[][] b2 = new int[b1.length][b1[0].length];
		for(int i=0;i<b1.length;i++) {
			b2[i] = b1[i].clone();
		}
		System.out.println(b1 == b2);//输出为true，因为b2 = b1表示b2引用了b1的地址，b1和b2指向同一个地址，所以b2[0][0]的值也变为0
	}
}
