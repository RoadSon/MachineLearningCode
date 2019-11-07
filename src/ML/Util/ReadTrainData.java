package ML.Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadTrainData {
	private static ArrayList<ArrayList<String>> trainData = new ArrayList<>();
	private static ArrayList<String> label = new ArrayList<>();    
	public static ArrayList<ArrayList<String>> readTrainData(String path) {
		FileInputStream file;
		try {
			file = new FileInputStream(path);
			InputStreamReader ir = new InputStreamReader(file);
			BufferedReader br = new BufferedReader(ir);
			String line = null;
			try {
				line = br.readLine();
				String[] temp = line.split(",");
				for(int i = 0 ; i<temp.length;i++) {
					label.add(temp[i]);
				}
				while((line = br.readLine())!=null) {
					temp = line.split(",");
					ArrayList<String> lineTrainData = new ArrayList<String>();
					for(int i = 0 ; i<temp.length;i++) {
						lineTrainData.add(temp[i]);
					}
					trainData.add(lineTrainData);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return trainData;
	}
	public static ArrayList<String> getLabel() {
		return label;
	}
}
