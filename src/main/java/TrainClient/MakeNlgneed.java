package TrainClient;

import UtilTools.UtilTools;
import webservice.Price;
import webservice.TrainInfoData;
import webservice.TrainTicketRealTimeData;
import webservice.TrainToFromData;

import javax.rmi.CORBA.Util;
import java.util.*;

/**
 * Created by oliver on 2017/4/20.
 */
public class MakeNlgneed {

    private SchemaConifg schemaConifg;
    public MakeNlgneed(SchemaConifg schemaConifg)
    {
        this.schemaConifg = schemaConifg;
    }


    public Map makeByIntentWithTrainList(Map nlgneed,List<TrainToFromData> trainToFromDataList)
    {
        switch (schemaConifg.getIntent())
        {
            case "TRAINLIST":
                make_trainlistneed(trainToFromDataList,nlgneed);
                break;
            case "TRAINPRICE":
                make_trainpriceneed(trainToFromDataList,nlgneed);
                break;
            case "TRAINTIMECOST":
                make_traintimecostneed(trainToFromDataList,nlgneed);
                break;
            default:
                System.out.println("TRAINLIST intent not catch ！！");
        }
        return nlgneed;
    }


    public Map makeTrainTicketNlgNeed(List<TrainTicketRealTimeData> trainTicketRealTimeDataList,Map nlgneed,SchemaConifg schemaConifg)
    {
        //对火车数量过多进行处理，过多就找到适当的槽位选择澄清
        if(trainTicketRealTimeDataList.size()>GlobalData.MAXTRAINNUM)
        {
            boolean boolresult = processOutOfMaxTrain(GlobalData.CARESLOT_TRAINTICKET,nlgneed);
            if(boolresult) return nlgneed;
        }
        String tickelist ="";
        if(!nlgneed.containsKey("seattype"))
        {
            nlgneed.put("intent","Clarify");
            nlgneed.put("type","clarify_seattype");
            return nlgneed;
        }
        /* 这里对orderticket 有问题这里虽然只有一个火车但是无法写到session 里，下次读不出该火车名
        if(trainTicketRealTimeDataList.size()==1)
        {
            nlgneed.put("trainname",trainTicketRealTimeDataList.get(0).getTrain_code());

        }*/

        if(nlgneed.containsKey("trainname"))
        {
            nlgneed.put("start_time",trainTicketRealTimeDataList.get(0).getStart_time());
            nlgneed.put("arrive_time",trainTicketRealTimeDataList.get(0).getArrive_time());
            String temp = (String)trainTicketRealTimeDataList.get(0).getTicketmap().get(GlobalData.GetSeatType(schemaConifg.getSeattype().getValue()));
                String ticketstate="";
                switch (temp)
                {
                    case "--":
                        ticketstate = "没有卖的哈";
                        break;
                    case "无":
                        ticketstate ="暂时卖完了哈";
                        break;
                    case "有":
                        ticketstate ="还很充裕呢";
                        break;
                    default:
                        ticketstate = "有"+ temp +"张";
                }
                nlgneed.put("ticketstate",ticketstate);


        }
        else
        {
            int fitnum=0;
            for(int i=0;i<trainTicketRealTimeDataList.size();i++)
            {
                String tempstr = (String)trainTicketRealTimeDataList.get(i).getTicketmap().get(GlobalData.GetSeatType(schemaConifg.getSeattype().getValue()));
                if(!tempstr.equals("--")&&!tempstr.equals("无")&&!tempstr.equals("0"))
                {
                    tickelist+=trainTicketRealTimeDataList.get(i).getTrain_code()+" "+trainTicketRealTimeDataList.get(i).getStart_time()+" 票数为 "+tempstr+",";
                    fitnum++;
                }
            }
            if(fitnum==0)
            {
                nlgneed.put("type","sellout");
            }
            else
            {
                nlgneed.put("ticketlist",tickelist);
            }

        }


        return nlgneed;
    }
    public Map makeByIntentWithTrainInfo(Map nlgneed,TrainInfoData trainInfoData)
    {
        nlgneed.put("trainname",schemaConifg.getTrainname().getValue());
        switch (schemaConifg.getIntent())
        {
            case "TRAININFO":
                nlgneed = make_traininfoneed(trainInfoData,nlgneed);
                break;
            case "TRAINTIMEARRIVE":
                nlgneed =make_trainarriveneed(trainInfoData,nlgneed);
                break;
            case "TRAINTIMESTART":
                nlgneed = make_trainstartneed(trainInfoData,nlgneed);
                break;
        }
        return nlgneed;

    }

