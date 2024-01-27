package scanner;

/**
 * An enum, containing all possible token types.
 */
enum TokenType
{
    IDENTIFIER, NUMBER, OPERATOR, EOF
}


/**
 * Represents a token that will be scanned by a Scanner.
 *
 * @author Agastya Ravuri
 * @version 1.26.2024
 */
public class Token
{

    private final TokenType type;

    private final String value;

    /**
     * Contructs a Token
     * @param type the type of the token
     * @param value the value of the token
     */
    public Token(TokenType type, String value)
    {
        this.type = type;
        this.value = value;
    }

    /**
     * Gets the type of the token
     * @return the type of the token
     */
    public TokenType getType()
    {
        return type;
    }

    /**
     * Gets the value of the token
     * @return the value of the token
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Converts a token to a string.
     * @return a string, of the token
     */
    @Override
    public String toString()
    {
        return "{" + value + ", type: " + type + "}";
    }
}


