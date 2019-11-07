package ML.KNN;

import java.util.ArrayList;
import java.util.TreeMap;

public class KNN {
	private int k;
	private ArrayList<ArrayList<String>> trainData;
	private TreeMap<Double, String> distLabel;
	public ArrayList<ArrayList<String>> getTrainData() {
		return trainData;
	}
	public void setTrainData(ArrayList<ArrayList<String>> trainData) {
		this.trainData = trainData;
	}
	public TreeMap<Double, String> getDistLabel() {
		return distLabel;
	}
	public void setDistLabel(TreeMap<Double, String> distLabel) {
		this.distLabel = distLabel;
	}
	public KNN(int k) {
		this.k = k;
		distLabel = new TreeMap<>();
		trainData = new ArrayList<>();
	}
	public String predict(ArrayList<String> testData) {
		distLabel = new TreeMap<>();
		TreeMap<String, Integer> resultMap = new TreeMap<>();
		for(ArrayList<String> train:trainData) {
			double dist = Math.sqrt(Math.pow((Double.parseDouble(train.get(1))-Double.parseDouble(testData.get(1))), 2)+Math.pow((Double.parseDouble(train.get(2))-Double.parseDouble(testData.get(2))),2));
			distLabel.put(dist, train.get(3));
		}
		//System.out.println(distLabel);
		int neighbor = this.k;
		for(String result:distLabel.values()) {
			if(neighbor == 0) {
				break;
			}
			if(resultMap.containsKey(result)) {
				resultMap.put(result, resultMap.get(result)+1);
			}
			else {
				resultMap.put(result, 1);
			}
			neighbor -= 1;
		}
		//System.out.println(resultMap.toString());
		for(String result:resultMap.keySet()) {
			return result;
		}
		return null;
	}
}
