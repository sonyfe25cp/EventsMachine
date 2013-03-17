package gossip.app;

import android.app.ListActivity;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * 
 * @author lins 2012-8-24
 */
public class GossipEventDetailActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// If the screen is now in landscape mode, we can show the
			// dialog in-line with the list so we don't need this activity.
			finish();
			return;
		}

		if (savedInstanceState == null) {
			System.out.println("now i'll create detail activity from fragment");
			// During initial setup, plug in the details fragment.
			GossipEventDetailFragment details = new GossipEventDetailFragment();
			details.setArguments(getIntent().getExtras());
			getFragmentManager().beginTransaction()
					.add(android.R.id.content, details).commit();
		}
	}
}
