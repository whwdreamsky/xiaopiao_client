package DialogManagement;

import com.google.gson.JsonElement;

import java.util.HashMap;

/**
 * Created by oliver on 2017/4/11.
 */
public class DMResultBean {
    private String actionname;
    private String actiontype;
    private String intent;
    private HashMap<String,JsonElement> slotlist;
    private String userid;
    private String query;
    private int offset=0;
    private String appname="";

    public DMResultBean(){
        actionname="";
        actiontype="";
        intent="";
        slotlist = new HashMap<String,JsonElement>();
    }

    public String getActionname() {
        return actionname;
    }

    public void setActionname(String actionname) {
        this.actionname = actionname;
    }

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public HashMap<String, JsonElement> getSlotlist() {
        return slotlist;
    }

    public void setSlotlist(HashMap<String, JsonElement> slotlist) {
        this.slotlist = slotlist;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
