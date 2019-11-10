/**
 * Author Name: Aarush Gupta
 * Author ID: aarushg
 * <p>
 * This acts as the Model class for the MongoServlet. It is used to make the connection to the API end point of
 * cocktails db, fetch the json, parse and send it to the servlet to send it further to the android application and
 * store that data in the mongo collection.
 */

package cmu.edu.ds.aarushg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;

public class MongoUserModel {

    String inputLine;
    String url;
    String strDrink;
    String idDrink;
    String strDrinkThumb;
    String strAlcoholic;
    String strInstructions;

    /**
     * Method() getCocktailDetails
     * Makes a HTTP connection with the API endpoint and fetches the data. Upon successfully fetching the data, it
     * parses the JSON data by fetching from an array and building a json representation to pass in the form of a
     * string to the servlet in order to send it to the android application.
     *
     * Source for randomizer: https://stackoverflow.com/questions/5271598/java-generate-random-number-between-two-given-values
     *
     * @param searchTerm
     * @return
     */

    public String getCocktailDetails(String searchTerm) {
        String outputResponse = null;
        StringBuffer response = new StringBuffer();
        url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + searchTerm + "&format=json";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            JSONObject jo = new JSONObject(response.toString());
            JSONArray jsonArray = jo.getJSONArray("drinks");
            Random r = new Random();
            int low = 0;
            int high = jsonArray.length();
            int result = r.nextInt(high - low) + low;
            JSONObject jo1 = jsonArray.getJSONObject(result);
            strDrink = (String) jo1.get("strDrink");
            idDrink = (String) jo1.get("idDrink");
            strDrinkThumb = (String) jo1.get("strDrinkThumb");
            strAlcoholic = (String) jo1.get("strAlcoholic");
            strInstructions = (String) jo1.get("strInstructions");
            outputResponse = toString();
            in.close();
        }

        /**
         * Exception Handling
         */ catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputResponse;
    }

    /**
     * Method() toString
     * JSON representation of the data received through the JSON array from the API endpoint
     *
     * @return JSON representation to pass to servlet
     */

    @Override
    public String toString() {
        return "{\"drinks\":[" +
                "{\"strDrink\":" + "\"" + strDrink + "\"" + "," +
                "\"idDrink\":" + "\"" + idDrink + "\"" + "," +
                "\"strDrinkThumb\":" + "\"" + strDrinkThumb + "\"" + "," +
                "\"strAlcoholic\":" + "\"" + strAlcoholic + "\"" + "," +
                "\"strInstructions\":" + "\"" + strInstructions + "\"" + "}]}";
    }
}