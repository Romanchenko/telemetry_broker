COUNTER=1
while true
do
  curl 'http://51.250.72.130/sentiment' \
  -H 'Connection: keep-alive' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_16_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 YaBrowser/20.9.0.1697 Yowser/2.5 Safari/537.36' \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'Origin: http://51.250.72.130' \
  -H 'Referer: http://51.250.72.130/' \
  -H 'Accept-Language: ru,en;q=0.9,hy;q=0.8' \
  --data-binary '{"sentence":"disgusting"}' \
  --compressed \
  --insecure
  COUNTER=$[$COUNTER +1]
  echo ""
  if (( $COUNTER % 20 == 0 ))
  then
    echo "Sent "  $COUNTER " queries"
  fi
done