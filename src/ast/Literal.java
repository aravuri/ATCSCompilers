package ast;

import emitter.Emitter;
import environment.Environment;
import type.Type;

/**
 * Represents a literal expression.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class Literal implements Expression
{
    private final Object value;
    private final Type type;

    /**
     * Constructor for a literal expression.
     * @param value the value of the literal.
     */
    public Literal(Object value, Type type)
    {
        this.value = value;
        this.type = type;
    }

    @Override
    public Object eval(Environment env)
    {
        return value;
    }

    @Override
    public void compile(Emitter e)
    {
        e.emit(String.format("li $v0 %d", (int) value));
    }

    @Override
    public Type getType()
    {
        return type;
    }
}
