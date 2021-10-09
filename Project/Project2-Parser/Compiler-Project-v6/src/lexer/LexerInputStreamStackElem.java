package lexer;

import java.io.InputStream;

public class LexerInputStreamStackElem{
	private final int lineNum;
	private final String curDirAddress;
	private final InputStream inStream;
	
	public LexerInputStreamStackElem(InputStream inStream, int lineNum, String curDirAddress){
		this.lineNum= lineNum;
		this.inStream= inStream;
		this.curDirAddress= curDirAddress;
	}

	public int getLineNum(){
		return this.lineNum;
	}

	public String getCurDirAddress(){
		return this.curDirAddress;
	}
	
	public InputStream getInStream(){
		return this.inStream;
	}
}