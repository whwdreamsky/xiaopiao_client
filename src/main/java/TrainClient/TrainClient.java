package TrainClient;
import DialogManagement.DialogManagement;
import DialogManagement.DMResultBean;
import NLU.*;
import com.google.gson.Gson;
import webservice.TrainInfoData;
import webservice.TrainTicketInquire;
import webservice.TrainTicketRealTimeData;
import webservice.TrainToFromData;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oliver on 2017/4/9.
 */
public class TrainClient {

    /**
     * Text client reads requests line by line from stdandart input.
     */
    public static String INPUT_PROMPT="> ";
    private QueryUnderstand queryUnderstand;
    private DialogManagement dialogManagement;
    private Map<String,String> nlgneed;
    private List<String> result;

  public static void main(String args[])
  {
      String line="";
      QueryUnderstand queryUnderstand = new QueryUnderstand();
      DialogManagement dialogManagement;
      NLUResult nluResult;
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
          System.out.print(INPUT_PROMPT);
          while (null != (line = reader.readLine())) {
              nluResult =queryUnderstand.nluProcess("xiaopiao","o20",line);
              System.out.println(new Gson().toJson(nluResult.getSlotlist()));
              dialogManagement = new DialogManagement();
              dialogManagement.dialogManageProcess(nluResult);
              System.err.println(dialogManagement.getActionname());
              printResult(nluResult);
              System.out.print(INPUT_PROMPT);
          }
      } catch (IOException ex) {
          ex.printStackTrace();
      }
      System.out.println("See ya!");
  }
  public void run(String userid,String query)
  {
      //NLU 模块
      RequestNLU requestNLU = new RequestNLU();
      requestNLU.setQuery(query);
      requestNLU.setUserid(userid);
      queryUnderstand = new QueryUnderstand();
      NLUResult nluResult=null;
      nluResult = queryUnderstand.nluProcess(requestNLU);
      System.out.println(new Gson().toJson(nluResult.getSlotlist()));

      // DM 动作管理模块
      dialogManagement = new DialogManagement();
      dialogManagement.dialogManageProcess(nluResult);
      DMResultBean dmResultBean  = dialogManagement.getDmResultBean();

      //逻辑适配模块
      String actiontype = dmResultBean.getActiontype();
      SchemaConifg schemaConifg = new SchemaConifg();
      schemaConifg.setDefaultValue(requestNLU);
      schemaConifg.filledSchema(dmResultBean);
      printparams(schemaConifg);
      nlgneed = new HashMap<String,String>();
      trainLogicMatch(schemaConifg,dmResultBean);
      //NLG 自然语言生成
      NLGenerateFactory nlGenerateFactory = new NLGenerateFactory(nlgneed,schemaConifg);
      nlGenerateFactory.nlgProcess();
      result = nlGenerateFactory.getNlgresult();
      for(int i=0;i<result.size();i++)
      {
          System.out.println("result"+result.get(i));
      }

  }

  public Map trainLogicMatch(SchemaConifg schemaConifg,DMResultBean dmResultBean)
  {
      if (schemaConifg.getActiontype().equals("satisfy"))
      {
          //临时补救

          if(schemaConifg.getIntent().equals("TRAINPRICE")&&!dmResultBean.getSlotlist().containsKey("user_seattype"))
          {
              schemaConifg.setIntent("Clarify");
              nlgneed.put("type","clarify_seattype");
          }
          else
          {
              filterdataByIntent(schemaConifg,dmResultBean);
          }



          //System.out.println(schemaConifg.getStartpoint().getValue()+schemaConifg.getArrivepoint().getValue());
      }
      else if(schemaConifg.getActiontype().equals("clarify"))
      {

          System.out.println("澄清："+schemaConifg.getActionname());
          schemaConifg.setIntent("Clarify");
          nlgneed.put("type",schemaConifg.getActionname());
      }
      else
      {
          System.out.println("原来:"+schemaConifg.getIntent());
          schemaConifg.setIntent("Fallback");
          nlgneed.put("type","fail");

      }
      return nlgneed;

  }


  public void filterdataByIntent(SchemaConifg schemaConifg,DMResultBean dmResultBean)
  {
      TrainTicketInquire trainTicketInquire = new TrainTicketInquire();
      MakeNlgneed makeNlgneed = new MakeNlgneed(schemaConifg);
      TrainDataRedis getTrainDataRedis = new TrainDataRedis(schemaConifg);
      if(schemaConifg.getIntent().equals("TRAINLIST")||schemaConifg.getIntent().equals("TRAINPRICE")||schemaConifg.getIntent().equals("TRAINTIMECOST"))
      {


          List<TrainToFromData> trainToFromDataList=getTrainDataRedis.getTrainListData();
          if(trainToFromDataList==null)
          {
              trainToFromDataList = trainTicketInquire.getTrainList(schemaConifg.getStartpoint().getValue(),schemaConifg.getArrivepoint().getValue());
              if(trainToFromDataList!=null)
              {
                  getTrainDataRedis.setTrainList(trainToFromDataList);

              }

          }


          if(trainToFromDataList!=null)
          {
              nlgneed.put("type","satisfy");
              FilterTrainList filterTrainList = new FilterTrainList(schemaConifg,dmResultBean.getSlotlist(),nlgneed);
              filterTrainList.filterTrainList(trainToFromDataList);
              trainToFromDataList = filterTrainList.getTrainToFromDatalist();

              nlgneed = filterTrainList.getNlgneed();

              if(trainToFromDataList.size()>0)
              {
                  makeNlgneed.makeByIntentWithTrainList(nlgneed,trainToFromDataList);
              }
          }
          else
          {
              nlgneed.put("type","nodata");
          }

      }
      else if(schemaConifg.getIntent().equals("TRAININFO")||schemaConifg.getIntent().equals("TRAINTIMESTART")||schemaConifg.getIntent().equals("TRAINTIMEARRIVE"))
      {
          //Reids 访问过程
          TrainInfoData trainInfoData = getTrainDataRedis.getTrainInfo();
          if(trainInfoData==null)
          {
              trainInfoData = trainTicketInquire.getTrainInfo(schemaConifg.getTrainname().getValue());
              if(trainInfoData!=null)
              getTrainDataRedis.setTrainInfo(trainInfoData);
          }
          if(trainInfoData!=null)
          {
              nlgneed.put("type","satisfy");
              nlgneed =makeNlgneed.makeByIntentWithTrainInfo(nlgneed,trainInfoData);

          }
          else
          {
              nlgneed.put("type","nodata");
          }

      }
      else if(schemaConifg.getIntent().equals("TRAINTICKET"))
      {
          List <TrainTicketRealTimeData> trainTicketRealTimeDataList = getTrainDataRedis.getTrainTicketData();
          if(trainTicketRealTimeDataList==null)
          {
              GlobalData globalData = new GlobalData();
              globalData.LOADTRAINCODE();
              System.err.println("a1");
              String startcode = globalData.traincode.get(schemaConifg.getStartpoint().getValue());
              String arrivecode = globalData.traincode.get(schemaConifg.getArrivepoint().getValue());
              System.out.println("startcode:"+startcode);
              System.out.println("arrivecode:"+arrivecode);
              if(globalData.traincode.containsKey(schemaConifg.getStartpoint().getValue())&&globalData.traincode.containsKey(schemaConifg.getArrivepoint().getValue()))
              {
                  System.err.println("a2");
                  startcode = globalData.traincode.get(schemaConifg.getStartpoint().getValue());
                  arrivecode = globalData.traincode.get(schemaConifg.getArrivepoint().getValue());
                  System.out.println("startcode1:"+startcode);
                  System.out.println("arrivecode2:"+arrivecode);
                  trainTicketRealTimeDataList = trainTicketInquire.getTrainTicketRealTime(startcode,arrivecode,schemaConifg.getStarttime().getDate(),"");

              }
              else
              {
                  System.err.println("a3");
                  nlgneed.put("type","wrongstation");

              }
              if(trainTicketRealTimeDataList!=null)
              {
                  getTrainDataRedis.setTrainTicket(trainTicketRealTimeDataList);
              }
          }

          if(trainTicketRealTimeDataList!=null)
          {
              System.err.println("a5");
              nlgneed.put("type","satisfy");
              FilterTrainTicket filterTrainTicket = new FilterTrainTicket(schemaConifg,dmResultBean.getSlotlist(),nlgneed);
              nlgneed = filterTrainTicket.filterTrainTicket(trainTicketRealTimeDataList);
              trainTicketRealTimeDataList = filterTrainTicket.getTrainTicketRealTimeDataList();
              if(trainTicketRealTimeDataList.size()==0)
              {
                  nlgneed.put("type","notmatch");
                  System.err.println("a6");
              }
              else
              {
                  nlgneed = makeNlgneed.makeTrainTicketNlgNeed(trainTicketRealTimeDataList,nlgneed,schemaConifg);
                  System.err.println("a7");
              }
              //nlgneed =makeNlgneed.makeByIntentWithTrainInfo(nlgneed,trainTicketRealTimeDataList);

          }
          else
          {
              //这里作为临时修补
              System.err.println("a8");
              if(nlgneed.containsKey("type"))
              {
                  if(!nlgneed.get("type").equals("wrongstation"))
                  {
                      nlgneed.put("type","nodata");
                  }
              }
              else
              {
                  System.err.println("a9");
                  nlgneed.put("type","nodata");
              }

          }


      }
      else
      {
          nlgneed.put("type","satisfy");
      }
  }


    public static void printparams(SchemaConifg schemaConifg) {
        //System.out.println("intent:"+nluResult.getIntent());
        //System.out.println("userid："+nluResult.getUserid());
        System.err.println("1:"+schemaConifg.getIntent());
        System.err.println("2:"+schemaConifg.getActionname());
        System.err.println("3:"+schemaConifg.getPasscity().getValue()+schemaConifg.getPasscity().getOffset());
        System.err.println("4:"+schemaConifg.getSeattype().getValue()+schemaConifg.getSeattype().getOffset());
        System.err.println("5:"+schemaConifg.getStartpoint().getValue()+schemaConifg.getStartpoint().getOffset());
        System.err.println("6:"+schemaConifg.getArrivepoint().getValue()+schemaConifg.getArrivepoint().getOffset());
        System.err.println("7:"+schemaConifg.getTrainname().getValue()+schemaConifg.getTrainname().getOffset());
        System.err.println("8:"+schemaConifg.getStarttime().getTime()+schemaConifg.getStarttime().getOffset());
        System.err.println("9:"+schemaConifg.getStarttime().getDate()+schemaConifg.getStarttime().getOffset());
        System.err.println("10:"+schemaConifg.getTraintype().getValue()+schemaConifg.getTraintype().getOffset());
        System.err.println("1:"+schemaConifg.getArrivetime().getDate()+schemaConifg.getArrivetime().getOffset());
        System.err.println("12:"+schemaConifg.getArrivetime().getTime()+schemaConifg.getArrivetime().getOffset());

    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public static void printResult(NLUResult nluResult)
  {
      //System.out.println("intent:"+nluResult.getIntent());
      //System.out.println("userid："+nluResult.getUserid());
        System.err.println("slot"+nluResult.getSlotlist().toString());
        /*
      if(nluResult.getSlotlist().containsKey("user_arrivepoint"))
      {
          if (nluResult.getSlotlist().get("user_arrivepoint") instanceof JsonNull)
          {
              System.out.println("null");
          }
          else if(nluResult.getSlotlist().get("user_arrivepoint") instanceof JsonArray)
          {
              JsonArray slot_arry = nluResult.getSlotlist().get("user_arrivepoint").getAsJsonArray();
              int i=0;
              //slot_arry.size()
              for(JsonElement element:slot_arry)
              {
                  i=i+1;
              }
              if(i==0)
              {
                  System.out.println("list&空");
              }



          }
          System.out.println("slots："+nluResult.getSlotlist().get("user_arrivepoint"));
      }
      */



  }


}


