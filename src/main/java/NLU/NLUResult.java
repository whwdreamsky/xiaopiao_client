package NLU;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


import java.util.*;
/**
 * Created by oliver on 2017/4/9.
 */


public class NLUResult {
    private String id;
    private int statuscode;
    private String statustype;
    private String query;
    private String userid;
    private String timestamp;
    private String intent;
    private HashMap<String,JsonElement> slotlist;
    private String sessionid;
    private float score;
    private String appname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public String getStatustype() {
        return statustype;
    }

    public void setStatustype(String statustype) {
        this.statustype = statustype;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public HashMap<String, JsonElement> getSlotlist() {
        return slotlist;
    }

    //过滤掉空槽位
    public void setSlotlist(HashMap<String, JsonElement> slotlist) {
        Iterator iterator = slotlist.entrySet().iterator();
        HashMap<String,JsonElement> slotlist_sloved = new HashMap<String,JsonElement>();
        while(iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            JsonElement slotelement = (JsonElement)entry.getValue();
            if(slotelement.isJsonArray())
            {
                if(slotelement.getAsJsonArray().size()>0)
                {

                    slotlist_sloved.put(entry.getKey().toString(),slotelement);
                }
            }
            else if (slotelement.isJsonPrimitive())
            {
                if(!slotelement.getAsString().equals(""))
                {
                    slotlist_sloved.put(entry.getKey().toString(),slotelement);
                }
            }
            else
            {
                slotlist_sloved.put(entry.getKey().toString(),slotelement);
            }

        }

        this.slotlist = slotlist_sloved;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }



    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }
}
