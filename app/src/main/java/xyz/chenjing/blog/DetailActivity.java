package xyz.chenjing.blog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.chenjing.blog.adapter.DetailAdapter;
import xyz.chenjing.blog.entity.Article;
import xyz.chenjing.blog.fragment.CommentFragment;

/**
 * Created by london on 16/2/19.
 * Detail of articles
 */
public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.activity_detail_lv)
    ListView activityDetailLv;

    private Article article;
    private FootHolder footHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        article = (Article) getIntent().getSerializableExtra("article");
        if (article == null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(article.title);
        }
        View header = LayoutInflater.from(this).inflate(R.layout.head_detail, activityDetailLv, false);
        View footer = LayoutInflater.from(this).inflate(R.layout.foot_detail, activityDetailLv, false);
        HeadHolder headHolder = new HeadHolder(header);
        headHolder.initViews();
        footHolder = new FootHolder();
        activityDetailLv.addHeaderView(header);
        activityDetailLv.addFooterView(footer);
        activityDetailLv.setAdapter(new DetailAdapter(article.items));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class HeadHolder {
        @Bind(R.id.head_detail_imgCover)
        ImageView headDetailImgCover;
        @Bind(R.id.head_detail_tvTitle)
        TextView headDetailTvTitle;

        private final ImageLoader loader = ImageLoader.getInstance();

        HeadHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void initViews() {
            if (article.cover == null ||
                    article.cover.length() == 0 ||
                    article.cover.contains("/image/no_pic.jpg")) {
                headDetailImgCover.setVisibility(View.GONE);
                headDetailTvTitle.setBackgroundColor(Color.TRANSPARENT);
                headDetailTvTitle.setTextColor(ContextCompat
                        .getColor(DetailActivity.this, R.color.textColorDefault));
            } else {
                headDetailImgCover.setVisibility(View.VISIBLE);
                loader.displayImage(article.cover, headDetailImgCover);
            }
            headDetailTvTitle.setText(article.title);
        }
    }

    class FootHolder {
        private CommentFragment commentFragment;

        FootHolder() {
            commentFragment = (CommentFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.foot_detail_fragmentComment);
            commentFragment.setArticle(article);
        }
    }
}
