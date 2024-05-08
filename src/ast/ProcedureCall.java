package ast;

import emitter.Emitter;
import environment.Environment;
import type.Type;

import java.util.List;

/**
 * Represents a procedure call.
 *
 * @author Agastya Ravuri
 * @version 3.25.2024
 */
public class ProcedureCall implements Expression, Statement
{
    private final String name;
    private final List<Expression> args;

    /**
     * Constructor for a procedure call.
     *
     * @param name the procedure called.
     * @param args the arguments passed into the procedure.
     */
    public ProcedureCall(String name, List<Expression> args)
    {
        this.name = name;
        this.args = args;
    }

    @Override
    public Object eval(Environment env)
    {
        Procedure p = env.getProcedure(name);
        Environment scope = new Environment(env);
        List<Variable> vars = p.getArgs();
        for (int i = 0; i < args.size(); i++)
        {
            scope.declareVariable(vars.get(i), args.get(i).eval(env));
        }
        scope.declareVariable(new Variable(p.getName(), Type.INT), 0);
        p.getBody().exec(scope);
        return scope.getVariable(new Variable(p.getName(), Type.INT));
    }

    @Override
    public void exec(Environment env)
    {
        eval(env);
    }

    @Override
    public void compile(Emitter e)
    {
        for (Expression arg : args)
        {
            arg.compile(e);
            e.emitPushArg("$v0");
        }
        e.emitFormat("jal %s", e.genProcedureLabel(name));
    }

    @Override
    public Type getType()
    {
        return Type.INT;
    }
}
