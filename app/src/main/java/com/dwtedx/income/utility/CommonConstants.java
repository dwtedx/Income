/**
 * @Title: CommonConstant.java
 * @Package com.meineke.auto11.base
 * @author qinyl
 * @date 2014年12月4日 上午9:51:12
 * @version V1.0
 */
package com.dwtedx.income.utility;

import android.graphics.Color;
import android.os.Environment;

import com.dwtedx.income.R;

/**
 * @author qinyl
 * @ClassName: CommonConstant
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2014年12月4日 上午9:51:13
 */
public class CommonConstants {

    public final static String INCOME_TAG = "Income_Logcat_Tag";

    public final static int ClientType = 1;//1:Android   2:Android Pad    3:iPhone    4:iPad

    public final static int PAGE_LENGTH_NUMBER = 20;

    public final static String INCOME = Environment.getExternalStorageDirectory() + "/Income";
    public final static String INCOME_IMAGES = Environment.getExternalStorageDirectory() + "/Income/Images";
    public final static String INCOME_TEMP = Environment.getExternalStorageDirectory() + "/Income/Temp";
    public final static String INCOME_DOWN = Environment.getExternalStorageDirectory() + "/Income/Download";
    public final static String INCOME_VIDEO = Environment.getExternalStorageDirectory() + "/Income/Video";

    public final static String INCOME_APP_NAME = "Income";

    public final static int INCOME_ROLE_ALL = 0;//全部
    public final static int INCOME_ROLE_INCOME = 1;//收入
    public final static int INCOME_ROLE_PAYING = 2;//支出
    public final static int INCOME_ROLE_START = 3;//开始节点
    public final static int INCOME_ROLE_POOL = 4;//开始节点（预算）
    public final static int INCOME_ROLE_ADD_TYPE = 5;//自定义添加类型节点

    public final static int INCOME_RECORD_NOT_UPDATE = 1;//未上传
    public final static int INCOME_RECORD_UPDATEED = 2;//已上传

    public final static int OTHER_LOGIN_WEIXIN = 1;
    public final static int OTHER_LOGIN_QQ = 2;
    public final static int OTHER_LOGIN_SINA = 3;

    public final static String INCOME_ROLE_ADD_TYPE_ICON = "icon_zhichu_shouru_type_add.png";

//    public final static int INCOME_START_YEAR = 2016;
//    public final static int INCOME_START_MONTH = 0;
//    public final static int INCOME_START_DAY = 1;

    //账户相关
    public final static int INCOME_ACCOUNT_TYPE_SYS = 1;
    public final static int INCOME_ACCOUNT_TYPE_USER = 2;
    public final static String INCOME_ACCOUNT_TYPE_ADD = "account_diy.png";

    //收入自定义类型颜色
    public final static String INCOME_DIY_COLOR = "195,187,71";
    public final static String INCOME_DIY_ICON = "icon_shouru_type_diy.png";
    //支出自定义类型颜色
    public final static String PAYTYPE_DIY_COLOR = "54,151,208";
    public final static String PAYTYPE_DIY_ICON = "icon_zhichu_type_diy.png";
    //自定义类型报表颜色数组
    public static final String[] INCOME_PAYTYPE_DIY_ICON_COLOR = {"255,140,157","140,234,255","255,208,140","255,247,140",
            "192,255,140","179,100,53","106,150,31","245,199,0","255,102,0","193,37,82","179,48,80","191,134,134","217,184,162",
            "149,165,124","64,89,128","53,194,209","106,167,134","254,247,120","254,149,7","217,80,138","42,109,130","118,174,175",
            "136,180,187","148,212,212","207,248,246"};


    //帐户常量
    public static final String[] ACCOUNT_NAME = {"现金", "微信", "支付宝", "信用卡", "储蓄卡"};
    public static final String[] ACCOUNT_ICON = {"account_money.png", "account_weixin.png", "account_alipay.png", "account_credit.png", "account_creditxu.png",};
    public static final String[] ACCOUNT_ICON_COLOR = {"255,127,0", "0,207,12", "0,170,238", "42,39,38", "111,170,112"};


