package cn.milkmo.dnsxxx.utils;

import java.util.List;
import java.util.regex.Pattern;

public class DomainUtils {
    private final static Pattern DOMAIN_PATTERN = Pattern.compile("^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$");

    public static boolean isValid(String domain) {
        return DOMAIN_PATTERN.matcher(domain).matches();
    }

    public static String extractTopDomain(String domain, List<String> domainSuffixes) {
        domainSuffixes.sort((s, t) -> t.length() - s.length());

        String domainSuffix = null;
        for (String suffix : domainSuffixes) {
            if (domain.endsWith(suffix)) {
                domainSuffix = suffix;
                break;
            }
        }

        if (null == domainSuffix) {
            return null;
        }

        String topDomain = domain.substring(0, domain.lastIndexOf(domainSuffix));
        int index = topDomain.lastIndexOf(".");
        index = index < 0 ? 0 : index + 1 ;

        return topDomain.substring(index) + domainSuffix;
    }
}
