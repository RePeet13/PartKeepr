package com.unrulyrecursion.partkeeprconnector;

import com.unrulyrecursion.partkeeprconnector.utilities.SessionManagement;
import com.unrulyrecursion.partkeeprconnector.model.DBHelper;

import android.app.Application;

public class PartKeeprConnectorApp extends Application {
	
	private SessionManagement mSession;
	private String base_url;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public String getBase_url() {
		return base_url;
	}

	public void setBase_url(String base_url) {
		this.base_url = base_url;
	}

	public SessionManagement getmSession() {
		return mSession;
	}

	public void setmSession(SessionManagement mSession) {
		this.mSession = mSession;
	}
}
