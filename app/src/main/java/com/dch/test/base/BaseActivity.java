package com.dch.test.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：${User} on 2017/4/10 16:26
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public abstract class BaseActivity extends AppCompatActivity{

    public Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        unbinder = ButterKnife.bind(this);
//        initInject();
        initView();
        initData();
    }

//    public void initInject() {
//        getActivityComponent().inject(this);
//    }
//
//    public HomeActivityComponent getActivityComponent(){
//        return DaggerActivityComponent.builder()
//                .appComponent(getAppComponent())
//                .activityModule(getActivityModule())
//                .build();
//    }
//
//    public AppComponent getAppComponent() {
//        return DaggerAppComponent.builder()
//                .appModule(new AppModule((BaseApplication) getApplicationContext()))
//                .build();
//    }
//
//    protected HomeActivityModule getActivityModule() {
//        return new HomeActivityModule(this);
//    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract int setLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
