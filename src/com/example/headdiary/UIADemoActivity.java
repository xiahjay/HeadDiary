package com.example.headdiary;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.w.song.widget.navigation.RollNavigationBar;
import cn.w.song.widget.navigation.adapter.RollNavigationBarAdapter;
import cn.w.song.widget.scroll.SlidePageView;
import cn.w.song.widget.scroll.SlidePageView.OnPageViewChangedListener;

/**
 * 注意确保将w.song.android.widget-1.0.4.jar导入项目
 * w.song.android.widget-1.0.4.jar下载地址 http://download.csdn.net/detail/swadair/4289692
 * QQ 2636852590
 * @author w.song
 * @version 1.0.1
 * @date 2012-5-9
 */
public class UIADemoActivity extends Activity {
	private String tag = "UIADemoActivity";
	private String[] title = { "关注", "热门" };
	private int[] photo = { R.drawable.nav_menu_home, R.drawable.nav_menu_hot};
	private int[] photoSelected = { R.drawable.nav_menu_home_selected,
			R.drawable.nav_menu_hot_selected,};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uiademo_ui);
		/* 获取组件 */
		final RollNavigationBar rnb = (RollNavigationBar) findViewById(R.id.uiademo_ui_RollNavigationBar);
		final SlidePageView spv = (SlidePageView) findViewById(R.id.uiademo_ui_SlidePageView);
		/* 定制动态数据 */
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		for (int i = 0; i < title.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", title[i]);
			map.put("photo", photo[i]);
			map.put("photoSelected", photoSelected[i]);
			list.add(map);
		}
		/* 设置滑动条的滑动时间，时间范围在0.1~1s，不在范围则默认0.1s */
		rnb.setSelecterMoveContinueTime(0.1f);// 可以不设置，默认0.1s
		/* 设置滑动条样式（图片） */
		//rnb.setSelecterDrawableSource(R.drawable.nav_menu_bg);// 必须
		/* 设置导航栏的被选位置 */
		rnb.setSelectedChildPosition(0);// 可以不设置

		/* 导航栏扩展 */
		final MyNavigationBarAdapter adapter = new MyNavigationBarAdapter(this,
				list);
		rnb.setAdapter(adapter);// 必须
		rnb.setNavigationBarListener(new RollNavigationBar.NavigationBarListener() {
			/**
			 * position 被选位置 view 为导航栏 event 移动事件
			 */
			@Override
			public void onNavigationBarClick(int position, View view,
					MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 按下去时
					spv.setCurrPagePosition(position);
					spv.CurrPageScrollToScreenCenter();
					break;
				case MotionEvent.ACTION_MOVE:// 移动中

					break;
				case MotionEvent.ACTION_UP:// 抬手时

					break;
				}

			}

		});
		
		/* 滚动页面（正文）监听 */
		spv.setOnPageViewChangedListener(new OnPageViewChangedListener() {

			@Override
			public void OnPageViewChanged(int currPagePosition,
					View currPageView) {
				rnb.setSelectedChildPosition(currPagePosition);
				rnb.refreshView(adapter);
			}
		});
	}

	/**
	 * 导航栏扩展
	 * 
	 * @author w.song
	 * @version 1.0.1
	 * @date 2012-4-22
	 */
	class MyNavigationBarAdapter extends RollNavigationBarAdapter {
		private List<Map<String, Object>> list;
		private LayoutInflater mInflater;

		public MyNavigationBarAdapter(Activity activity,
				List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(activity);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * 获取每个组件
		 * 
		 * @param position
		 *            组件的位置
		 * @param contextView
		 *            组件
		 * @param parent
		 *            上层组件
		 */
		@Override
		public View getView(int position, View contextView, ViewGroup parent) {
			mInflater.inflate(R.layout.item, (ViewGroup) contextView);
			RollNavigationBar rollNavigationBar = (RollNavigationBar) parent;
			/* 获取组件 */
			ImageView imageView = (ImageView) contextView
					.findViewById(R.id.image_view);
			TextView titleView = (TextView) contextView
					.findViewById(R.id.title_view);

			/* 获取参数 */
			String title = "" + list.get(position).get("title");
			int photo = (Integer) list.get(position).get("photo");
			int photoSelected = (Integer) list.get(position).get(
					"photoSelected");

			/* 组件设置参数 */
			// 区分选择与被选择图片
			if (position == rollNavigationBar.getSelectedChildPosition()) {// 被选择
				imageView.setBackgroundResource(photoSelected);
			} else {// 没被选择
				imageView.setBackgroundResource(photo);
				titleView.setTextColor(Color.argb(0, 0xff, 0, 0));
			}						
			titleView.setText(title);

			return contextView;
		}

	}
}
