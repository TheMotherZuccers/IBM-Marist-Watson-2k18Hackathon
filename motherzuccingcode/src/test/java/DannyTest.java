//Find the name of method in line of code
//Find previous changes in past commits
import java.util.Scanner;
import java.io.*;

public class DannyTest{

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        String longest = "";
        String shortest = "";

        String fileName = input.nextLine();

        FileReader fReader;
        try {
            fReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fReader);
            String cursor; //
            String content = "";
            int lines = 0;
            int words = 0;
            int chars = 0;


            while((cursor = reader.readLine()) != null){
                // count lines
                lines += 1;
                content += cursor;

                // count words
                String []_words = cursor.split(" ");
                for( String w : _words)
                {
                    words++;
                }

            }
            chars = content.length();

            System.out.println("The filename is " + fileName);
            System.out.println(chars + " Characters,");
            System.out.println(words + " words and " + lines + " lines.");
            while((cursor = reader.readLine()) != null){
                // compare shortest and longest.
                int currentSize = cursor.length();

                if (currentSize > longest.length() || longest.equals("")) {
                    longest = cursor;
                } else if (currentSize < shortest.length() || longest.equals("")) {
                    shortest = cursor;
                }
                // count lines
                lines += 1;
                content += cursor;
                // count words
                String []_words = cursor.split(" ");
                for( String w : _words)
                {
                    words++;
                }
                System.out.println("Longest line has " + longest.length());
                System.out.println("Shortest line has " + shortest.length());
            }
        } catch (FileNotFoundException ex) {
            // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!");
        } catch (IOException ex) {
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("An error has occured: " + ex.getMessage());
        }

    }


}