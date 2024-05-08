package emitter;

import ast.Variable;

import java.io.*;
import java.util.*;

public class Emitter
{
	private PrintWriter out;
	private Map<String, Integer> offsetMap;
	private Stack<List<String>> scopes;

	private int bytesPushed;
	private int nextLabelID;

	//creates an emitter for writing to a new file with given name
	public Emitter(String outputFileName)
	{
		try
		{
			out = new PrintWriter(new FileWriter(outputFileName), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}

		nextLabelID = 0;
		bytesPushed = 0;

		offsetMap = new HashMap<>();
		scopes = new Stack<>();
	}

	//prints one line of code to file (with non-labels indented)
	public void emit(String code)
	{
		if (!code.endsWith(":"))
			code = "\t" + code;
		out.println(code);
	}
	
	public void emitFormat(String code, Object... args)
	{
		emit(String.format(code, args));
	}

	public void emitPush(String reg)
	{
//		if (reg.charAt(1) == 'f') // double register
//		{
//			emit("subu $sp $sp 8");
//			emitFormat("s.d %s ($sp)\t# Push dword to stack", reg);
//			bytesPushed += 8;
//			return;
//		}
		emit("subu $sp $sp 4");
		emitFormat("sw %s ($sp)\t# Push word to stack", reg);
		bytesPushed += 4;
	}

	public void emitPushArg(String reg)
	{
//		if (reg.charAt(1) == 'f') // double register
//		{
//			emit("subu $sp $sp 8");
//			emitFormat("s.d %s ($sp)\t# Push dword to stack", reg);
//			return;
//		}
		emit("subu $sp $sp 4");
		emitFormat("sw %s ($sp)\t# Push word to stack", reg);
	}

	public void emitPop(String reg)
	{
//		if (reg.charAt(1) == 'f') // double register
//		{
//			emitFormat("l.d %s ($sp)", reg);
//			emit("addu $sp $sp 8\t# Pop dword from stack");
//		}
		emitFormat("lw %s ($sp)", reg);
		emit("addu $sp $sp 4\t# Pop from stack");
		bytesPushed -= 4;
	}

	public void emitStore(String reg, String varName)
	{
		if (offsetMap.containsKey(varName))
		{
			emitFormat("# Set variable %s:", varName);
			int offset = bytesPushed - offsetMap.get(varName);
			emitFormat("sw %s %d($sp)", reg, offset);
		}
		else
		{
			emitFormat("# Store variable %s:", varName);
			emitPush(reg);
			offsetMap.put(varName, bytesPushed);
			scopes.peek().add(varName);
		}
	}

	/**
	 * Gets the value of a variable and puts it in reg
	 * @param reg the register to load the variable into.
	 * @param varName the variable name.
	 */
	public void emitRetrieve(String reg, String varName)
	{
		emitFormat("# Retrieve variable %s:", varName);

		if (!offsetMap.containsKey(varName))
		{
			emitFormat("li %s 0", reg);
			return;
		}
		int offset = bytesPushed - offsetMap.get(varName);
		emitFormat("lw %s %d($sp)", reg, offset);
	}

	public void linkProcedure(List<Variable> args)
	{
		beginScope();
		bytesPushed = args.size() * 4;
		for (int i = 0; i < args.size(); i++)
		{
			offsetMap.put(args.get(i).getName(), (i + 1) * 4);
			scopes.peek().add(args.get(i).getName());
		}
	}

	public void beginScope()
	{
		scopes.push(new ArrayList<>());
	}

	public void emitFreeScope()
	{
		List<String> clear = scopes.pop();

		if (clear.size() > 0)
		{
			emitFormat("addu $sp $sp %d", 4 * clear.size());
			bytesPushed = bytesPushed - 4 * clear.size();
			for (String var : clear)
			{
				offsetMap.remove(var);
			}
		}
	}

	public String genLabel()
	{
		return "label" + (nextLabelID++);
	}
	public String genProcedureLabel(String procedureName)
	{
		return "procedure" + procedureName;
	}

	//closes the file.  should be called after all calls to emit.
	public void close()
	{
		out.close();
	}
}