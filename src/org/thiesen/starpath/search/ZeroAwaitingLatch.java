/*
 * (c) Copyright 2011 Marcus Thiesen (marcus@thiesen.org)
 *
 *  This file is part of StarPath.
 *
 *  StarPath is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  StarPath is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with StarPath.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.thiesen.starpath.search;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ZeroAwaitingLatch {

    private final AtomicLong _counter = new AtomicLong( 1 );
    private final AtomicBoolean _reachedZero = new AtomicBoolean();
    private final Object _notifier = new Object();
    
    public void createOne() {
        _counter.incrementAndGet();
    }

    public void destroyOne() {
        if ( _counter.decrementAndGet() == 0 ) {
            signalZeroReached();
        }
    }

    private void signalZeroReached() {
        _reachedZero.set( true );
        synchronized ( _notifier ) {
            _notifier.notify();
        }
    }

    public boolean awaitEmpty( final int timeout, final TimeUnit unit ) throws InterruptedException {
        if ( _reachedZero.get() ) {
            return true;
        }
        synchronized ( _notifier ) {
            _notifier.wait( unit.toMillis( timeout ) );
        }
        return _reachedZero.get();
    }

}
