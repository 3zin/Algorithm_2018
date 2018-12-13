import java.util.Arrays;
import java.util.StringTokenizer;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution4.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output4.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution4

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution4
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution4   // 0.5초 수행
       timeout 1 java Solution4     // 1초 수행
 */

class Solution4 {
	static final int max_n = 100000;

	static int n;
	static int[][] A = new int[3][max_n];
	static int Answer;
	
	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input4.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output4.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input4.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output4.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 놀이판의 열의 개수를 n에 읽어들입니다.
			   그리고 첫 번째 행에 쓰여진 n개의 숫자를 차례로 A[0][0], A[0][1], ... , A[0][n-1]에 읽어들입니다.
			   마찬가지로 두 번째 행에 쓰여진 n개의 숫자를 차례로 A[1][0], A[1][1], ... , A[1][n-1]에 읽어들이고,
			   세 번째 행에 쓰여진 n개의 숫자를 차례로 A[2][0], A[2][1], ... , A[2][n-1]에 읽어들입니다.
			 */
			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken());
			for (int i = 0; i < 3; i++) {
				stk = new StringTokenizer(br.readLine());
				for (int j = 0; j < n; j++) {
					A[i][j] = Integer.parseInt(stk.nextToken());
				}
			}

			/////////////////////////////////////////////////////////////////////////////////////////////
			
			// C[i][p] = i+1열이 패턴 p로 놓일 때의 최고 점수. 총 여섯개의 패턴 enum이 있으므로 변수 p의 도메인은 0~5이다.
			int[][] C = new int[n][6];
			
			// 각 패턴에 따른 첫 번째 열의 점수 계산
			for(int p=0 ; p<6 ;p++){
				C[0][p] = Solution4.score(A, 0, Pattern.fromInt(p));
			}
			
			// 구체적인 알고리즘의 얼개는 수업시간에 배운 조약돌 문제와 유사하다 
			// for문이 두 번 중첩되어 있지만, 두 번째 for문은 최대 6번 수행되므로 시간복잡도는 O(n)이다
			for(int i=1 ; i<n ;i++){
				for(int p=0 ; p<6; p++){
					Pattern[] patterns = Pattern.fromInt(p).compatiblePatterns(); // 양립 가능한 패턴들의 목록. 항상 두 개의 패턴이 배열 형태로 리턴된다. 
					C[i][p] = Math.max(C[i-1][patterns[0].ordinal()], C[i-1][patterns[1].ordinal()]) + Solution4.score(A, i, Pattern.fromInt(p));
				}
			}
			
			Answer = Arrays.stream(C[n-1]).max().getAsInt();
			
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
	
	/* 패턴 타입 분류를 위해 만든 enum */
	// 세 칸으로 된 패턴의 위에부터 각 칸을 한 글자씩 대표하여 적는다 (C: circle, T: triangle, X: x)
	public enum Pattern {
		CTX, CXT, TCX, TXC, XCT, XTC;
		public static Pattern fromInt(int x) {
	        switch(x) {
	        case 0:
	            return CTX;
	        case 1:
	            return CXT;
	        case 2:
	            return TCX;
	        case 3:
	            return TXC;
	        case 4:
	            return XCT;
	        case 5:
	            return XTC;
	        default:
	        	return null;
	        }
	    }
		
		/* 주어진 패턴과 양립 가능한 패턴을 반환하는 enum 함수 */
		// 함수를 호출할 시 양립 가능한 패턴이 Pattern 배열의 형태로 반환된다
		public Pattern[] compatiblePatterns(){
			Pattern[] comp;
			switch(this){
			case CTX:
				comp = new Pattern[] {TXC, XCT};
				break;
	        case CXT:
	        	comp = new Pattern[] {TCX, XTC};
	        	break;
	        case TCX:
	        	comp = new Pattern[] {CXT, XTC};
	        	break;
	        case TXC:
	        	comp = new Pattern[] {CTX, XCT};
	        	break;
	        case XCT:
	        	comp = new Pattern[] {CTX, TXC};
	        	break;
	        case XTC:
	        	comp = new Pattern[] {TCX, CXT};
	        	break;
	        default:
	        	comp = null;
	        }
			return comp;
		}
	}
	
	/* pattern에 따른 num 번째 세로줄의 점수 계산 함수 */
	public static int score(int[][] A, int num, Pattern type){
		int result = 0;
		switch (type) {
		case CTX:
			result = A[0][num] - A[2][num];
			break;
		case CXT:
			result = A[0][num] - A[1][num];
			break;
		case TCX:
			result = A[1][num] - A[2][num];
			break;
		case TXC:
			result = A[2][num] - A[1][num];
			break;
		case XCT:
			result = A[1][num] - A[0][num];
			break;
		case XTC:
			result = A[2][num] - A[0][num];
			break;
		default:
		}
		return result;
	}
}