    public Map make_traininfoneed(TrainInfoData infoData ,Map nlgneed)
    {
        nlgneed.put("start_point",infoData.getTrain_info().getStart());
        nlgneed.put("arrive_point",infoData.getTrain_info().getEnd());
        String passcitylist="";
        String passmatch ="";
        String arrivetime="";
        if (!schemaConifg.getPasscity().getValue().equals(""))
        {
            nlgneed.put("passcity",schemaConifg.getPasscity().getValue());
            for(int i=0;i<infoData.getStation_list().size();i++)
            {
                if(infoData.getStation_list().get(i).getStation_name().equals(schemaConifg.getPasscity().getValue()))
                {
                    arrivetime = infoData.getStation_list().get(i).getArrived_time();
                    break;
                }
            }
            if(arrivetime.equals(""))
            {
                nlgneed.put("type","notfindpass");
            }
            else
            {
                nlgneed.put("arrivepasstime",arrivetime);
                nlgneed.put("type","findpass");
            }

        }
        else
        {

            int i=0;
            for(i=0;i<infoData.getStation_list().size();i++)
            {
                passcitylist+=infoData.getStation_list().get(i).getStation_name()+" "+infoData.getStation_list().get(i).getArrived_time()+"，";
                if(infoData.getStation_list().get(i).getStation_name().equals(schemaConifg.getStartpoint().getValue()))
                {
                    passmatch+=infoData.getStation_list().get(i).getStation_name()+" "+infoData.getStation_list().get(i).getArrived_time()+"，";
                }
                if(!passmatch.equals(""))
                {
                    passmatch+=infoData.getStation_list().get(i).getStation_name()+" "+infoData.getStation_list().get(i).getArrived_time()+"，";
                }
                if(infoData.getStation_list().get(i).getStation_name().equals(schemaConifg.getArrivepoint().getValue()))
                {
                    break;
                }
            }
            if(passmatch.equals("")|| i==infoData.getStation_list().size())
            {
                nlgneed.put("type","satisfy");
                nlgneed.put("trainpasslist",passcitylist);
            }
            else
            {
                nlgneed.put("type","match_arrivepoint");
                nlgneed.put("trainpasslist",passmatch);
            }


        }
        return  nlgneed;
    }

    public Map make_trainarriveneed(TrainInfoData trainInfoData,Map nlgneed)
    {
        String arrivetime = "";
        for(int i=0;i<trainInfoData.getStation_list().size();i++)
        {
            if(trainInfoData.getStation_list().get(i).getStation_name().indexOf(schemaConifg.getArrivepoint().getValue())!=-1)
            {
                arrivetime = trainInfoData.getStation_list().get(i).getArrived_time();
                break;
            }
        }
        if(arrivetime.equals(""))
        {
            nlgneed.put("type","notmatch");
        }
        else
        {
            nlgneed.put("arrive_time",arrivetime);
        }
        return nlgneed;
    }
    public Map make_trainstartneed(TrainInfoData trainInfoData,Map nlgneed)
    {
        String starttime = "";
        for(int i=0;i<trainInfoData.getStation_list().size();i++)
        {
            if(trainInfoData.getStation_list().get(i).getStation_name().indexOf(schemaConifg.getStartpoint().getValue())!=-1)
            {
                starttime = trainInfoData.getStation_list().get(i).getLeave_time();
            }
        }
        if(starttime.equals(""))
        {
            nlgneed.put("type","notmatch");
        }
        else
        {
            nlgneed.put("start_time",starttime);
        }
        return nlgneed;
    }


