package xyz.chenjing.blog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.londonx.lutil.util.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.chenjing.blog.adapter.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.activity_main_pager)
    ViewPager activityMainPager;
    @Bind(R.id.activity_main_tab)
    TabLayout activityMainTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        activityMainPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), this));
        activityMainTab.setupWithViewPager(activityMainPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contact:
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case R.id.action_open_source:
                ToastUtil.show("¯\\_(ツ)_/¯");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
