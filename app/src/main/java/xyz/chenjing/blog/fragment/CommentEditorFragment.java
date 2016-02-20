package xyz.chenjing.blog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.londonx.lutil.entity.LResponse;
import com.londonx.lutil.util.LRequestTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.chenjing.blog.CommentActivity;
import xyz.chenjing.blog.R;
import xyz.chenjing.blog.entity.Article;
import xyz.chenjing.blog.net.NetConst;
import xyz.chenjing.blog.net.Params;
import xyz.chenjing.blog.util.TextUtil;
import xyz.chenjing.blog.util.ViewShakeHelper;

/**
 * Created by london on 16/2/20.
 * for my comment
 */
public class CommentEditorFragment extends Fragment implements LRequestTool.OnResponseListener {
    @Bind(R.id.foot_detail_etName)
    EditText footDetailEtName;
    @Bind(R.id.foot_detail_etEmail)
    EditText footDetailEtEmail;
    @Bind(R.id.foot_detail_etContent)
    EditText footDetailEtContent;
    @Bind(R.id.foot_detail_pb)
    ProgressBar footDetailPb;
    @Bind(R.id.foot_detail_tvErr)
    TextView footDetailTvErr;
    @Bind(R.id.foot_detail_tvOk)
    TextView footDetailTvOk;

    private View rootView;
    private LRequestTool requestTool = new LRequestTool(this);
    private ViewShakeHelper shakeHelper = new ViewShakeHelper();
    private Article article;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_comment_editor, container, false);
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.foot_detail_tvCommit)
    protected void sendComment() {
        if (article == null) {
            return;
        }
        if (footDetailPb.getVisibility() == View.VISIBLE) {
            return;
        }
        if (footDetailEtName.getText().length() == 0) {
            shakeHelper.shake((View) footDetailEtName.getParent());
            return;
        }
        if (footDetailEtEmail.getText().length() == 0) {
            shakeHelper.shake((View) footDetailEtEmail.getParent());
            return;
        }
        if (footDetailEtContent.getText().length() == 0) {
            shakeHelper.shake((View) footDetailEtContent.getParent());
            return;
        }
        if (!TextUtil.isEmail(footDetailEtEmail.getText().toString())) {
            shakeHelper.shake((View) footDetailEtEmail.getParent());
            return;
        }
        footDetailTvOk.setVisibility(View.GONE);
        footDetailTvErr.setVisibility(View.GONE);
        footDetailPb.setVisibility(View.VISIBLE);
        requestTool.doPost(NetConst.ROOT + NetConst.API_LEAVE_MESSAGES,
                new Params()
                        .put("article_id", article.id)
                        .put("name", footDetailEtName.getText().toString())
                        .put("email", footDetailEtEmail.getText().toString())
                        .put("content", footDetailEtContent.getText().toString())
                , 0);
    }

    public void reset() {
        footDetailEtContent.setText("");
        footDetailEtName.setText("");
        footDetailEtEmail.setText("");
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public void onResponse(LResponse response) {
        footDetailPb.setVisibility(View.GONE);
        if (response.responseCode != 201) {
            footDetailTvErr.setVisibility(View.VISIBLE);
            return;
        }
        footDetailTvOk.setVisibility(View.VISIBLE);
        ((CommentActivity) getActivity()).onRefresh();
        reset();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
