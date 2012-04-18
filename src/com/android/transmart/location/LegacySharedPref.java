package com.android.transmart.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.transmart.utils.SharedPref;

/**
 * Save {@link SharedPreferences} in a way compatible with Android 1.6.
 */
public class LegacySharedPref extends SharedPref {

	protected LegacySharedPref(Context context) {
		super(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void savePreferences(Editor editor, boolean backup) {
		editor.commit();
	}

}
