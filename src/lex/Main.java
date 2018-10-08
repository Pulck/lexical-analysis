package lex;

public class Main {
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();


        try {
            lexicalAnalyzer.Input();
            lexicalAnalyzer.startAnalysis();
            LexicalAnalyzer.WordArray wordArray = lexicalAnalyzer.getWordArray();

            for (String item : wordArray) {
                System.out.print(item);
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
