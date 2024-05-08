package type;

public enum Type
{
    INT, DOUBLE, CHAR;

    public static int sizeOf(Type t) {
        return switch(t)
        {
            case INT -> 4;
            case DOUBLE -> 8;
            case CHAR -> 1;
        };
    }
}
