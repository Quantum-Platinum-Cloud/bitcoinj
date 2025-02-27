/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.core;

import org.bitcoinj.base.VarInt;
import org.bitcoinj.base.internal.InternalUtils;
import org.bitcoinj.net.discovery.PeerDiscovery;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Represents an "addr" message on the P2P network, which contains broadcast IP addresses of other peers. This is
 * one of the ways peers can find each other without using the {@link PeerDiscovery} mechanism.
 * <p>
 * Instances of this class are not safe for use by multiple threads.
 */
public class AddressV1Message extends AddressMessage {

    /**
     * Construct a new 'addr' message.
     * @throws ProtocolException
     */
    AddressV1Message(ByteBuffer payload) throws ProtocolException {
        super(payload);
    }

    @Override
    protected void parse(ByteBuffer payload) throws BufferUnderflowException, ProtocolException {
        final VarInt numAddressesVarInt = VarInt.read(payload);
        int numAddresses = numAddressesVarInt.intValue();
        // Guard against ultra large messages that will crash us.
        if (numAddresses > MAX_ADDRESSES)
            throw new ProtocolException("Address message too large.");
        addresses = new ArrayList<>(numAddresses);
        MessageSerializer serializer = new DummySerializer(1);
        for (int i = 0; i < numAddresses; i++) {
            PeerAddress addr = new PeerAddress(payload, serializer);
            addresses.add(addr);
        }
    }

    public void addAddress(PeerAddress address) {
        addresses.add(address);
    }

    @Override
    public String toString() {
        return "addr: " + InternalUtils.SPACE_JOINER.join(addresses);
    }
}
