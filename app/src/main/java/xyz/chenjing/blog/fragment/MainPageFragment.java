package xyz.chenjing.blog.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.londonx.lutil.entity.LResponse;
import com.londonx.lutil.impl.NextPageLoader;
import com.londonx.lutil.util.LRequestTool;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import xyz.chenjing.blog.DetailActivity;
import xyz.chenjing.blog.MainActivity;
import xyz.chenjing.blog.R;
import xyz.chenjing.blog.adapter.MainAdapter;
import xyz.chenjing.blog.adapter.MainPagerAdapter;
import xyz.chenjing.blog.entity.Article;
import xyz.chenjing.blog.net.NetConst;
import xyz.chenjing.blog.net.Params;

/**
 * Created by london on 16/2/19.
 * page in {@link MainActivity}
 * bind data with {@link MainPagerAdapter}
 */
public class MainPageFragment extends Fragment implements
        LRequestTool.OnResponseListener,
        NextPageLoader.NextPagerTrigger {
    @Bind(R.id.page_main_srl)
    SwipeRefreshLayout pageMainSrl;
    @Bind(R.id.page_main_lv)
    ListView pageMainLv;

    private View rootView;
    private Article.Type type;
    private LRequestTool requestTool;
    private List<Article> articles;

    private int currentPage = 1;
    private boolean hasMore = true;
    private boolean isLoading = false;
    private MainAdapter adapter;
    private View headerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.page_main, container, false);
        }
        ButterKnife.bind(this, rootView);
        if (headerView == null && pageMainLv.getHeaderViewsCount() == 0) {
            headerView = inflater.inflate(R.layout.head_main, pageMainLv, false);
            pageMainLv.addHeaderView(headerView);
        }
        initViews();
        if (type != null) {
            getData();
        }
        return rootView;
    }

    private void initViews() {
        int startOffset = getResources().getDimensionPixelSize(R.dimen.subject_list_refresh_start_offset);
        int endOffset = getResources().getDimensionPixelSize(R.dimen.subject_list_refresh_end_offset);
        pageMainSrl.setProgressViewOffset(false, startOffset, endOffset);
        pageMainLv.setOnScrollListener(new NextPageLoader(
                pageMainLv, this, ContextCompat.getColor(getContext(), R.color.colorAccent)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public MainPageFragment setArticleType(Article.Type type) {
        this.type = type;
        if (pageMainLv != null) {
            getData();
        }
        return this;
    }

    private void getData() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        if (requestTool == null) {
            requestTool = new LRequestTool(this);
        }
        pageMainSrl.post(new Runnable() {
            @Override
            public void run() {
                pageMainSrl.setRefreshing(true);
            }
        });
        requestTool.doGet(NetConst.ROOT + NetConst.API_ARTICLES,
                new Params().put("article_type", type).put("page", currentPage),
                0);
    }

    @Override
    public void onResponse(LResponse response) {
        isLoading = false;
        if (pageMainSrl == null) {
            return;
        }
        pageMainSrl.post(new Runnable() {
            @Override
            public void run() {
                pageMainSrl.setRefreshing(false);
            }
        });
        if (response.responseCode != 200) {
            Snackbar.make(pageMainLv, getString(com.londonx.lutil.R.string.server_error_)
                    + response.responseCode, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.refresh, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRefresh();
                        }
                    })
                    .show();
            return;
        }
        List<Article> appendData = new Gson()
                .fromJson(response.body, new TypeToken<List<Article>>() {
                }.getType());
        hasMore = appendData.size() == 12;
        if (currentPage == 1) {
            articles = appendData;
            adapter = new MainAdapter(articles);
            pageMainLv.setAdapter(adapter);
        } else {
            articles.addAll(appendData);
            adapter.setNewData(articles);
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        hasMore = true;
        getData();
    }

    @Override
    public void onLoadMore() {
        if (isLoading || !hasMore) {
            return;
        }
        currentPage++;
        getData();
    }

    @OnItemClick(R.id.page_main_lv)
    public void inspireArrticle(int position) {
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        detailIntent.putExtra("article", articles.get(position - 1));//-1 header(s)
        startActivity(detailIntent);
    }
}
