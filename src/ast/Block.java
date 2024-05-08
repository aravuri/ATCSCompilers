package ast;

import emitter.Emitter;
import environment.Environment;

import java.util.List;

/**
 * Represents a block of statements.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class Block implements Statement
{
    private final List<Statement> statements;

    /**
     * Constructor for a block of statements.
     * @param statements the statements in the block.
     */
    public Block(List<Statement> statements)
    {
        this.statements = statements;
    }

    /**
     * Constructor for a block of statements.
     * @param statements the statements in the block.
     */
    public Block(Statement... statements)
    {
        this.statements = List.of(statements);
    }

    @Override
    public void exec(Environment env)
    {
        for (Statement s : statements)
        {
            s.exec(env);
        }
    }

    @Override
    public void compile(Emitter e)
    {
        statements.forEach((statement) -> {
            statement.compile(e);
        });
    }
}