    //支出常量
    public static final String[] PAYTYPE_NAME = {"一般", "餐饮", "交通", "酒水饮料", "水果", "零食", "买菜", "生活用品", "电影", "娱乐", "淘宝", "旅游", "网购", "红包", "书籍", "居家",
            "衣服鞋包", "话费充值", "借出", "护肤彩妆", "美容健身", "房租房贷", "人情送礼", "药品" };
    public static final String[] PAYTYPE_ICON = {"icon_zhichu_type_yiban.png", "icon_zhichu_type_canyin.png", "icon_zhichu_type_jiaotong.png",
            "icon_zhichu_type_yanjiuyinliao.png", "icon_zhichu_type_shuiguolingshi.png", "icon_zhichu_type_lingshi.png",
            "icon_zhichu_type_maicai.png", "icon_zhichu_type_shenghuoyongpin.png", "icon_zhichu_type_dianying.png", "icon_zhichu_type_yule.png",
            "icon_zhichu_type_taobao.png", "icon_zhichu_type_lvyou.png", "icon_zhichu_type_wanggou.png", "icon_shouru_type_hongbao.png",
            "icon_zhichu_type_shuji.png", "icon_zhichu_type_jujia.png", "icon_zhichu_type_yifu.png",
            "icon_zhichu_type_shoujitongxun.png", "icon_zhichu_type_jiechu.png", "icon_zhichu_type_hufucaizhuang.png",
            "icon_zhichu_type_meirongjianshen.png", "icon_zhichu_type_fangzu.png", "icon_zhichu_type_renqingsongli.png",
            "icon_zhichu_type_yaopinfei.png" };
    public static final String[] PAYTYPE_ICON_COLOR = {"255,127,0","181,186,62","243,156,18","255,107,107","111,170,112","239,65,112",
           "93,191,146","9,172,233","146,101,80","214,168,52","221,96,50","93,93,93","231,192,62","224,91,39",
           "158,120,102","179,106,102","253,85,125","180,133,176","216,67,103","211,106,168","226,114,142",
           "173,35,75","219,167,188","213,48,98"};


    //收入常量
    public static final String[] INCOMETYPE_NAME = {"工资", "生活费", "红包", "零花钱", "外快兼职", "投资收入", "奖金", "报销", "现金", "退款", "支付宝", "借入", "其他"};
    public static final String[] INCOMETYPE_ICON = {"icon_shouru_type_gongzi.png", "icon_shouru_type_shenghuofei.png", "icon_shouru_type_hongbao.png",
            "icon_shouru_type_linghuaqian.png", "icon_shouru_type_jianzhiwaikuai.png", "icon_shouru_type_touzishouru.png", "icon_shouru_type_jiangjin.png",
            "icon_shouru_type_baoxiaozhang.png", "icon_shouru_type_xianjin.png", "icon_shouru_type_tuikuan.png", "icon_shouru_type_zhifubao.png",
            "icon_shouru_type_jieru.png", "icon_shouru_type_qita.png"};
    public static final String[] INCOMETYPE_ICON_COLOR = {"107,131,183","209,185,93","224,91,39","136,145,116","95,176,197","204,102,3",
           "237,146,65","102,102,170","190,118,118","187,73,197","45,192,252","181,163,83","71,167,230"};

