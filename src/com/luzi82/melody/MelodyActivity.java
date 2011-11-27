package com.luzi82.melody;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewAnimator;

public class MelodyActivity extends Activity {

	LinkedList<BrowseEntry> mPathStack;
	MelodyAdapter[] mAdapter;
	ListView[] mMainlist;
	static final int[] LISTID = { R.id.mainlist_0, R.id.mainlist_1 };
	ViewAnimator mViewanimator;
	double PHI = (1.0d + Math.sqrt(5.0d)) / 2.0d;
	double PHI2 = PHI * PHI;
	int UNIT_HEIGHT = 64;
	int UNIT_HEIGHT_UNIT = TypedValue.COMPLEX_UNIT_DIP;
	int mCurrentListIndex = -1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mMainlist = new ListView[LISTID.length];
		for (int i = 0; i < LISTID.length; ++i) {
			mMainlist[i] = (ListView) findViewById(LISTID[i]);
		}

		mViewanimator = (ViewAnimator) findViewById(R.id.viewanimator);

		mAdapter = new MelodyAdapter[mMainlist.length];
		for (int i = 0; i < mMainlist.length; ++i) {
			mAdapter[i] = new MelodyAdapter(this);
			mMainlist[i].setAdapter(mAdapter[i]);
			mMainlist[i].setOnItemClickListener(mOnItemClickListener);
		}

		mPathStack = new LinkedList<BrowseEntry>();

		BrowseEntry rootEntry = new BrowseEntry();
		// rootEntry.mPath =
		// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
		rootEntry.mPath = "/";
		rootEntry.mItemIndex = 0;
		cd(rootEntry);
	}

	synchronized boolean cd(BrowseEntry aEntry) {
		int top = 0;
		int y = 0;

		int nextListIndex = (mCurrentListIndex + 1) % mMainlist.length;

		ListView nextView = mMainlist[nextListIndex];
		MelodyAdapter nextAdapter = mAdapter[nextListIndex];

		if (aEntry != null) {
			mPathStack.addLast(aEntry);
			mViewanimator.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			mViewanimator.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
		} else {
			top = mPathStack.getLast().mItemIndex;
			y = (int) ((nextView.getHeight() - TypedValue.applyDimension(UNIT_HEIGHT_UNIT, UNIT_HEIGHT, getResources().getDisplayMetrics())) / PHI2);
			mPathStack.removeLast();
			mViewanimator.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
			mViewanimator.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
		}
		if (mPathStack.isEmpty()) {
			return false;
		}
		BrowseEntry be = mPathStack.getLast();
		String p = be.mPath;
		nextAdapter.setDirectory(p);
		if (nextAdapter.getCount() > 0) {
			nextView.setSelectionFromTop(top, y);
		}
		setTitle(p);
		// viewanimator.setDisplayedChild(LISTID[mNextListIndex]);
		if (mCurrentListIndex >= 0) {
			mViewanimator.showNext();
			System.err.println(mViewanimator.getDisplayedChild());
		}

		mCurrentListIndex = nextListIndex;

		return true;
	}

	@Override
	public void onBackPressed() {
		if (!cd(null)) {
			super.onBackPressed();
		}
	}

	class BrowseEntry {
		String mPath;
		int mItemIndex;
	}

	AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId) {
			System.err.println(String.format("onItemClick mCurrentListIndex %d aPosition %d aId %d", mCurrentListIndex, aPosition, aId));
			MelodyAdapter.Entry e = (MelodyAdapter.Entry) mAdapter[mCurrentListIndex].getItem(aPosition);
			if (e.mType == MelodyAdapter.EntryType.FOLDER) {
				BrowseEntry next = new BrowseEntry();
				next.mPath = e.mPath;
				next.mItemIndex = aPosition;
				cd(next);
			}
		}
	};

}