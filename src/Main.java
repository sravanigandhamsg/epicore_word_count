import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class Main {

    // Set of stop words to filter out common, non-useful words
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

    // Pattern to remove non-alphabetical characters at the beginning or end of words
    private static final Pattern nonAlphaPattern = Pattern.compile("^[^a-zA-Z]+|[^a-zA-Z]+$"); // Remove trailing non-alphabetic

    // Pattern to remove possessive 's' from words like "whale's" -> "whale"
    private static final Pattern possessivePattern = Pattern.compile("'s$"); // Remove possessive 's

    public static void main(String[] args) {

        // Start the timer to measure execution time
        long startTime = System.nanoTime();

        String textFileUrl = "https://courses.cs.washington.edu/courses/cse390c/22sp/lectures/moby.txt";
        // Map to store frequency of each word
        Map<String, Integer> wordsCount = new HashMap<>();
        // Variable to count total words
        int totalWords=0;

        try {
            // Open a stream to read from the URL
            URL url = new URL(textFileUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = reader.readLine()) != null) {

                // Split line into words based on spaces and punctuation (.,)
                String[] words = line.split("[\\s,.]+");

                for (String word : words) {

                    // Clean word by removing non-alphabetic characters
                    word = nonAlphaPattern.matcher(word).replaceAll("");
                    // Remove possessive 's
                    word = possessivePattern.matcher(word).replaceAll("");
                    // Convert to lowercase for uniformity
                    word = word.toLowerCase();

                    // Only count the word if it's not empty, not a stop word, and not a number
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

        // Print the total word count (after filtering)
        System.out.println("Total word count (excluding the filtered words): " + totalWords);

        // Runnable task for top 5 frequent words
        Runnable topFrequentWordsTask = new Runnable() {
            @Override
            public void run() {
                System.out.println("\nTop 5 most frequent words with counts (excluding the filtered words):");
                wordsCount.entrySet().stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .limit(5)
                        .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));
            }
        };

        // Runnable task for top 50 alphabetical words
        Runnable topAlphabeticalWordsTask = new Runnable() {
            @Override
            public void run() {
                System.out.println("\nTop 50 alphabetically sorted list of all unique words (excluding the filtered words):");
                wordsCount.keySet().stream()
                        .sorted()
                        .limit(50)
                        .forEach(System.out::println);
            }
        };

        // Create threads for each task
        Thread thread1 = new Thread(topFrequentWordsTask);
        Thread thread2 = new Thread(topAlphabeticalWordsTask);

        // Start both threads
        thread1.start();
        thread2.start();

        // Wait for both threads to finish before calculating execution time
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        // End time after execution
        long endTime = System.nanoTime();

        // Calculate and print elapsed time
        long duration = endTime - startTime;

        // Optionally, convert to milliseconds if you want more readable output
        System.out.println("\nExecution time:" + String.format("%.3f", (double)duration / 1_000_000_000) + " seconds");
    }

}