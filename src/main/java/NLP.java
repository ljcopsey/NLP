import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class NLP {
//    private List<String> replacements = new ArrayList<>();

    public static List<String> splitText() {
        InputStreamReader reader;
        BufferedReader br;
        List<Integer> potentials = new ArrayList<>();
        List<String> tokens = new ArrayList<>();

        try {
            reader = new InputStreamReader(new FileInputStream("/home/libby/Dev/AP/resources/Q4/warandpeaceTEST.txt"));
            br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    //Whitespace splitting
                    String[] result = line.split("—|\\s+" );
                    tokens.addAll(Arrays.asList(result));
                }
            }

            //Check for capitalisation: if caps, add to 'potentials' array, then add all tokens to result
            for (int i = 0; i < tokens.size(); i++) {
                String token = tokens.get(i);
                if (token.length() > 0 && Character.isUpperCase(token.charAt(0))) {
                    potentials.add(i);
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        potentials = removeStartOfSentence(tokens, potentials);

        //Assume potentials is now the true list of proper nouns
        String redacted = redactProperNouns(tokens, potentials);
        System.out.println(String.join(" ", tokens));
        System.out.println(redacted);
        return tokens;
    }

    public static List<Integer> removeStartOfSentence(List<String> tokens, List<Integer> potentials) {
        //Iterate through potentials and ignore start of sentence capitals
        List<Integer> properNouns = new ArrayList<>();
        for (Integer p : potentials) {
            if (p == 0) {
                properNouns.add(p);
                continue;
            }
            int x = p - 1;
            String previous = tokens.get(x);
            if (previous.length() > 0) {
                String lastChar = previous.substring(previous.length() - 1);
                try {
                    if (!lastChar.matches("[.!?\\-]")) {
                        properNouns.add(p);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("oops");
                }
            }
        }
        removeI(tokens, properNouns);
        return properNouns;
    }

    public static List<Integer> removeI(List<String> tokens, List<Integer> properNouns) {
        //Remove "I" from proper nouns list. Can be reimplemented
        properNouns.removeIf(p -> tokens.get(p).equals("I"));
        return properNouns;
    }

    public static String redactProperNouns(List<String> tokens, List<Integer> properNouns) {
        //Get tokens to redact via properNouns indices
        String tokenToRedact;
        List<String> redactedTokens = new ArrayList<>(tokens);
        for (Integer p : properNouns) {
            tokenToRedact = tokens.get(p);
            if (!tokenToRedact.endsWith("[.!?\\-]")) {
                String asteriskString = "*".repeat(tokenToRedact.length());
                redactedTokens.set(p, asteriskString);
            }
        }
        return String.join(" ", redactedTokens);
    }


    public static void main(String[] args) {
        try {
            splitText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//Need to reimplement replaceAll

