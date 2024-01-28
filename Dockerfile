FROM openjdk:17-ea-jdk-alpine
VOLUME /product-order
EXPOSE 9090
ADD ./target/product-order-1.0.0.jar product-order.jar
ENTRYPOINT ["java","-jar","/product-order.jar"]