/*
 * Copyright (c) 2008-2012, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.collection.multimap.tx;

import com.hazelcast.collection.CollectionProxyId;
import com.hazelcast.collection.CollectionRecord;
import com.hazelcast.collection.CollectionService;
import com.hazelcast.core.TransactionalMultiMap;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.transaction.Transaction;
import com.hazelcast.transaction.TransactionException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @ali 3/29/13
 */
public class TransactionalMultiMapProxy<K,V> extends TransactionalMultiMapProxySupport implements TransactionalMultiMap<K, V> {




    public TransactionalMultiMapProxy(NodeEngine nodeEngine, CollectionService service, CollectionProxyId proxyId, Transaction tx) {
        super(nodeEngine, service, proxyId, tx);
    }

    public boolean put(K key, V value) throws TransactionException {
        Data dataKey = getNodeEngine().toData(key);
        Data dataValue = getNodeEngine().toData(value);
        return putInternal(dataKey, dataValue);
    }

    public Collection<V> get(K key) {
        Data dataKey = getNodeEngine().toData(key);
        Collection<CollectionRecord> coll = getInternal(dataKey);
        Collection<V> collection = new ArrayList<V>(coll.size());
        for (CollectionRecord record: coll){
            collection.add((V)getNodeEngine().toObject(record.getObject()));
        }
        return collection;
    }

    public boolean remove(Object key, Object value) {
        Data dataKey = getNodeEngine().toData(key);
        Data dataValue = getNodeEngine().toData(value);
        return removeInternal(dataKey, dataValue);
    }

    public Collection<V> remove(Object key) {
        Data dataKey = getNodeEngine().toData(key);
        Collection<CollectionRecord> coll = removeAllInternal(dataKey);
        Collection<V> result = new ArrayList<V>(coll.size());
        for (CollectionRecord record: coll){
            result.add((V)getNodeEngine().toObject(record.getObject()));
        }
        return result;
    }

    public int valueCount(K key) {
        Data dataKey = getNodeEngine().toData(key);
        return valueCountInternal(dataKey);
    }



}