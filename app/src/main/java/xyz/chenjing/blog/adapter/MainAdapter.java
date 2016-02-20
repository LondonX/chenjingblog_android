package xyz.chenjing.blog.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.londonx.lutil.adapter.LAdapter;
import com.londonx.lutil.entity.LEntity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.chenjing.blog.R;
import xyz.chenjing.blog.entity.Article;
import xyz.chenjing.blog.net.NetConst;

/**
 * Created by london on 16/2/19.
 * Main list adapter
 */
public class MainAdapter extends LAdapter {
    public MainAdapter(List<? extends LEntity> lEntities) {
        super(lEntities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_main, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.setArticle((Article) getItem(position));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_main_imgCover)
        ImageView itemMainImgCover;
        @Bind(R.id.item_main_tvTitle)
        TextView itemMainTvTitle;
        @Bind(R.id.item_main_tvPreview)
        TextView itemMainTvPreview;
        @Bind(R.id.item_main_linearDesc)
        LinearLayout itemMainLinearDesc;
        private static final ImageLoader LOADER = ImageLoader.getInstance();
        private static int textColor;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            textColor = ContextCompat.getColor(view.getContext(), R.color.textColorDefault);
        }

        public void setArticle(Article article) {
            if (article.cover.length() == 0 || article.cover.contains("/image/no_pic.jpg")) {
                itemMainImgCover.setVisibility(View.GONE);
                itemMainTvTitle.setTextColor(textColor);
                itemMainTvPreview.setTextColor(textColor);
                itemMainLinearDesc.setBackgroundColor(Color.WHITE);
            } else {
                itemMainImgCover.setVisibility(View.VISIBLE);
                if (article.cover.startsWith("/")) {
                    article.cover = NetConst.ROOT + article.cover;
                }
                LOADER.displayImage(article.cover, itemMainImgCover, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        Palette palette = Palette.from(loadedImage).generate();
                        int bg = palette.getDarkMutedColor(Color.WHITE);
                        if (bg == -1 || bg == 0) {
                            return;
                        }
                        itemMainLinearDesc.setBackgroundColor(bg);
                        itemMainTvTitle.setTextColor(Color.WHITE);
                        itemMainTvPreview.setTextColor(Color.WHITE);
                    }
                });
            }
            itemMainTvTitle.setText(article.title);
            itemMainTvPreview.setText(article.description);
        }
    }
}
