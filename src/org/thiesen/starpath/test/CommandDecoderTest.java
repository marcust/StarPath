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

import java.nio.charset.Charset;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.thiesen.starpath.net.CommandDecoder;
import org.thiesen.starpath.net.command.AddCommand;
import org.thiesen.starpath.net.command.Command;
import org.thiesen.starpath.net.command.DeleteCommand;

public class CommandDecoderTest {

    @Test
    public void testAdd() {
        final Command command = CommandDecoder.parse( asByte( "a 1 2" ) );
        
        Assert.assertEquals( command instanceof AddCommand, true );
        Assert.assertEquals( ((AddCommand)command).getFirst(), 1 );
        Assert.assertEquals( ((AddCommand)command).getSecond(), 2 );
    }

    @Test
    public void testDeleteSingle() {
        final Command command = CommandDecoder.parse( asByte( "d 1" ) );
        
        Assert.assertEquals( command instanceof DeleteCommand, true );
        Assert.assertEquals( ((DeleteCommand)command).getFirst(), 1 );
        Assert.assertEquals( ((DeleteCommand)command).getSecond(), -1 );
    }

    @Test
    public void testDeleteMulti() {
        final Command command = CommandDecoder.parse( asByte( "d 1 2" ) );
        
        Assert.assertEquals( command instanceof DeleteCommand, true );
        Assert.assertEquals( ((DeleteCommand)command).getFirst(), 1 );
        Assert.assertEquals( ((DeleteCommand)command).getSecond(), 2 );
    }

    
    private byte[] asByte( final String string ) {
        return string.getBytes( Charset.forName( "US-ASCII" ));
    }
    
}
