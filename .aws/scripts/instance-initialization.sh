#!/usr/bin/bash

# Script: instance-initialization.sh
# Description: This script will install java 21 corretto and check for the newest
#   application version to download from s3 bucket, and run it (setting the active profile to 'aws').
#   It will also check download and set the environment variables.
#
#   Must explicitly set the 'MICROSERVICE_NAME' environment variable outside of script
#
# Interesting links:
#   - https://stackoverflow.com/questions/31062365/get-last-modified-object-from-s3-using-aws-cli/31064378#31064378


if [[ -z "$MICROSERVICE_NAME" ]]; then
  echo "Error, environment variables are not valid"
  echo "Please set the MICROSERVICE_NAME environment variable"
  exit 1
fi

sudo yum install java-21-amazon-corretto-devel -y

# create environment variables
ENV_VARIABLE_SCRIPT="environment-variables.sh"

if [ ! -f "$ENV_VARIABLE_SCRIPT" ]; then
  echo "Retrieving environment file"
  aws s3 cp s3://gym-deploy-bucket/"$ENV_VARIABLE_SCRIPT" .
fi

echo "Sourcing environment variables script '$ENV_VARIABLE_SCRIPT'..."
source "$ENV_VARIABLE_SCRIPT"


# Check if 'deployments' directory exists
if [ ! -d "deployments" ]; then
  echo "Creating 'deployments' directory..."
  mkdir deployments || { echo "Failed to create 'deployments' directory"; exit 1; }
fi

# Move into 'deployments' directory
cd deployments || { echo "Failed to change directory"; exit 1; }

# list files, get 4th column (file name), sort, and get the last (higher version of file)
RECENT_VERSION=$(aws s3 ls s3://gym-deploy-bucket/"$MICROSERVICE_NAME"/ | awk '{print $4}' | sort | tail -n 1)

# download latest version
aws s3 cp s3://gym-deploy-bucket/"$MICROSERVICE_NAME"/"$RECENT_VERSION" .

# run jar
java -jar "$RECENT_VERSION" --spring.profiles.active=aws
