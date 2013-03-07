package gossip.app;

import gossip.app.data.Event;
import gossip.app.data.News;
import gossip.app.data.NewsDAO;
import gossip.app.data.RemoteDAO;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * 显示新闻的activity
 * 
 * @author lins 2012-8-24
 */
public class GossipNewsActivity extends Activity implements OnGestureListener {
	private ViewFlipper flipper;
	LayoutInflater inflater;
	private GestureDetector detector;
	Event event;
	int newsIdxInEvent = 0;
	ArrayList<Integer> flipper2News = new ArrayList<Integer>();// 表示flipper里面第i个view表示的是第几个news

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);

		/** 一堆数据的初始化 **/
		detector = new GestureDetector(this);// 检测手势
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper2);// 显示新闻flipper
		inflater = LayoutInflater.from(this.getApplicationContext());// 读取额外的布局文件用的对象

		/** 显示数据 **/
		Bundle extras = getIntent().getExtras();// 获得从前一个activity传来的信息
		if (extras == null) {
			finish();
			return;
		}
		News n = (News) extras.get("news");
		event = (Event) extras.get("event");
		newsIdxInEvent = extras.getInt("position");
		// NewsRetrievalAsyncTask task = new NewsRetrievalAsyncTask();
		// System.out.format(
		// "now i'll retrieve the news %d from both db and server.",
		// currentNewsId);
		// task.execute(currentNewsId);
		// 将数据 填入view
		addFlipperItem(n);

		System.out.println("now i'll show u the news.");
	}

	/**
	 * 往flipper里面添加news
	 * 
	 * @param n
	 *            当新闻为空时，创建一个只有progress bar的view并添加到flipper里面；否则正常。
	 * @param pos
	 *            当pos为负数时，则是往flipper的末尾添加
	 */
	public void addFlipperItem(final News n) {
		View v = (View) inflater.inflate(R.layout.news_inflipper, null);
		if (n != null) {
			ProgressBar progressBar = (ProgressBar) v
					.findViewById(R.id.progressBar1);
			progressBar.setVisibility(View.GONE);
			TextView title = (TextView) v.findViewById(R.id.textViewTitle);
			title.setText(n.getTitle());
			title.setVisibility(View.VISIBLE);
			TextView etc = (TextView) v.findViewById(R.id.textViewETC);
			etc.setText(n.getDate());
			etc.setVisibility(View.VISIBLE);
			TextView body = (TextView) v.findViewById(R.id.textViewBody);
			body.setText(n.getBody());
			body.setVisibility(View.VISIBLE);
			Button button = (Button) v.findViewById(R.id.button1);
			button.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					Uri uri = Uri.parse(n.getUrl());
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				}
			});
			button.setVisibility(View.VISIBLE);
		}
		flipper.addView(v);
		flipper2News.add(newsIdxInEvent);
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// 跳转动画，暂无
		// this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
		// R.anim.push_left_in));
		// this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
		// R.anim.push_left_out));
		if (e1.getX() - e2.getX() > 100) {
			newsIdxInEvent++;
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.animator.left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.animator.left_out));
		} else if (e1.getX() - e2.getX() < -100) {
			newsIdxInEvent--;
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.animator.right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.animator.right_out));
		}
		// 假如是所有新闻里面的第一个或者最后一个
		if (newsIdxInEvent == -1 || newsIdxInEvent == event.getPages().size()) {
			finish();
			return true;
		}
		// 假如不在flipper里面
		int fvidx = flipper2News.indexOf(newsIdxInEvent);
		if (fvidx == -1) {
			addFlipperItem(null);
			this.flipper.setDisplayedChild(flipper2News.size() - 1);
			// 多线程更新
			NewsRetrievalAsyncTask task = new NewsRetrievalAsyncTask();
			task.execute(event.getPages().get(newsIdxInEvent));
			System.out.println("add new item");
		} else {
			this.flipper.setDisplayedChild(fvidx);
		}
		return true;
	}

	public void onLongPress(MotionEvent e) {

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.news_activity, menu);
	    this.getActionBar().setDisplayShowTitleEnabled(false);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, GossipMainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        case R.id.menu_revert:
	        	Log.i(this.getClass().getSimpleName(),"news-activity revert to main");
	        	finish();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	/**
	 * 用来获取news数据的AsyncTask 三个泛型分别为输入的news_id；Void；获得的news
	 * 
	 * @author lins 2012-8-27
	 */
	public class NewsRetrievalAsyncTask extends AsyncTask<Integer, Void, News> {
		boolean flag = false;

		/**
		 * 获得新闻,包括从数据库，从服务器. 这部分冰不在UI线程里面跑
		 */
		@Override
		protected News doInBackground(Integer... params) {
			News news = null;
			// 首先先查看数据库里面的数据
			RemoteDAO remoteDAO = new RemoteDAO();
			NewsDAO newsDAO = new NewsDAO(getApplicationContext());
			for (int i : params) {
				news = newsDAO.getNewsByIds(i);
				if (news == null) {
					// 然后查看服务器的数据
					if (RemoteDAO.isNetworkAvailable(getApplicationContext())) {
						news = remoteDAO.getNewsFromServer(i);
					} else {
						flag = true;// 连接未开启
					}
				}
			}
			newsDAO.close();

			return news;
		}

		/**
		 * 更新UI。 这部分在UI线程里面跑。
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(final News result) {
			super.onPostExecute(result);

			System.out
					.println("post execute in retrieving news from both db and server.");
			if (result != null) {
				System.out.println("change the flipper with news: "
						+ result.getId());
				// 将数据 填入view
				ProgressBar progressBar = (ProgressBar) flipper
						.getCurrentView().findViewById(R.id.progressBar1);
				progressBar.setVisibility(View.GONE);
				TextView title = (TextView) flipper.getCurrentView()
						.findViewById(R.id.textViewTitle);
				title.setText(result.getTitle());
				title.setVisibility(View.VISIBLE);
				TextView etc = (TextView) flipper.getCurrentView()
						.findViewById(R.id.textViewETC);
				etc.setText(result.getDate());
				etc.setVisibility(View.VISIBLE);
				TextView body = (TextView) flipper.getCurrentView()
						.findViewById(R.id.textViewBody);
				body.setText(result.getBody());
				body.setVisibility(View.VISIBLE);
				Button button = (Button) flipper.getCurrentView().findViewById(
						R.id.button1);
				button.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						Uri uri = Uri.parse(result.getUrl());
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					}
				});
				button.setVisibility(View.VISIBLE);
				// 将数据放入数据库
				NewsDAO newsDAO = new NewsDAO(getApplicationContext());
				newsDAO.updateOrInsertNews(Arrays.asList(result));
				newsDAO.close();
			} else {
				if (flag) {// 只提示一次网络部可用
					Toast toast = Toast.makeText(getApplicationContext(),
							R.string.net_unavailable, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {// 网络可用，但是服务器可能关闭了
					Toast toast = Toast.makeText(getApplicationContext(),
							R.string.server_unavailable, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				// 1s后回退到前一个activity
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						finish();
					}
				}, 700);
			}
		}

	}

}
