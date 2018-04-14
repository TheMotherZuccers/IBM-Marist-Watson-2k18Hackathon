package com.themotherzuccers.gitefficient.githubinteractions;

public class CommitFetcher {
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
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
