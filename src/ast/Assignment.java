package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Represents an assignment expression.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class Assignment implements Statement
{
    private final Variable lhs;
    private final Expression rhs;

    /**
     * Constructor for an assignment statement
     * @param lhs the left hand side
     * @param rhs the right hand side
     */
    public Assignment(Variable lhs, Expression rhs)
    {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void exec(Environment env)
    {
        env.setVariable(lhs, rhs.eval(env));
    }

    @Override
    public void compile(Emitter e)
    {
        e.emit("# ASSIGNMENT START:");
        rhs.compile(e);
        e.emitStore("$v0", lhs.getName());
        e.emit("# ASSIGNMENT END:");
    }
}
