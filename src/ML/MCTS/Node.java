package ML.MCTS;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Node implements Serializable{
	private HashMap<ArrayList<Integer>, Node> children;
	private State state;
	private Node parent;
	private int visitedTime;
	private int qualityValue;
	private ArrayList<ArrayList<Integer>> untriedAciton;
	public State getState() {
		return state;
	}
	public HashMap<ArrayList<Integer>, Node> getChildren() {
		return children;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parents) {
		this.parent = parents;
	}
	public int getVisitedTime() {
		return visitedTime;
	}
	public void setVisitedTime(int visitedTime) {
		this.visitedTime = visitedTime;
	}
	public int getQualityValue() {
		return qualityValue;
	}
	public void setQualityValue(int qualityValue) {
		this.qualityValue = qualityValue;
	}
	public Node(State state,Node parent) {
		this.state = CloneUtil.clone(state);
		this.parent = parent;
		this.visitedTime = 0;
		this.qualityValue = 0;
		this.untriedAciton = state.getAvailableAction();
		children = new HashMap<ArrayList<Integer>, Node>();
	}
	public double UTC(double c) {
		if(visitedTime == 0)
			return 0.0;
		else {
			//System.out.println("2.  "+c+":"+(double)-qualityValue/visitedTime);
			return (double)-qualityValue/visitedTime + c*Math.sqrt(2*Math.log(this.parent.visitedTime)/this.visitedTime);
		}
	}
	public ArrayList<Integer> getRandomAction(ArrayList<ArrayList<Integer>> available){
		int random = (int)(Math.random()*available.size());
		return available.get(random);
	}
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
	public HashMap<ArrayList<Integer>, Node> selection(double c) {
		double maxUTC = -Double.MAX_VALUE;
		HashMap<ArrayList<Integer>, Node> bestChild = new HashMap<>();
		Node bestChildNode = null;
		ArrayList<Integer> bestAction = null;
		for(Map.Entry<ArrayList<Integer>, Node> entry:children.entrySet()) {
			//print(entry.getValue());
			//System.out.println(entry.getValue().UTC(c));
			double tempUTC = entry.getValue().UTC(c);
			if(maxUTC < tempUTC) {
				maxUTC = tempUTC;
				bestChildNode = entry.getValue();
				bestAction = entry.getKey();
			}
		}
		//System.out.println("3.  maxUTC:"+bestChildNode.UTC(c));
		bestChild.put(bestAction,bestChildNode);
		return bestChild;
	}
	public Node expansion() {
		//System.out.println(state.getAvailableAction().toString());
		//System.out.println(untriedAciton.toString());
		ArrayList<Integer> expanAction = (ArrayList<Integer>) untriedAciton.remove(0);
		int currentPlayer = state.getPlayer();
		int nextPlayer = -currentPlayer;
		int[][] nextBoard = CloneUtil.clone(state.getBoard());
		nextBoard[expanAction.get(0)][expanAction.get(1)] = currentPlayer;
		State state = new State(nextBoard, nextPlayer);
		Node expanNode = new Node(state, this);
		children.put(expanAction, expanNode);
		return expanNode;
	}
	public void backpropagation(int winner) {
		visitedTime += 1;
		if(winner == state.getPlayer()) {
			qualityValue += 1;
		}
		else if(winner == -state.getPlayer()){
			qualityValue -= 1;
		}
		if(!isRootNode()) {
			this.parent.backpropagation(winner);
		}
	}
	public boolean isRootNode() {
		return this.parent == null;
	}
	public void print(State stateIN) {
		State state = stateIN;
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
	public int playOut() {
		State currentState = CloneUtil.clone(state);
		while(currentState.getWinner() == 0) {
			if(currentState.getAvailableAction().size() == 0) {
				return 2;
			}
			ArrayList<ArrayList<Integer>> availableAction = currentState.getAvailableAction();
			currentState = currentState.getNextState(getRandomAction(availableAction));
		}
		//System.out.println("模拟结果"+currentState.getWinner());
		//print(currentState);
		return currentState.getWinner();
	}
	public boolean isFullExpansion() {
		return untriedAciton.size() == 0;
	}
}
