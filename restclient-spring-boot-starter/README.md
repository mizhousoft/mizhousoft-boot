#导入证书
keytool -importcert -file ca.cer -keystore trust-keystore.jks -alias pae.mizhousoft.com -storepass changeit

#删除证书
keytool -delete -alias pae.mizhousoft.com -keystore trust-keystore.jks -storepass changeit

#查询证书
keytool -list -keystore trust-keystore.jks -storepass changeit


# 导入p12证书
keytool -v -importkeystore -srckeystore apiclient_cert.p12 -srcstoretype PKCS12 -destkeystore trust-keystore.jks -deststoretype JKS