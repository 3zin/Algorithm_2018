import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution1 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution1.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output4.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution1

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution1
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution1   // 0.5초 수행
       timeout 1 java Solution1     // 1초 수행
 */

class Solution1 {
	static final int max_n = 200;

	static int n;
	static int[][] A = new int[max_n][max_n];
	static int Answer;	
	
	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input4.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output4.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input1.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output1.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 파일에서 읽어옵니다.
			   먼저 정사각 행렬의 크기를 n에 읽어들입니다.
			   그리고 첫 번째 행에 쓰여진 n개의 숫자를 차례로 A[0][0], A[0][1], ... , A[0][n-1]에 읽어들입니다.
			   마찬가지로 두 번째 행에 쓰여진 n개의 숫자를 차례로 A[1][0], A[1][1], ... , A[1][n-1]에 읽어들이고,
			   세 번째 행에 쓰여진 n개의 숫자를 차례로 A[2][0], A[2][1], ... , A[2][n-1]에 읽어들입니다.
			 */
			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken());
			for (int i = 0; i < n; i++) {
				stk = new StringTokenizer(br.readLine());
				for (int j = 0; j < n; j++) {
					A[i][j] = Integer.parseInt(stk.nextToken());
				}
			}
			
			/////////////////////////////////////////////////////////////////////////////////////////////

			Solution1.preProcess(A, n); // O(n^2) 
			Answer = Solution1.maxSumOfSubMatrix(A, n); // O(n^3)
			
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

	/* 홀수 판정 함수 */
	public static boolean isOdd(int x) {
		return x % 2 == 1;
	}

	/* 주어진 matrix의 홀수 칸을 음수로 변환하는 전처리 함수 */
	// 시간복잡도는 O(n^2)이다
	public static void preProcess(int[][] A, int len) {
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				if (Solution1.isOdd(A[i][j])) {
					A[i][j] = -A[i][j];
				}
			}
		}
	}

	/* 1차원 배열의 최대 부분합을 구하는 함수 */
	// 시간복잡도는 O(n)이다
	public static int maxSumOfSubArray(int[] A, int len) {
		int sum = 0;
		int maxSum = 0;
		boolean isAllNegative = true;

		for (int i = 0; i < len; i++) {
			if (sum + A[i] > 0) {
				isAllNegative = false;
				sum += A[i];
			} else { // 부분합이 0이하로 떨어졌을 경우 초기화한다
				sum = 0;
			}
			if (maxSum < sum) {
				maxSum = sum;
			}
		}

		if (isAllNegative) { // 원소가 모두 음수일 경우 가장 0에 가까운 음수가 곧 최대 부분합이다
			int maxNeg = Integer.MIN_VALUE;
			for (int i = 0; i < len; i++) {
				if (A[i] > maxNeg) {
					maxNeg = A[i];
				}
			}
			maxSum = maxNeg;
		}
		return maxSum;
	}

	/* 2차원 배열의 최대 부분합을 구하는 함수 */
	// 총 세 개의 for문이 중첩되어 있으므로 본 함수의 시간복잡도는 O(n^3)이다 (maxSumOfSubArray의 복잡도는
	// O(n)이므로, 이는 세 개의 for문이 중첩된 것과 동일하다)
	public static int maxSumOfSubMatrix(int[][] A, int len) {
		int max = Integer.MIN_VALUE;
		int[] subMatrix = new int[len];
		for (int i = 0; i < len; i++) {
			for (int x = 0; x < len; x++) {
				subMatrix[x] = 0;
			}
			// i~n의 범위에서 사각형의 가로 길이를 1씩 키워가며 부분합을 순차적으로 구한다
			for (int j = i; j < len; j++) {
				for (int k = 0; k < len; k++) {
					subMatrix[k] += A[j][k]; // 이 정교한 조작을 통해 1차원 배열인 SubMatrix만으로 유동적인 가로 길이를 한꺼번에 처리할 수 있게 된다
				}
				int tempMax = Solution1.maxSumOfSubArray(subMatrix, len); // 가로 길이가 고정된 상태에서 세로방향의 최대 부분합
				if (tempMax > max) {
					max = tempMax;
				}
			}
		}
		return max;
	}
}
