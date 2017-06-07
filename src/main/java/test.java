import NLU.AIAPIResponse;
import NLU.NLUResult;
import NLU.NLUWebService;
import NLU.QueryUnderstand;
import TrainClient.FilterTrainList;
import TrainClient.RequestNLU;
import TrainClient.SchemaConifg;
import TrainClient.TimeBean;
import UtilTools.ReadProperties;
import UtilTools.UtilTools;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.swing.text.html.HTMLDocument;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import static javafx.scene.input.KeyCode.F;

/**
 * Created by oliver on 2017/5/28.
 */
public class test {
    public static void main(String args[])
    {
        QueryUnderstand queryUnderstand = new QueryUnderstand();
        String line="{\"id\":\"bb2ab679-9b10-4d90-a931-a712a5561bac\",\"timestamp\":\"2017-06-05T02:39:27.065Z\",\"lang\":\"zh-cn\",\"result\":{\"source\":\"agent\",\"resolvedQuery\":\"你好小票\",\"action\":\"input.welcome\",\"actionIncomplete\":false,\"parameters\":{},\"contexts\":[],\"metadata\":{\"intentId\":\"284ad872-aaee-4027-97cf-cab140029df0\",\"webhookUsed\":\"false\",\"webhookForSlotFillingUsed\":\"false\",\"intentName\":\"welcome\"},\"fulfillment\":{\"speech\":\"欢迎归来。\",\"messages\":[{\"type\":0,\"speech\":\"嘿！小票很荣幸为您服务\"}]},\"score\":1},\"status\":{\"code\":200,\"errorType\":\"success\"},\"sessionId\":\"oliver\"}\n";
        RequestNLU requestNLU = new RequestNLU();
        requestNLU.setQuery("123");
        requestNLU.setUserid("o20");
        NLUResult nluResult =queryUnderstand.nluProcess(requestNLU,line);
         // JsonObject covertedObject = new Gson().fromJson(line, JsonObject.class);
            //AIAPIResponse aiResponse = new Gson().fromJson(line,new TypeToken<AIAPIResponse>(){}.getType());
            //System.out.println(line);
        SchemaConifg schemaConifg = new SchemaConifg();
        HashMap<String,JsonElement> dmslotlist = new HashMap<>();
        Map nlgneed = new HashMap();

        FilterTrainList filterTrainList = new FilterTrainList(schemaConifg,dmslotlist,nlgneed);
        TimeBean timeBean =new TimeBean(0,"09:00","");
        filterTrainList.filterByTimeBean(timeBean,"starttime");

         //Map map = ReadProperties.getSlotClyclePropers();
         //Iterator iterator= map.entrySet().iterator();
         /*while (iterator.hasNext())
         {
             Map.Entry entry= (Map.Entry)iterator.next();
             System.out.println("key:"+(String) entry.getKey());
             System.out.println("value:"+(String) entry.getValue());

         }
         */
         /*
        NLUWebService nluWebService = new NLUWebService();
        try {
            nluWebService.NluWebAccess(new String("你好小票".getBytes("gbk"),"utf8"),"oliver1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        */


    }



}
