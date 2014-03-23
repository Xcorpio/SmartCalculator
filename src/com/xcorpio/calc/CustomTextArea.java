package com.xcorpio.calc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * 自定义的TextArea，实现键盘的监听，对TextView的控制
 * @author Dong
 *
 */
public class CustomTextArea extends JTextPane implements  KeyListener{

	StyledDocument mDocument;
	int mUnableEditLength;		//不能编辑的字符长度，控制退格键
	String mExpression;			//需要解析的表达式
	MainWindow mainWindow;		//Frame引用
	ArrayList<String> commandsHistory;	//存储历史命令
	FunctionHighlighter mHighlighter;	//实现函数名高亮
	int commandIndex;					//命令索引，配合commandsHistory实现 查询记录
	
	public CustomTextArea(MainWindow main) {
		super.setText(">");
		commandsHistory=new ArrayList<>(50);	//最多可保留50条记录
		commandIndex=1;							//索引
		this.mainWindow=main;
		mDocument=this.getStyledDocument();
		mHighlighter=new FunctionHighlighter(mDocument);
		mUnableEditLength=mDocument.getLength();
		this.setCaretPosition(mDocument.getLength());
		this.addKeyListener(this);
		//设置左边距,画出行数
		this.setMargin(new Insets(0, 40, 0, 0));
	}


