package stu.file;

import java.util.Stack;

public class LongestValidParenteses {

	public static int longestValidParenteses(String s){
		Stack<Character> stack = new Stack<Character>();
		int sum = 0;
		char l = '(';
		char r = ')';
		stack.push(s.charAt(0));
		for(int i=1; i<s.length(); i++){
			char tmp = s.charAt(i);
			if(tmp == r){
				if(!stack.isEmpty() && l == stack.peek()){
					stack.pop();
					sum += 2;
				}
			}else{
				if(stack.size() < s.length() >> 1){
					stack.push(tmp);
				}
			}
		}
		return sum;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(longestValidParenteses("()()(()())"));
		System.out.println(longestValidParenteses("(()()())"));
		System.out.println(longestValidParenteses("()(()()()"));
		System.out.println(longestValidParenteses("()(()()())))())"));
		System.out.println(longestValidParenteses("(((((()))))))))))))))))))))))))))))))))))))"));
	}

}
