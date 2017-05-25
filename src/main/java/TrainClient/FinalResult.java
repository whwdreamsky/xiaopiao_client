package TrainClient;


import DialogManagement.DMResultBean;

import java.util.List;

/**
 * Created by oliver on 2017/5/21.
 */
public class FinalResult {
    private List<String> resultlist;
    private List<String> patternlist;
    private DMResultBean dmResultBean;
    private String response="";

    public List<String> getResultlist() {
        return resultlist;
    }

    public void setResultlist(List<String> resultlist) {
        this.resultlist = resultlist;
    }

    public List<String> getPatternlist() {
        return patternlist;
    }

    public void setPatternlist(List<String> patternlist) {
        this.patternlist = patternlist;
    }

    public DMResultBean getDmResultBean() {
        return dmResultBean;
    }

    public void setDmResultBean(DMResultBean dmResultBean) {
        this.dmResultBean = dmResultBean;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


}
