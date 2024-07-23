#!/bin/bash -x

createParameterStore(){
  awslocal --endpoint http://localhost:4566 ssm put-parameter \
          --name "/config/spring-boot-localstack/helloWorld" \
          --value "Hello World Mrs Monteiro" --type String

  awslocal --endpoint http://localhost:4566 ssm put-parameter \
          --name "/config/spring-boot-localstack/sqsQueueName" \
          --value "sqsHelloWorld" --type String

  awslocal --endpoint http://localhost:4566 ssm put-parameter \
          --name "/config/spring-boot-localstack/snsNotificationName" \
          --value "snsHelloWorld" --type String

  awslocal --endpoint http://localhost:4566 ssm put-parameter \
          --name "/config/spring-boot-localstack/s3Bucket" \
          --value "s3-helloworld" --type String
}

createSecretsManager(){
  awslocal --endpoint http://localhost:4566 secretsmanager create-secret \
          --name /secrets/stringKey1 --description "Exemplo de Secrets Manager" \
          --secret-string "{\"valor1\":\"Oi Mundo\",\"valor2\":\"Hello World\",\"valor3\":\"Hola Mundo\"}"

  awslocal --endpoint http://localhost:4566 secretsmanager create-secret \
          --name /secret/stringKey2 --description "Exemplo de Secrets Manager" \
          --secret-string "{\"valor1\":\"Oi Mundo\",\"valor2\":\"Hello World\",\"valor3\":\"Hola Mundo\"}"

#  awslocal --endpoint http://localhost:4566 secretsmanager create-secret \
#          --name /secret/application --description "Exemplo de Secrets Manager" \
#          --secret-string "{\"valor1\":\"Oi Mundo\",\"valor2\":\"Hello World\",\"valor3\":\"Hola Mundo\"}"
}

createSQS(){
  awslocal --endpoint http://localhost:4566 sqs create-queue --queue-name sqsHelloWorld
  awslocal --endpoint http://localhost:4566 sqs send-message --queue-url http://localhost:4566/000000000000/sqsHelloWorld \
            --message-body "Hello World SQS!!!" --delay-seconds 1
# awslocal --endpoint http://localhost:4566  sqs receive-message --queue-url http://localhost:4566/000000000000/sqsHelloWorld
}

createSNS(){
  awslocal --endpoint http://localhost:4566 sns create-topic --name snsHelloWorld
  awslocal --endpoint http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:snsHelloWorld \
          --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:sqsHelloWorld
}

createBucketS3(){
  awslocal --endpoint http://localhost:4566 --region us-east-1 s3 mb s3://s3-helloworld
}


createParameterStore

createSecretsManager

createSQS

createSNS

createBucketS3