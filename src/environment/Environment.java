package environment;

import ast.Procedure;
import ast.Statement;
import ast.Variable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an environment in which to run a program.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class Environment
{

    private final Environment parent;
    private final Map<Variable, Object> varTable;
    private final Map<String, Procedure> procedureTable;

    /**
     * Constructs an environment.
     *
     * @param parent the parent of this environment, which will be searched
     *               when a variable is not found in the current scope.
     */
    public Environment(Environment parent)
    {
        this.parent = parent;
        this.varTable = new HashMap<>();
        this.procedureTable = new HashMap<>();
    }

    /**
     * Constructs an environment.
     */
    public Environment()
    {
        this(null);
    }

    /**
     * Sets the value of a variable; searches parent environments if necessary.
     * @param var the variable to set the value of.
     * @param value the value to set the variable to.
     */
    public void setVariable(Variable var, Object value)
    {
        Environment e = this;
        while (e != null)
        {
            if (e.varTable.containsKey(var))
            {
                e.varTable.put(var, value);
                return;
            }
            e = e.parent;
        }
        varTable.put(var, value);
    }

    /**
     * Sets the value of a variable in the current environment.
     * @param var the variable to set the value of.
     * @param value the value to set the variable to.
     */
    public void declareVariable(Variable var, Object value)
    {
        varTable.put(var, value);
    }

    /**
     * Gets the value of a variable; searches parent environments if necessary.
     * @param var the variable.
     * @return the value of the variable.
     */
    public Object getVariable(Variable var)
    {
        if (varTable.containsKey(var))
            return varTable.get(var);
        if (parent == null)
            return null;
        return parent.getVariable(var);
    }

    /**
     * Adds a procedure to the procedure table.
     * @param name The name of the procedure.
     * @param run The procedure to add.
     */
    public void setProcedure(String name, Procedure run)
    {
        procedureTable.put(name, run);
    }

    /**
     * Gets a procedure from the procedure table.
     * @param name the name of the procedure.
     * @return a Procedure object, representing the procedure.
     */
    public Procedure getProcedure(String name)
    {
        if (procedureTable.containsKey(name))
            return procedureTable.get(name);
        if (parent == null)
            return null;
        return parent.getProcedure(name);
    }
}
