package com.dwtedx.income.home.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.dwtedx.income.R;
import com.dwtedx.income.addrecord.IncomeDetailActivity;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.scan.ScanDetailActivity;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.dwtedx.income.utility.ParseJsonToObject;
import com.dwtedx.income.widget.riseview.RiseNumberTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DiIncomeLineAdapter extends RecyclerView.Adapter<DiIncomeLineAdapter.DiIncomeMoneyLineViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<DiIncome> mDiIncomeItems;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat formatMMdd = new SimpleDateFormat("MM-dd");

    private DlIncomeService mDlIncomeService;

    private ImageView mDisplayEditView;

    public DiIncomeLineAdapter(Context context, List<DiIncome> mDiIncomeItems, DlIncomeService mDlIncomeService) {
        this.mContext = context;
        this.mDiIncomeItems = mDiIncomeItems;
        this.mDlIncomeService  = mDlIncomeService;
    }

    /**
     * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/9/16 14:33.
     * Company 路之遥网络科技有限公司
     * Description 时间显示
     */
    private String getDate(String date){
        if(CommonUtility.isEmpty(date)){
            return null;
        }
        try {
            String ret = null;
            long create = format.parse(date).getTime();
            Calendar now = Calendar.getInstance();
            long ms  = 1000*(now.get(Calendar.HOUR_OF_DAY)*3600+now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND));//毫秒数
            long ms_now = now.getTimeInMillis();
            if(ms_now-create<ms){
                ret = "今天";
            }else if(ms_now-create<(ms+24*3600*1000)){
                ret = "昨天";
            }else if(ms_now-create<(ms+24*3600*1000*2)){
                ret = "前天";
            }else{
                ret = formatMMdd.format(new Date(create));
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DiIncomeLineAdapter.DiIncomeMoneyLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_income_line_item, parent, false);//填充这个item布局
        DiIncomeMoneyLineViewHolder viewHolder = new DiIncomeMoneyLineViewHolder(itemView);//创建ViewHolder
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DiIncomeMoneyLineViewHolder viewHolder, int position) {
        final DiIncome mDiIncome = mDiIncomeItems.get(position);
        if(CommonConstants.INCOME_ROLE_START != mDiIncome.getRole()) {
            mDiIncome.setRecordtimeformat(getDate(mDiIncome.getRecordtime()));
            DiIncome lastDiIncome;
            try {
                lastDiIncome = mDiIncomeItems.get(position - 1);
            }catch (Exception e){
                lastDiIncome = null;
            }
            if (null == lastDiIncome || !mDiIncome.getRecordtimeformat().equals(lastDiIncome.getRecordtimeformat())) {
                if(CommonConstants.INCOME_ROLE_INCOME == mDiIncome.getRole() ||
                        CommonConstants.INCOME_ROLE_PAYING == mDiIncome.getRole()) {
                    String incomeTime = CommonUtility.stringDateFormart(mDiIncome.getRecordtime(), "yyyy-MM-dd");
                    double incomesum = mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_INCOME, incomeTime + " 00:00:00", incomeTime + " 24:00:00").getMoneysum();
                    double paysum = mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_PAYING, incomeTime + " 00:00:00", incomeTime + " 24:00:00").getMoneysum();
                    if (incomesum > 0) {
                        viewHolder.incomeAllText.setVisibility(View.VISIBLE);
                        viewHolder.incomeAllText.setText(mContext.getString(R.string.record_all_income) + mContext.getString(R.string.rmb) + CommonUtility.twoPlaces(incomesum));
                    } else {
                        viewHolder.incomeAllText.setVisibility(View.GONE);
                    }
                    if (paysum > 0) {
                        viewHolder.pyaAllText.setVisibility(View.VISIBLE);
                        viewHolder.pyaAllText.setText(mContext.getString(R.string.record_all_pay) + mContext.getString(R.string.rmb) + CommonUtility.twoPlaces(paysum));
                    } else {
                        viewHolder.pyaAllText.setVisibility(View.GONE);
                    }
                }
                viewHolder.timelinetimetext.setText(mDiIncome.getRecordtimeformat());
                viewHolder.timeLayoutView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.timelinetimetext.setText("");
                viewHolder.timeLayoutView.setVisibility(View.GONE);
            }
            //记账条目点击事件
            viewHolder.timelineLayout.setOnClickListener(this);
            viewHolder.incomeEditView.setTag(viewHolder.getAdapterPosition());
            viewHolder.incomeEditView.setOnClickListener(this);
        }else{
            viewHolder.timelinetimetext.setText("");
            viewHolder.timeLayoutView.setVisibility(View.GONE);
            //viewHolder.timelineLayout.setOnLongClickListener(null);
        }

        //支出和扫单逻辑相同
        if(CommonConstants.INCOME_ROLE_PAYING == mDiIncome.getRole()) {
            //文字
            //viewHolder.pyaText.setText(mDiIncome.getType()+ " ￥" + CommonUtility.doubleFormat(mDiIncome.getMoneysum()));
            viewHolder.pyaText.setText(mDiIncome.getType()+ mContext.getString(R.string.rmb) + CommonUtility.twoPlaces(mDiIncome.getMoneysum()));
            viewHolder.incomeText.setText("");

            //时光轴图片
            Glide.with(mContext)
                    .load(CommonUtility.getImageIdByName(mContext, null != mDiIncome.getIcon()?mDiIncome.getIcon():CommonConstants.PAYTYPE_DIY_ICON))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(viewHolder.timeline);
            //viewHolder.timeline.setTimelineType(TimelineView.TYPE_MIDDLE);

            //是否第一个
            viewHolder.tipText.setText("");
            viewHolder.tipView.setVisibility(View.GONE);
            viewHolder.timelineLayout.setVisibility(View.VISIBLE);
            viewHolder.pollLayoutView.setVisibility(View.GONE);
        }else if(CommonConstants.INCOME_ROLE_INCOME == mDiIncome.getRole()){
            //文字
            //viewHolder.incomeText.setText(mDiIncome.getType()+ "￥" + CommonUtility.doubleFormat(mDiIncome.getMoneysum()));
            viewHolder.incomeText.setText(mDiIncome.getType()+ mContext.getString(R.string.rmb) + CommonUtility.twoPlaces(mDiIncome.getMoneysum()));
            viewHolder.pyaText.setText("");

            //时光轴图片
            Glide.with(mContext)
                    .load(CommonUtility.getImageIdByName(mContext, null != mDiIncome.getIcon()?mDiIncome.getIcon():CommonConstants.INCOME_DIY_ICON))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(viewHolder.timeline);
            //viewHolder.timeline.setTimelineType(TimelineView.TYPE_MIDDLE);

            //是否第一个
            viewHolder.tipText.setText("");
            viewHolder.tipView.setVisibility(View.GONE);
            viewHolder.timelineLayout.setVisibility(View.VISIBLE);
            viewHolder.pollLayoutView.setVisibility(View.GONE);
        }else if(CommonConstants.INCOME_ROLE_POOL == mDiIncome.getRole()){
            //文字
            viewHolder.incomepolltextView.setText(mContext.getString(R.string.rmb) + mDiIncome.getMoneysumLeft());
            viewHolder.paytextpollView.setText(mContext.getString(R.string.rmb) + mDiIncome.getMoneysumRight());
            viewHolder.incomepollTitletextView.setText(mDiIncome.getRemark() + mContext.getString(R.string.record_income_all));
            viewHolder.paytextpollTitleView.setText(mDiIncome.getRemark() + mContext.getString(R.string.record_pay_all));
            animText(viewHolder.timelinePollText, (float) mDiIncome.getMoneysum());
            //时光轴图片
            //viewHolder.timelinePollTime.setTimelineType(TimelineView.TYPE_MIDDLE);

            //是否第一个
            viewHolder.tipText.setText("");
            viewHolder.tipView.setVisibility(View.GONE);
            viewHolder.timeLayoutView.setVisibility(View.GONE);
            viewHolder.timelineLayout.setVisibility(View.GONE);
            viewHolder.pollLayoutView.setVisibility(View.VISIBLE);
        }else if(CommonConstants.INCOME_ROLE_START == mDiIncome.getRole()){
            //文字
            viewHolder.incomeText.setText("");
            viewHolder.pyaText.setText("");

            //时光轴图片
            Glide.with(mContext).load(R.mipmap.time_line_start).into(viewHolder.timeline);
            //viewHolder.timeline.setTimelineType(TimelineView.TYPE_END);

            //是否第一个
            viewHolder.tipText.setText(mDiIncome.getCreatetime());
            viewHolder.tipView.setVisibility(View.VISIBLE);
            viewHolder.pollLayoutView.setVisibility(View.GONE);
            viewHolder.timelineLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mDiIncomeItems.size();
    }

    /**
     * 给一个TextView设置一个数字增长动画
     */
    public static void animText(RiseNumberTextView tv, float number) {
        // 设置数据
        tv.withNumber(number);
        // 设置动画播放时间
        tv.setDuration(1500);
        tv.start();
    }


    public ImageView getmDisplayEditView() {
        return mDisplayEditView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.income_edit_view:
                incomeDetail(v);
                break;

            case R.id.timeline_layout:
                itemClick(v);
                break;
        }
    }

    private void incomeDetail(View v) {
        int position = ((int) v.getTag());//减1修正
        final DiIncome diIncome = mDiIncomeItems.get(position);
        if(CommonConstants.INCOME_RECORD_TYPE_1 == diIncome.getRecordtype()) {
            Intent intent = new Intent(mContext, ScanDetailActivity.class);
            intent.putExtra("income", ParseJsonToObject.getJsonFromObj(diIncome).toString());
            mContext.startActivity(intent);
        }else{
            Intent intent = new Intent(mContext, IncomeDetailActivity.class);
            intent.putExtra("income", ParseJsonToObject.getJsonFromObj(diIncome).toString());
            mContext.startActivity(intent);
        }
        //添加动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top);
        mDisplayEditView.startAnimation(hyperspaceJumpAnimation);
        mDisplayEditView.setVisibility(View.GONE);
    }

    private void itemClick(View v){
        ImageView incomeEditView = (ImageView) v.findViewById(R.id.income_edit_view);
        //如果有 先隐藏
        if(null != mDisplayEditView && View.VISIBLE == mDisplayEditView.getVisibility()){
            //添加动画
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top);
            mDisplayEditView.startAnimation(hyperspaceJumpAnimation);
            mDisplayEditView.setVisibility(View.GONE);
        }
        //添加动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_top); // 使用AnimationUtils载入并实例化动画
        incomeEditView.startAnimation(hyperspaceJumpAnimation); // startAnimation是View提供的方法, 你也可以用View.setAnimation()指定一个已通过Animation.setStartTime()设定了开始时间的定时动画
        incomeEditView.setVisibility(View.VISIBLE);
        mDisplayEditView = incomeEditView;
    }

    class DiIncomeMoneyLineViewHolder extends RecyclerView.ViewHolder {

        TextView incomeText, pyaText, tipText, timelinetimetext, incomeAllText, pyaAllText, incomepolltextView, paytextpollView, incomepollTitletextView, paytextpollTitleView;
        RiseNumberTextView timelinePollText;
        ImageView timeline;
        LinearLayout tipView;
        RelativeLayout timeLayoutView, timelineLayout, pollLayoutView;
        ImageView incomeEditView;

        public DiIncomeMoneyLineViewHolder(View itemView) {
            super(itemView);
            incomeText = (TextView) itemView.findViewById(R.id.incometextView);
            pyaText = (TextView) itemView.findViewById(R.id.paytextView);
            incomeAllText = (TextView) itemView.findViewById(R.id.incometextallView);
            pyaAllText = (TextView) itemView.findViewById(R.id.paytextallView);
            timeline = (ImageView) itemView.findViewById(R.id.timeline);
            timelinetimetext = (TextView) itemView.findViewById(R.id.timeline_time_text);
            timeLayoutView = (RelativeLayout) itemView.findViewById(R.id.timeline_time_layout);
            tipText = (TextView) itemView.findViewById(R.id.paytextViewTip);
            timelinePollText = (RiseNumberTextView) itemView.findViewById(R.id.timeline_poll_text);
            incomepolltextView = (TextView) itemView.findViewById(R.id.incomepolltextView);
            paytextpollView = (TextView) itemView.findViewById(R.id.paytextpollView);
            incomepollTitletextView = (TextView) itemView.findViewById(R.id.incomepollTitletextView);
            paytextpollTitleView = (TextView) itemView.findViewById(R.id.paytextpollTitleView);
            tipView = (LinearLayout) itemView.findViewById(R.id.paytextViewTip_layout);
            timelineLayout = (RelativeLayout) itemView.findViewById(R.id.timeline_layout);
            pollLayoutView = (RelativeLayout) itemView.findViewById(R.id.timeline_poll_layout);
            incomeEditView = (ImageView) itemView.findViewById(R.id.income_edit_view);
        }
    }
}
