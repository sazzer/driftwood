FROM nginx:1.15.8

COPY build /driftwood/html/
COPY docker/run.sh /driftwood/
COPY docker/nginx.config /etc/nginx/conf.d/default.conf

CMD /driftwood/run.sh
