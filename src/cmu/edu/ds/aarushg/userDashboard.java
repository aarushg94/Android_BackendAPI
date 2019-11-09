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

    MongoClientURI uri = new MongoClientURI("mongodb+srv://aarushg:aarushpassword@mealdbcluster-aky53.mongodb.net/test?retryWrites=true&w=majority");
    MongoClient mongoClient = new MongoClient(uri);
    MongoDatabase database = mongoClient.getDatabase("Project4Task2");

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
    }

    public HashMap<String, Integer> searchCount() {
        HashMap<String, Integer> searchWordMap = new HashMap<>();
        ArrayList<String> searchedWords = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("searchWord");
        FindIterable<Document> fi = collection.find();
        MongoCursor<Document> cursor = fi.iterator();
        while (cursor.hasNext()) {
            searchedWords.add(cursor.next().get("searchWord").toString());
        }
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