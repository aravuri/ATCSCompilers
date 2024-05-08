package ast;

import emitter.Emitter;
import environment.Environment;
import type.Type;

/**
 * Represents an abstract expression.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public interface Expression
{
    /**
     * Evaluates the expression.
     * @param env The environment in which to evaluate this expression.
     * @return the value of the evaluated expression.
     */
    Object eval(Environment env);

    /**
     * Compiles this expression into MIPS assembly, storing the return value into $v0.
     * @param e the emitter containing the file to write to.
     */
    void compile(Emitter e);

    /**
     * Gets the type of this expression
     * @return a Class, the type of this expression.
     */
    Type getType();
}
