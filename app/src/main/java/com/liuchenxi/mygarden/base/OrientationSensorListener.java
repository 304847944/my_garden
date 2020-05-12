package com.liuchenxi.mygarden.base;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.view.Surface;

import androidx.annotation.IntDef;

import com.liuchenxi.mygarden.R;

/**
 * 横竖屏、重力监听！
 */
public class OrientationSensorListener implements SensorEventListener {
    // Number of nanoseconds per millisecond.
    protected static final long NANOS_PER_MS = 1000000;
    // Number of milliseconds per nano second.
    protected static final float MILLIS_PER_NANO = 0.000001f;
    // The minimum amount of time that must have elapsed since the screen was last touched
    // before the proposed rotation can change.
    protected static final long PROPOSAL_MIN_TIME_SINCE_TOUCH_END_NANOS =
        500 * NANOS_PER_MS;

    // We work with all angles in degrees in this class.
    private static final float RADIANS_TO_DEGREES = (float) (180 / Math.PI);
    // Indices into SensorEvent.values for the accelerometer sensor.
    private static final int ACCELEROMETER_DATA_X = 0;
    private static final int ACCELEROMETER_DATA_Y = 1;
    private static final int ACCELEROMETER_DATA_Z = 2;
    // The minimum amount of time that a predicted rotation must be stable before it
    // is accepted as a valid rotation proposal.  This value can be quite small because
    // the low-pass filter already suppresses most of the noise so we're really just
    // looking for quick confirmation that the last few samples are in agreement as to
    // the desired orientation.
    private static final long PROPOSAL_SETTLE_TIME_NANOS = 100 * NANOS_PER_MS;
    // The minimum amount of time that must have elapsed since the device last exited
    // the flat state (time since it was picked up) before the proposed rotation
    // can change.
    private static final long PROPOSAL_MIN_TIME_SINCE_FLAT_ENDED_NANOS = 500 * NANOS_PER_MS;
    // The minimum amount of time that must have elapsed since the device stopped
    // swinging (time since device appeared to be in the process of being put down
    // or put away into a pocket) before the proposed rotation can change.
    private static final long PROPOSAL_MIN_TIME_SINCE_SWING_ENDED_NANOS = 300 * NANOS_PER_MS;
    // The minimum amount of time that must have elapsed since the device stopped
    // undergoing external acceleration before the proposed rotation can change.
    private static final long PROPOSAL_MIN_TIME_SINCE_ACCELERATION_ENDED_NANOS =
        500 * NANOS_PER_MS;
    // If the tilt angle remains greater than the specified angle for a minimum of
    // the specified time, then the device is deemed to be lying flat
    // (just chillin' on a table).
    private static final float FLAT_ANGLE = 80;
    private static final long FLAT_TIME_NANOS = 1000 * NANOS_PER_MS;
    // If the tilt angle has increased by at least delta degrees within the specified amount
    // of time, then the device is deemed to be swinging away from the user
    // down towards flat (tilt = 90).
    private static final float SWING_AWAY_ANGLE_DELTA = 20;
    private static final long SWING_TIME_NANOS = 300 * NANOS_PER_MS;
    // The maximum sample inter-arrival time in milliseconds.
    // If the acceleration samples are further apart than this amount in time, we reset the
    // state of the low-pass filter and orientation properties.  This helps to handle
    // boundary conditions when the device is turned on, wakes from suspend or there is
    // a significant gap in samples.
    private static final long MAX_FILTER_DELTA_TIME_NANOS = 1000 * NANOS_PER_MS;
    // The acceleration filter time constant.
    //
    // This time constant is used to tune the acceleration filter such that
    // impulses and vibrational noise (think car dock) is suppressed before we
    // try to calculate the tilt and orientation angles.
    //
    // The filter time constant is related to the filter cutoff frequency, which is the
    // frequency at which signals are attenuated by 3dB (half the passband power).
    // Each successive octave beyond this frequency is attenuated by an additional 6dB.
    //
    // Given a time constant t in seconds, the filter cutoff frequency Fc in Hertz
    // is given by Fc = 1 / (2pi * t).
    //
    // The higher the time constant, the lower the cutoff frequency, so more noise
    // will be suppressed.
    //
    // Filtering adds latency proportional the time constant (inversely proportional
    // to the cutoff frequency) so we don't want to make the time constant too
    // large or we can lose responsiveness.  Likewise we don't want to make it too
    // small or we do a poor job suppressing acceleration spikes.
    // Empirically, 100ms seems to be too small and 500ms is too large.
    private static final float FILTER_TIME_CONSTANT_MS = 200.0f;
    /* State for orientation detection. */
    // Thresholds for minimum and maximum allowable deviation from gravity.
    //
    // If the device is undergoing external acceleration (being bumped, in a car
    // that is turning around a corner or a plane taking off) then the magnitude
    // may be substantially more or less than gravity.  This can skew our orientation
    // detection by making us think that up is pointed in a different direction.
    //
    // Conversely, if the device is in freefall, then there will be no gravity to
    // measure at all.  This is problematic because we cannot detect the orientation
    // without gravity to tell us which way is up.  A magnitude near 0 produces
    // singularities in the tilt and orientation calculations.
    //
    // In both cases, we postpone choosing an orientation.
    //
    // However, we need to tolerate some acceleration because the angular momentum
    // of turning the device can skew the observed acceleration for a short period of time.
    private static final float NEAR_ZERO_MAGNITUDE = 1; // m/s^2
    private static final float ACCELERATION_TOLERANCE = 4; // m/s^2
    private static final float MIN_ACCELERATION_MAGNITUDE =
        SensorManager.STANDARD_GRAVITY - ACCELERATION_TOLERANCE;
    private static final float MAX_ACCELERATION_MAGNITUDE =
        SensorManager.STANDARD_GRAVITY + ACCELERATION_TOLERANCE;
    // Maximum absolute tilt angle at which to consider orientation data.  Beyond this (i.e.
    // when screen is facing the sky or ground), we completely ignore orientation data
    // because it's too unstable.
    private static final int MAX_TILT = 80;
    // The tilt angle below which we conclude that the user is holding the device
    // overhead reading in bed and lock into that state.
    private static final int TILT_OVERHEAD_ENTER = -40;
    // The tilt angle above which we conclude that the user would like a rotation
    // change to occur and unlock from the overhead state.
    private static final int TILT_OVERHEAD_EXIT = -15;
    // The gap angle in degrees between adjacent orientation angles for hysteresis.
    // This creates a "dead zone" between the current orientation and a proposed
    // adjacent orientation.  No orientation proposal is made when the orientation
    // angle is within the gap between the current orientation and the adjacent
    // orientation.
    private static final int ADJACENT_ORIENTATION_ANGLE_GAP = 45;
    // The tilt angle range in degrees for each orientation.
    // Beyond these tilt angles, we don't even consider transitioning into the
    // specified orientation.  We place more stringent requirements on unnatural
    // orientations than natural ones to make it less likely to accidentally transition
    // into those states.
    // The first value of each pair is negative so it applies a limit when the device is
    // facing down (overhead reading in bed).
    // The second value of each pair is positive so it applies a limit when the device is
    // facing up (resting on a table).
    // The ideal tilt angle is 0 (when the device is vertical) so the limits establish
    // how close to vertical the device must be in order to change orientation.
    private final int[][] mTiltToleranceConfig = new int[][] {
        /* ROTATION_0   */ { -25, 70 }, // note: these are overridden by config.xml
        /* ROTATION_90  */ { -25, 65 },
        /* ROTATION_180 */ { -25, 60 },
        /* ROTATION_270 */ { -25, 65 }
    };
    private Context mContext;
    // Timestamp and value of the last accelerometer sample.
    private long mLastFilteredTimestampNanos = -1;
    private float mLastFilteredX, mLastFilteredY, mLastFilteredZ;
    // The last proposed rotation, -1 if unknown.
    private int mProposedRotation;
    // Value of the current predicted rotation, -1 if unknown.
    private int mPredictedRotation;
    // Timestamp of when the predicted rotation most recently changed.
    private long mPredictedRotationTimestampNanos;
    // Timestamp when the device last appeared to be flat for sure (the flat delay elapsed).
    private long mFlatTimestampNanos;
    private boolean mFlat;
    // Timestamp when the device last appeared to be swinging.
    private long mSwingTimestampNanos;
    private boolean mSwinging;
    // Timestamp when the device last appeared to be undergoing external acceleration.
    private long mAccelerationTimestampNanos;
    private boolean mAccelerating;
    // Timestamp when the last touch to the touch screen ended
    private long mTouchEndedTimestampNanos = Long.MIN_VALUE;
    private boolean mTouched;
    // Whether we are locked into an overhead usage mode.
    private boolean mOverhead;
    // History of observed tilt angles.
    private static final int TILT_HISTORY_SIZE = 200;
    private float[] mTiltHistory = new float[TILT_HISTORY_SIZE];
    private long[] mTiltHistoryTimestampNanos = new long[TILT_HISTORY_SIZE];
    private int mTiltHistoryIndex;

