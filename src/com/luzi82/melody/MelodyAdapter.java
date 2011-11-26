package com.luzi82.melody;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


// extends BasicAdapter

public class MelodyAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;

	public MelodyAdapter(Context aContext) {
		mContext = aContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setDirectory(String aPath) {
		System.err.println("setDirectory "+aPath);
		
		mEntryList.clear();

		LinkedList<Entry> dirList = new LinkedList<Entry>();
		LinkedList<Entry> musicList = new LinkedList<Entry>();

		File dir = new File(aPath);
		if (dir.exists() && dir.isDirectory()) {
			File[] members = dir.listFiles();
			if (members != null) {
				Arrays.sort(members);
				for (File member : members) {
					Entry e = new Entry();
					if (member.isDirectory()) {
						e.mType = EntryType.FOLDER;
						dirList.addLast(e);
					} else if (member.isFile()) {
						e.mType = EntryType.MUSIC;
						musicList.addLast(e);
					} else {
						continue;
					}
					e.mName = member.getName();
					e.mPath = member.getAbsolutePath();
					// TODO Should use DB to store all file/folder IDs
					e.mId = e.mPath.hashCode() & 0x7fffffff;
				}
			}
		}

		mEntryList.addAll(dirList);
		mEntryList.addAll(musicList);

//		notifyOnChanged();
		notifyDataSetChanged();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public int getCount() {
		return mEntryList.size();
	}

	@Override
	public Object getItem(int position) {
		return mEntryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		Entry e = mEntryList.get(position);
		if (e == null)
			return -1;
		else
			return e.mId;
	}

	@Override
	public int getItemViewType(int position) {
		Entry e = mEntryList.get(position);
		if (e == null)
			return -1;
		else
			return e.mType.ordinal();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Entry e = mEntryList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.track_list_item, parent, false);
		}
		TextView line1 = (TextView) convertView.findViewById(R.id.line1);
		line1.setText(e.mName);
		if (e.mType == MelodyAdapter.EntryType.FOLDER) {
			line1.setTextColor(Color.YELLOW);
		} else if (e.mType == MelodyAdapter.EntryType.MUSIC) {
			line1.setTextColor(Color.WHITE);
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return EntryType.values().length;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return mEntryList.isEmpty();
	}

	LinkedList<Entry> mEntryList = new LinkedList<Entry>();

	public static class Entry {
		public EntryType mType;
		public String mName;
		public String mPath;
		public int mId;
	}

	enum EntryType {
		MUSIC, FOLDER
	}

}
