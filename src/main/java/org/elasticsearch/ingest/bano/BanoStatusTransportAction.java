/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.ingest.bano;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpRequest;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.security.PrivilegedExceptionAction;

import static org.elasticsearch.ingest.bano.ClientSecurityManager.doPrivilegedException;

public class BanoStatusTransportAction extends HandledTransportAction<BanoStatusRequest, BanoStatusResponse> {

    @Inject
    public BanoStatusTransportAction(Settings settings, ThreadPool threadPool, ActionFilters actionFilters,
                                     IndexNameExpressionResolver resolver, TransportService transportService,
                                     ClusterService clusterService) {
        super(settings, BanoStatusAction.NAME, threadPool, transportService, actionFilters, resolver, BanoStatusRequest::new);
    }

    @Override
    protected void doExecute(BanoStatusRequest request, ActionListener<BanoStatusResponse> listener) {
        BanoStatusResponse response = new BanoStatusResponse();

        logger.error("Inside prepareRequest");
        try (CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.create().build()) {
            logger.error("Client is built");

            doPrivilegedException((PrivilegedExceptionAction<HttpResponse>) () -> {
                logger.error("Executing request");
                CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(new HttpHost("127.0.0.1", 9200), new BasicHttpRequest("GET", "/"));
                logger.error("Request executed");
                return httpResponse;
            });

            response.setMessage("foo");
            listener.onResponse(response);
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }
}

