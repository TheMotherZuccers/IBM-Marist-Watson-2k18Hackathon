package com.themotherzuccers.gitefficient.githubinteractions;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.DocumentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
import com.jcabi.github.Github;
import com.jcabi.github.RtGithub;
import com.jcabi.http.response.JsonResponse;
import com.themotherzuccers.gitefficient.filehandling.CSVWriter;
import com.themotherzuccers.gitefficient.watsonintegration.ToneAnalyzing;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommitFetcher {

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  private static ArrayList<JSONObject> readJsonArrayFromUrl(String url)
      throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      // Remove the [ and ] at the start and end of the response
      JSONArray jsonArray = new JSONArray(jsonText);

      ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
      for (int i = 0; i < jsonArray.length(); ++i) {
        jsonObjects.add(jsonArray.getJSONObject(i));
      }
      return jsonObjects;
    } finally {
      is.close();
    }
  }

  public static void main(String[] args) {
    CSVWriter csvWriter = new CSVWriter("Date", "Hour", "Message", "Anger", "Fear", "Joy",
        "Sadness", "Analytical", "Confident", "Tentative");
    ToneAnalyzing toneAnalyzing = new ToneAnalyzing();

    try {
      final Github github = new RtGithub();
      final JsonResponse resp = github.entry()
          .uri().path("/search/users")
          .queryParam("q", "williamkluge").queryParam("type", "Users").back()
          .fetch()
          .as(JsonResponse.class);
      final List<JsonObject> items = resp.json().readObject()
          .getJsonArray("items")
          .getValuesAs(JsonObject.class);
      for (final JsonObject item : items) {
        System.out.println(item.toString());
        String eventsUrl = item.getString("events_url");
        eventsUrl = eventsUrl.replace("{/privacy}", "");
        System.out.println("Events url: " + eventsUrl);
        ArrayList<JSONObject> jsonObjects = readJsonArrayFromUrl(eventsUrl);

        for (JSONObject jsonObject : jsonObjects) {
//          System.out.println(jsonObject.toString());
          String eventType = jsonObject.getString("type");

          if (eventType.equals("PushEvent")) {
            JSONObject payload = jsonObject.getJSONObject("payload");
            JSONArray commitArray = payload.getJSONArray("commits");
            ArrayList<JSONObject> commits = new ArrayList<JSONObject>();
            for (int i = 0; i < commitArray.length(); ++i) {
              commits.add(commitArray.getJSONObject(i));
            }

            String dateStr = jsonObject.getString("created_at");
            //2018-04-14T14:19:34Z Gotta remove the bad characters
            dateStr = dateStr.replace("T", " ");
            dateStr = dateStr.replace("Z", "");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date createdAt = dateFormat.parse(dateStr);
            System.out.println(createdAt.toString());

            for (JSONObject commit : commits) {
              System.out.println("SHA: " + commit.getString("sha"));
              String message = commit.getString("message");
              System.out.println("Message: " + message);
              DocumentAnalysis toneAnalysis = toneAnalyzing.analyzetone(message).getDocumentTone();
              Double angerScore = 0.0;
              Double fearScore = 0.0;
              Double joyScore = 0.0;
              Double sadnessScore = 0.0;
              Double analyticalScore = 0.0;
              Double confidentScore = 0.0;
              Double tentativeScore = 0.0; // Yeah, I know this is bad, just getting it working
              for (ToneScore toneScore : toneAnalysis.getTones()) {
                String toneId = toneScore.getToneId();
                if (toneId.equals("anger")) {
                  angerScore = toneScore.getScore();
                } else if (toneId.equals("fear")) {
                  fearScore = toneScore.getScore();
                } else if (toneId.equals("joy")) {
                  joyScore = toneScore.getScore();
                } else if (toneId.equals("sadness")) {
                  sadnessScore = toneScore.getScore();
                } else if (toneId.equals("analytical")) {
                  analyticalScore = toneScore.getScore();
                } else if (toneId.equals("confident")) {
                  confidentScore = toneScore.getScore();
                } else if (toneId.equals("tentative")) {
                  tentativeScore = toneScore.getScore();
                }
              }
              csvWriter.addValues(csvWriter.formatDateForCSV(createdAt),
                  String.valueOf(createdAt.getHours()), message,
                  String.valueOf(angerScore), String.valueOf(fearScore), String.valueOf(joyScore),
                  String.valueOf(sadnessScore), String.valueOf(analyticalScore),
                  String.valueOf(confidentScore), String.valueOf(tentativeScore));
            }
          }
        }
        csvWriter.writeFile();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
