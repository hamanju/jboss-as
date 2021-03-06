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

package org.jboss.as.deployment.chain;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.jboss.as.deployment.unit.DeploymentUnitContext;
import org.jboss.as.deployment.unit.DeploymentUnitProcessingException;
import org.jboss.as.deployment.unit.DeploymentUnitProcessor;

/**
 * Deployment chain implementation used to execute multiple DeploymentUnitProcessor instances in priority order.
 *
 * @author John E. Bailey
 */
public class DeploymentChainImpl implements DeploymentChain {
    private final Set<OrderedProcessor> orderedProcessors = new ConcurrentSkipListSet<OrderedProcessor>();

    /**
     * Process the deployment unit using the chain of DeploymentUnitProcessor instances.
     *
     * @param context the deployment unit context
     * @throws org.jboss.as.deployment.unit.DeploymentUnitProcessingException
     *          if an error occurs during processing
     */
    public void processDeployment(DeploymentUnitContext context) throws DeploymentUnitProcessingException {
        final Set<OrderedProcessor> processors = this.orderedProcessors;
        for(OrderedProcessor orderedProcessor : processors) {
            orderedProcessor.processor.processDeployment(context);
        }
    }

    @Override
    public void addProcessor(DeploymentUnitProcessor processor, long priority) {
        final Set<OrderedProcessor> processors = this.orderedProcessors;
        if (!processors.add(new OrderedProcessor(processor, priority)))
            throw new IllegalArgumentException("Duplicate processor name and priority: " + processor.getClass().getSimpleName() + ":" + priority);
    }

    @Override
    public void removeProcessor(DeploymentUnitProcessor processor, long priority) {
        orderedProcessors.remove(new OrderedProcessor(processor, priority));
    }

    @Override
    public String toString() {
        return "DeploymentChainImpl{processors=" + orderedProcessors + "}";
    }

    private static final class OrderedProcessor implements Comparable<OrderedProcessor> {
        private final DeploymentUnitProcessor processor;
        private final String name;
        private final long processingOrder;

        private OrderedProcessor(final DeploymentUnitProcessor processor, final long processingOrder) {
            if (processor == null)
                throw new IllegalArgumentException("Processor can not be null");

            this.processor = processor;
            this.name = processor.getClass().getName();
            this.processingOrder = processingOrder;
        }

        @Override
        public int compareTo(final OrderedProcessor other) {
            long diff = this.processingOrder - other.processingOrder;
            return diff != 0L ? (diff < 0L ? -1 : 1) : this.name.compareTo(other.name);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o)
                return true;

            if (!(o instanceof OrderedProcessor))
                return false;

            final OrderedProcessor that = (OrderedProcessor) o;
            return this.processingOrder == that.processingOrder && this.name.equals(that.name);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + (int) (processingOrder ^ (processingOrder >>> 32));
            return result;
        }

        public String toString() {
            return processingOrder + " => " + processor;
        }
    }
}
