package com.hrishikeshmishra.intellij.plugin.code.service;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.hrishikeshmishra.intellij.plugin.code.model.gist.FileContent;
import com.hrishikeshmishra.intellij.plugin.code.model.gist.UpdatePayload;
import org.jetbrains.annotations.NotNull;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class GistUpdaterService {

    private final String GIST_URL = "https://api.github.com/gists/";
    private final String TOKEN;

    public GistUpdaterService() {
        this.TOKEN = getTokenFromFile();
    }

    public void update(String gistId, String fileName, String fileContent) {

        try {

            URL obj = new URL(GIST_URL.concat(gistId));
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            setConnectionProperties(con);

            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            Gson gson = new Gson();
            out.write(gson.toJson(getUpdatePayload(fileName, fileContent)));
            out.close();

            int responseCode = con.getResponseCode();
            Notify.info("Gist server", "GET Response Code : "+(responseCode));

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = getResponse(con);
                Notify.info("Gist server", response);
            } else {
                Notify.error("Gist server", "GET request not worked");
            }

        } catch ( IOException e) {
            Notify.error("Gist server", e.getMessage());
        }
    }

    @NotNull
    private String getResponse(HttpsURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private void setConnectionProperties(HttpsURLConnection con) throws ProtocolException {
        con.setRequestProperty("Authorization", "token ".concat(TOKEN));
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        con.setRequestMethod("POST");
        con.setDoOutput(true);
    }


    private static UpdatePayload getUpdatePayload(String fileName, String fileContent) {
        Map<String, FileContent> files = new HashMap<>();
        files.put(fileName, new FileContent(fileContent));
        return new UpdatePayload(null, files);
    }


    private String getTokenFromFile() {
        String tokenFile = "/Users/hrishikesh.mishra/hrishi/doc/g.txt";
        try {
            return Files.readFirstLine(new File(tokenFile), Charset.defaultCharset());
        } catch (IOException e) {
            Notify.error("Token File", "Token file not found.");
            return null;
        }
    }

}
