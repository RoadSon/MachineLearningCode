package ML.KM;

import java.util.ArrayList;
import ML.Util.Util;
import java.io.*;
import java.text.DecimalFormat;

public class Kmeans
{
	static int k = 2;
	static int dimension;
	static int xNum;
	static int iterNum = 500;
	static boolean stopFlag;
	static double [][] cluster;
	static double [][] x;
	static int []clusterIndex;
	static int []clusterCount;
	public static void initU(ArrayList<ArrayList<String>> data) {
		ArrayList<Integer> index = new ArrayList<>();
		for(int i=0;i<k;i++) {
			int randomIndex = (int)(data.size()*Math.random());
			if(!index.contains(randomIndex)) {
				index.add(randomIndex);
				for(int j=1;j<data.get(randomIndex).size();j++) {
					cluster[i][j-1] = Double.parseDouble(data.get(randomIndex).get(j));
				}
			}
			else {
				i -= 1;
			}
		}
	}
	public static void readData(String path,String split) throws IOException
	{
		ArrayList<ArrayList<String>> data = Util.LoadData(path,split);
		dimension = data.get(0).size()-1;
		xNum = data.size();
		cluster = new double[k][dimension];
		x = new double[xNum][dimension];
		clusterCount = new int[k];
		clusterIndex = new int[xNum];
		for(int i=0;i<data.size();i++) {
			for(int j=1;j<data.get(i).size();j++) {
				x[i][j-1] = Double.parseDouble(data.get(i).get(j));
			}
		}
		initU(data);
	}
	public static void updateCluster() {
		for(int i=0;i<xNum;i++){
			double distance=Double.MAX_VALUE;
			for(int j=0;j<k;j++){
				double tempDist = 0.0;
				for(int q=0;q<dimension;q++) {
					tempDist += Math.pow(x[i][q]-cluster[j][q], 2);
				}
				tempDist = Math.sqrt(tempDist);
				if(tempDist<distance){
					distance=tempDist;
					clusterIndex[i]=j;
				}
			}
			clusterCount[clusterIndex[i]]++;
		}
	}
	public static void updateMeans() {
		double [][]clus=new double[k][dimension];
		for(int i=0;i<xNum;i++) {
			for(int j=0;j<dimension;j++) {
				clus[clusterIndex[i]][j]+=x[i][j];
			}
		}
		for(int i=0;i<k;i++) {
			for(int j=0;j<dimension;j++) {
				cluster[i][j]=clus[i][j]/(clusterCount[i]);
			}
			clusterCount[i]=0;
		}
	}
	public static void km()
	{
		for(int i=0;i<iterNum;i++) {
			updateCluster();
			updateMeans();
		}
	}
	public static void main(String[] args) throws IOException {
		readData("D:\\Eclipece-java\\Optimization\\src\\ML\\KM\\watermelon4.0.txt", "\t");
		km();
		DecimalFormat dFormat = new DecimalFormat("0.000");
		for(int i=0;i<k;i++) {
			System.out.print("cluster"+i+"\t");
			for(double clusteri:cluster[i])
				System.out.print(dFormat.format(clusteri)+"\t");
			System.out.println("");
			for(int j=0;j<xNum;j++){
				if(clusterIndex[j] == i) {
					  System.out.println(clusterIndex[j]+"\t"+j+"\t"+x[j][0]+"\t"+x[j][1]);
				}
			}
		}
	}
}