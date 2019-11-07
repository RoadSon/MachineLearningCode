package ML.MCTS;

import java.util.ArrayList;

public class AITOAI {
	static final int AI_1 = 1;
	static final int AI_2 = -1;
	public static void main(String[] args) {
		int AI1_WIN_COUNT = 0;
		int AI2_WIN_COUNT = 0;
		int AI1_AI2_EQUAL = 0;
		for(int i=0;i<100;i++) {
			int turn = AI_1;
			Game game = new Game(null,turn);
			MCTS ai1 = new MCTS();
			MCTSOpponent ai2 = new MCTSOpponent();
			while(true) {
				State currentState = game.getState();
				//System.out.println(Arrays.deepToString(currentState.getBoard()));
				ArrayList<Integer> action = new ArrayList<>();
				if(turn == AI_1) {
					action = ai1.takeBestAction(currentState);
					if(action != null)
						System.out.println("AI_1在"+(action.get(0)+1)+","+(action.get(1)+1)+"落子");
				}
				else if(turn == AI_2){
					action = ai2.takeBestAction(currentState);
					if(action != null)
						System.out.println("AI_2在"+(action.get(0)+1)+","+(action.get(1)+1)+"落子");
				}
				game.step(action);
				game.print();
				int winner = game.gameResult();
				if(winner == AI_1) {
					AI1_WIN_COUNT += 1;
					System.out.println("AI_1 WIN");
					break;
				}
				else if(winner == AI_2) {
					AI2_WIN_COUNT += 1;
					System.out.println("AI_2 WIN");
					break;
				}
				else if(winner == 2) {
					AI1_AI2_EQUAL += 1;
					System.out.println("平局");
					break;
				}
				turn = -turn;
			}
		}
		System.out.println("AI1的胜利次数："+AI1_WIN_COUNT);
		System.out.println("AI2的胜利次数："+AI2_WIN_COUNT);
		System.out.println("平局次数："+AI1_AI2_EQUAL);
	}
}
