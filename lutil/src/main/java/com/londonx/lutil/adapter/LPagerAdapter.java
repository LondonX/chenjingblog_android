package com.londonx.lutil.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.londonx.lutil.entity.LEntity;

import java.util.List;

/**
 * Created by london on 15/9/29.
 * simple PagerAdapter without exceptions
 */
public abstract class LPagerAdapter extends PagerAdapter {
    protected SparseArray<View> views;
    protected List<? extends LEntity> lEntities;

    public LPagerAdapter(List<? extends LEntity> lEntities) {
        this.lEntities = lEntities;
        views = new SparseArray<>();
    }

    @Override
    public int getCount() {
        return lEntities.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        if (view == null) {
            view = newView(position, container);
            views.put(position, view);
        }
        container.addView(view);
        return view;
    }

    protected abstract View newView(int position, ViewGroup parent);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    public void setNewData(List<LEntity> lEntities) {
        this.lEntities = lEntities;
        notifyDataSetChanged();
    }

    public SparseArray<View> getAllViews() {
        return views;
    }
}
