package com.dwtedx.income.report;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class ReportPayingLineFragment extends BaseFragment implements View.OnClickListener {

    private DlIncomeService mDlIncomeService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
    private SimpleDateFormat formatMonthDesplay = new SimpleDateFormat("MM月");
    private Typeface mTf;

    private NestedScrollView mReportScrollview;

    //支出趋势图片
    private LineChart mLineChart;
    private TextView mLineStartTimeTextView;
    private TextView mLineEndTimeTextView;

    private String mLineStartTimeLine;
    private String mLineEndTimeLine;
    private Calendar mLineStartTimeCalendar;
    private Calendar mLineEndTimeCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_report_paying_line, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReportScrollview = (NestedScrollView) view.findViewById(R.id.report_scrollview);

        mLineChart = (LineChart) view.findViewById(R.id.line_chart);
        mLineStartTimeTextView = (TextView) view.findViewById(R.id.line_chart_starttime);
        mLineStartTimeTextView.setOnClickListener(this);
        mLineEndTimeTextView = (TextView) view.findViewById(R.id.line_chart_endtime);
        mLineEndTimeTextView.setOnClickListener(this);

        mDlIncomeService = DlIncomeService.getInstance(getContext());

        mTf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public void onResume() {
        super.onResume();

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

        generateDataLine();
        //==================线性图=======结束=============================

    }

    @Override
    public void onClick(View v) {
        Calendar today = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.line_chart_starttime:
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(),
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                mLineStartTimeCalendar.set(selectedYear, selectedMonth, 1);
                                String yearMonth = formatMonth.format(mLineStartTimeCalendar.getTime());
                                mLineStartTimeTextView.setText(yearMonth);
                                mLineStartTimeLine = yearMonth + "-01 00:00:00";
                                generateDataLine();
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setMinYear(1991)
                        .setTitle(getString(R.string.report_date_select))
                        .setActivatedMonth(mLineStartTimeCalendar.get(Calendar.MONTH))
                        .setActivatedYear(mLineStartTimeCalendar.get(Calendar.YEAR))
                        .build()
                        .show();

                break;

            case R.id.line_chart_endtime:
                MonthPickerDialog.Builder builderEnd = new MonthPickerDialog.Builder(getContext(),
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                mLineEndTimeCalendar.set(selectedYear, selectedMonth, 1);
                                mLineEndTimeCalendar.set(Calendar.DAY_OF_MONTH, mLineEndTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                String yearMonth = formatMonth.format(mLineEndTimeCalendar.getTime());
                                mLineEndTimeTextView.setText(yearMonth);
                                mLineEndTimeLine = format.format(mLineEndTimeCalendar.getTime()) + " 24:00:00";
                                generateDataLine();
                            }
                        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builderEnd.setMinYear(1991)
                        .setTitle(getString(R.string.report_date_select))
                        .build()
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
            //Toast.makeText(getContext(), getString(R.string.report_date_error_ago), Toast.LENGTH_SHORT).show();
            Snackbar.make(mReportScrollview, R.string.report_date_error_ago, Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
            mLineMonthArr.add(formatMonthDesplay.format(curr.getTime()));
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
        d1.setDrawValues(true);

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
        d2.setDrawValues(true);

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
                    int valueint = (int)value;
                    if(0 == (value-(float)valueint)) {
                        return mLineMonthArr.get(valueint);
                    }else {
                        return "";
                    }
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
        mLineChart.setData(cd);

        // do not forget to refresh the chart
        // mLineChart.invalidate();
        mLineChart.animateX(750);
    }

}
