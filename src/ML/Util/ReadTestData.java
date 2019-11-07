package ML.Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadTestData {
	private static ArrayList<ArrayList<String>> testData = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> readTestData(String path) throws FileNotFoundException {
		FileInputStream file = new FileInputStream(path);
		InputStreamReader ir = new InputStreamReader(file);
		BufferedReader br = new BufferedReader(ir);
		String line = null;
		try {
			line = br.readLine();
			String[] temp = line.split(",");
			while((line = br.readLine())!=null) {
				temp = line.split(",");
				ArrayList<String> lineTestData = new ArrayList<String>();
				for(int i = 0 ; i<temp.length;i++) {
					lineTestData.add(temp[i]);
				}
				testData.add(lineTestData);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testData;
	}
}
