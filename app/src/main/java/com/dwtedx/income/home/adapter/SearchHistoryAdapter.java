package com.dwtedx.income.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dwtedx.income.R;

import java.util.List;

/**
 * Created by A150189 on 2016/4/1.
 * 热门搜索
 */
public class SearchHistoryAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<String> list;
    private Context context;

    public SearchHistoryAdapter(Context context, List<String> list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list != null ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_history_search, null);
            holder.mTvContent = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null && list.size() > 0) {
            holder.mTvContent.setText(list.get(position));
        }
        return convertView;
    }

    class ViewHolder {
        TextView mTvContent;
    }
}
