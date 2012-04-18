package com.android.transmart.location;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.transmart.utils.SharedPref;

/**
 * Save {@link SharedPreferences} and provide the option to notify the
 * BackupManager to initiate a backup.
 */
public class FroyoSharedPref extends SharedPref {
	protected BackupManager backupManager;

	protected FroyoSharedPref(Context context) {
		super(context);
		backupManager = new BackupManager(context);
	}

	@Override
	public void savePreferences(Editor editor, boolean backup) {
		editor.commit();
		backupManager.dataChanged();
	}
	

}
