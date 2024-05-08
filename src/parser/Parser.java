package parser;

import ast.*;
import scanner.ScanErrorException;
import scanner.Scanner;
import scanner.Token;
import scanner.Token.TokenType;
import type.Type;

import java.util.*;

/**
 * Parser is a simple LL(1) recursive descent parser (without backtracking)
 *                     for ATCS Compilers and Interpreters (2024)
 *
 * @author Agastya Ravuri
 * @version 3.4.2024
 * <p>
 * Usage:
 * Use parseProgram() to parse the program.
 */
public class Parser
{

    /**
     * Represents every binary operator, with precedence.
     */
    public static final Map<Integer, Map<String, BinaryOperation>> binaryOperators =
            new HashMap<>(){{
                put(0, new HashMap<>(){{
                    put("*", BinOp.MUL);
                    put("/", BinOp.DIV);
                    put("mod", BinOp.MOD);
                }});
                put(1, new HashMap<>(){{
                    put("+", BinOp.ADD);
                    put("-", BinOp.SUB);
                }});
                put(2, new HashMap<>(){{
                    put("=", BinOp.EQ);
                    put("<>", BinOp.NEQ);
                    put("<", BinOp.LT);
                    put(">", BinOp.GT);
                    put("<=", BinOp.LEQ);
                    put(">=", BinOp.GEQ);
                }});
                put(3, new HashMap<>(){{
                    put("&&", BinOp.AND);
                    put("||", BinOp.OR);
                }});
            }};

    /**
     * The maximum operator precedence value.
     */
    public static final int maxPrecedence = binaryOperators.keySet().stream().reduce(Integer::max).get();

    private final Scanner sc;

    private Token currentToken;

