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

import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;
import javolution.util.FastList;

import org.cliffc.high_scale_lib.NonBlockingHashMapLong;
import org.thiesen.starpath.ConnectionContainer;

public class AStarAlgorithm implements SearchAlgorithm {

    private static final long[] EMPTY_ARRAY = new long[0];

    @Override
    public long[] find( final ConnectionContainer container,
            final long first,
            final long second ) {

        final FastList<PathContainer> paths = new FastList<PathContainer>( 10000 );
        final TLongSet seen = new TLongHashSet( 1024 * 100 );
        
        paths.add( new PathContainer( first ) );

        while ( !paths.isEmpty() ) {
            final PathContainer firstPath = paths.removeFirst();

            final NonBlockingHashMapLong<Boolean> entry = container.getValuesFor( firstPath.getLast() );

            if ( entry == null ) {
                continue;
            }

            if ( entry.containsKey( second ) ) {
                return firstPath.append( second ).asArray();
            }

            if ( firstPath.size() == 1000 ) {
                continue;
            }

            for ( final long value : entry.keySet() ) {
                if ( !seen.contains( value ) ) {
                    seen.add( value );
                    paths.add( firstPath.append( value ) );
                }
            }
        }

        return EMPTY_ARRAY;

    }
  

}
