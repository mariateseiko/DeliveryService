/*
 * Copyright (c) 2016, Andrew Grivachevsky, Anastasiya Kostyukova,
 * Maria Teseiko
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package by.bsuir.deliveryservice.service.util;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * An utility class for hashing purposes. Currently offers only md5 hash.
 */
public class Hasher {
    final static Logger LOG = Logger.getLogger(Hasher.class);

    /**
     * Calculates a hash value of a given string with md5 algorithm
     * @param value String to hash
     * @return md5 hash value of the string
     */
    public static String md5Hash(String value) {
        String valueHash = null;
        if (value != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(value.getBytes());
                byte[] bytes = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : bytes) {
                    sb.append(String.format("%02x", b & 0xff));
                }
                valueHash = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                LOG.error("Error in hashing value " + value);
            }
        }
        return valueHash;
    }
}
