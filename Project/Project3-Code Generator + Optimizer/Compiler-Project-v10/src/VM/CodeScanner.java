package VM;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CodeScanner {
	private ArrayList<String> Code=new ArrayList<>();
	private int PC=0;
	public CodeScanner(String file) throws FileNotFoundException
	{
		Scanner scanner=new Scanner(new File(file));
		while (scanner.hasNextLine())
			Code.add(scanner.nextLine());
		scanner.close();
	}
	public String Next()
	{
		if (PC>=0 && PC<Code.size())
		{
			String res=Code.get(PC);
			PC++;
			return res;
		}
		return null;
	}
	public int getSize()
	{
		return Code.size();
	}
	public int getPC()
	{
		return PC;
	}

	public void setPC(int PC)
	{
		this.PC = PC;
	}
}