    public Map make_trainpriceneed(List<TrainToFromData> trainToFromDataList,Map nlgneed)
    {
        nlgneed = make_trainlistneed(trainToFromDataList,nlgneed);
        if (nlgneed.containsKey("minprice"))
        {
            return nlgneed;
        }
        if(nlgneed.containsKey("trainname"))
        {
                List <Price> priceList = trainToFromDataList.get(0).getPrice_list();
                String priceliststr="";
                String pricewithseattype="";
                for(int i=0;i<priceList.size();i++)
                {
                    if(priceList.get(i).getPrice_type().equals(schemaConifg.getSeattype().getValue()))
                    {
                        pricewithseattype = schemaConifg.getSeattype().getValue()+": "+priceList.get(i).getPrice();
                        break;
                    }
                    else
                    {
                        priceliststr = priceliststr + priceList.get(i).getPrice_type()+" "+priceList.get(i).getPrice()+",";
                    }

                }
                if(pricewithseattype.equals(""))
                {
                    nlgneed.put("pricelist",priceliststr.substring(0,priceliststr.length()-1));
                }
                else
                {
                    nlgneed.put("pricelist",pricewithseattype);
                }
        }
        if(schemaConifg.getSeattype().getValue().equals("")) return nlgneed;
        int MAXPIRCE = 1000000;
        float minprice =MAXPIRCE;
        float maxprice = -1;
        int cheapest_train =0;
        int expensivetest_train = 0;
        for (int i=0;i<trainToFromDataList.size();i++)
        {
            for(int j =0;j<trainToFromDataList.get(i).getPrice_list().size();j++)
            {
                Price price = trainToFromDataList.get(i).getPrice_list().get(j);
                if(price.getPrice_type().equals(schemaConifg.getSeattype().getValue()))
                {
                    if(i==0)
                    {
                        minprice = Float.parseFloat(price.getPrice());
                        maxprice = Float.parseFloat(price.getPrice());

                    }
                    if(minprice>Float.parseFloat(price.getPrice()))
                    {
                        minprice=Float.parseFloat(price.getPrice());
                        cheapest_train = i;
                    }
                    if(maxprice<Float.parseFloat(price.getPrice()))
                    {
                        maxprice = Float.parseFloat(price.getPrice());
                        expensivetest_train =i;
                    }
                    break;
                }
            }

        }
        if(minprice !=MAXPIRCE &&maxprice!=-1)
        {
            nlgneed.put("minprice",String.valueOf(minprice));
            nlgneed.put("maxprice",String.valueOf(maxprice));
        }
        nlgneed.put("seattype",schemaConifg.getSeattype().getValue());

        return  nlgneed;
    }


    public Map make_trainlistneed(List<TrainToFromData> trainToFromDataList,Map nlgneed)
    {
        if(trainToFromDataList.size()==0) return nlgneed;
        int trainnum = trainToFromDataList.size();
        String trainlist = "";
        //对火车数量过多进行处理，过多就找到适当的槽位选择澄清
        if(trainToFromDataList.size()>GlobalData.MAXTRAINNUM)
        {
            boolean boolresult = processOutOfMaxTrain(GlobalData.CARESLOT_TRAINLIST,nlgneed);
            if(boolresult) return nlgneed;
        }
        for(int i=0;i<trainToFromDataList.size();i++)
        {
            trainlist+=trainToFromDataList.get(i).getTrain_no()+" "+trainToFromDataList.get(i).getStart_time()+"，";
        }
        nlgneed.put("trainnum",String.valueOf(trainnum));
        nlgneed.put("trainlist",trainlist.substring(0,trainlist.length()-1));
        return nlgneed;

    }

