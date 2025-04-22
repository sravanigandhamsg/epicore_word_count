import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Main {
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "in", "on", "at", "by", "to", "of", "for", "with", "about", "against", "between", "into",
            "through", "during", "before", "after", "above", "below", "from", "up", "down", "off", "over", "under",
            "he", "she", "it", "they", "we", "you", "i", "me", "him", "her", "them", "us", "my", "your", "his", "their", "our",
            "and", "or", "but", "if", "because", "as", "until", "while",
            "the", "a", "an",
            "is", "are", "was", "were", "be", "been", "being", "am", "do", "does", "did", "have", "has", "had", "will", "would", "can", "could", "should", "shall", "may", "might", "must"
    ));

    public static void main(String[] args) {

        String textFileUrl = "https://courses.cs.washington.edu/courses/cse390c/22sp/lectures/moby.txt";
        Map<String, Integer> wordsCount = new HashMap<>();

        try {
            URL url = new URL(textFileUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("[\\s,\\.]+");

                for (String word : words) {

                    //Replace double quote & single quote
                    word = word.replaceAll("^[\"']+|[\"']+$", "");

                    word = word.replaceAll("'s$", "").toLowerCase();

                    if (!word.isEmpty() && !STOP_WORDS.contains(word)) {
                        wordsCount.put(word, wordsCount.getOrDefault(word, 0)+1);
                    }
                }
            }

            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading from URL: " + e.getMessage());
        }

        System.out.println(wordsCount.toString());

    }

}