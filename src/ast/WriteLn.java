package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Represents a WriteLn statement.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class WriteLn implements Statement
{
    private final Expression expr;

    /**
     * Constructor for a WriteLn statement
     * @param expr the expression to print.
     */
    public WriteLn(Expression expr)
    {
        this.expr = expr;
    }

    @Override
    public void exec(Environment env)
    {
        System.out.println(expr.eval(env));
    }

    @Override
    public void compile(Emitter e)
    {
        e.emit("# WRITELN START:");
        expr.compile(e);
        e.emit("move $a0 $v0");
        e.emit("li $v0 1");
        e.emit("syscall");

        e.emit("li $v0 11");
        e.emit("li $a0 10");
        e.emit("syscall");
        e.emit("# WRITELN END:");
    }
}
