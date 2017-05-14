package webservice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliver on 2017/4/15.
 */
public class TrainTicketRealTimeData {
    private String train_code;
    private String start_station_name;
    private String end_station_name;
    private String from_station_name;
    private String to_station_name;
    private String start_time;
    private String arrive_time;
    private String run_time;//数字多少分钟，这里注意修改
    //高级软卧
    private String gjrw_num;
    //其他
    private String qtxb_num;
    //软卧
    private String rw_num;
    //软座
    private String rz_num;
    //特等座
    private String tdz_num;
    //无座
    private String wz_num;
    //硬卧
    private String yw_num;
    //硬座
    private String yz_num;
    //二等座
    private String edz_num;
    //一等座
    private String ydz_num;
    //商务座
    private String swz_num;

    private Map ticketmap;
    public String getTrain_code() {
        return train_code;
    }

    public void setTrain_code(String train_code) {
        this.train_code = train_code;
    }

    public String getStart_station_name() {
        return start_station_name;
    }

    public void setStart_station_name(String start_station_name) {
        this.start_station_name = start_station_name;
    }

    public String getEnd_station_name() {
        return end_station_name;
    }

    public void setEnd_station_name(String end_station_name) {
        this.end_station_name = end_station_name;
    }

    public String getFrom_station_name() {
        return from_station_name;
    }

    public void setFrom_station_name(String from_station_name) {
        this.from_station_name = from_station_name;
    }

    public String getTo_station_name() {
        return to_station_name;
    }

    public void setTo_station_name(String to_station_name) {
        this.to_station_name = to_station_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }




    public String getRun_time() {
        return run_time;
    }

    public void setRun_time(String run_time) {
        this.run_time = run_time;
    }

    public String getGjrw_num() {
        return gjrw_num;
    }

    public void setGjrw_num(String gjrw_num) {
        this.gjrw_num = gjrw_num;
    }

    public String getQtxb_num() {
        return qtxb_num;
    }

    public void setQtxb_num(String qtxb_num) {
        this.qtxb_num = qtxb_num;
    }

    public String getRw_num() {
        return rw_num;
    }

    public void setRw_num(String rw_num) {
        this.rw_num = rw_num;
    }

    public String getRz_num() {
        return rz_num;
    }

    public void setRz_num(String rz_num) {
        this.rz_num = rz_num;
    }

    public String getTdz_num() {
        return tdz_num;
    }

    public void setTdz_num(String tdz_num) {
        this.tdz_num = tdz_num;
    }

    public String getWz_num() {
        return wz_num;
    }

    public void setWz_num(String wz_num) {
        this.wz_num = wz_num;
    }

    public String getYw_num() {
        return yw_num;
    }

    public void setYw_num(String yw_num) {
        this.yw_num = yw_num;
    }

    public String getYz_num() {
        return yz_num;
    }

    public void setYz_num(String yz_num) {
        this.yz_num = yz_num;
    }

    public String getEdz_num() {
        return edz_num;
    }

    public void setEdz_num(String edz_num) {
        this.edz_num = edz_num;
    }

    public String getYdz_num() {
        return ydz_num;
    }

    public void setYdz_num(String ydz_num) {
        this.ydz_num = ydz_num;
    }

    public String getSwz_num() {
        return swz_num;
    }

    public void setSwz_num(String swz_num) {
        this.swz_num = swz_num;
    }


    public void setTicketMap()
    {
        ticketmap = new HashMap();

        ticketmap.put("rw_num",getRw_num());
        ticketmap.put("rz_num",getRz_num());
        ticketmap.put("yz_num",getYz_num());
        ticketmap.put("yw_num",getWz_num());
        ticketmap.put("edz_num", getEdz_num());
        ticketmap.put("ydz_num", getYdz_num());
        ticketmap.put("tdz_num", getTdz_num());
        ticketmap.put("gjrw_num", getGjrw_num());
        ticketmap.put("swz_num",getSwz_num());
        ticketmap.put("qtxb_num", getQtxb_num());
        ticketmap.put("wz_num",getWz_num());

    }
    public Map getTicketmap()
    {
        return ticketmap;
    }
}
