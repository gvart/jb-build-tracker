#!/usr/bin/env bash
set -euo pipefail

echo "CONFIGURATION AWS COMPONENTS"
echo "=========================================="
AWS_REGION=us-east-1

awslocal sqs create-queue --queue-name main.fifo --region ${AWS_REGION} --attributes '{"FifoQueue":"true", "FifoThroughputLimit":"perMessageGroupId", "DeduplicationScope":"messageGroup"}'