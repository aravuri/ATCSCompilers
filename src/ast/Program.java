package ast;

import emitter.Emitter;
import environment.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pascal Program.
 *
 * @author Agastya Ravuri
 * @version 3.25.24
 */
public class Program
{

    private final Statement run;
    private final Block procedures;

    /**
     * Constructor for a program, which is a list of procedure declarations
     *  followed by a single statement.
     *
     * @param procedures a list of procedure declarations.
     * @param run the statement to run.
     */
    public Program(List<ProcedureDeclaration> procedures, Statement run)
    {
        this.run = run;
        this.procedures = new Block(new ArrayList<>(procedures));
    }

    /**
     * Runs the program.
     */
    public void run() {
        Environment e = new Environment();
        procedures.exec(e);
        run.exec(e);
    }

    /**
     * Compiles the program into MIPS Assembly into the file specified by the emitter.Emitter class.
     *
     * @param e the emitter used to write the compiled program
     * @postcondition the program is compiled.
     */
    public void compile(Emitter e) {
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");

        e.beginScope();
        run.compile(e);
        e.emitFreeScope();

        e.emit("li $v0 10");
        e.emit("syscall");

        procedures.compile(e);

        e.close();
    }
}
