package com.luzi82.melody;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MelodyActivity extends Activity {

	LinkedList<BrowseEntry> mPathStack;
	MelodyAdapter mAdapter;
	ListView mainList;
	double PHI = (1.0d + Math.sqrt(5.0d)) / 2.0d;
	double PHI2 = PHI * PHI;
	int UNIT_HEIGHT = 64;
	int UNIT_HEIGHT_UNIT = TypedValue.COMPLEX_UNIT_DIP;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mAdapter = new MelodyAdapter(this);

		mainList = (ListView) findViewById(R.id.main_list);
		mainList.setAdapter(mAdapter);
		mainList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId) {
				MelodyAdapter.Entry e = (MelodyAdapter.Entry) mAdapter.getItem(aPosition);
				if (e.mType == MelodyAdapter.EntryType.FOLDER) {
					BrowseEntry next = new BrowseEntry();
					next.mPath = e.mPath;
					next.mItemIndex = aPosition;
					cd(next);
				}
			}
		});

		mPathStack = new LinkedList<BrowseEntry>();

		BrowseEntry rootEntry = new BrowseEntry();
		rootEntry.mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
		rootEntry.mItemIndex = 0;
		cd(rootEntry);
	}

	boolean cd(BrowseEntry aEntry) {
		int top = 0;
		int y = 0;
		if (aEntry != null) {
			mPathStack.addLast(aEntry);
		} else {
			top = mPathStack.getLast().mItemIndex;
			y = (int) ((mainList.getHeight() - TypedValue.applyDimension(UNIT_HEIGHT_UNIT, UNIT_HEIGHT, getResources().getDisplayMetrics())) / PHI2);
			mPathStack.removeLast();
		}
		if (mPathStack.isEmpty()) {
			return false;
		}
		BrowseEntry be = mPathStack.getLast();
		String p = be.mPath;
		mAdapter.setDirectory(p);
		mainList.setSelectionFromTop(top, y);
		// mainList.setSelectionFromTop(top, 100);
		setTitle(p);
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

}