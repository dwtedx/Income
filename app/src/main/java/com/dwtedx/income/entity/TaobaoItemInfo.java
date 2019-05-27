package com.dwtedx.income.entity;

/**
 * Created by dwtedx(qinyl dwtedx.com) on 17/10/11.
 * Company 路之遥网络科技有限公司
 * Description 类用途 (TODO)
 */

public class TaobaoItemInfo {

    /**
     * id : 859
     * itemUrl : http://item.taobao.com/item.htm?id=557785818961
     * nick : 米又服饰旗舰店
     * numIid : 557785818961
     * categoryId : 43
     * pictUrl : http://img.alicdn.com/bao/uploaded/i4/2999302852/TB1nxXPalUSMeJjSszbXXberFXa_!!0-item_pic.jpg
     * provcity : null
     * reservePrice : 0
     * sellerId : 2999302852
     * smallImages : null
     * title : 金丝绒阔腿裤女大码直筒拖地长裤丝绒阔腿裤女显瘦高腰宽松女裤秋
     * userType : 1
     * volume : 688
     * zkFinalPrice : 89.00
     * taokeUrl : https://s.click.taobao.com/t?e=m%3D2%26s%3DF5QFTqlyuDkcQipKwQzePOeEDrYVVa64K7Vc7tFgwiFRAdhuF14FMb5oVZyeo4O25x%2BIUlGKNpUaWhx%2BqGV%2FIbmC%2B6Ir001q6JDiZr6NjSPMzhrgZv%2FStpgurCry9bUAkdW8lPcDy07eXj06usHdElrcBoUc%2FZDMIYULNg46oBA%3D
     * brokeragePro : 10.50
     * brokerage : 9.35
     * storeName : 米又服饰旗舰店
     * coupon : 满80元减50元
     * couponId : c2fb4aff94174577bbf1a24361108f0b
     * couponUrl : https://taoquan.taobao.com/coupon/unify_apply.htm?sellerId=2999302852&activityId=c2fb4aff94174577bbf1a24361108f0b
     * couponUrlTaoke : https://uland.taobao.com/coupon/edetail?e=Udz1yWJl0J0N%2BoQUE6FNzLJ0iUlMmtbCl%2BleiOJe%2Fg82pBqXNLtfmUSYnPwzMcPWrFGZsyT%2BLDfBzFtTi2lwzRpywujSvOp2nUIklpPPqYIqe1oa4xpq6yMzK4wf8DnNgK6x3jgy1EhqPlZbU31kNqiMTbTBY865&pid=mm_115403208_16988206_66306847&af=1
     * couponCount : 10000
     * couponSurplus : 10000
     * couponStart : 2017-10-12
     * couponEnd : 2017-10-17
     * tagContent : null
     * createtime : 1508288991000
     * updatetime : 1508288991000
     */

    private int id;
    private String itemUrl;
    private String nick;
    private String numIid;
    private int categoryId;
    private String pictUrl;
    private Object provcity;
    private String reservePrice;
    private String sellerId;
    private Object smallImages;
    private String title;
    private int userType;
    private int volume;
    private String zkFinalPrice;
    private String taokeUrl;
    private String brokeragePro;
    private String brokerage;
    private String storeName;
    private String coupon;
    private String couponId;
    private String couponUrl;
    private String couponUrlTaoke;
    private int couponCount;
    private int couponSurplus;
    private String couponStart;
    private String couponEnd;
    private Object tagContent;
    private long createtime;
    private long updatetime;

    private String price;//客户端用

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getPictUrl() {
        return pictUrl;
    }

    public void setPictUrl(String pictUrl) {
        this.pictUrl = pictUrl;
    }

    public Object getProvcity() {
        return provcity;
    }

    public void setProvcity(Object provcity) {
        this.provcity = provcity;
    }

    public String getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(String reservePrice) {
        this.reservePrice = reservePrice;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Object getSmallImages() {
        return smallImages;
    }

    public void setSmallImages(Object smallImages) {
        this.smallImages = smallImages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getZkFinalPrice() {
        return zkFinalPrice;
    }

    public void setZkFinalPrice(String zkFinalPrice) {
        this.zkFinalPrice = zkFinalPrice;
    }

    public String getTaokeUrl() {
        return taokeUrl;
    }

    public void setTaokeUrl(String taokeUrl) {
        this.taokeUrl = taokeUrl;
    }

    public String getBrokeragePro() {
        return brokeragePro;
    }

    public void setBrokeragePro(String brokeragePro) {
        this.brokeragePro = brokeragePro;
    }

    public String getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(String brokerage) {
        this.brokerage = brokerage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public String getCouponUrlTaoke() {
        return couponUrlTaoke;
    }

    public void setCouponUrlTaoke(String couponUrlTaoke) {
        this.couponUrlTaoke = couponUrlTaoke;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public int getCouponSurplus() {
        return couponSurplus;
    }

    public void setCouponSurplus(int couponSurplus) {
        this.couponSurplus = couponSurplus;
    }

    public String getCouponStart() {
        return couponStart;
    }

    public void setCouponStart(String couponStart) {
        this.couponStart = couponStart;
    }

    public String getCouponEnd() {
        return couponEnd;
    }

    public void setCouponEnd(String couponEnd) {
        this.couponEnd = couponEnd;
    }

    public Object getTagContent() {
        return tagContent;
    }

    public void setTagContent(Object tagContent) {
        this.tagContent = tagContent;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
