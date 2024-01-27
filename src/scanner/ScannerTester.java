package scanner;

import java.io.*;

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
    public static void main(String[] args) throws FileNotFoundException, ScanErrorException
    {
        Scanner sc = new Scanner(new FileInputStream("scannerTestAdvanced.txt"));

        while (sc.hasNext())
        {
            System.out.println(sc.nextToken());
        }

    }
}
