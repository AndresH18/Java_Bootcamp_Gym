#!/usr/bin/bash
#
# Script: instance-initialization.sh
# Description: This script will install java 21 corretto and check for the newest
#   application version to download from s3 bucket
# Interesting links:
#   - https://stackoverflow.com/questions/31062365/get-last-modified-object-from-s3-using-aws-cli/31064378#31064378

sudo yum install java-21-amazon-corretto-devel -y

if [[ -z "$MICROSERVICE_NAME" ]]; then
  echo "Error, environment variables are not valid"
  echo "Please set the MICROSERVICE_NAME environment variable"
  exit 1
fi

# Check if 'deployments' directory exists
if [ ! -d "deployments" ]; then
  echo "Creating 'deployments' directory..."
  mkdir deployments || { echo "Failed to create 'deployments' directory"; exit 1; }
fi

# Move into 'deployments' directory
cd deployments || { echo "Failed to change directory"; exit 1; }

# list files, get 4th column (file name), sort, and get the last (higher version of file)
RECENT_VERSION=$(aws s3 ls s3://gym-deploy-bucket/crm/ | awk '{print $4}' | sort | tail -n 1)

# download latest version
aws s3 cp s3://gym-deploy-bucket/"$MICROSERVICE_NAME"/"$RECENT_VERSION" .

# run jar
java -jar "$RECENT_VERSION" --spring.profiles.active=aws
