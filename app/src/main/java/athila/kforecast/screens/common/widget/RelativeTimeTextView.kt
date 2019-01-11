package athila.kforecast.screens.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import athila.kforecast.R;

import java.lang.ref.WeakReference


/**
 * A `TextView` that, given a reference time, renders that time as a time period relative to the current time.
 * @author Kiran Rao
 * @see .setReferenceTime
 */
class RelativeTimeTextView : TextView {

  private var mReferenceTime: Long = 0
  private var mPrefix: String? = null
  private var mSuffix: String? = null
  private val mHandler = Handler()
  private var mUpdateTimeTask: UpdateTimeRunnable? = null
  private var isUpdateTaskRunning = false

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init(context, attrs)
  }

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
    init(context, attrs)
  }

  private fun init(context: Context, attrs: AttributeSet) {
    val a = context.theme.obtainStyledAttributes(attrs,
        R.styleable.RelativeTimeTextView, 0, 0)
    try {
      mPrefix = a.getString(R.styleable.RelativeTimeTextView_relative_time_prefix)
      mSuffix = a.getString(R.styleable.RelativeTimeTextView_relative_time_suffix)

      mPrefix = if (mPrefix == null) "" else mPrefix
      mSuffix = if (mSuffix == null) "" else mSuffix
    } finally {
      a.recycle()
    }
  }

  /**
   * Sets the reference time for this view. At any moment, the view will render a relative time period relative to the time set here.
   *
   *
   * This value can also be set with the XML attribute `reference_time`
   * @param referenceTime The timestamp (in milliseconds since epoch) that will be the reference point for this view.
   */
  fun setReferenceTime(referenceTime: Long) {
    this.mReferenceTime = referenceTime

    /*
         * Note that this method could be called when a row in a ListView is recycled.
         * Hence, we need to first stop any currently running schedules (for example from the recycled view.
         */
    stopTaskForPeriodicallyUpdatingRelativeTime()

    /*
         * Instantiate a new runnable with the new reference time
         */
    initUpdateTimeTask()

    /*
         * Start a new schedule.
         */
    startTaskForPeriodicallyUpdatingRelativeTime()

    /*
         * Finally, update the text display.
         */
    updateTextDisplay()
  }

  @SuppressLint("SetTextI18n")
  private fun updateTextDisplay() {
    /*
         * TODO: Validation, Better handling of negative cases
         */
    if (this.mReferenceTime == -1L)
      return
    text = mPrefix + getRelativeTimeDisplayString(mReferenceTime, System.currentTimeMillis()) + mSuffix
  }

  /**
   * Get the text to display for relative time. By default, this calls [DateUtils.getRelativeTimeSpanString] passing [DateUtils.FORMAT_ABBREV_RELATIVE] flag.
   * <br></br>
   * You can override this method to customize the string returned. For example you could add prefixes or suffixes, or use Spans to style the string etc
   * @param referenceTime The reference time passed in through [.setReferenceTime] or through `reference_time` attribute
   * @param now The current time
   * @return The display text for the relative time
   */
  protected fun getRelativeTimeDisplayString(referenceTime: Long, now: Long): CharSequence {
    val difference = now - referenceTime
    return if (difference >= 0 && difference <= DateUtils.MINUTE_IN_MILLIS)
      resources.getString(R.string.just_now)
    else
      DateUtils.getRelativeTimeSpanString(
          mReferenceTime,
          now,
          DateUtils.MINUTE_IN_MILLIS,
          DateUtils.FORMAT_ABBREV_RELATIVE)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    startTaskForPeriodicallyUpdatingRelativeTime()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    stopTaskForPeriodicallyUpdatingRelativeTime()
  }

  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    super.onVisibilityChanged(changedView, visibility)
    if (visibility == View.GONE || visibility == View.INVISIBLE) {
      stopTaskForPeriodicallyUpdatingRelativeTime()
    } else {
      startTaskForPeriodicallyUpdatingRelativeTime()
    }
  }

  private fun startTaskForPeriodicallyUpdatingRelativeTime() {
    if (mUpdateTimeTask?.isDetached == true) initUpdateTimeTask()
    mHandler.post(mUpdateTimeTask)
    isUpdateTaskRunning = true
  }

  private fun initUpdateTimeTask() {
    mUpdateTimeTask = UpdateTimeRunnable(this, mReferenceTime)
  }

  private fun stopTaskForPeriodicallyUpdatingRelativeTime() {
    if (isUpdateTaskRunning) {
      mUpdateTimeTask?.detach()
      mHandler.removeCallbacks(mUpdateTimeTask)
      isUpdateTaskRunning = false
    }
  }

  override fun onSaveInstanceState(): Parcelable? {
    val superState = super.onSaveInstanceState()
    val ss = SavedState(superState)
    ss.referenceTime = mReferenceTime
    return ss
  }

  override fun onRestoreInstanceState(state: Parcelable) {
    if (state !is SavedState) {
      super.onRestoreInstanceState(state)
      return
    }

    mReferenceTime = state.referenceTime
    super.onRestoreInstanceState(state.superState)
  }

  class SavedState : View.BaseSavedState {

    internal var referenceTime: Long = 0

    constructor(superState: Parcelable) : super(superState) {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
      super.writeToParcel(dest, flags)
      dest.writeLong(referenceTime)
    }

    private constructor(`in`: Parcel) : super(`in`) {
      referenceTime = `in`.readLong()
    }

    companion object {

      @JvmField
      val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
        override fun createFromParcel(`in`: Parcel): SavedState {
          return SavedState(`in`)
        }

        override fun newArray(size: Int): Array<SavedState?> {
          return arrayOfNulls(size)
        }
      }
    }
  }

  private class UpdateTimeRunnable internal constructor(rttv: RelativeTimeTextView, private val mRefTime: Long,
      private val weakRefRttv: WeakReference<RelativeTimeTextView> = WeakReference(rttv)) : Runnable {

    internal val isDetached: Boolean
      get() = weakRefRttv.get() == null

    internal fun detach() {
      weakRefRttv.clear()
    }

    override fun run() {
      val rttv = weakRefRttv.get() ?: return
      val difference = Math.abs(System.currentTimeMillis() - mRefTime)
      var interval = INITIAL_UPDATE_INTERVAL
      if (difference > DateUtils.WEEK_IN_MILLIS) {
        interval = DateUtils.WEEK_IN_MILLIS
      } else if (difference > DateUtils.DAY_IN_MILLIS) {
        interval = DateUtils.DAY_IN_MILLIS
      } else if (difference > DateUtils.HOUR_IN_MILLIS) {
        interval = DateUtils.HOUR_IN_MILLIS
      }
      rttv.updateTextDisplay()
      rttv.mHandler.postDelayed(this, interval)

    }
  }

  companion object {

    private val INITIAL_UPDATE_INTERVAL = DateUtils.MINUTE_IN_MILLIS
  }
}