Android Slider Preference
=========================

![Screenshot](https://raw.github.com/jayschwa/AndroidSliderPreference/master/screenshot.png)

## Overview

* Slider represents a `float` between `0.0` and `1.0`
  * Access with `SliderPreference.getValue()` or [`SharedPreferences.getFloat()`][shar]
* Supports multiple summaries (e.g. "Low", "Medium", "High") and selects one based on the slider's position
  * Java: `SliderPreference.setSummary(CharSequence[] summaries)`
  * XML: `android:summary="@array/string_array_of_summaries"`
  * A single `String` still works too
* Subclass of [`DialogPreference`][diag]
  * Supports all [dialog-specific attributes][datr] such as `android:dialogMessage`
  * Visually-consistent with official Android preferences
  * Less error-prone than displaying the slider directly on the settings screen

[datr]: https://developer.android.com/reference/android/preference/DialogPreference.html#lattrs "DialogPreference attributes"
[diag]: https://developer.android.com/reference/android/preference/DialogPreference.html "DialogPreference"
[shar]: https://developer.android.com/reference/android/content/SharedPreferences.html#getFloat(java.lang.String,%20float) "getFloat()"

## Why?

Official [Android Design Pattern][ptrn] documentation recommends a slider for certain types of settings:

> Use this pattern for a setting where the range of values are not discrete and fall along a continuum.

Unfortunately, the Android SDK does not provide a [`Preference`][pref] with slider functionality. Various custom implementations can be found around the web, but many have issues:

* Slider is displayed directly on the settings screen
  * Higher chance of accidental clicks
  * No way to confirm or cancel potential changes
* Discrete values are displayed to the user
  * Not ideal for this design pattern

This implementation aims to be as consistent as possible with the design pattern and official Android [`Preference`][pref] implementations.

[ptrn]: https://developer.android.com/design/patterns/settings.html#patterns "Settings Design Patterns"
[pref]: https://developer.android.com/reference/android/preference/Preference.html "Preference"