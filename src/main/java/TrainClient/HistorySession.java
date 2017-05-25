package TrainClient;

/**
 * Created by oliver on 2017/5/23.
 */
public class HistorySession {
    private int sessionid;
    private String finalresultlist ;
    //APP名称
    private String appname = "xiaopiao";
    //更新时间
    private String updatetime;
    //用户名
    private String userid;
    //总轮数
    private int round=1;
    private int state=1;//表示可用

    public int getSessionid() {
        return sessionid;
    }

    public String getFinalresultlist() {
        return finalresultlist;
    }

    public void setFinalresultlist(String finalresultlist) {
        this.finalresultlist = finalresultlist;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
