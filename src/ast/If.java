package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Represents an if statement.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class If implements Statement
{

    private final Expression condition;
    private final Statement trueBranch;
    private final Statement falseBranch;

    /**
     * Constructor for an if statement.
     * @param condition the condition.
     * @param trueBranch the branch to go to if the condition is true.
     * @param falseBranch the branch to go to if the condition is false.
     */
    public If(Expression condition, Statement trueBranch, Statement falseBranch)
    {
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    /**
     * Constructor for an if statement.
     * @param condition the condition.
     * @param trueBranch the branch to go to if the condition is true.
     */
    public If(Expression condition, Statement trueBranch)
    {
        this(condition, trueBranch, null);
    }

    @Override
    public void exec(Environment env)
    {
        if ((Boolean) condition.eval(env))
        {
            trueBranch.exec(env);
        }
        else
        {
            if (falseBranch != null)
                falseBranch.exec(env);
        }
    }

    @Override
    public void compile(Emitter e)
    {
        if (falseBranch == null)
        {
            String exitLabel = e.genLabel();

            condition.compile(e);
            e.emitFormat("beqz $v0 %s", exitLabel);

            e.emit("# TRUE START:");
            e.beginScope();
            trueBranch.compile(e);
            e.emitFreeScope();
            e.emit("# TRUE END:");

            e.emitFormat("%s: #ENDIF", exitLabel);
        }
        else
        {
            String falseLabel = e.genLabel();
            String exitLabel = e.genLabel();

            condition.compile(e);
            e.emitFormat("beqz $v0 %s", falseLabel);

            e.emit("# TRUE START:");
            e.beginScope();
            trueBranch.compile(e);
            e.emitFreeScope();
            e.emit("# TRUE END:");

            e.emitFormat("j %s", exitLabel);
            e.emitFormat("%s:", falseLabel);

            e.emit("# FALSE START:");
            e.beginScope();
            falseBranch.compile(e);
            e.emitFreeScope();
            e.emit("# FALSE END:");

            e.emitFormat("%s: #ENDIF:", exitLabel);
        }
    }
}
