package com.londonx.lutil.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.londonx.lutil.R;
import com.londonx.lutil.entity.NamedEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by london on 15/12/18.
 * a {@link PopupWindow} to show {@link com.londonx.lutil.entity.NamedEntity}
 */
public class NamedEntityPopup extends PopupWindow implements AdapterView.OnItemClickListener {
    public static NamedEntityPopup create(@NonNull ViewGroup parent) {
        return new NamedEntityPopup(parent);
    }

    private static float density;

    private ListView lvNamedEntities;
    private ViewGroup parent;
    private View lastAnchor;
    private EntityPopupSelectListener EntityPopupSelectListener;

    private NamedEntityPopup(@NonNull ViewGroup parent) {
        super(parent.getContext());
        this.parent = parent;
        if (density == 0) {
            density = this.parent.getResources().getDisplayMetrics().density;
        }
        View rootView = LayoutInflater.from(this.parent.getContext())
                .inflate(R.layout.popup_named_entity, this.parent, false);
        lvNamedEntities = (ListView) rootView.findViewById(R.id.lvNamedEntities);
        lvNamedEntities.setOnItemClickListener(this);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//lv margin is 8dp
        setContentView(rootView);
    }

    @Override
    public void showAsDropDown(@NonNull View anchor) {
        setWidth((int) (anchor.getWidth() + 16 * density));
        lastAnchor = anchor;
        super.showAsDropDown(anchor, (int) (-8 * density), (int) (-8 * density) - anchor.getHeight());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        if (EntityPopupSelectListener != null) {
            EntityPopupSelectListener.selectStringAt(this, position);
        }
    }

    public void setEntityPopupSelectListener(EntityPopupSelectListener EntityPopupSelectListener) {
        this.EntityPopupSelectListener = EntityPopupSelectListener;
    }

    public void setEntities(List<? extends NamedEntity> entities) {
        List<String> strings = new ArrayList<>();
        for (NamedEntity e : entities) {
            strings.add(e.name);
        }
        lvNamedEntities.setAdapter(new ArrayAdapter<>(parent.getContext(), R.layout.item_named_entity, strings));
        int height = (int) ((strings.size() * 40 + 24) * density);//lv margin is 8dp
        setHeight(height > 320 * density ? (int) (320 * density) : height);
    }

    public View getLastAnchor() {
        return lastAnchor;
    }

    public interface EntityPopupSelectListener {
        void selectStringAt(NamedEntityPopup popup, int index);
    }
}
