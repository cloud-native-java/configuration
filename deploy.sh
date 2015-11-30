#!/usr/bin/env bash

set -e

source ./build/utils/cf-common.sh

cs=configuration-service

cf s | grep $cs && cf ds -f $cs
deploy_app $cs
deploy_service $cs
deploy_app configuration-client

