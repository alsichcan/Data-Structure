import java.io.*;
import java.util.Stack;

public class CalculatorTest
{
	public static boolean ERROR = false;  // 처리과정에 에러가 있음을 나타내는 변수

    public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
                ERROR = false;  // 매 input 마다 새롭게 에러 여부 판단을 위한 초기화
			    String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;
				command(input);
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input) {
        if (checkInput(input)){ // 들어온 input 자체가 적법한 infix expression인 경우
            input = input.replaceAll("\\s",""); // input의 공백을 모두 제거
            input = parseInput(input);    // input을 postfix expression으로 변환
            long answer = calculate(input); // postfix expression에 대한 연산 수행

            if (ERROR){  // 계산 도중 수학적 오류가 있는 경우
                System.out.println("ERROR");
            } else { // 모든 것이 정상일 경우
               System.out.println(input);
               System.out.println(answer);
            }
        }
        else { // 들어온 input 자체가 적법한 infix expression이 아닌 경우
            System.out.println("ERROR");
        }
    } // end command

    public static boolean checkInput(String input) { // 입력된 Input이 적법한 infix expression인지 판단하는 메소드
        int paran = 0;  // 괄호의 짝이 맞는지 판단하기 위한 변수
        int operand = 0; // 피연산자의 개수에 관한 변수
        int operator = 0; // 연산자의 개수에 관한 변수 (이 때 unary - 는 operator로 세지 않기로한다)
        boolean lastopr = false; // 앞의 문자가 연산자인지에 관한 변수 (unary - 또는 숫자가 들어가도 되는 순서인지 판단)
        int temp = 0; // 괄호 안의 에러를 위한 변수

        char[] inputList = input.toCharArray();

        for (int i =0; i < inputList.length ; i++){
            if (inputList[i] == '(' ){
                if (operand == 0 || lastopr){ // 좌괄호가 와도 되는 상황 .. 맨 앞에 오는 경우나 바로 앞에 연산자가 있는 경우
                    paran++;
                    lastopr = true;
                    temp = operand; // 괄호가 시작될 때의 피연산자의 개수를 저장한다
                }
                else{ // 좌괄호가 오기에 적합하지 않은 상황
                    return false;
                }
            } else if (inputList[i] == ')'){
                paran--;
                lastopr = false;

                if (temp == operand){ // ( ) 와 같이 괄호 안에 어떠한 infix expression이 없는 경우, ERROR
                    return false;
                }


            } else if (inputList[i] == '+' || inputList[i] == '*' || inputList[i] == '/' || inputList[i] == '%' || inputList[i] == '^'){
                operator++;
                lastopr = true;
            } else if (inputList[i] == '-'){
                if (!(operand == 0 || lastopr)){ // 맨 앞에 오는 - 이거나 바로 앞에 연산자가 있는 경우에는 unary expression이다
                    operator++; // binary - 일 때만 연산자로 간주한다
                }
                lastopr = true;

            } else if (Character.isDigit(inputList[i])){

                if (operand == 0 || lastopr) { // 숫자가 와도 되는 상황 .. 맨 앞에 오는 경우나 바로 앞에 연산자가 있는 경우
                    while (i < inputList.length - 1 && Character.isDigit(inputList[i + 1])) { // 연산자 또는 공백으로 숫자가 분리될 때까지
                        i++;
                    }
                    operand++;
                    lastopr = false;
                } else{ // 숫자가 나오면 안되는 상황 .. 앞에 ( 또는 연산자가 아닌 경우
                    return false;
                }
            } else{ // Input이 정상적이라면 발생할 수가 없다
                continue;
            }
        } // end for

        if (paran != 0 || operand - operator != 1){ // 괄호의 짝이 맞지 않거나 적법한 infix expression처럼 피연산자와 연산자의 개수의 관계가 올바르지 않은 경우
            return false;
        } else{
            return true;
        }
    }



    public static String parseInput(String input) { // Infix expression인 Input을 Postfix expression으로 변환하는 메소드
        Stack<Character> operatorStack = new Stack<>(); // 연산자를 담기 위한 Stack
        String postfix = ""; // 반환할 결과를 담는 String

        for (int i = 0; i < input.length(); i++) {
            Character symbol = input.charAt(i);

            if (Character.isDigit(symbol)) { // 숫자인 경우
                postfix += symbol; // 결과값 String 에 추가한다
                if (!(i != input.length()-1 && order(input.charAt(i+1)) == -1)) { // 다음 기호가 숫자가 아닌 경우, 공백을 추가한다
                    postfix += ' ';
                }


            } else if (symbol.equals('(')) {
                operatorStack.push(symbol);

            } else if (symbol.equals(')')) {
                while (!operatorStack.peek().equals('(')) { // 우괄호가 나오면 좌괄호가 나올때까지 Stack에 있는 연산자를 결과값 String에 추가한다
                    postfix += operatorStack.pop();
                    postfix += ' ';
                }
                operatorStack.pop();

            } else if (order(symbol) > 0){  // 연산자인 경우
                if (symbol.equals('-') && ( i==0 || order(input.charAt(i-1)) > 0) || input.charAt(i-1) == '('){ // unary - 인 경우 symbol을 ~로 변환한다
                    symbol = '~';
                }
                while (!operatorStack.empty() && order(symbol) != 3 && order(symbol) != 4 && !(order(operatorStack.peek()) < order(symbol))) { // symbol보다 우선순위가 낮은 연산자가 Stack에 있는 경우 결과값 String에 추가
                    postfix += operatorStack.pop();
                    postfix += ' ';
                }
                operatorStack.push(symbol);
            }
        }//end for
        while (!operatorStack.empty()) { // Stack에 남아있는 연산자를 모두 결과괎 String에 추가한다
            postfix += operatorStack.pop();
            postfix += ' ';
        }
        return postfix.substring(0, postfix.length()-1); // 마지막 공백 제거를 위해 다음과 같이 반환한다
	}


	public static int order(char symbol){ // 연산자의 우선순위를 나타내기 위한 메소드
	    String opr = Character.toString(symbol);
	    if (opr.equals("+") || opr.equals("-")){
	        return 1;
        } else if (opr.equals("*") || opr.equals("/") || opr.equals("%")){
            return 2;
        } else if (opr.equals("~")){
            return 3;
        } else if (opr.equals("^")){
            return 4;
        } else if (opr.equals("(") || opr.equals(")")) {
            return 0;
	    } else {
	        return -1;
        }

    } // end order

    public static long calculate(String postfix){ // postfix expression의 연산을 수행하기 위한 메소드. 강의노트를 참고함
	    long value1, value2;
	    String[] expr = postfix.split("\\s");
        Stack<Long> calThread = new Stack<>();

        for (int i = 0 ; i < expr.length; i++){
            if (expr[i].equals("+")){
                calThread.push(calThread.pop() + calThread.pop());
            } else if (expr[i].equals("-")){
                value2 = calThread.pop();
                value1 = calThread.pop();
                calThread.push(value1 - value2);
            } else if (expr[i].equals("*")){
                calThread.push(calThread.pop() * calThread.pop());
            } else if (expr[i].equals("/")){
                value2 = calThread.pop();
                value1 = calThread.pop();

                if (value2 == 0){ // ERROR 발생
                    ERROR = true;
                    return -1;
                }

                calThread.push(value1 / value2);
            } else if (expr[i].equals("%")){
                value2 = calThread.pop();
                value1 = calThread.pop();

                if (value2 == 0){ // ERROR 발생
                    ERROR = true;
                    return -1;
                }

                calThread.push( value1 % value2);
            } else if (expr[i].equals("~")){
                calThread.push(calThread.pop() * -1);
            } else if (expr[i].equals("^")){
                value2 = calThread.pop();
                value1 = calThread.pop();

                if (value1 == 0 && value2 <0){ // ERROR 발생
                    ERROR = true;
                    return -1;
                }

                calThread.push((long) Math.pow(value1, value2));
            } else{ // 숫자인 경우 Stack에 push
                calThread.push(Long.parseLong(expr[i]));
            }
        } // end for
        return calThread.pop();
    }
}
