package com.pdfqa.pdfqa.service;

import com.pdfqa.pdfqa.Question;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfReaderService {

    private static final Pattern QUESTION_PATTERN = Pattern.compile("QUESTION (\\d+)");
    private static final Pattern ANSWER_PATTERN = Pattern.compile("Correct Answer: ([A-F,]+)");
    private static final int KEY_LENGTH = 30;

    private final Map<String, Question> sharedMap = new HashMap<>();


    public void mapContent(String filePath) {

        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            Matcher questionMatcher = QUESTION_PATTERN.matcher(text);
            Matcher answerMatcher = ANSWER_PATTERN.matcher(text);

            while (questionMatcher.find()) {
                if (answerMatcher.find(questionMatcher.end())) {
                    String questionContent = text.substring(questionMatcher.start(), answerMatcher.start()).trim();
                    String correctAnswer = answerMatcher.group(1);
                    String key = generateKeyFromContent(questionContent);

                    // Ensure uniqueness in the map, add numeric suffix in case of duplicates
                    String uniqueKey = ensureUniqueKey(key, sharedMap);
                    sharedMap.put(uniqueKey, new Question(questionContent, correctAnswer));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateKeyFromContent(String content) {
        StringBuilder key = new StringBuilder();
        String[] words = content.split("\\s+");
        for (String word : words) {
            if (!word.isEmpty() && !word.matches("Q\\d+")) {  // Skip words that match the pattern Q followed by any number
                key.append(word.charAt(0));
            }
            if (key.length() == KEY_LENGTH) {
                break;
            }
        }
        // Check to ensure at least two characters are available to remove
        if (key.length() > 2) {
            return key.substring(2); // Remove the first two characters from the key (ex. "Q1", "Q12" etc.)
        }
        return key.toString();
    }

    private String ensureUniqueKey(String baseKey, Map<String, Question> map) {
        if (!map.containsKey(baseKey)) {
            return baseKey;
        }
        int suffix = 1;
        while (map.containsKey(baseKey + suffix)) {
            suffix++;
        }
        return baseKey + suffix;
    }

    public Question findMostSimilarQuestion(String inputKey) {
        String closestKey = null;
        int minDistance = Integer.MAX_VALUE;

        for (String key : sharedMap.keySet()) {
            int distance = calculateLevenshteinDistance(inputKey, key);
            if (distance < minDistance) {
                minDistance = distance;
                closestKey = key;
            }
        }

        return sharedMap.get(closestKey);
    }

    private int calculateLevenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + costOfSubstitution(a.charAt(i - 1), b.charAt(j - 1)), Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

}
