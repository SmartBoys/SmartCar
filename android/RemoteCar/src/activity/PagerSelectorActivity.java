package activity;

import java.util.ArrayList;
import java.util.List;
import preference.PreferenceSettings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.zjlh.remotecar.R;

public class PagerSelectorActivity extends Activity implements View.OnClickListener {
	private Button imgbt_back, imgbt_confirm;
	private ViewPager mViewPager;
	private ViewPagerAdapter mAdapter;
	public static final String TYPE = "type";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mode_navi);
		
		imgbt_back = (Button) findViewById(R.id.imgbt_back);
		imgbt_confirm = (Button) findViewById(R.id.imgbt_confirm);
		imgbt_back.setOnClickListener(this);
		imgbt_confirm.setOnClickListener(this);
		
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		mAdapter = new ViewPagerAdapter(this, getIntent().getIntExtra(TYPE, ViewPagerAdapter.TYPE_MODE));
		mViewPager.setAdapter(mAdapter);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbt_back:
			finish();
			break;
			
		case R.id.imgbt_confirm:
			if (mViewPager.getCurrentItem() == 0) {
				if (mAdapter.mType == ViewPagerAdapter.TYPE_MODE) {
					PreferenceSettings.setMode(PreferenceSettings.MODE_MOVE_BALL);
					startActivity(new Intent(this, MoveBallActivity.class));
				} else if (mAdapter.mType == ViewPagerAdapter.TYPE_NAVIGATION) {
					Intent intent = new Intent(this, NavigationActivity.class);
					intent.putExtra(NavigationActivity.TYPE, NavigationActivity.TYPE_N);
					startActivity(intent);
				}
			} else if (mViewPager.getCurrentItem() == 1) {
				if (mAdapter.mType == ViewPagerAdapter.TYPE_MODE) {
					PreferenceSettings.setMode(PreferenceSettings.MODE_USE_ARROWS);
					startActivity(new Intent(this, UseArrowsActivity.class));
				} else if (mAdapter.mType == ViewPagerAdapter.TYPE_NAVIGATION) {
					Intent intent = new Intent(this, NavigationActivity.class);
					intent.putExtra(NavigationActivity.TYPE, NavigationActivity.TYPE_FB);
					startActivity(intent);
				}
			} else if (mViewPager.getCurrentItem() == 2) {
				if (mAdapter.mType == ViewPagerAdapter.TYPE_MODE) {
					PreferenceSettings.setMode(PreferenceSettings.MODE_USE_GSENSOR);
					startActivity(new Intent(this, UseGsensorActivity.class));
				} else if (mAdapter.mType == ViewPagerAdapter.TYPE_NAVIGATION) {
					Intent intent = new Intent(this, NavigationActivity.class);
					intent.putExtra(NavigationActivity.TYPE, NavigationActivity.TYPE_RECT);
					startActivity(intent);
				}
			}
			break;
		}
	}
	
	public class ViewPagerAdapter extends PagerAdapter {
		private List<View> viewList;
		private View view1, view2, view3;
		public static final int TYPE_MODE = 1;
		public static final int TYPE_NAVIGATION = 2;
		private int mType;
		private Context mContext;

		public ViewPagerAdapter(Context context, int type) {
			mType = type;
			mContext = context;
			
			view1 = LayoutInflater.from(context).inflate(R.layout.modenavi_img, null);
			view2 = LayoutInflater.from(context).inflate(R.layout.modenavi_img, null);
			view3 = LayoutInflater.from(context).inflate(R.layout.modenavi_img, null);
			
			if (type == TYPE_MODE) {
				view1.setBackgroundResource(R.drawable.m1);
				view2.setBackgroundResource(R.drawable.m2);
				view3.setBackgroundResource(R.drawable.m3);
				
				TextView textView_mode_type = null, txt_title = null, img_title = null;
				
				textView_mode_type = (TextView) view1.findViewById(R.id.textView_mode_type);
				textView_mode_type.setText(R.string.mode1);
				txt_title = (TextView) view1.findViewById(R.id.txt_title);
				txt_title.setText(R.string.mode1_title);
				img_title = (TextView) view1.findViewById(R.id.img_title);
				img_title.setText(R.string.moveball);
				
				textView_mode_type = (TextView) view2.findViewById(R.id.textView_mode_type);
				textView_mode_type.setText(R.string.mode2);
				txt_title = (TextView) view2.findViewById(R.id.txt_title);
				txt_title.setText(R.string.mode2_title);
				img_title = (TextView) view2.findViewById(R.id.img_title);
				img_title.setText(R.string.user_arrows);
				
				textView_mode_type = (TextView) view3.findViewById(R.id.textView_mode_type);
				textView_mode_type.setText(R.string.mode3);
				txt_title = (TextView) view3.findViewById(R.id.txt_title);
				txt_title.setText(R.string.mode3_title);
				img_title = (TextView) view3.findViewById(R.id.img_title);
				img_title.setText(R.string.tiltdevice);
				
			} else if (type == TYPE_NAVIGATION) {
				view1.setBackgroundResource(R.drawable.navi_n);
				view2.setBackgroundResource(R.drawable.navi_fb);
				view3.setBackgroundResource(R.drawable.navi_rect);

				TextView textView_mode_type = null, txt_title = null, img_title = null;
				
				textView_mode_type = (TextView) view1.findViewById(R.id.textView_mode_type);
				textView_mode_type.setText(R.string.kind1);
				txt_title = (TextView) view1.findViewById(R.id.txt_title);
				txt_title.setText(R.string.kind1_title);
				img_title = (TextView) view1.findViewById(R.id.img_title);
				img_title.setText(R.string.movestate);
				
				textView_mode_type = (TextView) view2.findViewById(R.id.textView_mode_type);
				textView_mode_type.setText(R.string.kind2);
				txt_title = (TextView) view2.findViewById(R.id.txt_title);
				txt_title.setText(R.string.kind2_title);
				img_title = (TextView) view2.findViewById(R.id.img_title);
				img_title.setText(R.string.userstate);
				
				textView_mode_type = (TextView) view3.findViewById(R.id.textView_mode_type);
				textView_mode_type.setText(R.string.kind3);
				txt_title = (TextView) view3.findViewById(R.id.txt_title);
				txt_title.setText(R.string.kind3_title);
				img_title = (TextView) view3.findViewById(R.id.img_title);
				img_title.setText(R.string.tiltstate);
			}
			
			viewList = new ArrayList<View>();
			viewList.add(view1);
			viewList.add(view2);
			viewList.add(view3);
			
			view1.setOnClickListener(itemClickListener);
			view2.setOnClickListener(itemClickListener);
			view3.setOnClickListener(itemClickListener);
		}
		
		private View.OnClickListener itemClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v == view1) {
					if (mType == TYPE_MODE) {
						PreferenceSettings.setMode(PreferenceSettings.MODE_MOVE_BALL);
						startActivity(new Intent(mContext, MoveBallActivity.class));
					} else if (mType == TYPE_NAVIGATION) {
						Intent intent = new Intent(mContext, NavigationActivity.class);
						intent.putExtra(NavigationActivity.TYPE, NavigationActivity.TYPE_N);
						startActivity(intent);
					}
				} else if (v == view2) {
					if (mType == TYPE_MODE) {
						PreferenceSettings.setMode(PreferenceSettings.MODE_USE_ARROWS);
						startActivity(new Intent(mContext, UseArrowsActivity.class));
					} else if (mType == TYPE_NAVIGATION) {
						Intent intent = new Intent(mContext, NavigationActivity.class);
						intent.putExtra(NavigationActivity.TYPE, NavigationActivity.TYPE_FB);
						startActivity(intent);
					}
				} else if (v == view3) {
					if (mType == TYPE_MODE) {
						PreferenceSettings.setMode(PreferenceSettings.MODE_USE_GSENSOR);
						startActivity(new Intent(mContext, UseGsensorActivity.class));
					} else if (mType == TYPE_NAVIGATION) {
						Intent intent = new Intent(mContext, NavigationActivity.class);
						intent.putExtra(NavigationActivity.TYPE, NavigationActivity.TYPE_RECT);
						startActivity(intent);
					}
				}
			}
		};
		
		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = viewList.get(position);
			container.addView(view);
			return view;
		}
	}
}
