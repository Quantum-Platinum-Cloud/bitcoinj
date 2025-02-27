/*
 * Copyright 2012 Matt Corallo
 * Copyright 2015 Andreas Schildbach
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

import org.bitcoinj.base.internal.ByteUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * <p>See <a href="https://github.com/bitcoin/bips/blob/master/bip-0031.mediawiki">BIP31</a> for details.</p>
 *
 * <p>Instances of this class are not safe for use by multiple threads.</p>
 */
public class Pong extends Message {
    private long nonce;

    public Pong(ByteBuffer payload) throws ProtocolException {
        super(payload);
    }
    
    /**
     * Create a Pong with a nonce value.
     * Only use this if the remote node has a protocol version greater than 60000
     */
    public Pong(long nonce) {
        this.nonce = nonce;
    }
    
    @Override
    protected void parse(ByteBuffer payload) throws BufferUnderflowException, ProtocolException {
        nonce = ByteUtils.readInt64(payload);
    }
    
    @Override
    public void bitcoinSerializeToStream(OutputStream stream) throws IOException {
        ByteUtils.writeInt64LE(nonce, stream);
    }
    
    /** Returns the nonce sent by the remote peer. */
    public long getNonce() {
        return nonce;
    }
}
