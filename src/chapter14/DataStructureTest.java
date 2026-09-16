package chapter14;

public class DataStructureTest {
    /*
    1. 数据结构概念：
    数据结构，就是一种程序设计优化的方法论，研究数据的`逻辑结构`和`物理结构`以及它们之间相互关系，
    并对这种结构定义相应的`运算`，目的是加快程序的执行速度、减少内存占用的空间。
     * 数据结构 = 研究“数据怎么组织 + 怎么存 + 怎么操作”。
     * 逻辑结构 = 数据之间的关系(谁挨着谁)；物理结构 = 在内存里实际怎么摆放(连续还是分散)；
     * 运算 = 在这种组织方式上能做的增删改查。最终目的就是让程序更快、更省内存。
    2. 数据结构的研究对象
    研究对象1：数据之间的逻辑关系
    > 集合结构：无关系（元素之间“除了同属一个集合，没有任何先后/从属关系”，无法用“一对几”描述）
    > 线性结构：一对一关系，像排队
    > 树形结构：一对多关系，像家谱
    > 图形结构：多对多关系，像地图路线
    研究对象2：数据的存储结构（或物理结构）
    > 顺序结构
    > 链式结构
    > 索引结构
    > 散列结构
    开发中，我们更习惯上用下面的方式理解存储结构：
    > 线性表(一对一关系): 一维数组、单向链表、双向链表、栈、队列
    > 树(一对多关系):各种树。比如：二叉树、B+树
    > 图(多对多关系)
     * 图：由“顶点(存数据)”和“边(表示连接)”组成，任意两个顶点都可能相连，是多对多关系。
     * 例子：地铁线路图(站点是顶点、轨道是边)、社交好友关系网。
    > 哈希表：比如：HashMap、HashSet
    研究对象3：相关的算法操作
    - 分配资源，建立结构，释放资源
    - 插入和删除
    - 获取和遍历
    - 修改和排序
    3. 常见存储结构之：数组
     * 数组是一段连续内存，长度固定，靠下标(从 0 开始)随机访问，取第 i 个元素极快(O(1))；
     * 缺点是中间插入/删除要移动大量元素。
    4. 常见存储结构之：链表
     * 单向链表每个节点只指向“下一个”，只能一个方向走；
     * 双向链表每个节点同时指向“上一个”和“下一个”，能前后两个方向走。区别就在“能不能往回走”。
     * 用途：需要频繁在中间插入/删除、又不太在乎按下标随机访问时用链表；双向链表在需要
     * 反向遍历、或已知某节点想删它自己时更方便(如 java.util.LinkedList、LRU 缓存)。
     * 共同点是都不像数组那样必须连续存储。
     * “单向存两个、双向存三个数据”不准确：节点里真正的“数据”只有一份(data)，
     * 另外那 1 个或 2 个是“指针/引用”，不是数据。单向 = data + next(1 个引用)，
     * 双向 = prev + data + next(2 个引用)，多出来的 prev 就是为了能往回指。
     * 单向： [AA|next]-->[BB|next]-->null
     * 双向： null<--[prev|AA|next]<-->[prev|BB|next]<-->[prev|CC|next]-->null
    链表中的基本单位是：节点(Node)
    4.1 单向链表
     * “单向”体现在 Node 里只有一个 next、没有指向前一个的引用，所以只能顺着 next 往后走。
     * Node next 表示：节点里存着“下一个 Node 的引用(地址)”。Java 允许把“自己这个类型”
     * 当作自己的字段，这叫“自引用”，正是链表能一环扣一环的关键。
     * node1.next = node2; 就是把 node1 的“下一个”指向 node2，从而把两个孤立节点连成 AA -> BB。
    class Node{
        Object data;
        Node next;

        public Node(Object data){
            this.data = data;
        }
    }
    创建对象：
    Node node1 = new Node("AA");
    Node node2 = new Node("BB");
    node1.next = node2;
    4.2 双向链表
     * 演示“双向链表节点”。三个成员是 prev(上一个)、data(数据)、next(下一个)。
     * 两个构造器是“重载”：一个只传 data(prev/next 先为 null)，一个 prev/data/next 一次都传。
     * 参数要按构造器声明的“顺序和位置”一一对应，不能随意换位置(换了顺序含义就错了)。
     * 最后的 node1.next=node2、node2.next=node3 把三个节点从前往后串起来。
     * 补充：这里其实只设置了 next，严格的双向链表还应补上 node2.prev=node1、node3.prev=node2，
     * 否则“往回走”的链就是断的。
    class Node{
        Node prev;
        Object data;
        Node next;

        public Node(Object data){
            this.data = data;
        }

        public Node(Node prev,Object data,Node next){
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }
    创建对象：
    Node node1 = new Node(null,"AA",null);
    Node node2 = new Node(node1,"BB",null);
    Node node3 = new Node(node2,"CC",null);

    node1.next = node2;
    node2.next = node3;
    5. 常见存储结构之：二叉树
     * 树也是用“节点(Node)”连成的，只是节点里存的不是“下一个”，
     * 而是“左孩子 left / 右孩子 right”。所以它叫 TreeNode，表示“树的节点”。
     * 成员含义：left(左子节点引用)、data(本节点数据)、right(右子节点引用)。
     * node1.left = leftNode、node1.right = rightNode：把 node1 的左、右孩子分别接上，
     * 形成 “AA 下面挂着 BB(左) 和 CC(右)” 的结构。
     * “或”之后那份多了一个 parent(父节点引用)：区别在于它能从孩子往上找到父亲，方便向上回溯
     * (如求祖先、平衡树旋转)；上面那份没有 parent，只能自上而下走。作用类似，只是多了“回指父亲”。
     * 它下面的 node1.left/right 作用一样，都是接左右孩子。
     * 三种遍历(以 AA 为根、BB 左、CC 右)用文字图最直观：
     *          AA
     *         /  \
     *       BB    CC
     * 前序(根->左->右)： AA BB CC
     * 中序(左->根->右)： BB AA CC
     * 后序(左->右->根)： BB CC AA
     * 记忆点：“前/中/后”指的是“根”被访问的时机在最前、中间还是最后。
    class TreeNode{
        TreeNode left;
        Object data;
        TreeNode right;

        public TreeNode(Object data){
            this.data = data;
        }

        public TreeNode(TreeNode left,Object data,TreeNode right){
            this.left = left;
            this.data = data;
            this.right = right;
        }
    }
    创建对象：
    TreeNode node1 = new TreeNode(null,"AA",null);
    TreeNode leftNode = new TreeNode(null,"BB",null);
    TreeNode rightNode = new TreeNode(null,"CC",null);

    node1.left = leftNode;
    node1.right = rightNode;
    或
    class TreeNode{
        TreeNode parent;
        TreeNode left;
        Object data;
        TreeNode right;

        public TreeNode(Object data){
            this.data = data;
        }

        public TreeNode(TreeNode left,Object data,TreeNode right){
            this.left = left;
            this.data = data;
            this.right = right;
        }

        public TreeNode(TreeNode parent,TreeNode left,Object data,TreeNode right){
            this.parent = parent;
            this.left = left;
            this.data = data;
            this.right = right;
        }
    }
    创建对象：
    TreeNode node1 = new TreeNode(null,null,"AA",null);
    TreeNode leftNode = new TreeNode(node1,null,"BB",null);
    TreeNode rightNode = new TreeNode(node1,null,"CC",null);

    node1.left = leftNode;
    node1.right = rightNode;

     * 栈只是“规则(先进后出)”，底层用数组、单向链表、双向链表都能实现。
    6. 常见存储结构之：栈(stack、先进后出、first in last out、FILO、LIFO）
    > 属于抽象数据类型（ADT）
    > 可以使用数组或链表来构建
    // 数组实现栈
    class Stack{
         * Object 是所有类的父类，用 Object[] 就能存“任意类型”的元素(字符串、整数包装类等)，
         * 这样这个栈才通用。代价是取出时是 Object，需要强转。更现代的做法是用泛型 Stack<E>，
         * 可在编译期保证类型安全、免去强转。
        Object[] values;
        int size; // 记录存储的元素的个数

        * 构造器不仅能声明参数，方法体里也可以写代码，用来“初始化对象”。
        * 这里 values = new Object[length]; 的作用是：在创建栈时，按传入的 length 开辟一个
        * 定长数组作为底层容器。不写这句，values 就是 null，一 push 就会空指针。
        public Stack(int length){
            values = new Object[length];
        }

          * 入栈(push)=往栈顶放一个元素；出栈(pop)=从栈顶拿走一个元素。永远只在“同一端”操作，
          * 所以后进的先出。实际用途：撤销/回退(Ctrl+Z)、方法调用栈、括号匹配、表达式求值、DFS。
          * ele 是形参名，代表“要入栈的那个元素(element 的简写)”；用 Object 是为了能接收任意类型。
          * push 逻辑：先判断 size 是否已达数组容量(满了就抛异常防越界)，否则把 ele 放到 values[size]，
          * 再 size++ 让计数前移一位。
        // 入栈
        public void push(Object ele){
            if(size >= values.length){
                throw new RuntimeException("栈空间已满，入栈失败");
            }

            values[size] = ele;
            size++;
        }

        // 出栈
        public Object pop(){
            if(size <= 0){
                throw new RuntimeException("栈空间已空，出栈失败");
            }

            * 因为底层是 Object[] 数组，取出的元素静态类型就是 Object，所以用 Object 变量接收最自然。
            * 若想拿到具体类型(如 String)，需要在外面强制类型转换。pop 逻辑：取栈顶 values[size-1]，
            * 把该槽位置 null(帮助垃圾回收、避免“内存泄漏”)，size-- 回退计数，最后把取出的值返回。
            Object obj = values[size - 1];
            values[size - 1] = null;
            size--;
            return obj;
        }
    }
     * 队列(queue)是“先进先出(FIFO)”的结构：从一端进(队尾 add)、另一端出(队头 get)，像排队买票。
     * 和栈的区别就在出入端：栈是“同一端进出(后进先出)”，队列是“一端进另一端出(先进先出)”。
    7. 常见存储结构之：队列(queue、先进先出、first in first out、FIFO)
    > 属于抽象数据类型（ADT）
    > 可以使用数组或链表来构建
    // 数组实现队列
    class Queue{
        Object[] values;
        int size; // 记录存储的元素的个数

        * length 是构造器的“形参”，表示“想开多大的数组(容量)”，只在创建时用一次；
        * size 是成员变量，表示“当前实际存了几个元素”，会随 add/get 变化。
        * 一个是“最大能装多少”，一个是“现在装了多少”。
        public Queue(int length){
            values = new Object[length];
        }

        public void add(Object ele){ // 添加
            if(size >= values.length){
                throw new RuntimeException("队列已满，添加失败");
            }

            values[size] = ele;
            size++;
        }

        public Object get(){ // 获取
            if(size <= 0){
                throw new RuntimeException("队列已空，获取失败");
            }

            Object obj = values[0];

            // 数据前移
            for(int i = 0;i < size - 1;i++){
                values[i] = values[i + 1];
            }

            * 清空搬移后遗留的末位引用，帮助垃圾回收。
            * 这种每次 get 都整体前移(O(n))效率低，通常用“环形队列”(front/rear 两个下标
            * 取模移动)来避免搬移，这是常见的更优实现方式。
            values[size - 1] = null;

            size--;

            return obj;
        }
    }
     */
}
