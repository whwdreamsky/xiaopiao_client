package TrainClient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import webservice.TrainTicketRealTimeData;

import java.util.*;

/**
 * Created by oliver on 2017/4/22.
 */
public class FilterTrainTicket
{
    private  SchemaConifg schemaConifg;
    private Map nlgneed;
    private HashMap<String,JsonElement> dmslotlist;
    private List<TrainTicketRealTimeData> trainTicketRealTimeDataList;
    public FilterTrainTicket(SchemaConifg schemaConifg,HashMap<String,JsonElement> dmslotlist, Map nlgneed)
    {
        this.schemaConifg = schemaConifg;
        this.nlgneed = nlgneed;
        this.dmslotlist = dmslotlist;
    }

    public Map filterTrainTicket(List<TrainTicketRealTimeData> trainTicketRealTimeData)
    {
        this.trainTicketRealTimeDataList = trainTicketRealTimeData;
        for(int i=0;i<trainTicketRealTimeDataList.size();i++)
        {
            trainTicketRealTimeDataList.get(i).setTicketMap();
        }
        List<Map.Entry<String, JsonElement>> list = new ArrayList<Map.Entry<String,JsonElement>>(dmslotlist.entrySet());
        Collections.sort(list, new ComparatorSlotListImp());
        System.out.println("火车数："+trainTicketRealTimeDataList.size());
        List<TrainTicketRealTimeData> newtraindata=null;
        for(Map.Entry<String, JsonElement> mapping : list)
        {
            System.out.println("name:"+mapping.getKey()+"offset:"+mapping.getValue().getAsJsonObject().get("offset"));
            //System.out.println(trainToFromData.size());

            if(mapping.getValue().getAsJsonObject().get("offset").getAsInt()==0)
            {
                trainTicketRealTimeDataList = filterBySlot(mapping.getKey().toString());
                System.out.println("火车数："+trainTicketRealTimeDataList.size());
                //setTrainToFromDatalist(newtraindata);
                if(trainTicketRealTimeData.size()==0)
                {
                    System.out.println("没有相关数据");
                    break;
                }
            }
            else
            {
                newtraindata = filterBySlot(mapping.getKey().toString());
                System.out.println("火车数："+newtraindata.size());
                if(newtraindata.size()!=0)
                {
                    setTrainTicketRealTimeDataList(newtraindata);

                }
                else
                {
                    System.out.println("二级需求不满足");
                }

            }

        }
        System.out.println(trainTicketRealTimeDataList.size());
        if(trainTicketRealTimeDataList.size()>0)
        {
            System.out.println(new Gson().toJson(trainTicketRealTimeDataList));
        }
        return  nlgneed;

    }


    public List<TrainTicketRealTimeData> filterBySlot(String slotname)
    {
        switch (slotname)
        {
            case "user_traintype":
                return filteredTrainType();
            case "user_starttime":
                return filterByStarttime();
            case "user_arrivetime":
                return filterByArriveTime();
            case "user_seattype":
                return filteredBySeattype();
            case "user_trainname":
                //这里trainname 作为比较限制的条件，有效区间只保留本轮或者上轮
                if(schemaConifg.getTrainname().getOffset()==0)
                {
                    return filteredByTrainname();
                }
                break;
            case "user_advicetype":
                return filteredByAdviceType();
            default:
                System.out.println("TICKET : No Catch!!");
                return trainTicketRealTimeDataList;
        }
        return trainTicketRealTimeDataList;
    }

    private List<TrainTicketRealTimeData> filteredByTrainname() {
        List<TrainTicketRealTimeData> newtrainlist = new ArrayList<TrainTicketRealTimeData>();
        for (int i=0;i<trainTicketRealTimeDataList.size();i++)
        {
            if(trainTicketRealTimeDataList.get(i).getTrain_code().equalsIgnoreCase(schemaConifg.getTrainname().getValue()))
            {
                newtrainlist.add(trainTicketRealTimeDataList.get(i));
                if(newtrainlist.size()!=0||schemaConifg.getSeattype().getOffset()==0){nlgneed.put("trainname",schemaConifg.getTrainname().getValue());}
                return newtrainlist;
            }

        }
        if(newtrainlist.size()!=0||schemaConifg.getTrainname().getOffset()==0){nlgneed.put("trainname",schemaConifg.getTrainname().getValue());}
        return newtrainlist;
    }


    private List<TrainTicketRealTimeData> filteredTrainType()
    {
        String traintypestr = this.schemaConifg.getTraintype().getValue();
        List<TrainTicketRealTimeData> newtrainlist = new ArrayList<TrainTicketRealTimeData>();
        if(traintypestr.equals("H"))return trainTicketRealTimeDataList;
        for (int i=0;i<newtrainlist.size();i++)
        {
            if(newtrainlist.get(i).getTrain_code().indexOf(traintypestr)!=-1)
            {
                newtrainlist.add(trainTicketRealTimeDataList.get(i));
            }

        }

        if(newtrainlist.size()!=0||schemaConifg.getTraintype().getOffset()==0){nlgneed.put("traintype",GlobalData.GetTrainType(schemaConifg.getTraintype().getValue()));}
        return newtrainlist;

    }

