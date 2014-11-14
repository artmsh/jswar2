package utils

import java.util.concurrent.{ScheduledThreadPoolExecutor, ThreadPoolExecutor}
import java.util.concurrent.locks.{Condition, ReentrantLock}

class PausableThreadPoolExecutor extends ScheduledThreadPoolExecutor(1) {
  var isPaused: Boolean = false
  val pauseLock: ReentrantLock = new ReentrantLock()
  val unpaused: Condition = pauseLock.newCondition()

  override def beforeExecute(t: Thread, r: Runnable) {
    super.beforeExecute(t, r)
    pauseLock.lock()
    try {
      while (isPaused) unpaused.await()
    } catch {
      case e: InterruptedException => t.interrupt()
    } finally {
      pauseLock.unlock()
    }
  }

  def pause() {
    pauseLock.lock()
    try {
      isPaused = true
    } finally {
      pauseLock.unlock()
    }
  }

  def resume() {
    pauseLock.lock()
    try {
      isPaused = false
      unpaused.signalAll();
    } finally {
      pauseLock.unlock()
    }
  }
}