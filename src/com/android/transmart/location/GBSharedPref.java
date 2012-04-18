package com.android.transmart.location;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Save {@link SharedPreferences} using the asynchronous apply method available
 * in Gingerbread, and provide the option to notify the BackupManager to
 * initiate a backup.
 */
public class GBSharedPref extends FroyoSharedPref {

	protected GBSharedPref(Context context) {
		super(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void savePreferences(SharedPreferences.Editor editor, boolean backup) {
		editor.apply();
		backupManager.dataChanged();
	}

}
