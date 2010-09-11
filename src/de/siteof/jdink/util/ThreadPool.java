package de.siteof.jdink.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Simple thread pool implementation</p>
 * @deprecated use task manager instead
 */
@Deprecated
public class ThreadPool {

	protected static class WorkerThread extends Thread {

		private final ThreadPool threadPool;

		private boolean terminating;
		private boolean started;
		private Runnable currentTask;

		private static final Log log	= LogFactory.getLog(ThreadPool.class);


		protected WorkerThread(ThreadPool threadPool) {
			this.threadPool	= threadPool;
		}


		public void run() {
			while(!terminating) {
				Runnable task;
				synchronized(this) {
					task = currentTask;
					if (task == null) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							log.error("wait interrupted - " + e, e);
						}
						task = currentTask;
					}
					currentTask = null;
				}
				if (terminating) {
					break;
				}
				if (task != null) {
					try {
						task.run();
					} finally {
						synchronized(this) {
							threadPool.taskCompleted(this, task);
						}
					}
				}
			}
			log.debug("thread terminated");
			started = false;
		}

		public void startTask(Runnable task) {
			synchronized(this) {
				if (currentTask != null) {
					throw new RuntimeException("thread is not idle");
				}
				currentTask = task;
				if (started) {
					this.notifyAll();
				} else {
					started = true;
					this.start();
				}
			}
		}

		public void terminate() {
			terminating = true;
			this.interrupt();
		}
	}

	private List<WorkerThread> idleThreads = new LinkedList<WorkerThread>();
	private List<WorkerThread> busyThreads = new LinkedList<WorkerThread>();
	private List<Runnable> pendingTasks = new LinkedList<Runnable>();
	private int maxThreadCount;

	public ThreadPool(int maxThreadCount) {
		this.maxThreadCount = maxThreadCount;
	}

	protected WorkerThread getNextThread() {
		if (!idleThreads.isEmpty()) {
			WorkerThread thread = (WorkerThread) idleThreads.get(0);
			idleThreads.remove(0);
			busyThreads.add(thread);
			return thread;
		}
		if (busyThreads.size() < maxThreadCount) {
			WorkerThread thread = new WorkerThread(this);
			busyThreads.add(thread);
			return thread;
		}
		return null;
	}

	protected void taskCompleted(WorkerThread thread, Runnable task) {
		synchronized(this) {
			if (!pendingTasks.isEmpty()) {
				Runnable newTask = (Runnable) pendingTasks.get(0);
				pendingTasks.remove(0);
				thread.startTask(newTask);
			} else {
				busyThreads.remove(thread);
				idleThreads.add(thread);
			}
		}
	}

	public void addTask(Runnable task) {
		synchronized(this) {
			WorkerThread thread = getNextThread();
			if (thread != null) {
				thread.startTask(task);
			} else {
				pendingTasks.add(task);
			}
		}
	}

	public void terminate() {
		synchronized(this) {
			for (WorkerThread workerThread: idleThreads) {
				workerThread.terminate();
			}
			for (WorkerThread workerThread: busyThreads) {
				workerThread.terminate();
			}
		}
	}

}
