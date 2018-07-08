package org.openobservatory.ooniprobe.test.impl;

import android.support.annotation.NonNull;

import org.openobservatory.ooniprobe.activity.AbstractActivity;
import org.openobservatory.ooniprobe.model.JsonResult;
import org.openobservatory.ooniprobe.model.Result;
import org.openobservatory.ooniprobe.test.AbstractTest;

import static org.openobservatory.ooniprobe.model.Measurement.MeasurementState.measurementFailed;

public class Whatsapp extends AbstractTest<JsonResult> {
	public static final String NAME = "whatsapp";

	public Whatsapp(AbstractActivity activity, Result result) {
		super(activity, NAME, new org.openobservatory.measurement_kit.nettests.WhatsappTest(), JsonResult.class, result);
	}

	/*
         if "whatsapp_endpoints_status", "whatsapp_web_status", "registration_server" are null => failed
         if "whatsapp_endpoints_status" or "whatsapp_web_status" or "registration_server_status" are "blocked" => anomalous
     */
	@Override public void onEntry(@NonNull JsonResult json) {
		JsonResult.TestKeys keys = json.test_keys;
		if (keys.whatsapp_endpoints_status == null || keys.whatsapp_web_status == null || keys.registration_server_status == null)
			measurement.state = measurementFailed;
		else if (keys.whatsapp_endpoints_status.equals("blocked") || keys.whatsapp_web_status.equals("blocked") || keys.registration_server_status.equals("blocked"))
			measurement.anomaly = true;
		measurement.result.getSummary().getTestKeysMap().put(measurement.name, json.test_keys);
		super.onEntry(json);
	}
}
