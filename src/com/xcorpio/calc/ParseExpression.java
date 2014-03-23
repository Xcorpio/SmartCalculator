/*
 	确定运算的优先级，从高到低分别为：原子元素表达式，包括数字和变量；括号表达式；一元表达式，取数的负数；指数表达式；乘、除、取模表达式；加、减表达式；赋值表达式。
	对于每一级别的运算，都由一个方法实现，在方法中先完成比自己高一级别的运算，再处理本级别的运算。因此，在计算整个表达式的主方法中，只需调用最低级别运算的实现方法即可。
	确定表达式中的分隔符，有：+（加）、-（减）、*（乘）、/（除）、%（取模）、^（指数）、=（赋值）、(（左括号）、)（右括号）。
	利用这些分隔符将表达式分成多段，每一段称作一个token（标识符），分隔符也算token。
 */

package com.xcorpio.calc;

import java.util.HashSet;

/**
 * 提供解析表达式的方法
 */
public class ParseExpression implements Msg{
	
	private String exp;				//表达式字符串
	private int expIndex;			//解析器当前指针在表达式中位置
	private String  token;			//解析器当前处理的标记
	private int tokenType;			//解析器当前处理的标记类型
	
	
	/** 解析一个表达式，返回表达式的值。  */
	
	public double evaluate(String expStr) throws Exception{
		double result;
		this.exp=preProcess(expStr);;
		this.expIndex=0;
		
		//获取第一个标记
		this.getToken();
		if(this.token.equals(EOE)){
			//没有表达式异常
			this.handleError(NOEXP_ERROR);
		}
		
		result=this.parseAddOrSub();		//处理加减语句
		return result;
	}
	
	
	/**计算加减法表达式*/
	private double parseAddOrSub() throws Exception{
		char op;	//操作符
		double result;	//结果
		double partialResult;	//子表达式的结果
		
		result=this.parseMulOrDiv();	//用乘法计算当前子表达式的值
		//如果当前标记的第一个字母是加减号，则继续进行加减法运算
		while((op=this.token.charAt(0))=='+'||op=='-'){
			this.getToken();	//取下一个标记
			//用乘法计算当前字表达式的值
			partialResult=this.parseMulOrDiv();
			switch(op){
			case '-':
				//如果是减法，则用已处理的子表达式的值减去当前子表达式的值
				result=result-partialResult;
				break;
			case '+':
				//如果是加法，则用已处理的子表达式的值加上当前子表达式的值
				result=result+partialResult;
				break;
			}
		}
		return result;
	}
	
	/**计算乘除法表达式，包括取模运算*/
	private double parseMulOrDiv() throws Exception{
		char op;	//操作符
		double result;	//结果
		double partialResult;	//子表达式的结果
		
		//用指数运算计算当前子表达式的值
		result=this.parseExponent();
		//如果当前标记的第一个字母是乘除或者取模运算符，则继续进行乘除运算
		while((op=this.token.charAt(0))=='*'||op=='/'||op=='%'){
			this.getToken();//取下一个标记
			//用指数运算计算当前子表达式的值
			partialResult=this.parseExponent();
			switch(op){
			case '*':
				//如果是乘法，则用已处理表达式的值乘以当前子表达式的值
				result=result*partialResult;
				break;
			case '/':
				//如果是除法，则判断当前子表达式的值是否为0，如果是0，则抛出被0除异常
				if(partialResult==0.0){
					this.handleError(DIVBYZERO_ERROR);
				}
				//除数不为0，则进行除法运算
				result=result/partialResult;
				break;
			case '%':
				//如果是取模运算，也要判断当前子表达式的值是否为0
				//如果为0，则抛出异常
				if(partialResult==0.0){
					this.handleError(DIVBYZERO_ERROR);
				}
				//进行取模运算
				result=result%partialResult;
				break;
			}
		}
		return result;
	}
	
	/**计算指数表达式*/
	private double parseExponent() throws Exception{
		double result;		//结果
		double partialResult;	//子表达式的值
		double ex;		//底数
		
		//用一元运算计算当前子表达式值
		result=this.parseUnaryOperator();
		//如果当前标记为^运算符，则为指数计算
		if(this.token.equals("^")){
			//获取下一个标记，即获取指数的幂
			this.getToken();
			partialResult=this.parseExponent();
			ex=result;
			if(partialResult==0.0){
				//如果指数幂为0，则指数的值为1
				result=1.0;
			}else{
				result=Math.pow(ex, partialResult);
			}
		}
		return result;
	}
	
