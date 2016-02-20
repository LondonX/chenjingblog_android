package com.londonx.lutil.adapter;

import android.widget.BaseAdapter;

import com.londonx.lutil.entity.LEntity;

import java.util.List;

/**
 * Created by london on 15/5/29.
 * LAdapter,more simple BaseAdapter.
 */
public abstract class LAdapter extends BaseAdapter {
    protected List<? extends LEntity> lEntities;

    public LAdapter(List<? extends LEntity> lEntities) {
        this.lEntities = lEntities;
    }

    public void setNewData(List<? extends LEntity> lEntities) {
        this.lEntities = lEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return lEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lEntities.get(position).id;
    }
}
