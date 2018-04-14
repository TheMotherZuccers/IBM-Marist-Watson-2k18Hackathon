package com.themotherzuccers.gitefficient.watsonintegration;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import io.github.cdimascio.dotenv.Dotenv;

public class ToneAnalyzing {

    private ToneAnalyzer toneAnalyzer;

    public ToneAnalyzing() {
      Dotenv dotenv = Dotenv.load();

      toneAnalyzer = new ToneAnalyzer(
          "2018-4-13",
          dotenv.get("WATSON_USERNAME"),
          dotenv.get("WATSON_PASSWORD"));
    }

    public ToneAnalysis analyzetone(String message) {
      ToneOptions toneOptions = new ToneOptions.Builder().text(message).build();
      return toneAnalyzer.tone(toneOptions).execute();
    }

  public static void main(String[] args) {
    ToneAnalyzing toneAnalyzing = new ToneAnalyzing();

    String text = "Damion?! More like LAMEion! haha yeet gotem!";
    ToneAnalysis tone = toneAnalyzing.analyzetone(text);
    System.out.println(tone);

  }

}
