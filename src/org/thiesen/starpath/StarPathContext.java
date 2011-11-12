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

import java.util.Iterator;

import org.cliffc.high_scale_lib.NonBlockingHashMapLong;
import org.thiesen.starpath.net.command.AddCommand;
import org.thiesen.starpath.net.command.ClearCommand;
import org.thiesen.starpath.net.command.DeleteCommand;
import org.thiesen.starpath.net.command.FindCommand;
import org.thiesen.starpath.net.command.PrintCommand;
import org.thiesen.starpath.search.AStarAlgorithm;
import org.thiesen.starpath.search.SearchAlgorithm;

public class StarPathContext {
    
    private final static StarPathContext INSTANCE = new StarPathContext();
    private final ConnectionContainer _container = new ConnectionContainer();
    private final SearchAlgorithm _algorithm = new AStarAlgorithm();
    
    private StarPathContext() {
        // singleton;
    }
    
    public static StarPathContext instance() {
        return INSTANCE;
    }
    
    public String execute( final AddCommand addCommand ) {
        return _container.addConnection( addCommand.getFirst(), addCommand.getSecond() ) + "\n";
    }

    public String execute( @SuppressWarnings( "unused" ) final ClearCommand clearCommand ) {
        return _container.clear() + "\n";
    }

    public String execute( final FindCommand findCommand ) {
         final long[] path = _algorithm.find( _container, findCommand.getFirst(), findCommand.getSecond() );
         final StringBuilder retvalBuilder = new StringBuilder( 1000 );
         for ( int i = 0; i < path.length; i++ ) {
             retvalBuilder.append( path[i] );
             if ( i + 1 < path.length ) {
                 retvalBuilder.append( ' ' );
             }
         }
         retvalBuilder.append('\n');
         return retvalBuilder.toString();
    }

    public String execute( final DeleteCommand deleteCommand ) {
        if ( deleteCommand.isSingle() ) {
            return _container.remove( deleteCommand.getFirst() ) + "\n";
        } 
        
        return _container.remove( deleteCommand.getFirst(), deleteCommand.getSecond() ) + "\n";
    }

    public String execute( final PrintCommand printCommand ) {
        final NonBlockingHashMapLong<Boolean> values = _container.getValuesFor( printCommand.getValue() );
        if ( values == null ) {
            return "\n";
        }
        final Iterator<Long> iterator = values.keySet().iterator();
        final StringBuilder retvalBuilder = new StringBuilder( 1000 );
        while ( iterator.hasNext() ) {
            retvalBuilder.append( iterator.next() );
            
            if ( iterator.hasNext() ) {
                retvalBuilder.append(", ");
            }
        }

        return retvalBuilder.append('\n').toString();
        
        
    }


}
