FROM postgres
ENV POSTGRES_PASSWORD=admin
VOLUME ["/etc/postgresql", "/var/log/postgresql", "var/lib/postgresql"]
COPY ./var-14/ /var/lib/postgresql/data
EXPOSE 5432