    /**
     * 初始化手机方向，使用此属性来确保当用户主动切为横屏或竖屏时（如用在播放视频时用户主动点击全屏，或退出全屏播放）
     * 当手机方向与切换后的反向不一致时不会切为原来的模式
     */
    private boolean mIsLocked = false;
    //private int mInitialOrientation;
    // The last proposed rotation, -1 if unknown.
    private int mCurrentRotation;

    @IntDef({ Surface.ROTATION_0, Surface.ROTATION_90, Surface.ROTATION_180, Surface.ROTATION_270 })
    public @interface Orientation {

    }

    /**
     * 方向变化监听器
     */
    private OrientationWatcher mWatcher;

    /**
     * 屏幕旋转时的回调接口
     */
    public interface OrientationWatcher {

        /**
         * 当手机屏幕方向与当前页面方向不一致时调用次方法
         *
         * @param sensorListener OrientationSensorListener实例
         * @param newOrientation 新的屏幕方向，{@link Surface#ROTATION_0},{@link Surface#ROTATION_90},
         * {@link Surface#ROTATION_180}和 {@link Surface#ROTATION_270}其中之一
         */
        void onOrientationChanged(OrientationSensorListener sensorListener,
                                  @Orientation int newOrientation);
    }

    public OrientationSensorListener(Context mContext , boolean forceLandScape) {
        this.mContext = mContext;
        this.mCurrentRotation = forceLandScape ? Surface.ROTATION_90
            : Surface.ROTATION_0;
        this.mProposedRotation = forceLandScape ? Surface.ROTATION_90
            : Surface.ROTATION_0;
        mIsLocked = forceLandScape;

        // Load tilt tolerance configuration.
        int[] tiltTolerance = mContext.getResources().getIntArray(
            R.array.config_autoRotationTiltTolerance);
        if (tiltTolerance.length == 8) {
            for (int i = 0; i < 4; i++) {
                int min = tiltTolerance[i * 2];
                int max = tiltTolerance[i * 2 + 1];
                if (min >= -90 && min <= max && max <= 90) {
                    mTiltToleranceConfig[i][0] = min;
                    mTiltToleranceConfig[i][1] = max;
                }
            }
        }
    }

