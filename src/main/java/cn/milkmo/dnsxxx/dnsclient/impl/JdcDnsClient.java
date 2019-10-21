package cn.milkmo.dnsxxx.dnsclient.impl;

import cn.milkmo.dnsxxx.Result;
import cn.milkmo.dnsxxx.config.EcsConfig;
import cn.milkmo.dnsxxx.dnsclient.DnsClient;
import com.jdcloud.sdk.JdcloudSdkException;
import com.jdcloud.sdk.auth.StaticCredentialsProvider;
import com.jdcloud.sdk.http.HttpRequestConfig;
import com.jdcloud.sdk.http.Protocol;
import com.jdcloud.sdk.model.ServiceError;
import com.jdcloud.sdk.service.clouddnsservice.client.ClouddnsserviceClient;
import com.jdcloud.sdk.service.clouddnsservice.model.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@ConditionalOnProperty(prefix = "app-config.ecs-config", name = "provider", havingValue = "jdc")
@Service
public class JdcDnsClient implements DnsClient {
    private ClouddnsserviceClient client;
    private String regionId;

    public JdcDnsClient(EcsConfig ecsConfig) {
        regionId = ecsConfig.getRegionId();
        client = ClouddnsserviceClient.builder()
                .credentialsProvider(new StaticCredentialsProvider(ecsConfig.getAccessKeyId(), ecsConfig.getAccessKeySecret()))
                .httpRequestConfig(new HttpRequestConfig.Builder().protocol(Protocol.HTTPS).build())
                .build();
    }

    @Override
    public Result deleteSubDomainRecord(String topDomain, String hostRecord, String recordType) {
        Result<Integer> domainIdResult = getDomainId(topDomain);
        if (domainIdResult.getCode() == -1) {
            return domainIdResult;
        }

        Integer domainId = domainIdResult.getData();
        Result<List<Integer>> recordIdsResult = getRecordIds(domainId, hostRecord, recordType);
        if (recordIdsResult.getCode() == -1) {
            return recordIdsResult;
        }

        List<Integer> ids = recordIdsResult.getData();
        if (ids.isEmpty()) {
            return Result.success();
        }

        OperateRRRequest request = new OperateRRRequest()
                .action("del")
                .regionId(regionId)
                .domainId(domainId.toString())
                .ids(ids)
                ;

        try {
            client.operateRR(request);
        } catch (JdcloudSdkException e) {
            return Result.error(e.getMessage());
        }

        return Result.success();
    }

    @Override
    public Result addSubDomainRecord(String topDomain, String hostRecord, String recordType, String recordValue) {
        Result<Integer> domainIdResult = getDomainId(topDomain);
        if (domainIdResult.getCode() == -1) {
            return domainIdResult;
        }

        Integer domainId = domainIdResult.getData();

        AddRR addRR = new AddRR()
                .hostRecord(hostRecord)
                .hostValue(recordValue)
                .type(recordType)
                .ttl(600)
                .viewValue(-1)
                ;

        AddRRRequest request = new AddRRRequest()
                .regionId(regionId)
                .domainId(domainId.toString())
                .req(addRR)
                ;

        try {
            client.addRR(request);
        } catch (JdcloudSdkException e) {
            return Result.error(e.getMessage());
        }

        return Result.success();
    }

    private Result<Integer> getDomainId(String domainName) {
        GetDomainsRequest request = new GetDomainsRequest()
                .regionId(regionId)
                .pageNumber(1)
                .pageSize(50)
                ;

        GetDomainsResponse response;

        try {
            response = client.getDomains(request);
        } catch (JdcloudSdkException e) {
            return Result.error(e.getMessage());
        }

        ServiceError error = response.getError();
        if (error != null) {
            return Result.error(error.getMessage());
        }

        List<Domain> domainList = response.getResult().getDataList();
        for (Domain domain : domainList) {
            if (domain.getDomainName().equals(domainName)) {
                return Result.success(domain.getId());
            }
        }

        return Result.error("no match domain[" + domainName + "]");
    }

    private Result<List<Integer>> getRecordIds(Integer domainId, String hostRecord, String recordType) {
        SearchRRRequest request = new SearchRRRequest()
                .regionId(regionId)
                .domainId(domainId.toString())
                .pageNumber(1)
                .pageSize(50)
                ;

        SearchRRResponse response;

        try {
            response = client.searchRR(request);
        } catch (JdcloudSdkException e) {
            return Result.error(e.getMessage());
        }

        ServiceError error = response.getError();
        if (error != null) {
            return Result.error(error.getMessage());
        }

        List<Integer> ids = new ArrayList<>();
        List<RR> rRs = response.getResult().getDataList();
        for (RR rR : rRs) {
            if (rR.getType().equals(recordType) && rR.getHostRecord().equals(hostRecord)) {
                ids.add(rR.getId());
            }
        }

        return Result.success(ids);
    }
}
