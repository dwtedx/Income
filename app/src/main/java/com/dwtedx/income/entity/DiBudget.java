package com.dwtedx.income.entity;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/14.
 * Company 路之遥网络科技有限公司
 * Description DiIncome
 */
public class DiBudget {
    private int id;
    private double moneysum;
    private double moneylast;
    private String username;
    private int userid;
    private int yearnom;
    private int monthnom;
    private String remark;
    private String createtime;
    private String updatetime;
    private int isupdate;

    public DiBudget() {
    }

    public DiBudget(int id, double moneysum, double moneylast, String username, int userid, int yearnom, int monthnom, String remark, String createtime, String updatetime, int isupdate) {
        this.id = id;
        this.moneysum = moneysum;
        this.moneylast = moneylast;
        this.username = username;
        this.userid = userid;
        this.yearnom = yearnom;
        this.monthnom = monthnom;
        this.remark = remark;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.isupdate = isupdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoneysum() {
        return moneysum;
    }

    public void setMoneysum(double moneysum) {
        this.moneysum = moneysum;
    }

    public double getMoneylast() {
        return moneylast;
    }

    public void setMoneylast(double moneylast) {
        this.moneylast = moneylast;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getYearnom() {
        return yearnom;
    }

    public void setYearnom(int yearnom) {
        this.yearnom = yearnom;
    }

    public int getMonthnom() {
        return monthnom;
    }

    public void setMonthnom(int monthnom) {
        this.monthnom = monthnom;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(int isupdate) {
        this.isupdate = isupdate;
    }
}
