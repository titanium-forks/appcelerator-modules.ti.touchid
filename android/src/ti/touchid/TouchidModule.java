/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.touchid;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CancellationSignal;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiBaseActivity;
import org.appcelerator.titanium.TiC;
import org.appcelerator.kroll.KrollProxy;

import java.lang.Override;
import java.util.HashMap;

import android.app.Activity;

@Kroll.module(name="Touchid", id="ti.touchid")
public class TouchidModule extends KrollModule
{
	public static final int PERMISSION_CODE_FINGERPRINT = 99;

	@Kroll.constant public static final int SUCCESS = 0;
	@Kroll.constant public static final int SERVICE_MISSING = 1;
	@Kroll.constant public static final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
	@Kroll.constant public static final int SERVICE_DISABLED = 3;
	@Kroll.constant public static final int SERVICE_INVALID = 9;

	protected FingerPrintHelper mfingerprintHelper;

	public TouchidModule() {
		super();
		Activity activity = TiApplication.getAppRootOrCurrentActivity();
		if (Build.VERSION.SDK_INT >= 23) {
			try {
				mfingerprintHelper = new FingerPrintHelper();
			} catch (Exception e) {
				mfingerprintHelper = null;
			}
		}
	}

	@Kroll.method
	public void authenticate(HashMap params) {
		if (params == null || mfingerprintHelper == null) {
			return;
		}
		if (params.containsKey("callback")) {
			Object callback = params.get("callback");
			if (callback instanceof KrollFunction) {
				mfingerprintHelper.startListening((KrollFunction)callback, getKrollObject());
			}
		}
	}

	@Kroll.method
	public HashMap deviceCanAuthenticate() {
		if (Build.VERSION.SDK_INT >= 23 && mfingerprintHelper != null) {
			return mfingerprintHelper.deviceCanAuthenticate();
		}

		KrollDict response = new KrollDict();
		response.put("canAuthenticate", false);

		if (Build.VERSION.SDK_INT < 23) {
			response.put("error", "Device is running with API < 23");
		} else {
			response.put("error", "Device does not support fingerprint authentication");
		}

		return response;
	}

	@Kroll.method
	public boolean isSupported() {
		if (Build.VERSION.SDK_INT >= 23 && mfingerprintHelper != null) {
			return mfingerprintHelper.isDeviceSupported();
		}
		return false;
	}
	
	@Override
	public void onPause(Activity activity) {
		super.onPause(activity);	
		if (mfingerprintHelper != null) {
			mfingerprintHelper.stopListening();
		}	
	}

	@Kroll.method
	public void invalidate() {
		if (mfingerprintHelper != null) {
			mfingerprintHelper.stopListening();
		}
	}
}
