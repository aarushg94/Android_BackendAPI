/**
 * Author Name: Aarush Gupta
 * Author ID: aarushg
 *
 * This acts as the Model class for the MongoServlet. It is used to make the connection to the API end point of
 * cocktails db, fetch the json, parse and send it to the servlet to send it further to the android application and
 * store that data in the mongo collection.
 */

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

    /**
     * Method() getCocktailDetails
     * Makes a HTTP connection with the API endpoint and fetches the data. Upon successfully fetching the data, it
     * sends the same to the servlet in order to send it to the android application.
     *
     * @param searchTerm
     * @return
     */

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
        }

        /**
         * Exception Handling
         */

        catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString().trim();
    }
}