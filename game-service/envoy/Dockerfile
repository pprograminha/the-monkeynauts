FROM envoyproxy/envoy:v1.18.3

COPY envoy.yml /etc/envoy/service-envoy.yaml
COPY descriptor/themonkeynauts.pb /data/descriptor.pb
CMD /usr/local/bin/envoy -c /etc/envoy/service-envoy.yaml