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

package org.jboss.as.deployment.item;

import java.io.Serializable;

import org.jboss.msc.service.BatchBuilder;

/**
 * DeploymentItem that executes a ServiceDeployment against a batchBuilder.
 *
 * @author John E. Bailey
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ServiceDeploymentItem implements DeploymentItem, Serializable {

    private static final long serialVersionUID = -8208357864488821428L;

    private final ServiceDeployment serviceDeployment;

    public ServiceDeploymentItem(ServiceDeployment serviceDeployment) {
        this.serviceDeployment = serviceDeployment;
    }

    @Override
    public void install(final DeploymentItemContext context) {
        final BatchBuilder builder = context.getBatchBuilder();
        final ClassLoader currentCl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(serviceDeployment.getClass().getClassLoader());
        try {
            serviceDeployment.install(builder);
        } finally {
            Thread.currentThread().setContextClassLoader(currentCl);
        }
    }
}