package com.dwtedx.income.report;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.entity.DiIncome;
import com.dwtedx.income.sqliteservice.DlIncomeService;
import com.dwtedx.income.utility.CommonConstants;
import com.dwtedx.income.utility.CommonUtility;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class ReportIncomePieFragment extends BaseFragment implements View.OnClickListener {

    private DlIncomeService mDlIncomeService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
    private Typeface mTf;

    private NestedScrollView mReportScrollview;

    //收入圆形图片
    private PieChart mPieIncomeChart;
    private TextView mPieIncomeStartTimeTextView;
    private TextView mPieIncomeEndTimeTextView;
    private String mPieIncomeStartTimePie;
    private String mPieIncomeEndTimePie;
    private Calendar mPieIncomeStartTimeCalendar;
    private Calendar mPieIncomeEndTimeCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_report_income_pie, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReportScrollview = (NestedScrollView) view.findViewById(R.id.report_scrollview);

        mPieIncomeChart = (PieChart) view.findViewById(R.id.pie_income_chart);
        mPieIncomeStartTimeTextView = (TextView) view.findViewById(R.id.pie_income_chart_starttime);
        mPieIncomeStartTimeTextView.setOnClickListener(this);
        mPieIncomeEndTimeTextView = (TextView) view.findViewById(R.id.pie_income_chart_endtime);
        mPieIncomeEndTimeTextView.setOnClickListener(this);

        mDlIncomeService = DlIncomeService.getInstance(getContext());

        mTf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public void onResume() {
        super.onResume();

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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private void generateDataPieIncome() {

        if (-1 != CommonUtility.compareDate(mPieIncomeStartTimePie, mPieIncomeEndTimePie)) {
            //Toast.makeText(getContext(), getString(R.string.report_date_error_ago), Toast.LENGTH_SHORT).show();
            Snackbar.make(mReportScrollview, R.string.report_date_error_ago, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }

        List<DiIncome> mDiIncomeList = mDlIncomeService.getByTypeSumMoneyData(CommonConstants.INCOME_ROLE_INCOME, mPieIncomeStartTimePie, mPieIncomeEndTimePie);

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> entriescolors = new ArrayList<>();
        double allPay = 0;
        String showColor;
        int iShowColor = -1;

        for (int i = 0; i < mDiIncomeList.size(); i++) {
            entries.add(new PieEntry((float) mDiIncomeList.get(i).getMoneysum(), mDiIncomeList.get(i).getType() + CommonUtility.doubleTrans(mDiIncomeList.get(i).getMoneysum())));
            //2019-02-13 自定义类型报表颜色更换
            showColor = CommonConstants.INCOME_DIY_COLOR;
            if(!CommonUtility.isEmpty(mDiIncomeList.get(i).getColor())){
                if(CommonConstants.INCOME_DIY_COLOR.equals(mDiIncomeList.get(i).getColor())){
                    if(iShowColor >= 0){
                        showColor = CommonConstants.INCOME_PAYTYPE_DIY_ICON_COLOR[iShowColor % CommonConstants.INCOME_PAYTYPE_DIY_ICON_COLOR.length];
                    }else{
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

        SpannableString allPayStr = new SpannableString(getString(R.string.record_income_all) + "\n￥" + CommonUtility.doubleFormat(allPay));
        allPayStr.setSpan(new RelativeSizeSpan(1.6f), 0, 3, 0);
        allPayStr.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.common_body_tip_colors)), 0, 3, 0);
        allPayStr.setSpan(new RelativeSizeSpan(1.8f), 3, allPayStr.length(), 0);


        PieData cd = new PieData(dataSet);

        // apply styling
        //mPieIncomeChart.setDescription("");
        mPieIncomeChart.setDrawEntryLabels(false);
        mPieIncomeChart.setHoleRadius(52f);
        mPieIncomeChart.setTransparentCircleRadius(57f);
        mPieIncomeChart.setCenterText(allPayStr);
        mPieIncomeChart.setCenterTextSize(105);
        mPieIncomeChart.setCenterTextTypeface(mTf);
        mPieIncomeChart.setCenterTextSize(9f);
        mPieIncomeChart.setUsePercentValues(true);
        mPieIncomeChart.setExtraOffsets(5, 0, 40, 0);
        mPieIncomeChart.getDescription().setEnabled(false);

        cd.setValueFormatter(new PercentFormatter());
        cd.setValueTypeface(mTf);
        cd.setValueTextSize(11f);
        cd.setValueTextColor(Color.WHITE);
        // set data
        mPieIncomeChart.setData((PieData) cd);

        Legend l = mPieIncomeChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // do not forget to refresh the chart
        // mPieIncomeChart.invalidate();
        mPieIncomeChart.animateY(900);
    }

}
