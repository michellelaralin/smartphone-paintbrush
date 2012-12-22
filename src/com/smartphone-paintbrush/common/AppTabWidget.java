package com.internalerror.common;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.internalerror.measure.MeasureActivity;
import com.internalerror.R;
import com.internalerror.sketching.TestGraphicsActivity;

public class AppTabWidget extends TabActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, MeasureActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("options").setIndicator("Options",
                          res.getDrawable(R.drawable.question))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, TestGraphicsActivity.class);
        spec = tabHost.newTabSpec("drawing").setIndicator("Drawing",
                res.getDrawable(R.drawable.exclamation))
            .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
	}
}
