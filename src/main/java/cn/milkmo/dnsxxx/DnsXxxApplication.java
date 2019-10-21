package cn.milkmo.dnsxxx;

import cn.milkmo.dnsxxx.config.AppConfig;
import cn.milkmo.dnsxxx.dnsclient.DnsClient;
import cn.milkmo.dnsxxx.utils.DomainUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DnsXxxApplication implements CommandLineRunner {
	@Autowired
	private AppConfig appConfig;

	@Autowired
	private DnsClient dnsClient;

	public static void main(String[] args) {
		if (args.length != 3 && args.length != 4) {
			log.error("usage:java -jar xxx.jar domain record recordType [recordValue]");
			return;
		}

		String domain = args[0];
		if (!DomainUtils.isValid(domain)) {
			log.error("invalid domain[" + domain + "]");
			return;
		}

		SpringApplication.run(DnsXxxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String domain = args[0];
		String topDomain = DomainUtils.extractTopDomain(domain, appConfig.getDomainSuffixes());
		if (null == topDomain) {
			log.error("invalid domain[" + domain + "]");
			return;
		}

		String record = args[1];
		String recordType = args[2];
		if (args.length == 3) {
			Result result = dnsClient.deleteSubDomainRecord(topDomain, record, recordType);
			if (result.getCode() != 0) {
				log.error(result.getMsg());
			}
			return;
		}

		String recordValue = args[3];
		Result result = dnsClient.addSubDomainRecord(topDomain, record, recordType, recordValue);
		if (result.getCode() != 0) {
			log.error(result.getMsg());
		}
	}
}
