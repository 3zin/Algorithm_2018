import java.util.LinkedList;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution1 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution1.java -encoding UTF8

   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output1.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution1

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution1
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution1   // 0.5초 수행
       timeout 1 java Solution1     // 1초 수행
 */

class Solution1 {
	static final int MAX_N = 10000;
	static final int MAX_E = 500000;

	static int N, E;
	static int[] U = new int[MAX_E], V = new int[MAX_E], W = new int[MAX_E];
	static int[] Answer = new int[MAX_N-1];

	
	public static void DFS_TS(Node[] graph, boolean[] visited, int index, LinkedList<Node> sorted_graph){
		visited[index] = true;
		Node node = graph[index];
		for(Node n : node.getL()){
			if(visited[n.getIndex()] == false){
				DFS_TS(graph, visited, n.getIndex(), sorted_graph);
			}
		}
		sorted_graph.add(0, node);
	}
	
	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input1.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output1.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input1.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output1.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 정점의 개수와 간선의 개수를 각각 N, E에 읽어들입니다.
			   그리고 각 i번째 간선의 시작점, 끝점, 가중치를 각각 U[i], V[i], W[i]에 읽어들입니다. (0 ≤ i ≤ E-1, 1 ≤ U[i] ≤ N, 1 ≤ V[i] ≤ N)
			 */
			stk = new StringTokenizer(br.readLine());
			N = Integer.parseInt(stk.nextToken()); E = Integer.parseInt(stk.nextToken());
			for (int i = 0; i < E; i++) {
				stk = new StringTokenizer(br.readLine());
				U[i] = Integer.parseInt(stk.nextToken());
				V[i] = Integer.parseInt(stk.nextToken());
				W[i] = Integer.parseInt(stk.nextToken());
			}
			
			/////////////////////////////////////////////////////////////////////////////////////////////
			
			// Node 배열, 각각의 Node는 자신 뒤에 연결된(->인접한) Node 정보를 갖는다
			Node[] graph = new Node[N];
			for(int i=0 ; i<N ;i++){
				graph[i] = new Node(i, 0);
			}
			for(int i=0 ; i<E ; i++){
				Node n = new Node(V[i]-1, W[i]);
				graph[U[i]-1].add(n);
			}
			
			// (1) 위상 정렬을 진행한다
			// 새롭게 위상 정렬될 정점 LinkedList
			LinkedList<Node> sorted_graph = new LinkedList<Node>();
			boolean[] visited = new boolean[N];
			for(int i=0 ; i<N ; i++){
				visited[i] = false;
			}
			for(int i=0 ; i<N ; i++){
				if(visited[i] == false){
					DFS_TS(graph, visited, i, sorted_graph);
				}
			}
			
			
			// (2) 위상 정렬 순서대로 최단 경로를 구한다
			int[] D = new int[N];
			for(int i=0 ; i<N ;i++){
				D[i] = Integer.MAX_VALUE; // 임의의 inf
			}
			D[0] = 0;
			for(Node n : sorted_graph) {
				for(Node v : n.getL()) {
					if(D[n.getIndex()] != Integer.MAX_VALUE){
						if(D[n.getIndex()] + v.getWeight() < D[v.getIndex()]){
							D[v.getIndex()] = D[n.getIndex()] + v.getWeight();
						}
					}
				}
			}
			
			for(int i=0 ; i<N-1 ; i++){
				Answer[i] = D[i+1];
			}

			/////////////////////////////////////////////////////////////////////////////////////////////

			// output1.txt로 답안을 출력합니다. 문자 'X'를 출력하기 위해 필요에 따라 아래 코드를 수정하셔도 됩니다.
			pw.print("#" + test_case);
			for (int i = 0; i < N-1; i++) {
				
				if(Answer[i] == Integer.MAX_VALUE){
					pw.print(" " + 'X');
				} else {
					pw.print(" " + Answer[i]);
				}
			}
			pw.println();
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
	private LinkedList<Node> L = new LinkedList<Node>();
	private int index;
	private int weight;
	
	public void add(Node E){
		this.L.add(E);
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public int getWeight(){
		return this.weight;
	}
	
	public LinkedList<Node> getL(){
		return this.L;
	}
	
	public Node(int i, int w){
		this.index = i;
		this.weight = w;
	}

}

