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
package org.thiesen.starpath.test;

import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.thiesen.starpath.ConnectionContainer;
import org.thiesen.starpath.search.AStarAlgorithm;
import org.thiesen.starpath.search.ParallelAStarAlgorithm;
import org.thiesen.starpath.search.SearchAlgorithm;

public class AlgorithmTestFactory {

    @Factory
    public Object[] createAlgorithmTests() {
        return new Object[] { new AlgorithmTest( new AStarAlgorithm() ), new AlgorithmTest( new ParallelAStarAlgorithm() ) };
    }
    

    public static class AlgorithmTest {

        private final SearchAlgorithm _algorithm;

        public AlgorithmTest( final SearchAlgorithm algorithm ) {
            _algorithm = algorithm;
        }

        @Test
        public void testPath() {
            final ConnectionContainer container = new ConnectionContainer();
            container.addConnection( 1, 2 );
            container.addConnection( 2, 3 );
            container.addConnection( 3, 4 );
            container.addConnection( 4, 5 );

            final long[] path = _algorithm.find( container, 1, 5 );

            Assert.assertEquals( path, new long[] { 1, 2, 3, 4, 5 } );
        }

        @Test
        public void testNoPath() {
            final ConnectionContainer container = new ConnectionContainer();
            container.addConnection( 1, 2 );
            container.addConnection( 2, 3 );
            container.addConnection( 4, 5 );

            final long[] path = _algorithm.find( container, 1, 5 );

            Assert.assertEquals( path, new long[0] );
        }
        
    }



}
