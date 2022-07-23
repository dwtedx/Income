package com.dwtedx.income.discovery;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.trade.biz.context.AlibcResultType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.dwtedx.income.utility.ToastUtil;

/**
 * Created by fenghaoxiu on 16/8/23.
 */
public class TaobaoTradeCallback implements AlibcTradeCallback {

    @Override
    public void onTradeSuccess(AlibcTradeResult tradeResult) {
        //当addCartPage加购成功和其他page支付成功的时候会回调

        if(tradeResult.resultType.equals(AlibcResultType.TYPECART)){
            //加购成功
            ToastUtil.toastShow("加购成功", ToastUtil.ICON.SUCCESS);
        }else if (tradeResult.resultType.equals(AlibcResultType.TYPEPAY)){
            //支付成功
            ToastUtil.toastShow("支付成功,成功订单号为"+tradeResult.payResult.paySuccessOrders, ToastUtil.ICON.SUCCESS);
        }
    }

    @Override
    public void onFailure(int errCode, String errMsg) {
        ToastUtil.toastShow("电商SDK出错,错误码="+errCode+" / 错误消息="+errMsg, ToastUtil.ICON.WARNING);
    }
}
