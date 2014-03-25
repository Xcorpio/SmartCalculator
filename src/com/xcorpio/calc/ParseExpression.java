/*
 	ȷ����������ȼ����Ӹߵ��ͷֱ�Ϊ��ԭ��Ԫ�ر��ʽ���������ֺͱ��������ű��ʽ��һԪ���ʽ��ȡ���ĸ�����ָ�����ʽ���ˡ�����ȡģ���ʽ���ӡ������ʽ����ֵ���ʽ��
	����ÿһ��������㣬����һ������ʵ�֣��ڷ���������ɱ��Լ���һ��������㣬�ٴ�����������㡣��ˣ��ڼ����������ʽ���������У�ֻ�������ͼ��������ʵ�ַ������ɡ�
	ȷ�����ʽ�еķָ������У�+���ӣ���-��������*���ˣ���/��������%��ȡģ����^��ָ������=����ֵ����(�������ţ���)�������ţ���
	������Щ�ָ��������ʽ�ֳɶ�Σ�ÿһ�γ���һ��token����ʶ�������ָ���Ҳ��token��
 */

package com.xcorpio.calc;

import java.util.HashSet;

/**
 * �ṩ�������ʽ�ķ���
 */
public class ParseExpression implements Msg{
	
	private String exp;				//���ʽ�ַ���
	private int expIndex;			//��������ǰָ���ڱ��ʽ��λ��
	private String  token;			//��������ǰ����ı��
	private int tokenType;			//��������ǰ����ı������
	
	
	/** ����һ�����ʽ�����ر��ʽ��ֵ��  */
	
	public double evaluate(String expStr) throws Exception{
		double result;
		this.exp=preProcess(expStr);;
		this.expIndex=0;
		
		//��ȡ��һ�����
		this.getToken();
		if(this.token.equals(EOE)){
			//û�б��ʽ�쳣
			this.handleError(NOEXP_ERROR);
		}
		
		result=this.parseAddOrSub();		//����Ӽ����
		return result;
	}
	
	
	/**����Ӽ������ʽ*/
	private double parseAddOrSub() throws Exception{
		char op;	//������
		double result;	//���
		double partialResult;	//�ӱ��ʽ�Ľ��
		
		result=this.parseMulOrDiv();	//�ó˷����㵱ǰ�ӱ��ʽ��ֵ
		//�����ǰ��ǵĵ�һ����ĸ�ǼӼ��ţ���������мӼ�������
		while((op=this.token.charAt(0))=='+'||op=='-'){
			this.getToken();	//ȡ��һ�����
			//�ó˷����㵱ǰ�ֱ��ʽ��ֵ
			partialResult=this.parseMulOrDiv();
			switch(op){
			case '-':
				//����Ǽ����������Ѵ�����ӱ��ʽ��ֵ��ȥ��ǰ�ӱ��ʽ��ֵ
				result=result-partialResult;
				break;
			case '+':
				//����Ǽӷ��������Ѵ�����ӱ��ʽ��ֵ���ϵ�ǰ�ӱ��ʽ��ֵ
				result=result+partialResult;
				break;
			}
		}
		return result;
	}
	
	/**����˳������ʽ������ȡģ����*/
	private double parseMulOrDiv() throws Exception{
		char op;	//������
		double result;	//���
		double partialResult;	//�ӱ��ʽ�Ľ��
		
		//��ָ��������㵱ǰ�ӱ��ʽ��ֵ
		result=this.parseExponent();
		//�����ǰ��ǵĵ�һ����ĸ�ǳ˳�����ȡģ���������������г˳�����
		while((op=this.token.charAt(0))=='*'||op=='/'||op=='%'){
			this.getToken();//ȡ��һ�����
			//��ָ��������㵱ǰ�ӱ��ʽ��ֵ
			partialResult=this.parseExponent();
			switch(op){
			case '*':
				//����ǳ˷��������Ѵ�����ʽ��ֵ���Ե�ǰ�ӱ��ʽ��ֵ
				result=result*partialResult;
				break;
			case '/':
				//����ǳ��������жϵ�ǰ�ӱ��ʽ��ֵ�Ƿ�Ϊ0�������0�����׳���0���쳣
				if(partialResult==0.0){
					this.handleError(DIVBYZERO_ERROR);
				}
				//������Ϊ0������г�������
				result=result/partialResult;
				break;
			case '%':
				//�����ȡģ���㣬ҲҪ�жϵ�ǰ�ӱ��ʽ��ֵ�Ƿ�Ϊ0
				//���Ϊ0�����׳��쳣
				if(partialResult==0.0){
					this.handleError(DIVBYZERO_ERROR);
				}
				//����ȡģ����
				result=result%partialResult;
				break;
			}
		}
		return result;
	}
	
