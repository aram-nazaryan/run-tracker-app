FROM postgis/postgis:12-3.4-alpine

ENV POSTGRES_USER=runtracker
ENV POSTGRES_PASSWORD=runtrackerpass
ENV POSTGRES_DB=runtracker

EXPOSE 5432

CMD ["postgres"]
