/*
 * Copyright 2012 Jay Weisskopf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
	protected CharSequence[] mSummaries;
	protected final static float DEFAULT_VALUE = 0.5f;

	/**
	 * @param context
	 * @param attrs
	 */
	public SliderPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SliderPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup(context, attrs);
	}

	private void setup(Context context, AttributeSet attrs) {
		setDialogLayoutResource(R.layout.slider_preference_dialog);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SliderPreference);
		try {
			setSummary(a.getTextArray(R.styleable.SliderPreference_android_summary));
		} catch (Exception e) {
			// Do nothing
		}
		a.recycle();
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getFloat(index, DEFAULT_VALUE);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		setValue(restoreValue ? getPersistedFloat(mValue) : (Float) defaultValue, false);
	}

	@Override
	public CharSequence getSummary() {
		if (mSummaries != null && mSummaries.length > 0) {
			int index = (int) (mValue * mSummaries.length);
			index = Math.min(index, mSummaries.length - 1);
			return mSummaries[index];
		} else {
			return super.getSummary();
		}
	}

	public void setSummary(CharSequence[] summaries) {
		mSummaries = summaries;
	}

	@Override
	public void setSummary(CharSequence summary) {
		super.setSummary(summary);
		mSummaries = null;
	}

	@Override
	public void setSummary(int summaryResId) {
		try {
			setSummary(getContext().getResources().getStringArray(summaryResId));
		} catch (Exception e) {
			super.setSummary(summaryResId);
		}
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
		SeekBar seekbar = (SeekBar) view.findViewById(R.id.slider_preference_seekbar);
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
