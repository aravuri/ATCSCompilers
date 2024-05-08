package scanner;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import scanner.Token.TokenType;

/**
 * Scanner is a simple scanner for Compilers and Interpreters (2024) lab exercise 1
 *
 * @author Agastya Ravuri
 * @version 1.22.2024
 * <p>
 * Usage:
 * Use nextToken() to progressively get the next scanned token.
 */
public class Scanner
{
    private final BufferedReader in;
    private char currentChar;
    private boolean eof;

    /**
     * A set of all valid operators.
     */
    public static final Set<String> OPERATORS = Set.of(
            "=",
            "+",
            "-",
            "*",
            "/",
            "%",
            "(",
            ")",
            "<",
            ">",
            "//",
            "/*",
            "<=",
            ">=",
            "<>",
            ":=",
            ";",
            ",",
            "."
    );

    /**
     * Defines a decision tree for scanning an operator.
     */
    public static final Tree<String> OPERATOR_TREE = toScanTree(OPERATORS);

    /**
     * Scanner constructor for construction of a scanner that
     * uses an InputStream object for input.
     * Usage:
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     *
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }

    /**
     * Scanner constructor for constructing a scanner that
     * scans a given input string.  It sets the end-of-file flag and then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     *
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Gets the next char.
     *
     * @postcondition the next char is in currentChar. if there is no next char, eof=true.
     */
    private void getNextChar()
    {
        try
        {
            int c = in.read();
            if (c == -1 || c == '$')
            {
                eof = true;
                currentChar = '$';
                return;
            }
            currentChar = (char) c;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Gets the next char, safely.
     *
     * @throws ScanErrorException, when the expected character is not the current char.
     * @postcondition the next char is in currentChar. if there is no next char, eof=true.
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (expected != currentChar)
            throw new ScanErrorException(
                    "Illegal character; expected " + currentChar + " and got " + expected
            );

        getNextChar();
    }

    /**
     * Returns whether there are more tokens.
     *
     * @return true if there are more tokens, false otherwise.
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Gets the next token.
     *
     * @return the next token.
     */
    public Token nextToken() throws ScanErrorException
    {
        while (isWhitespace(currentChar))
        {
            eat(currentChar);
        }

        if (eof)
            return new Token(TokenType.EOF, "EOF");

        if (isOperator(currentChar))
        {
            Token token = scanOperator();
            if (token.getValue().equals("//"))
            {
                handleInlineComment();
                return nextToken();
            }
            else if (token.getValue().equals("/*"))
            {
                handleBlockComment();
                return nextToken();
            }
            return token;
        }
        else if (isDigit(currentChar))
        {
            return scanNumber();
        }
        else if (isLetter(currentChar))
        {
            return scanIdentifier();
        }

        throw new ScanErrorException("Invalid character: '" + currentChar + "'");
    }

    /**
     * Skips over an inline comment.
     *
     * @postcondition currentChar is the character after the inline comment.
     * @throws ScanErrorException if there is an illegal character.
     */
    private void handleInlineComment() throws ScanErrorException
    {
        do
        {
            eat(currentChar);
        }
        while (currentChar != '\n');
        eat(currentChar);
    }

    /**
     * Skips over a block comment during scanning.
     *
     * @postcondition currentChar is the character after the block comment.
     * @throws ScanErrorException if there is an illegal character.
     */
    private void handleBlockComment() throws ScanErrorException
    {
        do
        {
            do
            {
                eat(currentChar);

                if (currentChar == '/')
                {
                    eat(currentChar);
                    if (currentChar == '*') handleBlockComment();
                }

            }
            while (currentChar != '*');
            eat(currentChar);
        }
        while (currentChar != '/');
        eat(currentChar);
    }

    /**
     * Scans a number of the form [0-9]*.
     *
     * @postcondition currentChar is the character after the number.
     * @return A String, the scanned number.
     * @throws ScanErrorException if there is an illegal character.
     */
    private Token scanNumber() throws ScanErrorException
    {
        String ret = "";

        while (isDigit(currentChar))
        {
            ret += currentChar;
            eat(currentChar);
        }

        if (ret.length() == 0)
            throw new ScanErrorException("Invalid number");

        return new Token(TokenType.NUMBER, ret);
    }

    /**
     * Scans an identifier of the form [a-zA-Z][a-zA-z0-9]*.
     *
     * @postcondition currentChar is the character after the identifier.
     * @return a String, the scanned identifier.
     * @throws ScanErrorException if there is an illegal character.
     */
    private Token scanIdentifier() throws ScanErrorException
    {
        String ret = "";

        if (isLetter(currentChar))
        {
            ret += currentChar;
            eat(currentChar);
        }
        while (isLetter(currentChar) || isDigit(currentChar))
        {
            ret += currentChar;
            eat(currentChar);
        }

        if (ret.length() == 0)
            throw new ScanErrorException("Invalid identifier");

        return new Token(TokenType.IDENTIFIER, ret);
    }

    /**
     * Scans an operator (the set of operators is defined above)
     *
     * @postcondition currentChar is the character after the operator.
     * @return a String, the scanned operator.
     * @throws ScanErrorException if there is an illegal character.
     */
    private Token scanOperator() throws ScanErrorException
    {
        String ret = "";

        Tree<String> valid = OPERATOR_TREE;
        while (valid.contains(currentChar + ""))
        {
            ret += currentChar;
            valid = valid.get(currentChar + "");
            eat(currentChar);
        }

        if (!valid.contains(""))
        {
            throw new ScanErrorException("Invalid Operator");
        }

        return new Token(TokenType.OPERATOR, ret);
    }

    /**
     * Returns whether c is a digit [0-9]
     *
     * @param c the character
     * @return true if c is a digit, false otherwise.
     */
    public static boolean isDigit(char c)
    {
        return '0' <= c && c <= '9';
    }

    /**
     * Returns whether c is a letter [a-zA-Z]
     *
     * @param c the character
     * @return true if c is a letter, false otherwise.
     */
    public static boolean isLetter(char c)
    {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }

    /**
     * Returns whether c is character that starts an operator.
     *
     * @param c the character
     * @return true if c is an operator, false otherwise.
     */
    public static boolean isOperator(char c)
    {
        return OPERATOR_TREE.contains("" + c);
    }

    /**
     * Returns whether c is whitespace (space or tab or newline or carriage return)
     *
     * @param c the character
     * @return true if c is whitespace, false otherwise.
     */
    public static boolean isWhitespace(char c)
    {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }

    /**
     * Takes in a set of valid strings and returns a character decision tree
     *     to check if a given string is valid.
     * For example, if the valid strings were "<", "<=", ":=", and "+",
     *  it would output the following tree:
     *
     *  null: {
     *      "<": {
     *          "=": {
     *              "": {}
     *          },
     *          ""
     *      },
     *      ":": {
     *          "=": {
     *              "": {}
     *          }
     *      },
     *      "+": {
     *          "": {}
     *      }
     *  }
     *
     * , such that any DFS ending with an empty string is considered a valid operator.
     * The root node will have a null value.
     *
     * @param set the set of valid strings.
     * @return a character decision tree to check if a string is valid.
     */
    public static Tree<String> toScanTree(Set<String> set)
    {
        Tree<String> ret = new Tree<>(null, new HashSet<>());

        for (String s : set)
        {
            Tree<String> current = ret;
            for (char c : s.toCharArray())
            {
                if (current.contains(c + ""))
                {
                    current = current.get(c + "");
                }
                else
                {
                    Tree<String> toAdd = new Tree<>(c + "", new HashSet<>());
                    current.add(toAdd);
                    current = toAdd;
                }
            }
            current.add(Tree.of(""));
        }

        return ret;
    }
}
