package ast;

import java.util.List;

/**
 * Represents a procedure.
 *
 * @author Agastya Ravuri
 * @version 3.25.2024
 */
public class Procedure
{

    private final String name;
    private final List<Variable> args;
    private final Statement run;

    /**
     * Constructor for a procedure.
     *
     * @param name the name of the procedure.
     * @param args the arguments of the procedure.
     * @param run the body of the procedure.
     */
    public Procedure(String name, List<Variable> args, Statement run)
    {
        this.name = name;
        this.args = args;
        this.run = run;
    }

    /**
     * Gets the name of the procedure.
     * @return a String, the name of the procedure.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the arguments of the procedure.
     * @return a list of variables, the arguments of the procedure.
     */
    public List<Variable> getArgs()
    {
        return args;
    }

    /**
     * Gets the body of the procedure.
     * @return a Statement, the body of the procedure.
     */
    public Statement getBody() {
        return run;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Procedure p)) return false;
        return name.equals(p.name);
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
