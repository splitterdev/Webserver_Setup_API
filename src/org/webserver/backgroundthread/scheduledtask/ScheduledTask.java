package org.webserver.backgroundthread.scheduledtask;

public abstract class ScheduledTask extends Thread {

	private long delayInMillis;
	private long repeatTimes;
	private boolean autoCloseOnFailure;
	private boolean keepRunning;

	protected ScheduledTask(long delayInMillis, boolean autoCloseOnFailure) {
		this(delayInMillis, -1L, autoCloseOnFailure);
	}

	protected ScheduledTask(long delayInMillis, long repeatTimes, boolean autoCloseOnFailure) {
		this.delayInMillis = delayInMillis;
		this.autoCloseOnFailure = autoCloseOnFailure;
		this.keepRunning = true;
		this.repeatTimes = repeatTimes;
	}

	public long getDelayInMillis() {
		return delayInMillis;
	}

	public void setDelayInMillis(long delayInMillis) {
		this.delayInMillis = delayInMillis;
	}

	public boolean isAutoCloseOnFailure() {
		return autoCloseOnFailure;
	}

	public void setAutoCloseOnFailure(boolean autoCloseOnFailure) {
		this.autoCloseOnFailure = autoCloseOnFailure;
	}

	public boolean isKeepRunning() {
		return keepRunning;
	}

	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

	protected abstract void performTask() throws Exception;

	@Override
	public void run() {
		long executedTimes = 0;
		while (keepRunning) {
			try {
				executedTimes++;
				if (repeatTimes >= 0 && executedTimes > repeatTimes) {
					keepRunning = false;
				} else {
					sleep(delayInMillis);
					performTask();
				}
			} catch (Exception e) {
				if (autoCloseOnFailure) {
					keepRunning = false;
				}
				e.printStackTrace();
			}
		}
	}
}
