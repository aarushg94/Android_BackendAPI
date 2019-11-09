/**
 * Author Name: Aarush Gupta
 * Author ID: aarushg
 *
 * This acts as the dashboard servlet. It fetches all the data from the mongo collections in order to parse and
 * perform operations on the same to show valuable insights on the dashboard page.
 */

package cmu.edu.ds.aarushg;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "userDashboard", urlPatterns = "/userMetrics")
public class userDashboard extends HttpServlet {

    /**
     * Make initial connection to MongoDB in order to fetch the database from which the data has to be retreived.
     */

    MongoClientURI uri = new MongoClientURI("mongodb+srv://aarushg:aarushpassword@mealdbcluster-aky53.mongodb.net/test?retryWrites=true&w=majority");
    MongoClient mongoClient = new MongoClient(uri);
    MongoDatabase database = mongoClient.getDatabase("Project4Task2");

    /**
     * Method() doGet
     * Calls all the helper methods in order to perform the operations to show valuable insights on the metrics
     * dashboard page. Once all opearations have been performed and the data is rendered, it closes the mongo
     * connection.
     *
     * @param request
     * @param response
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        getElapsedTime();
        searchCount();
        getUserAgentList();
        try {
            getUserLogs();
            getAPIResponseDetails();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mongoClient.close();
    }

    /**
     * Method() searchCount
     * It fetches all the data from the searchWord collection and reads the documents one by one with the help of
     * MongoCursor. For each word it adds it to an arraylist. The arraylist in then traversed through in order to
     * take the unique words and store them in a Hashmap with their respective counts to check for words with
     * highest totals.
     *
     * @return -> Hashmap with words and their counts
     */

    public HashMap<String, Integer> searchCount() {
        HashMap<String, Integer> searchWordMap = new HashMap<>();
        ArrayList<String> searchedWords = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("searchWord");
        FindIterable<Document> fi = collection.find();
        MongoCursor<Document> cursor = fi.iterator();
        while (cursor.hasNext()) {
            searchedWords.add(cursor.next().get("searchWord").toString());
        }
        cursor.close();
        int temp = 0;
        for (int i = 0; i < searchedWords.size(); i++) {
            if (searchWordMap.containsKey(searchedWords.get(i))) {
                temp = searchWordMap.get(searchedWords.get(i));
                temp++;
                searchWordMap.put(searchedWords.get(i), temp);
            } else {
                temp = 1;
                searchWordMap.put(searchedWords.get(i), temp);
            }
        }
        return searchWordMap;
    }

    /**
     * Method() getElapsedTime
     * This fetches all documents from elapsedTime collection in order to calculate an average elapsedTime for all
     * user requests till date.
     *
     * @return -> elapsed time in milliseconds
     */

    public long getElapsedTime() {
        int count = 0;
        long time = 0;
        MongoCollection<Document> collection = database.getCollection("elapsedTime");
        FindIterable<Document> fi = collection.find();
        MongoCursor<Document> cursor = fi.iterator();
        while (cursor.hasNext()) {
            count++;
            time += (long) (cursor.next().get("elapsedTime"));
        }
        return time / count;
    }

    /**
     * Method() getUserAgentList
     * This fetches all the documents from userAgent collection in order to add the same in an array list.
     *
     * @return -> Arraylist of all user Agents
     */

    public ArrayList<String> getUserAgentList() {
        MongoDatabase database = mongoClient.getDatabase("Project4Task2");
        MongoCollection<Document> collection = database.getCollection("userAgent");
        FindIterable<Document> fi = collection.find();
        MongoCursor<Document> cursor = fi.iterator();
        ArrayList<String> userAgentList = new ArrayList<>();
        while (cursor.hasNext()) {
            userAgentList.add(cursor.next().get("userAgent").toString());
        }
        return userAgentList;
    }

    /**
     * Method() getUserLogs
     * This fetches the dataLogs collection and parses through each document with the help of MongoCursor to fetch
     * records, parse and store then in a JSON array.
     *
     * @return -> JSON Array of user pertaining logs.
     */

    public JSONArray getUserLogs() throws ParseException {
        MongoCollection<Document> collection = database.getCollection("dataLogs");
        FindIterable<Document> fi = collection.find();
        MongoCursor<Document> cursor = fi.iterator();
        JSONArray jsonArray = new JSONArray();
        while (cursor.hasNext()) {
            Document temp = cursor.next();
            JSONObject jo = new JSONObject();
            jo.put("searchWord", (temp.get("searchWord").toString()));
            jo.put("elapsedTime", (temp.get("elapsedTime").toString()));
            jo.put("userAgent", (temp.get("userAgent").toString()));
            jo.put("startTime", (temp.get("startTime").toString()));
            jo.put("endTime", (temp.get("endTime").toString()));
            jsonArray.add(jo);
        }
        return jsonArray;
    }

    /**
     * Method() getAPIResponseDetails
     * This fetches all the documents from inputFromAPI collection. It then loops through each document via
     * MongoCursor and fetches the data and stores the same in a JSON Array.
     *
     * @return -> JSON Array of API response logs.
     */

    public JSONArray getAPIResponseDetails() throws ParseException {
        MongoCollection<Document> collection = database.getCollection("inputFromAPI");
        FindIterable<Document> fi = collection.find();
        MongoCursor<Document> cursor = fi.iterator();
        JSONArray jsonArray1 = new JSONArray();
        while (cursor.hasNext()) {
            Document temp = cursor.next();
            JSONObject jo = new JSONObject();
            jo.put("searchWord", (temp.get("searchWord").toString()));
            jo.put("drinkID", (temp.get("drinkID").toString()));
            jo.put("drinkName", (temp.get("drinkName").toString()));
            jo.put("type", (temp.get("type").toString()));
            jo.put("instructions", (temp.get("instructions").toString()));
            jo.put("imageURL", (temp.get("imageURL").toString()));
            jsonArray1.add(jo);
        }
        return jsonArray1;
    }

}