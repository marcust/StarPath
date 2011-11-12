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

import java.net.InetAddress;
import java.util.Date;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.thiesen.starpath.StarPathContext;
import org.thiesen.starpath.net.command.Command;
import org.thiesen.starpath.net.command.QuitCommand;

public class StarPathHandler extends SimpleChannelUpstreamHandler {

    @Override
    public void channelConnected(
            @SuppressWarnings( "unused" ) final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        // Send greeting for a new connection.
        e.getChannel().write(
                "Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        e.getChannel().write("It is " + new Date() + " now.\r\n");
    }

    @Override
    public void messageReceived( @SuppressWarnings( "unused" ) final ChannelHandlerContext ctx, final MessageEvent e ) throws Exception {
        final Command message = (Command) e.getMessage();
        
        final String result;
        if ( message == null ) {
            result = "No Command\n";
        } else {
            result = message.execute( StarPathContext.instance() );
        }
        
        final ChannelFuture future = e.getChannel().write( result );
        
        if ( message instanceof QuitCommand ) {
            future.addListener( ChannelFutureListener.CLOSE );
        }
    }

}
