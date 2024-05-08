package scanner;

import java.io.*;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Tests the Scanner class.
 *
 * @author Agastya Ravuri
 * @version 1.26.2024
 */
public class ScannerTester
{

    /**
     * Tests scanner
     * @param args arguments from the command line
     * @throws FileNotFoundException when the file is not found
     * @throws ScanErrorException when there is a scanning error
     */
    public static void main(String[] args) throws IOException, ScanErrorException
    {
        Scanner sc = new Scanner(new FileInputStream("ScannerTest.txt"));

        System.out.println("ScannerTest.txt:");
        while (sc.hasNext())
        {
            Token token = sc.nextToken();
            System.out.println(token);
        }
    }
}
