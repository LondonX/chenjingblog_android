package xyz.chenjing.blog.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.chenjing.blog.R;
import xyz.chenjing.blog.entity.Article;
import xyz.chenjing.blog.fragment.MainPageFragment;

/**
 * Created by london on 16/2/19.
 * ViewPagerAdapter in {@link xyz.chenjing.blog.MainActivity}
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    String[] titles;
    List<MainPageFragment> pages;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles = context.getResources().getStringArray(R.array.pages);
        pages = new ArrayList<>(titles.length);
        pages.add(new MainPageFragment().setArticleType(Article.Type.all));
        pages.add(new MainPageFragment().setArticleType(Article.Type.work));
        pages.add(new MainPageFragment().setArticleType(Article.Type.collect));
        pages.add(new MainPageFragment().setArticleType(Article.Type.diary));
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
