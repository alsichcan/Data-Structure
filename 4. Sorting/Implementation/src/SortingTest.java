import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
				    // 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
					    System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value)
	{ // copy from https://ko.wikipedia.org/wiki/%EA%B1%B0%ED%92%88_%EC%A0%95%EB%A0%AC 의사 코드를 바탕으로 수정
	    int temp;
		for(int i = 0; i < value.length ; i++){
		    for (int j = 0 ; j < value.length - (i + 1);  j++){
                // 인접한 두 수를 비교하여 정렬이 되어있지 않으면 둘을 교환한다.
		        if(value[j] > value[j+1]){
                    temp = value[j];
                    value[j] = value[j+1];
                    value[j+1] = temp;
                } //end if
            } // end for
        }
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{ // 강의노트에서 제시된 개념을 바탕으로 개인적으로 재구성함
	    for (int i = 1 ; i < value.length ; i++){
		    int j = 0;
		    // value[i]가 삽입되어야할 위치를 탐색
	        while (value[i] > value[j]){
		        j++;
            }
            int temp = value[i];
	        // value[i]가 삽입되어야할 위치(j)부터의 원소를 한 칸씩 오른쪽으로 이동시키고 value[i]에 해당하는 값을 삽입한다.
            for (int shift = i; shift > j ; shift--){
	            value[shift] = value[shift - 1];
            }
            value[j] = temp;
        }
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value)
	{ // copy from 강의노트의 HeapSort 의사 코드
        // maxheap 구조 생성
		for (int i = (int) Math.floor((value.length -1)/ 2); i >= 0 ; i--){
			percolateDown(value, i, value.length);
		}

		for (int size= value.length-1 ; size >= 0 ; size--){
			// last node와 root node의 값을 교환함
		    int temp = value[size];
			value[size] = value[0];
			value[0] = temp;
			// maxheap 구조 생성
			percolateDown(value, 0, size);
		}
		return value;
	}

	private static void percolateDown(int[] value, int i, int n){
	    // copy from 강의노트의 percolateDown 의사 코드
		int child = 2*i + 1;
		int rightChild = 2*i + 2;

		// 부모 노드의 값과 자식 노드의 값을 비교하여 자식 노드의 크기가 더 크다면 자식 노드 중 더 큰 값과 부모노드의 값을 교환한다
		if (child <= n-1){
			if((rightChild <= n-1 ) && (value[child] < value[rightChild])){
				child = rightChild;
			}
			if(value[i] < value[child]){
				int temp = value[child];
				value[child] = value[i];
				value[i] = temp;
				// 부모노드의 값이 더이상 자식노드의 값과 교환되지 않을 때까지 진행한다.
				percolateDown(value, child, n);
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value)
	{ // copy from 강의노트의 MergeSort 의사 코드
		if (value.length > 1){
		    // 입력된 배열을 절반으로 분할한다
		    int halfidx = (int) Math.floor(value.length / 2);
		    int[] value1 = new int[halfidx];
		    int[] value2 = new int[value.length - halfidx];
		    for (int i = 0; i < halfidx ; i++){
		        value1[i] = value[i];
            } //end for
            for (int i = halfidx; i < value.length ; i++){
		        value2[i-halfidx] = value[i];
            } //end for

            // 각각의 배열에 대해서 재귀적으로 합병정렬을 수행한다
            value1 = DoMergeSort(value1);
		    value2 = DoMergeSort(value2);

		    // 두 배열을 순서에 맞게 합병한다.
		    value = merge(value1, value2);
        }
        return value;
	}

	private static int[] merge(int[] value1, int[] value2) {
	    // MergeSort의 합병 방식에 대한 강의 내용을 바탕으로 작성
        int[] value = new int[value1.length + value2.length];
        int v1idx = 0; // value1에서 삽입 대상이 되는 원소의 index 값
        int v2idx = 0; // value2에서 삽입 대상이 되는 원소의 index 값

        for (int i = 0; i < value.length; i++) {
            if (v1idx != value1.length && v2idx != value2.length) {
                // value1, value2의 값이 모두 남아있는 경우
                if (value1[v1idx] < value2[v2idx]) {
                    // value1의 값이 들어가는 경우
                    value[i] = value1[v1idx];
                    v1idx++;
                } else if (value1[v1idx] > value2[v2idx]) {
                    // value2의 값이 들어가는 경우
                    value[i] = value2[v2idx];
                    v2idx++;
                } else {
                    // value1의 값과 value2의 값이 같아 함께 들어가는 경우
                    value[i] = value1[v1idx];
                    v1idx++;
                    i++;
                    value[i] = value2[v2idx];
                    v2idx++;
                }
            }// end if
            else if (v1idx != value1.length && v2idx == value2.length) {
                // value1의 값만 남은 경우
                value[i] = value1[v1idx];
                v1idx++;
            } else if (v1idx == value1.length && v2idx != value2.length) {
                // value2의 값만 남은 경우
                value[i] = value2[v2idx];
                v2idx++;
            } else {
                // 정렬이 끝난 경우
                break;
            }
        }//end for
        return value;
    }

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value)
	{ // copy from 강의노트의 QuickSort의 의사 코드
	    if(value.length > 1){
	        int pivot = value[0]; // 배열의 첫 번째 원소를 분할 기준으로 설정

            // pivot을 담는 크기 1짜리 배열 생성
            int[] pvt = new int[1];
	        pvt[0] = pivot;

	        // pivot을 기준으로 배열 value를 분할하여 나오는 두 배열을 담는 변수 parts
	        int[][] parts = partition(value, pivot);

	        // 각 배열에 대해서 재귀적으로 QuickSort 수행
	        parts[0] = DoQuickSort(parts[0]);
	        parts[1] = DoQuickSort(parts[1]);

	        // QuickSort를 통해 정렬이 완료된 배열을 기존의 value에 덮어써서 리턴한다.
            System.arraycopy(parts[0], 0, value, 0, parts[0].length);
            System.arraycopy(pvt, 0, value, parts[0].length, 1);
            System.arraycopy(parts[1], 0 , value, parts[0].length+1, parts[1].length);
        }
		return (value);
	}

	private static int[][] partition(int[] value, int pivot){
	    // QuickSort의 partition 방식에 대한 강의 내용을 바탕으로 구성
	    int last = 0; // pivot보다 작은 원소 중 마지막 원소의 index 값
        int[][] part = new int[2][]; // pivot을 기준으로 분할한 파티션을 담을 배열 part

        // pivot과의 대소비교를 통해서 작은 값은 last index보다 작은 위치에, 큰 값은 last index보다 큰 위치에 오도록 정렬
	    for (int i = 1 ; i < value.length ; i++){
	        if(value[i] <= pivot) {
                int temp = value[i];
                value[i] = value[last + 1];
                value[last + 1] = temp;
                last++;
            }
        }

        part[0] = new int[last]; // pivot보다 작은 수들의 배열
        part[1] = new int[value.length - (last+1)]; // pivot보다 큰 수들의 배열

        // pivot을 기준으로 한 파티션을 각각 배열에 담는다.
        System.arraycopy(value, 1, part[0], 0, last);
        System.arraycopy(value, last+1, part[1], 0, value.length - (last+1));

        return part;
    }

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value)
	{ //copy from http://juff.tistory.com/entry/%EC%A0%95%EB%A0%AC-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98-7-%EA%B8%B0%EC%88%98-%EC%A0%95%EB%A0%ACRadix-Sort
        // Queue를 이용해야한다는 개념을 얻어 스스로 재구성함

        // 입력받은 원소를 음의 정수와 음이 아닌 정수로 구분한다
		ArrayList<Integer> posval = new ArrayList<>(); // 음이 아닌 정수
		ArrayList<Integer> negval = new ArrayList<>(); // 음의 정수

		for(int i = 0 ; i < value.length ; i++){
			if(value[i] >= 0){
				posval.add(value[i]);
			} else{
				negval.add(value[i]);
			}
		}

		// ArrayList를 int[] 형태로 변환
		Integer[] posV = posval.toArray(new Integer[posval.size()]);
		Integer[] negV = negval.toArray(new Integer[negval.size()]);
		int[] pos = Arrays.stream(posV).mapToInt(Integer::intValue).toArray(); // 음이 아닌 정수를 담는 배열
		int[] neg = Arrays.stream(negV).mapToInt(Integer::intValue).toArray(); // 음의 정수를 담는 배열


        // 음이 아닌 정수에 대해 radixSort를 수행한다
		for (int dgt = 0 ; dgt < maxDigit(pos); dgt++){
			sort(pos, dgt);
		}


		// 음의 정수에 대해 radisSort를 수행한다
        // 이 때, 음의 정수 중에서 최소값을 구하여 각 원소에 그 절댓값을 더하여
        // 음이 아닌 정수로 만들어 radixSort를 진행하고 마지막에 더해주었던 절댓값을 다시 뺀다.
        int negMin = getMin(neg); // 음의 정수 중에서 최소값을 구한다

		for(int i = 0; i < neg.length ; i++){
			neg[i] += negMin * -1;
		}

		for (int dgt = 0 ; dgt < maxDigit(neg); dgt++){
			sort(neg, dgt);
		}

		for(int i = 0; i < neg.length ; i++){
			neg[i] -= negMin * -1;
		}


		//radixSort를 통해 정렬이 완료된 배열을 value에 덮어써 리턴한다.
		System.arraycopy(neg, 0, value, 0, neg.length);
		System.arraycopy(pos, 0, value, neg.length, pos.length);
		return (value);
	}

	private static void sort(int[] value, int dgt){
	    // Queue를 이용하여 radixSort를 수행한다

        // 각 자리의 값에 해당하는 Queue를 생성한다.
        Queue<Integer>[] digitBox = new Queue[10];

		for (int i = 0 ; i < 10; i++){
			Queue<Integer> queue = new LinkedList<>();
			digitBox[i] = queue;
		}


		// 정렬 기준이 되는 자리수에 따라 알맞는 Queue에 push한다
		for (int i = 0; i < value.length ; i++){
			digitBox[getDigit(value[i], dgt)].offer(value[i]);
		}

		// 작은 자리수 값을 위한 Queue부터 empty가 될 때까지 pop을 하여 배열 value에 덮어쓰고 리턴한다.
        int idx = 0;
		for (int i = 0; i < 10; i++){
			while (!digitBox[i].isEmpty()){
				value[idx] = digitBox[i].poll();
				idx++;
			} // end while
		} // end for
	} //end sort

	private static int getMin(int[] value){
	    // 음의 정수의 radixSort를 위해 음의 정수 중 최솟값을 구하는 메소드
		int min = value[0];
		for(int i=1 ; i<value.length ; i++){
			if(min > value[i]){
				min = value[i];
			}
		}
		return min;
	}

	private static int maxDigit(int[] value){
	    // 정렬 대상인 배열의 가장 큰 자릿수를 구하는 메소드
		int max = value[0];
		for (int i =1; i < value.length ; i++){
			if (max < value[i]){
				max = value[i];
			}
		}
		return Integer.toString(max).length();
	}

	private static int getDigit(int num, int digit){
	    // num의 뒤에서부터 digit번째 자릿수를 구하여 리턴하는 메소드
		String val = Integer.toString(num);
		if (val.length() < digit + 1){
			return 0;
		} else{
			return Integer.parseInt(Character.toString(val.charAt(val.length() - (digit + 1))));
		}
	}
}
