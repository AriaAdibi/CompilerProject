package VM;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Scanner;

public class VM {

	private CodeScanner reader;
	private Scanner system;
	private final static int memSize=200*1000*1000;
	private byte[] mem = new byte[memSize];
	private int stackPointer = memSize;

	public static void main(String[] args){
		if (args.length != 1)
			System.err.println("usage: VM file");
		else{
			try{
				VM vm = new VM(args[0]);
				vm.execute();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public VM(String fileName) throws Exception{
		reader = new CodeScanner(fileName);
		system = new Scanner(System.in);
	}

	public void execute() throws Exception
	{
		String operand1, operand2, operand3;
		OperandType type;
		Opcode opcode;
		while (reader.getPC() >= 0 && reader.getPC() < reader.getSize())
		{
			String line=reader.Next();
			Scanner lineScanner=new Scanner(line);
			if (!lineScanner.hasNext())
			{
				lineScanner.close();
				continue;
			}
			opcode=getOpcode(lineScanner.next());
			switch (opcode)
			{
				case PLUS:
				case MINUS:
				case MUL:
				case DIV:
				case MOD:
				{
					operand1 = lineScanner.next();
					operand2 = lineScanner.next();
					operand3 = lineScanner.next();
					if (getType(operand1).equals(getType(operand2)) && getType(operand1).equals(getType(operand3)))
					{
						type = getType(operand1);
						switch (type)
						{
							case INT:
							{
								int f = getInt(operand2), s = getInt(operand3);
								int result = (opcode.equals(Opcode.PLUS) ? f + s : opcode.equals(Opcode.MINUS) ? f - s : opcode.equals(Opcode.MUL) ? f * s : opcode.equals(Opcode.DIV) ? f / s : f % s);
								put(operand1, result);
								break;
							}
							case FLOAT:
							{
								float f = getFloat(operand2), s = getFloat(operand3);
								float result = (opcode.equals(Opcode.PLUS) ? f + s : opcode.equals(Opcode.MINUS) ? f - s : opcode.equals(Opcode.MUL) ? f * s : opcode.equals(Opcode.DIV) ? f / s : f % s);
								put(operand1,result);
								break;
							}

						}
					}
					break;
				}
				case AND:
				case OR:
				{
					operand1 = lineScanner.next();
					operand2 = lineScanner.next();
					operand3 = lineScanner.next();
					boolean f=getBool(operand2);
					boolean s=getBool(operand3);
					put(operand1,opcode.equals(Opcode.AND)?(f&&s):(f||s));
					break;
				}
				case NE:
				case E:
				{
					operand1 = lineScanner.next();
					operand2 = lineScanner.next();
					operand3 = lineScanner.next();
					if (getType(operand2).equals(getType(operand3)))
					{
						type = getType(operand2);
						boolean res = false;
						switch (type)
						{
							case INT:
								res=(getInt(operand2)==getInt(operand3));
								break;
							case FLOAT:
								res=(getFloat(operand2)==getFloat(operand3));
								break;
							case BOOL:
								res=(getBool(operand2)==getBool(operand3));
								break;
							case CHAR:
								res=(getChar(operand2)==getChar(operand3));
								break;
						}
						if (opcode.equals(Opcode.NE))
							res=!res;
						put(operand1,res);
					}
					break;
				}
				case L:
				case G:
				case LE:
				case GE:
				{
					operand1 = lineScanner.next();
					operand2 = lineScanner.next();
					operand3 = lineScanner.next();
					if (getType(operand2).equals(getType(operand3)))
					{
						type = getType(operand2);
						boolean lt=false,eq=false;
						switch (type)
						{
							case INT:
								eq=(getInt(operand2)==getInt(operand3));
								lt=(getInt(operand2)<getInt(operand3));
								break;
							case FLOAT:
								eq=(getFloat(operand2)==getFloat(operand3));
								lt=(getFloat(operand2)<getFloat(operand3));
								break;
							case CHAR:
								eq=(getChar(operand2)==getChar(operand3));
								lt=(getChar(operand2)<getChar(operand3));
								break;
						}
						put(operand1,(opcode.equals(Opcode.L)?lt:opcode.equals(Opcode.LE)?(lt || eq):opcode.equals(Opcode.G)?(!lt && !eq):(!lt || eq)));
					}
					break;
				}
				case NOT:
				{
					operand1=lineScanner.next();
					operand2=lineScanner.next();
					put(operand1,!getBool(operand2));
					break;
				}
				case RI:
				{
					operand1 = lineScanner.next();
					put(operand1,system.nextInt());
					break;
				}
				case RF:
				{
					operand1 = lineScanner.next();
					put(operand1,system.nextFloat());
					break;
				}
				case RB:
				{
					operand1 = lineScanner.next();
					put(operand1,system.nextBoolean());
					break;
				}
				case RC:
				{
					operand1 = lineScanner.next();
					put(operand1,system.next(".").charAt(0));
					break;
				}
				case WI:
				{
					operand1 = lineScanner.next();
					int f = getInt(operand1);
					System.out.print(f);
					break;
				}
				case WF:
				{
					operand1 = lineScanner.next();
					float f = getFloat(operand1);
					System.out.print(f);
					break;
				}
				case WB:
				{
					operand1 = lineScanner.next();
					boolean f = getBool(operand1);
					System.out.print((f?"true":"false"));
					break;
				}
				case WC:
				{
					operand1 = lineScanner.next();
					char f = getChar(operand1);
					System.out.print(f);
					break;
				}
				case ASSIGN:
				{
					operand1 = lineScanner.next();
					operand2 = lineScanner.next();
					if (getType(operand1).equals(getType(operand2)))
					{
						type = getType(operand1);
						switch (type)
						{
							case INT:
							{
								int f = getInt(operand2);
								put(operand1, f);
								break;
							}
							case FLOAT:
							{
								float f=getFloat(operand2);
								put(operand1,f);
								break;
							}
							case CHAR:
							{
								char f=getChar(operand2);
								put(operand1,f);
								break;
							}
							case BOOL:
							{
								boolean f=getBool(operand2);
								put(operand1,f);
								break;
							}
						}
					}
					break;
				}
				case PC:
				{
					operand1=lineScanner.next();
					put(operand1,reader.getPC());
					break;
				}
				case JT:
				{
					operand1=lineScanner.next();
					operand2=lineScanner.next();
					boolean f=getBool(operand1);
					if (f)
						reader.setPC(getInt(operand2));
					break;
				}
				case JMP:
				{
					operand1=lineScanner.next();
					reader.setPC(getInt(operand1));
					break;
				}
				case SP:
				{
					operand1=lineScanner.next();
					put(operand1,stackPointer);
					break;
				}
				case ASP:
				{
					operand1=lineScanner.next();
					stackPointer=getInt(operand1);
					break;
				}
				case RET:
				{
					operand1=lineScanner.next();
					operand2=lineScanner.next();
					reader.setPC(getInt(operand1));
					stackPointer=getInt(operand2);
					break;
				}

			}
			lineScanner.close();
		}
	}
	private <T> void put(String operand,T res)
	{
		int t=getAddress(operand);
		if (res instanceof Integer)
			writeToMem(t,ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt((Integer)res).array());
		if (res instanceof Float)
			writeToMem(t,ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat((Float) res).array());
		if (res instanceof Character)
			writeToMem(t, Charset.forName("US-ASCII").encode(Character.toString((Character)res)).array());
		if (res instanceof Boolean)
			writeToMem(t,ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN).put((byte)((Boolean)res?1:0)).array());
	}



	private int getInt(String operand)
	{
		if (operand.substring(0,2).equals("im"))
			return Integer.parseInt(operand.substring(5));
		int t=getAddress(operand);
		return getIntByAddress(t);
	}
	private float getFloat(String operand)
	{
		if (operand.substring(0,2).equals("im"))
			return Float.parseFloat(operand.substring(5));
		int t=getAddress(operand);
		return ByteBuffer.wrap(readFromMemory(t,4)).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get();
	}
	private boolean getBool(String operand)
	{
		if (operand.substring(0,2).equals("im"))
			return Boolean.parseBoolean(operand.substring(5));
		int t=getAddress(operand);
		return readFromMemory(t,1)[0]!=0;
	}
	private char getChar(String operand)
	{
		if (operand.substring(0,2).equals("im"))
			return (char)Integer.parseInt(operand.substring(5));
		int t=getAddress(operand);
		return Charset.forName("US-ASCII").decode(ByteBuffer.wrap(readFromMemory(t,1)).order(ByteOrder.LITTLE_ENDIAN)).get();
	}
	private int getIntByAddress(int address)
	{
		return ByteBuffer.wrap(readFromMemory(address,4)).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().get();
	}

	private int getAddress(String operand)
	{
		int res;
		if (operand.charAt(1)=='i')
			res=getIntByAddress((operand.charAt(0)=='l'?stackPointer:0)+Integer.parseInt(operand.substring(5)));
		else
			res = Integer.parseInt(operand.substring(5));
		if (operand.charAt(0)=='l')
			res+=stackPointer;
		return res;

	}

	private OperandType getType(String operand)
	{
		switch (operand.charAt(3))
		{
			case 'i':
				return OperandType.INT;
			case 'f':
				return OperandType.FLOAT;
			case 'b':
				return OperandType.BOOL;
			case 'c':
				return OperandType.CHAR;
			default:
				return null;
		}
	}

	private Opcode getOpcode(String next)
	{
		for (int i=0;i<Opcode.strings.length;i++) if (next.equals(Opcode.strings[i]))
			return Opcode.values()[i];
		return null;
	}
	private void writeToMem(int address, byte[] array)
	{
		System.arraycopy(array, 0, mem, address, array.length);
	}
	private byte[] readFromMemory(int address, int length)
	{
		byte [] res=new byte[length];
		System.arraycopy(mem, address, res, 0, length);
		return res;
	}
}
