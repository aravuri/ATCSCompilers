package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Represents an abstract statement.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public interface Statement
{

    /**
     * Executes the statement.
     * @param env the environment in which to execute.
     */
    void exec(Environment env);

    /**
     * Compiles this statement into MIPS assembly.
     * @param e the emitter containing the file to write to.
     */
    void compile(Emitter e);
}
