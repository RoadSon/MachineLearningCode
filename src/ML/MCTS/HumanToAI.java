package ML.MCTS;

import java.util.ArrayList;

public class HumanToAI {
	static final int AI_X = 1;
	static final int HUMAN_O = -1;
	public static void main(String[] args) {
		int turn = HUMAN_O;
		Game game = new Game(null,turn);
		Human human = new Human();
		MCTS ai = new MCTS();
		while(true) {
			State currentState = game.getState();
			//System.out.println(Arrays.deepToString(currentState.getBoard()));
			ArrayList<Integer> action = new ArrayList<>();
			if(turn == AI_X) {
				action = ai.takeBestAction(currentState);
				if(action != null)
					System.out.println("AI在"+(action.get(0)+1)+","+(action.get(1)+1)+"落子");
			}
			else if(turn == HUMAN_O){
				action = human.takeAction(currentState);
				if(action != null)
					System.out.println("Human在"+(action.get(0)+1)+","+(action.get(1)+1)+"落子");
			}
			game.step(action);
			game.print();
			int winner = game.gameResult();
			if(winner == AI_X) {
				System.out.println("AI WIN");
				break;
			}
			else if(winner == HUMAN_O) {
				System.out.println("Human WIN");
				break;
			}
			else if(winner == 2) {
				System.out.println("平局");
			}
			turn = -turn;
		}
	}
}
