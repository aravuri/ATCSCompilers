package ast;

import emitter.Emitter;
import environment.Environment;
import type.Type;

import java.util.HashMap;

/**
 * Represents a binary operator expression within an ast.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class BinOp implements Expression
{

    /**
     * Gets the sum of two integer values.
     */
    public static final BinaryOperation ADD =
            new BinaryOperation(
                    a -> b -> (Integer) a + (Integer) b,
                    new HashMap<>(){{
                        put(Type.INT, "addu %1$s %2$s %3$s");
                    }},
                    Type.INT
            );

    /**
     * Gets the difference of two integer values.
     */
    public static final BinaryOperation SUB =
            new BinaryOperation(
                    a -> b -> (Integer) a - (Integer) b,
                    new HashMap<>(){{
                        put(Type.INT, "subu %1$s %2$s %3$s");
                    }},
                    Type.INT
            );


    /**
     * Gets the product of two integer values.
     */
    public static final BinaryOperation MUL =
            new BinaryOperation(
                    a -> b -> (Integer) a * (Integer) b,
                    new HashMap<>(){{
                        put(Type.INT, "mul %1$s %2$s %3$s");
                    }},
                    Type.INT
            );



    /**
     * Gets the quotient of two integer values.
     */
    public static final BinaryOperation DIV =
            new BinaryOperation(
                    a -> b -> (Integer) a / (Integer) b,
                    new HashMap<>(){{
                        put(Type.INT, "div %1$s %2$s %3$s");
                    }},
                    Type.INT
            );

    /**
     * Gets the remainder when one value is divided by another.
     */
    public static final BinaryOperation MOD =
            new BinaryOperation(
                    a -> b -> (Integer) a % (Integer) b,
                    new HashMap<>(){{
                        put(Type.INT,
                                "div %2$s %3$s\n" +
                                "mfhi %1$s");
                    }},
                    Type.INT
            );

    /**
     * Checks if two integer values are equal.
     */
    public static final BinaryOperation EQ =
            new BinaryOperation(
                    a -> b -> a == b,
                    new HashMap<>(){{
                        put(Type.INT, "seq %1$s %2$s %3$s");
                    }},
                    Type.INT
            );

    /**
     * Checks if two integer values are not equal.
     */
    public static final BinaryOperation NEQ =
            new BinaryOperation(
                    a -> b -> a != b,
                    new HashMap<>(){{
                        put(Type.INT, "sne %1$s %2$s %3$s");
                    }},
                    Type.INT
            );

    /**
     * Checks if an integer value is less than another.
     */
    public static final BinaryOperation LT =
            new BinaryOperation(
                    a -> b -> (Integer) a < (Integer) b,
                    new HashMap<>(){{
                        put(Type.INT, "slt %1$s %2$s %3$s");
                    }},
                    Type.INT
            );

    /**
     * Checks if one integer value is greater than another.
     */
    public static final BinaryOperation GT =
            new BinaryOperation(
                    a -> b -> (Integer) a > (Integer) b,
                    new HashMap<>(){{
                        put(Type.INT, "sgt %1$s %2$s %3$s");
                    }},
                    Type.INT
            );

    /**
     * Checks if one integer value is less than or equal to another.
     */
    public static final BinaryOperation LEQ =
            new BinaryOperation(
                    a -> b -> (Integer) a <= (Integer) b,
                    new HashMap<>(){{
                        put(Type.INT, "sle %1$s %2$s %3$s");
                    }},
                    Type.INT
            );

    /**
     * Checks if one integer value is greater than or equal to another.
     */
    public static final BinaryOperation GEQ =
            new BinaryOperation(
                    a -> b -> (Integer) a >= (Integer) b,
                    new HashMap<>(){{
                        put(Type.INT, "sge %1$s %2$s %3$s");
                    }},
                    Type.INT
            );

    /**
     * Gets the and of two boolean values.
     */
    public static final BinaryOperation AND =
            new BinaryOperation(
                    a -> b -> (Boolean) a && (Boolean) b,
                    new HashMap<>(){{
                        put(Type.INT, "and %1$s %2$s %3$s");
                    }},
                    Type.INT
            );

    /**
     * Gets the or of two boolean values.
     */
    public static final BinaryOperation OR =
            new BinaryOperation(
                    a -> b -> (Boolean) a || (Boolean) b,
                    new HashMap<>(){{
                        put(Type.INT, "or %1$s %2$s %3$s");
                    }},
                    Type.INT
            );

    private final Expression lhs;
    private final Expression rhs;
    private final BinaryOperation operation;

    /**
     * Constructor for a BinOp
     * @param lhs the left hand side
     * @param rhs the right hand side
     * @param operation the operation to apply.
     */
    public BinOp(Expression lhs, Expression rhs,
                 BinaryOperation operation)
    {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operation = operation;
    }

    @Override
    public Object eval(Environment env)
    {
        return operation.apply(lhs.eval(env), rhs.eval(env));
    }

    @Override
    public void compile(Emitter e)
    {
        e.emit("# BINOP START:");
        lhs.compile(e);
        e.emitPush("$v0");
        rhs.compile(e);
        e.emitPop("$t0");

        e.emit(operation.format(Type.INT, "$v0", "$t0", "$v0"));
        e.emit("# BINOP END:");
    }

    @Override
    public Type getType()
    {
        return operation.getReturnType();
    }
}
