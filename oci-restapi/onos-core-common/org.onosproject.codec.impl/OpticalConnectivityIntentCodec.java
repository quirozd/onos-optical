/*
 * Copyright 2016 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.codec.impl;

import org.onosproject.codec.CodecContext;
import org.onosproject.codec.JsonCodec;
import org.onosproject.net.ConnectPoint;
import org.onosproject.net.OduSignalType;
import org.onosproject.net.intent.Intent;
import org.onosproject.net.intent.OpticalConnectivityIntent;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Optical connectivity intent codec.
 */
public class OpticalConnectivityIntentCodec extends JsonCodec<OpticalConnectivityIntent> {

    private static final String INGRESS_POINT = "src";
    private static final String EGRESS_POINT = "dst";
    private static final String SIGNAL_TYPE = "signalType";
    private static final String BIDIRECTIONAL = "isBidirectional";

    @Override
    public ObjectNode encode(OpticalConnectivityIntent intent, CodecContext context) {
        checkNotNull(intent, "Optical connectivity intent cannot be null");

        final JsonCodec<Intent> intentCodec = context.codec(Intent.class);
        final ObjectNode result = intentCodec.encode(intent, context);

        final JsonCodec<ConnectPoint> connectPointCodec =
                context.codec(ConnectPoint.class);
        final ObjectNode ingress =
                connectPointCodec.encode(intent.getSrc(), context);
        final ObjectNode egress =
                connectPointCodec.encode(intent.getDst(), context);
        final ObjectNode odusignal = context.mapper().createObjectNode()
                .put(SIGNAL_TYPE, intent.getSignalType().toString());
        final ObjectNode bid = context.mapper().createObjectNode()
                .put(BIDIRECTIONAL, intent.isBidirectional());

        result.set(INGRESS_POINT, ingress);
        result.set(EGRESS_POINT, egress);
        result.set(SIGNAL_TYPE, odusignal);
        result.set(BIDIRECTIONAL, bid);

        return result;
    }

    @Override
    public OpticalConnectivityIntent decode(ObjectNode json, CodecContext context) {

        OpticalConnectivityIntent.Builder builder = OpticalConnectivityIntent.builder();
        IntentCodec.intentAttributes(json, context, builder);
        ObjectNode ingressJson = get(json, INGRESS_POINT);
        ObjectNode egressJson = get(json, EGRESS_POINT);
        JsonCodec<ConnectPoint> connectPointCodec = context.codec(ConnectPoint.class);
        if (ingressJson != null) {
            ConnectPoint src = connectPointCodec.decode(ingressJson, context);
            builder.src(src);
        }

        if (egressJson != null) {
            ConnectPoint dst = connectPointCodec.decode(egressJson, context);
            builder.dst(dst);
        }

        builder.signalType(OduSignalType.ODU4); //Automate this assignment

        ObjectNode bidJson = get(json, BIDIRECTIONAL);
        if (bidJson != null) {
            boolean isBidirectional = bidJson.get(BIDIRECTIONAL).booleanValue();
            builder.bidirectional(isBidirectional);
        }
        return builder.build();
    }

}
