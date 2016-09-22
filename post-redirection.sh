#!/bin/sh
curl -u 'newuser:HGY3WbEb' -v -H "Content-Type: application/json" -X POST -d '{"url":"http://10.10.152.40", "redirectType":"302"}' http://localhost:8080/bip/register
curl -u 'newuser:HGY3WbEb' -v -H "Content-Type: application/json" -X POST -d '{"url":"http://10.10.152.50", "redirectType":"302"}' http://localhost:8080/bip/register
