package gossip.app;

import gossip.app.data.Event;
import gossip.app.data.News;
import gossip.app.data.NewsDAO;
import gossip.app.data.RemoteDAO;
import gossip.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

/**
 * 用于显示事件详细的块
 * 
 * @author lins
 */
public class GossipEventDetailFragment extends ListFragment  implements OnScrollListener{
	Event event = null;
	List<News> newsList;
	int index = 0;
	EventDetailRetrievalAsyncTask task;
	boolean isUpdated = false;

	public static GossipEventDetailFragment newInstance(int index, Event event) {
		GossipEventDetailFragment f = new GossipEventDetailFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		args.putSerializable("event", event);
		f.setArguments(args);

		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}
		return inflater.inflate(R.layout.event_detail, container, false);
	}

	/**
	 * 
	 * 进行各种数据操作
	 * 
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		/** 从Bundle里面获得数据来填充list **/
		Bundle extras = getArguments();
		// 处理传入数据为空的情况
		if (extras == null || (event = (Event) extras.get("event")) == null) {
			// 设置事件
			TextView textView = (TextView) this.getActivity().findViewById(
					R.id.textViewEvent);
			textView.setAlpha((float) 0.6);
			textView.setText("事件细节");
			System.out.println("event detail process null event");
			return;
		}

		index = extras.getInt("index");
		// 设置事件
		TextView textView = (TextView) this.getActivity().findViewById(
				R.id.textViewEvent);
		textView.setAlpha((float) 0.6);
		textView.setText(event.getTitle());
		// 使用async task更新事件对应的新闻
		task = new EventDetailRetrievalAsyncTask();
		task.execute(event.getPages().toArray(
				new Integer[event.getPages().size()]));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		System.out.format("news %d is clicked.", position);
		Intent intent = new Intent();
		intent.setClass(getActivity(), GossipNewsActivity.class);
		intent.putExtra("event", event);
		intent.putExtra("news", newsList.get(position));
		intent.putExtra("position", position);
		intent.putExtra("news-id", event.getPages().get(position));
		startActivity(intent);
	}

	@Override
	public void onStop() {
		super.onStop();
		if(task!=null)
			task.cancel(true);
		if(isUpdated){
			NewsDAO newsDAO =new NewsDAO(getActivity().getApplicationContext());
			newsDAO.updateOrInsertNews(newsList);
			newsDAO.close();
			isUpdated = false;
		}
	}
	
	/**
	 * 用来获取EventDetail数据,也就是一些新闻摘要,的AsyncTask
	 * 三个泛型分别为输入的当前的event_id；Void；获得的event
	 * 
	 * @author lins 2012-8-27
	 */
	public class EventDetailRetrievalAsyncTask extends
			AsyncTask<Integer, Void, List<News>> {
		boolean flag = false;// flase表示连接已开
		// boolean stop = false;// activity是不是被停止

		/**
		 * 获得事件,包括从数据库，从服务器. 这部分冰不在UI线程里面跑
		 */
		@Override
		protected List<News> doInBackground(Integer... params) {
			List<News> newsList = new ArrayList<News>();
			News n = null;

			try {
				RemoteDAO remoteDAO = new RemoteDAO();
				Activity activty = getActivity();
				NewsDAO newsDAO = new NewsDAO(activty.getApplicationContext());

				for (int i : params) {
					// 先读数据库
					n = newsDAO.getNewsByIds(i);
					if (n == null) {// 再看服务器
						// 然后查看服务器的数据
						if (!flag
								&& RemoteDAO.isNetworkAvailable(activty
										.getApplicationContext())) {
							n = remoteDAO.getNewsFromServer(i);
							n.setId(i);
							n.setEventId(event.getId());
							isUpdated = true;
						} else {
							flag = true;// 连接未开启
						}
					}

					if (n != null)
						newsList.add(n);
				}
				newsDAO.close();
			} catch (Exception e) {

			}

			return newsList;
		}

		/**
		 * 更新UI。 这部分在UI线程里面跑。
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(List<News> result) {
			super.onPostExecute(result);
			try {
				if (result == null) {
					if (flag) {// 只提示一次网络部可用
						Toast toast = Toast.makeText(getActivity()
								.getApplicationContext(),
								R.string.net_unavailable, Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					} else {// 网络可用，但是服务器可能关闭了
						Toast toast = Toast.makeText(getActivity()
								.getApplicationContext(),
								R.string.server_unavailable, Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
				} else {
					// Populate list with our static array of titles.
					List<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
					newsList = new ArrayList<News>();
					for (News n : result) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", StringUtils.getAbstract(n.getTitle(),10,1));
						map.put("time", StringUtils.getAbstract(n.getDate(),10,0));
						map.put("abstract", StringUtils.getAbstract(n.getBody(),80,2));
						mapList.add(map);
						newsList.add(n);
					}
					SimpleAdapter adapter = new SimpleAdapter(getActivity(),
							mapList, R.layout.event_lite, new String[] {
									"title", "time", "abstract" }, new int[] {
									R.id.textViewTitle, R.id.textViewTime,
									R.id.textViewAbstract });
					setListAdapter(adapter);
					System.out.println("event details list is okay now.");
				}
			} catch (Exception e) {

			}
		}

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		System.out.println("now i scroll the event detail fragment");
		
	}
}
