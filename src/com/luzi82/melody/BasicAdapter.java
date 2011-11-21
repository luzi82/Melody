package com.luzi82.melody;

import java.util.LinkedList;

import android.database.DataSetObserver;
import android.widget.ListAdapter;

public abstract class BasicAdapter implements ListAdapter {

	protected LinkedList<DataSetObserver> mObserverList = new LinkedList<DataSetObserver>();

	protected void notifyOnChanged() {
		for (DataSetObserver observer : mObserverList) {
			observer.onChanged();
		}
	}

	protected void notifyOnInvalidated() {
		for (DataSetObserver observer : mObserverList) {
			observer.onInvalidated();
		}
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		mObserverList.add(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mObserverList.remove(observer);
	}

}
