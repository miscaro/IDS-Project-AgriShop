package it.ids.mad.ProgettoIDS.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OsmService {

    private final WebClient webClient;

    public OsmService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org").build();
    }

    public double[] getCoordinates(String address) {
        try {
            String response = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("q", address)
                            .queryParam("format", "json")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(response);

            if (!jsonArray.isEmpty()) {
                JSONObject firstResult = (JSONObject) jsonArray.get(0);
                String lat = (String) firstResult.get("lat");
                String lon = (String) firstResult.get("lon");
                return new double[]{Double.parseDouble(lat), Double.parseDouble(lon)};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
