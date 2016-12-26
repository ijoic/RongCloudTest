package test.ijoic.rongchat;

import android.os.Handler;

/**
 * 重复事件检查器
 *
 * <p>隔一段时间，执行一次检查判断，直到判断返回值为true</p>
 *
 * @author ijoic
 */
public class RepeatActionChecker {

  /**
   * 检查器
   */
  interface Checker {
    /**
     * 执行检查
     *
     * @return 检查结果
     */
    boolean performCheck();

    /**
     * 检查成功回调
     *
     * @param passedDuration 检查过程消耗的毫秒数
     */
    void onCheckSuccess(long passedDuration);

    /**
     * 检查失败回调
     *
     * @param passedDuration 检查过程消耗的毫秒数
     */
    void onCheckFailed(long passedDuration);
  }

  private Checker checker;
  private Runnable checkRunnable;
  private long checkDuration;
  private long passedDuration;

  private static final Handler handler = new Handler();

  private static final long MIN_CHECK_DURATION = 100;

  /**
   * 设置检查器
   *
   * @param checker 检查器
   */
  public void setChecker(Checker checker) {
    this.checker = checker;
  }

  /**
   * 设置两次检查相隔的毫秒数
   *
   * @param duration 两次检查相隔的毫秒数
   */
  public void setCheckDuration(long duration) {
    this.checkDuration = Math.max(duration, MIN_CHECK_DURATION);
  }

  /**
   * 执行检查
   */
  public void performCheck() {
    passedDuration = 0;

    if (checkRunnable == null) {
      checkRunnable = new Runnable() {
        @Override
        public void run() {
          passedDuration += checkDuration;

          if (checker == null) {
            return;
          }
          if (checker.performCheck()) {
            checker.onCheckSuccess(passedDuration);
          } else {
            checker.onCheckFailed(passedDuration);
            handler.postDelayed(this, checkDuration);
          }
        }
      };
    }
    handler.postDelayed(checkRunnable, checkDuration);
  }

  /**
   * 取消检查
   */
  public void cancel() {
    passedDuration = 0;

    if (checkRunnable != null) {
      handler.removeCallbacks(checkRunnable);
    }
  }

}
