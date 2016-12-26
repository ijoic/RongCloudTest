package test.ijoic.rongchat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class MainActivity extends AppCompatActivity {

  private RepeatActionChecker connectChecker = new RepeatActionChecker();
  private boolean connectResponse;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    connectResponse = false;

    // 连接状态检查配置
    connectChecker.setCheckDuration(10000);
    connectChecker.setChecker(new RepeatActionChecker.Checker() {
      @Override
      public boolean performCheck() {
        return connectResponse;
      }

      @Override
      public void onCheckSuccess(long passedDuration) {
      }

      @Override
      public void onCheckFailed(long passedDuration) {
        showMessage("connection not response after " + (passedDuration / 1000) + " seconds");
      }
    });
    connectChecker.performCheck();

    // 发起连接
    RongIM.connect(TestConfig.USER_TOCKEN_1, new RongIMClient.ConnectCallback() {
      @Override
      public void onTokenIncorrect() {
        connectResponse = true;
        showMessage("token incorrect!!");
      }

      @Override
      public void onSuccess(String s) {
        connectResponse = true;
        showMessage("connect success: " + s);
      }

      @Override
      public void onError(RongIMClient.ErrorCode errorCode) {
        connectResponse = true;
        showMessage("connect error: " + (errorCode == null ? "" : errorCode.getMessage()));
      }
    });
  }

  protected void onDestroy() {
    super.onDestroy();
    connectChecker.cancel();
  }

  private void showMessage(@NonNull String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
