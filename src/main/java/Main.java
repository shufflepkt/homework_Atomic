import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static final int NUMBER_OF_WORDS = 100_000;
    public static final int SHORTEST_WORD = 3;
    public static final int MAX_WORD_INCREMENT = 3;

    public static AtomicInteger counterWordsW3Ch = new AtomicInteger(0);
    public static AtomicInteger counterWordsW4Ch = new AtomicInteger(0);
    public static AtomicInteger counterWordsW5Ch = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[NUMBER_OF_WORDS];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", SHORTEST_WORD + random.nextInt(MAX_WORD_INCREMENT));
        }

        Thread palindromThread = new Thread(() -> {
            for (String text : texts) {
                if (checkPalindrom(text)) {
                    counterIncrement(text);
                }
            }
        });

        Thread sameCharThread = new Thread(() -> {
            for (String text : texts) {
                if (checkSameChar(text)) {
                    counterIncrement(text);
                }
            }
        });

        Thread inOrderThread = new Thread(() -> {
            for (String text : texts) {
                if (checkInOrder(text)) {
                    counterIncrement(text);
                }
            }
        });

        palindromThread.start();
        sameCharThread.start();
        inOrderThread.start();

        palindromThread.join();
        sameCharThread.join();
        inOrderThread.join();

        System.out.println("Красивых слов с длиной 3: " + counterWordsW3Ch + " шт\n" +
                "Красивых слов с длиной 4: " + counterWordsW4Ch + " шт\n" +
                "Красивых слов с длиной 5: " + counterWordsW5Ch + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean checkPalindrom(String text) {
        StringBuilder paliText = new StringBuilder();
        for (int i = text.length(); i > 0; i--) {
            paliText.append(text.charAt(i - 1));
        }
        return paliText.toString().equals(text);
    }

    public static boolean checkSameChar(String text) {
        int frequency = (int) text.chars().filter(ch -> ch == text.charAt(0)).count();
        return text.length() == frequency;
    }

    public static boolean checkInOrder(String text) {
        char[] chars = text.toCharArray();
        Arrays.sort(chars);
        String orderedText = new String(chars);
        return text.equals(orderedText);
    }

    public static void counterIncrement(String text) {
        switch (text.length()) {
            case 3 -> counterWordsW3Ch.incrementAndGet();
            case 4 -> counterWordsW4Ch.incrementAndGet();
            case 5 -> counterWordsW5Ch.incrementAndGet();
        }
    }
}