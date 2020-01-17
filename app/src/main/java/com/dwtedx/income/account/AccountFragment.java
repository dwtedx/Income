package com.dwtedx.income.account;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dwtedx.income.R;
import com.dwtedx.income.base.BaseFragment;
import com.dwtedx.income.entity.DiAccount;
import com.dwtedx.income.home.HomeV2Activity;
import com.dwtedx.income.home.adapter.DiAccountAdapter;
import com.dwtedx.income.sqliteservice.DIAccountService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dwtedx(qinyl)(http://dwtedx.com) on 16/5/16.
 * Company 路之遥网络科技有限公司
 * Description TODO(这里用一句话描述这个类的作用)
 */
public class AccountFragment extends BaseFragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2<ListView>{

    private PullToRefreshListView mListView;
    private List<DiAccount> mDiAccountItems;
    private DiAccountAdapter mDiAccountAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_account, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = (PullToRefreshListView) view.findViewById(R.id.listView);
        mDiAccountItems = new ArrayList<>();
        mDiAccountAdapter = new DiAccountAdapter(getContext(), mDiAccountItems);
        mListView.setAdapter(mDiAccountAdapter);
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDiIncomeItems();
    }

    private void getDiIncomeItems() {
        ((HomeV2Activity) getActivity()).showProgressDialog();

        mDiAccountItems.clear();
        mDiAccountItems.addAll(DIAccountService.getInstance(getContext()).findAll());
        mDiAccountAdapter.notifyDataSetChanged();
        mListView.onRefreshComplete();
        ((HomeV2Activity) getActivity()).cancelProgressDialog();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), AccountListActivity.class);
        intent.putExtra("ACCOUNTID", mDiAccountItems.get(position - 1).getId());
        intent.putExtra("ACCOUNTCOLOR", mDiAccountItems.get(position - 1).getColor());
        intent.putExtra("ACCOUNTICON", mDiAccountItems.get(position - 1).getIcon());
        intent.putExtra("ACCOUNTMONTY", mDiAccountItems.get(position - 1).getMoneysum());
        startActivity(intent);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDiIncomeItems();
            }
        }, 500);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDiIncomeItems();

            }
        }, 500);
    }
}
