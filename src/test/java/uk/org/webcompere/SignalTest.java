package uk.org.webcompere;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignalTest {
	private Signal signal;
	private static final int TIMEOUT = 1000;
	private static final int SHORT_TIME = 20;
	
	@Before
	public void before() {
		signal = new Signal();
	}
	
	@Test
	public void signalIsNotSet() {
		assertFalse(signal.isSignalled());
	}
	
	@Test
	public void signalIsSet() {
		signal.setSignal();
		assertTrue(signal.isSignalled());
	}
	
	@Test(timeout=TIMEOUT)
	public void whenWaitingForAlreadySetSignalNoBlock() throws InterruptedException {
		signal.setSignal();
		
		// this should not wait, but should pass straight through
		signal.waitForSignal();
	}
	
	@Test(timeout=TIMEOUT)
	public void timedOutWaitForUnsetSignal() throws InterruptedException {
		signal.waitForSignal(SHORT_TIME);
		
		// should reach here after a short time and not fail the test because of a timeout
	}
	
	@Test(timeout=TIMEOUT)
	public void interThreadSignalling() throws InterruptedException {
		final Signal threadStarted = new Signal();
		final Signal threadAllowedToProceed = new Signal();
		final Signal threadFinished = new Signal();
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				// set that we've started
				threadStarted.setSignal();
				
				// wait until we're allowed to proceed
				try {
					threadAllowedToProceed.waitForSignal();
				} catch (InterruptedException e) {
					// do nothing here
				}
				
				// signal we've finished
				threadFinished.setSignal();
				
			}
			
		});
		
		thread.start();
		
		// wait for the thread to have started
		threadStarted.waitForSignal();
		
		// thread should not have finished - so wait to see if it has and check it hasn't
		threadFinished.waitForSignal(SHORT_TIME);
		assertFalse(threadFinished.isSignalled());
		
		// then signal the thread to finish
		threadAllowedToProceed.setSignal();
		
		// then wait indefinitely for the thread to say it's finished
		threadFinished.waitForSignal();
		
		// reaching here before the overall timeout is proof that the signalling works between threads
	}
}