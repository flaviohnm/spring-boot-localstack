#!/bin/bash -x

createParameterStore(){
  awslocal --endpoint http://localhost:4566 ssm put-parameter \
           --name "/config/spring-boot-localstack_localstack/helloWorld" \
           --value "Hello World Mrs Monteiro" --type String
}

echo "Criando o Parameter Store"
createParameterStore