	/**
	 * 把结果输出到TextArea
	 * @param result
	 */
	private void printResult(String result){
		try {//getLength+1 应为 换行符还没输出
			mDocument.insertString(mDocument.getLength(), result+"\n>", null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//更新不可编辑内容的长度
		mUnableEditLength=mDocument.getLength();
		this.setCaretPosition(mDocument.getLength());
	}
	
	/**
	 * 重写可绘制行号
	 */
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Element root = mDocument.getDefaultRootElement(); 
		//System.out.println("Element-> count:"+root.getElementCount()+" name:"+root.getName());
		//获得行号
		int line = root.getElementIndex(mDocument.getLength());
		//设置颜色
		g.setColor(Color.LIGHT_GRAY);
		//绘制行数右边直线
		g.drawLine(30, 0, 30, getHeight());
		//设置行号的颜色
		g.setColor(new Color(88,178,196));
		
		//每行绘制一个行号
		int ascent,height;
		ascent=this.getFontMetrics(this.getFont()).getAscent();
		height=this.getFontMetrics(this.getFont()).getHeight();
		g.drawString("    "+String.valueOf(1), 3, ascent );
		for (int count = 1, j = 2; count <= line; count++, j++) {
			//三个字符，靠右边
			String num=String.valueOf(j);
			if(num.length()==1){
				num="    "+num;
			}else if(num.length()==2){
				num="  "+num;
			}
			g.drawString(num, 3,ascent+count*height);
		}
	}
	

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("keyType->char:"+e.getKeyChar()+" code:"+e.getKeyCode());
	}
	/** 按键处理 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("keyPressed->char:"+e.getKeyChar()+" code:"+e.getKeyCode());
		if(e.getKeyCode()==10){
			this.setCaretPosition(mDocument.getLength());
		}
		if(this.getCaretPosition()<mUnableEditLength||e.getKeyCode()==8&&mDocument.getLength()<=mUnableEditLength||(mDocument.getLength()-(this.getSelectionEnd()-this.getSelectionStart())<mUnableEditLength)){
			//按下退格键，如果删除后，就到了'>'字符前的内容，则不可删除，光标在不可编辑区域
			this.setEditable(false);
		}else{
			this.setEditable(true);
		}
		if(this.getCaretPosition()<mUnableEditLength&&Character.isLetter(e.getKeyChar())){
			//如果当前光标在不可编辑区域，且字符是字幕，则把光标定位到最后
			this.setCaretPosition(mDocument.getLength());
			this.setEditable(true);
		}
		
		//处理方向键，显示历史记录
		if(e.getKeyCode()==KeyEvent.VK_UP){//显示上一条记录
			if(commandsHistory.size()>0){
				this.setCaretColor(this.getBackground());//设置和背景颜色一样，防止显示跳位置
				if(commandIndex==0){
					commandIndex=commandsHistory.size()-1;
				}else{
					commandIndex--;
				}
				try {
					//System.out.println(mUnableEditLength+"  "+mDocument.getLength());
					mDocument.remove(mUnableEditLength, mDocument.getLength()-this.mUnableEditLength);
					mDocument.insertString(mUnableEditLength, commandsHistory.get(commandIndex), null);
					this.setCaretPosition(mDocument.getLength());
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN){//显示下一条记录
			if(commandIndex>=commandsHistory.size()-1){
				//commandIndex=0;	//再按下，不向上循环
			}else{
				commandIndex++;
				if(commandsHistory.size()>0){
					try {
						mDocument.remove(mUnableEditLength, mDocument.getLength()-this.mUnableEditLength);
						mDocument.insertString(mUnableEditLength, commandsHistory.get(commandIndex), null);
						this.setCaretPosition(mDocument.getLength());
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

	/** 释放键盘处理 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("keyReleased->char:"+e.getKeyChar()+" code:"+e.getKeyCode());
		if(e.getKeyCode()==10){//换行符
			try {//取得表达式的字符串
				mExpression=mDocument.getText(mUnableEditLength, mDocument.getLength()-mUnableEditLength-1);//减换行符
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println("expression:"+mExpression+"  length:"+mExpression.length());
			//加入记录
			String tmp=mExpression.trim();
			if(tmp.length()>0){//是有效记录
				if(commandsHistory.size()<50){//容器未满
					if(commandsHistory.size()>0&&!tmp.endsWith(commandsHistory.get(commandsHistory.size()-1))){//和上条记录不一样
						commandsHistory.add(tmp);
						commandIndex=commandsHistory.size();
					}else if(commandsHistory.size()==0){
						commandsHistory.add(tmp);
						commandIndex=commandsHistory.size();
					}
				}else{	//容器已满，清除首个记录
					if(commandsHistory.size()>0&&!tmp.endsWith(commandsHistory.get(commandsHistory.size()-1))){//和上条记录不一样
						commandsHistory.remove(0);
						commandsHistory.add(tmp);
						commandIndex=49;
					}else if(commandsHistory.size()==0){
						commandsHistory.remove(0);
						commandsHistory.add(tmp);
						commandIndex=49;
					}
				}
			}
			//System.out.println(commandsHistory.size()+":"+commandIndex+commandsHistory);
			
			//判断是否为命令
			String com=null;
			if((com=ParseExpression.isCommand(mExpression))==null){//不是条命令
				this.printResult(ParseExpression.parse(mExpression));
			}else{//是条命令
				if(com.equals("exit")){
					System.exit(0);
				}else if(com.endsWith("clear")){
					//把状态初始化
					this.setText(">");
					mUnableEditLength=mDocument.getLength();
					this.setCaretPosition(mDocument.getLength());
				}
			}
			//使水平滚动条，到最左边
			this.mainWindow.mScrollPane.getHorizontalScrollBar().setValue(0);
		}
		if(e.getKeyCode()==KeyEvent.VK_UP||e.getKeyCode()==KeyEvent.VK_DOWN){
			//把光标射到最后
			this.setCaretPosition(mDocument.getLength());
			this.setCaretColor(Color.BLACK);
		}
	}

	/**  当文本输入区的有字符插入或者删除时, 进行高亮.  */
	public class FunctionHighlighter implements DocumentListener {
		private Set<String> keywords;
		private Style keywordStyle;
		private Style normalStyle;

		public FunctionHighlighter(StyledDocument document) {
			
			document.addDocumentListener(this);
			// 准备着色使用的样式
			keywordStyle =document.addStyle("Keyword_Style", null);
			normalStyle = document.addStyle("Keyword_Style", null);
			//设置颜色
			StyleConstants.setForeground(keywordStyle, new Color(148,0,85));
			StyleConstants.setForeground(normalStyle, Color.BLACK);

			// 准备关键字
			keywords = new HashSet<String>();
			String[] key={
					"sin","cos","tan","arcsin","arccos","arctan",
					"sinh","cosh","tanh","log","log10","ln",
					"pow","exp","fact","mod","sqrt","cuberoot","yroot",
					"avg","sum","var","varp","stdev","stdevp",
					"clear","exit"
					};
			for(int i=0;i<key.length;++i){
				keywords.add(key[i]);
			}
		}

