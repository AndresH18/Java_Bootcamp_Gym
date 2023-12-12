#!/usr/bin/bash
#
# Script: create-environment-variables.sh
# Description: This script will create a file with the environment these environment variables.
#     To set the environment variables then run
#       "source environment-variables.sh" or ". environment-variables.sh"


#sudo yum install java-21-amazon-corretto-devel -y

#cd ~ || exit
#echo -e "\n\n\n\n" >> .bashrc

echo "#!/usr/bin/bash" >> environment-variables.sh

declare -A envVariables

envVariables["ACCESS_KEY"]=$ACCESS_KEY
envVariables["SECRET_KEY"]=$SECRET_ACCESS_KEY
envVariables["DATASOURCE_URL"]=$DATA_SOURCE_URL
envVariables["DATASOURCE_USERNAME"]=$DATA_SOURCE_USERNAME
envVariables["DATASOURCE_PASSWORD"]=$DATA_SOURCE_PASSWORD
envVariables["SQS_ENDPOINT"]=$SQS_ENDPOINT
envVariables["SQS_NAME"]=$SQS_NAME

# echo "${envVariables[@]}"
for key in "${!envVariables[@]}"
do
#    echo -e "$key=${envVariables[$key]}\n"  >> environment-configuration.txt
  echo "Exporting $key=${envVariables[$key]}"
  echo "export ${key}=\"${envVariables[$key]}"\" >> environment-variables.sh
done



