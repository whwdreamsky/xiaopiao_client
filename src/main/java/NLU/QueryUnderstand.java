package NLU;
import TrainClient.RequestNLU;
import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

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


  public NLUResult nluProcess (String appname,String userid,String query)
  {

    try{
      AIServiceContext aiServiceContext = AIServiceContextBuilder.buildFromSessionId(userid);
      AIRequest request = new AIRequest(query);
      AIResponse response = dataService.request(request,aiServiceContext);

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
  public NLUResult nluProcess (RequestNLU requestNLU)
  {

    try{
      AIServiceContext aiServiceContext = AIServiceContextBuilder.buildFromSessionId(requestNLU.getUserid());
      AIRequest request = new AIRequest(requestNLU.getQuery());

      AIResponse response = dataService.request(request,aiServiceContext);

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

}
