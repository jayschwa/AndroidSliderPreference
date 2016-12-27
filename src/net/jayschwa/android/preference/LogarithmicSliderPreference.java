/*
 * Copyright 2016 Jay Weisskopf
 *
 * Licensed under the MIT License (see LICENSE.txt)
 */

package net.jayschwa.android.preference;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Ryan Archer
 */
public class LogarithmicSliderPreference extends SliderPreference {

	private static final double logarithmicMinimum = 0.1;
	private static final double logarithmicMaximum = 10.0;
	private static final double linearMinimum = 0.0;
	private static final double linearMaximum = 1.0;
	private static final double base = 10.0;

	// mapping equation is: logarithmicValue = a * base ^ (b * linearValue), where a and b are constants, so:
	private static final double b = Math.log10(logarithmicMaximum / logarithmicMinimum) / (linearMaximum - linearMinimum);
	private static final double a = logarithmicMaximum / Math.pow(base, (b * linearMaximum));

	/**
	 * @param context
	 * @param attrs
	 */
	public LogarithmicSliderPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public LogarithmicSliderPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public float getValue() {
		return toLogarithmic(super.getValue());
	}

	@Override
	public void setValue(float value) {
		super.setValueInternally(toLinear(value));
	}

	@Override
	protected void setPersistentValue(float value) {
		persistFloat(toLogarithmic(value));
	}

	private float toLogarithmic(float linear) {
		return (float) (a * Math.pow(base, (b * linear)));
	}

	private float toLinear(float logarithmic) {
		return (float) (Math.log10(logarithmic / a) / b);
	}
}
