import java.io.*;

public class StringDifference {
    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//Creates BufferedReader object

        //Prompts user for strings
        System.out.println("Enter first string");
        String a = br.readLine();
        System.out.println("Enter second string");
        String b = br.readLine();

        //Checks to see whether strings are equal
        if (a.equalsIgnoreCase(b)){
            System.out.println("You done did good!");
        }
        else{
            System.out.println("You done did not so very good.");
        }
        //Method for comparing lines of code
    }
}
