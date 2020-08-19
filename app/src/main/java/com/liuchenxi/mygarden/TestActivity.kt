package com.liuchenxi.mygarden

import com.liuchenxi.foundation.base.BaseActivity

interface TestIml<E> : Collection<E>{
    fun geta(a:Int):E
    fun getb(){

    }
}

class TestActivity : BaseActivity() {
   @Deprecated("xxx")
   fun <T> testParent(list:List<T>, threshold: T):List<T> where T:Comparable{
       return listOf();
   }

    public interface Iterable<out T> {
        public operator fun iterator(): Iterator<T>
    }


}