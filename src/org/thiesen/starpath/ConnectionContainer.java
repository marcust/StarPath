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
package org.thiesen.starpath;

import org.cliffc.high_scale_lib.NonBlockingHashMapLong;

public class ConnectionContainer {

    private final NonBlockingHashMapLong<NonBlockingHashMapLong<Boolean>> _map = new NonBlockingHashMapLong<NonBlockingHashMapLong<Boolean>>();
    
    public Boolean addConnection( final long first, final long second ) {
        NonBlockingHashMapLong<Boolean> map = _map.putIfAbsent( first, new NonBlockingHashMapLong<Boolean>() );
        
        if ( map == null ) {
            map = _map.get( first );
        }
        
        return Boolean.valueOf( map.put( second, Boolean.TRUE ) == null );
    }

    public Boolean clear() {
        _map.clear();
        return Boolean.TRUE;
    }

    public Boolean remove( final long first ) {
        return Boolean.valueOf( _map.remove( first ) != null );
    }

    public Boolean remove( final long first, final long second ) {
        final NonBlockingHashMapLong<Boolean> entries = _map.get( first );
        if ( entries == null ) {
            return Boolean.FALSE;
        }
        return entries.remove( second );
    }

    public NonBlockingHashMapLong<Boolean> getValuesFor( final long id ) {
        return _map.get( id );
    }
    
    
}
