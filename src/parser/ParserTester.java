package parser;

import ast.Program;
import emitter.Emitter;
import scanner.Scanner;

import java.io.*;

/**
 * Tests the Parser class.
 *
 * @author Agastya Ravuri
 * @version 3.22.2024
 */
public class ParserTester
{

    public static final String file = "compilertest.txt";

    /**
     * Tests parser.
     * @param args arguments from the command line
     * @throws FileNotFoundException when the file is not found
     */
    public static void main(String[] args) throws IOException
    {
        Scanner sc = new Scanner(new FileInputStream(file));
        Parser p = new Parser(sc);

        System.out.println(file + ": ");

        Emitter e = new Emitter("compiled.txt");
        Program par = p.parseProgram();
        par.compile(e);
        par.run();
    }
}
