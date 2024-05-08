package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Represents the declaration of a procedure.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class ProcedureDeclaration implements Statement
{
    private final Procedure procedure;

    /**
     * Constructor for a procedure declaration object in the AST.
     * @param procedure the procedure name.
     */
    public ProcedureDeclaration(Procedure procedure)
    {
        this.procedure = procedure;
    }

    @Override
    public void exec(Environment env)
    {
        env.setProcedure(procedure.getName(), procedure);
    }

    @Override
    public void compile(Emitter e)
    {
        e.linkProcedure(procedure.getArgs());

        e.emitFormat("%s:", e.genProcedureLabel(procedure.getName()));
        e.emit("li $v0 0");
        e.emitStore("$v0", procedure.getName());
        e.emitStore("$ra", "$ra");
        procedure.getBody().compile(e);
        e.emitRetrieve("$v0", procedure.getName());
        e.emitRetrieve("$ra", "$ra");

        e.emitFreeScope();
        e.emit("jr $ra");
    }
}