	/** 执行一元运算，+，-表示正数和负数*/
	private double parseUnaryOperator() throws Exception{
		double result;	//结果
		String op;		//操作符
		op="";
		//如果当前标记类型为分隔符，而且分隔符的值为+或-
		if((this.tokenType==DELIMITER_TOKEN)&&this.token.equals("+")||this.token.equals("-")){
			op=this.token;
			this.getToken();
		}
		//用括号运算计算当前子表达式的值
		result=this.parseBracket();
		if(op.equals("-")){
			//如果操作符为-，则表示负数，将子表达式的值变为负数
			result=-result;
		}
		return result;
	}
	
	/**执行括号运算*/
	private double parseBracket() throws Exception{
		double result; // 结果  
	    // 如果当前标记为左括号，则表示是一个括号运算  
	    if (this.token.equals("(")) {  
	    	this.getToken(); // 取下一个标记  
	        result = this.parseAddOrSub();// 用加减法运算计算子表达式的值  
	        // 如果当前标记不是右括号，则抛出括号不匹配异常  
	        if (!this.token.equals(")")){  
	            this.handleError(UNBALPARENS_ERROR);  
	        }  
	        this.getToken(); // 否则取下一个标记  
	    } else {  
	        // 如果标记不是左括号，表示不是一个括号运算，则用原子元素运算计算子表达式的值  
	        result = this.parseAtomElement();  
	    }  
	    return result;
	}
	
	/**执行原子元素运算，包括变量和数字 */    
    private double parseAtomElement() throws Exception {  
        double result = 0.0; // 结果  
          
        switch (this.tokenType) {  
        case NUMBER_TOKEN:  
            // 如果当前标记类型为数字  
            try {  
                // 将数字的字符串转换成数字值  
                result = Double.parseDouble(this.token);  
            } catch (NumberFormatException exc) {  
                this.handleError(SYNTAX_ERROR);  
            }  
            this.getToken(); // 取下一个标记  
            break;  
        case VARIABLE_TOKEN:  
            // 如果当前标记类型是变量，则取变量的值  
            result = MyMath.getFunctionValue(token);  
            this.getToken();  
            break;  
        default:  
            this.handleError(SYNTAX_ERROR);  
            break;  
        }  
        return result;  
    }  
    
    
	/** 处理异常情况 */
	private void handleError(int errorType) throws Exception{
		//遇到异常情况时，根据错误类型，取得异常提示信息，将提示信息封装在异常中抛出
		throw new Exception(ERROR_MESSAGE[errorType]);
	}
	
	/**回滚，将解析器当前指针往前移到当前标记位置 */  
    private void putBack() {  
        if (this.token == EOE){  
            return;  
        }  
        // 解析器当前指针往前移动  
        for (int i = 0; i < this.token.length(); i++) {  
            this.expIndex--; 
        }
    }
    
    
	
	/**获取下一个标记 */
	private void getToken(){
		//设置初始值
		this.tokenType=NONE_TOKEN;
		this.token="";
		
		//检测表达式是否结束，如果解析器的指针下标与字符串长度相等
		//则表明表达式已经结束，置当前标志为EOE
		if(this.expIndex==this.exp.length()){
			this.token=EOE;
			return;
		}
		//跳过表达式的空白符
		while(this.expIndex<this.exp.length()
				&&Character.isWhitespace(this.exp.charAt(this.expIndex))){
			this.expIndex++;
		}
		//再次检查表达式是否结束
		if(this.expIndex==this.exp.length()){
			this.token=EOE;
			return;
		}
		char currentChar=this.exp.charAt(expIndex);
		//取得解析器当前指针指向的字符
		//如果当前字符是一个分隔符，则认为这是一个分隔符标记
		//给当前标记和标记类型赋值，并将指针后移
		if(isDelim(currentChar)){
			this.token+=currentChar;
			this.expIndex++;
			this.tokenType=DELIMITER_TOKEN;
		}else if(Character.isLetter(currentChar)){
			//如果当前字符是一个字母，则认为是一个变量标记
			//将解析式指针往后移，直到遇到一个分隔符，之间的字符都是变量的组成部分
			//while(!isDelim(currentChar)){
			while(!isFunctionOver()){
				this.token+=currentChar;
				this.expIndex++;
				if(this.expIndex>=this.exp.length()){
					break;
				}else{
					currentChar=this.exp.charAt(this.expIndex);
				}
			}
			//System.out.println("finction:"+token);
			this.tokenType=VARIABLE_TOKEN;		//设置标记类型为变量/函数
		}else if(Character.isDigit(currentChar)){
			//如果当前字符是一个数字，则认为当前标记的类型为数字
			//将解析器指针往后移，直到遇到一个分隔符，之间的字符都是该字符的组成部分
			while(!isDelim(currentChar)){
				this.token+=currentChar;
				this.expIndex++;
				if(this.expIndex>=this.exp.length()){
					break;
				}else{
					currentChar=this.exp.charAt(this.expIndex);
				}
			}
			this.tokenType=NUMBER_TOKEN;
		}else{
			//无法识别的字符，则认为表达式结束
			this.token=EOE;
			return;
		}
	}
	
