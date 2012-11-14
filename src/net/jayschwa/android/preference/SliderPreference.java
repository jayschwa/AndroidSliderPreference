/*
 * Copyright 2012 Jay Weisskopf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.jayschwa.android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

/**
 * @author Jay Weisskopf
 */
public class SliderPreference extends DialogPreference {

	protected float mValue;
	protected int mSeekBarValue;
	protected int mSeekBarResolution = 1000;
	protected final static float DEFAULT_VALUE = 0.5f;

	/**
	 * @param context
	 * @param attrs
	 */
	public SliderPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.dialog);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SliderPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setDialogLayoutResource(R.layout.dialog);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getFloat(index, DEFAULT_VALUE);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		setValue(restoreValue ? getPersistedFloat(mValue) : (Float) defaultValue, false);
	}

	public float getValue() {
		return mValue;
	}

	public void setValue(float value, boolean notify) {
		if (value != mValue) {
			mValue = value;
			if (isPersistent()) {
				persistFloat(mValue);
			}
			if (notify) {
				notifyChanged();
			}
		}
	}

	public void setValue(float value) {
		setValue(value, true);
	}

	protected void setSeekBarValue(int value) {
		mSeekBarValue = value;
	}

	@Override
	protected View onCreateDialogView() {
		mSeekBarValue = (int) (mValue * mSeekBarResolution);
		View view = super.onCreateDialogView();
		SeekBar seekbar = (SeekBar) view.findViewById(R.id.seekbar);
		seekbar.setMax(mSeekBarResolution);
		seekbar.setProgress(mSeekBarValue);
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					SliderPreference.this.setSeekBarValue(progress);
				}
			}
		});
		return view;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		final float newValue = (float) mSeekBarValue / mSeekBarResolution;
		if (positiveResult && callChangeListener(newValue)) {
			setValue(newValue);
		}
		super.onDialogClosed(positiveResult);
	}

	// TODO: Save and restore preference state.
}
