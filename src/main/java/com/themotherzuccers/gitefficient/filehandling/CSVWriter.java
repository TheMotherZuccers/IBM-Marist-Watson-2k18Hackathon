package com.themotherzuccers.gitefficient.filehandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class CSVWriter {

  private DateFormat csvDateFormat;
  private ArrayList<String> columns;
  private PrintWriter csvWriter;
  private StringBuilder csvBuilder;

  public CSVWriter(String... columns) {
    csvDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    try {
      csvWriter = new PrintWriter(
          new File("csv_output/" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss'.csv'")
              .format(new Date())));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    csvBuilder = new StringBuilder();

    this.columns = new ArrayList<String>();
    this.columns.addAll(Arrays.asList(columns));

    for (String columnHeader : columns) {
      csvBuilder.append(columnHeader).append(",");
    }
    csvBuilder.replace(csvBuilder.length() - 1, csvBuilder.length(), "\n");
  }

  public void addValues(String... values) {
    if (values.length != columns.size()) {
      throw new IllegalArgumentException(
          "A different amount of values were given than there are columns");
    }

    for (String value : values) {
      // No newlines in CSV where they arne't allowed
      value = value.replaceAll("[\n]", "");
      csvBuilder.append(value).append(",");
    }

    csvBuilder.replace(csvBuilder.length() - 1, csvBuilder.length(), "\n");
  }

  public String formatDateForCSV(Date date) {
    return csvDateFormat.format(date);
  }

  public void writeFile() {
    csvWriter.write(csvBuilder.toString());
    csvWriter.close();
  }

}
