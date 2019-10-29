/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.netty.util.internal;

import io.netty.util.concurrent.FastThreadLocal;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The internal data structure that stores the thread-local variables for Netty and all {@link FastThreadLocal}s.
 * Note that this class is for internal use only and is subject to change at any time.  Use {@link FastThreadLocal}
 * unless you know what you are doing.
 */
class UnpaddedInternalThreadLocalMap {
    /**
     * 如果是普通的Thread使用FastThreadLocal,还是采用ThreadLocal存储资源
     */
    static final ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = new ThreadLocal<InternalThreadLocalMap>();
    static final AtomicInteger nextIndex = new AtomicInteger();

    /** Used by {@link FastThreadLocal} */
    /**
     * FastThreadLocal的资源存放地址，ThreadLocal中是通过ThreadLocalMap存放资源，索引是ThreadLocal对象的threadLocalHashCode进行hash得到
     * FastThreadLocal使用Object[]数组，使用通过nextIndex自增得到的数值作为索引，保证每次查询数值都是O(1)操作
     * 需要注意，FastThreadLocal对象为了避免伪共享带来的性能损耗，使用padding使得FastThreadLocal的对象大小超过128byte
     * 避免伪共享的情况下，indexedVariables的多个连续数值在不更新的前提下可以被缓存至cpu chache line中，这样大大的提高了查询效率
     */
    //另外Object[0]存储一个Set<FastThreadLocal<?>>集合。
    Object[] indexedVariables;

    // Core thread-locals
    int futureListenerStackDepth;
    int localChannelReaderStackDepth;
    Map<Class<?>, Boolean> handlerSharableCache;
    IntegerHolder counterHashCode;
    ThreadLocalRandom random;
    Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache;
    Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache;

    // String-related thread-locals
    StringBuilder stringBuilder;
    Map<Charset, CharsetEncoder> charsetEncoderCache;
    Map<Charset, CharsetDecoder> charsetDecoderCache;

    // ArrayList-related thread-locals
    ArrayList<Object> arrayList;

    UnpaddedInternalThreadLocalMap(Object[] indexedVariables) {
        this.indexedVariables = indexedVariables;
    }
}
