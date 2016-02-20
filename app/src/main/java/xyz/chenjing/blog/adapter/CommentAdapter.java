package xyz.chenjing.blog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.londonx.lutil.adapter.LAdapter;
import com.londonx.lutil.entity.LEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.chenjing.blog.CommentActivity;
import xyz.chenjing.blog.R;
import xyz.chenjing.blog.entity.Comment;

/**
 * Created by london on 16/2/20.
 * adapter in {@link CommentActivity}
 */
public class CommentAdapter extends LAdapter {
    public CommentAdapter(List<? extends LEntity> lEntities) {
        super(lEntities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Comment c = (Comment) getItem(position);
        holder.itemCommentTvName.setText(c.name);
        holder.itemCommentTvContent.setText(c.content);
        holder.itemCommentTvTime.setText(c.created_at);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_comment_tvName)
        TextView itemCommentTvName;
        @Bind(R.id.item_comment_tvContent)
        TextView itemCommentTvContent;
        @Bind(R.id.item_comment_tvTime)
        TextView itemCommentTvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
