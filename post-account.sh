#!/bin/sh
curl -v -H "Content-Type: application/json" -X POST -d '{"AccountId":"newuser"}' http://localhost:8080/bip/account
