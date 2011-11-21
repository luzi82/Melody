package com.luzi82.melody;

import java.io.File;
import java.util.LinkedList;

import android.view.View;
import android.view.ViewGroup;

public class MelodyAdapter extends BasicAdapter {

	public void setDirectory(String aPath) {
		mEntryList.clear();

		File dir = new File(aPath);
		if (dir.exists() && dir.isDirectory()) {
			File[] members = dir.listFiles();
			for (File member : members) {
				if (member.isDirectory()) {
					FolderEntry e = new FolderEntry();
					e.mName = member.getName();
					e.mPath = member.getAbsolutePath();
					mEntryList.add(e);
				}else if(member.isFile()){
					
				}
			}
		}

		notifyOnChanged();
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
		return position;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return EntryType.values().length;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return mEntryList.isEmpty();
	}

	LinkedList<Entry> mEntryList = new LinkedList<Entry>();

	public static abstract class Entry {
		protected Entry(EntryType aType) {
			mType = aType;
		}

		public final EntryType mType;
		public String mName;
		public String mPath;
	}

	public static class MusicEntry extends Entry {
		protected MusicEntry() {
			super(EntryType.MUSIC);
		}
	}

	public static class FolderEntry extends Entry {
		protected FolderEntry() {
			super(EntryType.FOLDER);
		}
	}

	enum EntryType {
		MUSIC, FOLDER
	}

}
