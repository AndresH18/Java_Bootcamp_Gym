import boto3
import os
import csv
from datetime import date

# import logging

# logging.basicConfig(level=logging.DEBUG)
# logger = logging.getLogger("MySuperLogger-dm")

def lambda_handler(event, context):
    # Get environment variables
    table_name = os.environ.get("DYNAMODB_TABLE_NAME")
    s3_bucket_name = os.environ.get("S3_BUCKET_NAME")

    # Initialize DynamoDB client
    dynamodb = boto3.resource('dynamodb')
    s3 = boto3.client("s3")
    table = dynamodb.Table(table_name)  # Replace 'YourDynamoDBTableName' with your table's name

    current_date = date.today()
    current_year = str(current_date.year).zfill(4)
    current_month = str(current_date.month).zfill(2)

    file_name = f"TRAINERS_TRAININGS_{current_year}_{current_month}.csv"
    file_path = f'/tmp/{file_name}'

    try:
        # Retrieve items from DynamoDB
        response = table.scan()
        print("Hello")

        items = response['Items']
        table_contents = []

        #        logger.info(f"Found {len(items)} items")
        print(f"Found {len(items)} items")
        for item in items:
            #            logger.debug(f"processing item {item}")
            print(f"Processing item {item}")
            trainer_username = item["trainerUsername"]
            summary = item.get("summary", {})

            for year, month_data in summary.items():
                for month, value in month_data.items():
                    table_contents.append({
                        "TRAINER_USERNAME": trainer_username,
                        "YEAR": str(year),
                        "MONTH": str(month),
                        "DURATION": str(value),
                    })

        table_contents = sorted(table_contents, key=lambda x: (x["TRAINER_USERNAME"], x["YEAR"], x["MONTH"]))

        #        logger.info("Creating report file")
        print("Creating report file")
        with open(file_path, 'w', newline='') as csvfile:
            fieldnames = ['TRAINER_USERNAME', 'YEAR', 'MONTH', "DURATION"]
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

            writer.writeheader()
            for row in table_contents:
                writer.writerow(row)

        #        logger.info(f"Uploading to s3, '{file_name}'")
        print(f"Uploading to s3, '{file_name}'")
        s3.upload_file(file_path, s3_bucket_name, f"reports/{file_name}")

        # writer.writerow({'first_name': 'Baked', 'last_name': 'Beans'})
        return {
            'statusCode': 200,
            "body": "Lambda function completed"
        }

    except Exception as e:
        print(f"Error processing function: {e}")
        return {
            "statusCode": 500,
            "body": f"Error processing function: {e}"
        }
