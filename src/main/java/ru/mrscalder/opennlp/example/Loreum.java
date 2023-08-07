package ru.mrscalder.opennlp.example;

import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Loreum {
    public static void main(String[] args) throws IOException {
        new Loreum().execute();
    }

    public void execute() throws IOException {
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

        String[] sentences = getSentences(sb.toString());
        for (String sentence : sentences) {
            System.out.println("Sentence: " + sentence);
            String[] tokens = getTokens(sentence);
            String[] posTags = getPosTag(tokens);
            String[] lemmas = getLemmas(tokens, posTags);

            for (int i = 0; i < tokens.length; i++) {
                System.out.printf("token: %s tag: %s lemma: %s%n", tokens[i], posTags[i], lemmas[i]);
            }
        }
    }

    private String[] getLemmas(String[] tokens, String[] posTags) throws IOException {
        try (FileInputStream fis = new FileInputStream("model/en-lemmatizer.bin")) {
            LemmatizerModel model = new LemmatizerModel(fis);
            return new LemmatizerME(model).lemmatize(tokens, posTags);
        }
    }

    private String[] getPosTag(String[] tokens) throws IOException {
        try (FileInputStream fis = new FileInputStream("model/en-tagger.bin")) {
            POSModel posModel = new POSModel(fis);
            return new POSTaggerME(posModel).tag(tokens);
        }
    }

    private String[] getTokens(String sentence) throws IOException {
        try (FileInputStream fis = new FileInputStream("model/en-token.bin")) {
            TokenizerModel tokenizerModel = new TokenizerModel(fis);
            return new TokenizerME(tokenizerModel).tokenize(sentence);
        }
    }

    private String[] getSentences(String loreumText) throws IOException {
        try (FileInputStream fis = new FileInputStream("model/en-sent.bin")) {
            SentenceModel model = new SentenceModel(fis);
            return new SentenceDetectorME(model).sentDetect(loreumText);
        }
    }
}
