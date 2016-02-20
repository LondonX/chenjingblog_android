package xyz.chenjing.blog;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.londonx.lutil.entity.LResponse;
import com.londonx.lutil.util.LRequestTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.chenjing.blog.net.NetConst;
import xyz.chenjing.blog.net.Params;
import xyz.chenjing.blog.util.TextUtil;
import xyz.chenjing.blog.util.ViewShakeHelper;

/**
 * Created by london on 16/2/20.
 * contact
 */
public class ContactActivity extends AppCompatActivity implements LRequestTool.OnResponseListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.activity_contact_etName)
    EditText activityContactEtName;
    @Bind(R.id.activity_contact_etEmail)
    EditText activityContactEtEmail;
    @Bind(R.id.activity_contact_etPhone)
    EditText activityContactEtPhone;
    @Bind(R.id.activity_contact_etContent)
    EditText activityContactEtContent;
    @Bind(R.id.activity_contact_pb)
    ProgressBar activityContactPb;
    @Bind(R.id.activity_contact_tvErr)
    TextView activityContactTvErr;
    @Bind(R.id.activity_contact_tvOk)
    TextView activityContactTvOk;

    private ViewShakeHelper shakeHelper = new ViewShakeHelper();
    private LRequestTool requestTool = new LRequestTool(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.activity_contact_tvCommit)
    protected void sendComment() {
        if (activityContactPb.getVisibility() == View.VISIBLE) {
            return;
        }
        if (activityContactEtName.getText().length() == 0) {
            shakeHelper.shake((View) activityContactEtName.getParent());
            return;
        }
        if (activityContactEtEmail.getText().length() == 0) {
            shakeHelper.shake((View) activityContactEtEmail.getParent());
            return;
        }
        if (activityContactEtContent.getText().length() == 0) {
            shakeHelper.shake((View) activityContactEtContent.getParent());
            return;
        }
        if (!TextUtil.isEmail(activityContactEtEmail.getText().toString())) {
            shakeHelper.shake((View) activityContactEtEmail.getParent());
            return;
        }
        activityContactTvOk.setVisibility(View.GONE);
        activityContactTvErr.setVisibility(View.GONE);
        activityContactPb.setVisibility(View.VISIBLE);
        requestTool.doPost(NetConst.ROOT + NetConst.API_CONTACT_ME,
                new Params()
                        .put("name", activityContactEtName.getText().toString())
                        .put("email", activityContactEtEmail.getText().toString())
                        .put("content", activityContactEtContent.getText().toString())
                        .put("phone", activityContactEtPhone.getText().toString())
                , 0);
    }

    public void reset() {
        activityContactEtContent.setText("");
        activityContactEtName.setText("");
        activityContactEtEmail.setText("");
        activityContactEtPhone.setText("");
    }

    @Override
    public void onResponse(LResponse response) {
        activityContactPb.setVisibility(View.GONE);
        if (response.responseCode != 201) {
            activityContactTvErr.setVisibility(View.VISIBLE);
            return;
        }
        activityContactTvOk.setVisibility(View.VISIBLE);
        reset();
    }
}
