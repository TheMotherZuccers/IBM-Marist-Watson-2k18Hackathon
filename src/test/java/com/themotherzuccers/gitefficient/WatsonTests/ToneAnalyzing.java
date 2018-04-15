package com.themotherzuccers.gitefficient.WatsonTests;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import io.github.cdimascio.dotenv.Dotenv;

public class ToneAnalyzing {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        ToneAnalyzer toneAnalyzer = new ToneAnalyzer(
                "2018-4-13",
                dotenv.get("WATSON_USERNAME"),
                dotenv.get("WATSON_PASSWORD"));

        String text = "Hello friend";

        ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
        ToneAnalysis tone = toneAnalyzer.tone(toneOptions).execute();
        System.out.println(tone);

    }

}
