package TrainClient;


import DialogManagement.DMResultBean;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by oliver on 2017/4/15.
 */
public class SchemaConifg {
    //public
    private static  String API_LOC="威海";
    private String query;
    private String intent;
    private String actionname;
    private String actiontype;
    private String userid ;
    private SlotBean startpoint;//API_LOC
    private SlotBean arrivepoint;
    private TimeBean starttime;
    //private SlotBean startdate;
    private TimeBean arrivetime;
    //private SlotBean arrivedate;
    private SlotBean traintype;//"H"
    private SlotBean trainname;
    private SlotBean passcity;
    private SlotBean seattype;
    private SlotBean advicetype ;
    

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getActionname() {
        return actionname;
    }

    public void setActionname(String actionname) {
        this.actionname = actionname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public SchemaConifg()
    {
        startpoint = new SlotBean(GlobalData.MAXOFFSET,"");//API_LOC
        arrivepoint= new SlotBean(GlobalData.MAXOFFSET,"");
        starttime= new TimeBean(GlobalData.MAXOFFSET,"","");
        //startdate = new SlotBean("");
        arrivetime= new TimeBean(GlobalData.MAXOFFSET,"","");
        //arrivedate = new SlotBean("");
        traintype= new SlotBean(GlobalData.MAXOFFSET,"");//"H"
        trainname= new SlotBean(GlobalData.MAXOFFSET,"");
        passcity= new SlotBean(GlobalData.MAXOFFSET,"");
        seattype= new SlotBean(GlobalData.MAXOFFSET,"");
        advicetype = new SlotBean(GlobalData.MAXOFFSET,"");
    }
    public void setDefaultValue(RequestNLU requestNLU)
    {
        query= requestNLU.getQuery();
        userid = requestNLU.getUserid();
        startpoint.setValue(API_LOC);
        traintype.setValue("H");
        traintype.setOffset(0);
        Date date = new Date();
        SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");
        String dateformat = sdfdate.format(date);
        starttime.setDate(dateformat);
    }

    public void filledSchema(DMResultBean dmResultBean)
    {
        this.intent = dmResultBean.getIntent();
        this.actionname = dmResultBean.getActionname();
        setActiontype(dmResultBean.getActiontype());
        HashMap<String,JsonElement> slotlist= dmResultBean.getSlotlist();
        Map<String,SlotBean> slotneedfilled = new HashMap();
        //单值槽位
        slotneedfilled.put("startpoint",this.getStartpoint());
        slotneedfilled.put("arrivepoint",this.arrivepoint);
        slotneedfilled.put("passcity",this.passcity);
        slotneedfilled.put("seattype",this.seattype);
        slotneedfilled.put("traintype",this.traintype);
        slotneedfilled.put("advicetype",this.advicetype);
        Iterator iterator = slotneedfilled.entrySet().iterator();
        String key="";
        String api_key="";
        String value="";
        int offset=1;
        while(iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry)iterator.next();
            key = entry.getKey().toString();
            api_key="user_"+key;
            System.out.println("api_key:"+api_key);
            if(slotlist.containsKey(api_key))
            {
                //取槽位第一层
                JsonObject slotobject = (JsonObject) slotlist.get(api_key);
                offset = slotobject.get("offset").getAsInt();
                JsonElement jsonElement = slotobject.get("value");
                JsonObject jsonObject =null;
                value="";
                if(jsonElement instanceof JsonObject)
                {
                    jsonObject=(JsonObject)jsonElement;
                    value = jsonObject.get(key).getAsString();

                }
                else if (jsonElement instanceof JsonArray)
                {
                    jsonObject = (JsonObject) ((JsonArray)jsonElement).get(0);
                    value = jsonObject.get(key).getAsString();
                }
                else if(jsonElement instanceof JsonPrimitive)
                {
                    value = ((JsonPrimitive)jsonElement).getAsString();
                }
                if(value!="")
                {
                    SlotBean slotBean = new SlotBean(offset,value);
                    slotneedfilled.put(key,slotBean);
                }

            }
        }
        setSingleSlot(slotneedfilled);
        //复合槽位
        if(slotlist.containsKey("user_starttime"))
        {
            this.starttime = filledtimeslot(slotlist.get("user_starttime"),this.starttime);
        }
        if(slotlist.containsKey("user_arrivetime"))
        {
            this.arrivetime = filledtimeslot(slotlist.get("user_arrivetime"),this.arrivetime);
        }
        if(slotlist.containsKey("user_trainname"))
        {
            JsonElement jsonname = slotlist.get("user_trainname").getAsJsonObject().get("value");
            this.trainname.setOffset(slotlist.get("user_trainname").getAsJsonObject().get("offset").getAsInt());
            String train_name = "";
                if(jsonname.getAsJsonObject().has("traintype"))
                {
                    this.traintype.setValue(jsonname.getAsJsonObject().get("traintype").getAsString());
                    train_name = this.traintype.getValue();
                }
                if(jsonname.getAsJsonObject().has("traincode"))
                {
                    train_name=train_name+jsonname.getAsJsonObject().get("traincode").getAsString();
                this.trainname.setValue(train_name);
            }
        }




    }

