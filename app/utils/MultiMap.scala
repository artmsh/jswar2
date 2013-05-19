package utils

import scala.collection.immutable._

/**
 * A multimap for immutable member sets (the Scala libraries only have one for mutable sets).
 *
 * taken from http://www.monomorphic.org/wordpress/an-immutable-multimap-for-scala/
 */
class MultiMap[A, B](val myMap: Map[A, Set[B]]) {

  def this() = this(new HashMap[A, Set[B]])

  def +(kv: Tuple2[A, B]): MultiMap[A, B] = {
    val set = if (myMap.contains(kv._1)) {
      myMap(kv._1) + kv._2
    } else {
      new HashSet() + kv._2
    }

    new MultiMap[A, B](myMap + ((kv._1, set)))
  }

  def -(key: A): MultiMap[A, B] = {
    new MultiMap[A, B](myMap - key)
  }

  def -(kv: Tuple2[A, B]): MultiMap[A, B] = {
    if (!myMap.contains(kv._1)) {
      throw new Exception("No such key")
    }
    val set = myMap(kv._1) - kv._2
    if (set.isEmpty) {
      new MultiMap[A, B](myMap - kv._1)
    } else {
      new MultiMap[A, B](myMap + ((kv._1, set)))
    }
  }

  def entryExists(kv: Tuple2[A, B]): Boolean = {
    if (!myMap.contains(kv._1)) {
      false
    } else {
      myMap(kv._1).contains(kv._2)
    }
  }

  def size = myMap.foldLeft(0)(_ + _._2.size)

  def keys = myMap.keys

  def values = myMap.values

  def getOrElse(key: A, elval: Set[B]): Set[B] = {
    myMap.getOrElse(key, elval)
  }

  def apply(key: A) = myMap(key)
}