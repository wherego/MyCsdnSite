package com.dch.test.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dch.test.R;
import com.dch.test.base.BaseActivity;
import com.dch.test.base.BaseApplication;
import com.dch.test.base.BaseFragment;
import com.dch.test.contract.HomeContract;
//import com.dch.test.di.app.AppModule;
//import com.dch.test.di.app.DaggerAppComponent;
import com.dch.test.contract.presenter.HomePresenter;
import com.dch.test.di.activity.DaggerHomeActivityComponent;
import com.dch.test.di.activity.HomeActivityComponent;
import com.dch.test.di.activity.HomePresenterModule;
import com.dch.test.di.app.AppComponent;
import com.dch.test.di.app.DaggerAppComponent;
import com.dch.test.repository.ArticalRepositoryComponent;
import com.dch.test.ui.fragment.CsdnBlogFragment;
import com.dch.test.ui.fragment.GankAndroidFragment;
import com.dch.test.ui.fragment.GankMeiziFragment;
import com.dch.test.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 作者：MrCoder on 2017年4月10日 16:17:38
 * 描述：主界面
 * 邮箱：codermr@163.com
 */
public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<BaseFragment> mFragmentList = new ArrayList<>();
    private String[] titles = {"Android", "妹纸", "博客"};
    private int currentIndex = 0;

    @Inject
    HomePresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.tabs_home)
    TabLayout tabs_home;
    @BindView(R.id.vp_home)
    ViewPager mViewPager;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "点击切换主题", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(HomeActivity.this, "切换成功", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        mFragmentList.add(GankAndroidFragment.newInstance());
        mFragmentList.add(GankMeiziFragment.newInstance());
        mFragmentList.add(CsdnBlogFragment.newInstance());

        RxBus.getInstance().registSubject(new RxBus.CallBack<String>() {
            @Override
            public void onNext(String s) {
                toolbar.setTitle(s);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        mViewPager.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager()));
        tabs_home.setupWithViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                RxBus.getInstance().post(titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(0);
        RxBus.getInstance().post(titles[0]);

        initInject();
    }
    public void initInject() {
        DaggerHomeActivityComponent.builder()
                .homePresenterModule(new HomePresenterModule(mFragmentList.get(currentIndex)))
                .articalRepositoryComponent(((BaseApplication) getApplication()).getArticalRepositoryComponent())
                .build().inject(this);
    }

    protected HomePresenterModule getActivityModule() {
        return new HomePresenterModule(mFragmentList.get(currentIndex));
    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class HomeViewPagerAdapter extends FragmentPagerAdapter {

        public HomeViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
