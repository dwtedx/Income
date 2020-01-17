package com.dwtedx.income.entity;

import android.graphics.Rect;
import android.os.Parcel;
import androidx.annotation.Nullable;

import com.previewlibrary.enitity.IThumbViewInfo;

public class DiTopicimg implements IThumbViewInfo {
    private int id;

    private int topicid;

    private String name;

    private String path;

    private int width;

    private int height;

    private String fullpath;

    private String remark;

    private int deleteflag;

    private String createtime;

    private int createuser;

    private String updatetime;

    private int updateuser;

    private String url;  //图片地址
    private Rect mBounds; // 记录坐标
    private String videoUrl;

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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public String getFullpath() {
        return fullpath;
    }

    public void setFullpath(String fullpath) {
        this.fullpath = fullpath;
    }

    @Override
    public String getUrl() {
        return fullpath;
    }

    @Override
    public Rect getBounds() {
        return mBounds;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeParcelable(this.mBounds, flags);
        dest.writeString(this.videoUrl);

        dest.writeInt(this.id);
        dest.writeInt(this.topicid);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.fullpath);
        dest.writeString(this.remark);
        dest.writeInt(this.deleteflag);
        dest.writeString(this.createtime);
        dest.writeInt(this.createuser);
        dest.writeString(this.updatetime);
        dest.writeInt(this.updateuser);

    }

    public DiTopicimg() {
    }

    public DiTopicimg(String url) {
        this.url = url;
    }
    public DiTopicimg(String videoUrl,String url) {
        this.url = url;
        this.videoUrl = videoUrl;
    }
    protected DiTopicimg(Parcel in) {
        this.url = in.readString();
        this.mBounds = in.readParcelable(Rect.class.getClassLoader());
        this.videoUrl = in.readString();

        this.id = in.readInt();
        this.topicid = in.readInt();
        this.name = in.readString();
        this.path = in.readString();
        this.fullpath = in.readString();
        this.remark = in.readString();
        this.deleteflag = in.readInt();
        this.createtime = in.readString();
        this.createuser = in.readInt();
        this.updatetime = in.readString();
        this.updateuser = in.readInt();
    }

    public static final Creator<DiTopicimg> CREATOR = new Creator<DiTopicimg>() {
        @Override
        public DiTopicimg createFromParcel(Parcel source) {
            return new DiTopicimg(source);
        }

        @Override
        public DiTopicimg[] newArray(int size) {
            return new DiTopicimg[size];
        }
    };

    public void setBounds(Rect bounds) {
        this.mBounds = bounds;
    }
}