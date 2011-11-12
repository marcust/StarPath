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


public class PathContainer {

    private final long _value;
    private final long _size;
    private final PathContainer _prev;
    
    public PathContainer( final long value ) {
        _value = value;
        _size = 1;
        _prev = null;
    }

    private PathContainer( final PathContainer firstPath, final long value ) {
        _value = value;
        _size = firstPath.size() + 1;
        _prev = firstPath;
    }

    public long getLast() {
        return _value;
    }

    public long size() {
        return _size;
    }

    public PathContainer append( final long i ) {
        return new PathContainer( this, i );
    }

    public long[] asArray() {
        final long[] values = new long[ (int)this.size() ];
        recursiveToArray( this, values, (int) ( size() - 1 ) );
        return values;
    }

    private void recursiveToArray( final PathContainer pathContainer, final long[] values, final int index ) {
        if ( pathContainer._prev != null ) {
            recursiveToArray( pathContainer._prev, values, index - 1 );
        }
        
        values[index] = pathContainer._value;
    }

    @Override
    public String toString() {
        final StringBuilder retvalBuilder = new StringBuilder("(");
        final long[] iterator = asArray();
        for ( int i = 0; i < iterator.length; i++ ) {
            retvalBuilder.append( iterator[i] );
            
            if ( i + 1 < iterator.length ) {
                retvalBuilder.append( ", " );
            }
            
        }
        
        return retvalBuilder.append(')').toString();
        
        
    }
    
    
}
