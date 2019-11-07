package ML.Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Util {
	public static ArrayList<ArrayList<String>> LoadData(String path,String split) {
		ArrayList<ArrayList<String>> data = new ArrayList<>();
		FileInputStream file;
		try {
			file = new FileInputStream(path);
			InputStreamReader ir = new InputStreamReader(file);
			BufferedReader br = new BufferedReader(ir);
			String line = null;
			try {
				while((line = br.readLine())!=null) {
					String[] temp = line.split(split);
					ArrayList<String> lineData = new ArrayList<String>();
					for(int i = 0 ; i<temp.length;i++) {
						lineData.add(temp[i]);
					}
					data.add(lineData);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return data;
	}
}
