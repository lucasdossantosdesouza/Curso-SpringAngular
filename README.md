#helptask back-end
Utilize a ide intellij para rodar o projeto
Para testar a Api utilize o endereço http://localhost:8080/swagger-ui/index.html?configUrl=/api-docs/swagger-config
Após acessar vá ao endponint de autenticação feito com Spring Security e JWT
POST ​/api​/auth endpoint que gera o token
clique na opção, vai abir a demonstração do endpoint
clique na opção try out
informe o seguinte json: {
  "email": "admin@admin.com",
  "password": "123456"
}
Após isso clique em execute.
No requeste body copie o valor sem as aspas da opçãp token.
Vá ao início da pagina e clique na opção authorize.
Vai abrir uma janela(Pop-up).
Cole o valor copiado no campo Value e clique em authorize.
após isso vc pode utilizar os endpois para fazer as operações disponíveis na interface do swagger para a api.


