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
 * �Զ����TextArea��ʵ�ּ��̵ļ�������TextView�Ŀ���
 * @author Dong
 *
 */
public class CustomTextArea extends JTextPane implements  KeyListener{

	StyledDocument mDocument;
	int mUnableEditLength;		//���ܱ༭���ַ����ȣ������˸��
	String mExpression;			//��Ҫ�����ı��ʽ
	MainWindow mainWindow;		//Frame����
	ArrayList<String> commandsHistory;	//�洢��ʷ����
	FunctionHighlighter mHighlighter;	//ʵ�ֺ���������
	int commandIndex;					//�������������commandsHistoryʵ�� ��ѯ��¼
	
	public CustomTextArea(MainWindow main) {
		super.setText(">");
		commandsHistory=new ArrayList<>(50);	//���ɱ���50����¼
		commandIndex=1;							//����
		this.mainWindow=main;
		mDocument=this.getStyledDocument();
		mHighlighter=new FunctionHighlighter(mDocument);
		mUnableEditLength=mDocument.getLength();
		this.setCaretPosition(mDocument.getLength());
		this.addKeyListener(this);
		//������߾�,��������
		this.setMargin(new Insets(0, 40, 0, 0));
	}


	/**
	 * �ѽ�������TextArea
	 * @param result
	 */
	private void printResult(String result){
		try {//getLength+1 ӦΪ ���з���û���
			mDocument.insertString(mDocument.getLength(), result+"\n>", null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//���²��ɱ༭���ݵĳ���
		mUnableEditLength=mDocument.getLength();
		this.setCaretPosition(mDocument.getLength());
	}
	
	/**
	 * ��д�ɻ����к�
	 */
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Element root = mDocument.getDefaultRootElement(); 
		//System.out.println("Element-> count:"+root.getElementCount()+" name:"+root.getName());
		//����к�
		int line = root.getElementIndex(mDocument.getLength());
		//������ɫ
		g.setColor(Color.LIGHT_GRAY);
		//���������ұ�ֱ��
		g.drawLine(30, 0, 30, getHeight());
		//�����кŵ���ɫ
		g.setColor(new Color(88,178,196));
		
		//ÿ�л���һ���к�
		int ascent,height;
		ascent=this.getFontMetrics(this.getFont()).getAscent();
		height=this.getFontMetrics(this.getFont()).getHeight();
		g.drawString("    "+String.valueOf(1), 3, ascent );
		for (int count = 1, j = 2; count <= line; count++, j++) {
			//�����ַ������ұ�
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
	/** �������� */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("keyPressed->char:"+e.getKeyChar()+" code:"+e.getKeyCode());
		if(e.getKeyCode()==10){
			this.setCaretPosition(mDocument.getLength());
		}
		if(this.getCaretPosition()<mUnableEditLength||e.getKeyCode()==8&&mDocument.getLength()<=mUnableEditLength||(mDocument.getLength()-(this.getSelectionEnd()-this.getSelectionStart())<mUnableEditLength)){
			//�����˸�������ɾ���󣬾͵���'>'�ַ�ǰ�����ݣ��򲻿�ɾ��������ڲ��ɱ༭����
			this.setEditable(false);
		}else{
			this.setEditable(true);
		}
		if(this.getCaretPosition()<mUnableEditLength&&Character.isLetter(e.getKeyChar())){
			//�����ǰ����ڲ��ɱ༭�������ַ�����Ļ����ѹ�궨λ�����
			this.setCaretPosition(mDocument.getLength());
			this.setEditable(true);
		}
		
		//�����������ʾ��ʷ��¼
		if(e.getKeyCode()==KeyEvent.VK_UP){//��ʾ��һ����¼
			if(commandsHistory.size()>0){
				this.setCaretColor(this.getBackground());//���úͱ�����ɫһ������ֹ��ʾ��λ��
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
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN){//��ʾ��һ����¼
			if(commandIndex>=commandsHistory.size()-1){
				//commandIndex=0;	//�ٰ��£�������ѭ��
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

	/** �ͷż��̴��� */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("keyReleased->char:"+e.getKeyChar()+" code:"+e.getKeyCode());
		if(e.getKeyCode()==10){//���з�
			try {//ȡ�ñ��ʽ���ַ���
				mExpression=mDocument.getText(mUnableEditLength, mDocument.getLength()-mUnableEditLength-1);//�����з�
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println("expression:"+mExpression+"  length:"+mExpression.length());
			//�����¼
			String tmp=mExpression.trim();
			if(tmp.length()>0){//����Ч��¼
				if(commandsHistory.size()<50){//����δ��
					if(commandsHistory.size()>0&&!tmp.endsWith(commandsHistory.get(commandsHistory.size()-1))){//��������¼��һ��
						commandsHistory.add(tmp);
						commandIndex=commandsHistory.size();
					}else if(commandsHistory.size()==0){
						commandsHistory.add(tmp);
						commandIndex=commandsHistory.size();
					}
				}else{	//��������������׸���¼
					if(commandsHistory.size()>0&&!tmp.endsWith(commandsHistory.get(commandsHistory.size()-1))){//��������¼��һ��
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
			
			//�ж��Ƿ�Ϊ����
			String com=null;
			if((com=ParseExpression.isCommand(mExpression))==null){//����������
				this.printResult(ParseExpression.parse(mExpression));
			}else{//��������
				if(com.equals("exit")){
					System.exit(0);
				}else if(com.endsWith("clear")){
					//��״̬��ʼ��
					this.setText(">");
					mUnableEditLength=mDocument.getLength();
					this.setCaretPosition(mDocument.getLength());
				}
			}
			//ʹˮƽ���������������
			this.mainWindow.mScrollPane.getHorizontalScrollBar().setValue(0);
		}
		if(e.getKeyCode()==KeyEvent.VK_UP||e.getKeyCode()==KeyEvent.VK_DOWN){
			//�ѹ���䵽���
			this.setCaretPosition(mDocument.getLength());
			this.setCaretColor(Color.BLACK);
		}
	}

	/**  ���ı������������ַ��������ɾ��ʱ, ���и���.  */
	public class FunctionHighlighter implements DocumentListener {
		private Set<String> keywords;
		private Style keywordStyle;
		private Style normalStyle;

		public FunctionHighlighter(StyledDocument document) {
			
			document.addDocumentListener(this);
			// ׼����ɫʹ�õ���ʽ
			keywordStyle =document.addStyle("Keyword_Style", null);
			normalStyle = document.addStyle("Keyword_Style", null);
			//������ɫ
			StyleConstants.setForeground(keywordStyle, new Color(148,0,85));
			StyleConstants.setForeground(normalStyle, Color.BLACK);

			// ׼���ؼ���
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
			// ȡ�ò������ɾ����Ӱ�쵽�ĵ���.
			// ����"public"��b�����һ���ո�, �ͱ����:"pub lic", ��ʱ������������Ҫ����:"pub"��"lic"
			// ��ʱҪȡ�õķ�Χ��pub��pǰ���λ�ú�lic��c�����λ��
			int start = indexOfWordStart(doc, pos);
			int end = indexOfWordEnd(doc, pos + len);

			char ch;
			while (start < end) {
				ch = getCharAt(doc, start);
				if (Character.isLetter(ch) || ch == '_') {
					// ���������ĸ�����»��߿�ͷ, ˵���ǵ���
					// posΪ���������һ���±�
					start = colouringWord(doc, start);
				} else {
					SwingUtilities.invokeLater(new ColouringTask(doc, start, 1,
							normalStyle));
					++start;
				}
			}
		}

		/**
		 * �Ե��ʽ�����ɫ, �����ص��ʽ������±�.
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
				// ����ǹؼ���, �ͽ��йؼ��ֵ���ɫ, ����ʹ����ͨ����ɫ.
				// ������һ��Ҫע��, ��insertUpdate��removeUpdate�ķ������õĹ�����, �����޸�doc������.
				// ��������Ҫ�ﵽ�ܹ��޸�doc������, ���԰Ѵ�����ŵ��������������ȥִ��.
				// ʵ����һĿ��, ����ʹ�����߳�, ���ŵ�swing���¼�������ȥ��������һ��.
				SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd
						- pos, keywordStyle));
			} else {
				SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd
						- pos, normalStyle));
			}

			return wordEnd;
		}

		/**
		 * ȡ�����ĵ����±���pos�����ַ�.
		 * ���posΪdoc.getLength(), ���ص���һ���ĵ��Ľ�����, �����׳��쳣. ���pos<0, ����׳��쳣.
		 * ����pos����Чֵ��[0, doc.getLength()]
		 * @param doc
		 * @param pos
		 * @return
		 * @throws BadLocationException
		 */
		public char getCharAt(Document doc, int pos) throws BadLocationException {
			return doc.getText(pos, 1).charAt(0);
		}

		/**
		 * ȡ���±�Ϊposʱ, �����ڵĵ��ʿ�ʼ���±�.
		 * @param doc
		 * @param pos
		 * @return
		 * @throws BadLocationException
		 */
		public int indexOfWordStart(Document doc, int pos)
				throws BadLocationException {
			// ��pos��ʼ��ǰ�ҵ���һ���ǵ����ַ�.
			for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos)
				;
			return pos;
		}

		/**
		 * ȡ���±�Ϊposʱ, �����ڵĵ��ʽ������±�.
		 * @param doc
		 * @param pos
		 * @return
		 * @throws BadLocationException
		 */
		public int indexOfWordEnd(Document doc, int pos)
				throws BadLocationException {
			// ��pos��ʼ��ǰ�ҵ���һ���ǵ����ַ�.
			for (; isWordCharacter(doc, pos); ++pos)
				;
			return pos;
		}

		/**
		 * ���һ���ַ�����ĸ, ����, �»���, �򷵻�true.
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
				//�����б�����ɫ��Ч
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
				// ��Ϊɾ�����������Ӱ��ĵ�������, ���Գ��ȾͲ���Ҫ��
				colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}

		/**
		 * �����ɫ����
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
					// ������Ƕ��ַ�������ɫ
					doc.setCharacterAttributes(pos, len, style, true);
				} catch (Exception e) {
				}
			}
		}
	}
	
}
