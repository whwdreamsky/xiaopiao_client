package DialogManagement;

/**
 * Created by oliver on 2017/4/9.
 */
public class SessionObject {
    //BOT 结果
    private String sessionid;
    private String dmresultbeanlist;
    //APP名称
    private String appname = "xiaopiao";
    //更新时间
    private String updatetime;
    //用户名
    private String userid;
    //总轮数
    private int round;

    public String getDmresultbeanlist() {
        return dmresultbeanlist;
    }

    public void setDmresultbeanlist(String dmresultbeanlist) {
        this.dmresultbeanlist = dmresultbeanlist;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}
