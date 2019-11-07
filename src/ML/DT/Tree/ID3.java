package ML.DT.Tree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
/**
 * ID3决策树，即以信息增益为准则来进行决策树的划分属性选择
 * @author RoadSon
 *
 */

class Node{
	private String name;
	public Node(String name) {
		this.name = name;
		edge = new ArrayList<String>();
		children = new ArrayList<Node>();
	}
	public String getName(){
		return this.name;
	}
	ArrayList<String> edge;
	ArrayList<Node> children;
}
public class ID3 {
	public ArrayList<ArrayList<String>> trainData;   //训练数据，例如青绿,蜷缩,浊响,清晰,凹陷,硬滑,是
	public ArrayList<ArrayList<String>> testData;	//测试数据
	public ArrayList<String> label;                 //训练数据样本标签，例如色泽,根蒂,敲声,纹理,脐部,触感
	public ArrayList<String> predictResult;			//预测结果
	public ArrayList<String> sumClass;			   //分类的总的类别，例如是，否
	public ID3() {
		label = new ArrayList<String>();
		trainData = new ArrayList<ArrayList<String>>();
		testData = new ArrayList<ArrayList<String>>();
		predictResult = new ArrayList<String>();
		sumClass = new ArrayList<String>();
	}
	
	public void init() {
		try {
			readTrainData("D:\\Eclipece-java\\Optimization\\src\\ML\\DT\\data\\watermelon_2_train.txt");
			readTestData("D:\\Eclipece-java\\Optimization\\src\\ML\\DT\\data\\watermelon_2_test.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getSumClasses();
	}
		
	public String getNumClass(int i,ArrayList<ArrayList<String>> data) {		//获得第i个样本的类别
		return data.get(i).get(data.get(0).size()-1);
	}
	public void readTrainData(String path) throws FileNotFoundException {
		FileInputStream file = new FileInputStream(path);
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
	}
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String> getLabelClass(int i,ArrayList<ArrayList<String>> data){    //获得标签对应的所有类别，例如色泽对应的类别为青绿、乌黑、浅白
		ArrayList<String> labelClass = new ArrayList<String>();
		labelClass.add(data.get(0).get(i));
		for(int ii=0;ii<data.size();ii++) {
			if(labelClass.contains(data.get(ii).get(i))==false) {
				labelClass.add(data.get(ii).get(i));
			}
		}
		return labelClass;
	}
	public void getSumClasses() {		//计算sumClass，即分类总的类别
		sumClass.add(getNumClass(0,trainData));
		for(int i =1;i<trainData.size();i++) {
			if(!sumClass.contains(getNumClass(i,trainData))) {
				sumClass.add(getNumClass(i,trainData));
			}
		}
	}
	public boolean isAllClass(ArrayList<ArrayList<String>> data) {    //判断样本是否全属于同一类别
		if(data.isEmpty()) {
			return true;
		}
		String firstClass = getNumClass(0,data);
		for(int i=1;i<data.size();i++) {
			if(!firstClass.equals(data.get(i).get(data.get(i).size()-1))) {
				return false;
			}
		}
		return true;
	}
	public double Ent(ArrayList<ArrayList<String>> data) {		//计算信息熵
		double plogp =0.0;
		for(int i=0;i<sumClass.size();i++) {
			int oneClass = 0;
			for(int j=0;j<data.size();j++) {
				if(sumClass.get(i).equals(getNumClass(j,data))) {
					oneClass+=1;
				}
			}
			if((double)oneClass/data.size()==0) {
				continue;
			}
			plogp+=((double)oneClass/data.size())*(Math.log((double)oneClass/data.size())/Math.log(2.0));
		}
		return -plogp;
	}
	public double Gain(int i,ArrayList<ArrayList<String>> data) {		//计算信息增益
		double Gain = 0.0;
		ArrayList<String> cutClass = new ArrayList<String>();
		cutClass = getLabelClass(i, data);
		for(int ii=0;ii<cutClass.size();ii++) {
			ArrayList<ArrayList<String>> group = new ArrayList<ArrayList<String>>();
			for(int jj=0;jj<data.size();jj++) {
				if(cutClass.get(ii).equals(data.get(jj).get(i))) {
					group.add(data.get(jj));
				}
			}
			Gain+=((double)group.size()/data.size())*Ent(group);
		}
		Gain = Ent(data) - Gain;
		return Gain;
	}
	public int maxGain(ArrayList<ArrayList<String>> data) {		//选出最大的信息增益，返回索引
		ArrayList<Double> gain = new ArrayList<Double>();
		int index = 0;
		for(int i=0;i<label.size()-1;i++) {
			gain.add(Gain(i,data));		
		}
		double maxGain = gain.get(0);
		for(int i=1;i<gain.size();i++) {
			if(gain.get(i)>maxGain) {
				maxGain = gain.get(i);
				index = i;
			}
		}
		return index;
	}
	public Node createTree(ArrayList<ArrayList<String>> data) {
		int maxGainIndex = maxGain(data);//初始选出最大信息增益对应的属性，即最优划分属性
		Node node = new Node(label.get(maxGainIndex));//创建根结点，名称为该属性名称
		ArrayList<String> cutClass = new ArrayList<String>();//cutClass表示该属性的所有类别
		cutClass = getLabelClass(maxGainIndex, data);
		for(int i=0;i<cutClass.size();i++) {
			ArrayList<ArrayList<String>> group = new ArrayList<ArrayList<String>>();//样本子集
			node.edge.add(cutClass.get(i));//该结点的第i条边为cutClass数组的第i个元素，即第i类
			for(int j=0;j<data.size();j++) {
				if(cutClass.get(i).equals(data.get(j).get(maxGainIndex))) {
					group.add(data.get(j));//遍历样本集合data，将样本按属性类别划分成样本子集
				}
			}
			if(isAllClass(group)) {
				Node childNode = new Node(group.get(0).get(group.get(0).size()-1));
				node.children.add(childNode);//对应图4.2的2，5两种情况
			}
			else {
				node.children.add(createTree(group));//继续划分样本子集
			}
		}
		return node;
	}
	public void dfsTree(ArrayList<ArrayList<String>> data) {
		Node node = createTree(data);
		dfs(node);
	}
	public void dfs(Node node) {
		System.out.println("结点："+node.getName());
		for(int i=0;i<node.edge.size();i++) {
			System.out.println(node.getName()+"的第"+(i+1)+"条边："+node.edge.get(i));
			if(node.children.get(i).children.isEmpty()) {
				System.out.println("叶子节点："+node.children.get(i).getName());
			}
			else {
				dfs(node.children.get(i));
			}
		}
	}
	public void predict(Node node,ArrayList<String> inputPredicted) {
		int index = 0;
		for(int i=0;i<label.size();i++) {
			if(label.get(i).equals(node.getName())) {
				index = i;
			}
		}
		for(int i=0;i<node.edge.size();i++) {
			if(inputPredicted.get(index).equals(node.edge.get(i))) {
				if(node.children.get(i).children.isEmpty()) {
					predictResult.add(node.children.get(i).getName());
				}
				predict(node.children.get(i),inputPredicted);
			}
		}
	}
	public void predictAll(String path) {
		Node node=null;
		int count=0;
		node=createTree(trainData);
		dfs(node);
		try {
		BufferedWriter out=new BufferedWriter(new FileWriter(path));
		for(int i=0;i<testData.size();i++) {
			predict(node,testData.get(i));
			for(int j=0;j<testData.get(i).size();j++) {
				out.write(testData.get(i).get(j)+",");
			}
			if(predictResult.get(i).equals(testData.get(i).get(testData.get(i).size()-1))==true) {
				count++;
			}
			out.write(predictResult.get(i));
			out.newLine();
		}
		System.out.println("该次分类结果正确率为："+(double)count/testData.size()*100+"%");
		out.flush();
		out.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void predictAll() {
		Node node=null;
		node=createTree(trainData);
		//dfs(node);
		for(int i=0;i<testData.size();i++) {
			predict(node,testData.get(i));
		}
	}
	public ArrayList<String> getPredictResult() {
		return predictResult;
	}
	public static void main(String[] args) {
		ID3 id3 = new ID3();
		id3.init();
		//id3.dfsTree(id3.trainData);
		id3.predictAll();
	}
}