    private void setSingleSlot(Map<String,SlotBean> singleSlot) {
        this.startpoint = singleSlot.get("startpoint");
        this.arrivepoint = singleSlot.get("arrivepoint");
        this.passcity = singleSlot.get("passcity");
        this.seattype = singleSlot.get("seattype");
        this.traintype = singleSlot.get("traintype");
        this.advicetype = singleSlot.get("advicetype");
    }

    //starttime,arrivetime
    public TimeBean filledtimeslot(JsonElement slotobject,TimeBean timeBean)
    {
        int offset=1;
        JsonElement jsonElement = slotobject.getAsJsonObject().get("value");
        if(!(jsonElement instanceof JsonObject)) return timeBean;
        JsonObject jsonObject = (JsonObject)jsonElement;
        offset =  slotobject.getAsJsonObject().get("offset").getAsInt();
        timeBean.setOffset(offset);
        if(jsonObject.has("time"))
        {
            String timestr = jsonObject.get("time").getAsString();
            String[] timearry = timestr.split(":");
            if(timearry.length==3)
            {
                String timenew = timearry[0]+":"+timearry[1];
                timeBean.setTime(timenew);
            }

        }
        if(jsonObject.has("date"))
        {
            String date = jsonObject.get("date").getAsString();
            if(date.split("-").length>2)
            {
                timeBean.setDate(jsonObject.get("date").getAsString());
            }
        }
        if(jsonObject.has("date-time"))
        {
            String datetimestr = jsonObject.get("date-time").getAsString();
            String date_time[] = datetimestr.split("T",2);
            if(date_time.length==2)
            {
                timeBean.setDate(date_time[0]);
                String timestr = date_time[1].substring(0,date_time[1].length()-1);
                String[] timearry = timestr.split(":");
                if(timearry.length==3)
                {
                    String timenew = timearry[0]+":"+timearry[1];
                    timeBean.setTime(timenew);
                }
            }
        }


        return timeBean;
    }
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SlotBean getStartpoint() {
        return startpoint;
    }

    public void setStartpoint(SlotBean startpoint) {
        this.startpoint = startpoint;
    }

    public SlotBean getArrivepoint() {
        return arrivepoint;
    }

    public void setArrivepoint(SlotBean arrivepoint) {
        this.arrivepoint = arrivepoint;
    }

    public TimeBean getStarttime() {
        return starttime;
    }

    public void setStarttime(TimeBean starttime) {
        this.starttime = starttime;
    }

    public TimeBean getArrivetime() {
        return arrivetime;
    }

    public void setArrivetime(TimeBean arrivetime) {
        this.arrivetime = arrivetime;
    }

    public SlotBean getTraintype() {
        return traintype;
    }

    public void setTraintype(SlotBean traintype) {
        this.traintype = traintype;
    }

    public SlotBean getTrainname() {
        return trainname;
    }

    public void setTrainname(SlotBean trainname) {
        this.trainname = trainname;
    }

    public SlotBean getPasscity() {
        return passcity;
    }

    public void setPasscity(SlotBean passcity) {
        this.passcity = passcity;
    }

    public SlotBean getSeattype() {
        return seattype;
    }

    public void setSeattype(SlotBean seattype) {
        this.seattype = seattype;
    }

    public SlotBean getAdvicetype() {
        return advicetype;
    }

    public void setAdvicetype(SlotBean advicetype) {
        this.advicetype = advicetype;
    }

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }
}
