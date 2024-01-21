FROM openjdk:17-ea-jdk-alpine
VOLUME /product-order-mgmt
EXPOSE 9090
ADD ./target/product-order-mgmt-1.0.0.jar product-order-mgmt.jar
ENTRYPOINT ["java","-jar","/product-order-mgmt.jar"]