### 主要修改如下

1. [修改项目Parent](#修改项目Parent)
1. [jackson冲突解决](#jackson冲突解决)
2. [启动类调整](#启动类调整)
3. 拦截器调整：HandlerInterceptorAdapter弃用，使用HandlerInterceptor
4. [MongoTemplate配置文件调整](#MongoTemplate配置文件调整)
5. [openfeign调整](#openfeign调整)
6. [dynamodb调整](#dynamodb调整)
7. [swagger调整](#swagger调整)
8. [配置文件调整](#配置文件调整)



#### 修改项目Parent

~~~java
	<parent>
        <groupId>com.mkenterprise</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.0.3</version>
    </parent>
~~~

**注：可将repositories、dependencies import版本包、部分properties移除，详情请看parent的POM文件**



#### jackson冲突解决

> 设置ObjectMapper serializationInclusion：不可直接调用Jackson2ObjectMapperBuilder的serializationInclusion方法
>
> ~~~java
> @Override
>     public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
>         Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
>                 .indentOutput(true)
>                 .failOnUnknownProperties(false)
>                 // .serializationInclusion(JsonInclude.Include.ALWAYS)
>                 .simpleDateFormat("yyyy-MM-dd HH:mm:ss")
>                .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
>                    .modulesToInstall(getJavaTimeModule())
>                .modulesToInstall(new ParameterNamesModule());
>         ObjectMapper objectMapper = builder.build().setSerializationInclusion(JsonInclude.Include.ALWAYS);
>        HttpMessageConverter<?> tempConverter = converters.get(0);
>         converters.set(0, new DefaultJackson2HttpMessageConverter(objectMapper));
>         converters.add(tempConverter);
>     }
> ~~~
> 
> 



#### 启动类调整

新版本升级时，ideal会报红，尽量使用SpringBootApplication中的属性进行配置。

默认扫描包：启动类所在包，如果存在相同，请删除。

移除未存在包的扫描配置：@MapperScan({"com.marykay.cn.orderingextensionapp.mapper"})

**示例：**

~~~java
@Import(value = {LogHelper.class})
@SpringBootApplication
@MapperScan({"com.marykay.cn.orderingextensionapp.mapper"})
@ComponentScan(basePackages = {
        "com.marykay.cn.orderingextensionapp"
})
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients
@EnableCaching
~~~

调整为：

~~~java
@Import(value = {LogHelper.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableCaching
~~~



####  MongoTemplate配置文件调整

**示例：**

```java
	@Bean
    @ConfigurationProperties(prefix="spring.data.proxypayment.mongodb")
    public MongoProperties proxyPaymentMongoProperties() {
        return new MongoProperties();
    }

    @Bean(name = "proxyPaymentMongoTemplate")
    public MongoTemplate proxyPaymentMongoTemplate() throws Exception {
        return new MongoTemplate(proxyPaymentFactory(proxyPaymentMongoProperties()));
    }

    @Bean
    public MongoDbFactory proxyPaymentFactory(MongoProperties mongoProperties) throws Exception {
        MongoClientURI mongoClientURI = new MongoClientURI(mongoProperties.getUri());
        return new SimpleMongoDbFactory(new MongoClient(mongoClientURI), mongoProperties.getDatabase());
    }
```

调整为：

~~~java
@Bean(name = "proxypayment-mongodb-properties")
    @ConfigurationProperties(prefix="spring.data.proxypayment.mongodb")
    public MongoProperties proxyPaymentMongoProperties() {
        return new MongoProperties();
    }

    @Bean(name = "proxyPaymentMongoTemplate")
    public MongoTemplate proxyPaymentMongoTemplate(@Qualifier("proxypayment-mongodb-properties") MongoProperties properties) throws Exception {
        MongoClient mongoClient = MongoClients.create(properties.getUri());
        return new MongoTemplate(mongoClient,properties.getDatabase());
    }
~~~



#### openfeign调整

**调整事项：**

1. POM调整（新版本有些依赖是可选的，不会自动导入）

   ~~~java
   		<dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-openfeign</artifactId>
           </dependency>
   ~~~

   调整为：

   ~~~java
   		<dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-openfeign</artifactId>
           </dependency>
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-loadbalancer</artifactId>
           </dependency>
           <dependency>
               <groupId>com.netflix.hystrix</groupId>
               <artifactId>hystrix-core</artifactId>
               <version>1.5.18</version>
           </dependency>
   ~~~

2. @FeignClient修饰的接口不支持：@RequestMapping，需将前缀移至每个URL上。

3. 自定义feignClient调整

~~~java
	@Bean
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        return new LoadBalancerFeignClient(new Client.Default(ctx.getSocketFactory(),
                (hostname, sslSession) -> true),
                cachingFactory, clientFactory);
    }
~~~

调整为：

~~~java
	@Bean
    public Client feignClient(LoadBalancerClient loadBalancerClient,
                              LoadBalancerClientFactory loadBalancerClientFactory) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        return new FeignBlockingLoadBalancerClient(new Client.Default(ctx.getSocketFactory(),null),
                loadBalancerClient, loadBalancerClientFactory);
    }
~~~



#### dynamodb调整

1. POM调整

   ~~~java
   		<dependency>
               <groupId>com.github.derjust</groupId>
               <artifactId>spring-data-dynamodb</artifactId>
               <version>5.0.4</version>
           </dependency>
   ~~~

   调整为：

   ~~~~java
   		<dependency>
               <groupId>io.github.boostchicken</groupId>
               <artifactId>spring-data-dynamodb</artifactId>
               <version>5.2.5</version>
           </dependency>
   ~~~~

   原因：

   ​	之前的[dynamodb](https://github.com/derjust/spring-data-dynamodb)现在不支持spring boot2.6了，三四年没更新了

   ​	io.github.boostchicken 的 [dynamodb](https://github.com/boostchicken/spring-data-dynamodb)支持，且是原来的作者[推荐](https://github.com/derjust/spring-data-dynamodb/issues/273)。

2. 配置调整（移除Bean：DynamoDBMapperConfig、DynamoDBMapper）

   ~~~java
   @Configuration
   @EnableDynamoDBRepositories(basePackages = "com.marykay.cn.orderingextensionapp.repository.dynamo")
   public class DynamoDBConfig {
   
       @Value("${spring.data.dynamo.access-key}")
       private String amazonAWSAccessKey;
   
       @Value("${spring.data.dynamo.secret-key}")
       private String amazonAWSSecretKey;
   
       public AWSCredentialsProvider amazonAWSCredentialsProvider() {
           return new AWSStaticCredentialsProvider(amazonAWSCredentials());
       }
   
       @Bean
       public DynamoDBMapperConfig dynamoDBMapperConfig() {
           return DynamoDBMapperConfig.DEFAULT;
       }
   
       @Bean
       public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
           return new DynamoDBMapper(amazonDynamoDB, config);
       }
   
       @Bean
       public AmazonDynamoDB amazonDynamoDB() {
           return AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
                   .withRegion(Regions.CN_NORTH_1).build();
       }
   
       @Bean
       public AWSCredentials amazonAWSCredentials() {
           return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
       }
   }
   ~~~

   调整为：

   ~~~java
   @Configuration
   @EnableDynamoDBRepositories(basePackages = "com.marykay.cn.orderingextensionapp.repository.dynamo")
   public class DynamoDBConfig {
   
       @Value("${spring.data.dynamo.access-key}")
       private String amazonAWSAccessKey;
   
       @Value("${spring.data.dynamo.secret-key}")
       private String amazonAWSSecretKey;
   
       public AWSCredentialsProvider amazonAWSCredentialsProvider() {
           return new AWSStaticCredentialsProvider(amazonAWSCredentials());
       }
   
       @Bean
       public AmazonDynamoDB amazonDynamoDB() {
           return AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
                   .withRegion(Regions.CN_NORTH_1).build();
       }
   
       @Bean
       public AWSCredentials amazonAWSCredentials() {
           return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
       }
   
   }
   ~~~



#### swagger调整

#### [swagger完成配置指南](https://dev.azure.com/marykayapac/AppMidEnd/_git/fwk-mkenterprise-springboot-starter-swagger?path=/README.md)

1. POM调整：

   ~~~java
   		<dependency>
               <groupId>io.springfox</groupId>
               <artifactId>springfox-swagger2</artifactId>
               <version>2.7.0</version>
           </dependency>
           <!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger-ui -->
           <dependency>
               <groupId>io.springfox</groupId>
               <artifactId>springfox-swagger-ui</artifactId>
               <version>2.7.0</version>
           </dependency>
   ~~~

   调整为：

   ~~~java
   		<dependency>
               <groupId>com.marykay.ame</groupId>
               <artifactId>swagger-spring-boot-starter</artifactId>
               <version>1.0.0-SNAPSHOT</version>
           </dependency>
   ~~~

2. 移除原swagger相关配置：

   如：SwaggerConfig、MVC中的静态文件配置、自定义的swagger相关属性。

3. 新配置示例：

~~~yml
spring:
  mvc:
    pathmatch:
      matching-strategy: ANT_PATH_MATCHER
swagger:
  enable: true
  docker-infos:
    - groupName: ${ApplicationName}
      basePackage: com.marykay.cn.orderingextensionapp.api.controller
      title: ${ApplicationTitle}
      description: ${ApplicationTitle}
      version: 1.0
      parameters:
        - name: subsidiary
          required: true
          defaultValue: CN
          description: subsidiary
          parameter-type: header
        - name: culture
          required: true
          defaultValue: zh-CN
          description: culture
          parameter-type: header
~~~



#### 配置文件调整

> ~~~java
> 1. spring.resources.add-mappings -> spring.web.resources.add-mappings
> 2. mysql 的driver：com.mysql.jdbc.Driver -> com.mysql.cj.jdbc.Driver
> 3. mysql 的URL参数连接符：&amp; -> &
> ~~~
>
> 

