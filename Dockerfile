# docker tarafindan kullanilacak JDK surumunu belirtir.
FROM amazoncorretto:17
# JAR_FILE -> jar dosyasinin path inin dinamik olarak verilebilmesini saglar.
ARG JAR_FILE=target/*.jar
# JAR dosyalarinin bilgisayarda bir kopyasini uretmek icin kullanilir, yazmaksak da calisabilir.
COPY ${JAR_FILE} application.jar
# dockerfile in hangi parametreleri alarak calisacagini belirler.
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/application.jar"]