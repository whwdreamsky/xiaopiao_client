package NLU;
import TrainClient.RequestNLU;
import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class QueryUnderstand {

  private static final String APIKEY="85e15ecbcd2243db9e931fde7251a7a0";
  private AIConfiguration configuration;
  private AIDataService dataService;
  private String userid;



  public QueryUnderstand()
  {

    configuration = new AIConfiguration(APIKEY,AIConfiguration.SupportedLanguages.ChineseChina);

    dataService = new AIDataService(configuration);
  }


  public NLUResult transferToNLUResult(AIResponse response,String userid,String appname)
  {
    NLUResult nluresult = new NLUResult();
    Result result = response.getResult();
    nluresult.setStatuscode(response.getStatus().getCode());
    nluresult.setStatustype(response.getStatus().getErrorType());
    nluresult.setTimestamp(response.getTimestamp().toString());
    nluresult.setSessionid(response.getSessionId());
    nluresult.setUserid(userid);
    nluresult.setIntent(result.getMetadata().getIntentName());
    nluresult.setQuery(result.getResolvedQuery());
    nluresult.setScore(result.getScore());
    nluresult.setSlotlist(result.getParameters());
    nluresult.setAppname(appname);
    /*
    HashMap<String,JsonElement> params = result.getParameters();
    HashMap<String,String> nluparam = new HashMap<String,String>;
    Iterator iterator = params.keySet().iterator();
    while (iterator.hasNext())
    {
      Object key = iterator.next();
      if (params.get(key).isJsonObject())
      {
        nluparam.put(key.toString(),params.get(key).toString());
      }
      else if (params.get(key).isJsonArray())
      {

      }
    }
    */
    return nluresult;
  }
  //自定义的response 数据结构
  public NLUResult transferToNLUResult_MyResponse(AIAPIResponse response,String userid,String appname)
  {
    NLUResult nluresult = new NLUResult();
    NLU.Result result = response.getResult();
    nluresult.setStatuscode(response.getStatus().getCode());
    nluresult.setStatustype(response.getStatus().getErrorType());
    nluresult.setTimestamp(response.getTimestamp());
    nluresult.setSessionid(response.getSessionId());
    nluresult.setUserid(userid);
    nluresult.setIntent(result.getMetadata().getIntentName());
    nluresult.setQuery(result.getResolvedQuery());
    nluresult.setScore(result.getScore());
    nluresult.setSlotlist(result.getParameters());
    nluresult.setAppname(appname);

    return nluresult;
  }

  // query 预处理, 这里简单解决火车名问题,这个是api.ai 对中文识别有问题
  public String preprocess(String query)
  {
    char[] traintype = {'t','T','d','D','g','G','z','Z','y','Y','k','K','p','P','h','H'};
    String newstr = "";
    for(int i=0;i<query.length();i++)
    {
      for(int j=0;j<traintype.length;j++)
      {
        if(query.charAt(i)==traintype[j])
        {
          newstr=query.substring(0,i+1)+' '+query.substring(i+1,query.length());
          return newstr;
        }
      }
    }
    return query;


  }

/*
  public NLUResult nluProcess (String appname,String userid,String query)
  {

    try{
      query = preprocess(query);
      System.setProperty("https.protocols", "TLSv1.1");
      AIServiceContext aiServiceContext = AIServiceContextBuilder.buildFromSessionId(userid);
      AIRequest request = new AIRequest(query);
      AIAPIResponse response = dataService.request(request,aiServiceContext);
      System.out.println("nluresult : from api.ai"+new Gson().toJson(response));

      if (response.getStatus().getCode() == 200) {

        return transferToNLUResult(response,userid,appname);

      } else {
        System.err.println(response.getStatus().getErrorDetails());
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;

  }
  */
  public NLUResult nluProcess (RequestNLU requestNLU,String reusltapiai)
  {

    if(reusltapiai.equals("")||reusltapiai==null)
    {
      return  nluProcessBySDK(requestNLU);
    }
    //通过js 的方式访问
    else {

      NLUResult nluResult = nluProcessByJs(requestNLU,reusltapiai);
      if(nluResult==null) {
        return nluProcessBySDK(requestNLU);
      }
      else return nluResult;

    }

  }
  public NLUResult nluProcessBySDK(RequestNLU requestNLU)
  {
    System.out.println("访问api的方式是java SDK");
    try{
      System.setProperty("https.protocols", "TLSv1.1");
      //AIServiceContext aiServiceContext = AIServiceContextBuilder.buildFromSessionId(requestNLU.getUserid());
      AIRequest request = new AIRequest(preprocess(requestNLU.getQuery()));

      //AIAPIResponse response = dataService.request(request,aiServiceContext);
      AIResponse response = dataService.request(request);
      System.out.println("nluresult : from api.ai"+new Gson().toJson(response));

      if (response.getStatus().getCode() == 200) {

        return transferToNLUResult(response,requestNLU.getUserid(),requestNLU.getAppname());

      } else {
        System.err.println(response.getStatus().getErrorDetails());
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public NLUResult nluProcessByJs(RequestNLU requestNLU,String reusltapiai)
  {
    System.out.println("访问api的方式是js");
    AIAPIResponse aiResponse;
    try {
       aiResponse =  new Gson().fromJson(reusltapiai, new TypeToken<AIAPIResponse>(){}.getType());
      return  transferToNLUResult_MyResponse(aiResponse,requestNLU.getUserid(),requestNLU.getAppname());

    }
    catch (JsonSyntaxException e)
    {
      System.out.println("JsonSyntaxException"+e.getMessage());
      e.printStackTrace();
    }
    catch (JsonParseException e) {
      System.out.println("JsonParseException"+e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

}
