package ast;

import type.Type;

import java.util.Map;
import java.util.function.Function;

/**
 * Wrapper for a binary operation function and its compile format.
 * Represents an abstract binary operation not necessarily in the compile tree.
 */
public class BinaryOperation
{

    private final Map<Type, String> compileFormat;
    private final Function<Object, Function<Object, Object>> run;
    Type returnType;

    public BinaryOperation(Function<Object, Function<Object, Object>> run, Map<Type, String> compileFormat, Type returnType)
    {
        this.compileFormat = compileFormat;
        this.run = run;
    }

    public String format(Type type, String ret, String lhs, String rhs)
    {
        return String.format(compileFormat.get(type), ret, lhs, rhs);
    }

    public Type getReturnType()
    {
        return returnType;
    }

    public Object apply(Object a, Object b)
    {
        return run.apply(a).apply(b);
    }
}
