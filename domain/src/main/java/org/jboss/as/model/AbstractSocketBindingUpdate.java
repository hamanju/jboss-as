/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.model;

import org.jboss.as.model.socket.SocketBindingGroupElement;

/**
 * The abstract socket binding update.
 *
 * @author Emanuel Muckenhuber
 */
public abstract class AbstractSocketBindingUpdate extends AbstractModelElementUpdate<SocketBindingGroupElement> {

    private static final long serialVersionUID = 8177705582229479376L;

    protected AbstractSocketBindingUpdate() {
        //
    }

    /** {@inheritDoc} */
    public abstract AbstractSocketBindingUpdate getCompensatingUpdate(SocketBindingGroupElement original);

    /** {@inheritDoc} */
    public Class<SocketBindingGroupElement> getModelElementType() {
        return SocketBindingGroupElement.class;
    }

    protected AbstractServerModelUpdate<Void> getServerModelUpdate() {
        return new ServerSocketBindingUpdate(this);
    }

    /**
     * Apply update when run within a {@link ServerSocketBindingUpdate}.
     *
     * @param <P> the param type
     * @param updateContext the update context
     * @param resultHandler the update result handler
     * @param param the param
     */
    protected abstract <P> void applyUpdate(UpdateContext updateContext, UpdateResultHandler<? super Void, P> resultHandler, P param);

}
