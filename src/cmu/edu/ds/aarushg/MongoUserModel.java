package cmu.edu.ds.aarushg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MongoUserModel {

    String inputLine;
    String url;

    public String getCocktailDetails(String searchTerm) {
        StringBuffer response = new StringBuffer();
        if (searchTerm != "") {
            url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + searchTerm + "&format=json";
        } else {
            url = "https://www.thecocktaildb.com/api/json/v1/1/random.php?format=json";
        }
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString().trim();
    }
}