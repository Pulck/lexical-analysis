package lex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Analyze statements to extract vocabulary.
 *
 * @author Chen Liang
 */
public class LexicalAnalyzer {
    private String line;
    private WordArray wordArray = new WordArray();
    private Hashtable<String, Symbol> legalCharMap;

    /**
     * Constructs a new LexicalAnalyzer
     */
    public LexicalAnalyzer() {
        Symbol[] operatorSymbols = {Symbol.plus, Symbol.minus, Symbol.times, Symbol.slash,
                Symbol.eql, Symbol.neq, Symbol.lss, Symbol.leq, Symbol.gtr, Symbol.geq,
                 Symbol.becomes};

        legalCharMap = new Hashtable<String, Symbol>();
        for (int i = 0; i < operatorSymbols.length; i++)
            legalCharMap.put(Condition.OperatorSet.get(i), operatorSymbols[i]);

        legalCharMap.put("(", Symbol.lparen);
        legalCharMap.put(")", Symbol.rparen);
        legalCharMap.put("Identifier", Symbol.ident);
        legalCharMap.put("Number", Symbol.number);
    }

    /**
     * Access the analyzed word array
     *
     * @return The analyzed word array
     */
    public WordArray getWordArray() { return wordArray; }

    /**
     * Access the current input string
     *
     * @return  The current input string
     */
    public String getCurrentInputString() { return line; }

    /**
     * Get and store user input.
     */
    public void Input() {
        wordArray.clear();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            do {
                System.out.print("Please input program string, end with '.': ");
                line = in.readLine();
            } while (!line.endsWith("."));
        } catch(IOException e) {
            e.printStackTrace();
        }

