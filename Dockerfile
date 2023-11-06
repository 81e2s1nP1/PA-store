# Sử dụng hình ảnh Maven để xây dựng ứng dụng
FROM maven:3.8.7-openjdk-17 AS build

# Sao chép mã nguồn của bạn vào hình ảnh
COPY . /app
WORKDIR /app

# Sử dụng Maven để xây dựng ứng dụng
RUN mvn clean package -DskipTests

# Sử dụng hình ảnh OpenJDK để chạy ứng dụng đã xây dựng
FROM openjdk:17-jdk-slim

# Sao chép tệp JAR đã xây dựng từ hình ảnh Maven sang hình ảnh OpenJDK
COPY --from=build /app/target/Website-banhang-1.0.jar /app/app.jar

# Thiết lập cổng mà ứng dụng của bạn lắng nghe
EXPOSE 8080

# Chạy ứng dụng khi hình ảnh Docker được khởi chạy
CMD ["java", "-jar", "/app/app.jar"]


