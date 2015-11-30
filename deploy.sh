#!/usr/bin/env bash

set -e

source ./build/utils/cf-common.sh

cs=configuration-service
rb=rabbitmq-bus

cf s | grep $rb || cf cs cloudamqp lemur $rb

cf d -f configuration-client
cf d -f $cs
cf s | grep $cs && cf ds -f $cs

deploy_app $cs
deploy_service $cs
deploy_app configuration-client