        int pos;
        if ((pos = line.indexOf(".")) != line.length() - 1)
            line = line.substring(0, pos + 1);

    }

    private ArrayList<String> breakDown() {
        ArrayList<String> decomposedList = new ArrayList<String>();
        Scanner scanner = new Scanner(line);
        String strBlock;

        while (scanner.hasNext()) {
            strBlock = scanner.next();
            if (strBlock.endsWith("."))
                strBlock = strBlock.substring(0, strBlock.length() - 1);


            if (Condition.isKeyword(strBlock)) {
                decomposedList.add(strBlock);
            } else {
                boolean isEndsWithSemicolon = strBlock.endsWith(Condition.Semicolon);
                String[] splits = strBlock.split(Condition.Semicolon);

                for (int i = 0; i < splits.length; i++) {
                    decomposedList.add(splits[i]);
                    if (i != splits.length - 1)
                        decomposedList.add(Condition.Semicolon);
                }
                if (isEndsWithSemicolon)
                    decomposedList.add(Condition.Semicolon);
            }
        }
        decomposedList.trimToSize();
        scanner.close();
        return decomposedList;
    }

    private int generateToken(int count, int index, String substring,
                              String character, String identifiedKey, Condition condition) {
        StringBuilder container = new StringBuilder();
        int[] point;

        while (condition.computeCondition(character)) {
            container.append(character);
            index++;
            if (index < count) {
                point = new int[]{substring.codePointAt(index)};
                character = new String(point, 0, 1);
            } else {
                break;
            }
        }

        if (identifiedKey.equals("Symbol"))
            identifiedKey = container.toString();
        wordArray.addWord(container.toString(), legalCharMap.get(identifiedKey));
        index--;
        return index;
    }

    /**
     * Start analyzing the stored user input string
     *
     * @throws Exception Illegal character
     */
    public void startAnalysis() throws IOException {
        if (line.length() == 0)
            return;

        for (String item : breakDown()) {
            if (Condition.isKeyword(item)) {
                wordArray.addWord(item, Symbol.valueOf(item + "sym"));
            } else if (item.equals(Condition.Semicolon)) {
                wordArray.addWord(Condition.Semicolon, Symbol.semicolon);
            } else {
                int count = item.codePointCount(0, item.length());

                for (int i = 0; i < count; i++) {
                    int[] point = {item.codePointAt(i)};
                    String character = new String(point, 0, 1);

                    if (Condition.isLetter(character)) {
                        i = generateToken(count, i, item, character, "Identifier", Condition.toIdentifier());
                    } else if (Condition.isDigit(character)) {
                        i = generateToken(count, i, item, character, "Number", Condition.toNumber());
                    } else if (Condition.isOperator(character)) {
                        i = generateToken(count, i, item, character, "Symbol", Condition.toSymbol());
                    } else if (Condition.isParentheses(character)) {
                        wordArray.addWord(character, legalCharMap.get(character));
                    } else {
                        throw new IOException("Position " + line.indexOf(character) +
                                " occur the unexpected char \'" + character + "\'.");
                    }
                }
            }
        }
        wordArray.addWord(".", Symbol.period);
    }

    private enum Symbol {
        period(".",0),	plus("+",1), minus("-",2), 	times("*",3), slash("/",4),
        eql("=",5),	neq("<>",6), lss("<",7), leq("<=",8), gtr(">",9), geq(">=",10),
        lparen("(",11),	rparen(")",12),	semicolon(";",13),	becomes(":=",14),
        beginsym("begin",15), endsym("end",16),	ifsym("if",17),	thensym("then",18),
        whilesym("while",19), dosym("do",20), ident("IDENT",21), number("number",22);

        String name;
        int index;

        Symbol(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }

    private static class Word {
        String name;
        Symbol symbolType;

        Word(String name, Symbol symbolType) {
            this.name = name;
            this.symbolType = symbolType;
        }

        @Override
        public String toString() {
            return "("+ this.symbolType.index +","+this.name.trim()+")";
        }
    }

    /**
     * Store the word after the analysis.
     */
    public static class WordArray implements Iterable<String> {
        private ArrayList<Word> contents = new ArrayList<Word>();

        /**
         *
         * @param i The index of the word to access
         * @return The represent of the word
         */
        public String get(int i) {
            return contents.get(i).toString();
        }

        private void addWord(String strBlock, Symbol symbol) {
            Word word = new Word(strBlock, symbol);
            contents.add(word);
        }

        private void clear() {
            contents.clear();
        }

        @Override
        public Iterator<String> iterator() {
           return new Iterator<String>() {
                private int index = 0;

                @Override
                public boolean hasNext() { return index != contents.size(); }

                @Override
                public String next() {
                    Word element = contents.get(index);
                    index++;
                    return element.toString();
                }
            };
        }
    }

    private static class Condition {
        static final int IDENTIFIER_STATE = 0;
        static final int NUMBER_STATE = 1;
        static final int SYMBOL_STATE = 2;
        static final List<String> OperatorSet = Arrays.asList("+", "-", "*", "/", "=", "<>", "<", "<=", ">", ">=", ":=" ,":");
        static final List<String> Keywords = Arrays.asList("begin", "do", "end", "if", "then", "while");
        static final String Semicolon = ";";

        int state = 0;

        Condition(int state) { this.state = state; }

        static Condition toIdentifier() { return new Condition(IDENTIFIER_STATE); }
        static Condition toNumber() { return new Condition(NUMBER_STATE); }
        static Condition toSymbol() { return new Condition(SYMBOL_STATE); }

        static boolean isLetter(String character) { return character.compareToIgnoreCase("a") >= 0 &&
                    character.compareToIgnoreCase("z") <= 0; }
        static boolean isDigit(String character) { return character.compareTo("0") >= 0 &&
                character.compareTo("9") <= 0; }
        static boolean isOperator(String character) { return OperatorSet.contains(character); }
        static boolean isKeyword(String character) { return Keywords.contains(character); }
        static boolean isParentheses(String character) { return character.equals("(") || character.equals(")"); }

        boolean computeCondition(String character) {
            switch (state) {
                case IDENTIFIER_STATE:
                    return isLetter(character) || isDigit(character);
                case NUMBER_STATE:
                    return isDigit(character);
                case SYMBOL_STATE:
                    return isOperator(character);
                default:
                    return false;
            }
        }
    }

    public static void main(String[] args) {
        try {
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

            lexicalAnalyzer.Input();
            lexicalAnalyzer.startAnalysis();

            for (String item : lexicalAnalyzer.getWordArray()) {
                System.out.print(item);
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}