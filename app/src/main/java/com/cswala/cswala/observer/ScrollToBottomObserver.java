package com.cswala.cswala.observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollToBottomObserver extends RecyclerView.AdapterDataObserver {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<?> adapter;
    private LinearLayoutManager manager;

    public ScrollToBottomObserver(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter, LinearLayoutManager manager) {
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.manager = manager;
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        int count = adapter.getItemCount();
        int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
        boolean isLoading = lastVisiblePos == -1;
        boolean isBottom = positionStart >= (count - 1) && lastVisiblePos == (positionStart - 1);
        if (isLoading || isBottom) {
            recyclerView.scrollToPosition(positionStart);
        }
    }
}