		public void colouring(StyledDocument doc, int pos, int len)
				throws BadLocationException {
			// 取得插入或者删除后影响到的单词.
			// 例如"public"在b后插入一个空格, 就变成了:"pub lic", 这时就有两个单词要处理:"pub"和"lic"
			// 这时要取得的范围是pub中p前面的位置和lic中c后面的位置
			int start = indexOfWordStart(doc, pos);
			int end = indexOfWordEnd(doc, pos + len);

			char ch;
			while (start < end) {
				ch = getCharAt(doc, start);
				if (Character.isLetter(ch) || ch == '_') {
					// 如果是以字母或者下划线开头, 说明是单词
					// pos为处理后的最后一个下标
					start = colouringWord(doc, start);
				} else {
					SwingUtilities.invokeLater(new ColouringTask(doc, start, 1,
							normalStyle));
					++start;
				}
			}
		}

		/**
		 * 对单词进行着色, 并返回单词结束的下标.
		 * @param doc
		 * @param pos
		 * @return
		 * @throws BadLocationException
		 */
		public int colouringWord(StyledDocument doc, int pos)
				throws BadLocationException {
			int wordEnd = indexOfWordEnd(doc, pos);
			String word = doc.getText(pos, wordEnd - pos);

			if (keywords.contains(word)) {
				// 如果是关键字, 就进行关键字的着色, 否则使用普通的着色.
				// 这里有一点要注意, 在insertUpdate和removeUpdate的方法调用的过程中, 不能修改doc的属性.
				// 但我们又要达到能够修改doc的属性, 所以把此任务放到这个方法的外面去执行.
				// 实现这一目的, 可以使用新线程, 但放到swing的事件队列里去处理更轻便一点.
				SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd
						- pos, keywordStyle));
			} else {
				SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd
						- pos, normalStyle));
			}

			return wordEnd;
		}

		/**
		 * 取得在文档中下标在pos处的字符.
		 * 如果pos为doc.getLength(), 返回的是一个文档的结束符, 不会抛出异常. 如果pos<0, 则会抛出异常.
		 * 所以pos的有效值是[0, doc.getLength()]
		 * @param doc
		 * @param pos
		 * @return
		 * @throws BadLocationException
		 */
		public char getCharAt(Document doc, int pos) throws BadLocationException {
			return doc.getText(pos, 1).charAt(0);
		}

		/**
		 * 取得下标为pos时, 它所在的单词开始的下标.
		 * @param doc
		 * @param pos
		 * @return
		 * @throws BadLocationException
		 */
		public int indexOfWordStart(Document doc, int pos)
				throws BadLocationException {
			// 从pos开始向前找到第一个非单词字符.
			for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos)
				;
			return pos;
		}

		/**
		 * 取得下标为pos时, 它所在的单词结束的下标.
		 * @param doc
		 * @param pos
		 * @return
		 * @throws BadLocationException
		 */
		public int indexOfWordEnd(Document doc, int pos)
				throws BadLocationException {
			// 从pos开始向前找到第一个非单词字符.
			for (; isWordCharacter(doc, pos); ++pos)
				;
			return pos;
		}

		/**
		 * 如果一个字符是字母, 数字, 下划线, 则返回true.
		 * 
		 * @param doc
		 * @param pos
		 * @return
		 * @throws BadLocationException
		 */
		public boolean isWordCharacter(Document doc, int pos)
				throws BadLocationException {
			char ch = getCharAt(doc, pos);
			if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') {
				return true;
			}
			return false;
		}

		@Override
		public void changedUpdate(DocumentEvent e) {

		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			//System.out.println("Document insert event");
			try {
				//设置行背景白色无效
				colouring((StyledDocument) e.getDocument(), e.getOffset(),
						e.getLength());
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			//System.out.println("Document remove event");
			try {
				// 因为删除后光标紧接着影响的单词两边, 所以长度就不需要了
				colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}

		/**
		 * 完成着色任务
		 * 
		 */
		private class ColouringTask implements Runnable {
			private StyledDocument doc;
			private Style style;
			private int pos;
			private int len;

			public ColouringTask(StyledDocument doc, int pos, int len, Style style) {
				this.doc = doc;
				this.pos = pos;
				this.len = len;
				this.style = style;
			}

			public void run() {
				try {
					// 这里就是对字符进行着色
					doc.setCharacterAttributes(pos, len, style, true);
				} catch (Exception e) {
				}
			}
		}
	}
	
}
