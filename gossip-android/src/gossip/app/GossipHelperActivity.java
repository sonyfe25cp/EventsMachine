package gossip.app;

import gossip.app.data.GossipActions;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ViewFlipper;

/**
 * 显示帮助图片
 * @author lins
 */
public class GossipHelperActivity extends Activity implements OnGestureListener {
	private ViewFlipper flipper;
	private GestureDetector detector;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helper);

		detector = new GestureDetector(this);

		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper1);
		
		imageView = (ImageView) this.findViewById(R.id.imageViewHelper3);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

//	private View addTextView(int id) {
//		ImageView iv = new ImageView(this);
//		iv.setImageResource(id);
//		iv.setLayoutParams(layoutParams);
//		return iv;
//	}

	public boolean onDown(MotionEvent arg0) {
		System.out.println("finger down!");
		return false;
	}

	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		if (arg0.getX() - arg1.getX() > 120) {
			//跳转动画，暂无
			// this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
			// R.anim.push_left_in));
			// this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
			// R.anim.push_left_out));
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.animator.left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.animator.left_out));
			this.flipper.showNext();
			System.out.println("To next navigation slide.");
			
			//最后一页，跳转到主页面
			if(this.flipper.getCurrentView().equals(imageView)){
				Intent intent = new Intent();
				intent.setAction(GossipActions.ACTION_MAIN);
	        	intent.setClass(GossipHelperActivity.this, GossipMainActivity.class);
	        	startActivity(intent);
				finish();
			}
			return true;
		} else if (arg0.getX() - arg1.getX() < -120) {
			// this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
			// R.anim.push_right_in));
			// this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
			// R.anim.push_right_out));
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.animator.right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.animator.right_out));
			this.flipper.showPrevious();
			System.out.println("To previous navigation slide.");
			return true;
		}

		return false;
	}

	public void onLongPress(MotionEvent arg0) {

	}

	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	public void onShowPress(MotionEvent arg0) {

	}

	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

}