    //自定义类型常量
    public static final String[] INCOME_TYPE_DIY_ICON = {"icon_zhichu_type_diy.png", "icon_shouru_type_diy.png", "icon_shouru_type_diy_caipiao.png",
            "icon_shouru_type_diy_qingxi.png", "icon_shouru_type_diy_aixing.png", "icon_shouru_type_diy_party.png", "icon_shouru_type_diy_maibao.png",
            "icon_shouru_type_diy__shuiguolingshi.png", "icon_shouru_type_diy_xizao.png", "icon_shouru_type_diy_canyin.png", "icon_shouru_type_diy_fuwu.png",
            "icon_shouru_type_diy_chuanpiao.png", "icon_shouru_type_diy_chashuikafei.png", "icon_shouru_type_diy_huochepiao.png", "icon_shouru_type_diy_xuefei.png",
            "icon_shouru_type_diy_jiaotongchuxing.png", "icon_shouru_type_diy_baidan.png", "icon_shouru_type_diy_youxi.png", "icon_shouru_type_diy_lifa.png",
            "icon_shouru_type_diy_richangyongpin.png", "icon_shouru_type_diy_xiaochi.png", "icon_shouru_type_diy_dapai.png", "icon_shouru_type_diy_weixiubaoyang.png",
            "icon_shouru_type_diy_ad.png", "icon_shouru_type_diy_baobao.png", "icon_shouru_type_diy_lingshixiaochi.png", "icon_shouru_type_diy_baoxian.png",
            "icon_shouru_type_diy_daoyou.png", "icon_shouru_type_diy_naifen.png", "icon_shouru_type_diy_mao.png", "icon_shouru_type_diy_xiezi.png",
            "icon_shouru_type_diy_gouwu.png", "icon_shouru_type_diy_wanggou.png", "icon_shouru_type_diy_youfei.png", "icon_shouru_type_diy_shichanghuodong.png",
            "icon_shouru_type_diy_wanju.png", "icon_shouru_type_diy_dianfei.png", "icon_shouru_type_diy_anjie.png", "icon_shouru_type_diy_fangdai.png",
            "icon_shouru_type_diy_gonggongqiche.png", "icon_shouru_type_diy_jiayou.png", "icon_shouru_type_diy_yiwaiposun.png", "icon_shouru_type_diy_baoxiao.png",
            "icon_shouru_type_diy_jiehun.png", "icon_shouru_type_diy_fangzu.png", "icon_shouru_type_diy_maibaobao.png", "icon_shouru_type_diy_wanfan.png",
            "icon_shouru_type_diy_changge.png", "icon_shouru_type_diy_majiang.png", "icon_shouru_type_diy_feijipiao.png", "icon_shouru_type_diy_yifu.png",
            "icon_shouru_type_diy_baojian.png", "icon_shouru_type_diy_fanka.png", "icon_shouru_type_diy_huankuan.png", "icon_shouru_type_diy_quxian.png",
            "icon_shouru_type_diy_huazhuangpin.png", "icon_shouru_type_diy_keige.png", "icon_shouru_type_diy_goumai.png", "icon_shouru_type_diy_yan.png",
            "icon_shouru_type_diy_xiaojingjiazhang.png", "icon_shouru_type_diy_yinhangshouxufei.png", "icon_shouru_type_diy_yuebiangeng.png",
            "icon_shouru_type_diy_daikuan.png", "icon_shouru_type_diy_wuye.png", "icon_shouru_type_diy_chongwu.png", "icon_shouru_type_diy_lingshi.png",
            "icon_shouru_type_diy_naizui.png", "icon_shouru_type_diy_jiaozi.png", "icon_shouru_type_diy_lvyoudujia.png", "icon_shouru_type_diy_haiwaidaigou.png",
            "icon_shouru_type_diy_huwaishebei.png", "icon_shouru_type_diy_maicai.png", "icon_shouru_type_diy_tuikuan.png", "icon_shouru_type_diy_yingbi.png",
            "icon_shouru_type_diy_wangfei.png"};
    public static final String[] INCOME_TYPE_DIY_ICON_COLOR = {"54,151,208", "195,187,71", "255,107,107", "191,178,190", "255,153,153", "255,144,178",
            "255,154,182", "245,218,109", "139,207,255", "254,200,123", "182,202,158", "132,239,233", "235,181,164", "157,199,228", "214,218,106",
            "162,182,255", "238,187,150", "190,191,235", "255,184,207", "192,186,235", "218,201,247", "141,160,206", "171,218,240", "159,156,193", "177,153,148",
            "255,181,136", "225,164,223", "198,224,218", "245,166,169", "153,229,199", "253,194,154", "165,220,170", "247,182,255", "178,201,180", "229,98,116",
            "155,213,237", "142,212,166", "162,202,159", "198,187,216", "153,219,174", "128,199,195", "246,200,201", "176,213,202", "223,60,60", "144,186,223",
            "143,178,236", "242,207,148", "216,191,235", "113,205,155", "186,172,212", "199,173,245", "149,231,243", "231,198,177", "246,174,199", "128,228,158",
            "240,168,212", "240,216,120", "97,210,214", "165,180,219", "147,144,222", "239,189,145", "255,198,50", "38,182,223", "204,227,175", "161,178,165",
            "180,224,129", "232,128,162", "192,171,88", "128,234,219", "244,159,139", "200,204,206", "153,222,211", "154,220,184", "107,131,183", "190,168,215"};

    //淘宝产品类型
    public final static int TAOBAO_TYPE_TIANMAO = 1;
    public final static int TAOBAO_TYPE_TAOBAO = 0;

    public final static int DELETEFALAG_NOTDELETE = 0;
    public final static int DELETEFALAG_DELETEED = 1;

    public final static int INCOME_RECORD_TYPE_0 = 0;//普通记账
    public final static int INCOME_RECORD_TYPE_1 = 1;//扫单

    public final static int INCOME_SCAN_ADDBUTTON = 1;//扫单的添加按钮

    //1：话题  2：投票
    public final static int TOPIC_TYPE_TALK = 1;
    public final static int TOPIC_TYPE_VOTE = 2;

    //0:审核中 1:上线
    public final static int APP_VERSION_AUDIT_DEF = 0;
    public final static int APP_VERSION_AUDIT_PASS = 1;

    //0:新建 1:导出中 2:导出完成
    public static final int STATUS_EXPNEW = 0;
    public static final int STATUS_EXPING = 1;
    public static final int STATUS_EXPDONE = 2;
    public static final int STATUS_EXPERR = 3;

}
