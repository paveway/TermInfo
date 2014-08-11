package info.paveway.terminfo;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    private Resources mResources;

    private TabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mResources = getResources();

        mTabHost = getTabHost();
        addTab(SystemDataListActivity.class,  "system",  "System",  R.drawable.ic_tab_system);
        addTab(DisplayDataListActivity.class, "display", "Display", R.drawable.ic_tab_display);
        addTab(SensorDataListActivity.class,  "sensor",  "Sensor",  R.drawable.ic_tab_sensor);
        addTab(CameraDataListActivity.class,  "camera",  "Camera",  R.drawable.ic_tab_camera);
    }

    @SuppressWarnings("rawtypes")
    private void addTab(Class dst, String tag, String label, int id) {
        Intent intent = new Intent().setClass(this, dst);
        TabHost.TabSpec spec = mTabHost.newTabSpec(tag);
        spec.setIndicator(label, mResources.getDrawable(id));
        spec.setContent(intent);
        mTabHost.addTab(spec);
    }
}
