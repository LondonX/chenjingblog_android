package xyz.chenjing.blog.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.londonx.lutil.entity.LResponse;
import com.londonx.lutil.util.LRequestTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.chenjing.blog.CommentActivity;
import xyz.chenjing.blog.R;
import xyz.chenjing.blog.entity.Article;
import xyz.chenjing.blog.entity.Comment;
import xyz.chenjing.blog.net.NetConst;
import xyz.chenjing.blog.net.Params;

/**
 * Created by london on 16/2/19.
 * to show others comment
 */
public class CommentFragment extends Fragment implements LRequestTool.OnResponseListener {
    @Bind(R.id.fragment_comment_lnComments)
    LinearLayout fragmentCommentLnComments;
    @Bind(R.id.fragment_comment_tvMore)
    TextView fragmentCommentTvMore;


    private View rootView;
    private Article article;
    private LRequestTool requestTool = new LRequestTool(this);
    private List<Comment> comments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_comment, container, false);
        }
        rootView.setVisibility(View.GONE);
        ButterKnife.bind(this, rootView);
        if (article != null) {
            getComments();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResponse(LResponse response) {
        if (response.responseCode != 200) {
            comments.clear();
        } else {
            comments = new Gson()
                    .fromJson(response.body, new TypeToken<List<Comment>>() {
                    }.getType());
        }
        makeViewByComments();
    }

    @OnClick(R.id.fragment_comment_tvMore)
    protected void moreComment() {
        Intent commentIntent = new Intent(getContext(), CommentActivity.class);
        commentIntent.putExtra("article", article);
        commentIntent.putExtra("write", comments.size() == 0);
        startActivity(commentIntent);
    }

    public void setArticle(Article article) {
        this.article = article;
        if (fragmentCommentLnComments != null) {
            getComments();
        }
    }

    public void getComments() {
        requestTool.doGet(NetConst.ROOT + NetConst.API_LEAVE_MESSAGES,
                new Params().put("article_id", article.id).put("page", 1),
                0);
    }

    private void makeViewByComments() {
        rootView.setVisibility(View.VISIBLE);
        if (comments.size() == 0) {
            fragmentCommentTvMore.setText(R.string.write_comment);
            return;
        }
        fragmentCommentLnComments.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int index = 0;
        for (Comment c : comments) {
            View v = inflater.inflate(R.layout.item_comment, fragmentCommentLnComments, false);
            CommentHolder holder = new CommentHolder(v);
            holder.itemCommentName.setText(c.name);
            holder.itemCommentTvContent.setText(c.content);
            holder.itemCommentTvTime.setText(c.created_at);

            fragmentCommentLnComments.addView(v);
            index++;
            if (index > 2) {
                break;
            }
        }
    }

    class CommentHolder {
        @Bind(R.id.item_comment_tvName)
        TextView itemCommentName;
        @Bind(R.id.item_comment_tvContent)
        TextView itemCommentTvContent;
        @Bind(R.id.item_comment_tvTime)
        TextView itemCommentTvTime;

        CommentHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
