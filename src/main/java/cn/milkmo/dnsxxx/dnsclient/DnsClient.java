package cn.milkmo.dnsxxx.dnsclient;

import cn.milkmo.dnsxxx.Result;

public interface DnsClient {
    Result deleteSubDomainRecord(String topDomain, String hostRecord, String recordType);
    Result addSubDomainRecord(String topDomain, String hostRecord, String recordType, String recordValue);
}
