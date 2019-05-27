package com.dwtedx.income.connect;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.dwtedx.income.R;

/**
 * @Created by dwtedx(qinyl)(http://dwtedx.com) on 15/12/21 上午9:14.
 * @Company 路之遥网络科技有限公司
 * @Description 请求等待动画
 */
public class ProgressDialog extends Dialog {
    public ProgressDialog(Context context, String strMessage) {
        this(context, R.style.custom_progress_dialog, strMessage);
    }

    public ProgressDialog(Context context, int theme, String strMessage) {
        super(context, theme);
        this.setContentView(R.layout.custom_progress_dialog);
        //this.setCancelable(false);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        TextView tvMsg = (TextView) this.findViewById(R.id.prograss_dlg_msg);
        if (tvMsg != null) {
            tvMsg.setVisibility((null != strMessage) ? View.VISIBLE : View.GONE);
            tvMsg.setText(strMessage);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {  
  
        if (!hasFocus) {  
            //dismiss();  
        }  
    }  
}  

