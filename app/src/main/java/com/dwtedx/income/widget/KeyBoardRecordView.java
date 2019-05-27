/**
 * @Title: CommonTitle.java
 * @Package com.meineke.auto11.base.widget
 * @author heli
 * @date 2014年9月12日 下午3:15:45
 * @version V1.0
 */

package com.dwtedx.income.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwtedx.income.R;

import java.lang.ref.WeakReference;

/**
 * @author heli
 * @ClassName: CommonTitle
 * @Description: 自定义程序通用title，并实现左右按钮点击事件
 * @date 2014年9月12日 下午3:15:45
 */
public class KeyBoardRecordView extends RelativeLayout implements OnClickListener {

    private WeakReference<OnKeyBoardClickListener> mListener;

    public KeyBoardRecordView(Context context) {
        super(context);
    }

    public KeyBoardRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.key_board_record_view, this, true);

    }

    @Override
    public void onClick(View view) {



        if (null != mListener && null != mListener.get()) {
            mListener.get().OnKeyBoardClick(view);
        }
    }

    public void setOnTitleClickListener(OnKeyBoardClickListener listener) {
        mListener = new WeakReference<OnKeyBoardClickListener>(listener);
    }

    public interface OnKeyBoardClickListener {
        abstract void OnKeyBoardClick(View view);
    }

}
