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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.cliffc.high_scale_lib.NonBlockingHashMapLong;
import org.thiesen.starpath.ConnectionContainer;

public class ParallelAStarAlgorithm implements SearchAlgorithm {

    private static class FindTask implements Runnable {

        private final PathContainer _pathContainer;
        private final ConnectionContainer _container;
        private final long _first;
        private final long _second;
        private final NonBlockingHashMapLong<Boolean> _seen;
        private final BlockingQueue<long[]> _resultQueue;
        private final ZeroAwaitingLatch _lock;

        public FindTask( final BlockingQueue<long[]> resultQueue, final PathContainer pathContainer, final ConnectionContainer container, final long first, final long second, final NonBlockingHashMapLong<Boolean> seen, final ZeroAwaitingLatch locker ) {
            _resultQueue = resultQueue;
            _pathContainer = pathContainer;
            _container = container;
            _first = first;
            _second = second;
            _seen = seen;
            _lock = locker;
        }

        @Override
        public void run() {
            try {
                final NonBlockingHashMapLong<Boolean> entry = _container.getValuesFor( _pathContainer.getLast() );
                if ( _resultQueue.size() != 0 ) {
                    return;
                }

                if ( entry == null ) {
                    return;
                }

                if ( entry.containsKey( _second ) ) {
                    _resultQueue.add( _pathContainer.append( _second ).asArray() );
                    return;
                }

                if ( _pathContainer.size() == 1000 ) {
                    return;
                }

                for ( final long value : entry.keySet() ) {
                    if ( _seen.putIfAbsent( value, Boolean.TRUE ) == null ) {
                        _lock.createOne();
                        EXECUTOR.submit( new FindTask( _resultQueue, _pathContainer.append( value ), _container, _first, _second, _seen, _lock ) );
                    }
                }

            } finally {
                _lock.destroyOne();
            }
        }
    }

    private static final long[] EMPTY_ARRAY = new long[0];

    private final static ExecutorService EXECUTOR = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors(), new ThreadFactory() {

        @Override
        public Thread newThread( final Runnable r ) {
            final Thread t = new Thread( r );
            t.setDaemon( true );
            return t;
        }
    } );

    @Override
    public long[] find( final ConnectionContainer container,
            final long first,
            final long second ) {

        final NonBlockingHashMapLong<Boolean> seen = new NonBlockingHashMapLong<Boolean>();
        final BlockingQueue<long[]> resultQueue = new SynchronousQueue<long[]>();
        final ZeroAwaitingLatch locker = new ZeroAwaitingLatch();

        EXECUTOR.submit( new FindTask( resultQueue, new PathContainer( first ), container, first, second, seen, locker ) );

        final AtomicBoolean resultFound = new AtomicBoolean();

        final Thread noResultThread = new Thread( new Runnable() {

            @Override
            public void run() {
                try {
                    while ( !locker.awaitEmpty( 1, TimeUnit.SECONDS ) ) {
                        if ( resultFound.get() ) {
                            return;
                        }
                    }
                    resultQueue.offer( EMPTY_ARRAY );
                } catch ( final InterruptedException e ) {
                    Thread.interrupted();
                }
            } 
        }
        );
        noResultThread.start();

        try {
            final long[] result = resultQueue.poll( 1, TimeUnit.DAYS );
            resultFound.set( true );
            return result;

        } catch ( final InterruptedException e ) {
            Thread.interrupted();
            return EMPTY_ARRAY;
        }  

    }


}
