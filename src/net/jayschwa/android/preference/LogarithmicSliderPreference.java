/*
 * Copyright 2016 Jay Weisskopf
 *
 * Licensed under the MIT License (see LICENSE.txt)
 */

package net.jayschwa.android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * @author Ryan Archer
 */
public class LogarithmicSliderPreference extends SliderPreference {

	private static final float defaultLogarithmicMinimum = (float) 0.1;
	private static final float defaultLogarithmicMaximum = (float) 10.0;
	private static final double base = 10.0;

	// mapping equation is: logarithmicValue = a * base ^ (b * linearValue), where a and b are constants, so:
	private final double a;
	private final double b;

	private static double calculateA(final double logarithmicMaximum, final double b) {
		return logarithmicMaximum / Math.pow(base, (b * SliderPreference.MAXIMUM));
	}

	private static double calculateB(final double logarithmicMinimum, final double logarithmicMaximum) {
		return Math.log10(logarithmicMaximum / logarithmicMinimum) / (SliderPreference.MAXIMUM - SliderPreference.MINIMUM);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public LogarithmicSliderPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LogarithmicSliderPreference);
		final float logarithmicMinimum = attributes.getFloat(R.styleable.LogarithmicSliderPreference_minimum, defaultLogarithmicMinimum);
		final float logarithmicMaximum = attributes.getFloat(R.styleable.LogarithmicSliderPreference_maximum, defaultLogarithmicMaximum);
		attributes.recycle();

		b = calculateB(logarithmicMinimum, logarithmicMaximum);
		a = calculateA(logarithmicMaximum, b);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public LogarithmicSliderPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LogarithmicSliderPreference);
		final float logarithmicMinimum = attributes.getFloat(R.styleable.LogarithmicSliderPreference_minimum, defaultLogarithmicMinimum);
		final float logarithmicMaximum = attributes.getFloat(R.styleable.LogarithmicSliderPreference_maximum, defaultLogarithmicMaximum);
		attributes.recycle();

		b = calculateB(logarithmicMinimum, logarithmicMaximum);
		a = calculateA(logarithmicMaximum, b);
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
