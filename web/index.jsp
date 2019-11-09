<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="cmu.edu.ds.aarushg.userDashboard" %>
<%@ page import="java.util.*" %>
<%@ page import="org.json.simple.parser.JSONParser" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
    <title>User Metrics Dashboard</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>
<body>
<div class="col-lg-12">
    <div class="jumbotron jumbotron-fluid" style="padding:10px !important;">
        <hr>
        <div class="container">
            <h1 class="display-4" style="text-align: center;font-size: 35px;">User Metrics Dashboard</h1>
        </div>
        <hr>
    </div>
</div>
<div class="container" style="min-width: 100%;">
    <div class="row">
        <div class="col-lg-6">
            <div class="card">
                <div class="card-header" style="text-align: center;font-weight: bold;">
                    Most Searched Words
                </div>
                <div class="card-body">
                    <table class="table table-bordered table-md table-hover" style="font-size: 12px !important;">
                        <thead>
                        <tr>
                            <th scope="col">Searched Word</th>
                            <th scope="col">#</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            userDashboard ud = new userDashboard();
                            TreeMap<String, Integer> searchWordMap = new TreeMap<>(ud.searchCount());
                            for (String key : searchWordMap.keySet()) {
                        %>
                        <tr>
                            <td><%out.println(key); %></td>
                            <td><%out.println(searchWordMap.get(key)); %></td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="card">
                <div class="card-header" style="text-align: center;font-weight: bold;">
                    Unique Devices Using the Application
                </div>
                <div class="card-body">
                    <table class="table table-bordered table-md table-hover" style="font-size: 12px !important;">
                        <thead>
                        <tr>
                            <th scope="col">S. No.</th>
                            <th scope="col">User Agent</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            TreeSet<String> userAgentTree = new TreeSet<>(ud.getUserAgentList());
                            int index = 1;
                            for (String s : userAgentTree) {
                        %>
                        <tr>
                            <td><%out.println(index); %></td>
                            <td><%out.println(s); %></td>
                        </tr>
                        <%
                                index++;
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <p></p>
    <div class="row">
        <div class="col-lg-12">
            <div class="card">
                <div class="card-header" style="text-align: center;font-weight: bold;">
                    Logs Based on User Interaction
                </div>
                <div class="card-body">
                    <table class="table table-bordered table-md table-hover" style="font-size: 12px !important;">
                        <thead>
                        <tr>
                            <th scope="col">Searched Word</th>
                            <th scope="col">User Agent</th>
                            <th scope="col">Start Time</th>
                            <th scope="col">End Time</th>
                            <th scope="col">Elapsed Time (milliseconds)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            org.json.simple.JSONArray jsonArray = ud.getUserLogs();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                Object jo = new JSONParser().parse(jsonArray.get(i).toString());
                                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jo;
                        %>
                        <tr>
                            <th scope="row"><%out.println(jsonObject.get("searchWord")); %></th>
                            <td><%out.println(jsonObject.get("userAgent")); %></td>
                            <td><%
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                                java.util.Date finalStartTime = new java.util.Date(Long.valueOf(jsonObject.get("startTime").toString()));
                                out.println(simpleDateFormat.format(finalStartTime));
                            %></td>
                            <td><%
                                java.util.Date finalEndTime = new java.util.Date(Long.valueOf(jsonObject.get("endTime").toString()));
                                out.println(simpleDateFormat.format(finalEndTime));
                            %></td>
                            <td><%out.println(jsonObject.get("elapsedTime")); %></td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <p></p>
    <div class="row">
        <div class="col-lg-12">
            <div class="card">
                <div class="card-header" style="text-align: center;font-weight: bold;">
                    Logs Based on User Interaction
                </div>
                <div class="card-body">
                    <table class="table table-bordered table-md table-hover" style="font-size: 12px !important;">
                        <thead>
                        <tr>
                            <th scope="col">Drink ID</th>
                            <th scope="col">Searched Word</th>
                            <th scope="col">Drink Name</th>
                            <th scope="col">Type</th>
                            <th scope="col">Instructions</th>
                            <th scope="col">Image URL</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            org.json.simple.JSONArray apiLogArray = ud.getAPIResponseDetails();
                            for (int i = 0; i < apiLogArray.size(); i++) {
                                Object jo = new JSONParser().parse(apiLogArray.get(i).toString());
                                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jo;
                        %>
                        <tr>
                            <td><%out.println(jsonObject.get("drinkID")); %></td>
                            <th scope="row"><%out.println(jsonObject.get("searchWord")); %></th>
                            <td><%out.println(jsonObject.get("drinkName")); %></td>
                            <td><%out.println(jsonObject.get("type")); %></td>
                            <td><%out.println(jsonObject.get("instructions")); %></td>
                            <td><%out.println(jsonObject.get("imageURL")); %></td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>