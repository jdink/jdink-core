package de.siteof.jdink.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggingReentrantReadWriteLock extends ReentrantReadWriteLock {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(LoggingReentrantReadWriteLock.class);

	@Override
	public Thread getOwner() {
		return super.getOwner();
	}

	public void lock(Lock lock) {
		boolean lockAquired = false;
		int index = 0;
		while (!lockAquired) {
			try {
				lockAquired = lock.tryLock(1000, TimeUnit.SECONDS);
				if (!lockAquired) {
					log.info("[lock] waiting for lock, index=" + index + ", lock=" + lock);
					index++;
					if (index >= 1) {
						if ((lock == this.writeLock()) || (lock == this.readLock())) {
							Thread owner = this.getOwner();
//							log.info("[lock] waiting for lock - " + lock, new Exception());
							if (owner != null) {
								StackTraceElement[] stackTrace = owner.getStackTrace();
								int count = Math.min(3, stackTrace.length);
								for (int i = 0; i < count; i++) {
									log.info("[lock] waiting for lock - owner stack trace: " +
											stackTrace[i]);
								}
							}							
							StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
							int count = Math.min(3, stackTrace.length);
							for (int i = 0; i < count; i++) {
								log.info("[lock] waiting for lock - this stack trace: " +
										stackTrace[i]);
							}
						}
					}
				}
			} catch (InterruptedException e) {
				log.info("[lock] interrupted");
			}
		}
	}

}
