package Action;

import DAO.HistoryRecordDAO;
import DialogManagement.DMResultBean;
import TrainClient.FinalResult;
import TrainClient.HistorySession;
import UtilTools.UtilTools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliver on 2017/5/24.
 */
public class HistoryRecordAction {
    private String userid;
    private String appname;
    private HistoryRecordDAO historyRecordDAO;
    public HistoryRecordAction(String userid,String appname)
    {

        this.userid = userid;
        this.appname = appname;
        this.historyRecordDAO = new HistoryRecordDAO();


    }
    public int insertSession(FinalResult finalResult)
    {
         HistorySession historySession =historyRecordDAO.selectFitRecord(userid,appname);
        int result;
         if(historySession==null)
         {
             historySession = new HistorySession();
             makeHistorySession(historySession,finalResult);
             result= historyRecordDAO.insertHistorySession(historySession);
             if(result>0)
             {
                 System.out.println("插入成功！！插入"+result+"条");
             }
             else System.out.println("插入失败！");
         }
         else
         {
            updateHisSession(historySession,finalResult);
            result = historyRecordDAO.updateHistorySession(historySession);

         }
         return result;
    }

    private void updateHisSession(HistorySession historySession,FinalResult finalResult) {
        List<FinalResult> finalResultList = new Gson().fromJson(historySession.getFinalresultlist(),new TypeToken<ArrayList<FinalResult>>(){}.getType());
        if(finalResult!=null)
        {
            finalResultList.add(0,finalResult);
            historySession.setFinalresultlist(new Gson().toJson(finalResultList));
        }
        else {System.err.println("更新历史信息失败");}
        historySession.setRound(historySession.getRound()+1);
        historySession.setUpdatetime(UtilTools.GetCurrentTimeStr());


    }

    private void makeHistorySession(HistorySession historySession,FinalResult finalResult) {
        historySession.setUserid(userid);
        historySession.setAppname(appname);
        List<FinalResult> finalResultList =new ArrayList<>();
        finalResultList.add(finalResult);
        historySession.setFinalresultlist(new Gson().toJson(finalResultList).toString());
        historySession.setUpdatetime(UtilTools.GetCurrentTimeStr());
        historySession.setRound(1);
        historySession.setState(1);

    }
    public void modifyState()
    {
        HistorySession historySession =historyRecordDAO.selectFitRecord(userid,appname);
        if(historySession!=null)
        {
            historyRecordDAO.modifyState(historySession.getSessionid(),0);

        }
        System.out.println("设置历史日志状态成功");
    }


}
