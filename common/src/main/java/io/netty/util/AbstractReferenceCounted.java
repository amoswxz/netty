/*
 * Copyright 2013 The Netty Project
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
package io.netty.util;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import io.netty.util.internal.ReferenceCountUpdater;

/**
 * Abstract base class for classes wants to implement {@link ReferenceCounted}.
 */
public abstract class AbstractReferenceCounted implements ReferenceCounted {
    private static final long REFCNT_FIELD_OFFSET =
            ReferenceCountUpdater.getUnsafeOffset(AbstractReferenceCounted.class, "refCnt");
    /**
     * AtomicIntegerFieldUpdater:允许类中里面一个字段具有原子性。这个字段必须是volatile类型的，在线程间共享变量时保证其可见性.
     * 字段描述类型是与调用者与操作对象字段的关系一致,可以进行反射进行原子操作.对于父类的字段,子类不能直接操作,只能是实例变量和可修改的变量,不能再被修饰的类前面加 static 和final 的修饰符.
     * 比如refCnt不能加static和final修饰符,如果是包装类Integer和Long的化可以使用AtomicReferenceFieldUpdater类.
     */
    private static final AtomicIntegerFieldUpdater<AbstractReferenceCounted> AIF_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCounted.class, "refCnt");

    private static final ReferenceCountUpdater<AbstractReferenceCounted> updater =
            new ReferenceCountUpdater<AbstractReferenceCounted>() {
                @Override
                protected AtomicIntegerFieldUpdater<AbstractReferenceCounted> updater() {
                    return AIF_UPDATER;
                }

                @Override
                protected long unsafeOffset() {
                    return REFCNT_FIELD_OFFSET;
                }
            };

    // Value might not equal "real" reference count, all access should be via the updater
    @SuppressWarnings("unused")
    private volatile int refCnt = updater.initialValue();

    @Override
    public int refCnt() {
        return updater.refCnt(this);
    }

    /**
     * An unsafe operation intended for use by a subclass that sets the reference count of the buffer directly
     */
    protected final void setRefCnt(int refCnt) {
        updater.setRefCnt(this, refCnt);
    }

    @Override
    public ReferenceCounted retain() {
        return updater.retain(this);
    }

    @Override
    public ReferenceCounted retain(int increment) {
        return updater.retain(this, increment);
    }

    @Override
    public ReferenceCounted touch() {
        return touch(null);
    }

    @Override
    public boolean release() {
        return handleRelease(updater.release(this));
    }

    @Override
    public boolean release(int decrement) {
        return handleRelease(updater.release(this, decrement));
    }

    private boolean handleRelease(boolean result) {
        if (result) {
            deallocate();
        }
        return result;
    }

    /**
     * Called once {@link #refCnt()} is equals 0.
     */
    protected abstract void deallocate();
}
