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

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.thiesen.starpath.net.CommandDecoder;
import org.thiesen.starpath.net.StarPathHandler;


public class Main {

    private final static class StarPathPipelineFactory implements ChannelPipelineFactory {

        @Override
        public ChannelPipeline getPipeline() throws Exception {
            final ChannelPipeline pipeline = new DefaultChannelPipeline();

            
            pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
                    8192, Delimiters.lineDelimiter()));
            pipeline.addLast("decoder", new CommandDecoder());
            pipeline.addLast("encoder", new StringEncoder());

            pipeline.addLast("handler", new StarPathHandler());

            return pipeline;
        }

    }

    public static void main( final String... args ) {
        final ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new StarPathPipelineFactory());

        bootstrap.bind( new InetSocketAddress( 19582 ) );
    }


}
