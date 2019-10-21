package cn.milkmo.dnsxxx.dnsclient.impl;

import cn.milkmo.dnsxxx.Result;
import cn.milkmo.dnsxxx.config.EcsConfig;
import cn.milkmo.dnsxxx.dnsclient.DnsClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.AddDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteSubDomainRecordsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(prefix = "app-config.ecs-config", name = "provider", havingValue = "alc")
@Service
public class AlcDnsClient implements DnsClient {
    private IAcsClient client;

    public AlcDnsClient(EcsConfig ecsConfig) {
        client = new DefaultAcsClient(DefaultProfile.getProfile(ecsConfig.getRegionId(), ecsConfig.getAccessKeyId(), ecsConfig.getAccessKeySecret()));
    }


    @Override
    public Result deleteSubDomainRecord(String topDomain, String hostRecord, String recordType) {
        DeleteSubDomainRecordsRequest request = new DeleteSubDomainRecordsRequest();
        request.setDomainName(topDomain);
        request.setRR(hostRecord);
        request.setType(recordType);

        try {
            client.getAcsResponse(request);
        } catch (ClientException e) {
            return Result.error(e.getErrMsg());
        }

        return Result.success();
    }

    @Override
    public Result addSubDomainRecord(String topDomain, String hostRecord, String recordType, String recordValue) {
        AddDomainRecordRequest request = new AddDomainRecordRequest();
        request.setDomainName(topDomain);
        request.setRR(hostRecord);
        request.setType(recordType);
        request.setValue(recordValue);

        try {
            client.getAcsResponse(request);
        } catch (ClientException e) {
            return Result.error(e.getErrMsg());
        }

        return Result.success();
    }
}
