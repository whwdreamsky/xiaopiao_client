package TrainClient;

/**
 * Created by oliver on 2017/4/15.
 */
public class RequestNLU {
    private String userid="oliver";
    private String query="";
    private static String appname="xiaopiao";

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAppname() {
        return appname;
    }

}
