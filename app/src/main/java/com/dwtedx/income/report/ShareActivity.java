package com.dwtedx.income.report;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ShareActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mTitleBackBtn;
    private LinearLayout mTitleRightBtn;
    private LinearLayout mTitleRightShareBtn;
    private DlIncomeService mDlIncomeService;
    private ProgressDialog mProgressDialog;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
    private SimpleDateFormat formatMonthShare = new SimpleDateFormat("yyyy年MM月");
    private SimpleDateFormat formatMonthDesplay = new SimpleDateFormat("dd日");
    private Typeface mTf;

    private NestedScrollView mReportScrollview;

    //时间相关
    private Calendar mStartTimeCalendar;
    private Calendar mEndTimeCalendar;
    private String mStartTime;
    private String mEndTime;

    //支出圆形图片
    private PieChart mPieChart;
    private TextView mPieChartTitle;

    //支出趋势图片
    private LineChart mLineChart;
    private TextView mLineChartTitle;

    //收入圆形图片
    private PieChart mPieIncomeChart;
    private TextView mPieIncomeChartTitle;

    //纯收趋势图片
    private BarChart mBarChart;
    private TextView mBarChartTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        mTitleBackBtn = (LinearLayout)findViewById(R.id.m_title_back_btn);
        mTitleRightBtn = (LinearLayout)findViewById(R.id.m_title_right_btn);
        mTitleRightShareBtn = (LinearLayout)findViewById(R.id.m_title_right_share_btn);
        mTitleBackBtn.setOnClickListener(this);
        mTitleRightBtn.setOnClickListener(this);
        mTitleRightShareBtn.setOnClickListener(this);

        mReportScrollview = (NestedScrollView) findViewById(R.id.report_scrollview);
        mPieChart = (PieChart) findViewById(R.id.pie_chart);
        mLineChart = (LineChart) findViewById(R.id.line_chart);
        mPieIncomeChart = (PieChart) findViewById(R.id.pie_income_chart);
        mBarChart = (BarChart) findViewById(R.id.bar_chart);

        mPieChartTitle = (TextView) findViewById(R.id.pie_chart_lable);
        mLineChartTitle = (TextView) findViewById(R.id.line_chart_lable);
        mPieIncomeChartTitle = (TextView) findViewById(R.id.pie_income_chart_lable);
        mBarChartTitle = (TextView) findViewById(R.id.bar_chart_lable);

        mDlIncomeService = DlIncomeService.getInstance(this);
        mTf = Typeface.createFromAsset(this.getAssets(), "OpenSans-Regular.ttf");

        //设置为1号,当前日期既为本月第一天
        mStartTimeCalendar = Calendar.getInstance();
        if (mStartTimeCalendar.get(Calendar.DAY_OF_MONTH) > 20) {
            mStartTimeCalendar.set(Calendar.DAY_OF_MONTH, 1);
            mStartTime = format.format(mStartTimeCalendar.getTime()) + " 00:00:00";

            //获取当前月最后一天
            mEndTimeCalendar = Calendar.getInstance();
            mEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            mEndTime = format.format(mEndTimeCalendar.getTime()) + " 24:00:00";
        } else {
            mStartTimeCalendar.add(Calendar.MONTH, -1);
            mStartTimeCalendar.set(Calendar.DAY_OF_MONTH, 1);
            mStartTime = format.format(mStartTimeCalendar.getTime()) + " 00:00:00";

            //获取当前月最后一天
            mEndTimeCalendar = Calendar.getInstance();
            mEndTimeCalendar.add(Calendar.MONTH, -1);
            mEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            mEndTime = format.format(mEndTimeCalendar.getTime()) + " 24:00:00";
        }

        showChartInfo();

    }

    private void showChartInfo() {
        //==================圆形图=======开始=============================
        generatePayingDataPie();
        //==================圆形图=======结束=============================

        //==================线性图=======开始=============================
        generateDataLine();
        //==================线性图=======结束=============================

        //==================收入圆形图=======开始=============================
        //generateDataPieIncome();
        //==================收入圆形图=======结束=============================

        //==================纯收入线性图=======开始=============================
        //generateDataBar();
        //==================纯收入线性图=======结束=============================

        String strMonthTime = formatMonthShare.format(mStartTimeCalendar.getTime());
        mPieChartTitle.setText(strMonthTime + getString(R.string.report_paying_pie));
        mLineChartTitle.setText(strMonthTime + getString(R.string.report_paying_line));
        //mPieIncomeChartTitle.setText(strMonthTime + getString(R.string.report_income_pie));
        //mBarChartTitle.setText(strMonthTime + getString(R.string.report_income_bar));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_title_back_btn:
                finish();
                break;
            case R.id.m_title_right_btn:
                Calendar today = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                //设置为1号,当前日期既为本月第一天
                                mStartTimeCalendar.set(selectedYear, selectedMonth, 1);
                                mStartTime = format.format(mStartTimeCalendar.getTime()) + " 00:00:00";

                                //获取当前月最后一天
                                mEndTimeCalendar.set(selectedYear, selectedMonth, 1);
                                mEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                mEndTime = format.format(mEndTimeCalendar.getTime()) + " 24:00:00";

                                showChartInfo();
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setMinYear(1991)
                        .setTitle(getStringFromResources(R.string.report_date_select))
                        .setActivatedMonth(mStartTimeCalendar.get(Calendar.MONTH))
                        .setActivatedYear(mStartTimeCalendar.get(Calendar.YEAR))
                        .build()
                        .show();
                break;
            case R.id.m_title_right_share_btn:
                openShareImage(CommonUtility.getBitmapByView(mReportScrollview));
                break;
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private void generateDataLine() {

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        //计算中间月
        final ArrayList<String> mLineMonthArr = new ArrayList<>();
        int ii = 0;
        Calendar curr = Calendar.getInstance();
        curr.set(mStartTimeCalendar.get(Calendar.YEAR), mStartTimeCalendar.get(Calendar.MONTH), mStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
        while (curr.before(mEndTimeCalendar)) {
            mLineMonthArr.add(formatMonthDesplay.format(curr.getTime()));
            e1.add(new Entry(ii, (float) mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_PAYING, format.format(curr.getTime()) + " 00:00:00", format.format(curr.getTime()) + " 24:00:00").getMoneysum()));
            curr.add(Calendar.DAY_OF_MONTH, 1);
            ii++;
        }

        LineDataSet d1 = new LineDataSet(e1, "支出趋势");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(true);
        d1.setDrawFilled(false);
        d1.setDrawCircles(false);
        d1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<Entry> e2 = new ArrayList<Entry>();
        //计算中间月
        ii = 0;
        curr = Calendar.getInstance();
        curr.set(mStartTimeCalendar.get(Calendar.YEAR), mStartTimeCalendar.get(Calendar.MONTH), mStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
        while (curr.before(mEndTimeCalendar)) {
            //curr.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
            e2.add(new Entry(ii, (float) mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_INCOME, format.format(curr.getTime()) + " 00:00:00", format.format(curr.getTime()) + " 24:00:00").getMoneysum()));
            curr.add(Calendar.DAY_OF_MONTH, 1);
            ii++;
        }

        LineDataSet d2 = new LineDataSet(e2, "收入趋势");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(true);
        d2.setDrawFilled(false);
        d2.setDrawCircles(false);
        d2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);


        // apply styling
        // mLineChart.setValueTypeface(mTf);
        //mLineChart.setDescription("");
        mLineChart.setDrawGridBackground(false);
        mLineChart.getDescription().setEnabled(false);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                try {
                    return mLineMonthArr.get((int) value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return value + "";
            }
        });

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        // set data
        mLineChart.setData((LineData) cd);

        // do not forget to refresh the chart
        // mLineChart.invalidate();
        mLineChart.animateX(750);
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private void generateDataBar() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        //计算中间月
        final ArrayList<String> mBarMonthArr = new ArrayList<>();
        List<Integer> colors = new ArrayList<Integer>();
        int ii = 0;
        double income, paying;
        int green = Color.rgb(110, 190, 102);
        int red = Color.rgb(211, 74, 88);
        Calendar curr = Calendar.getInstance();
        curr.set(mStartTimeCalendar.get(Calendar.YEAR), mStartTimeCalendar.get(Calendar.MONTH), mStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
        while (curr.before(mEndTimeCalendar)) {
            mBarMonthArr.add(formatMonthDesplay.format(curr.getTime()));
            //curr.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
            income = mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_INCOME, format.format(curr.getTime()) + " 00:00:00", format.format(curr.getTime()) + " 24:00:00").getMoneysum();
            paying = mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_PAYING, format.format(curr.getTime()) + " 00:00:00", format.format(curr.getTime()) + " 24:00:00").getMoneysum();
            float nombers = (float) (income - paying);
            entries.add(new BarEntry(ii, nombers));
            if (nombers >= 0)
                colors.add(red);
            else
                colors.add(green);
            curr.add(Calendar.DAY_OF_MONTH, 1);
            ii++;
        }

        mBarChart.setBackgroundColor(Color.WHITE);
        mBarChart.setExtraTopOffset(-30f);
        mBarChart.setExtraBottomOffset(10f);
        mBarChart.setExtraLeftOffset(70f);
        mBarChart.setExtraRightOffset(70f);

        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);

        //mBarChart.setDescription();

        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        mBarChart.setDrawGridBackground(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(5);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                try {
                    if (value < 0) {
                        return "";
                    }
                    return mBarMonthArr.get((int) value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return value + "";
            }
        });

        YAxis left = mBarChart.getAxisLeft();
        left.setDrawLabels(false);
        left.setStartAtZero(false);
        left.setSpaceTop(25f);
        left.setSpaceBottom(25f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.getLegend().setEnabled(false);


        BarDataSet set = new BarDataSet(entries, "Values");
        //set.setBarSpacePercent(40f);
        set.setColors(colors);
        set.setValueTextColors(colors);

        BarData data = new BarData(set);
        data.setValueTextSize(13f);
        data.setValueTypeface(mTf);
        data.setBarWidth(0.8f);
        data.setValueFormatter(new ValueFormatter());
        mBarChart.animateY(700);
        mBarChart.setData(data);
        mBarChart.invalidate();

    }

    /**
     * 支出饼图
     *
     * @return
     */
    private void generatePayingDataPie() {

        if (-1 != CommonUtility.compareDate(mStartTime, mEndTime)) {
            Snackbar.make(mReportScrollview, R.string.report_date_error_ago, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }

        showProgressDialog();
        List<DiIncome> mDiIncomeList = mDlIncomeService.getByTypeSumMoneyData(CommonConstants.INCOME_ROLE_PAYING, mStartTime, mEndTime);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> entriescolors = new ArrayList<>();
        double allPay = 0;
        String showColor;
        int iShowColor = -1;

        for (int i = 0; i < mDiIncomeList.size(); i++) {
            entries.add(new PieEntry((float) mDiIncomeList.get(i).getMoneysum(), mDiIncomeList.get(i).getType()));
            //2019-02-13 自定义类型报表颜色更换
            showColor = CommonConstants.INCOME_DIY_COLOR;
            if (!CommonUtility.isEmpty(mDiIncomeList.get(i).getColor())) {
                if (CommonConstants.PAYTYPE_DIY_COLOR.equals(mDiIncomeList.get(i).getColor())) {
                    if (iShowColor >= 0) {
                        showColor = CommonConstants.INCOME_PAYTYPE_DIY_ICON_COLOR[iShowColor % CommonConstants.INCOME_PAYTYPE_DIY_ICON_COLOR.length];
                    } else {
                        showColor = mDiIncomeList.get(i).getColor();
                    }
                    iShowColor++;
                } else {
                    showColor = mDiIncomeList.get(i).getColor();
                }
            }

            String[] colorArr = showColor.split(",");
            entriescolors.add(Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2])));
            allPay += mDiIncomeList.get(i).getMoneysum();
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        // space between slices
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(entriescolors);

        SpannableString allPayStr = new SpannableString(getString(R.string.record_pay_all) + "\n￥" + CommonUtility.doubleFormat(allPay));
        allPayStr.setSpan(new RelativeSizeSpan(1.6f), 0, 3, 0);
        allPayStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.common_body_tip_colors)), 0, 3, 0);
        allPayStr.setSpan(new RelativeSizeSpan(1.8f), 3, allPayStr.length(), 0);

        PieData cd = new PieData(dataSet);

        // apply styling
        //mPieChart.setDescription("");
        mPieChart.setHoleRadius(52f);
        mPieChart.setTransparentCircleRadius(57f);
        mPieChart.setCenterText(allPayStr);
        mPieChart.setCenterTextSize(105);
        mPieChart.setCenterTextTypeface(mTf);
        mPieChart.setCenterTextSize(9f);
        mPieChart.setUsePercentValues(true);
        mPieChart.setExtraOffsets(5, 0, 40, 0);
        mPieChart.getDescription().setEnabled(false);

        cd.setValueFormatter(new PercentFormatter());
        cd.setValueTypeface(mTf);
        cd.setValueTextSize(11f);
        cd.setValueTextColor(Color.WHITE);
        // set data
        mPieChart.setData((PieData) cd);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // do not forget to refresh the chart
        // mPieChart.invalidate();
        mPieChart.animateY(900);
        cancelProgressDialog();
    }


    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private void generateDataPieIncome() {

        showProgressDialog();
        List<DiIncome> mDiIncomeList = mDlIncomeService.getByTypeSumMoneyData(CommonConstants.INCOME_ROLE_INCOME, mStartTime, mEndTime);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> entriescolors = new ArrayList<>();
        double allPay = 0;

        for (int i = 0; i < mDiIncomeList.size(); i++) {
            entries.add(new PieEntry((float) mDiIncomeList.get(i).getMoneysum(), mDiIncomeList.get(i).getType()));

            String[] colorArr = null != mDiIncomeList.get(i).getColor() ? mDiIncomeList.get(i).getColor().split(",") : CommonConstants.INCOME_DIY_COLOR.split(",");
            entriescolors.add(Color.rgb(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2])));
            allPay += mDiIncomeList.get(i).getMoneysum();
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        // space between slices
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(entriescolors);

        SpannableString allPayStr = new SpannableString(getString(R.string.record_income_all) + "\n￥" + CommonUtility.doubleFormat(allPay));
        allPayStr.setSpan(new RelativeSizeSpan(1.6f), 0, 3, 0);
        allPayStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.common_body_tip_colors)), 0, 3, 0);
        allPayStr.setSpan(new RelativeSizeSpan(1.8f), 3, allPayStr.length(), 0);

        PieData cd = new PieData(dataSet);

        // apply styling
        //mPieIncomeChart.setDescription("");
        mPieIncomeChart.setHoleRadius(52f);
        mPieIncomeChart.setTransparentCircleRadius(57f);
        mPieIncomeChart.setCenterText(allPayStr);
        mPieIncomeChart.setCenterTextSize(105);
        mPieIncomeChart.setCenterTextTypeface(mTf);
        mPieIncomeChart.setCenterTextSize(9f);
        mPieIncomeChart.setUsePercentValues(true);
        mPieIncomeChart.setExtraOffsets(5, 10, 50, 10);
        mPieIncomeChart.getDescription().setEnabled(false);

        cd.setValueFormatter(new PercentFormatter());
        cd.setValueTypeface(mTf);
        cd.setValueTextSize(11f);
        cd.setValueTextColor(Color.WHITE);
        // set data
        mPieIncomeChart.setData((PieData) cd);

        Legend l = mPieIncomeChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // do not forget to refresh the chart
        // mPieIncomeChart.invalidate();
        mPieIncomeChart.animateY(900);
        cancelProgressDialog();
    }

    public void showProgressDialog() {
        mProgressDialog = getProgressDialog();
        mProgressDialog.show();
    }

    public void cancelProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    // 用来配置各个平台的SDKF
    private void openShareImage(final Bitmap bitmap) {
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.ALIPAY, SHARE_MEDIA.EMAIL};

        String shareContent = "我正在使用DD记账，记录生活点点滴滴，这是我的个人帐单。下载地址：http://income.dwtedx.com";
        UMImage uminame = new UMImage(ShareActivity.this, bitmap);
        //UMImage thumb =  new UMImage(ShareActivity.this, CommonUtility.getCompressImage(bitmap));
        //uminame.setThumb(thumb);
        uminame.setDescription(shareContent);
        new ShareAction(ShareActivity.this)
                .setDisplayList(displaylist)
                .withText(shareContent)
                .withMedia(uminame)
                .setCallback(umShareListener)
                .open();

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Toast.makeText(ShareActivity.this, share_media.toString() + "正在分享...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(ShareActivity.this, share_media.toString() + "分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(ShareActivity.this, share_media.toString() + "分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(ShareActivity.this, share_media.toString() + "分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private class ValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public ValueFormatter() {
            mFormat = new DecimalFormat("######.0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }
}
