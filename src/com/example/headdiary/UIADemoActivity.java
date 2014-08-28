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
 * ע��ȷ����w.song.android.widget-1.0.4.jar������Ŀ
 * w.song.android.widget-1.0.4.jar���ص�ַ http://download.csdn.net/detail/swadair/4289692
 * QQ 2636852590
 * @author w.song
 * @version 1.0.1
 * @date 2012-5-9
 */
public class UIADemoActivity extends Activity {
	private String tag = "UIADemoActivity";
	private String[] title = { "��ע", "����" };
	private int[] photo = { R.drawable.nav_menu_home, R.drawable.nav_menu_hot};
	private int[] photoSelected = { R.drawable.nav_menu_home_selected,
			R.drawable.nav_menu_hot_selected,};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uiademo_ui);
		/* ��ȡ��� */
		final RollNavigationBar rnb = (RollNavigationBar) findViewById(R.id.uiademo_ui_RollNavigationBar);
		final SlidePageView spv = (SlidePageView) findViewById(R.id.uiademo_ui_SlidePageView);
		/* ���ƶ�̬���� */
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		for (int i = 0; i < title.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", title[i]);
			map.put("photo", photo[i]);
			map.put("photoSelected", photoSelected[i]);
			list.add(map);
		}
		/* ���û������Ļ���ʱ�䣬ʱ�䷶Χ��0.1~1s�����ڷ�Χ��Ĭ��0.1s */
		rnb.setSelecterMoveContinueTime(0.1f);// ���Բ����ã�Ĭ��0.1s
		/* ���û�������ʽ��ͼƬ�� */
		//rnb.setSelecterDrawableSource(R.drawable.nav_menu_bg);// ����
		/* ���õ������ı�ѡλ�� */
		rnb.setSelectedChildPosition(0);// ���Բ�����

		/* ��������չ */
		final MyNavigationBarAdapter adapter = new MyNavigationBarAdapter(this,
				list);
		rnb.setAdapter(adapter);// ����
		rnb.setNavigationBarListener(new RollNavigationBar.NavigationBarListener() {
			/**
			 * position ��ѡλ�� view Ϊ������ event �ƶ��¼�
			 */
			@Override
			public void onNavigationBarClick(int position, View view,
					MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// ����ȥʱ
					spv.setCurrPagePosition(position);
					spv.CurrPageScrollToScreenCenter();
					break;
				case MotionEvent.ACTION_MOVE:// �ƶ���

					break;
				case MotionEvent.ACTION_UP:// ̧��ʱ

					break;
				}

			}

		});
		
		/* ����ҳ�棨���ģ����� */
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
	 * ��������չ
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
		 * ��ȡÿ�����
		 * 
		 * @param position
		 *            �����λ��
		 * @param contextView
		 *            ���
		 * @param parent
		 *            �ϲ����
		 */
		@Override
		public View getView(int position, View contextView, ViewGroup parent) {
			mInflater.inflate(R.layout.item, (ViewGroup) contextView);
			RollNavigationBar rollNavigationBar = (RollNavigationBar) parent;
			/* ��ȡ��� */
			ImageView imageView = (ImageView) contextView
					.findViewById(R.id.image_view);
			TextView titleView = (TextView) contextView
					.findViewById(R.id.title_view);

			/* ��ȡ���� */
			String title = "" + list.get(position).get("title");
			int photo = (Integer) list.get(position).get("photo");
			int photoSelected = (Integer) list.get(position).get(
					"photoSelected");

			/* ������ò��� */
			// ����ѡ���뱻ѡ��ͼƬ
			if (position == rollNavigationBar.getSelectedChildPosition()) {// ��ѡ��
				imageView.setBackgroundResource(photoSelected);
			} else {// û��ѡ��
				imageView.setBackgroundResource(photo);
				titleView.setTextColor(Color.argb(0, 0xff, 0, 0));
			}						
			titleView.setText(title);

			return contextView;
		}

	}
}
