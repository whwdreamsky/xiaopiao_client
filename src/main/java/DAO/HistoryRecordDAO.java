package DAO;

import TrainClient.HistorySession;
import UtilTools.UtilTools;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oliver on 2017/5/24.
 */
public class HistoryRecordDAO extends MyBatisAbstractDAO{

    public HistorySession selectFitRecord(String usrid,String appname)
    {
        sqlSession = getSqlSession();
        Map selectword = new HashMap();
        selectword.put("userid",usrid);
        selectword.put("appname",appname);
        selectword.put("state",1);//状态为1 表示可插入
        List<HistorySession> historySessions = null;
        historySessions = sqlSession.selectList("historyselect",selectword);
        closeSqlSession(sqlSession);
        if(historySessions == null || historySessions.size()==0)
        {
            System.out.println("select : 无可插入历史信息");
            return null;
        }
        else
        {
            //处理可能出现的冗余情况，默认只允许一个满足条件的条目
            if(historySessions.size()>1)
            {
                System.out.println("有冗余可插入历史");
                int index = getLatestSession(historySessions);
                batchmodifystate(historySessions,index);
                return historySessions.get(index);
            }
            else return historySessions.get(0);
        }

    }

    //这个用于将冗余的用户session 状态值为不可用，因为同时只允许一个可用historysession
    private void batchmodifystate(List<HistorySession> historySessions,int notinclde)
    {


        for(int i=0;i<historySessions.size();i++)
        {
            if(i!=notinclde)
            {
                //将状态修改为不可添加
                modifyState(historySessions.get(i).getSessionid(),0);
            }
        }
    }



    public int insertHistorySession(HistorySession historySession)
    {
        sqlSession = getSqlSession();
        int result = sqlSession.insert("historyinsert",historySession);
        sqlSession.commit();
        closeSqlSession(sqlSession);
        return result;

    }

    public int updateHistorySession(HistorySession historySession)
    {

        sqlSession = getSqlSession();
        int result = sqlSession.update("historyupdate",historySession);
        sqlSession.commit();
        closeSqlSession(sqlSession);
        return result;

    }

    public int modifyState(int sessionid,int state)
    {
        Map modifyparam = new HashMap();
        modifyparam.put("state",0);
        modifyparam.put("sessionid",sessionid);
        sqlSession = getSqlSession();
        int result =  sqlSession.update("modifystate",modifyparam);
        sqlSession.commit();
        closeSqlSession(sqlSession);
        System.out.println("history: 修改状态成功！！");
        return  result;
    }

    /**
     * 获取时间上最近的一个历史信息
     * @param historySessions
     * @return index
     */

    private int getLatestSession(List<HistorySession> historySessions) {
        int index = 0;
        Date latest = UtilTools.TransferTimestrToDate(historySessions.get(0).getUpdatetime());
        Date temp =null;
        for(int i=1;i<historySessions.size();i++)
        {
            temp = UtilTools.TransferTimestrToDate(historySessions.get(i).getUpdatetime());
            if(latest.before(temp))
            {
                latest = temp;
                index = i;
            }
        }
        return index;
    }


}
