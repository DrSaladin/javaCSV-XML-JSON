import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import employee.Employee;
import org.json.XML;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;


public class Main {

  public static void main(String[] args) {
    String sourceCsvFileName = "data.csv";
    String resultedCsvToJsonFileName = "dataFromCsv.json";
    String sourceXmlFileName = "data.xml";
    String resultedXmlToJsonFileName = "dataFromXml.json";

    parseCSVToJSON(sourceCsvFileName, resultedCsvToJsonFileName);
    parseXMLToJSON(sourceXmlFileName, resultedXmlToJsonFileName);
  }

  public static void parseCSVToJSON(String SourceFileName, String resultedFileName) {
    Pattern pattern = Pattern.compile(",");

    try (BufferedReader br = new BufferedReader(new FileReader("./src/main/java/"+SourceFileName))) {
      List<Employee> employees = br.lines()
        .map(line -> {
          String[] x = pattern.split(line);
          return new Employee(Integer.parseInt(x[0]), x[1], x[2], x[3], Integer.parseInt(x[4]));
        })
        .toList();

      try(FileWriter file = new FileWriter("./src/main/java/" + resultedFileName)) {
        JsonElement element = new Gson().toJsonTree(employees, new TypeToken<List<Employee>>() {}.getType());

        if (!element.isJsonArray() ) {
          throw new Exception();
        }
        file.write(String.valueOf(element.getAsJsonArray()));
        file.flush();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void parseXMLToJSON(String sourceFileName, String resultedFileName)  {
    String xml;
    try {
      xml = Files.readString(Path.of("./src/main/java/" + sourceFileName));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e);
    }

    try (FileWriter file = new FileWriter("./src/main/java/"+ resultedFileName)) {

        file.write(XML.toJSONObject(xml).toString());
        file.flush();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
  }
}
