/*
 * Copyright (c) 2013, 2014 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.jruby.truffle.nodes.objects;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.source.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;
import org.jruby.truffle.nodes.*;
import org.jruby.truffle.runtime.*;
import org.jruby.truffle.runtime.core.*;

public class ReadClassVariableNode extends RubyNode {

    private final String name;
    private final LexicalScope lexicalScope;

    public ReadClassVariableNode(RubyContext context, SourceSection sourceSection, String name, LexicalScope lexicalScope) {
        super(context, sourceSection);
        this.name = name;
        this.lexicalScope = lexicalScope;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        notDesignedForCompilation();

        final RubyModule moduleObject = lexicalScope.getLiveModule();

        final Object value = ModuleOperations.lookupClassVariable(moduleObject, name);

        if (value == null) {
            // TODO(CS): is this right?
            // TODO: NameError!
            return getContext().getCoreLibrary().getNilObject();
        }

        return value;
    }

    @Override
    public Object isDefined(VirtualFrame frame) {
        final RubyModule moduleObject = lexicalScope.getLiveModule();

        final Object value = ModuleOperations.lookupClassVariable(moduleObject, name);

        if (value == null) {
            return getContext().getCoreLibrary().getNilObject();
        } else {
            return getContext().makeString("class variable");
        }
    }

}
