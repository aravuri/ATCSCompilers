package ast;

import emitter.Emitter;
import environment.Environment;

import java.util.Scanner;

/**
 * Represents a ReadLn statement.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class ReadLn implements Statement
{
    private final Scanner sc;
    private final Variable var;

    /**
     * Constructor for a ReadLn statement.
     * @param var the variable to read into.
     */
    public ReadLn(Variable var)
    {
        sc = new Scanner(System.in);
        this.var = var;
    }

    @Override
    public void exec(Environment env)
    {
        env.setVariable(var, sc.nextInt());
    }

    @Override
    public void compile(Emitter e)
    {
        throw new RuntimeException(":/");
    }
}
