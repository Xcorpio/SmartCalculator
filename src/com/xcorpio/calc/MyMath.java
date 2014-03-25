package com.xcorpio.calc;

import java.util.ArrayList;


/**��ѧ����*/

public class MyMath implements Msg{
	
	//���Ǻ���
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
	
	//�����Ǻ���
	public static double arcsin(String params){
		return Math.asin(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double arccos(String params){
		return Math.acos(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double arctan(String params){
		return Math.atan(Double.parseDouble(ParseExpression.parse(params)));
	}
	
	//˫������
	public static double sinh(String params){
		return Math.sinh(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double cosh(String params){
		return Math.cosh(Double.parseDouble(ParseExpression.parse(params)));
	}
	public static double tanh(String params){
		return Math.tanh(Double.parseDouble(ParseExpression.parse(params)));
	}
	
	//����
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
	
	//ָ���ͽ׳�
	/** x�� nPower ���� ��pow(x,nPower) 
	 * @throws Exception */
	public static double pow(String params) throws Exception{
		String[] param=splitToTwoParams(params);
		return Math.pow (Double.parseDouble(ParseExpression.parse(param[0])),Double.parseDouble(ParseExpression.parse(param[1])));
	}
	/** e��x ���� */
	public static double exp(String params){
		return Math.exp(Double.parseDouble(ParseExpression.parse(params)));
	}
	/** �׳� 
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
	
	//ģ����Ϳ�������
	/** ģ���� 
	 * @throws Exception */
	public static double mod(String params) throws Exception{
		String[] param=splitToTwoParams(params);
		return Double.parseDouble(ParseExpression.parse(param[0]))%Double.parseDouble(ParseExpression.parse(param[1]));
	}
	/** ƽ���� */
	public static double sqrt(String params){
		return Math.sqrt(Double.parseDouble(ParseExpression.parse(params)));
	}
	/** �����η��� */
	public static double cuberoot(String params){
		return Math.cbrt(Double.parseDouble(ParseExpression.parse(params)));
	}
	/** ��ֵx��y�η���  :yroot(x,y)
	 * @throws Exception */
	public static double yroot(String params) throws Exception{
		String[] param=splitToTwoParams(params);
		return Math.pow(Double.parseDouble(ParseExpression.parse(param[0])),1/Double.parseDouble(ParseExpression.parse(param[1])));
	}
	
	//����ͳ�ƺ���
	/** ��������ƽ��ֵ */
	public static double avg(String params){
		Double[] args=parseToArray(params);
		return avg(args);
	}
	/** ����ͳ�� */
	public static double sum(String params){
		Double[] args=parseToArray(params);
		return sum(args);
	}
	/** ���Ϲ��㷽�� */
	public static double var(String params){
		Double[] args=parseToArray(params);
		double sum=0.0;
		double avg=avg(args);
		for(int i=0;i<args.length;++i){
			sum+=(args[i]-avg)*(args[i]-avg);
		}
		return sum/(args.length-1);
	}
	/** �������巽�� */
	public static double varp(String params){
		Double[] args=parseToArray(params);
		double sum=0.0;
		double avg=avg(args);
		for(int i=0;i<args.length;++i){
			sum+=(args[i]-avg)*(args[i]-avg);
		}
		return sum/args.length;
	}
	/** ���Ϲ����׼���� */
	public static double stdev(String params){
		return Math.sqrt(var(params));
	}
	/** ���������׼���� */
	public static double stdevp(String params){
		return Math.sqrt(varp(params));
	}
	
	/** ��ƽ��ֵ  */
	private static double avg(Double[] args){
		double sum=0.0;
		for(int i=0;i<args.length;++i){
			sum+=args[i];
		}
		return sum/args.length;
	}
	
	/** ���  */
	private static double sum(Double[] args){
		double sum=0.0;
		for(int i=0;i<args.length;++i){
			sum+=args[i];
		}
		return sum;
	}
	/** �Ѳ��������� ���� */
	private static Double[] parseToArray(String param){
		ArrayList<Double> results = new ArrayList<>();
		int commaIndex=0;	//��һ�ζ��ŵ�����,�����С�
		int oddBracket=0;	//�����ż�1�������ż�һ
		//System.out.println(param+":);
		for(int i=0;i<param.length();++i){
			if(param.charAt(i)=='('){
				oddBracket++;
			}else if(param.charAt(i)==')'){
				oddBracket--;
			}else if(param.charAt(i)==','){
				if(oddBracket==0){//�ǲ����ָ��
					//System.out.println(param.substring(commaIndex+1, i));
					results.add(Double.parseDouble(ParseExpression.parse(param.substring(commaIndex+1, i))));
					commaIndex=i;
				}
			}
		}
		//�ָ�ŵ����һ������
		//System.out.println(param.substring(commaIndex+1, param.length()-1));
		results.add(Double.parseDouble(ParseExpression.parse(param.substring(commaIndex+1, param.length()-1))));
		return results.toArray(new Double[]{});
	}
	/** Ϊ��Ҫ���������ĺ����Ѳ����ַ������ֳ��������� 
	 * @throws Exception */
	private static String[] splitToTwoParams(String param) throws Exception{
		String[] splits=param.split(",");
		if(splits.length==2){//ֻ��1�����š������ַ�����û�и��Ͻṹ
			return splits;
		}else if(splits.length>2){//�ж������  log(x,log(2,3)) , log(4,6)
			int commaIndex=0;	//�ָ�ŵ�����
			int oddBracket=0;	//�����ż�1�������ż�һ
			int spiltCommaCount=0;	//�����ָ�������.����������
			for(int i=0;i<param.length();++i){
				if(param.charAt(i)=='('){
					oddBracket++;
				}else if(param.charAt(i)==')'){
					oddBracket--;
				}else if(param.charAt(i)==','){
					if(oddBracket==0){//�ǲ����ָ��
						commaIndex=i;
						spiltCommaCount++;
					}
				}
			}
			if(spiltCommaCount==1){//ֻ����������
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
	
	/**���ַ�����ú���ֵ */   
    public static double getFunctionValue(String token) throws Exception {  
    	double result=0.0;
    	String origin=token;//��һ��
    	token=token.replaceAll("\\p{Space}", "");	//��Ҫȥ����������������֮��Ŀհ�
    	//���Ǻ���
        if(token.startsWith("sin(")){//����
        	result=sin(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("cos(")){//����
        	result=cos(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("tan(")){//����
        	result=tan(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //�����Ǻ���
        else if(token.startsWith("arcsin(")){
        	result=arcsin(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("arccos(")){
        	result=arccos(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("arctan(")){
        	result=arctan(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //˫������
        else if(token.startsWith("sinh(")){
        	result=sinh(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("cosh(")){
        	result=cosh(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("tanh(")){
        	result=tanh(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //����
        else if(token.startsWith("log(")){
        	result=log(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("log10(")){
        	result=log10(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("ln(")){
        	result=ln(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //ָ���ͽ׳�
        else if(token.startsWith("pow(")){
        	result=pow(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("exp(")){
        	result=exp(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("fact(")){//�׳�
        	result=fact(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //ģ����Ϳ�������
        else if(token.startsWith("mod(")){
        	result=mod(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("sqrt(")){
        	result=sqrt(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("cuberoot(")){
        	result=cuberoot(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("yroot(")){//x��y�η���
        	result=yroot(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }
        //ͳ�ƺ���
        else if(token.startsWith("avg(")){
        	result=avg(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("sum(")){
        	result=sum(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("var(")){//���㷽��
        	result=var(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("varp(")){//���巽��
        	result=varp(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("stdev(")){//�����׼����
        	result=stdev(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else if(token.startsWith("stdevp(")){//�����׼����
        	result=stdevp(token.substring(token.indexOf('(')+1, token.lastIndexOf(')')));
        }else{
        	throw new Exception(ERROR_MESSAGE[UNKNOWN_FUNCTION]+": "+origin);
        }
        
        //System.out.println(token+"="+result);
        return result;
    }
    
    /** ������ */
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
