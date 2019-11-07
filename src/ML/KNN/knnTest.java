package ML.KNN;

import java.util.ArrayList;

public class knnTest {
	public static void main(String[] args) {
		ArrayList<ArrayList<String>> train =  Util.LoadData("src/ML/KNN/data/watermelon_3a.txt");
		ArrayList<ArrayList<String>> test = Util.LoadData("src/ML/KNN/data/test.txt");
		KNN knn = new KNN(5);
		knn.setTrainData(train);
		int error = 0;
		for(ArrayList<String> testData:test) {
			if(!testData.get(testData.size()-1).equals(knn.predict(testData))) {
				error += 1;
			}
		}
		System.out.println(error);
	}
}