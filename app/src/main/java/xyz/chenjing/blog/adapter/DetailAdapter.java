package xyz.chenjing.blog.adapter;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.londonx.lutil.adapter.LAdapter;
import com.londonx.lutil.entity.LEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.chenjing.blog.R;
import xyz.chenjing.blog.entity.Item;

/**
 * Created by london on 16/2/19.
 * Article {@link Item}s adapter
 */
public class DetailAdapter extends LAdapter {
    public DetailAdapter(List<? extends LEntity> lEntities) {
        super(lEntities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_detail, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.setItem((Item) getItem(position));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_detail_tvContent)
        TextView itemDetailTvContent;
        @Bind(R.id.item_detail_imgContent)
        ImageView itemDetailImgContent;

        private static final ImageLoader LOADER = ImageLoader.getInstance();

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setItem(Item item) {
            if (item.file_url == null || item.file_url.length() == 0) {
                itemDetailImgContent.setVisibility(View.GONE);
            } else {
                itemDetailImgContent.setVisibility(View.VISIBLE);
                LOADER.displayImage(item.file_url, itemDetailImgContent);
            }
            if (item.content == null || item.content.length() == 0) {
                itemDetailTvContent.setVisibility(View.GONE);
            } else {
                itemDetailTvContent.setVisibility(View.VISIBLE);
                itemDetailTvContent.setText(Html.fromHtml(item.content));
                itemDetailTvContent.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }
}
