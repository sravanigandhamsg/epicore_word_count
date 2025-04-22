import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Main {
    public static void main(String[] args) {

        String textFileUrl = "https://courses.cs.washington.edu/courses/cse390c/22sp/lectures/moby.txt";

        try {
            URL url = new URL(textFileUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("[\\s,\\.]+");

                for (String word : words) {

                    //Replace double quote & single quote
                    word = word.replaceAll("^[\"']+|[\"']+$", "");

                    if (!word.isEmpty()) {
                        System.out.println(word);
                    }
                }
            }

            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading from URL: " + e.getMessage());
        }
    }

}