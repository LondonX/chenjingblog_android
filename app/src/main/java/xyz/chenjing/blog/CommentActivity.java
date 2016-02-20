package xyz.chenjing.blog;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.londonx.lutil.entity.LResponse;
import com.londonx.lutil.impl.NextPageLoader;
import com.londonx.lutil.util.LRequestTool;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.chenjing.blog.adapter.CommentAdapter;
import xyz.chenjing.blog.entity.Article;
import xyz.chenjing.blog.entity.Comment;
import xyz.chenjing.blog.fragment.CommentEditorFragment;
import xyz.chenjing.blog.net.NetConst;
import xyz.chenjing.blog.net.Params;

/**
 * Created by london on 16/2/20.
 * More comment
 */
public class CommentActivity extends AppCompatActivity implements LRequestTool.OnResponseListener, NextPageLoader.NextPagerTrigger {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.activity_comment_lv)
    ListView activityCommentLv;
    @Bind(R.id.activity_comment_srl)
    SwipeRefreshLayout activityCommentSrl;
    @Bind(R.id.activity_comment_fab)
    FloatingActionButton activityCommentFab;
    CommentEditorFragment commentEditorFragment;
    @Bind(R.id.activity_comment_fragmentEditor)
    View editorView;

    private CommentEditorFragment editorFragment;

    private Article article;
    private LRequestTool requestTool = new LRequestTool(this);
    private List<Comment> comments;
    private boolean hasMore;
    private boolean isLoading;
    private int currentPage = 1;
    private CommentAdapter adapter;
    private boolean isShowingEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        article = (Article) getIntent().getSerializableExtra("article");
        boolean write = getIntent().getBooleanExtra("write", false);
        if (article == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        activityCommentLv.setOnScrollListener(new NextPageLoader(activityCommentLv,
                this,
                ContextCompat.getColor(this, R.color.colorAccent)));
        editorFragment = (CommentEditorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_comment_fragmentEditor);
        editorFragment.setArticle(article);
        if (write) {
            showEditor();
        } else {
            getData();
            editorView.post(new Runnable() {
                @Override
                public void run() {
                    editorView.animate().setDuration(0).translationY(editorView.getHeight()).start();
                }
            });
        }
    }

    @OnClick(R.id.activity_comment_fab)
    protected void showEditor() {
        if (isShowingEditor) {
            return;
        }
        isShowingEditor = true;
        editorView.animate()
                .setDuration(200)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .start();
        activityCommentFab.hide();
    }

    private void hideEditor() {
        if (!isShowingEditor) {
            return;
        }
        isShowingEditor = false;
        editorView.animate()
                .setDuration(200)
                .translationY(editorView.getHeight())
                .setInterpolator(new DecelerateInterpolator())
                .start();
        activityCommentFab.show();
    }

    @Override
    public void onBackPressed() {
        if (isShowingEditor) {
            hideEditor();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(LResponse response) {
        isLoading = false;
        if (activityCommentSrl == null) {
            return;
        }
        activityCommentSrl.post(new Runnable() {
            @Override
            public void run() {
                activityCommentSrl.setRefreshing(false);
            }
        });
        if (response.responseCode != 200) {
            Snackbar.make(activityCommentFab,
                    getString(com.londonx.lutil.R.string.server_error_) + response.responseCode,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.refresh, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRefresh();
                        }
                    })
                    .show();
            return;
        }
        List<Comment> appendData = new Gson()
                .fromJson(response.body, new TypeToken<List<Comment>>() {
                }.getType());
        hasMore = appendData.size() == 12;
        if (currentPage == 1) {
            comments = appendData;
            adapter = new CommentAdapter(comments);
            activityCommentLv.setAdapter(adapter);
        } else {
            comments.addAll(appendData);
            adapter.setNewData(comments);
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        hasMore = false;
        getData();
        hideEditor();
    }

    @Override
    public void onLoadMore() {
        if (isLoading || !hasMore) {
            return;
        }
        currentPage++;
        getData();
    }

    private void getData() {
        if (isLoading) {
            return;
        }
        if (currentPage == 1) {
            activityCommentSrl.post(new Runnable() {
                @Override
                public void run() {
                    activityCommentSrl.setRefreshing(true);
                }
            });
        }
        isLoading = true;
        requestTool.doGet(NetConst.ROOT + NetConst.API_LEAVE_MESSAGES,
                new Params().put("article_id", article.id).put("page", currentPage),
                0);
    }
}
