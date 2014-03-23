package com.xcorpio.calc;

import java.util.ArrayList;


/**数学函数*/

public class MyMath implements Msg{
	
	//三角函数
	public static double sin(String params){
		//System.out.println(params);
		return Math.sin(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double cos(String params){
		return Math.cos(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double tan(String params){
		return Math.tan(Double.parseDouble(ParseExpression.parse(params)));
	}
	
	//反三角函数
	public static double arcsin(String params){
		return Math.asin(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double arccos(String params){
		return Math.acos(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double arctan(String params){
		return Math.atan(Double.parseDouble(ParseExpression.parse(params)));
	}
	
	//双曲函数
	public static double sinh(String params){
		return Math.sinh(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double cosh(String params){
		return Math.cosh(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double tanh(String params){
		return Math.tanh(Double.parseDouble(ParseExpression.parse(params)));
	}
	
	//对数
	/** log(x,nBase) 
	 * @throws Exception */
	public static double log(String params) throws Exception{
		String[] param=splitToTwoParams(params);
		//System.out.println(param[0]+"  "+param[1]);
		return Math.log(Double.parseDouble(ParseExpression.parse(param[0])))/Math.log(Double.parseDouble(ParseExpression.parse(param[1])));
	}
	public static double log10(String params){
		return Math.log10(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double ln(String params){
		return Math.log(Double.parseDouble(ParseExpression.parse(params)));
	}
	
	//指数和阶乘
	/** x的 nPower 次幂 ：pow(x,nPower) 
	 * @throws Exception */
	public static double pow(String params) throws Exception{
		String[] param=splitToTwoParams(params);
		return Math.pow (Double.parseDouble(ParseExpression.parse(param[0])),Double.parseDouble(ParseExpression.parse(param[1])));
	}
	/** e的x 次幂 */
	public static double exp(String params){
		return Math.exp(Double.parseDouble(ParseExpression.parse(params)));
	}
	/** 阶乘 
	 * @throws Exception */
	public static double fact(String params) throws Exception{
		Double fact=Double.parseDouble(ParseExpression.parse(params));
		System.out.println("fact:"+fact);
		double result=1;
		if(fact<1){
			throw new Exception(ERROR_MESSAGE[FUCTION_PARAMS_ERROR]);
		}
		while(fact>=1){
			result*=fact--;
		}
		return result;
	}
	
	//模运算和开方运算
	/** 模运算 
	 * @throws Exception */
	public static double mod(String params) throws Exception{
		String[] param=splitToTwoParams(params);
		return Double.parseDouble(ParseExpression.parse(param[0]))%Double.parseDouble(ParseExpression.parse(param[1]));
	}
	/** 平方根 */
	public static double sqrt(String params){
		return Math.sqrt(Double.parseDouble(ParseExpression.parse(params)));
	}
	/** 开三次方根 */
	public static double cuberoot(String params){
		return Math.cbrt(Double.parseDouble(ParseExpression.parse(params)));
	}
	/** 求值x的y次方根  :yroot(x,y)
	 * @throws Exception */
	public static double yroot(String params) throws Exception{
		String[] param=splitToTwoParams(params);
		return Math.pow(Double.parseDouble(ParseExpression.parse(param[0])),1/Double.parseDouble(ParseExpression.parse(param[1])));
	}
	
	//集合统计函数
	/** 集合算数平均值 */
	public static double avg(String params){
		Double[] args=parseToArray(params);
		return avg(args);
	}
	/** 集合统计 */
	public static double sum(String params){
		Double[] args=parseToArray(params);
		return sum(args);
	}
	/** 集合估算方差 */
	public static double var(String params){
		Double[] args=parseToArray(params);
		double sum=0.0;
		double avg=avg(args);
		for(int i=0;i<args.length;++i){
			sum+=(args[i]-avg)*(args[i]-avg);
		}
		return sum/(args.length-1);
	}
	/** 集合总体方差 */
	public static double varp(String params){
		Double[] args=parseToArray(params);
		double sum=0.0;
		double avg=avg(args);
		for(int i=0;i<args.length;++i){
			sum+=(args[i]-avg)*(args[i]-avg);
		}
		return sum/args.length;
	}
	/** 集合估算标准方差 */
	public static double stdev(String params){
		return Math.sqrt(var(params));
	}
	/** 集合总体标准方差 */
	public static double stdevp(String params){
		return Math.sqrt(varp(params));
	}
	
	/** 求平均值  */
	private static double avg(Double[] args){
		double sum=0.0;
		for(int i=0;i<args.length;++i){
			sum+=args[i];
		}
		return sum/args.length;
	}
	
	/** 求和  */
	private static double sum(Double[] args){
		double sum=0.0;
		for(int i=0;i<args.length;++i){
			sum+=args[i];
		}
		return sum;
	}
	/** 把参数解析成 数组 */
	private static Double[] parseToArray(String param){
		ArrayList<Double> results = new ArrayList<>();
		int commaIndex=0;	//上一次逗号的索引,考虑有【
		int oddBracket=0;	//左括号加1，右括号减一
		//System.out.println(param+":);
		for(int i=0;i<param.length();++i){
			if(param.charAt(i)=='('){
				oddBracket++;
			}else if(param.charAt(i)==')'){
				oddBracket--;
			}else if(param.charAt(i)==','){
				if(oddBracket==0){//是参数分割符
					//System.out.println(param.substring(commaIndex+1, i));
					results.add(Double.parseDouble(ParseExpression.parse(param.substring(commaIndex+1, i))));
					commaIndex=i;
				}
			}
		}
		//分割逗号的最后一个变量
		//System.out.println(param.substring(commaIndex+1, param.length()-1));
		results.add(Double.parseDouble(ParseExpression.parse(param.substring(commaIndex+1, param.length()-1))));
		return results.toArray(new Double[]{});
	}
	/** 为需要两个参数的函数把参数字符串，分成两个参数 
	 * @throws Exception */
	private static String[] splitToTwoParams(String param) throws Exception{
		String[] splits=param.split(",");
		if(splits.length==2){//只有1个逗号。参数字符串中没有复合结构
			return splits;
		}else if(splits.length>2){//有多个逗号  log(x,log(2,3)) , log(4,6)
			int commaIndex=0;	//分割逗号的索引
			int oddBracket=0;	//左括号加1，右括号减一
			int spiltCommaCount=0;	//参数分隔符个数.检测参数个数
			for(int i=0;i<param.length();++i){
				if(param.charAt(i)=='('){
					oddBracket++;
				}else if(param.charAt(i)==')'){
					oddBracket--;
				}else if(param.charAt(i)==','){
					if(oddBracket==0){//是参数分割符
						commaIndex=i;
						spiltCommaCount++;
					}
				}
			}
			if(spiltCommaCount==1){//只有两个参数
				splits=new String[2];
				splits[0]=param.substring(0, commaIndex);
				splits[1]=param.substring(commaIndex+1, param.length());
			}else{
				throw new Exception(ERROR_MESSAGE[FUCTION_PARAMS_ERROR]);
			}
		}else{
			throw new Exception(ERROR_MESSAGE[FUCTION_PARAMS_ERROR]);
		}
		return splits;
	}
	
	/**由字符串获得函数值 */   
    public static double getFunctionValue(String token) throws Exception {  
    	double result=0.0;
    	String origin=token;//备一份
    	token=token.replaceAll("\\p{Space}", "");	//主要去掉函数名和左括号之间的空白
    	//三角函数
        if(token.startsWith("sin(")){//正弦
        	result=sin(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("cos(")){//余弦
        	result=cos(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("tan(")){//正切
        	result=tan(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //反三角函数
        else if(token.startsWith("arcsin(")){
        	result=arcsin(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("arccos(")){
        	result=arccos(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("arctan(")){
        	result=arctan(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //双曲函数
        else if(token.startsWith("sinh(")){
        	result=sinh(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("cosh(")){
        	result=cosh(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("tanh(")){
        	result=tanh(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //对数
        else if(token.startsWith("log(")){
        	result=log(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("log10(")){
        	result=log10(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("ln(")){
        	result=ln(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //指数和阶乘
        else if(token.startsWith("pow(")){
        	result=pow(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("exp(")){
        	result=exp(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("fact(")){//阶乘
        	result=fact(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //模运算和开方运算
        else if(token.startsWith("mod(")){
        	result=mod(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("sqrt(")){
        	result=sqrt(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("cuberoot(")){
        	result=cuberoot(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("yroot(")){//x的y次方根
        	result=yroot(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //统计函数
        else if(token.startsWith("avg(")){
        	result=avg(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("sum(")){
        	result=sum(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("var(")){//估算方差
        	result=var(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("varp(")){//总体方差
        	result=varp(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("stdev(")){//估算标准方差
        	result=stdev(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("stdevp(")){//总体标准方差
        	result=stdevp(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else{
        	throw new Exception(ERROR_MESSAGE[UNKNOWN_FUNCTION]+": "+origin);
        }
        
        //System.out.println(token+"="+result);
        return result;
    }
    
    /** 测试用 */
	public static void main(String[] args){
		//parseToArray("[1,2,34,5]");
		//parseToArray("[1 + 2,3,sin(45)]");
		//parseToArray("[1 + 2,3,pow(45,pow(2,3))]");
		Double[] ret=parseToArray("[1 + 2,3,sin(45)]");
		for(int i=0;i<ret.length;++i){
			System.out.println(ret[i]);
		}
	}
}
