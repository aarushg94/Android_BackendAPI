/**
 * Author Name: Aarush Gupta
 * Author ID: aarushg
 * <p>
 * This acts as the server (API endpoint) and stores user data for the Android application - CocktailSelector.
 * It instantiates the CocktailModel class to perform fetch data from a URL end point i.e. CocktailDBAPI
 * (https://www.thecocktaildb.com/api.php) and parses the same and sends it back to the Android application.
 * It also records the user data along with the API response which was received from the cocktails db API after
 * parsing and selecting some of the data. This is responsible for storing logs and information about the application
 * overall to perform analytics on the same later.
 */

package cmu.edu.ds.aarushg;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "MongoServlet",
        urlPatterns = {"/MongoServletRequest"})
public class MongoServlet extends HttpServlet {

    String finalResponse;
    String searchWord = null;
    MongoUserModel mongoUserModel = null;
    String userAgent;
    long startTime;
    long endTime;

    /**
     * Method() init()
     * We instantiate the servlet by instantiating the model class it uses. The servlet responds to the GET request
     * via the doGet method.
     */

    @Override
    public void init() {
        mongoUserModel = new MongoUserModel();
    }

    /**
     * Method() doGet
     * This is responsible for parsing the JSON data and storing all user logs such as start time, end time, elapsed
     * time, word searched for, user agent (device) and logs for the android application. It does so by calling all
     * it's helper methods.
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException, ServletException {
        startTime = System.currentTimeMillis();
        searchWord = request.getQueryString();
        finalResponse = mongoUserModel.getCocktailDetails(searchWord);
        if (finalResponse.equals("{\"drinks\":null}")) {
            System.out.println("No data received for word " + searchWord);
        } else {
            addSearchWord(searchWord);
            parseToJSON(finalResponse);
            response.getWriter().append(finalResponse);
            endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            userAgent = request.getHeader("User-Agent");
            addUserAgent(userAgent);
            addTime(elapsedTime);
            addLogs(searchWord, elapsedTime, userAgent, startTime, endTime);
        }
    }

    /**
     * Method() parseToJSON
     * Opens a MongoDB connection. Fetches the database and the required collection. It then creates a new document
     * for each word that the user inputs. Since there can be many drinks for 1 word (base of alcohol), it randomizes
     * the jsonArray to provide with a randomly chosed drink. It then fetches the json response and appends the same
     * to each document. This corresponds to the inputFromAPI collection in mongoDB. It then stores this data in
     * that document and then closes the connection.
     *
     * @param finalResponse
     */

    private void parseToJSON(String finalResponse) {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://aarushg:aarushpassword@mealdbcluster-aky53.mongodb.net/test?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("Project4Task2");
        MongoCollection<Document> collection = database.getCollection("inputFromAPI");
        Document doc = new Document("searchWord", searchWord);
        JSONObject jo = new JSONObject(finalResponse);
        JSONArray jsonArray = jo.getJSONArray("drinks");
        Random r = new Random();
        int low = 0;
        int high = jsonArray.length();
        int result = r.nextInt(high - low) + low;
        JSONObject jo1 = jsonArray.getJSONObject(result);
        doc.append("drinkID", jo1.get("idDrink"));
        doc.append("drinkName", jo1.get("strDrink"));
        doc.append("type", jo1.get("strAlcoholic"));
        doc.append("instructions", jo1.get("strInstructions"));
        doc.append("imageURL", jo1.get("strDrinkThumb"));
        collection.insertOne(doc);
        mongoClient.close();
    }

    /**
     * Method() addSearchWord
     * Opens a MongoDB connection. Fetches the database and the required collection. It then creates a new document
     * for each word that the user has input. It then stores that word and closes the connection.
     *
     * @param searchWord
     */

    public void addSearchWord(String searchWord) {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://aarushg:aarushpassword@mealdbcluster-aky53.mongodb.net/test?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("Project4Task2");
        MongoCollection<Document> collection = database.getCollection("searchWord");
        Document searchDoc = new Document("searchWord", searchWord);
        collection.insertOne(searchDoc);
        mongoClient.close();
    }

    /**
     * Method() addTime
     * Opens a MongoDB connection. Fetches the database and the required collection. It then creates a new collection
     * to store the elapsed time which is the difference between the end time and start time of the user request and
     * rendering of data. It then stores that data and closes the connection.
     *
     * @param elapsedTime
     */

    public void addTime(long elapsedTime) {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://aarushg:aarushpassword@mealdbcluster-aky53.mongodb.net/test?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("Project4Task2");
        MongoCollection<Document> collection = database.getCollection("elapsedTime");
        Document timeDoc = new Document("elapsedTime", elapsedTime);
        collection.insertOne(timeDoc);
        mongoClient.close();
    }

    /**
     * Method() addUserAgent
     * Opens a MongoDB connection. Fetches the database and the required collection. It creates a new mongo document
     * each time a user tries to fetch data. It then stores that data and closes the connection.
     *
     * @param userAgent
     */

    public void addUserAgent(String userAgent) {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://aarushg:aarushpassword@mealdbcluster-aky53.mongodb.net/test?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("Project4Task2");
        MongoCollection<Document> collection = database.getCollection("userAgent");
        Document userAgentDoc = new Document("userAgent", userAgent);
        collection.insertOne(userAgentDoc);
        mongoClient.close();
    }

    /**
     * Method() addLogs
     * Opens a MongoDB connection. Fetches the database and the required collection. It then creates a new document
     * to store logs consisting of the search word, elapsed time, userAgent, start time and end time. It then appends
     * all this information to a document each time the user searches for a word. It then stores this document and
     * closes the connection.
     *
     * @param searchWord
     * @param elapsedTime
     * @param userAgent
     * @param startTime
     * @param endTime
     */

    public void addLogs(String searchWord, long elapsedTime, String userAgent, long startTime, long endTime) {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://aarushg:aarushpassword@mealdbcluster-aky53.mongodb.net/test?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("Project4Task2");
        MongoCollection<Document> collection = database.getCollection("dataLogs");
        Document doc = new Document("searchWord", searchWord);
        doc.append("elapsedTime", Long.toString(elapsedTime));
        doc.append("userAgent", userAgent);
        doc.append("startTime", Long.toString(startTime));
        doc.append("endTime", Long.toString(endTime));
        collection.insertOne(doc);
        mongoClient.close();
    }
}