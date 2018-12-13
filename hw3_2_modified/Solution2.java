import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution2 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution2.java -encoding UTF8

   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output2.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution2

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution2
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution2   // 0.5초 수행
       timeout 1 java Solution2     // 1초 수행
 */

class Solution2 {
	static final int MAX_N = 20000;
	static final int MAX_E = 80000;

	static int N, E;
	static int[] U = new int[MAX_E], V = new int[MAX_E], W = new int[MAX_E];
	static int Answer;

	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input2.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output2.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input2.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output2.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 정점의 개수와 간선의 개수를 각각 N, E에 읽어들입니다.
			   그리고 각 i번째 간선의 양 끝점의 번호를 U[i], V[i]에 읽어들이고, i번째 간선의 가중치를 W[i]에 읽어들입니다. (0 ≤ i ≤ E-1, 1 ≤ U[i] ≤ N, 1 ≤ V[i] ≤ N)
			 */
			stk = new StringTokenizer(br.readLine());
			N = Integer.parseInt(stk.nextToken()); E = Integer.parseInt(stk.nextToken());
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < E; i++) {
				U[i] = Integer.parseInt(stk.nextToken());
				V[i] = Integer.parseInt(stk.nextToken());
				W[i] = Integer.parseInt(stk.nextToken());
			}


			/////////////////////////////////////////////////////////////////////////////////////////////
			
			Node[] graph = new Node[N];
			for(int i=0 ; i<N ; i++){
				graph[i] = new Node();
			}
			
			// Important: 무방향 그래프이므로 양쪽 모두에 간선을 더해주어야 한다
			// idea: Prim 알고리즘은 음수 간선을 문제 없이 처리할 수 있으므로, 가중치를 반대로(음수로) 한다면 자동으로 maximum spanning tree를 찾게 된다
			for(int i=0 ; i<E ; i++){
				graph[U[i]-1].getL().add(new Edge(V[i]-1, -W[i])); // 끝점과 d값이 edge로 저장된다
				graph[V[i]-1].getL().add(new Edge(U[i]-1, -W[i]));
			}
			
			boolean[] visited = new boolean[N];
			for(int i=0 ; i<N ; i++){
				visited[i] = false;
			}
			
			// 자바 프레임워크를 사용해 d값들은 minheap으로 관리한다
			PriorityQueue<Edge> D_heap = new PriorityQueue<Edge>();
			// idea: Prim Algorithm에서 결국 d값은 시작 정점과 연결된 정점으로부터 하나씩 초기화되어 나간다. 
			// 모든 정점을 처음부터 Inf 값으로 초기화하고 heap에 넣는 대신, 갱신이되는 순간 heap에 하나씩 집어넣는 방식으로 시간을 아끼고 알고리즘을 깔끔하게 만들 수 있다.
			// 시작 정점을 0으로 가정한다
			for(Edge n: graph[0].getL()){
				D_heap.offer(n);
			}
			int res = 0;
			visited[0] = true;
			while(D_heap.size() != 0){
				Edge u = D_heap.poll(); // deleteMin()
				if(visited[u.index] == false){ // (1) 이 과정에서 이미 포함된 정점 사이의 간선이나, 혹은 weight가 높아서 선택될 가능성이 없는 간선들은 알아서 걸러지게 된다
					res += u.d;
					visited[u.index] = true;
					for(Edge n: graph[u.index].getL()){
						if(!visited[n.index]){
							D_heap.offer(n); // 일단 heap에 다 넣어주면, 위의 (1)에서 알아서 걸러지게 된다
						}
					}
				}
			}
			/////////////////////////////////////////////////////////////////////////////////////////////
			Answer = -res;


			// output2.txt로 답안을 출력합니다.
			pw.println("#" + test_case + " " + Answer);
			/*
			   아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때,
			   출력한 내용이 실제로 파일에 기록되지 않을 수 있습니다.
			   따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
			pw.flush();
		}

		br.close();
		pw.close();
	}
}

class Node {

	private LinkedList<Edge> L = new LinkedList<Edge>();
	
	public LinkedList<Edge> getL(){
		return this.L;
	}
}

class Edge implements Comparable<Edge> {

	int index;
	int d;
	
	public Edge(int index, int d){
		this.index = index;
		this.d = d;
	}

	@Override
	public int compareTo(Edge o) {	
		if(this.d > o.d){
			return 1;
		} else if(this.d <o.d){
			return -1;
		} else {
			return 0;
		}
	}
}


