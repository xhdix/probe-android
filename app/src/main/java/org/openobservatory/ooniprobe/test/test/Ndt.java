package org.openobservatory.ooniprobe.test.test;

import android.support.annotation.NonNull;

import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.activity.AbstractActivity;
import org.openobservatory.ooniprobe.model.jsonresult.JsonResult;
import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.model.jsonresult.TestKeys;

public class Ndt extends AbstractTest {
	public static final String NAME = "ndt";
	public static final String MK_NAME = "Ndt";

	private String[] countries;

	public Ndt(AbstractActivity activity) {
		super(activity, NAME, MK_NAME, R.string.Test_NDT_Fullname, 0);
		if(!activity.getPreferenceManager().isNdtServerAuto()){
			this.settings.options.server = activity.getPreferenceManager().getndtServer();
			this.settings.options.port = activity.getPreferenceManager().getndtServerPort();
		}
	}

	@Override public void run(AbstractActivity activity, Result result, int index, TestCallback testCallback) {
		countries = activity.getResources().getStringArray(R.array.countries);
		run(activity, new org.openobservatory.measurement_kit.nettests.NdtTest(), result, index, testCallback);
	}

	/*
	 	onEntry method for ndt test, check "failure" key
	 	!=null => failed
	 */
	@Override public void onEntry(AbstractActivity activity, @NonNull JsonResult json, Measurement measurement) {
		super.onEntry(activity, json, measurement);
		measurement.is_done = true;
		measurement.is_failed = json.test_keys.failure != null;
		calculateServerName(json.test_keys);
		measurement.setTestKeys(json.test_keys);
	}

	private void calculateServerName(TestKeys keys) {
		String[] parts = keys.server_address.split("\\.");
		if (parts.length > 3) {
			keys.server_name = parts[3];
			keys.server_country = getAirportCountry(parts[3].substring(0, 3));
		}
	}

	private String getAirportCountry(String serverName) {
		for (String country : countries)
			if (country.startsWith(serverName))
				return country.split("\\|")[1];
		return null;
	}
}
