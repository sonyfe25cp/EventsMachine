package gossip.app;

import gossip.app.data.Event;
import gossip.app.data.EventDAO;
import gossip.app.data.RemoteDAO;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 显示各种事件的activity
 * 
 * @author lins
 */
public class GossipMainActivity extends Activity {
	// This is the Adapter being used to display the list's data
	SimpleAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActionBar();
		super.onCreate(savedInstanceState);
		// 设置布局
		this.setContentView(R.layout.main);
		System.out.println("now i'll check the net!");
		if (!RemoteDAO.isNetworkAvailable(this)) {// 只在create时显示
			Toast toast = Toast.makeText(getApplicationContext(),
					"网络连接不可用请打开GPRS或者WIFI", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	/**
	 * 保存、更新数据进入数据库
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onStop() {
		super.onStop();
		System.out.println("the main activity has been stopped!");

//		// 更新所有关的action item
//		if (refreshItem != null) {
//			refreshItem.setIcon(R.drawable.ic_menu_refresh);
//			refreshItem.setEnabled(true);
//		}

		// 数据入库
		if (GossipEventsFragment.isUpdated) {
			// 获得fragment
			GossipEventsFragment fragment = (GossipEventsFragment) getFragmentManager()
					.findFragmentById(R.id.fragmentEvents);
			List<Event> events = fragment.getEvents();
			// 逐个将events放入数据库
			EventDAO eventDAO = new EventDAO(GossipMainActivity.this);
			eventDAO.updateOrInsertEvents(events);
			// 删除过期的数据
			eventDAO.removeExpired();
			// 关闭各种连接
			eventDAO.close();
			// 设置标志位为未更新
			GossipEventsFragment.isUpdated = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getActionBar().setDisplayShowTitleEnabled(false);
//		getFragmentManager().g
		return super.onCreateOptionsMenu(menu);
	}
	
	

	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// super.onConfigurationChanged(newConfig);
	//
	// FragmentTransaction ft = getFragmentManager().beginTransaction();
	// Fragment f = getFragmentManager().findFragmentById(
	// R.id.fragmentEventDetail);
	// if (f != null) {
	// System.out.println("remove the detail fragment!");
	// ft.remove(f);
	// ft.commit();
	// }
	// }
}
