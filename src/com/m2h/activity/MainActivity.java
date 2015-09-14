package com.m2h.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.m2h.bean.UserInfo;
import com.m2h.fragment.FragHome;
import com.m2h.fragment.FragMe;
import com.m2h.fragment.FragMe.FragMeCallbacks;
import com.m2h.fragment.FragMeInfo;
import com.m2h.fragment.FragMeInfo.FragMeInfoCallbacks;
import com.m2h.fragment.FragSearch;
import com.m2h.utils.ChangeColorIconWithTextView;
import com.m2h.utils.NetState;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener, FragMeCallbacks, FragMeInfoCallbacks {

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mTabs;
	private List<ChangeColorIconWithTextView> mTabIndicator;
	private ImageView refImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); // 去标题栏
		setContentView(R.layout.activity_main);

		// 初始化ViewPager
		initViewPager();
		
		//监听网络连接
		NetState receiver = new NetState();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(receiver, filter);
		receiver.onReceive(this, null);

	}

	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setOffscreenPageLimit(2);
		mTabs = new ArrayList<Fragment>();
		mTabIndicator = new ArrayList<ChangeColorIconWithTextView>();
		refImg = (ImageView) findViewById(R.id.ref);
		
		refImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FragHome fHome = (FragHome) mTabs.get(0);
				fHome.refresh();				
			}
		});

		initTabIndicator();

		Fragment frag1 = new FragHome();
		Fragment frag2 = new FragSearch();
		Fragment frag3 = new FragMe();
		mTabs.add(frag1);
		mTabs.add(frag2);
		mTabs.add(frag3);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mTabs.get(arg0);
			}
		};

		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);

	}

	private void initTabIndicator() {
		ChangeColorIconWithTextView one = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_one);
		ChangeColorIconWithTextView two = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_two);
		ChangeColorIconWithTextView three = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_three);

		mTabIndicator.add(one);
		mTabIndicator.add(two);
		mTabIndicator.add(three);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);

		one.setIconAlpha(1.0f);
	}

	public void onClick(View v) {

		resetOtherTabs();

		switch (v.getId()) {
		case R.id.id_indicator_one:
			mTabIndicator.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;

		case R.id.id_indicator_two:
			mTabIndicator.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			mTabIndicator.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		}

	}

	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicator.size(); i++) {
			mTabIndicator.get(i).setIconAlpha(0);
		}
	}

	public void onPageScrollStateChanged(int arg0) {

	}

	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (positionOffset > 0) {
			ChangeColorIconWithTextView left = mTabIndicator.get(position);
			ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);

			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}

	public void onPageSelected(int arg0) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.main, menu);
				return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
		
	}

	public void onChangeToFragInfo(UserInfo info, List<Cookie> cookies,
			CookieStore cookieStore) {
		/*
		 * 1.跳转到登陆成功的界面FragMeInfo
		 */
		Bundle arguments = new Bundle();
		arguments.putParcelable("userinfo", info);

		Fragment fragmeInfo = new FragMeInfo(cookieStore);
		fragmeInfo.setArguments(arguments);
		FragmentManager manager = getSupportFragmentManager(); // 获取FragmentManger对象
		FragmentTransaction transaction = manager.beginTransaction(); // 获取fragment的事务操作
		transaction.replace(R.id.fragme, fragmeInfo);
		transaction.addToBackStack(null);
		transaction.commit();

		/*
		 * 2.传递cookies到FragHome
		 */
		/*FragHome fHome = (FragHome) mAdapter.instantiateItem(mViewPager, 0);
		fHome.getCookies(cookies);*/

		///?????
	}

	public void onChangeToFragMe() {
		Fragment fragme = new FragMe();

		FragmentManager manager = getSupportFragmentManager(); // 获取FragmentManger对象
		FragmentTransaction transaction = manager.beginTransaction(); // 获取fragment的事务操作
		transaction.replace(R.id.frag_userinfo, fragme);
		transaction.commit();
	}
	
	
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
