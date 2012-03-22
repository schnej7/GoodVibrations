package teamwork.goodVibrations;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class GoodVibrationsActivity extends Activity
{
	private ViewPager awesomePager;
	private static int NUM_AWESOME_VIEWS = 2;
	private Context cxt;
	private AwesomePagerAdapter awesomeAdapter;
	private LayoutInflater inflater;


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		cxt = this;

		awesomeAdapter = new AwesomePagerAdapter();
		awesomePager = (ViewPager) findViewById(R.id.main_pager);
		awesomePager.setAdapter(awesomeAdapter);
		inflater = LayoutInflater.from(getApplicationContext());
	}

	@Override
	public void onStart()
	{
		super.onStart();
		//startService(new Intent(this, GoodVibrationsService.class));




	}

	private class AwesomePagerAdapter extends PagerAdapter{


		private static final String TAG = "AwesomePagerAdapter";

		@Override
		public int getCount() {
			return NUM_AWESOME_VIEWS;
		}

		/**
		 * Create the page for the given position.  The adapter is responsible
		 * for adding the view to the container given here, although it only
		 * must ensure this is done by the time it returns from
		 * {@link #finishUpdate()}.
		 *
		 * @param container The containing View in which the page will be shown.
		 * @param position The page position to be instantiated.
		 * @return Returns an Object representing the new page.  This does not
		 * need to be a View, but can be some other container of the page.
		 */
		@Override
		public Object instantiateItem(View collection, int position) {
			View v = new View(cxt.getApplicationContext());

			switch (position) {
			case 0:
				v = inflater.inflate(R.layout.testview0, null);
				break;
			case 1:
				v = inflater.inflate(R.layout.testview1, null);
				break;

			default:

				TextView tv = new TextView(cxt);
				tv.setText("Page " + position);
				tv.setTextColor(Color.WHITE);
				tv.setTextSize(30);
				v = tv;
				break;
			}

			((ViewPager) collection).addView(v, 0);

			return v;
		}

		/**
		 * Remove a page for the given position.  The adapter is responsible
		 * for removing the view from its container, although it only must ensure
		 * this is done by the time it returns from {@link #finishUpdate()}.
		 *
		 * @param container The containing View from which the page will be removed.
		 * @param position The page position to be removed.
		 * @param object The same object that was returned by
		 * {@link #instantiateItem(View, int)}.
		 */
		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((TextView) view);
		}



		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==((TextView)object);
		}


		/**
		 * Called when the a change in the shown pages has been completed.  At this
		 * point you must ensure that all of the pages have actually been added or
		 * removed from the container as appropriate.
		 * @param container The containing View which is displaying this adapter's
		 * page views.
		 */
		@Override
		public void finishUpdate(View arg0) {}


		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {}

	}
}