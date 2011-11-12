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
package org.thiesen.starpath.net.command;

import org.thiesen.starpath.StarPathContext;

public class QuitCommand implements Command {

    private static final QuitCommand INSTANCE = new QuitCommand();
    
    private QuitCommand() {
        // Singleton;
    }
    
    @Override
    public String execute( @SuppressWarnings( "unused" ) final StarPathContext context ) {
        return "Bye\n";
    }

    public static Command instance() {
        return INSTANCE;
    }

}
