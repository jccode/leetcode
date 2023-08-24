package com.github.jccode.demo

import java.util.concurrent.{BlockingQueue, ConcurrentHashMap, ConcurrentMap, Executors, Future, LinkedBlockingQueue, TimeUnit}
import scala.collection.mutable

/*
 将请求在内存中进行合并，将进行批量提交demo.
 */

case class UserRequest(userId: String, orderId: String, amount: Int)
case class Result(success: Boolean, msg: String)

/**
 * v1版本：两个线程之间wait/notify使用的是请求线程的request对象；
 * 但结果数据的同步，就只能引入ConcurrentHashMap来完成数据的交换
 */
object MergeRequestDemo {

  val queue: BlockingQueue[UserRequest] = new LinkedBlockingQueue()
  val resultMap: ConcurrentHashMap[UserRequest, Result] = new ConcurrentHashMap()
  var stock = 6

  def main(args: Array[String]): Unit = {
    mergeJob()
    // 创建10个用户请求
    val n = 10;
    val userRequests: mutable.ArrayBuffer[UserRequest] = mutable.ArrayBuffer()
    for (i <- 1 to n) {
      userRequests += UserRequest(s"uid:$i", s"oid:$i", 1)
    }

    println(s"-- 接收到客户端 $n 个请求 --")

    val executors = Executors.newCachedThreadPool()
    val resultFutureMap: mutable.Map[UserRequest, Future[Result]] = mutable.Map()
    userRequests.foreach { req =>
      val resultFuture = executors.submit(() => {
        operate(req)
      })
      resultFutureMap += (req -> resultFuture)
    }

    Thread.sleep(1000)
    println(s"-- 进行响应 --")

    resultFutureMap.foreach { case (req, future) =>
      val result = future.get(100, TimeUnit.MILLISECONDS)
      println(s"[rsp] ${req.userId}: $result")
    }

    executors.shutdownNow()
  }

  def operate(request: UserRequest): Result = {
    println(s"[req] ${request.userId}: $request")
    // 入队列
    queue.offer(request)
    // 线程挂起，阻塞，直到结果可用
    var result: Result = null
    request.synchronized {
      try {
        request.wait(500)
        // 等待合并队列响应,并唤醒该线程,返回结果
        result = resultMap.remove(request)
        if (result == null) {
          result = Result(false, "等待超时")
        }
      } catch {
        case _: InterruptedException =>
          Result(false, "被中断")
      }
    }
    result
  }

  def mergeJob(): Unit = {

    new Thread(() => {
      println("-- 启动合并线程 --")
      val batchSize = 3;
      while (true) {
        val requestBatch: mutable.ArrayBuffer[UserRequest] = mutable.ArrayBuffer()
        for (i <- 1 to batchSize) {
          requestBatch += queue.take()
        }
        val updateResult = batchUpdate(requestBatch)
        // 针对每个请求返回结果，并唤醒对应的进程
        requestBatch.foreach {req =>
          req.synchronized {
            // wait/notify 两个异步线程之间要交换数据，怎么交换？只能像queue一样，通过一块内存区域进行交换。因此这里使用hashMap;
            // 另外，因为queue中已经存了数据了，其实也可以将req,result放在同一个对象，然后放在queue里，这样来交换。感觉更好. (见v2版本)
            resultMap.put(req, Result(updateResult, req.userId))
            req.notify()
          }
        }
      }
    }, "mergeThread").start();
  }

  def batchUpdate(request: mutable.Seq[UserRequest]): Boolean = {
    println(s"[exec batch] ${request.map(_.userId)}")
    if (stock >= request.size) {
      stock = stock - request.size
      true
    } else {
      false
    }
  }
}



class UserPromise(val request: UserRequest, var result: Result = null) {
  def setResult(result: Result): Unit = {
    this.result = result
  }
}
object UserPromise {
  def apply(request: UserRequest): UserPromise = new UserPromise(request)
}

/**
 * V2版本，引入Promise对象，其实就是把request/result放在一起
 * 这样放到queue中就是Promise对象。
 * 线程之间的wait/notify也可以通过该对象的互斥锁来进行。
 * 而数据的交换可以直接在通过为该对象赋值，这其实一样是通过共享内存来实现，而且该对象存在queue上。
 */
object MergeRequestDemoV2 {
  val queue: BlockingQueue[UserPromise] = new LinkedBlockingQueue()
  var stock = 6

  def main(args: Array[String]): Unit = {
    mergeJob()
    // 创建10个用户请求
    val n = 10;
    val userRequests: mutable.ArrayBuffer[UserRequest] = mutable.ArrayBuffer()
    for (i <- 1 to n) {
      userRequests += UserRequest(s"uid:$i", s"oid:$i", 1)
    }

    println(s"-- 接收到客户端 $n 个请求 --")

    val executors = Executors.newCachedThreadPool()
    val resultFutureMap: mutable.Map[UserRequest, Future[Result]] = mutable.Map()
    userRequests.foreach { req =>
      val resultFuture = executors.submit(() => {
        operate(req)
      })
      resultFutureMap += (req -> resultFuture)
    }

    Thread.sleep(1000)
    println(s"-- 进行响应 --")

    resultFutureMap.foreach { case (req, future) =>
      val result = future.get(100, TimeUnit.MILLISECONDS)
      println(s"[rsp] ${req.userId}: $result")
    }

    executors.shutdownNow()
  }

  def operate(request: UserRequest): Result = {
    println(s"[req] ${request.userId}: $request")
    val promise = UserPromise(request)
    // 入队列
    queue.offer(promise)
    // 线程挂起，阻塞，直到结果可用
    var result: Result = null
    promise.synchronized {
      try {
        promise.wait(500)
        // 等待合并队列响应,并唤醒该线程,返回结果
        result = promise.result
        if (result == null) {
          result = Result(false, "等待超时")
        }
      } catch {
        case _: InterruptedException =>
          Result(false, "被中断")
      }
    }
    result
  }

  def mergeJob(): Unit = {
    new Thread(() => {
      println("-- 启动合并线程 --")
      val batchSize = 3;
      while (true) {
        val promiseBatch: mutable.ArrayBuffer[UserPromise] = mutable.ArrayBuffer()
        for (i <- 1 to batchSize) {
          promiseBatch += queue.take()
        }
        val updateResult = batchUpdate(promiseBatch)
        // 针对每个请求返回结果，并唤醒对应的进程
        promiseBatch.foreach {promise =>
          promise.synchronized {
            // 因为queue中已经存了数据了，其实也可以将promise,result放在同一个对象，然后放在queue里，这样来交换。感觉更好.
            promise.setResult(Result(updateResult, promise.request.userId))
            promise.notify()
          }
        }
      }
    }, "mergeThread").start();
  }

  def batchUpdate(request: mutable.Seq[UserPromise]): Boolean = {
    println(s"[exec batch] ${request.map(_.request.userId)}")
    if (stock >= request.size) {
      stock = stock - request.size
      true
    } else {
      false
    }
  }
}
