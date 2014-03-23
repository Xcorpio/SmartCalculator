package com.xcorpio.calc;

/**
 *错误消息，一些常量 
 * @author Dong
 */
public interface Msg {
	
	public static final String EOE="\0";			//表达式结束标志
	
	//4中标记类型
	public static final int NONE_TOKEN=0;	//标记为空或者结束符
	public static final int DELIMITER_TOKEN=1;	//标记为分隔符
	public static final int VARIABLE_TOKEN=2;	//标记为变量,函数名称
	public static final int NUMBER_TOKEN=3;		//标记位数字
	
	
	//错误类型
	public static final int SYNTAX_ERROR=0;			//语法错误
	public static final int UNBALPARENS_ERROR=1;	//括号没有结束错误
	public static final int NOEXP_ERROR=2;			//表达式为空错误
	public static final int DIVBYZERO_ERROR=3;		//0是除数错误
	public static final int FUCTION_PARAMS_ERROR=4;	//函数参数不对
	public static final int INVALID_EXPRESSION=5;	//无效的解析式
	public static final int UNKNOWN_FUNCTION=6;		//未知的函数
	
	//错误提示
	public static final String[] ERROR_MESSAGE={
		"syntax error.",
		"unbalanced parentheses",
		"no expression present",
		"division by zero",
		"function parameters incorrect",
		"invalid expression",
		"unknown function"
	};
}
