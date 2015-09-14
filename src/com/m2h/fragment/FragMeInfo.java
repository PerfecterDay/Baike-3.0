package com.m2h.fragment;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.m2h.activity.R;
import com.m2h.bean.UserInfo;
import com.m2h.utils.GetUrlImage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class FragMeInfo extends Fragment {

	private View view;
	private ImageView image;
	private TextView name;
	private TextView gender;
	private Button exitBtn;
	private UserInfo userInfo;
	private CookieStore cookieStore;

	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/index.php";
	private String imgURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/";
	// private String imgURL = "http://192.168.1.106/HDWiki/";
	private String logoutURL = devbaseURL + "?app-logout";

	public FragMeInfo() {
		super();
	}

	public interface FragMeInfoCallbacks {
		public void onChangeToFragMe();
	}

	private FragMeInfoCallbacks mCallbacks = new FragMeInfoCallbacks() {

		public void onChangeToFragMe() {

		}
	};

	private static FragMeInfoCallbacks sDummyCallbacks = new FragMeInfoCallbacks() {
		public void onChangeToFragMe() {

		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof FragMeInfoCallbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		mCallbacks = (FragMeInfoCallbacks) activity;

	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

	public FragMeInfo(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	private Handler handle = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				System.out.println("111");
				Bitmap bmp = (Bitmap) msg.obj;
				image.setImageBitmap(bmp);
				break;
			}
		};
	};

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.user_info, null);

		init();
		/*
		 * exitBtn.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View arg0) { logout();
		 * mCallbacks.onChangeToFragMe();
		 * 
		 * } });
		 */

		return view;
	}

	private void logout() {
		// Log.e("Logout", "Logout");
		Thread subThread = new Thread(new Runnable() {

			public void run() {

				try {

					HttpPost httpPost = new HttpPost(logoutURL);

					DefaultHttpClient httpClient = new DefaultHttpClient();
					httpClient.setCookieStore(cookieStore);
					httpClient.execute(httpPost);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		subThread.start();
		try {
			subThread.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	private void init() {

		image = (ImageView) view.findViewById(R.id.info_image);
		name = (TextView) view.findViewById(R.id.info_name);
		gender = (TextView) view.findViewById(R.id.info_gender);

		exitBtn = (Button) view.findViewById(R.id.info_exit);

		userInfo = getArguments().getParcelable("userinfo");
		// cookieStore = getArguments().getParcelable("cookiestore");
		name.setText(userInfo.getName());
		if (userInfo.getGender() == 1) {
			gender.setText("男");
		} else {
			gender.setText("女");
		}

		final String url = imgURL + userInfo.getImageUrl();
		// Log.i("TAG", url);

		new Thread(new Runnable() {

			public void run() {
				Bitmap bmp = GetUrlImage.getUrlimage(url);
				Message msg = new Message();
				msg.what = 0;
				msg.obj = bmp;
				handle.sendMessage(msg);
			}
		}).start();

		exitBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				logout();
				mCallbacks.onChangeToFragMe();
			}
		});
	}

}
