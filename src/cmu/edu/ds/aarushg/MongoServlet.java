package cmu.edu.ds.aarushg;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
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

    @Override
    public void init() {
        mongoUserModel = new MongoUserModel();
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException, ServletException {
        startTime = System.currentTimeMillis();
        searchWord = request.getQueryString();
        addSearchWord(searchWord);
        finalResponse = mongoUserModel.getCocktailDetails(searchWord);
        parseToJSON(finalResponse);
        response.getWriter().append(finalResponse);
        endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        userAgent = request.getHeader("User-Agent");
        addUserAgent(userAgent);
        addTime(elapsedTime);
        addLogs(searchWord, elapsedTime, userAgent, startTime, endTime);
        String nextView = null;
    }

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
    }

    public void addSearchWord(String searchWord) {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://aarushg:aarushpassword@mealdbcluster-aky53.mongodb.net/test?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("Project4Task2");
        MongoCollection<Document> collection = database.getCollection("searchWord");
        Document searchDoc = new Document("searchWord", searchWord);
        collection.insertOne(searchDoc);
    }

    public void addTime(long elapsedTime) {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://aarushg:aarushpassword@mealdbcluster-aky53.mongodb.net/test?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("Project4Task2");
        MongoCollection<Document> collection = database.getCollection("elapsedTime");
        Document timeDoc = new Document("elapsedTime", elapsedTime);
        collection.insertOne(timeDoc);
    }

    public void addUserAgent(String userAgent) {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://aarushg:aarushpassword@mealdbcluster-aky53.mongodb.net/test?retryWrites=true&w=majority");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("Project4Task2");
        MongoCollection<Document> collection = database.getCollection("userAgent");
        Document userAgentDoc = new Document("userAgent", userAgent);
        collection.insertOne(userAgentDoc);
    }

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
    }
}