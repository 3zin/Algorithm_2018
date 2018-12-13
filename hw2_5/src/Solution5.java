import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution5 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution5.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output5.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution5

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution5
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution5   // 0.5초 수행
       timeout 1 java Solution5     // 1초 수행
 */

class Solution5 {
	static final int max_n = 1000;

	static int n, H;
	static int[] h = new int[max_n], d = new int[max_n-1];
	static int Answer;

	public static void main(String[] args) throws Exception {
		
		/*
		   동일 폴더 내의 input5.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output5.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input5.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output5.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 블록의 개수와 최대 높이를 각각 n, H에 읽어들입니다.
			   그리고 각 블록의 높이를 h[0], h[1], ... , h[n-1]에 읽어들입니다.
			   다음 각 블록에 파인 구멍의 깊이를 d[0], d[1], ... , d[n-2]에 읽어들입니다.
			 */
			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken()); H = Integer.parseInt(stk.nextToken());
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < n; i++) {
				h[i] = Integer.parseInt(stk.nextToken());
			}
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < n-1; i++) {
				d[i] = Integer.parseInt(stk.nextToken());
			}

			/////////////////////////////////////////////////////////////////////////////////////////////
			
			// A1[i][j] : 첫 i+1개의 원소로 높이<=j 를 만족하도록 만들 수 있는 경우의 수 중, h[j]의 값을 포함한 경우(즉 구멍에 들어갈 수 있는 경우)
			int[][] A1 = new int[n][H+1];
			// A2[i][j] : 첫 i+1개의 원소로 높이<=j 를 만족하도록 만들 수 있는 경우의 수 중, h[j]의 값을 포함하지 않은 경우(즉 구멍에 들어갈 수 없는 경우)
			int[][] A2 = new int[n][H+1];
			
			// 첫 원소 하나만으로 높이<=j의 탑을 만들 수 있는지 여부를 판단한다 (초기값)
			for(int j=0 ; j<=H ; j++){
				if(h[0]>j){
					A1[0][j] = 0;
				} else {
					A1[0][j] = 1;
				}
				A2[0][j] = 1;
			}
			
			// for문이 두 개 중첩되어 있으므로 시간복잡도는 O(nH)이다 (블록의 개수: n, 최대 높이: H)
			for(int i=1 ; i<n ; i++){
				for(int j=0 ; j<=H; j++){
					// (1) h[i]값을 포함하지 않은 경우
					// 이는 첫 i개의 원소로 높이<=j의 탑을 만들 수 있는 경우의 수와 동일하다
					A2[i][j] = (A1[i-1][j] + A2[i-1][j]) % 1000000;
					
					// (2) h[i]값을 포함한 경우
					// 이는 첫 i개의 원소로 높이<=j-h[i]의 탑 혹은 마지막 부분이 연속될 경우 높이<=j-h[i]+d[i-1]의 탑을 만들 수 있는 경우의 수와 동일하다
					if (j-h[i]>=0){
						A1[i][j] = (A1[i-1][j-h[i]+d[i-1]] + A2[i-1][j-h[i]]) % 1000000;
						
					} else if (j-h[i]+d[i-1]>=0){
						A1[i][j] = (A1[i-1][j-h[i]+d[i-1]]) % 1000000;

					} else { // 이 경우는 불가능
						A1[i][j] = 0;
					}
				}
			}
			
			// 문제의 조건에 따라 블록을 아무것도 올리지 않는 경우를 제외해 준다
			Answer = (A1[n-1][H] + A2[n-1][H]) % 1000000 - 1;
			
			/////////////////////////////////////////////////////////////////////////////////////////////

			// output5.txt로 답안을 출력합니다.
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

