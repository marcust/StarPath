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
import org.testng.annotations.Test;
import org.thiesen.starpath.search.PathContainer;

public class PathContainerTest {

    @Test
    public void testContainer() {
        PathContainer pc = new PathContainer( 1 );
        pc = pc.append( 2 ); 
        pc = pc.append( 3 );
        pc = pc.append( 4 );
        pc = pc.append( 5 );
        
        Assert.assertEquals( pc.size(), 5, "Should have size of five" );
        final long[] iterator = pc.asArray();
        
        for ( int i = 1; i <= 5; i++ ) {
            Assert.assertEquals( iterator[i - 1], i, "Should have value of " + 1 );    
        }

        
            
    }
    
    
    
}
