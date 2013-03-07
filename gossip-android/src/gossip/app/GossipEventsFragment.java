package gossip.app;

import gossip.app.data.Event;
import gossip.app.data.EventDAO;
import gossip.app.data.GossipActions;
import gossip.app.data.RemoteDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 显示事件集的Fragment，它归属于{@link GossipMainActivity}。
 * 
 * @author lins
 * @since API level 11
 */
public class GossipEventsFragment extends ListFragment implements OnScrollListener{
	public static boolean isUpdated = false;
	boolean mDualPane;// 是不是pad
	int mCurCheckPosition = 0;// 当前点击了哪一个, 默认点击第一个
	EventsRetrievalAsyncTask task;

	List<Event> events = new ArrayList<Event>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.getActivity().getActionBar();
		this.setHasOptionsMenu(true);// 说明自己有menu
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.events, container, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurCheckPosition);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		showDetails(position);
	}

	/**
	 * 设置fragmen内部的数据
	 * 
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// 调父类方法
		super.onActivityCreated(savedInstanceState);
		// 从Bundle里面获得数据来填充list
		Bundle extras = getActivity().getIntent().getExtras();
		if (extras == null)
			return;
		// Populate list with our static array of titles.
		List<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
		Iterator<String> it = extras.keySet().iterator();
		Event e;
		HashMap<String, String> map;
		while (it.hasNext()) {
			map = new HashMap<String, String>();
			e = (Event) extras.get(it.next());
			events.add(e);
			map.put("title", e.getTitle());
			map.put("time", e.getCreateTime());
			map.put("abstract", e.getContentAbstract());
			mapList.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), mapList,
				R.layout.event_lite,
				new String[] { "title", "time", "abstract" }, new int[] {
						R.id.textViewTitle, R.id.textViewTime,
						R.id.textViewAbstract });
		setListAdapter(adapter);
		System.out.println("list should be ok?!");

		// 检查是否是pad那样可以分块显示：是的话，初始化右边的detial fragment
		View detailsFrame = getActivity()
				.findViewById(R.id.fragmentEventDetail);
		mDualPane = detailsFrame != null
				&& detailsFrame.getVisibility() == View.VISIBLE;
		System.out.println("DualPanel? " + mDualPane);
		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}
		if (mDualPane) {
			// In dual-pane mode, the list view highlights the selected item.
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			// Make sure our UI is in the correct state.
			showDetails(mCurCheckPosition);
		}
	}

	final int MAX_LIST_COUNT = 20;

	private void populateListView(Collection<Event> c) {
		if (c == null || c.size() == 0)
			return;

		// 将原本的event补充到获取的event里面
		for (int i = 0; c.size() < MAX_LIST_COUNT && i < events.size(); i++) {
			c.add(events.get(i));
		}
		// 刷新某些值
		isUpdated = true;
		events.clear();
		List<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
		Iterator<?> it = c.iterator();
		Event e;
		HashMap<String, String> map;
		while (it.hasNext()) {
			map = new HashMap<String, String>();
			e = (Event) it.next();
			events.add(e);
			map.put("title", e.getTitle());
			map.put("time", e.getCreateTime());
			map.put("abstract", e.getContentAbstract());
			mapList.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), mapList,
				R.layout.event_lite,
				new String[] { "title", "time", "abstract" }, new int[] {
						R.id.textViewTitle, R.id.textViewTime,
						R.id.textViewAbstract });
		setListAdapter(adapter);
	}

	/**
	 * Helper function to show the details of a selected item, either by
	 * displaying a fragment in-place in the current UI, or starting a whole new
	 * activity in which it is displayed.
	 */
	void showDetails(int index) {
		mCurCheckPosition = index;

		if (mDualPane) {
			// 选中左边的item
			getListView().setItemChecked(index, true);

			// Check what fragment is currently shown, replace if needed.
			GossipEventDetailFragment details = (GossipEventDetailFragment) getFragmentManager()
					.findFragmentById(R.id.fragmentEventDetail);
			if (details == null || details.getShownIndex() != index) {
				// 实例化一个右边的块
				details = GossipEventDetailFragment.newInstance(index,
						events.get(index));

				// 更新右边的块--事件细节
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.fragmentEventDetail, details);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
				System.out.println("dual panel: replace fragment ok! ");
			}
		} else {
			// 假如就是一般的手机，启动activity
			System.out.println(index
					+ " clicked and going to strat detail activity.");
			Intent intent = new Intent();
			intent.setClass(getActivity(), GossipEventDetailActivity.class);
			intent.putExtra("index", index);
			intent.setAction(GossipActions.ACTION_EVENT_DETAIL);
			intent.putExtra("event", events.get(index));
			startActivity(intent);
		}

	}

	public List<Event> getEvents() {
		return events;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
		super.onCreateOptionsMenu(menu, menuInflater);
		menuInflater.inflate(R.menu.main_activity, menu);
		loadingItem = menu.findItem(R.id.menu_loading);
		loadingItem.setVisible(false);
		refreshItem = menu.findItem(R.id.menu_refresh);
	}

	MenuItem loadingItem;
	MenuItem refreshItem;

	/**
	 * @param flag
	 *            真为显示progress bar；假为显示刷新。
	 */
	private void changeActionBar(boolean flag) {
		if (flag) {
			refreshItem.setVisible(false);
			loadingItem.setVisible(true);
		} else {
			refreshItem.setVisible(true);
			loadingItem.setVisible(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this.getActivity(),
					GossipMainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_refresh:
			// 改变显示的action item
			changeActionBar(true);
			// 執行async task更新事件列表
			task = new EventsRetrievalAsyncTask();
			if (events.size() > 0)
				task.execute(events.get(0).getId());
			else
				task.execute(0);
			Log.i(this.getClass().getSimpleName(), "refresh to get new events");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		changeActionBar(false);
		if(task!=null)
			task.cancel(true);
		if(isUpdated){
			EventDAO eventDAO =new EventDAO(getActivity().getApplicationContext());
			eventDAO.updateOrInsertEvents(events);
			isUpdated = false;
			eventDAO.close();
		}
	}

	/**
	 * 用来获取Events数据的AsyncTask 三个泛型分别为输入的当前的event_id；Void；获得的event
	 * 
	 * @author lins 2012-8-27
	 */
	public class EventsRetrievalAsyncTask extends
			AsyncTask<Integer, Void, List<Event>> {
		boolean isStopped = false;
		Context context;

		public EventsRetrievalAsyncTask() {
			context = getActivity().getApplicationContext();
		}

		public EventsRetrievalAsyncTask setStopped() {
			isStopped = true;
			return this;
		}

		/**
		 * 获得事件从服务器. 这部分不在UI线程里面跑
		 */
		@Override
		protected List<Event> doInBackground(Integer... params) {
			List<Event> events;
			RemoteDAO remoteDAO = new RemoteDAO();
			int id = params[0];
			// 查看服务器的数据
			if (RemoteDAO.isNetworkAvailable(context)) {
				events = remoteDAO.getEventsFromServer(id);
			} else {
				return null;
			}
			return events;
		}

		/**
		 * 更新UI。 这部分在UI线程里面跑。
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(List<Event> result) {
			if(isStopped)
				return;
			super.onPostExecute(result);
			// 更新列表
			populateListView(result);
			// TOAST提示信息
			StringBuilder sb = new StringBuilder();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
			sb.append(sdf.format(new Date()))
					.append(getResources().getString(R.string.update_info1))
					.append(result.size())
					.append(getResources().getString(R.string.update_info2));
			Toast toast = Toast.makeText(context, sb.toString(),
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			// 改变显示的action item
			changeActionBar(false);
		}

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
}
