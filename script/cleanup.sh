#!/bin/bash

DIR=$(cd `dirname $0`; pwd)
cd $DIR
java -jar ./dns-xxx-1.0.0.jar $CERTBOT_DOMAIN _acme-challenge TXT
