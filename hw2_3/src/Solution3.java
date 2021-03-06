import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution3.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output4.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution3

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution3
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution3   // 0.5초 수행
       timeout 1 java Solution3     // 1초 수행
 */

class Solution3 {
	static final int max_n = 100000;

	static int n;
	static String s;
	static int Answer;

	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input3.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output3.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input3.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output3.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 파일에서 읽어옵니다.
               첫 번째 행에 쓰여진 문자열의 길이를 n에 읽어들입니다.
               그 다음 행에 쓰여진 문자열을 s에 한번에 읽어들입니다.
			 */
			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken());
			s = br.readLine();
		
			/////////////////////////////////////////////////////////////////////////////////////////////
		
			// 입력된 문자열과 그 문자열을 뒤집은 문자열의 최장 공통 부분순서를 구하면 자동으로 회문 문제도 해결된다
			String revS = new StringBuilder(s).reverse().toString();
			
			// C[i][j] = s[0~i-1]와 revS[0~j-1]의 최장 공통 부분순서의 길이
			int[][] C = new int[n+1][n+1];
			
			// 한 문자열이 null이면 최장 공통 부분순서는 존재하지 않는다
			for(int i=0 ; i<=n ;i++){
				C[i][0] = 0;
				C[0][i] = 0;
			}
			
			// 구체적인 알고리즘은 수업시간에 배운 방식과 동일하다
			// 시간복잡도는 for문이 두 번 중첩되었으므로 O(n^2)이다
			for(int i=1 ; i<=n ; i++){
				for(int j=1 ; j<=n ; j++){
					if(s.charAt(i-1) == revS.charAt(j-1)){
						C[i][j] = C[i-1][j-1] + 1;
					} else {
						C[i][j] = Math.max(C[i-1][j], C[i][j-1]);
					}
				}
			}
			
			Answer = C[n][n];
			
			/////////////////////////////////////////////////////////////////////////////////////////////
			
			// output4.txt로 답안을 출력합니다.
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

