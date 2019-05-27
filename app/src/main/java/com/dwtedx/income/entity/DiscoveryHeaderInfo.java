package com.dwtedx.income.entity;

/**
 * Created by dwtedx(qinyl dwtedx.com) on 17/10/19.
 * Company 路之遥网络科技有限公司
 * Description 类用途 (TODO)
 */

public class DiscoveryHeaderInfo {

    private int icon;
    private int title;
    private int desc;
    private int chose;
    private Class classz;

    public DiscoveryHeaderInfo() {
    }

    public DiscoveryHeaderInfo(int icon, int title, int desc, int chose, Class classz) {
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.chose = chose;
        this.classz = classz;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getDesc() {
        return desc;
    }

    public void setDesc(int desc) {
        this.desc = desc;
    }

    public Class getClassz() {
        return classz;
    }

    public void setClassz(Class classz) {
        this.classz = classz;
    }

    public int getChose() {
        return chose;
    }

    public void setChose(int chose) {
        this.chose = chose;
    }
}
