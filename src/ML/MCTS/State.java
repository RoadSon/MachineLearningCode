package ML.MCTS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class State implements Serializable{
	private static final long serialVersionUID = 3290938713942784516L;
	private int player;
	private int[][] board;
	public boolean equals(State obj) {
		return this.player == obj.getPlayer() && Arrays.deepEquals(this.board, obj.getBoard());
	}
	public State(int[][] board,int player) {
		this.board = CloneUtil.clone(board);
		this.player = player;
	}
	public ArrayList<ArrayList<Integer>> getAvailableAction() {
		ArrayList<ArrayList<Integer>> availableAction = new ArrayList<>();
		for(int i=0;i<board.length;i++) {
			for(int j=0;j<board.length;j++) {
				if(board[i][j] == 0) {
					ArrayList<Integer> oneAciton = new ArrayList<>();
					oneAciton.add(i);
					oneAciton.add(j);
					availableAction.add(oneAciton);
				}
			}
		}
		return availableAction;
	}
	public int getWinner() {
		int winner = 0;
		ArrayList<Integer> sumResult = new ArrayList<>();
		for(int i=0;i<board.length;i++) {
			int rowSum = 0;
			for(int j=0;j<board[0].length;j++) {
				rowSum += board[i][j];
			}
			sumResult.add(rowSum);
		}
		for(int j=0;j<board[0].length;j++) {
			int lineSum = 0;
			for(int i=0;i<board.length;i++) {
				lineSum += board[i][j];
			}
			sumResult.add(lineSum);
		}
		int diagSum = 0;
		for(int i=0;i<board.length;i++) {
			diagSum += board[i][i];
		}
		sumResult.add(diagSum);
		diagSum = 0;
		for(int i=board.length-1;i>=0;i--) {
			int j = board.length-1 - i;
			diagSum += board[i][j];
		}
		sumResult.add(diagSum);
		for(int sum:sumResult) {
			if(sum == board.length) {
				winner = 1;
				return winner;
			}
			else if(sum == -board.length) {
				winner = -1;
				return winner;
			}
			else {
				winner = 0;
			}
		}
		if(getAvailableAction().size() == 0) {
			return 2;
		}
		return winner;
	}
	public State getNextState(ArrayList<Integer> action) {
		int [][] nextBoard = CloneUtil.clone(board);
		nextBoard[action.get(0)][action.get(1)] = this.player;
		State nextState = new State(nextBoard, -this.player);
		return nextState;
	}
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	public int[][] getBoard(){
		return board;
	}
	public static void main(String[] args) {
//		int[][] board = {{1,1,-1},{-1,0,-1},{-1,-1,1}};
//		ArrayList<Integer> action = new ArrayList<>();
//		action.add(1);
//		action.add(1);
//		State state = new State(board,1);
//		State nextState = state.getNextState(action);
//		System.out.println(state.getWinner());
//		System.out.println(nextState.getPlayer());
		int[][] board1 = {{1,1,-1},{-1,0,-1},{-1,-1,1}};
		int[][] board2 = {{1,1,-1},{-1,0,-1},{-1,-1,1}};
		System.out.println(Arrays.deepEquals(board1, board2));
		State s1 = new State(board1, 1);
		State s2 = new State(board2, 1);
		System.out.println(s1.equals(s2));
		System.out.println();
	}
}
