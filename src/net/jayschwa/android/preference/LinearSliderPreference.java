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
public class LinearSliderPreference extends SliderPreference {

	// mapping equation is: mappedValue = a * unmappedValue + b, where a and b are constants, so:
	private final float a;
	private final float b;

	private static float calculateA(final float minimum, final float maximum) {
		return (maximum - minimum) / (SliderPreference.MAXIMUM - SliderPreference.MINIMUM);
	}

	private static float calculateB(final float a, final float maximum) {
		return maximum - a * SliderPreference.MAXIMUM;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public LinearSliderPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LinearSliderPreference);
		final float minimum = attributes.getFloat(R.styleable.LinearSliderPreference_minimum, SliderPreference.MINIMUM);
		final float maximum = attributes.getFloat(R.styleable.LinearSliderPreference_maximum, SliderPreference.MAXIMUM);
		attributes.recycle();
		
		a = calculateA(minimum, maximum);
		b = calculateB(a, maximum);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public LinearSliderPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LinearSliderPreference);
		final float minimum = attributes.getFloat(R.styleable.LinearSliderPreference_minimum, SliderPreference.MINIMUM);
		final float maximum = attributes.getFloat(R.styleable.LinearSliderPreference_maximum, SliderPreference.MAXIMUM);
		attributes.recycle();
		
		a = calculateA(minimum, maximum);
		b = calculateB(a, maximum);
	}

	@Override
	public float getValue() {
		return map(super.getValue());
	}

	@Override
	public void setValue(float value) {
		super.setValueInternally(unmap(value));
	}

	@Override
	protected void setPersistentValue(float value) {
		persistFloat(map(value));
	}

	private float map(float unmapped) {
		return a * unmapped + b;
	}

	private float unmap(float mapped) {
		return (mapped - b) / a;
	}
}
