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
package org.thiesen.starpath.net;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.thiesen.starpath.net.command.AddCommand;
import org.thiesen.starpath.net.command.ClearCommand;
import org.thiesen.starpath.net.command.Command;
import org.thiesen.starpath.net.command.DeleteCommand;
import org.thiesen.starpath.net.command.FindCommand;
import org.thiesen.starpath.net.command.PrintCommand;
import org.thiesen.starpath.net.command.QuitCommand;

public class CommandDecoder extends OneToOneDecoder {

    @SuppressWarnings( "unused" )
    @Override
    protected Object decode( final ChannelHandlerContext ctx, final Channel channel, final Object msg ) throws Exception {
        if (!(msg instanceof ChannelBuffer)) {
            return msg;
        }
        
        final ChannelBuffer buffer = (ChannelBuffer) msg;
        
        return parse( buffer.array() );
    }

    public static Command parse( final byte[] array ) {
        if ( array.length == 0 ) {
            return null;
        }
        
        final char commandChar = (char)array[0];
        
        switch ( commandChar ) {
            case 'a': return parseAdd( array );
            case 'd': return parseDelete( array );
            case 'f': return parseFind( array );
            case 'c': return parseClear();
            case 'q': return parseQuit();
            case 'p': return parsePrint( array );
        }
        
        throw new IllegalArgumentException("No such command: " + commandChar );
        
    }

    private static Command parsePrint(final byte[] array) {
        return PrintCommand.from( parseFirstTwoLongs( array )[0] );
    }

    private static Command parseQuit() {
        return QuitCommand.instance();
    }

    private static AddCommand parseAdd( final byte[] array ) {
        final long[] firstTwoLongs = parseFirstTwoLongs( array );
        return AddCommand.create( firstTwoLongs[0], firstTwoLongs[1] );
        
    }

    private static DeleteCommand parseDelete( final byte[] array ) {
        final StringBuilder builder = new StringBuilder();

        int i;
        for ( i = 2; i < array.length && array[i] != ' '; i++ ) {
            builder.append( (char)array[i] );
        }
        final long first = Long.parseLong( builder.toString(), 10 );
        if ( i == array.length ) {
            return DeleteCommand.forSingle( first );
        }
        
        builder.setLength( 0 );
        for ( i++; i < array.length && array[i] != ' '; i++ ) {
            builder.append( (char)array[i] );
        }
        final long second = Long.parseLong( builder.toString(), 10 );
        
        return DeleteCommand.forConnection( first, second );
        
    }

    private static FindCommand parseFind( final byte[] array ) {
        final long[] firstTwoLongs = parseFirstTwoLongs( array );
        return FindCommand.create(  firstTwoLongs[0], firstTwoLongs[1] );
    }

    private static ClearCommand parseClear() {
        return ClearCommand.instance();
    }

    private static long[] parseFirstTwoLongs( final byte[] array ) {
        final StringBuilder builder = new StringBuilder();
        int i;
        for ( i = 2; i < array.length && array[i] != ' ' ; i++ ) {
            builder.append( (char)array[i] );
        }
        final long first = Long.parseLong( builder.toString(), 10 );
        builder.setLength( 0 );
        for ( i++; i < array.length && array[i] != ' '; i++ ) {
            builder.append( (char)array[i] );
        }
        if ( builder.length() == 0 ) {
            return new long[] { first };
        }
        
        final long second = Long.parseLong( builder.toString(), 10 );
        
        return new long[] { first, second };
    }
    
}
