#!/usr/bin/env bash

set -e


ls -la

./build/utils/cf-common.sh

echo "deploying CONFIGURATION" ;


deploy_app configuration-service
deploy_service configuration-service

deploy_app configuration-client