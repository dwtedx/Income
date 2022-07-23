package com.dwtedx.income.report;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.dwtedx.income.utility.ToastUtil;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.home.HomeActivity;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class AllReportFragment extends BaseFragment implements View.OnClickListener {

    private DlIncomeService mDlIncomeService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
    private Typeface mTf;

    private ScrollView mReportScrollview;

    //支出圆形图片
    private PieChart mPieChart;
    private TextView mPieStartTimeTextView;
    private TextView mPieEndTimeTextView;
    private String mPieStartTimePie;
    private String mPieEndTimePie;
    private Calendar mPieStartTimeCalendar;
    private Calendar mPieEndTimeCalendar;

    //支出趋势图片
    private LineChart mLineChart;
    private TextView mLineStartTimeTextView;
    private TextView mLineEndTimeTextView;
    private String mLineStartTimeLine;
    private String mLineEndTimeLine;
    private Calendar mLineStartTimeCalendar;
    private Calendar mLineEndTimeCalendar;
    private ArrayList<String> mLineMonthArr;

    //收入圆形图片
    private PieChart mPieIncomeChart;
    private TextView mPieIncomeStartTimeTextView;
    private TextView mPieIncomeEndTimeTextView;
    private String mPieIncomeStartTimePie;
    private String mPieIncomeEndTimePie;
    private Calendar mPieIncomeStartTimeCalendar;
    private Calendar mPieIncomeEndTimeCalendar;

    //纯收趋势图片
    private BarChart mBarChart;
    private TextView mBarStartTimeTextView;
    private TextView mBarEndTimeTextView;
    private String mBarStartTimeBar;
    private String mBarEndTimeBar;
    private Calendar mBarStartTimeCalendar;
    private Calendar mBarEndTimeCalendar;
    private ArrayList<String> mBarMonthArr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_report, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReportScrollview = (ScrollView) view.findViewById(R.id.report_scrollview);

        mPieChart = (PieChart) view.findViewById(R.id.pie_chart);
        mPieStartTimeTextView = (TextView) view.findViewById(R.id.pie_chart_starttime);
        mPieStartTimeTextView.setOnClickListener(this);
        mPieEndTimeTextView = (TextView) view.findViewById(R.id.pie_chart_endtime);
        mPieEndTimeTextView.setOnClickListener(this);

        mLineChart = (LineChart) view.findViewById(R.id.line_chart);
        mLineStartTimeTextView = (TextView) view.findViewById(R.id.line_chart_starttime);
        mLineStartTimeTextView.setOnClickListener(this);
        mLineEndTimeTextView = (TextView) view.findViewById(R.id.line_chart_endtime);
        mLineEndTimeTextView.setOnClickListener(this);

        mPieIncomeChart = (PieChart) view.findViewById(R.id.pie_income_chart);
        mPieIncomeStartTimeTextView = (TextView) view.findViewById(R.id.pie_income_chart_starttime);
        mPieIncomeStartTimeTextView.setOnClickListener(this);
        mPieIncomeEndTimeTextView = (TextView) view.findViewById(R.id.pie_income_chart_endtime);
        mPieIncomeEndTimeTextView.setOnClickListener(this);

        mBarChart = (BarChart) view.findViewById(R.id.bar_chart);
        mBarStartTimeTextView = (TextView) view.findViewById(R.id.bar_chart_starttime);
        mBarStartTimeTextView.setOnClickListener(this);
        mBarEndTimeTextView = (TextView) view.findViewById(R.id.bar_chart_endtime);
        mBarEndTimeTextView.setOnClickListener(this);

        mDlIncomeService = DlIncomeService.getInstance(getContext());

        mTf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public void onResume() {
        super.onResume();

        //==================圆形图=======开始=============================
        //获取当前月第一天：
        mPieStartTimeCalendar = Calendar.getInstance();
        mPieStartTimeCalendar.add(Calendar.DAY_OF_MONTH, -30);
        //设置为1号,当前日期既为本月第一天
        mPieStartTimeTextView.setText(format.format(mPieStartTimeCalendar.getTime()));
        mPieStartTimePie = format.format(mPieStartTimeCalendar.getTime()) + " 00:00:00";
        //获取当前月最后一天
        mPieEndTimeCalendar = Calendar.getInstance();
        mPieEndTimeTextView.setText(format.format(mPieEndTimeCalendar.getTime()));
        mPieEndTimePie = format.format(mPieEndTimeCalendar.getTime()) + " 24:00:00";
        generatePayingDataPie();
        //==================圆形图=======结束=============================

        //==================线性图=======开始=============================
        //获取开始月：
        mLineStartTimeCalendar = Calendar.getInstance();
        mLineStartTimeCalendar.add(Calendar.MONTH, -6);
        //mLineStartTimeCalendar.set(CommonConstants.INCOME_START_YEAR, CommonConstants.INCOME_START_MONTH, CommonConstants.INCOME_START_DAY);
        mLineStartTimeTextView.setText(formatMonth.format(mLineStartTimeCalendar.getTime()));
        mLineStartTimeLine = formatMonth.format(mLineStartTimeCalendar.getTime()) + "-01 00:00:00";
        //获取当前月最后一天
        mLineEndTimeCalendar = Calendar.getInstance();
        mLineEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mLineEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        mLineEndTimeTextView.setText(formatMonth.format(mLineEndTimeCalendar.getTime()));
        mLineEndTimeLine = format.format(mLineEndTimeCalendar.getTime()) + " 24:00:00";
        //计算中间月
        mLineMonthArr = new ArrayList<>();
        Calendar curr = Calendar.getInstance();
        curr.set(mLineStartTimeCalendar.get(Calendar.YEAR), mLineStartTimeCalendar.get(Calendar.MONTH), mLineStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
        while (curr.before(mLineEndTimeCalendar)) {
            mLineMonthArr.add(formatMonth.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        generateDataLine();
        //==================线性图=======结束=============================


        //==================收入圆形图=======开始=============================
        //获取当前月第一天：
        mPieIncomeStartTimeCalendar = Calendar.getInstance();
        mPieIncomeStartTimeCalendar.add(Calendar.DAY_OF_MONTH, -30);
        mPieIncomeStartTimeTextView.setText(format.format(mPieIncomeStartTimeCalendar.getTime()));
        mPieIncomeStartTimePie = format.format(mPieIncomeStartTimeCalendar.getTime()) + " 00:00:00";
        //获取当前月最后一天
        mPieIncomeEndTimeCalendar = Calendar.getInstance();
        mPieIncomeEndTimeTextView.setText(format.format(mPieIncomeEndTimeCalendar.getTime()));
        mPieIncomeEndTimePie = format.format(mPieIncomeEndTimeCalendar.getTime()) + " 24:00:00";
        generateDataPieIncome();
        //==================收入圆形图=======结束=============================


        //==================纯收入线性图=======开始=============================
        //获取开始月：
        mBarStartTimeCalendar = Calendar.getInstance();
        mBarStartTimeCalendar.add(Calendar.MONTH, -6);
        //mBarStartTimeCalendar.set(CommonConstants.INCOME_START_YEAR, CommonConstants.INCOME_START_MONTH, CommonConstants.INCOME_START_DAY);
        mBarStartTimeTextView.setText(formatMonth.format(mBarStartTimeCalendar.getTime()));
        mBarStartTimeBar = formatMonth.format(mBarStartTimeCalendar.getTime()) + "-01 00:00:00";
        //获取当前月最后一天
        mBarEndTimeCalendar = Calendar.getInstance();
        mBarEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mBarEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        mBarEndTimeTextView.setText(formatMonth.format(mBarEndTimeCalendar.getTime()));
        mBarEndTimeBar = format.format(mBarEndTimeCalendar.getTime()) + " 24:00:00";
        //计算中间月
        mBarMonthArr = new ArrayList<>();
        curr = Calendar.getInstance();
        curr.set(mBarStartTimeCalendar.get(Calendar.YEAR), mBarStartTimeCalendar.get(Calendar.MONTH), mBarStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
        while (curr.before(mBarEndTimeCalendar)) {
            mBarMonthArr.add(formatMonth.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        generateDataBar();
        //==================纯收入线性图=======结束=============================


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pie_chart_starttime:
                DatePickerDialog pieStartdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mPieStartTimeCalendar.set(year, monthOfYear, dayOfMonth);
                        mPieStartTimeTextView.setText(format.format(mPieStartTimeCalendar.getTime()));
                        mPieStartTimePie = format.format(mPieStartTimeCalendar.getTime()) + " 00:00:00";
                        generatePayingDataPie();
                    }
                }, mPieStartTimeCalendar.get(Calendar.YEAR), mPieStartTimeCalendar.get(Calendar.MONTH), mPieStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
                pieStartdd.show();
                break;

            case R.id.pie_chart_endtime:
                DatePickerDialog pieEnddd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mPieEndTimeCalendar.set(year, monthOfYear, dayOfMonth);
                        mPieEndTimeTextView.setText(format.format(mPieEndTimeCalendar.getTime()));
                        mPieEndTimePie = format.format(mPieEndTimeCalendar.getTime()) + " 24:00:00";
                        generatePayingDataPie();
                    }
                }, mPieEndTimeCalendar.get(Calendar.YEAR), mPieEndTimeCalendar.get(Calendar.MONTH), mPieEndTimeCalendar.get(Calendar.DAY_OF_MONTH));
                pieEnddd.show();
                break;
            case R.id.line_chart_starttime:
                new MaterialDialog.Builder(getContext())
                        .title(R.string.report_date_select)
                        .items(mLineMonthArr)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mLineStartTimeTextView.setText(text);
                                String[] textArr = text.toString().split("-");
                                mLineStartTimeCalendar.set(Integer.parseInt(textArr[0]), (Integer.parseInt(textArr[1]) - 1), 1);
                                mLineStartTimeLine = text + "-01 00:00:00";
                                generateDataLine();
                            }
                        })
                        .positiveText(android.R.string.cancel)
                        .show();
                break;

            case R.id.line_chart_endtime:
                new MaterialDialog.Builder(getContext())
                        .title(R.string.report_date_select)
                        .items(mLineMonthArr)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mLineEndTimeTextView.setText(text);
                                String[] textArr = text.toString().split("-");
                                mLineEndTimeCalendar.set(Integer.parseInt(textArr[0]), (Integer.parseInt(textArr[1]) - 1), 1);
                                mLineEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mLineEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                mLineEndTimeTextView.setText(formatMonth.format(mLineEndTimeCalendar.getTime()));
                                mLineEndTimeLine = format.format(mLineEndTimeCalendar.getTime()) + " 24:00:00";
                                generateDataLine();
                            }
                        })
                        .positiveText(android.R.string.cancel)
                        .show();
                break;
            case R.id.pie_income_chart_starttime:
                DatePickerDialog pieIncomeStartdd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mPieIncomeStartTimeCalendar.set(year, monthOfYear, dayOfMonth);
                        mPieIncomeStartTimeTextView.setText(format.format(mPieIncomeStartTimeCalendar.getTime()));
                        mPieIncomeStartTimePie = format.format(mPieIncomeStartTimeCalendar.getTime()) + " 00:00:00";
                        generateDataPieIncome();
                    }
                }, mPieIncomeStartTimeCalendar.get(Calendar.YEAR), mPieIncomeStartTimeCalendar.get(Calendar.MONTH), mPieIncomeStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
                pieIncomeStartdd.show();
                break;

            case R.id.pie_income_chart_endtime:
                DatePickerDialog pieIncomeEnddd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mPieIncomeEndTimeCalendar.set(year, monthOfYear, dayOfMonth);
                        mPieIncomeEndTimeTextView.setText(format.format(mPieIncomeEndTimeCalendar.getTime()));
                        mPieIncomeEndTimePie = format.format(mPieIncomeEndTimeCalendar.getTime()) + " 24:00:00";
                        generateDataPieIncome();
                    }
                }, mPieIncomeEndTimeCalendar.get(Calendar.YEAR), mPieIncomeEndTimeCalendar.get(Calendar.MONTH), mPieIncomeEndTimeCalendar.get(Calendar.DAY_OF_MONTH));
                pieIncomeEnddd.show();
                break;
            case R.id.bar_chart_starttime:
                new MaterialDialog.Builder(getContext())
                        .title(R.string.report_date_select)
                        .items(mBarMonthArr)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mBarStartTimeTextView.setText(text);
                                String[] textArr = text.toString().split("-");
                                mBarStartTimeCalendar.set(Integer.parseInt(textArr[0]), (Integer.parseInt(textArr[1]) - 1), 1);
                                mBarStartTimeBar = text + "-01 00:00:00";
                                generateDataBar();
                            }
                        })
                        .positiveText(android.R.string.cancel)
                        .show();
                break;

            case R.id.bar_chart_endtime:
                new MaterialDialog.Builder(getContext())
                        .title(R.string.report_date_select)
                        .items(mBarMonthArr)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mBarEndTimeTextView.setText(text);
                                String[] textArr = text.toString().split("-");
                                mBarEndTimeCalendar.set(Integer.parseInt(textArr[0]), (Integer.parseInt(textArr[1]) - 1), 1);
                                mBarEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mBarEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                mBarEndTimeTextView.setText(formatMonth.format(mBarEndTimeCalendar.getTime()));
                                mBarEndTimeBar = format.format(mBarEndTimeCalendar.getTime()) + " 24:00:00";
                                generateDataBar();
                            }
                        })
                        .positiveText(android.R.string.cancel)
                        .show();
                break;
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private void generateDataLine() {
        if (-1 != CommonUtility.compareDate(mLineStartTimeLine, mLineEndTimeLine)) {
            ToastUtil.toastShow(R.string.report_date_error_ago, ToastUtil.ICON.WARNING);
            return;
        }

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        //计算中间月
        final ArrayList<String> mLineMonthArr = new ArrayList<>();
        int ii = 0;
        Calendar curr = Calendar.getInstance();
        Calendar currWhile = Calendar.getInstance();
        curr.set(mLineStartTimeCalendar.get(Calendar.YEAR), mLineStartTimeCalendar.get(Calendar.MONTH), mLineStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
        while (curr.before(mLineEndTimeCalendar)) {
            mLineMonthArr.add(formatMonth.format(curr.getTime()));
            currWhile.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH), curr.get(Calendar.DAY_OF_MONTH));
            currWhile.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
            e1.add(new Entry(ii, (float) mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_PAYING, formatMonth.format(currWhile.getTime()) + "-01 00:00:00", format.format(currWhile.getTime()) + " 24:00:00").getMoneysum()));
            curr.add(Calendar.MONTH, 1);
            ii++;
        }

        LineDataSet d1 = new LineDataSet(e1, "支出趋势");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> e2 = new ArrayList<Entry>();
        //计算中间月
        ii = 0;
        curr = Calendar.getInstance();
        currWhile = Calendar.getInstance();
        curr.set(mLineStartTimeCalendar.get(Calendar.YEAR), mLineStartTimeCalendar.get(Calendar.MONTH), mLineStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
        while (curr.before(mLineEndTimeCalendar)) {
            //curr.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
            currWhile.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH), curr.get(Calendar.DAY_OF_MONTH));
            currWhile.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
            e2.add(new Entry(ii, (float) mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_INCOME, formatMonth.format(currWhile.getTime()) + "-01 00:00:00", format.format(currWhile.getTime()) + " 24:00:00").getMoneysum()));
            curr.add(Calendar.MONTH, 1);
            ii++;
        }

        LineDataSet d2 = new LineDataSet(e2, "收入趋势");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

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
                    return mLineMonthArr.get((int)value);
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
        Calendar currWhile = Calendar.getInstance();
        curr.set(mBarStartTimeCalendar.get(Calendar.YEAR), mBarStartTimeCalendar.get(Calendar.MONTH), mBarStartTimeCalendar.get(Calendar.DAY_OF_MONTH));
        while (curr.before(mBarEndTimeCalendar)) {
            mBarMonthArr.add(formatMonth.format(curr.getTime()));
            //curr.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
            currWhile.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH), curr.get(Calendar.DAY_OF_MONTH));
            currWhile.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
            income = mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_INCOME, formatMonth.format(currWhile.getTime()) + "-01 00:00:00", format.format(currWhile.getTime()) + " 24:00:00").getMoneysum();
            paying = mDlIncomeService.getSumMoneyByData(CommonConstants.INCOME_ROLE_PAYING, formatMonth.format(currWhile.getTime()) + "-01 00:00:00", format.format(currWhile.getTime()) + " 24:00:00").getMoneysum();
            float nombers = (float) (income - paying);
            entries.add(new BarEntry(ii, nombers));
            if (nombers >= 0)
                colors.add(red);
            else
                colors.add(green);
            curr.add(Calendar.MONTH, 1);
            ii++;
        }

        mBarChart.setBackgroundColor(Color.WHITE);
        mBarChart.setExtraTopOffset(-30f);
        mBarChart.setExtraBottomOffset(10f);
        mBarChart.setExtraLeftOffset(70f);
        mBarChart.setExtraRightOffset(70f);
        mBarChart.getDescription().setEnabled(false);

        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);

        //mBarChart.setDescription("");

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
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mBarMonthArr.get((int) value);
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
        set.setColors(colors);
        set.setValueTextColors(colors);

        BarData data = new BarData(set);
        data.setValueTextSize(13f);
        data.setValueTypeface(mTf);
        //data.setValueFormatter(new ValueFormatter());
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

        if (-1 != CommonUtility.compareDate(mPieStartTimePie, mPieEndTimePie)) {
            ToastUtil.toastShow(R.string.report_date_error_ago, ToastUtil.ICON.WARNING);
            return;
        }

        ((HomeActivity) getActivity()).showProgressDialog();
        List<DiIncome> mDiIncomeList = mDlIncomeService.getByTypeSumMoneyData(CommonConstants.INCOME_ROLE_PAYING, mPieStartTimePie, mPieEndTimePie);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> entriescolors = new ArrayList<>();
        double allPay = 0;

        for (int i = 0; i < mDiIncomeList.size(); i++) {
            entries.add(new PieEntry((float) mDiIncomeList.get(i).getMoneysum(), mDiIncomeList.get(i).getType()));

            String[] colorArr = null != mDiIncomeList.get(i).getColor()?mDiIncomeList.get(i).getColor().split(","):CommonConstants.PAYTYPE_DIY_COLOR.split(",");
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
        allPayStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.common_body_tip_colors)), 0, 3, 0);
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
        mPieChart.setExtraOffsets(5, 10, 50, 10);
        mPieChart.getDescription().setEnabled(false);

        cd.setValueFormatter(new PercentFormatter());
        cd.setValueTypeface(mTf);
        cd.setValueTextSize(11f);
        cd.setValueTextColor(Color.WHITE);
        // set data
        mPieChart.setData((PieData) cd);

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // do not forget to refresh the chart
        // mPieChart.invalidate();
        mPieChart.animateY(900);
        ((HomeActivity) getActivity()).cancelProgressDialog();
    }


    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private void generateDataPieIncome() {

        if (-1 != CommonUtility.compareDate(mPieIncomeStartTimePie, mPieIncomeEndTimePie)) {
            ToastUtil.toastShow(R.string.report_date_error_ago, ToastUtil.ICON.WARNING);
            return;
        }

        List<DiIncome> mDiIncomeList = mDlIncomeService.getByTypeSumMoneyData(CommonConstants.INCOME_ROLE_INCOME, mPieIncomeStartTimePie, mPieIncomeEndTimePie);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> entriescolors = new ArrayList<>();
        double allPay = 0;

        for (int i = 0; i < mDiIncomeList.size(); i++) {
            entries.add(new PieEntry((float) mDiIncomeList.get(i).getMoneysum(), mDiIncomeList.get(i).getType()));

            String[] colorArr = null != mDiIncomeList.get(i).getColor()?mDiIncomeList.get(i).getColor().split(","):CommonConstants.INCOME_DIY_COLOR.split(",");
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
        allPayStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.common_body_tip_colors)), 0, 3, 0);
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
    }


    public ScrollView getmReportScrollview() {
        return mReportScrollview;
    }

}
