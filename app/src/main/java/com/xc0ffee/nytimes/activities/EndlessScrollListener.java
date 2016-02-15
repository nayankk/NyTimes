package com.xc0ffee.nytimes.activities;

import android.widget.AbsListView;

/**
 * Created by qtc746 on 2/14/16.
 */

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int mVisibleThreshhold = 5;
    private int mCurrentPage = 0;
    private int mPrevTotalCnt = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

    public EndlessScrollListener() {}

    public EndlessScrollListener(int visibleThreshold) {
        this.mVisibleThreshhold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int startPage) {
        this.mVisibleThreshhold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.mCurrentPage = startPage;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (totalItemCount < mPrevTotalCnt) {
            this.mCurrentPage = this.startingPageIndex;
            this.mPrevTotalCnt = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        if (loading && (totalItemCount > mPrevTotalCnt)) {
            loading = false;
            mPrevTotalCnt = totalItemCount;
            mCurrentPage++;
        }

        if (!loading && (firstVisibleItem + visibleItemCount + mVisibleThreshhold) >= totalItemCount) {
            loading = onLoadMore(mCurrentPage + 1, totalItemCount);
        }
    }

    public abstract boolean onLoadMore(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
}