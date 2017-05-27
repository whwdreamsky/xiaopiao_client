package webservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import UtilTools.UtilTools;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javax.xml.bind.DatatypeConverter;


public class ASRVoice {

    private static final String serverURL = "http://vop.baidu.com/server_api";
    private static String token = "";
    private static final String testFileName = "src/test.pcm";
    //put your own params here
    private static final String apiKey = "jtLL3dZ6GetDECMrlBPvDZmd";
    private static final String secretKey = "6560e641f63968b769cb15654093aac7";
    private static final String cuid = "xiaopiaovoice";

    private ResultVoice resultVoice;



    public static void main(String[] args) throws Exception {

        //method2();
    }
    public ASRVoice()
    {
        resultVoice = new ResultVoice();
        try {
            getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getToken() throws Exception {
        String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" + 
            "&client_id=" + apiKey + "&client_secret=" + secretKey;
        HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject)jsonParser.parse(setRequestInfo(conn));
        token = gsonObject.get("access_token").getAsString();
    }


    public boolean transferToText(String filename) {

        File pcmFile = new File(filename);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(serverURL).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // construct params
        Map params = new HashMap();
        params.put("format", "pcm");
        params.put("rate", 8000);
        params.put("channel", "1");
        params.put("token", token);
        params.put("cuid", cuid);
        params.put("len", pcmFile.length());
        try {
            params.put("speech", DatatypeConverter.printBase64Binary(loadFile(pcmFile)));
        } catch (IOException e) {
            System.out.println("NO Found voice file !!!");
            e.printStackTrace();
            return false;
        }

        // add request header
        try {
            conn.setRequestMethod("POST");
    } catch (ProtocolException e) {
        e.printStackTrace();
    }
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = null;
        try {
            wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(new Gson().toJson(params).toString());
            wr.flush();
            wr.close();
            if (conn.getResponseCode() != 200) {
                // request error
                return false;
            }
            setResultInfo(conn);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private  void setResultInfo(HttpURLConnection conn) throws Exception {
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
        }
        rd.close();
        resultVoice = new Gson().fromJson(response.toString(),new TypeToken<ResultVoice>(){}.getType());
        if(resultVoice.getErr_no()==0)
        {
            String result = UtilTools.tranStrToUtf8(resultVoice.getResult().get(0));
            resultVoice.setResultstr(result.substring(0,result.length()));
        }

    }

    private  String setRequestInfo(HttpURLConnection conn) throws Exception {
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response.toString();
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }



    private  void method2() throws Exception {
        File pcmFile = new File(testFileName);
        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL
                + "?cuid=" + cuid + "&token=" + token).openConnection();

        // add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "audio/pcm; rate=8000");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(loadFile(pcmFile));
        wr.flush();
        wr.close();


        setResultInfo(conn);

    }

    public ResultVoice getResultVoice() {
        return resultVoice;
    }

    public void setResultVoice(ResultVoice resultVoice) {
        this.resultVoice = resultVoice;
    }
}