    public Map make_traintimecostneed(List<TrainToFromData> trainToFromDataList,Map nlgneed)
    {
        nlgneed = make_trainlistneed(trainToFromDataList, nlgneed);
        if(nlgneed.containsKey("runtime")) return nlgneed;
        int MAXTIME = 10000000;
        int minruntime = MAXTIME;
        String minruntimestr = "";
        int maxruntime = 0;
        String maxruntimestr ="";
        int runtimetemp =0;

        for (int i=0;i< trainToFromDataList.size();i++)
        {
            if(trainToFromDataList.get(i).getTrain_type().equals(schemaConifg.getTraintype().getValue())||schemaConifg.getTraintype().getValue().equals("H"))
            {

                System.out.println("runtime"+(trainToFromDataList.get(i).getRun_time()));
                runtimetemp = Integer.parseInt((trainToFromDataList.get(i).getRun_time()).replace("小时","").replace("分",""));
                if(minruntime>runtimetemp)
                {
                    minruntime = runtimetemp;
                    minruntimestr = trainToFromDataList.get(i).getRun_time();
                }
                if(maxruntime < runtimetemp)
                {
                    maxruntime = runtimetemp;
                    maxruntimestr = trainToFromDataList.get(i).getRun_time();
                }
            }

        }

        System.out.println("min&max:"+maxruntime+minruntime);
        if(maxruntime==0||minruntime==MAXTIME)
        {
            nlgneed.put("type","nodata");
        }
        else
        {
            nlgneed.put("minruntime",minruntimestr);
            nlgneed.put("maxruntime",maxruntimestr);
            if(minruntime==maxruntime)
            {
                nlgneed.put("runtime",minruntimestr);
            }
        }


        return nlgneed;
    }

    public Map makeOrderConfirmNlgNeed(Map nlgneed)
    {
        //{startdate} {startpoint} - {arrivepoint} {trainname} {seattype}，座次 {seatid}

            boolean boolresult = processOutOfMaxTrain(GlobalData.CARESLOT_ORDERTICKET,nlgneed);
            if(boolresult)
            {
                System.out.println("需要进行澄清");
                return nlgneed;
            }
            System.out.println("trainname:"+schemaConifg.getTrainname().getValue());
        nlgneed.put("arrivepoint",schemaConifg.getArrivepoint().getValue());
        nlgneed.put("trainname",schemaConifg.getTrainname().getValue());
        nlgneed.put("seattype",schemaConifg.getSeattype().getValue());
        nlgneed.put("seatid","24-1A");
        return nlgneed;

    }

    //这里处理当筛选的火车数量过多时，
    private boolean processOutOfMaxTrain(String []careslot,Map nlgneed)
    {
        boolean bool = false;
        List<String> needtoclarify =new ArrayList<>();

        for(int i=0;i<careslot.length;i++)
        {
            System.out.println(careslot[i]);
            switch (careslot[i])
            {
                case "user_seattype":
                    if(schemaConifg.getSeattype().getValue().equals(""))
                    {
                        needtoclarify.add("user_seattype");
                    }
                    break;
                case "user_starttime":
                    if(schemaConifg.getStarttime().getTime().equals("")&&schemaConifg.getStarttime().getTimeend()==null)
                    {
                        needtoclarify.add("user_starttime");
                    }
                    break;

                case "user_arrivetime":
                    if(schemaConifg.getArrivetime().getTime().equals(""))
                    {
                        needtoclarify.add("user_arrivetime");
                    }
                    break;
                case "user_traintype":
                    if(schemaConifg.getTraintype().getValue().equals("H"))
                    {
                        needtoclarify.add("user_traintype");
                    }
                    break;
                case "user_arrivepoint":
                    if(schemaConifg.getArrivepoint().getValue().equals(""))
                    {
                        needtoclarify.add("user_arrivepoint");
                    }
                    break;
            }
        }
        System.out.println("the size of needtoclarify is "+ needtoclarify.size());
        if(needtoclarify.size()>0)
        {
            bool = true;
            String randomslot = needtoclarify.get(UtilTools.GetRandomNum(1,needtoclarify.size())-1);
            System.out.println("火车数量超出"+GlobalData.MAXTRAINNUM+"  对"+randomslot+"进行澄清");
            switch (randomslot)
            {
                case "user_seattype":
                    nlgneed.put("intent","Clarify");
                    nlgneed.put("type","clarify_seattype");
                    break;
                case "user_starttime":
                    nlgneed.put("intent","Clarify");
                    nlgneed.put("type","clarify_starttime");
                    break;

                case "user_arrivetime":
                    nlgneed.put("intent","Clarify");
                    nlgneed.put("type","clarify_arrivetime");
                    break;
                case "user_traintype":
                    nlgneed.put("intent","Clarify");
                    nlgneed.put("type","clarify_traintype");
                    break;
                case "user_arrivepoint":
                    nlgneed.put("intent","Clarify");
                    nlgneed.put("type","clarify_arrivepoint");
                    break;
            }
        }
        return bool;
    }
}
