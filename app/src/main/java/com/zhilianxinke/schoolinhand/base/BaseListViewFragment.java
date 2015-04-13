package com.zhilianxinke.schoolinhand.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zhilianxinke.schoolinhand.R;

import java.util.List;

/**
 * Created by hh on 2015-03-04.
 */
public abstract class BaseListViewFragment<T> extends Fragment implements AdapterView.OnItemClickListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private View inflatedView;

    private String tag;
    private int rowContainerId;
    private List<T> _dataList;
    private ArrayAdapter<T> _arrayAdapter;
    public BaseListViewFragment(String Tag ,int rowContainerId) {
        this.tag = tag;
        this.rowContainerId = rowContainerId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(tag, "onCreateView+" + tag);
        inflatedView = inflater.inflate(R.layout.fragment_class2__news_info, container, false);

        mListView = (ListView)inflatedView.findViewById(rowContainerId);

        mSwipeRefreshLayout = (SwipeRefreshLayout)inflatedView.findViewById(R.id.swipe_refresh);

        if (pullToRefreshList()) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
//                    mActivity.onRefresh();
                }
            });
            mSwipeRefreshLayout.setColorSchemeResources(getPullToRefreshColorResources());
        } else
            mSwipeRefreshLayout.setEnabled(false);

        if (mListView != null) {
            ArrayAdapter<T> adapter = getListAdapter();
            if (adapter != null) {
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(this);
            }
        }
        return inflatedView;
    }

    protected void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    protected abstract ArrayAdapter<T> getListAdapter();

    protected abstract boolean useCustomContentView();

    protected abstract int getCustomContentView();

    protected abstract boolean pullToRefreshList();

    protected abstract int[] getPullToRefreshColorResources();

    protected abstract void onRefresh();

    public abstract void onItemClick(AdapterView<?> adapterView, View view, int position, long l);

}
