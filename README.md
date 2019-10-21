# dns-xxx
dns-xxx 用于添加或删除云服务器的DNS记录，目前支持京东云、阿里云

#### 用法
    #有recordValue存在时为添加，不存在时为删除
    java -jar xxx.jar domain record recordType [recordValue]

#### application.yaml 配置文件
    app-config:
        # 云服务器配置
        ecs-config:
            # 云服务器厂商
            provider:
                # jdc: 京东云
                # alc: 阿里云
         
            # 地域编码
            regionId:
                # 京东云参考: https://docs.jdcloud.com/cn/common-declaration/api/introduction
                # 阿里云参考：https://help.aliyun.com/document_detail/40654.html?spm=a2c6h.13066369.0.0.54a120f8j2JbKL
            
            accessKeyId: xxx
            accessKeySecret: xxx
            
        # 支持的域名后缀，默认为: .cn, .me, .com, .net, .top, .com.cn
        domainSuffixes:
            
#### authenticator.sh、cleanup.sh 
用于Certbot申请泛域名证书时的DNS自动验证