    /**
     * Constructs a parser.
     * @param sc the scanner to use.
     */
    public Parser(Scanner sc)
    {
        this.sc = sc;
        try
        {
            currentToken = sc.nextToken();
        }
        catch (ScanErrorException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Gets the next char, safely.
     *
     * @param expected the expected current token.
     * @throws IllegalArgumentException, when the current token is not the expected token.
     * @postcondition The next token is in currentToken.
     */
    private void eat(Token expected)
    {
        if (currentToken.equals(expected))
        {
            try
            {
                currentToken = sc.nextToken();
            }
            catch (ScanErrorException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            throw new IllegalArgumentException("Got " + currentToken + " but expected " + expected + ".");
        }
    }

    /**
     * Wraps a String into a Token before calling eat.
     * @param expected the expected next string.
     */
    private void eat(String expected)
    {
        eat(new Token(expected));
    }

    /**
     * Parses a number.
     * @return an Expression, the parsed number.
     */
    private Literal parseNumber()
    {
        if (currentToken.getType() != TokenType.NUMBER)
        {
            throw new IllegalArgumentException("Expected a number, but got " + currentToken);
        }
        int ret = Integer.parseInt(currentToken.getValue());
        eat(currentToken);
        return new Literal(ret, Type.INT);
    }

    /**
     * Parses a factor
     * @return an Expression, the parsed factor.
     */
    private Expression parseFactor()
    {
        return switch (currentToken.getType())
        {
            case OPERATOR ->
                switch (currentToken.getValue())
                {
                    case "(" -> {
                        eat("(");
                        Expression ret = parseExpr(maxPrecedence);
                        eat(")");
                        yield ret;
                    }
                    case "-" -> {
                        eat("-");
                        Expression ret = parseFactor();
                        yield new BinOp(new Literal(0, Type.INT), ret, BinOp.SUB);
                    }
                    default -> throw new IllegalArgumentException("Expected Factor, but was invalid.");
                };
            case NUMBER -> parseNumber();
            case IDENTIFIER -> {
                Expression ret = switch (currentToken.getValue())
                {
                    case "True" -> {
                        eat(currentToken);
                        yield new Literal(true, Type.INT);
                    }
                    case "False" -> {
                        eat(currentToken);
                        yield new Literal(false, Type.INT);
                    }
                    default -> {
                        String id = currentToken.getValue();
                        eat(currentToken);
                        if (currentToken.getValue().equals("("))
                        {
                            yield parseProcedureCall(id);
                        }
                        yield new Variable(id, Type.INT);
                    }
                };
                yield ret;
            }
            default -> throw new IllegalArgumentException("Expected Factor, but was invalid");
        };
    }


    /**
     * Parses an expression.
     * @param precedence the operator precedence currently being parsed.
     * @return an int, the evaluated parsed expression.
     */
    private Expression parseExpr(int precedence) {
        if (precedence == -1) {
            return parseFactor();
        }
        Expression ret = parseExpr(precedence - 1);
        while (binaryOperators.get(precedence).containsKey(currentToken.getValue()))
        {
            var operator = binaryOperators.get(precedence).get(currentToken.getValue());
            eat(currentToken);
            ret = new BinOp(ret, parseExpr(precedence - 1), operator);
        }
        return ret;
    }

    /**
     * Parses a variable.
     * @return a Variable, the variable parsed.
     */
    private Variable parseVariable() {
        Variable v = new Variable(currentToken.getValue(), Type.INT);
        eat(currentToken);
        return v;
    }

    /**
     * Parses a procedure.
     * @return a Procedure, the procedure parsed.
     */
    private Procedure parseProcedure() {
        String name = currentToken.getValue();
        eat(currentToken);
        eat("(");
        List<Variable> args = new ArrayList<>();
        boolean stop = currentToken.getValue().equals(")");
        while (!stop)
        {
            args.add(parseVariable());
            stop = currentToken.getValue().equals(")");
            if (!stop)
                eat(",");
        }
        eat(")");
        eat(";");
        Statement body = parseStatement();
        return new Procedure(name, args, body);
    }

    private ProcedureCall parseProcedureCall(String id)
    {
        eat("(");
        List<Expression> args = new ArrayList<>();
        boolean stop = currentToken.getValue().equals(")");
        while (!stop)
        {
            args.add(parseExpr(maxPrecedence));
            stop = currentToken.getValue().equals(")");
            if (!stop)
                eat(",");
        }
        eat(")");
        return new ProcedureCall(id, args);
    }

    /**
     * Parses a statement.
     * @return a Statement, the statement parsed.
     */
    private Statement parseStatement()
    {
        return switch (currentToken.getValue())
        {
            case "BEGIN" -> // TODO
            {
                eat("BEGIN");
                List<Statement> ret = new ArrayList<>();
                do
                {
                    ret.add(parseStatement());
                }
                while (!currentToken.getValue().equals("END"));
                eat("END");
                eat(";");
                yield new Block(ret);
            }
            case "PROCEDURE" ->
            {
                eat("PROCEDURE");
                Procedure p = parseProcedure();
                yield new ProcedureDeclaration(p);
            }
            case "WRITELN" ->
            {
                eat("WRITELN");
                eat("(");
                Expression e = parseExpr(maxPrecedence);
                eat(")");
                eat(";");
                yield new WriteLn(e);
            }
            case "READLN" ->
            {
                eat("READLN");
                eat("(");
                Variable v = parseVariable();
                eat(")");
                eat(";");
                yield new ReadLn(v);
            }
            case "IF" ->
            {
                eat("IF");
                Expression condition = parseExpr(maxPrecedence);
                eat("THEN");
                Statement trueBranch = parseStatement();
                if (currentToken.getValue().equals("ELSE"))
                {
                    eat("ELSE");
                    Statement falseBranch = parseStatement();
                    yield new If(condition, trueBranch, falseBranch);
                }
                yield new If(condition, trueBranch);
            }
            case "WHILE" ->
            {
                eat("WHILE");
                Expression condition = parseExpr(maxPrecedence);
                eat("DO");
                Statement body = parseStatement();
                yield new While(condition, body);
            }
            case "FOR" ->
            {
                eat("FOR");
                Variable loopVar = parseVariable();
                eat(":=");
                Expression from = parseExpr(maxPrecedence);
                eat("TO");
                Expression to = parseExpr(maxPrecedence);
                eat("DO");
                Statement body = parseStatement();
                yield new For(loopVar, from, to, body);
            }
            default ->
            {
                String id = currentToken.getValue();
                eat(currentToken);
                yield switch (currentToken.getValue()) {
                    case ":=" -> {
                        Variable lhs = new Variable(id, Type.INT);
                        eat(":=");
                        Expression rhs = parseExpr(maxPrecedence);
                        eat(";");
                        yield new Assignment(lhs, rhs);
                    }
                    case "(" ->
                    {
                        ProcedureCall ret = parseProcedureCall(id);
                        eat(";");
                        yield ret;
                    }
                    default -> throw new IllegalArgumentException("Illegal statement");
                };
            }
        };
    }

    /**
     * Parses the whole stream of tokens from the scanner.
     *
     * @return a Program, the program parsed.
     */
    public Program parseProgram()
    {
        List<ProcedureDeclaration> procedures = new ArrayList<>();

        while (sc.hasNext() && currentToken.getValue().equals("PROCEDURE"))
            procedures.add((ProcedureDeclaration) parseStatement());

        Statement run = parseStatement();

        return new Program(procedures, run);
    }

}