	/**����ָ�����ʽ*/
	private double parseExponent() throws Exception{
		double result;		//���
		double partialResult;	//�ӱ��ʽ��ֵ
		double ex;		//����
		
		//��һԪ������㵱ǰ�ӱ��ʽֵ
		result=this.parseUnaryOperator();
		//�����ǰ���Ϊ^���������Ϊָ������
		if(this.token.equals("^")){
			//��ȡ��һ����ǣ�����ȡָ������
			this.getToken();
			partialResult=this.parseExponent();
			ex=result;
			if(partialResult==0.0){
				//���ָ����Ϊ0����ָ����ֵΪ1
				result=1.0;
			}else{
				result=Math.pow(ex, partialResult);
			}
		}
		return result;
	}
	
	/** ִ��һԪ���㣬+��-��ʾ�����͸���*/
	private double parseUnaryOperator() throws Exception{
		double result;	//���
		String op;		//������
		op="";
		//�����ǰ�������Ϊ�ָ��������ҷָ�����ֵΪ+��-
		if((this.tokenType==DELIMITER_TOKEN)&&this.token.equals("+")||this.token.equals("-")){
			op=this.token;
			this.getToken();
		}
		//������������㵱ǰ�ӱ��ʽ��ֵ
		result=this.parseBracket();
		if(op.equals("-")){
			//���������Ϊ-�����ʾ���������ӱ��ʽ��ֵ��Ϊ����
			result=-result;
		}
		return result;
	}
	
	/**ִ����������*/
	private double parseBracket() throws Exception{
		double result; // ���  
	    // �����ǰ���Ϊ�����ţ����ʾ��һ����������  
	    if (this.token.equals("(")) {  
	    	this.getToken(); // ȡ��һ�����  
	        result = this.parseAddOrSub();// �üӼ�����������ӱ��ʽ��ֵ  
	        // �����ǰ��ǲ��������ţ����׳����Ų�ƥ���쳣  
	        if (!this.token.equals(")")){  
	            this.handleError(UNBALPARENS_ERROR);  
	        }  
	        this.getToken(); // ����ȡ��һ�����  
	    } else {  
	        // �����ǲ��������ţ���ʾ����һ���������㣬����ԭ��Ԫ����������ӱ��ʽ��ֵ  
	        result = this.parseAtomElement();  
	    }  
	    return result;
	}
	
	/**ִ��ԭ��Ԫ�����㣬�������������� */    
    private double parseAtomElement() throws Exception {  
        double result = 0.0; // ���  
          
        switch (this.tokenType) {  
        case NUMBER_TOKEN:  
            // �����ǰ�������Ϊ����  
            try {  
                // �����ֵ��ַ���ת��������ֵ  
                result = Double.parseDouble(this.token);  
            } catch (NumberFormatException exc) {  
                this.handleError(SYNTAX_ERROR);  
            }  
            this.getToken(); // ȡ��һ�����  
            break;  
        case VARIABLE_TOKEN:  
            // �����ǰ��������Ǳ�������ȡ������ֵ  
            result = MyMath.getFunctionValue(token);  
            this.getToken();  
            break;  
        default:  
            this.handleError(SYNTAX_ERROR);  
            break;  
        }  
        return result;  
    }  
    
    
	/** �����쳣��� */
	private void handleError(int errorType) throws Exception{
		//�����쳣���ʱ�����ݴ������ͣ�ȡ���쳣��ʾ��Ϣ������ʾ��Ϣ��װ���쳣���׳�
		throw new Exception(ERROR_MESSAGE[errorType]);
	}
	
	/**�ع�������������ǰָ����ǰ�Ƶ���ǰ���λ�� */  
    private void putBack() {  
        if (this.token == EOE){  
            return;  
        }  
        // ��������ǰָ����ǰ�ƶ�  
        for (int i = 0; i < this.token.length(); i++) {  
            this.expIndex--; 
        }
    }
    
    
	
