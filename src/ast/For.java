package ast;

import type.Type;

/**
 * Represents a for loop statement.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class For extends Block
{

    /**
     * Constructor for a for loop.
     * @param loopVar the loop variable.
     * @param from the value to start at.
     * @param to the value to go to.
     * @param body the body of the loop.
     */
    public For(Variable loopVar, Expression from, Expression to, Statement body)
    {
        super(
                new Assignment(loopVar, from),
                new While(new BinOp(loopVar, to, BinOp.LEQ),
                        new Block(
                                body,
                                new Assignment(
                                        loopVar,
                                        new BinOp(loopVar, new Literal(1, Type.INT), BinOp.ADD)
                                )
                        )
                )
        );
    }
}
