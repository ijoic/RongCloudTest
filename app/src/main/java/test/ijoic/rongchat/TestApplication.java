package test.ijoic.rongchat;

import android.app.Application;

import io.rong.imkit.RongIM;

/**
 * Simple Test Application
 *
 * @author ijoic
 */
public class TestApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    RongIM.getInstance().init(this);
  }
}
