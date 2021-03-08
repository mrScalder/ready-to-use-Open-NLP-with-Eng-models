package ru.mrscalder.opennlp.examples.loreum;

import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;

public class Loreum {
    public static void main(String[] args) throws IOException {
        new Loreum().execute();
    }

    public void execute() throws IOException {
        String loreumText = new String(getClass().getResourceAsStream("/loreum.txt").readAllBytes(), "utf-8");

        String[] sentences = getSentences(loreumText);
        for (String sentence : sentences) {
            System.out.println("Sentence: " + sentence);
            String[] tokens = getTokens(sentence);
            String[] posTags = getPosTag(tokens);
            String[] lemmas = getLemmas(tokens, posTags);

            for (int i = 0; i < tokens.length; i++) {
                System.out.println(String.format("token: %s tag: %s lemma: %s", tokens[i], posTags[i], lemmas[i]));
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
