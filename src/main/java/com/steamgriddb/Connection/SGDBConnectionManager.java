package com.steamgriddb.Connection;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 * This class handles the connection to SGDB.
 *
 * @author mpaterakis
 */
public class SGDBConnectionManager {

    /*
    * Fields
     */
    private static String APIUri = "https://www.steamgriddb.com/API/v2/";
    private static String authKey = "";

    /**
     * Get a JSONObject from an API path.
     *
     * @param APICallPath The API path
     * @return JSONObject containing the response (Or error code if the call fails)
     */
    public static JSONObject getJSON(String APICallPath) {
        int statusCode = 0;

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APIUri + APICallPath))
                    .GET()
                    .setHeader("Authorization", "Bearer " + authKey)
                    .build();

            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            statusCode = response.statusCode();
            
            System.out.println(response.toString());

            JSONObject json = new JSONObject(response.body().toString());

            if (statusCode != 200) {
                System.out.println("API Error! Status code: " + statusCode);
            }

            return json;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SGDBConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new JSONObject("{ \"success\": \"false\", \"status\": " + statusCode + "}");
    }

    
    /**
     * Make a POST request.
     * 
     * @param APICallPath The API path for the request
     * @return A JSONOBject containing the response of the request
     */
    public static JSONObject post(String APICallPath) {
        int statusCode = 0;

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APIUri + APICallPath))
                    .POST(BodyPublishers.ofString(""))
                    .setHeader("Authorization", "Bearer " + authKey)
                    .build();

            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            statusCode = response.statusCode();

            JSONObject json = new JSONObject(response.body().toString());

            if (statusCode != 200) {
                System.out.println("API Error! Status code: " + statusCode);
            }

            return json;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SGDBConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new JSONObject("{ \"success\": \"false\", \"status\": " + statusCode + "}");
    }

    
    /**
     * Make a multipart POST request.
     * 
     * @param APICallPath The API path for the request
     * @param params The parameters for the Multipart post
     * @return A JSONObject containing the response of the request
     */
    public static JSONObject postMultipart(String APICallPath, Map<Object, Object> params) {
        int statusCode = 0;
        
        try {
            String boundary = new BigInteger(256, new Random()).toString();
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APIUri + APICallPath))
                    .headers("Content-Type", "multipart/form-data;boundary=" + boundary,
                            "Authorization", "Bearer " + authKey)
                    .POST(ofMimeMultipartData(params, boundary))
                    .build();

            HttpResponse<?> response = client.send(request, BodyHandlers.ofString());
            statusCode = response.statusCode();

            JSONObject json = new JSONObject(response.body().toString());

            if (statusCode != 200) {
                System.out.println("API Error! Status code: " + statusCode + ",  Error: " + json.getJSONArray("errors"));
            }

            return json;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SGDBConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new JSONObject("{ \"success\": \"false\", \"status\": " + statusCode + "}");
    }

    /**
     * Make a DELETE request.
     * 
     * @param APICallPath The API path for the request
     * @return A JSONObject containing the response of the request
     */
    public static JSONObject delete(String APICallPath) {
        int statusCode = 0;

        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(APIUri + APICallPath))
                    .DELETE()
                    .setHeader("Authorization", "Bearer " + authKey)
                    .build();

            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            statusCode = response.statusCode();

            JSONObject json = new JSONObject(response.body().toString());

            if (statusCode != 200) {
                System.out.println("API Error! Status code: " + statusCode);
            }

            return json;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SGDBConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new JSONObject("{ \"success\": \"false\", \"status\": " + statusCode + "}");
    }

    /**
     * Get the API base uri.
     *
     * @return The API base uri
     */
    public static String getApiUri() {
        return APIUri;
    }

    /**
     * Set the API base uri.
     *
     * @param APIUri The API base uri
     */
    public static void setApiUri(String APIUri) {
        SGDBConnectionManager.APIUri = APIUri;
    }

    /**
     * Get the API authorization key.
     *
     * @return The API authorization key
     */
    public static String getAuthKey() {
        return authKey;
    }

    /**
     * Set the API authorization key.
     *
     * @param authKey The API authorization key
     */
    public static void setAuthKey(String authKey) {
        SGDBConnectionManager.authKey = authKey;
    }

    /**
     * Initialize SGDBConnectionManager's values.
     *
     * @param APIUri The API base uri
     * @param authKey The API authorization key
     */
    public static void initialize(String APIUri, String authKey) {
        if (APIUri.charAt(APIUri.length() - 1) != '/') {
            APIUri += "/";
        }
        SGDBConnectionManager.APIUri = APIUri;
        SGDBConnectionManager.authKey = authKey;
    }

    /**
     * Make constructor private to give class a static nature
     */
    private SGDBConnectionManager() {
    }

    /**
     * Create a multipart body. [Taken from https://golb.hplar.ch/2019/01/java-11-http-client.html].
     *
     * @param data The data to be converted to multipart
     * @param boundary A boundary String for the separator
     * @return A multipart BodyPublisher
     * @throws IOException
     */
    private static BodyPublisher ofMimeMultipartData(Map<Object, Object> data, String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=").getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName()
                        + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n")
                        .getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return BodyPublishers.ofByteArrays(byteArrays);
    }
}
