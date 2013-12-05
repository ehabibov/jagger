/*
 * Copyright (c) 2010-2012 Grid Dynamics Consulting Services, Inc, All Rights Reserved
 * http://www.griddynamics.com
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.griddynamics.jagger.coordinator.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Alexey Kiselyov
 *         Date: 28.07.11
 */
public class MemCoordinatorNode implements CoordinatorNode {
    private static final Logger log = LoggerFactory.getLogger(MemCoordinatorNode.class);

    private CoordinatorNode parent;

    private CoordinatorStorage coordinatorStorage;
    private final String path;

    private WatchableSet<CoordinatorNode> children;

    public MemCoordinatorNode(CoordinatorStorage coordinatorStorage, String path) {
        this.path = path;
        this.coordinatorStorage = coordinatorStorage;
    }

    @Override
    public CoordinatorNode getParent() {
        return this.parent;
    }

    @Override
    public Set<CoordinatorNode> children() {
        return this.children;
    }

    @Override
    public CoordinatorNode child(String path) {
        return new MemCoordinatorNode(coordinatorStorage, this.path + "/" + path);
    }


}