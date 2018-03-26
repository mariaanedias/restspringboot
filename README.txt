Rodar SpringBoot com Gradle -> ./gradlew bootRun no cmd

Usar Postman ou similar pra chamar as APIs REST.

Pra configurar a porta do server fazer um application.properties e configurar server.port

Pra escolher entre Tomcat, Jetty e Undertow configurar direto no Gradle ou Maven.

Veja:
https://spring.io/guides/gs/rest-service/
http://start.spring.io/
http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-change-the-http-port

Chamadas:

GET
http://localhost:8080/testGet

POST
http://localhost:8080/testPostParam
passando como par�metro form-data o key paramValue e qualquer valor. (se n�o passar nada retorna o default).

POST
http://localhost:8080/profiles
Body raw - JSON APPLICATION
Passar o conte�do do arquivo TestJSON no body.
Deve retornar o mesmo conte�do no response (significa que passou no controller, mapeou para as entities e retornou OK).

Voc� pode brincar e modificar qualquer par�metro/response.

Usando STS (Spring Tool Suite) Starter:
https://spring.io/blog/2015/03/18/spring-boot-support-in-spring-tool-suite-3-6-4