package com.xcorpio.calc;

/**
 *������Ϣ��һЩ���� 
 * @author Dong
 */
public interface Msg {
	
	public static final String EOE="\0";			//���ʽ������־
	
	//4�б������
	public static final int NONE_TOKEN=0;	//���Ϊ�ջ��߽�����
	public static final int DELIMITER_TOKEN=1;	//���Ϊ�ָ���
	public static final int VARIABLE_TOKEN=2;	//���Ϊ����,��������
	public static final int NUMBER_TOKEN=3;		//���λ����
	
	
	//��������
	public static final int SYNTAX_ERROR=0;			//�﷨����
	public static final int UNBALPARENS_ERROR=1;	//����û�н�������
	public static final int NOEXP_ERROR=2;			//���ʽΪ�մ���
	public static final int DIVBYZERO_ERROR=3;		//0�ǳ�������
	public static final int FUCTION_PARAMS_ERROR=4;	//������������
	public static final int INVALID_EXPRESSION=5;	//��Ч�Ľ���ʽ
	public static final int UNKNOWN_FUNCTION=6;		//δ֪�ĺ���
	
	//������ʾ
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
