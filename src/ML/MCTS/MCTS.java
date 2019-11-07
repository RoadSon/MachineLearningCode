package ML.MCTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MCTS {
	private Node currentNode = null;
	public void print(Node node) {
		State state = node.getState();
		System.out.println("Q:"+node.getQualityValue()+" N:"+node.getVisitedTime());
		for(int i=0;i<state.getBoard().length;i++) {
			for(int j=0;j<state.getBoard().length;j++) {
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
	public void simulation(int num) {
		Node leaf = null;
		for(int i=0;i<num;i++) {
			leaf = selection();
			int winner = leaf.playOut();
			leaf.backpropagation(winner);
			//print(leaf);
		}
		//print(leaf.getParent());
	}
	public Node selection() {
		Node currentNode = this.currentNode;
		while(true) {
			if(currentNode.getState().getWinner() != 0) {
				break;
			}
			if(currentNode.isFullExpansion()) {
				for(Map.Entry<ArrayList<Integer>, Node> entry : currentNode.selection(0.0).entrySet()) {
					currentNode = entry.getValue();
				}
			}
			else {
				return currentNode.expansion();
			}
		}
		return currentNode;
	}
	public ArrayList<Integer> takeBestAction(State currentState){
		 ArrayList<Integer> action = null;
		 currentNode = new Node(currentState,null);
//		else {
//			System.out.println("aaa");
//			for(Entry<ArrayList<Integer>, Node> entry:currentNode.getChildren().entrySet()) {
//				if(entry.getValue().getState().equals(currentState)) {
//					currentNode = entry.getValue();
//					break;
//				}
//			}
//		}
		simulation(100);
		HashMap<ArrayList<Integer>, Node> selectedNode = currentNode.selection(0.0);
		for(Entry<ArrayList<Integer>, Node> entry:selectedNode.entrySet()) {
			currentNode = entry.getValue();
			action = entry.getKey();
		}
		return action;
	}
}
