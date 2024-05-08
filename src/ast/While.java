package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Represents a while loop statement.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class While implements Statement
{
    private final Expression condition;
    private final Statement body;

    /**
     * Constructor for a while loop statement
     * @param condition the condition.
     * @param body the body of the loop.
     */
    public While(Expression condition, Statement body)
    {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void exec(Environment env)
    {
        while ((Boolean) condition.eval(env))
        {
            body.exec(env);
        }
    }

    @Override
    public void compile(Emitter e)
    {
        String startLabel = e.genLabel();
        e.emitFormat("%s:", startLabel);
        body.compile(e);
        condition.compile(e);
        e.emitFormat("bnez $v0 %s", startLabel);
    }
}
