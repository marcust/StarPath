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
package org.thiesen.starpath.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class Generator {

    public static void main( final String... args ) throws FileNotFoundException {
        final int NUM_USERS = 100000;
        final int MAX_NUM_CONNECTIONS = 100;
        final Random r = new Random();
        final PrintWriter writer = new PrintWriter( new File("/tmp/random.txt") );

        for ( int i = 0; i < NUM_USERS; i++ ) {
            final int numConnections = r.nextInt( MAX_NUM_CONNECTIONS );

            for ( int j = 0; j < numConnections; j++ ) {
                final int connectionId = r.nextInt( NUM_USERS );
                writer.print( "a " );
                writer.print( i );
                writer.print( ' ' );
                writer.print( connectionId );
                writer.print( '\n' );
            }


        }


        writer.close();

    }

}
