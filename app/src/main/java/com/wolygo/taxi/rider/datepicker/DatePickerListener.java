package com.wolygo.taxi.rider.datepicker;

import org.joda.time.DateTime;

/**
 * Created by jhonn on 02/03/2017.
 */
public interface DatePickerListener {
    void onDateSelected(DateTime dateSelected);
}
