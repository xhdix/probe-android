package org.openobservatory.ooniprobe.test.test;

import android.support.annotation.NonNull;

import org.openobservatory.measurement_kit.nettests.WebConnectivityTest;
import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.activity.AbstractActivity;
import org.openobservatory.ooniprobe.model.jsonresult.JsonResult;
import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.model.database.Url;

import java.util.ArrayList;
import java.util.Arrays;

public class WebConnectivity extends AbstractTest {
	public static final String NAME = "web_connectivity";
	public static final String MK_NAME = "WebConnectivitys";

	public WebConnectivity(AbstractActivity activity) {
		super(activity, NAME, MK_NAME, R.string.Test_WebConnectivity_Fullname, 0);
		//TODO move in another part
		this.settings.options.max_runtime = activity.getPreferenceManager().getMaxRuntime();
		this.settings.inputs = new ArrayList<>(Arrays.asList("http://4genderjustice.org/", "http://www.google.com/"));
	}

	@Override public void run(AbstractActivity activity, Result result, int index, TestCallback testCallback) {
		WebConnectivityTest test = new WebConnectivityTest();
		test.set_option("max_runtime", "90");
		test.add_input("http://4genderjustice.org/");
		run(activity, test, result, index, testCallback);
	}

	/*
     null => failed {state = FAILED}
     false => not blocked {state = DONE, anomaly = false}
     string (dns, tcp-ip, http-failure, http-diff) => {state = DONE, anomaly = true}
     */
	@Override public void onEntry(AbstractActivity activity, @NonNull JsonResult json, Measurement measurement) {
		super.onEntry(activity, json, measurement);
		measurement.is_done = true;
		if (json.test_keys.blocking == null)
			measurement.is_failed = true;
		else
			measurement.is_anomaly = !json.test_keys.blocking.equals("false");
		measurement.url = new Url(json.input);
	}
}
