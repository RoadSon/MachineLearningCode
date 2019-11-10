我的博客[蒙特卡洛树搜索]( https://blog.csdn.net/weixin_45828785/article/details/102879695 )

# 蒙特卡洛树搜索
## 一、基本思想
要搞清楚蒙特卡洛树搜索的基本思想首先要明白什么是蒙特卡洛树？**蒙特卡洛**指的就是蒙特卡洛方法，又称统计模拟方法，是通过产生随机数来解决问题的方法，例如投针方法来计算Π。**树**就是我们数据结构中学的树结构，注意，这里不是二叉树，每个结点有多个孩子结点。**搜索**就是遍历树，找到最优解。
蒙特卡洛树搜索算法是一种用于决策的启发式搜索算法，在上《人工智能基础》这门课时，接触过几个启发式搜索算法，例如A*算法。启发式搜索算法最重要的就是它的代价函数，A*算法的代价函数为实际距离值+启发函数值，而蒙特卡洛树搜索算法的代价函数就是UCT函数（后面会介绍）。为什么要提到我之前学过的A*算法呢？因为蒙特卡洛树搜索的基本思想和我接触的启发式搜索算法都不同，蒙特卡洛树搜索是通过大量的随机事件来逼近真实结果，而它的启发信息也是通过大量的随机模拟结果反向传播得到的。

## 二、算法流程
蒙特卡洛树搜索算法分为四步，分别是**选择（Selection）、扩展（Expansion）、模拟（Simulate）和反向传播（Backpropagation）**，蒙特卡洛树搜索一个最著名的应用就是AlphaGo，下面以下井字棋为例详细介绍这四步。
  如图2.1表示井字棋中蒙特卡洛树的代表含义（图来源[Monte Carlo Tree Search Beginners Guide](https://int8.io/monte-carlo-tree-search-beginners-guide/)），其中每个结点表示一个游戏状态，根节点就是初始状态（initial state），即游戏刚开始，两人都没落子。父节点在一次行动（a move）后，变成子节点的一种状态，父节点与子节点的边存储了该行为，子节点代表对手操作后的状态，终止结点（terminal node）表示游戏分出胜负的状态，从父节点到终止结点的一次遍历就是一次游戏的开始到结束。
![图2.1井字棋下的蒙特卡洛树](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC1iYjExZmY4MGIwMGVlYzZiLnBuZw?x-oss-process=image/format,png)
### 2.1 选择（Selection）
从根节点开始，根据UCT函数选择一个最有潜力（the most promising）的子结点，直到当前节点的还有可扩展的子节点（表示还没模拟过所有的对手行动），UCT函数的定义如下：
![UCT](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC0wYzI2YjdmN2FjYWRjZDY1LnBuZw?x-oss-process=image/format,png)
其中vi表示当前节点，$v$表示$v_i$的父节点，$Q（v_i）$表示vi节点胜利的次数，$N（v_i）$表示访问vi节点的次数，$N（v）$表示访问v节点的次数，这些参数是通过第四步反向传播得到的。
该公式包含两个部分，左边部分表示该节点的exploitation能力，右边部分表示该节点的exploration能力（exploitation和exploration在师兄的论文也出现过，当时还不是很明白这两个名词的含义），系数c用来控制exploitation和exploration的平衡。
### 2.2 扩展（Expansion）
扩展是对可拓展的节点进行的，即随机添加一个新的子节点。
### 2.3 模拟（Simulate）
模拟是对上一步扩展出来的子节点进行一次模拟游戏，双方随机下子，直到分出胜负。
### 2.4 反向传播（Backpropagation）
从扩展出来的子节点向上回溯，更新所有父节点的Q、N参数，即获胜次数和被访问次数。
***
蒙特卡洛树搜索的算法流程图如图2.2所示。
![图2.2 MCTS算法流程图](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC1kMjkyMTQxODVmMzJjNDM0LnBuZw?x-oss-process=image/format,png)
算法伪代码如下：
```java
	public void MonteCarloTreeSearch() {
		while(timeLeft())  {
			node = root;
			while(node.isFullExpension()) {
				node = Selection(node);
			}
 			new_node = Expansion(node);
			result = Simulation(new_node);
 			Backpropagation(new_node,result);
 		}
	}
 	public Node Selection(Node node) {
		Node selectedNode = new Node();
 		maxUCT = Double.MIN_VALUE;
		for(Node node:node.children) {
			if(node.UCT>maxUCT) {
 				maxUCT = node.UCT;
				selectedNode = node;
			}
 		}
 		return selectedNode;
	}
 	public Node Expansion(Node node) {
 		Node expanNode = randomGenerateChildren();
 		while(node.children.contains(expanNode))
 			expanNode = randomGenerateChildren();
		return expanNode
 	}
 	public Result Simulation(Node new_node) {
 		Node node = new_node.copy();
 		while(!node.isTerminate()) {
 			node = randomGenerateChildren();
 		}
 		return node.result;
 	}
 	public void Backpropagation(Node new_node,Result result) {
 		while(new_node.parent != null) {
			new_node.N += 1;
 			new_node.Q += result;
 			new_node = new_node.parent;
 		}
	}
```
## 三、应用实现
应用：实现基于蒙特卡洛树搜索的井字棋程序。如图3.1所示显示了模拟结果以及反向传播结果，首先我在最中间下了一个子，AI通过MCTS算法的四步来选择最佳的下子位置。
![图3.1 模拟结果](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC0yNTZiMTJkNDkyODE1NmE2LnBuZw?x-oss-process=image/format,png)
我创建了两个AI对象，让它们自己下棋，两个AI对象的部分参数不同，来比较这些参数对结果的影响。
#### 1. 探究模拟次数对结果的影响
设置AI1的模拟次数为10，AI2的模拟次数为200，进行100次游戏，结果如图3.2所示。
![图3.2 模拟次数不同的AI对决结果](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC1lODcxNzU3N2QyM2EzNmIxLnBuZw?x-oss-process=image/format,png)
通过图3.2可以看出模拟次数多的AI2胜利次数要远大于AI1。
当我设置AI1的模拟次数为100，AI2的模拟次数为500时，AI1的胜利次数为7，AI2的胜利次数为1，平局次数为92，这可能时AI2的模拟次数设置过大导致过拟合造成的。
#### 2. 探究UCT参数c对结果的影响
设置AI1的参数c为0，AI2的参数c为1.4，模拟次数均设为100，结果如图3.3所示。
![图3.3 参数c不同的AI对决结果](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xNzI5MDM5OC1mOTNmOGVlZDUxOGIzODE5LnBuZw?x-oss-process=image/format,png)
上面介绍UCT函数时提到过系数c是用来控制exploitation和exploration的平衡，理论上c取0的效果应该是不理想的，因为这样就只选择exploitation大的子节点，而没有考虑节点的exploration能力，但是图3.3的结果表明，当c取0和c取1.4时，两个算法找到最优解的能力相当，c取0的结果甚至比c取1.4的结果略好一些。我觉得可能是因为井字棋过于简单，能探索的子节点本身就很少，导致节点的exploration能力对结果的影响被削弱了。
***
在用Java实现基于蒙特卡洛树搜索的井字棋程序时，碰到了下面的一些问题，虽然算法的流程很简单，但是下面踩的“坑”让我debug了好长时间。
1. Java的深拷贝和浅拷贝，最开始没有考虑到深拷贝的问题，在编写部分函数需要复制Java对象，而且只复制对象的属性，不引用该对象，即两个对象的地址不同，我印象中有两个深拷贝的“坑”：
- 二维数组的深拷贝和浅拷贝
**浅拷贝：**
```java
int[][] b1 = {{1,1,-1},{-1,0,-1},{-1,-1,1}};
int[][] b2 = b1.clone();
System.out.println(b1 == b2);
```
输出为false，一开始我天真的以为，b1和b2的地址都不同，改变b1的值，b2应该不会变，但是这种情况是浅拷贝，改变b2数组的值b1也会跟着变。 
**&ensp;&ensp;&ensp;深拷贝：**
```java
int[][] b1 = {{1,1,-1},{-1,0,-1},{-1,-1,1}};
int[][] b2 = new int[b1.length][b1[0].length];
for(int i=0;i<b1.length;i++) {
	b2[i] = b1[i].clone();
}
```
- 对象的深拷贝
最开始用的implements Cloneable方法实现对象之间的复制，后来发现这个方法要实现对象里面的引用属性的复制比较麻烦，改用序列化的方法来实现对象的深拷贝，编写了CloneUtils类的静态clone方法，每次调用该方法拷贝对象即可。
2. 在计算UCT函数时，没有考虑精度问题，**(double)-qualityValue/visitedTime和(double)(-qualityValue/visitedTime).即(double)1/2 = 0.5,而(double)(1/2)=0.0**,这导致在通过UCT函数选取下一步的动作时，大部分的子节点的UCT值都为0，所以我自己跟基于MCTC的AI下时总时我赢。这个地方其实还有一个“坑”，就是前面提到UCT函数是Q/N，而实现的时候是-Q/N，因为子节点的获胜次数表示的是对手的获胜次数，所以要在前面加个负号。
3. 我把判断游戏状态和获取游戏赢家函数用一个函数实现的，思路是遍历棋盘数组（3x3的二维数组,AI下子就是把对应位置的二维数组的值置1，Human则置-1，如果没子，则为0）获取横向三行和纵向三行以及两个斜行的和，如果其中有和等于3，则返回赢家为AI，如果有和等于-3，则返回赢家为Human，其他情况则返回0，表示游戏没结束。这种思路漏掉了一种情况，就是游戏已经结束了，没有子可以下了，为平局，但是还是会返回0，表示游戏没结束，这就导致模拟游戏playOut函数出错了，因为模拟游戏的思路就是从选定节点开始，在可以走子的位置随机走子直到游戏结束，但是当判断游戏状态函数返回的是0表示游戏没结束，而此时已经没有位置可以走子了，这时候会报空指针的错误，无法继续随机走子。
