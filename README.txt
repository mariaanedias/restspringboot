Rodar SpringBoot com Gradle ->  gradle bootRun no cmd

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
passando como parâmetro form-data o key paramValue e qualquer valor. (se não passar nada retorna o default).

POST
http://localhost:8080/profiles
Body raw - JSON APPLICATION
Passar o conteúdo do arquivo TestJSON no body.
Deve retornar o mesmo conteúdo no response (significa que passou no controller, mapeou para as entities e retornou OK).

Você pode brincar e modificar qualquer parâmetro/response.