	/**��ȡ��һ����� */
	private void getToken(){
		//���ó�ʼֵ
		this.tokenType=NONE_TOKEN;
		this.token="";
		
		//�����ʽ�Ƿ�����������������ָ���±����ַ����������
		//��������ʽ�Ѿ��������õ�ǰ��־ΪEOE
		if(this.expIndex==this.exp.length()){
			this.token=EOE;
			return;
		}
		//�������ʽ�Ŀհ׷�
		while(this.expIndex<this.exp.length()
				&&Character.isWhitespace(this.exp.charAt(this.expIndex))){
			this.expIndex++;
		}
		//�ٴμ����ʽ�Ƿ����
		if(this.expIndex==this.exp.length()){
			this.token=EOE;
			return;
		}
		char currentChar=this.exp.charAt(expIndex);
		//ȡ�ý�������ǰָ��ָ����ַ�
		//�����ǰ�ַ���һ���ָ���������Ϊ����һ���ָ������
		//����ǰ��Ǻͱ�����͸�ֵ������ָ�����
		if(isDelim(currentChar)){
			this.token+=currentChar;
			this.expIndex++;
			this.tokenType=DELIMITER_TOKEN;
		}else if(Character.isLetter(currentChar)){
			//�����ǰ�ַ���һ����ĸ������Ϊ��һ���������
			//������ʽָ�������ƣ�ֱ������һ���ָ�����֮����ַ����Ǳ�������ɲ���
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
			this.tokenType=VARIABLE_TOKEN;		//���ñ������Ϊ����/����
		}else if(Character.isDigit(currentChar)){
			//�����ǰ�ַ���һ�����֣�����Ϊ��ǰ��ǵ�����Ϊ����
			//��������ָ�������ƣ�ֱ������һ���ָ�����֮����ַ����Ǹ��ַ�����ɲ���
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
			//�޷�ʶ����ַ�������Ϊ���ʽ����
			this.token=EOE;
			return;
		}
	}
	
	/** 
	 * �ж�һ���ַ��Ƿ�Ϊ�ָ���
	 * ���ʽ�е��ַ�������
	 * +(��),-(��),*,/,%(ȡģ),^(ָ��),((������),)(������)
	 */
	private boolean isDelim(char c){
		if(("+-*/%^()".indexOf(c))!=-1){
			return true;
		}
		return false;
	}
	
	/**��������ƥ���жϺ����Ƿ����*/
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
	
	/** Ԥ�ȴ�����ʽ�е����ֳ�����������  .3 �Ļ��� 0.3  ,��mod ���� %
	 * @throws Exception */
	private String preProcess(String expStr) throws Exception{
		StringBuilder exp=new StringBuilder(expStr);
		int index=0;
		int fromIndex=0;
		//����%
		while((index=exp.indexOf(".",fromIndex))!=-1){
			if(!Character.isDigit(exp.charAt((index-1)<0?0:index-1))){
				exp.insert(index, '0');
			}
			fromIndex=index+1;
		}
		
		//���� " mod " 
		index=0;
		fromIndex=0;
		while((index=exp.indexOf("mod",fromIndex))!=-1){
			if(!Character.isLetter(exp.charAt((index-1)<0?0:index-1))&&!Character.isLetter(exp.charAt(index+3))){
				int i=index+3;
				for(;i<expStr.length()&&Character.isWhitespace(expStr.charAt(i));++i)
				{
					//�ǿհ��ַ�����ѭ�����˴�����   2 mod 3 �� ���� ģ�磨2,3��
				}
				if(!(expStr.charAt(i)=='(')){
					exp.replace(index, index+3, "%");
				}
			}
			fromIndex=index+3;
		}
		
		//��������Ƿ�ƥ��
		int oddBracket=0;	//�����ż�1�������ż�һ
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
	//����
	public static void main(String[] args) throws Exception {  
        ParseExpression test = new ParseExpression();   
        String exp = "pow(2,3)+5 mod 3+ pow(1 ,3 )";  
        try{
        	System.out.println(exp+"=" + test.evaluate(exp));  
        }catch(Exception e){
        	System.out.println(e.getMessage());
        }
        
    }  
	
	/** �����ӿ� */
	public static String parse(String expression){
		expression=expression.toLowerCase();
		try {
			return ""+(new ParseExpression()).evaluate(expression);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "error: "+e.getMessage();
		}
	}
	
	/** ֧�ֵ����� */
	
	public static String[] commands={
		"exit",
		"clear"
	};
	public static HashSet<String> comSet=new HashSet<>();	//��������
	
	/** �ж��Ƿ���������  */
	
	public static String isCommand(String exp){
		if(comSet.size()==0){//���������
			for(int i=0;i<commands.length;++i){
				comSet.add(commands[i]);
			}
		}
		exp=exp.trim();		//ȥ���ߵĿհ�
		if(comSet.contains(exp)){
			return exp;
		}
		return null;
	}
}
