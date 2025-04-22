import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Main {
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            // Prepositions
            "in", "on", "at", "by", "to", "of", "for", "with", "about", "against", "between", "into",
            "through", "during", "before", "after", "above", "below", "from", "up", "down", "off", "over", "under",

            // Pronouns
            "he", "she", "it", "they", "we", "you", "i", "me", "him", "her", "them", "us",
            "my", "your", "his", "their", "our", "this", "that", "these", "those",

            // Conjunctions
            "and", "or", "but", "if", "because", "as", "until", "while", "so",

            // Articles
            "the", "a", "an",

            // Modal verbs
            "is", "are", "was", "were", "be", "been", "being", "am",
            "do", "does", "did", "have", "has", "had",
            "will", "would", "can", "could", "should", "shall", "may", "might", "must",

            // Additional filler/weak words
            "not", "very", "just", "too", "also",
            "all", "some", "many", "most", "much", "few", "each", "every", "any", "either", "neither"
    ));

    public static void main(String[] args) {

        String textFileUrl = "https://courses.cs.washington.edu/courses/cse390c/22sp/lectures/moby.txt";
        Map<String, Integer> wordsCount = new HashMap<>();
        int totalWords=0;

        try {
            URL url = new URL(textFileUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("[\\s,\\.]+");

                for (String word : words) {

                    //Replace double quote & single quote
                    word = word.replaceAll("^[^a-zA-Z]+|[^a-zA-Z]+$", "");

                    word = word.replaceAll("'s$", "").toLowerCase();

                    if (!word.isEmpty() && !STOP_WORDS.contains(word)  && !word.matches("\\d+")) {
                        wordsCount.put(word, wordsCount.getOrDefault(word, 0)+1);
                        totalWords++;
                    }
                }
            }

            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading from URL: " + e.getMessage());
        }

        System.out.println(wordsCount.toString());
        System.out.println("Total words" + totalWords);

        System.out.println("\nTop 5 most frequent words:");
        wordsCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));

        // Alphabetically sorted unique words (top 50)
        System.out.println("\nTop 50 unique words (alphabetically):");
        wordsCount.keySet().stream()
                .sorted()
                .limit(50)
                .forEach(System.out::println);
    }

}