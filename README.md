# IDP - Identity Provider Service

OAuth2 인증을 위한 필요한 서비스를 제공하는 것이 목적인 프로젝트입니다.
[Spring Authorization Server](https://spring.io/projects/spring-authorization-server) 를 이용하여 개발되었습니다.

## 모듈 설명

| 모듈명                | 포트   | 설명 |
|--------------------|------|----|
| authorization      | 8081 |    |
| sso                | 8082 |    |
| account            | 8083 |    |
| client-registration | 8084 |    |
| api-gateway        | 8080 |    |
| service-discovery  | 8090 |    |
|                    |      |    |

## 비대칭키 생성 (jks)

자바의 keytool을 사용해야 비대칭키를 생성한다.

```
keytool -genkeypair -alias identity-provider -keyalg RSA -keystore identity-provider-test.jks -storepass 123456!

keytool -list -v -keystore identity-provider-test.jks
```

`storepass`는 키 저장소에 접근할때 사용하는 암호이다.

공개키를 생성한다.

```
keytool -list -rfc --keystore identity-provider-test.jks | openssl x509 -inform pem -pubkey > publicKey.txt
```

`storepass` 암호를 입력한다.