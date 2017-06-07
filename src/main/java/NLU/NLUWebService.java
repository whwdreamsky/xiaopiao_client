package NLU;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by oliver on 2017/5/30.
 */
public class NLUWebService {
    public static String accesstoken = "85e15ecbcd2243db9e931fde7251a7a0";
    public static String errocode="";
    public static String erromeassage="";



    public String NluWebAccess(String query,String userid){
        String result =null;
        String url ="https://api.api.ai/v1/";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("query",query);//车次名称，如：G4
        params.put("lang","zh-cn");//应用APPKEY(应用详细页查询)
        params.put("sessionId",userid);//返回数据的格式,xml或json，默认json

       result= connetWeb(url,params);

/*
        if(errocode.equals("0"))
        {
           result= jsonObject.get("result").getAsJsonObject().toString();
        }
        return result;
        */
        return  result;
    }

    public String connetWeb(String url,Map params)
    {
        System.setProperty("https.protocols", "TLSv1.1");
        url=url+ "query?v=20150910";
        HttpClient httpClient = HttpClientBuilder.create().build();
        Gson gson = new Gson();
        String result =null;
        //System.setProperty("https.protocols", "TLSv1.1");
        try {

            HttpPost request = new HttpPost(url);
            StringEntity params_str = new StringEntity(gson.toJson(params));
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer"+accesstoken);
            request.setEntity(params_str);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if(entity!=null)
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line=bufferedReader.readLine())!=null)
                {
                    System.out.println(line);
                }
                //System.out.println(response.getEntity().getContent());
            }


// handle response here...
        } catch (Exception ex) {
            // handle exception here
            System.err.println(ex.getMessage());
        } finally {
        }
        return result;

    }



    private JsonObject connnectWebSerive(String url, Map params)
    {
        url=url+ "query?v=20150910";
        String result="";
        JsonObject object=null;
        try {
            //result =net(url, params, "POST");
            object = new JsonParser().parse(result).getAsJsonObject();
            if((object.get("status").getAsJsonObject().get("code").getAsInt()==0))
            {
                //System.out.println(object.get("result").toString());
            }else{
                //System.out.println(object.get("error_code")+":"+object.get("reason").toString());
                //errocode=(object.get("status").getAsJsonObject().get("code").getAsInt());
                //erromeassage=object.get("status").getAsString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            errocode="-1";
            erromeassage="网络异常,train data 访问失败";
        }
        return object;
    }


/*
    public  String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                    System.err.println("访问Train Data 失败！！");
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

*/
}
