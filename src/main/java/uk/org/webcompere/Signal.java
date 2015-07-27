package uk.org.webcompere;


/**
 * Represents a flag which can be set or waited for - once signalled, things waiting for it don't block
 */
public class Signal {
	private boolean signalled = false;
	
	/**
	 * Set the done flag
	 */
	public synchronized void setSignal() {
		signalled = true;
		notifyAll();
	}
	
	/**
	 * Wait up to timeout for the signal
	 * @param timeout timeout in milliseconds
	 * @throws InterruptedException
	 */
	public synchronized void waitForSignal(long timeout) throws InterruptedException {
		if (!signalled) {
			wait(timeout);
		}
	}
	
	/**
	 * Wait indefinitely for the done signal
	 * @throws InterruptedException on thread error
	 */
	public synchronized void waitForSignal() throws InterruptedException {
		if (!signalled) {
			wait();
		}
	}

	/**
	 * Peek at the signal
	 * @return the state of the signal
	 */
	public synchronized boolean isSignalled() {
		return signalled;
	}
}
