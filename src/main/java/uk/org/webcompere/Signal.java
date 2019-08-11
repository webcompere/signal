package uk.org.webcompere;

/**
 * Represents a flag which can be set or waited for - once signalled, things waiting for it don't block
 */
public class Signal {
	private boolean signalled;
	private Object signalSync = new Object();

	/**
	 * Set the done flag
	 */
	public void setSignal() {
		synchronized (signalSync) {
			signalled = true;
		}
		synchronized (this) {
			notifyAll();
		}
	}

	/**
	 * Clear the signal - doesn't notify, as nothing should be waiting for this, even if they are
	 * they're waiting for it to go true
	 */
	public void clearSignal() {
		synchronized (signalSync) {
			signalled = false;
		}
	}

	/**
	 * Wait up to timeout for the signal
	 *
	 * @param timeout timeout in milliseconds - this will be honoured unless wait wakes up spuriously
	 * @throws InterruptedException
	 */
	public void waitForSignal(long timeout) throws InterruptedException {
		if (!isSignalled()) {
			synchronized (this) {
				wait(timeout);
			}
		}
	}

	/**
	 * Wait indefinitely for the done signal
	 *
	 * @throws InterruptedException on thread error
	 */
	public void waitForSignal() throws InterruptedException {
		// as wait can wake up spuriously, put this in a loop
		while (!isSignalled()) {
			synchronized (this) {
				wait();
			}
		}
	}

	/**
	 * Peek at the signal
	 *
	 * @return the state of the signal
	 */
	public boolean isSignalled() {
		synchronized (signalSync) {
			return signalled;
		}
	}
}
