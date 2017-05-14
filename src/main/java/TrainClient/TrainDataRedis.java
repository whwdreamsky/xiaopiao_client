package TrainClient;


import UtilTools.RedisUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import webservice.TrainInfoData;
import webservice.TrainTicketRealTimeData;
import webservice.TrainToFromData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliver on 2017/4/23.
 */
public class TrainDataRedis {

    private SchemaConifg schemaConifg ;
    private RedisUtil redisUtil;
    public TrainDataRedis(SchemaConifg schemaConifg)
    {
        this.schemaConifg = schemaConifg;
        redisUtil = new RedisUtil();
        redisUtil.setup();
    }

    public List<TrainToFromData> getTrainListData()
    {

        if(redisUtil.getJedis()==null) return null;
        String rediskey = "trainlist"+schemaConifg.getStartpoint().getValue()+schemaConifg.getArrivepoint().getValue();
        if(redisUtil.getValueByKey(rediskey)!=null)
        {
            return new Gson().fromJson(redisUtil.getValueByKey(rediskey),new TypeToken<ArrayList<TrainToFromData>>(){}.getType());
        }
        return  null;
    }

    public void setTrainList(List<TrainToFromData> trainToFromDataList)
    {
        String rediskey = "trainlist"+schemaConifg.getStartpoint().getValue()+schemaConifg.getArrivepoint().getValue();
        if(redisUtil.getJedis()!=null)
        {
            redisUtil.saveStringKV(rediskey,new Gson().toJson(trainToFromDataList));

        }
    }

    public TrainInfoData getTrainInfo()
    {

        if(redisUtil.getJedis()==null) return null;
        String rediskey = "traininfo"+schemaConifg.getTrainname().getValue();
        if(redisUtil.getValueByKey(rediskey)!=null)
        {
            return new Gson().fromJson(redisUtil.getValueByKey(rediskey),new TypeToken<TrainInfoData>(){}.getType());
        }
        return  null;
    }
    public void setTrainInfo(TrainInfoData trainInfo)
    {
        String rediskey = "traininfo"+schemaConifg.getTrainname().getValue();
        redisUtil.saveStringKV(rediskey,new Gson().toJson(trainInfo));
    }



    public List<TrainTicketRealTimeData> getTrainTicketData()
    {

        if(redisUtil.getJedis()==null) return null;
        String rediskey = "trainticket"+schemaConifg.getStartpoint().getValue()+schemaConifg.getArrivepoint().getValue()+schemaConifg.getStarttime().getDate();
        if(redisUtil.getValueByKey(rediskey)!=null)
        {
            return new Gson().fromJson(redisUtil.getValueByKey(rediskey),new TypeToken<ArrayList<TrainTicketRealTimeData>>(){}.getType());
        }
        return  null;
    }
    public void setTrainTicket(List<TrainTicketRealTimeData> trainTicketRealTimeDataList)
    {
        String rediskey = "trainticket"+schemaConifg.getStartpoint().getValue()+schemaConifg.getArrivepoint().getValue()+schemaConifg.getStarttime().getDate();
        redisUtil.saveStringKV(rediskey,new Gson().toJson(trainTicketRealTimeDataList));
    }


}
