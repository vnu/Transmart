package com.android.transmart.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Abstract base class that can be extended to provide classes that save 
 * {@link SharedPreferences} in the most efficient way possible. 
 * Decendent classes can optionally choose to backup some {@link SharedPreferences}
 * to the Google BackupService on platforms where this is available.
 */
public abstract class SharedPref {
	protected Context context;
	  
	  protected SharedPref(Context context) {
	    this.context = context;
	  }
	  
	  /**
	   * Save the Shared Preferences modified through the Editor object.
	   * @param editor Shared Preferences Editor to commit.
	   * @param backup Backup to the cloud if possible.
	   */
	  public void savePreferences(SharedPreferences.Editor editor, boolean backup) {}

}
