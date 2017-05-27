package DAO;

import Action.HistoryRecordAction;
import DialogManagement.SessionObject;
import NLU.QueryUnderstand;
import TrainClient.HistorySession;
import UtilTools.UtilTools;

import java.util.Date;
import java.util.Random;

/**
 * Created by oliver on 2017/4/12.
 */
public class test {
    public static void  main(String args[])
    {
        HistoryRecordDAO historyRecordDAO = new HistoryRecordDAO();
        HistorySession historySession = new HistorySession();
        historySession.setAppname("weather");
        historySession.setFinalresultlist("fwfqa");
        historySession.setUpdatetime(UtilTools.GetCurrentTimeStr());
        historySession.setUserid("whw");
        //QueryUnderstand



        //int reuslt = historyRecordDAO.insertHistorySession(historySession);
        //System.out.println(reuslt);

        /*
        HistorySession historySession1 = historyRecordDAO.selectFitRecord("whw","weather");
        if(historySession1!=null)
        {
            System.out.println(historySession1.getSessionid());
            historySession1.setFinalresultlist("yeye");
            historyRecordDAO.updateHistorySession(historySession1);
        }

*/
        //HistoryRecordAction historyRecordAction = new HistoryRecordAction(historySession.getUserid(),historySession.getAppname());
        //historyRecordAction.insertSession()

        /*
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        Map<String,String> selectword = new HashMap<String,String>();
        selectword.put("appname","weather");
        selectword.put("userid","ifweo");
        List<SessionObject> sessionObjects =sqlSession.selectList("sessionforbot",selectword);
        System.out.println(sessionObjects.size());
        System.out.println(sessionObjects.get(0).getUserid());
        Logger logger =Logger.getLogger(test.class);
        sqlSession.close();
        logger.debug("debug:123123123");
        logger.error("12323");
        logger.trace("trace");
        */
        /*
        SessionDAO sessionDAO = new  SessionDAO();
        SessionObject sessionObject = sessionDAO.selectSession("weather","oliver");

        if (sessionObject!=null)
        {

            sessionObject.setUpdatetime(new Date().toString());
            sessionObject.setRound(1);
            sessionObject.setDmresultbeanlist("12312321312fwef");
            int i = sessionDAO.updateSession(sessionObject);
            System.err.println(i);

        }
        /*
        else
        {
            System.out.println(sessionObject.getUserid());
        }

*/
       /* SessionDAO sessionDAO = new  SessionDAO();
        int i =sessionDAO.deleteSession("xiaopiao","oliver10");
        System.err.println(i);

        sessionDAO.closeSession();
        */

       /*
        int max=1;
        int min=1;
        for(int i=0;i<20;i++)
        {
            Random random = new Random();
            int s = random.nextInt(max)%(max-min+1) + min;
            System.out.println(s);

        }
        */


    }
}