    public void lockScreenOrientation(int rotation) {
        // mInitialOrientation = rotation;
        mIsLocked =true;
        setCurrentOrientation(rotation);
    }

    public void lockCurrentScreenOrientation() {
        lockScreenOrientation(mCurrentRotation);
    }
    public void setCurrentOrientation(int rotation) {
        mCurrentRotation = rotation;
    }

    public void setOrientationWatcher(OrientationSensorListener.OrientationWatcher watcher) {
        this.mWatcher = watcher;
    }

    /**
     * 获取方向变化的监听者
     */
    public OrientationSensorListener.OrientationWatcher getWatcher() {
        return mWatcher;
    }

    public int getProposedRotationLocked() {
        return mProposedRotation;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isLockedBySettings() || mWatcher == null) {
            return;
        }
        int proposedRotation;
        int oldProposedRotation;
        // The vector given in the SensorEvent points straight up (towards the sky) under
        // ideal conditions (the phone is not accelerating).  I'll call this up vector
        // elsewhere.
        float x = event.values[ACCELEROMETER_DATA_X];
        float y = event.values[ACCELEROMETER_DATA_Y];
        float z = event.values[ACCELEROMETER_DATA_Z];

        // Apply a low-pass filter to the acceleration up vector in cartesian space.
        // Reset the orientation listener state if the samples are too far apart in time
        // or when we see values of (0, 0, 0) which indicates that we polled the
        // accelerometer too soon after turning it on and we don't have any data yet.
        final long now = event.timestamp;
        final long then = mLastFilteredTimestampNanos;
        final float timeDeltaMS = (now - then) * 0.000001f;
        final boolean skipSample;
        if (now < then
            || (then > 0 && now > then + MAX_FILTER_DELTA_TIME_NANOS)
            || (x == 0 && y == 0 && z == 0)) {

            resetLocked(true /* clearCurrentRotation */);
            skipSample = true;
        } else {
            final float alpha = timeDeltaMS / (FILTER_TIME_CONSTANT_MS + timeDeltaMS);
            x = alpha * (x - mLastFilteredX) + mLastFilteredX;
            y = alpha * (y - mLastFilteredY) + mLastFilteredY;
            z = alpha * (z - mLastFilteredZ) + mLastFilteredZ;

            skipSample = false;
        }
        mLastFilteredTimestampNanos = now;
        mLastFilteredX = x;
        mLastFilteredY = y;
        mLastFilteredZ = z;
        boolean isAccelerating = false;
        boolean isFlat = false;
        boolean isSwinging = false;
        if (!skipSample) {
            // Calculate the magnitude of the acceleration vector.
            final float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
            if (magnitude < NEAR_ZERO_MAGNITUDE) {

                clearPredictedRotationLocked();
            } else {
                // Determine whether the device appears to be undergoing external
                // acceleration.
                if (isAcceleratingLocked(magnitude)) {
                    isAccelerating = true;
                    mAccelerationTimestampNanos = now;
                }
                // Calculate the tilt angle.
                // This is the angle between the up vector and the x-y plane (the plane of
                // the screen) in a range of [-90, 90] degrees.
                //   -90 degrees: screen horizontal and facing the ground (overhead)
                //     0 degrees: screen vertical
                //    90 degrees: screen horizontal and facing the sky (on table)
                final int tiltAngle = (int) Math.round(
                    Math.asin(z / magnitude) * RADIANS_TO_DEGREES);
                addTiltHistoryEntryLocked(now, tiltAngle);
                // Determine whether the device appears to be flat or swinging.
                if (isFlatLocked(now)) {
                    isFlat = true;
                    mFlatTimestampNanos = now;
                }
                if (isSwingingLocked(now, tiltAngle)) {
                    isSwinging = true;
                    mSwingTimestampNanos = now;
                }
                // If the tilt angle is too close to horizontal then we cannot determine
                // the orientation angle of the screen.
                if (tiltAngle <= TILT_OVERHEAD_ENTER) {
                    mOverhead = true;
                } else if (tiltAngle >= TILT_OVERHEAD_EXIT) {
                    mOverhead = false;
                }
                if (mOverhead) {

                    clearPredictedRotationLocked();
                } else if (Math.abs(tiltAngle) > MAX_TILT) {

                    clearPredictedRotationLocked();
                } else {
                    // Calculate the orientation angle.
                    // This is the angle between the x-y projection of the up vector onto
                    // the +y-axis, increasing clockwise in a range of [0, 360] degrees.
                    int orientationAngle = (int) Math.round(
                        -Math.atan2(-x, y) * RADIANS_TO_DEGREES);
                    if (orientationAngle < 0) {
                        // atan2 returns [-180, 180]; normalize to [0, 360]
                        orientationAngle += 360;
                    }
                    // Find the nearest rotation.
                    int nearestRotation = (orientationAngle + 45) / 90;
                    if (nearestRotation == 4) {
                        nearestRotation = 0;
                    }
                    // Determine the predicted orientation.
                    if (isTiltAngleAcceptableLocked(nearestRotation, tiltAngle)
                        && isOrientationAngleAcceptableLocked(nearestRotation,
                        orientationAngle)) {
                        updatePredictedRotationLocked(now, nearestRotation);
                    } else {
                        clearPredictedRotationLocked();
                    }
                }
            }
        }
        mFlat = isFlat;
        mSwinging = isSwinging;
        mAccelerating = isAccelerating;
        // Determine new proposed rotation.
        oldProposedRotation = mProposedRotation;

