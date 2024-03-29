package com.dwtedx.income.entity;

public class DiTopicvote {
    private int id;

    private int topicid;

    private String name;

    private String path;

    private String remark;

    private int deleteflag;

    private String createtime;

    private int createuser;

    private String updatetime;

    private int updateuser;

    private int personnum;
    private String percent;
    private boolean checked;

    private int addbutton;//添加记录用

    public DiTopicvote() {

    }

    public DiTopicvote(int addbutton) {
        this.addbutton = addbutton;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicid() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid = topicid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public int getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(int deleteflag) {
        this.deleteflag = deleteflag;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getCreateuser() {
        return createuser;
    }

    public void setCreateuser(int createuser) {
        this.createuser = createuser;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(int updateuser) {
        this.updateuser = updateuser;
    }

    public int getPersonnum() {
        return personnum;
    }

    public void setPersonnum(int personnum) {
        this.personnum = personnum;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getAddbutton() {
        return addbutton;
    }

    public void setAddbutton(int addbutton) {
        this.addbutton = addbutton;
    }
}