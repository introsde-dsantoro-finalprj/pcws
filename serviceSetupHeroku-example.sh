#!/bin/bash
heroku config:set STGWS_ENDPOINT=127.0.0.1
heroku config:set STGWS_PORT=80
heroku config:set BLWS_ENDPOINT=127.0.0.1
heroku config:set BLWS_PORT=80