        if (mPredictedRotation < 0 || isPredictedRotationAcceptableLocked(now) && (!mIsLocked || mPredictedRotation == mCurrentRotation)) {
            mIsLocked = false;
            mProposedRotation = mPredictedRotation;
        }
        proposedRotation = mProposedRotation;

        if (proposedRotation != oldProposedRotation && proposedRotation >= 0) {
            onProposedRotationChanged(proposedRotation);
        }
    }

    private void onProposedRotationChanged(int rotation) {
        if (mWatcher != null) {

            mWatcher.onOrientationChanged(this, rotation);
        }
    }

    /**
     * 判断系统是否禁止了自动旋转模式
     */
    private boolean isLockedBySettings() {
        // 启动重力感应 1为自动旋转模式，0为锁定竖屏模式
        return (Settings.System.getInt(mContext.getContentResolver(),
            Settings.System.ACCELEROMETER_ROTATION, 0) != 1);
    }

    public void onTouchStartLocked() {
        mTouched = true;
    }

    public void onTouchEndLocked(long whenElapsedNanos) {
        mTouched = false;
        mTouchEndedTimestampNanos = whenElapsedNanos;
    }

    public void resetLocked(boolean clearCurrentRotation) {
        mLastFilteredTimestampNanos = Long.MIN_VALUE;
        if (clearCurrentRotation) {
            mProposedRotation = -1;
        }
        mFlatTimestampNanos = Long.MIN_VALUE;
        mFlat = false;
        mSwingTimestampNanos = Long.MIN_VALUE;
        mSwinging = false;
        mAccelerationTimestampNanos = Long.MIN_VALUE;
        mAccelerating = false;
        mOverhead = false;
        clearPredictedRotationLocked();
        clearTiltHistoryLocked();
    }

    /**
     * Returns true if the tilt angle is acceptable for a given predicted rotation.
     */
    private boolean isTiltAngleAcceptableLocked(int rotation, int tiltAngle) {
        return tiltAngle >= mTiltToleranceConfig[rotation][0]
            && tiltAngle <= mTiltToleranceConfig[rotation][1];
    }

    /**
     * Returns true if the orientation angle is acceptable for a given predicted rotation.
     *
     * This function takes into account the gap between adjacent orientations
     * for hysteresis.
     */
    private boolean isOrientationAngleAcceptableLocked(int rotation, int orientationAngle) {
        // If there is no current rotation, then there is no gap.
        // The gap is used only to introduce hysteresis among advertised orientation
        // changes to avoid flapping.
        final int currentRotation = mCurrentRotation;
        if (currentRotation >= 0) {
            // If the specified rotation is the same or is counter-clockwise adjacent
            // to the current rotation, then we set a lower bound on the orientation angle.
            // For example, if currentRotation is ROTATION_0 and proposed is ROTATION_90,
            // then we want to check orientationAngle > 45 + GAP / 2.
            if (rotation == currentRotation
                || rotation == (currentRotation + 1) % 4) {
                int lowerBound = rotation * 90 - 45
                    + ADJACENT_ORIENTATION_ANGLE_GAP / 2;
                if (rotation == 0) {
                    if (orientationAngle >= 315 && orientationAngle < lowerBound + 360) {
                        return false;
                    }
                } else {
                    if (orientationAngle < lowerBound) {
                        return false;
                    }
                }
            }
            // If the specified rotation is the same or is clockwise adjacent,
            // then we set an upper bound on the orientation angle.
            // For example, if currentRotation is ROTATION_0 and rotation is ROTATION_270,
            // then we want to check orientationAngle < 315 - GAP / 2.
            if (rotation == currentRotation
                || rotation == (currentRotation + 3) % 4) {
                int upperBound = rotation * 90 + 45
                    - ADJACENT_ORIENTATION_ANGLE_GAP / 2;
                if (rotation == 0) {
                    if (orientationAngle <= 45 && orientationAngle > upperBound) {
                        return false;
                    }
                } else {
                    if (orientationAngle > upperBound) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns true if the predicted rotation is ready to be advertised as a
     * proposed rotation.
     */
    private boolean isPredictedRotationAcceptableLocked(long now) {
        // The predicted rotation must have settled long enough.
        if (now < mPredictedRotationTimestampNanos + PROPOSAL_SETTLE_TIME_NANOS) {
            return false;
        }
        // The last flat state (time since picked up) must have been sufficiently long ago.
        if (now < mFlatTimestampNanos + PROPOSAL_MIN_TIME_SINCE_FLAT_ENDED_NANOS) {
            return false;
        }
        // The last swing state (time since last movement to put down) must have been
        // sufficiently long ago.
        if (now < mSwingTimestampNanos + PROPOSAL_MIN_TIME_SINCE_SWING_ENDED_NANOS) {
            return false;
        }
        // The last acceleration state must have been sufficiently long ago.
        if (now < mAccelerationTimestampNanos
            + PROPOSAL_MIN_TIME_SINCE_ACCELERATION_ENDED_NANOS) {
            return false;
        }
        // The last touch must have ended sufficiently long ago.
        if (mTouched || now < mTouchEndedTimestampNanos
            + PROPOSAL_MIN_TIME_SINCE_TOUCH_END_NANOS) {
            return false;
        }
        // Looks good!
        return true;
    }

    private void clearPredictedRotationLocked() {
        mPredictedRotation = -1;
        mPredictedRotationTimestampNanos = Long.MIN_VALUE;
    }

    private void updatePredictedRotationLocked(long now, int rotation) {
        if (mPredictedRotation != rotation) {
            mPredictedRotation = rotation;
            mPredictedRotationTimestampNanos = now;
        }
    }

    private boolean isAcceleratingLocked(float magnitude) {
        return magnitude < MIN_ACCELERATION_MAGNITUDE
            || magnitude > MAX_ACCELERATION_MAGNITUDE;
    }

    private void clearTiltHistoryLocked() {
        mTiltHistoryTimestampNanos[0] = Long.MIN_VALUE;
        mTiltHistoryIndex = 1;
    }

    private void addTiltHistoryEntryLocked(long now, float tilt) {
        mTiltHistory[mTiltHistoryIndex] = tilt;
        mTiltHistoryTimestampNanos[mTiltHistoryIndex] = now;
        mTiltHistoryIndex = (mTiltHistoryIndex + 1) % TILT_HISTORY_SIZE;
        mTiltHistoryTimestampNanos[mTiltHistoryIndex] = Long.MIN_VALUE;
    }

    private boolean isFlatLocked(long now) {
        for (int i = mTiltHistoryIndex; (i = nextTiltHistoryIndexLocked(i)) >= 0; ) {
            if (mTiltHistory[i] < FLAT_ANGLE) {
                break;
            }
            if (mTiltHistoryTimestampNanos[i] + FLAT_TIME_NANOS <= now) {
                // Tilt has remained greater than FLAT_TILT_ANGLE for FLAT_TIME_NANOS.
                return true;
            }
        }
        return false;
    }

    private boolean isSwingingLocked(long now, float tilt) {
        for (int i = mTiltHistoryIndex; (i = nextTiltHistoryIndexLocked(i)) >= 0; ) {
            if (mTiltHistoryTimestampNanos[i] + SWING_TIME_NANOS < now) {
                break;
            }
            if (mTiltHistory[i] + SWING_AWAY_ANGLE_DELTA <= tilt) {
                // Tilted away by SWING_AWAY_ANGLE_DELTA within SWING_TIME_NANOS.
                return true;
            }
        }
        return false;
    }

    private int nextTiltHistoryIndexLocked(int index) {
        index = (index == 0 ? TILT_HISTORY_SIZE : index) - 1;
        return mTiltHistoryTimestampNanos[index] != Long.MIN_VALUE ? index : -1;
    }
}
