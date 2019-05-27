package com.dwtedx.income.entity;

public class DiScan {

    private int id;
    private String username;
    private int userid;
    private int incomeid;
    private double moneysum;
    private String name;
    private String store;
    private String brand;
    private int quantity;
    private int type;
    private int sequence;
    private String remark;
    private String createtime;
    private String updatetime;
    private int serverid;
    private int deletefalag;
    private int isupdate;

    private int addbutton;//添加记录用

    public DiScan() {
        this.quantity = 1;
    }

    public DiScan(int addbutton) {
        this.addbutton = addbutton;
    }

    public DiScan(int id, String username, int userid, int incomeid, double moneysum, String name, String store, String brand, int quantity, int type, int sequence, String remark, String createtime, String updatetime, int serverid, int deletefalag, int isupdate) {
        this.id = id;
        this.username = username;
        this.userid = userid;
        this.incomeid = incomeid;
        this.moneysum = moneysum;
        this.name = name;
        this.store = store;
        this.brand = brand;
        this.quantity = quantity;
        this.type = type;
        this.sequence = sequence;
        this.remark = remark;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.serverid = serverid;
        this.deletefalag = deletefalag;
        this.isupdate = isupdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getIncomeid() {
        return incomeid;
    }

    public void setIncomeid(int incomeid) {
        this.incomeid = incomeid;
    }

    public double getMoneysum() {
        return moneysum;
    }

    public void setMoneysum(double moneysum) {
        this.moneysum = moneysum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
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

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public int getDeletefalag() {
        return deletefalag;
    }

    public void setDeletefalag(int deletefalag) {
        this.deletefalag = deletefalag;
    }

    public int getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(int isupdate) {
        this.isupdate = isupdate;
    }

    public int getAddbutton() {
        return addbutton;
    }

    public void setAddbutton(int addbutton) {
        this.addbutton = addbutton;
    }
}
