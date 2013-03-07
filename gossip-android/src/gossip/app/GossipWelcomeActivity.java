package gossip.app;

import gossip.app.data.EventDAO;
import gossip.app.data.GossipActions;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

/**
 * 欢迎界面activity，自动从数据库里面读数据
 * 
 * @author lins
 */
public class GossipWelcomeActivity extends Activity {
	public static final String PREFS_NAME = "MY_PREFS";
	private boolean isFirstUse;
	private SharedPreferences settings;
	private Intent intent = new Intent();
	private static final int DURATION = 3500;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		// 检查是否是第一次登陆
		settings = getSharedPreferences(PREFS_NAME, 0);
		isFirstUse = settings.getBoolean("isGossipFirstUsed", false);
		System.out.println("isGossipFirstUsed: " + isFirstUse);

		// 自动淡化启动画面，持续时间为DURATION*2
		ImageView image = (ImageView) findViewById(R.id.imageViewWelcome);
		AnimationSet animationset = new AnimationSet(true);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(DURATION * 2);
		animationset.addAnimation(alphaAnimation);
		image.startAnimation(animationset);

		// 使用handler进行延迟启动第二个activity：helper或者main
		Handler handler = new Handler();
		handler.postDelayed(new Thread() {
			public void run() {
				
				//根据是不是第一次使用，觉得下一个activity
				if (isFirstUse) {
					intent.setAction(GossipActions.ACTION_HELPER);
					intent.setClass(GossipWelcomeActivity.this,
							GossipHelperActivity.class);
					settings.edit().putBoolean("isFirstUse", false);
				} else {
					/** 从数据库中读出旧的数据 **/
					EventDAO eventDAO = new EventDAO(GossipWelcomeActivity.this);
					Bundle bundle = eventDAO.getTopNEventsByTime(10);
					eventDAO.close();
					intent.setAction(GossipActions.ACTION_MAIN);
					intent.setClass(GossipWelcomeActivity.this,
							GossipMainActivity.class);
					intent.putExtras(bundle);
				}
				startActivity(intent);
				finish();
			}
		}, DURATION/5);

	}
}