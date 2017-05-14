package webservice;

import TrainClient.GlobalData;

import java.util.List;

/**
 * Created by oliver on 2017/4/15.
 */
public class TestTrainData {



    public static void main(String args[])
    {
        TrainTicketInquire trainTicketInquire = new TrainTicketInquire();


        //Trainlist 的使用
        /*
        List<TrainToFromData> trainToFromDataList = trainTicketInquire.getTrainList("哈尔滨","大连");
        if(trainToFromDataList==null)
        {
            System.err.println("errocode:"+trainTicketInquire.getErrocode());
            System.err.println("erromessage:"+trainTicketInquire.getErromeassage());
        }
        else
        {
            System.out.println("");
            System.out.println("size: "+ trainToFromDataList.size());
            System.out.println(trainToFromDataList.get(0).getTrain_code()+trainToFromDataList.get(0).getPrice_list().get(0).getPrice_type());

        }
        */
        /*/
        // traininfo 的使用
        /*
        TrainInfoData trainInfoData =trainTicketInquire.getTrainInfo("K1068");
        if(trainInfoData==null)
        {
            System.err.println("errocode:"+trainTicketInquire.getErrocode());
            System.err.println("erromessage:"+trainTicketInquire.getErromeassage());
        }
        else
        {
            System.out.println("traininfo: "+ trainInfoData.getTrain_info().getName()+trainInfoData.getTrain_info().getStart()+trainInfoData.getTrain_info().getStarttime()+trainInfoData.getTrain_info().getEndtime());
            System.out.println(trainInfoData.getStation_list().size());
            System.out.println(trainInfoData.getStation_list().get(0).getTrain_id()+trainInfoData.getStation_list().get(0).getLeave_time()+trainInfoData.getStation_list().get(0).getArrived_time()+trainInfoData.getStation_list().get(0).getStation_name());


        }
        System.out.println(trainTicketInquire.getResult());
        */
        /*
        GlobalData.LOADTRAINCODE();
        List<TrainTicketRealTimeData> realTimeData = trainTicketInquire.getTrainTicketRealTime(GlobalData.traincode.get("北京"),GlobalData.traincode.get("威海"),"2017-05-18","");

        if(realTimeData==null)
        {
            System.err.println("errocode:"+trainTicketInquire.getErrocode());
            System.err.println("erromessage:"+trainTicketInquire.getErromeassage());
        }
        else
        {
            System.out.println(realTimeData.size());
            System.out.println("traininfo: "+ realTimeData.get(0).getArrive_time()+realTimeData.get(0).getTrain_code()+ realTimeData.get(0).getRun_time()+ realTimeData.get(0).getSwz_num());
            //System.out.println(trainInfoData.getStation_list().get(0).getTrain_id()+trainInfoData.getStation_list().get(0).getLeave_time()+trainInfoData.getStation_list().get(0).getArrived_time()+trainInfoData.getStation_list().get(0).getStation_name());


        }
        */


    }

}
