package ML.MCTS;

import java.util.ArrayList;
import java.util.Scanner;

public class Human {
	public  ArrayList<Integer> takeAction(State currentStation) {
		while(true) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("以 i,j 形式输入你的落子，例如1,2:");
			try{
				String[] splitIn = scanner.nextLine().split(",");
				ArrayList<Integer> in = new ArrayList<>();
				in.add(Integer.parseInt(splitIn[0])-1);
				in.add(Integer.parseInt(splitIn[1])-1);
				if(!currentStation.getAvailableAction().contains(in)) {
					System.out.println("输入的位置已经有棋子,请重新输入");
				}
				else {
					return in;
				}
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("输入格式有误，请重新输入");
			}


		}
	}
	public static void main(String[] args) {
//		int[][] board = {{1,1,-1},{-1,0,-1},{-1,-1,1}};
//		State state = new State(board,1);
//		System.out.println(state.getAvailableAction().toString());
//		System.out.println(takeAction(state));
	}
}