	/** 
	 * 判断一个字符是否为分隔符
	 * 表达式中的字符包括：
	 * +(加),-(减),*,/,%(取模),^(指数),((左括号),)(右括号)
	 */
	private boolean isDelim(char c){
		if(("+-*/%^()".indexOf(c))!=-1){
			return true;
		}
		return false;
	}
	
	/**根据括号匹配判断函数是否结束*/
	private boolean isFunctionOver(){
		if(this.token.length()>0){
			String tmp=this.token;
			int leftBracketCount=0;
			int rightBracketCount=0;
			char T[]=tmp.toCharArray();
			for(int i=0;i<T.length;++i){
				if(T[i]=='('){
					leftBracketCount++;
				}else if(T[i]==')'){
					rightBracketCount++;
				}
			}
			if(leftBracketCount>0||rightBracketCount>0){
				if(leftBracketCount==rightBracketCount){
					return true;
				}
			}
		}
		return false;
	}
	
	/** 预先处理表达式中的数字常量，把类似  .3 的换成 0.3  ,把mod 换成 %
	 * @throws Exception */
	private String preProcess(String expStr) throws Exception{
		StringBuilder exp=new StringBuilder(expStr);
		int index=0;
		int fromIndex=0;
		//处理%
		while((index=exp.indexOf(".",fromIndex))!=-1){
			if(!Character.isDigit(exp.charAt((index-1)<0?0:index-1))){
				exp.insert(index, '0');
			}
			fromIndex=index+1;
		}
		
		//处理 " mod " 
		index=0;
		fromIndex=0;
		while((index=exp.indexOf("mod",fromIndex))!=-1){
			if(!Character.isLetter(exp.charAt((index-1)<0?0:index-1))&&!Character.isLetter(exp.charAt(index+3))){
				int i=index+3;
				for(;i<expStr.length()&&Character.isWhitespace(expStr.charAt(i));++i)
				{
					//是空白字符，就循环，此处区分   2 mod 3 和 函数 模电（2,3）
				}
				if(!(expStr.charAt(i)=='(')){
					exp.replace(index, index+3, "%");
				}
			}
			fromIndex=index+3;
		}
		
		//检查括号是否匹配
		int oddBracket=0;	//左括号加1，右括号减一
		for(int i=0;i<expStr.length();++i){
			if(expStr.charAt(i)=='('){
				oddBracket++;
			}else if(expStr.charAt(i)==')'){
				oddBracket--;
			}
		}
		if(oddBracket!=0){
			throw new Exception(ERROR_MESSAGE[SYNTAX_ERROR]);
		}
		return exp.toString();
	}
	//测试
	public static void main(String[] args) throws Exception {  
        ParseExpression test = new ParseExpression();   
        String exp = "pow(2,3)+5 mod 3+ pow(1 ,3 )";  
        try{
        	System.out.println(exp+"=" + test.evaluate(exp));  
        }catch(Exception e){
        	System.out.println(e.getMessage());
        }
        
    }  
	
	/** 解析接口 */
	public static String parse(String expression){
		expression=expression.toLowerCase();
		try {
			return ""+(new ParseExpression()).evaluate(expression);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "error: "+e.getMessage();
		}
	}
	
	/** 支持的命令 */
	
	public static String[] commands={
		"exit",
		"clear"
	};
	public static HashSet<String> comSet=new HashSet<>();	//命令容器
	
	/** 判断是否是条命令  */
	
	public static String isCommand(String exp){
		if(comSet.size()==0){//把命令加入
			for(int i=0;i<commands.length;++i){
				comSet.add(commands[i]);
			}
		}
		exp=exp.trim();		//去两边的空白
		if(comSet.contains(exp)){
			return exp;
		}
		return null;
	}
}
