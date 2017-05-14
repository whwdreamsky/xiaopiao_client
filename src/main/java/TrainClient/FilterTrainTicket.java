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
            case "user_advicetype":
                if(schemaConifg.getAdvicetype().getOffset()==0)
                {
                    return filteredByAdviceType();
                }
                break;
            case "user_seattype":
                return filteredBySeattype();
            case "user_trainname":
                if(schemaConifg.getTrainname().getOffset()==0)
                {
                    return filteredByTrainname();
                }
                break;
        }
        System.out.println("TICKET : No Catch!!");
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
        if(schemaConifg.getStarttime().getTime().equals(""))
        {
            return this.trainTicketRealTimeDataList;
        }
        List<TrainTicketRealTimeData> newtrainlist = new ArrayList<TrainTicketRealTimeData>();
        int startimevalue = Integer.parseInt(this.schemaConifg.getStarttime().getTime().replace(":",""));
        int starttime2=0;
        for (int i=0;i<trainTicketRealTimeDataList.size();i++)
        {
            starttime2 = Integer.parseInt(trainTicketRealTimeDataList.get(i).getStart_time().replace(":",""));
            if(Math.abs(startimevalue-starttime2)<100)
            {
                newtrainlist.add(trainTicketRealTimeDataList.get(i));
            }
        }
        if(newtrainlist.size()!=0||schemaConifg.getStarttime().getOffset()==0){nlgneed.put("starttime",schemaConifg.getStarttime().getTime());}
        return newtrainlist;

    }

    public List<TrainTicketRealTimeData> filterByArriveTime()
    {
        if(schemaConifg.getArrivetime().getTime().equals(""))
        {
            return this.trainTicketRealTimeDataList;
        }
        List<TrainTicketRealTimeData> newtrainlist = new ArrayList<TrainTicketRealTimeData>();
        int arrivetimevalue = Integer.parseInt(this.schemaConifg.getArrivetime().getTime().replace(":",""));
        int arrivetime2=0;
        for (int i=0;i<trainTicketRealTimeDataList.size();i++)
        {
            arrivetime2 = Integer.parseInt(trainTicketRealTimeDataList.get(i).getArrive_time().replace(":",""));
            if(Math.abs(arrivetimevalue-arrivetime2)<100)
            {
                newtrainlist.add(trainTicketRealTimeDataList.get(i));
            }
        }
        if(newtrainlist.size()!=0||schemaConifg.getArrivetime().getOffset()==0){nlgneed.put("arrivetime",schemaConifg.getArrivetime().getTime());}
        return newtrainlist;

    }

    public List<TrainTicketRealTimeData> getTrainTicketRealTimeDataList() {
        return trainTicketRealTimeDataList;
    }

    public void setTrainTicketRealTimeDataList(List<TrainTicketRealTimeData> trainTicketRealTimeDataList) {
        this.trainTicketRealTimeDataList = trainTicketRealTimeDataList;
    }
}


