/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 7, 2026
 * Time:    7:38:44 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.datasource;

import java.util.concurrent.TimeUnit;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.infrastructure.dto.DatasourceInfo;

/**
 * 
 */
public class MongoDatasource extends ADatasource<MongoClient> {

    private MongoClient client;

    /**
     * @param datasourceInfo
     */
    public MongoDatasource(DatasourceInfo datasourceInfo) {
        super(datasourceInfo);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected MongoClient getConnectionFromDatasource() throws FitlyRuntimeException {
        return client;
    }

    @Override
    protected void initDatasource(DatasourceInfo datasourceInfo) {

        String uri = String.format("mongodb://%s:%s@%s:%d",
                datasourceInfo.getUsername(),
                datasourceInfo.getPassword(),
                datasourceInfo.getHost(),
                datasourceInfo.getPort());
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .applicationName("fitly")
                .applyToConnectionPoolSettings(builder -> builder.maxSize(datasourceInfo.getMaxConnection())
                        .minSize(1)
                        .maxWaitTime(2000, TimeUnit.MILLISECONDS) // Thời gian chờ tối đa để lấy connection
                )
                // Thêm cấu hình timeout (khuyến nghị cho năm 2026)
                .applyToSocketSettings(builder -> builder.connectTimeout(5000, TimeUnit.MILLISECONDS))
                .build();
        client = MongoClients.create(settings);
    }

    @Override
    public void closeDatasource() {

        if (client != null) {
            client.close();
        }
    }

    public MongoCollection<Document> getCollections(String tenantId, String tableName) {
        return client.getDatabase(tenantId).getCollection(tableName);
    }
}
