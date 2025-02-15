package org.example.DataLists.DataFiles;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNot.not;

public class DataRetriever {

   // @Test
    void GenerateTxtFile() {
       // scriptRetrievingData("type", 2, "cardTypes");
       // scriptRetrievingData("colors", 2, "cardColors");
        scriptRetrievingData("layout", 20, "layout");
    }

    void scriptRetrievingData(String dataType, int maxPages, String fileName) {
        Set<String> uniqueData = new HashSet<>();
        int page = 1;
        int pageSize = 100;

        while (true) {
            if(page == maxPages)
                break;
            Response response = given()
                    .queryParam("page", page)
                    .queryParam("pageSize", pageSize)
                    .when()
                    .get("https://api.magicthegathering.io/v1/cards")
                    .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("cards", not(empty()))
                    .extract()
                    .response();



            if(dataType.equals("type")) {
                List<String> dataList = response.jsonPath().getList("cards." + dataType);

                if (dataList.isEmpty()) {
                    break;
                }

                for (String data : dataList) {
                    System.out.println(data);

                    // Split and process the data based on its format
                    String[] splitData = data.split(" — ");
                    for (String split : splitData) {
                        // Further split by spaces to handle multi-word values
                        String[] words = split.split(" ");
                        for (String word : words) {
                            uniqueData.add(word.trim());
                        }
                    }
                }
                if (dataList.size() < pageSize) {
                    break; // End of data
                }
            }
            else {
                ArrayList<String> data = response.jsonPath().get("cards." + dataType);
                System.out.println(data);
                for (String word : data){
                    uniqueData.add(word.trim());
                }
            }


            page++;
        }
        String filePath = String.format("src/test/java/org/example/DataLists/DataFiles/%s.txt", fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String data : uniqueData) {
                writer.write(data);
                writer.newLine();
            }
            System.out.println("Data has been saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
