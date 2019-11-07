package ML.NB.Bayes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.input.PickResult;
/**
 * 朴素贝叶斯算法
 * @author RoadSon
 *
 */
public class NaiveBayes {
	public ArrayList<ArrayList<String>> trainData;   //训练数据，例如青绿,蜷缩,浊响,清晰,凹陷,硬滑,是
	public ArrayList<ArrayList<String>> testData;	//测试数据
	public ArrayList<String> sumClass;			   //分类的总的类别，例如是，否
	public Map<String, Double> priorPro;		   //条件概率
	public final double pi = 3.1415926;
	public NaiveBayes() {
		trainData = new ArrayList<>();
		testData = new ArrayList<>();
		sumClass = new ArrayList<>();
		priorPro = new HashMap<>();
	}
	/**
	 * 读取训练数据
	 * @param path 读取训练数据的路径
	 * @throws FileNotFoundException
	 */
	public void readTrainData(String path) throws FileNotFoundException {
		FileInputStream file = new FileInputStream(path);
		InputStreamReader ir = new InputStreamReader(file);
		BufferedReader br = new BufferedReader(ir);
		String line = null;
		try {
			line = br.readLine();
			String[] temp = line.split(",");
			while((line = br.readLine())!=null) {
				temp = line.split(",");
				ArrayList<String> lineTrainData = new ArrayList<String>();
				for(int i = 0 ; i<temp.length;i++) {
					lineTrainData.add(temp[i]);
					if(i == temp.length-1) {
						if(priorPro.containsKey(temp[i])) {
							priorPro.put(temp[i], (Double)priorPro.get(temp[i])+1);
						}
						else {
							priorPro.put(temp[i], 1.0);
						}
					}
				}
				trainData.add(lineTrainData);
			}
			for(String key:priorPro.keySet()) {
				priorPro.put(key, priorPro.get(key)/trainData.size());
			}
			getSumClasses();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 读取测试数据
	 * @param path 读取测试数据的路径
	 * @throws FileNotFoundException
	 */
	public void readTestData(String path) throws FileNotFoundException {
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
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获得第i个样本的类别
	 * @param i 获取类的索引
	 * @param data 传入样本数据
	 * @return 返回类别
	 */
	public String getNumClass(int i,ArrayList<ArrayList<String>> data) {		//获得第i个样本的类别
		return data.get(i).get(data.get(0).size()-1);
	}
	/**
	 * 获取样本数据的总类别
	 */
	public void getSumClasses() {		//计算sumClass，即分类总的类别
		sumClass.add(getNumClass(0,trainData));
		for(int i =1;i<trainData.size();i++) {
			if(!sumClass.contains(getNumClass(i,trainData))) {
				sumClass.add(getNumClass(i,trainData));
			}
		}
	}
	/**
	 * 判断String类型里是否含有数字
	 * @param content 传入需要判断的String
	 * @return 返回boolean，true表示String中含有数字，false表示不含数字
	 */
    public boolean HasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }
    /**
     * 获取连续型数据的均值
     * @param oneAttriIndex 类别的索引
     * @param label 类别标签
     * @return 返回对应均值
     */
    public double getU(int oneAttriIndex,String label) {
    	double u = 0.0;
    	int count = 0;
    	for(ArrayList<String> oneTrainData:trainData) {
    		if(oneTrainData.get(oneTrainData.size()-1).equals(label)) {
        		u += Double.parseDouble(oneTrainData.get(oneAttriIndex));
        		count += 1;
    		}
    	}
    	u = u/count;
    	return u;
    }
    /**
     * 获取连续型数据的方差
     * @param oneAttriIndex 类别的索引
     * @param label 类别标签
     * @param u 该类数据的均值
     * @return 返回对应的方差
     */
    public double getSigma(int oneAttriIndex,String label,double u) {
    	double sigma = 0.0;
    	int count = 0;
    	for(ArrayList<String> oneTrainData:trainData) {
    		if(oneTrainData.get(oneTrainData.size()-1).equals(label)) {
        		sigma += (Double.parseDouble(oneTrainData.get(oneAttriIndex)) - u) * (Double.parseDouble(oneTrainData.get(oneAttriIndex)) - u);
        		count += 1;
    		}
    	}
    	sigma = sigma/count;
    	return sigma;
    }
    /**
     * 获取条件概率p(x|y)
     * @param oneAttriIndex x对应的索引
     * @param oneAttri x
     * @param label y
     * @return 返回条件概率
     */
	public double getCondiProp(int oneAttriIndex,String oneAttri,String label) {
		double condiPro = 0.0;
		if(HasDigit(oneAttri)) {//无法判断小数点
			double x = Double.parseDouble(oneAttri);
			double u = getU(oneAttriIndex, label);
			double sigma = getSigma(oneAttriIndex, label, u);
			condiPro = (1/(Math.sqrt(2*pi)*Math.sqrt(sigma)))*Math.exp(-((x-u)*(x-u)/sigma));
		}
		else {
			int count = 0;
			for(ArrayList<String> oneTrainData:trainData) {
				if(oneTrainData.get(oneAttriIndex).equals(oneAttri) && oneTrainData.get(oneTrainData.size()-1).equals(label)) {
					count += 1;
				}
			}
			condiPro = (double)(count + 1) / (priorPro.get(label) * trainData.size() + sumClass.size());
		}
		return condiPro;
	}
	/**
	 * 得到最大似然概率
	 * @param oneTestData 一个测试数据
	 * @param label 假设的类别
	 * @return
	 */
	public double getSumCondiPro(ArrayList<String> oneTestData,String label) {
		double sumCondiPro = 0.0;
		double tempCondiPro = 0.0;
		for(int i=0;i<oneTestData.size()-1;i++) {
			tempCondiPro = getCondiProp(i, oneTestData.get(i), label);
			sumCondiPro += Math.log(tempCondiPro);
		}
		return sumCondiPro;
	}
	/**
	 * 预测测试数据的类别
	 * @return
	 */
	public Double predict() {
		ArrayList<Map<String, Double>> sumPre = new ArrayList<>();
		Map<String, Double> predict;
		int count = 0;
		for(int i=0;i<testData.size();i++) {
			predict = new HashMap<>();
			double maxPre = -Double.MAX_VALUE;
			double tempPre = 0.0;
			int index = 0;
			for(int j=0;j<sumClass.size();j++) {
				tempPre = priorPro.get(sumClass.get(j)) * getSumCondiPro(testData.get(i),sumClass.get(j));
				if(tempPre > maxPre) {
					maxPre = tempPre;
					index = j;
				}
			}
			if(testData.get(i).get(testData.get(i).size()-1).equals(sumClass.get(index)))
				count+=1;
			predict.put(sumClass.get(index), maxPre);
			sumPre.add(predict);
		}
		Double precise = (double)count/testData.size();
		return precise;
	}
	public static void main(String[] args) throws IOException {
		for(int i=0;i<10;i++) {
			BufferedWriter bwTrainData = new BufferedWriter(new FileWriter("D:\\Eclipece-java\\Optimization\\src\\ML\\NB\\data\\trainWine.txt"));
			bwTrainData.write("aa");
			bwTrainData.close();
		}

	}
}