    private List<TrainTicketRealTimeData> filteredBySeattype()
    {
        List<TrainTicketRealTimeData> newtrainlist = new ArrayList<TrainTicketRealTimeData>();
        String ticketliststr ="";
        for (int i=0;i<trainTicketRealTimeDataList.size();i++)
        {
            Map kindticketmap = trainTicketRealTimeDataList.get(i).getTicketmap();
            String num = (String) kindticketmap.get(GlobalData.GetSeatType(schemaConifg.getSeattype().getValue()));
            if(!num.equals("--"))
            {
                newtrainlist.add(trainTicketRealTimeDataList.get(i));
            }

        }
        if(newtrainlist.size()!=0||schemaConifg.getSeattype().getOffset()==0){nlgneed.put("seattype",schemaConifg.getSeattype().getValue());}
        return newtrainlist;

    }

    public List<TrainTicketRealTimeData> filteredByAdviceType()
    {
        List<TrainTicketRealTimeData> newtrainlist = new ArrayList<TrainTicketRealTimeData>();
        if(this.trainTicketRealTimeDataList.size()==0) return newtrainlist;
        if(this.schemaConifg.getAdvicetype().getValue().equals("最早"))
        {
            newtrainlist.add(this.trainTicketRealTimeDataList.get(0));
            nlgneed.put("advicetype",schemaConifg.getAdvicetype().getValue());

        }
        else if(this.schemaConifg.getAdvicetype().getValue().equals("最晚"))
        {
            newtrainlist.add(this.trainTicketRealTimeDataList.get(this.trainTicketRealTimeDataList.size()-1));
            nlgneed.put("advicetype",schemaConifg.getAdvicetype().getValue());
        }
        else
        {
            return trainTicketRealTimeDataList;
        }
        return newtrainlist;
    }

    public List<TrainTicketRealTimeData> filterByStarttime()
    {
        TimeBean timeBean = schemaConifg.getStarttime();
        if (timeBean.getTime().equals("")) return trainTicketRealTimeDataList;
        List<TrainTicketRealTimeData> newtrainlist = new ArrayList<TrainTicketRealTimeData>();
        for (int i = 0; i < trainTicketRealTimeDataList.size(); i++) {
            if (judegeTrainTime(trainTicketRealTimeDataList.get(i).getStart_time(),timeBean.getTime(),timeBean.getTimeend()))
            {
                newtrainlist.add(trainTicketRealTimeDataList.get(i));
            }
        }
        if(newtrainlist.size()>0||schemaConifg.getStarttime().getOffset()==0)
        {
            nlgneed.put("starttime",schemaConifg.getStarttime().getTime());
            if(!schemaConifg.getStarttime().getTimeend().equals(""))nlgneed.put("starttimeend",schemaConifg.getStarttime().getTimeend());
        }
        return newtrainlist;
    }



    private boolean judegeTrainTime(String timetrain,String time,String timeend)
    {
        int timetrainvalue  = Integer.parseInt(timetrain.replace(":", ""));
        int timevalue  = Integer.parseInt(time.replace(":", ""));
        boolean result = true;
        if(timeend.equals("")||timeend==null)
        {
            //表示前后一小时
            System.out.println("timeend 为空");
            if (Math.abs(timevalue - timetrainvalue) > 100) return false;
        }
        else
        {
            int timeendvalue = Integer.parseInt(timeend.replace(":",""));
            //消除零点时间问题
            if(timeendvalue==0)timeendvalue=2400;
            //避免出现timeperiod 异常的情况
            if(timeendvalue<timevalue)
            {
                System.out.println("timeendvalue<timevalue 不合法");
                if (Math.abs(timevalue - timetrainvalue) > 100) return false;
            }
            else
            {
                if(!(timetrainvalue >= timevalue && timetrainvalue <= timeendvalue + 100)) return false;
            }

        }

        return result;
    }

    public List<TrainTicketRealTimeData> filterByArriveTime()
    {
        TimeBean timeBean = schemaConifg.getArrivetime();
        if (timeBean.getTime().equals("")) return trainTicketRealTimeDataList;
        List<TrainTicketRealTimeData> newtrainlist = new ArrayList<TrainTicketRealTimeData>();
        for (int i = 0; i < trainTicketRealTimeDataList.size(); i++) {
            if (judegeTrainTime(trainTicketRealTimeDataList.get(i).getArrive_time(),timeBean.getTime(),timeBean.getTimeend()))
            {
                newtrainlist.add(trainTicketRealTimeDataList.get(i));
            }
        }
        if(newtrainlist.size()>0||schemaConifg.getArrivetime().getOffset()==0)
        {
            nlgneed.put("arrivettime",schemaConifg.getArrivetime().getTime());
            if(!schemaConifg.getArrivetime().getTimeend().equals(""))nlgneed.put("arrivetimeend",schemaConifg.getArrivetime().getTimeend());
        }
        return newtrainlist;
    }

    public List<TrainTicketRealTimeData> getTrainTicketRealTimeDataList() {
        return trainTicketRealTimeDataList;
    }

    public void setTrainTicketRealTimeDataList(List<TrainTicketRealTimeData> trainTicketRealTimeDataList) {
        this.trainTicketRealTimeDataList = trainTicketRealTimeDataList;
    }
}


