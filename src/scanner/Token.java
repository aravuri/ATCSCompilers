package scanner;

/**
 * An enum, containing all possible token types.
 */

import java.util.Objects;

/**
 * Represents a token that will be scanned by a Scanner.
 *
 * @author Agastya Ravuri
 * @version 1.26.2024
 */
public class Token
{
    public enum TokenType
    {
        IDENTIFIER, NUMBER, OPERATOR, EOF
    }

    private final TokenType type;

    private final String value;

    /**
     * Contructs a token
     * @param type the type of the token
     * @param value the value of the token
     */
    public Token(TokenType type, String value)
    {
        this.type = type;
        this.value = value;
    }

    /**
     * Constructs an unassociated token
     * @param value the value of the token
     */
    public Token(String value)
    {
        this.type = null;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return (type == null || token.type == null || type == token.type) && Objects.equals(value, token.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, value);
    }
}


