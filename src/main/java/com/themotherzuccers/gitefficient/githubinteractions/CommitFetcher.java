package com.themotherzuccers.gitefficient.githubinteractions;

import com.jcabi.github.Github;
import com.jcabi.github.RtGithub;
import com.jcabi.http.response.JsonResponse;
import java.util.ArrayList;
import java.util.List;
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

  public static JSONObject jsonfromURL(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }

  public static void main(String[] args) {
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

            for (JSONObject commit : commits) {
              System.out.println(commit.getString("sha"));
              System.out.println(commit.getString("message"));
              System.out.println(commit.getString("url"));
              String url = commit.getString("url");
              JSONObject stat = jsonfromURL(url).getJSONObject("stats");
              System.out.println("additions: " + stat.getInt("additions"));
              System.out.println("subtractions: " + stat.getInt("deletions"));
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}