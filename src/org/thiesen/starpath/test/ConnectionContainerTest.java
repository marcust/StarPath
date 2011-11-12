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
import org.thiesen.starpath.ConnectionContainer;

public class ConnectionContainerTest {

    @Test
    public void testDeleteAll() {
        final ConnectionContainer container = new ConnectionContainer();
        
        container.addConnection( 1, 2 );
        
        Assert.assertEquals(  container.remove( 1 ), Boolean.TRUE );
    }

    @Test
    public void testDeleteConnection() {
        final ConnectionContainer container = new ConnectionContainer();
        
        container.addConnection( 1, 2 );
        
        Assert.assertEquals(  container.remove( 1, 2 ), Boolean.TRUE );
    }

}
