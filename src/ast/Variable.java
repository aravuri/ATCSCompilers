package ast;

import emitter.Emitter;
import environment.Environment;
import type.Type;

/**
 * Represents a variable.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class Variable implements Expression
{

    private final String name;
    private final Type type;

    /**
     * Constructor for a variable.
     * @param name the variable name.
     */
    public Variable(String name, Type type)
    {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Variable v)) return false;
        return name.equals(v.name);
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public Object eval(Environment env)
    {
        if (env.getVariable(this) == null)
            env.setVariable(this, 0);
        return env.getVariable(this);
    }

    @Override
    public void compile(Emitter e)
    {
        e.emitRetrieve("$v0", name);
    }

    @Override
    public Type getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }
}
