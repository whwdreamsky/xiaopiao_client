package TrainClient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import webservice.Price;
import webservice.TrainInfoData;
import webservice.TrainToFromData;

import java.util.*;

/**
 * Created by oliver on 2017/4/17.
 */
public class FilterTrainList {
    private SchemaConifg schemaConifg;
    private List<TrainToFromData> trainToFromDatalist;
    private HashMap<String,JsonElement> dmslotlist;
    private  Map<String,String> nlgneed;
    public FilterTrainList(SchemaConifg schemaConifg, HashMap<String,JsonElement> dmslotlist,Map nlgneed)
    {
        this.schemaConifg=schemaConifg;
        this.dmslotlist = dmslotlist;
        this.nlgneed = nlgneed;
    }
    public void filterTrainList(List<TrainToFromData> trainToFromData)
    {
        //if (trainToFromData)
        this.trainToFromDatalist = trainToFromData;
        ComparatorAdviceIml comparatorAdviceIml = new ComparatorAdviceIml();
        Collections.sort(trainToFromDatalist,comparatorAdviceIml);
        List<SlotBean> careslots = new ArrayList<SlotBean>();
        List<Map.Entry<String, JsonElement>> list = new ArrayList<Map.Entry<String,JsonElement>>(dmslotlist.entrySet());
        Collections.sort(list, new ComparatorSlotListImp());
        List<TrainToFromData> newtraindata =null;
        for(Map.Entry<String, JsonElement> mapping : list)
        {
            System.out.println("name:"+mapping.getKey()+"offset:"+mapping.getValue().getAsJsonObject().get("offset"));
            //System.out.println(trainToFromData.size());
            if(mapping.getValue().getAsJsonObject().get("offset").getAsInt()==0)
            {
                newtraindata = filterBySlot(mapping.getKey().toString());
                System.out.println("火车数："+newtraindata.size());
                setTrainToFromDatalist(newtraindata);
                if(newtraindata.size()==0)
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
                    setTrainToFromDatalist(newtraindata);

                }
                else
                {
                    System.out.println("二级需求不满足");
                }

            }

        }
        if(this.trainToFromDatalist.size()>0)
        {
            nlgneed.put("type","satisfy");
        }
        else
        {
            nlgneed.put("type","nodata");
        }
        System.out.println(this.getTrainToFromDatalist().size());
        if(this.getTrainToFromDatalist().size()>0)
        {
            System.out.println(new Gson().toJson(this.getTrainToFromDatalist()));
        }
        //System.out.println(this.getTrainToFromDatalist().get(0).getTrain_code()+this.getTrainToFromDatalist().get(0).getPrice_list().get(0).getPrice_type());
        //String[] careslots = {"traintype","starttime","arrivetime","advicetype"};


    }

    public Map<String, String> getNlgneed() {
        return nlgneed;
    }

    public List<TrainToFromData> filterBySlot(String slotname)
    {
        List<TrainToFromData> newtrainlist = new ArrayList<TrainToFromData>();
           switch (slotname)
            {
                case "user_traintype":
                    return filteredByTrainType();
                case "user_starttime":
                    return filterByStarttime();
                case "user_arrivetime":
                    return filterByArriveTime();
                case "user_advicetype":
                        return filteredByAdviceType();
                case "user_seattype":
                    return filteredBySeattype();
                case "user_trainname":
                        return filteredByTrainname();
            }
        System.out.println("No Catch!!");
        return getTrainToFromDatalist();
    }

    private List<TrainToFromData> filteredBySeattype() {
        List<TrainToFromData> newtrainlist = new ArrayList<TrainToFromData>();
        for (int i=0;i<trainToFromDatalist.size();i++)
        {
            List<Price> pricelist = trainToFromDatalist.get(i).getPrice_list();
            for(int j=0;j<pricelist.size();j++)
            {
                if(pricelist.get(j).getPrice_type().equals(schemaConifg.getSeattype().getValue()))
                {
                    newtrainlist.add(trainToFromDatalist.get(i));
                    break;
                }
            }
        }
        if(newtrainlist.size()!=0||schemaConifg.getSeattype().getOffset()==0){nlgneed.put("seattype",schemaConifg.getSeattype().getValue());}
        return newtrainlist;
    }

    private List<TrainToFromData> filteredByTrainname() {
        List<TrainToFromData> newtrainlist = new ArrayList<TrainToFromData>();
        for (int i=0;i<trainToFromDatalist.size();i++)
        {
            //System.out.println("123:"+trainToFromDatalist.get(i).getTrain_code());
            if(trainToFromDatalist.get(i).getTrain_no().equalsIgnoreCase(schemaConifg.getTrainname().getValue()))
            {
                newtrainlist.add(trainToFromDatalist.get(i));
                if(newtrainlist.size()!=0||schemaConifg.getSeattype().getOffset()==0){nlgneed.put("trainname",schemaConifg.getTrainname().getValue());}
                return newtrainlist;
            }

        }
        if(newtrainlist.size()!=0||schemaConifg.getTrainname().getOffset()==0){nlgneed.put("trainname",schemaConifg.getTrainname().getValue());}
        return newtrainlist;
    }

    public List<TrainToFromData> filteredByTrainType()
    {
        String traintypestr = this.schemaConifg.getTraintype().getValue();
        List<TrainToFromData> newtrainlist = new ArrayList<TrainToFromData>();
        if(traintypestr.equals("H"))return trainToFromDatalist;
        for (int i=0;i<trainToFromDatalist.size();i++)
        {
            if(trainToFromDatalist.get(i).getTrain_type().equals(traintypestr))
            {
                newtrainlist.add(trainToFromDatalist.get(i));
            }

        }

        if(newtrainlist.size()!=0||schemaConifg.getTraintype().getOffset()==0){nlgneed.put("traintype",GlobalData.GetTrainType(schemaConifg.getTraintype().getValue()));}
        return newtrainlist;

    }

    public List<TrainToFromData> filteredByAdviceType()
    {
        List<TrainToFromData> newtrainlist = new ArrayList<TrainToFromData>();
        if(this.trainToFromDatalist.size()==0) return newtrainlist;
        if(this.schemaConifg.getAdvicetype().getValue().equals("最早"))
        {
            newtrainlist.add(this.trainToFromDatalist.get(0));

        }
        else if(this.schemaConifg.getAdvicetype().getValue().equals("最晚"))
        {
            newtrainlist.add(this.trainToFromDatalist.get(this.trainToFromDatalist.size()-1));
        }
        else if(this.schemaConifg.getAdvicetype().getValue().equals("最快"))
        {
            int runtime = 10000;
            int runtimetemp=10000;
            int fastesttrain = 0;
            String runtimestr;
            for (int i=0;i<getTrainToFromDatalist().size();i++)
            {
                runtimestr = this.trainToFromDatalist.get(i).getRun_time();
                runtimetemp = Integer.parseInt(runtimestr.replace("小时","").replace("分",""));
                if(runtime>runtimetemp)
                {
                    runtime = runtimetemp;
                    fastesttrain=i;
                }

            }
            newtrainlist.add(trainToFromDatalist.get(fastesttrain));
            nlgneed.put("runtime",trainToFromDatalist.get(fastesttrain).getRun_time());
            /*
            if(schemaConifg.getIntent().equals("TRAINTIMECOST")){

                nlgneed.put("advicetype",schemaConifg.getAdvicetype().getValue());
            }
            */

        }
        else if(this.schemaConifg.getAdvicetype().getValue().equals("最便宜"))
        {
            Float MAXPIRCE = 10000000f;
            Float minprice = MAXPIRCE;
            int cheapest_train =0;
            for (int i=0;i<getTrainToFromDatalist().size();i++)
            {
                for(int j =0;j<this.getTrainToFromDatalist().get(i).getPrice_list().size();j++)
                {
                    Price price = this.getTrainToFromDatalist().get(i).getPrice_list().get(j);
                    if(price.getPrice_type().equals(schemaConifg.getSeattype().getValue()))
                    {
                        if(i==0)
                        {
                            minprice = Float.parseFloat(price.getPrice());

                        }
                        if(minprice>Float.parseFloat(price.getPrice()))
                        {
                            minprice=Float.parseFloat(price.getPrice());
                            cheapest_train = i;
                        }
                        break;
                    }
                    else if(schemaConifg.getSeattype().getValue().equals(""))
                    {
                        if(i==0)
                        {
                            minprice = Float.parseFloat(price.getPrice());

                        }
                        if(minprice>Float.parseFloat(price.getPrice()))
                        {
                            minprice=Float.parseFloat(price.getPrice());
                            cheapest_train = i;
                        }
                        break;
                    }
                }

            }
            newtrainlist.add(getTrainToFromDatalist().get(cheapest_train));
            if(minprice !=MAXPIRCE)
            {
                nlgneed.put("minprice",String.valueOf(minprice));
                nlgneed.put("advicetype",schemaConifg.getAdvicetype().getValue());
            }

        }
        else
        {
            return this.trainToFromDatalist;
        }
        if(newtrainlist.size()!=0||schemaConifg.getAdvicetype().getOffset()==0){nlgneed.put("advicetype",schemaConifg.getAdvicetype().getValue());}
        return newtrainlist;
    }


    public List<TrainToFromData>  filterByStarttime()
    {
        TimeBean timeBean = schemaConifg.getStarttime();
        if (timeBean.getTime().equals("")) return trainToFromDatalist;
        List<TrainToFromData> newtrainlist = new ArrayList<TrainToFromData>();
        for (int i = 0; i < trainToFromDatalist.size(); i++) {
            if (judegeTrainTime(trainToFromDatalist.get(i).getStart_time(),timeBean.getTime(),timeBean.getTimeend()))
            {
                newtrainlist.add(trainToFromDatalist.get(i));
            }
        }
        if(newtrainlist.size()>0||schemaConifg.getStarttime().getOffset()==0)
        {
            nlgneed.put("starttime",schemaConifg.getStarttime().getTime());
            if(!schemaConifg.getStarttime().getTimeend().equals(""))nlgneed.put("starttimeend",schemaConifg.getStarttime().getTimeend());
        }
        return newtrainlist;
    }
    public List<TrainToFromData>  filterByArriveTime()
    {
        TimeBean timeBean = schemaConifg.getArrivetime();
        if (timeBean.getTime().equals("")) return trainToFromDatalist;
        List<TrainToFromData> newtrainlist = new ArrayList<TrainToFromData>();
        for (int i = 0; i < trainToFromDatalist.size(); i++) {
            if (judegeTrainTime(trainToFromDatalist.get(i).getEnd_time(),timeBean.getTime(),timeBean.getTimeend()))
            {
                newtrainlist.add(trainToFromDatalist.get(i));
            }
        }
        if(newtrainlist.size()>0||schemaConifg.getArrivetime().getOffset()==0)
        {
            nlgneed.put("arrivettime",schemaConifg.getArrivetime().getTime());
            if(!schemaConifg.getArrivetime().getTimeend().equals(""))nlgneed.put("arrivetimeend",schemaConifg.getArrivetime().getTimeend());
        }
        return newtrainlist;
    }

    public List<TrainToFromData> filterByTimeBean(TimeBean timeBean,String type) {
        List<TrainToFromData> newtrainlist = new ArrayList<TrainToFromData>();
        if (timeBean.getTime().equals("")) return trainToFromDatalist;
        for (int i = 0; i < trainToFromDatalist.size(); i++) {
            if (judegeTrainTime(getTimeByType(trainToFromDatalist.get(i),type),timeBean.getTime(),timeBean.getTimeend()))
            {
                newtrainlist.add(trainToFromDatalist.get(i));
            }
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
                if (Math.abs(timevalue - timetrainvalue) > 100) return false;
            }
            else
            {
                if(!(timetrainvalue >= timevalue && timetrainvalue <= timeendvalue + 100)) return false;
            }

        }

        return result;
    }

    public String getTimeByType(TrainToFromData trainToFromData,String type)
    {
        if(type.equals("arrivetime"))
        {
            return  trainToFromData.getEnd_time();
        }
        else
        {
            return  trainToFromData.getStart_time();
        }
    }


    public SchemaConifg getSchemaConifg() {
        return schemaConifg;
    }

    public void setSchemaConifg(SchemaConifg schemaConifg) {
        this.schemaConifg = schemaConifg;
    }

    public List<TrainToFromData> getTrainToFromDatalist() {
        return trainToFromDatalist;
    }

    public void setTrainToFromDatalist(List<TrainToFromData> trainToFromDatalist) {
        this.trainToFromDatalist = trainToFromDatalist;
    }
}
