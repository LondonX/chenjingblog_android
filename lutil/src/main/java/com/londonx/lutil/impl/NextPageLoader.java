package com.londonx.lutil.impl;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by london on 15/12/7.
 * load next page and {@link android.support.v4.widget.SwipeRefreshLayout} quickly
 */
public class NextPageLoader implements
        AbsListView.OnScrollListener,
        SwipeRefreshLayout.OnRefreshListener {
    public static final int PAGE_SIZE = 10;
    private SwipeRefreshLayout swipeRefreshLayout;
    @NonNull
    private ListView listView;
    private NextPagerTrigger nextPagerTrigger;

    public NextPageLoader(@NonNull ListView listView,
                          NextPagerTrigger nextPagerTrigger,
                          @ColorInt int... refreshColor) {
        this.listView = listView;
        this.nextPagerTrigger = nextPagerTrigger;
        if (listView.getParent() instanceof SwipeRefreshLayout) {
            swipeRefreshLayout = (SwipeRefreshLayout) listView.getParent();
            swipeRefreshLayout.setColorSchemeColors(refreshColor);
            swipeRefreshLayout.setOnRefreshListener(this);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view.getCount() == 0) {
            return;
        }
        if (nextPagerTrigger == null) {
            return;
        }
        //Load more when scrolling over last PAGE_SIZE / 2 items;
        boolean isDivDPage = firstVisibleItem + visibleItemCount > totalItemCount - PAGE_SIZE / 2;
        if (isDivDPage) {
            nextPagerTrigger.onLoadMore();
        }
    }

    @Override
    public void onRefresh() {
        if (nextPagerTrigger == null) {
            return;
        }
        nextPagerTrigger.onRefresh();
    }

    public void stopAnim(long delay) {
        if (swipeRefreshLayout == null) {
            return;
        }
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout == null) {
                    return;
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, delay);
    }

    public interface NextPagerTrigger {
        void onRefresh();

        void onLoadMore();
    }
}
