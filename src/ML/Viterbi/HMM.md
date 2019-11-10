我的博客[隐马尔可夫模型]( https://blog.csdn.net/weixin_45828785/article/details/102874826 )
#  一、基本思想
隐马尔可夫模型是**基于时序的概率模型**，由**初始状态概率向量Π、状态转移概率矩阵A和观测概率矩阵B**组成。
隐马尔可夫做了两个基本假设：
1. 齐次马尔可夫性假设：任意时刻的状态只与上一时刻的状态有关
2. 观测独立性假设：任意时刻的观测值只与该时刻的状态有关

隐马尔科夫模型主要用来解决三个问题：
1. **概率计算问题**：给定模型λ=(Π,A,B)和观察序列O，求在模型λ下观测序列O出现的概率**P(O|λ)**
2. **学习问题**：已知观察序列O，求模型**λ的三个参数**，使得P(O|λ)最大
3. **预测(解码)问题**：给定模型λ=(Π,A,B)和观察序列O，求在给定观察序列O的基础上，最有可能对应的状态序列I，即**P(I|O)**

# 二、算法流程
## 1. 概率计算问题
### 1）前向算法
![图来源《统计学习方法》](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC1lODZmZDBhN2ExODY3MDBkLnBuZw?x-oss-process=image/format,png)
其中前向概率$\alpha_t(i)$表示t时刻产生观察序列$o_1,o_2,o_3...o_t$且t时刻的状态为i的概率。
1. 公式10.15表示状态为i，观察序列为$o_1$的联合概率
2. 公式10.16中[]里面的内容表示状态为i，观测序列为$o_1,o_2,o_3...o_t$的联合概率，最后乘以$b_i(o_{t+1})$得到状态为i，观察序列为$o_1,o_2,o_3...o_{t+1}$的联合概率
3. 公式10.17表示将所有可能的状态i且对应的观察序列为$o_1,o_2,o_3...o_T$联合概率累加可以得到在模型λ下观测序列O出现的概率P(O|λ)

### 2）后向算法
![图来源《统计学习方法》](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC01OGZhYmE1Y2Q0MWIwYWQ0LnBuZw?x-oss-process=image/format,png)
其中后向概率$\beta_t(i)$表示t+1时刻到T时刻产生观察序列$o_{t+1},o_{t+2},o_{t+3}...o_T$且t时刻的状态为i的概率。
后向算法的思路与前向算法的思路是相似的，只不过前向算法是从第一个观察状态开始往后算，后向算法是从最后一个观察状态开始往前算。
**区别：**
![前向算法](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC1kNGQyMmQ2ZmYyOTBkMTQ1LnBuZw?x-oss-process=image/format,png)
![后向算法](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC1kNmY4NTY4NzU4ZDkwNTExLnBuZw?x-oss-process=image/format,png)

## 2. 学习问题
### Baum-Welch算法(EM算法)
1. EM算法的E步：求Q函数
累加对数似然函数logP(O,I|λ)
2. EM算法的M步：极大化Q函数
通过拉格朗日乘子法求参数A,B,Π
3. 不断迭代，最后得到满足终止条件的的三个参数A,B,Π

## 3. 预测问题
### 维特比算法
用动态规划求最优路径(一条路径对应一个状态序列)
![图来源《统计学习方法》](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC1iMzhjMzEzNzY1MmIyN2ZlLnBuZw?x-oss-process=image/format,png)
其中$\delta_t(i)$表示t时刻观察序列为$o_1,o_2,o_3...o_t$和t时刻状态为i的联合概率，$\varphi _t(i)$存储t-1时刻对应的概率最大的状态。
# 三、应用实现
### 维特比算法实现
假设 $A = \begin{bmatrix}0.5 & 0.2 & 0.3 \\0.3 & 0.5 & 0.2 \\0.2 & 0.3 & 0.5\end{bmatrix}， B=\begin{bmatrix}0.5 & 0.5\\0.4 & 0.6\\0.7 & 0.3\end{bmatrix},\pi=(0.2,0.4,0.4)^T$ ,状态集合Q={1，2，3}，观察集合V = {红，白}已知观察序列O = (红，白，红)，求最优状态序列I。
```java
//1. 初始化
for(int state=0;state<statesNum;state++) {
	delta[0][state] = pi[state] * B[state][o[0]-1];
	w[0][state] = 0;
}
//2. 递推
for(int observe=1;observe<observeNum;observe++) {
	for(int state=0;state<statesNum;state++) {
		double max = -Double.MAX_VALUE;
		int maxIndex = 0;
		for(int states=0;states<statesNum;states++) {
			double temp = delta[observe-1][states] * A[states][state] * B[state][o[observe]-1];
			if(temp > max) {
				max = temp;
				maxIndex = states;
			}
		}
		w[observe][state] = maxIndex;
		delta[observe][state] = max;
	}
}
//3. 终止
int bestPathIndex = 0;
double bestPathPro = -Double.MAX_VALUE;
for(int state=0;state<statesNum;state++) {
	double temp = delta[observeNum-1][state];
	if(temp > bestPathPro) {
		bestPathPro = temp;
		bestPathIndex = state;
	}
}
path[2] = bestPathIndex;
System.out.println("最优路径概率："+String.format("%.4f", bestPathPro));
//4. 最优路径回溯
for(int i=1;i>=0;i--) {
	path[i] = w[i+1][path[i+1]];
}
System.out.print("最优状态序列：");
for(int p:path) {
	System.out.print((p+1)+" ");
}
```
结果如下(结果和《统计学习方法》例10.2一致)：
![维特比算法结果](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC1iMjEwYzc3ZmFhOWVjMGY5LnBuZw?x-oss-process=image/format,png)

