#!/bin/sh


TASKMANAGEMENT_DB_CREATED="TASKMANAGEMENT_DB_CREATED"
if [ -n "${TASKMANAGEMENT_DB}" ] && [ ! -e /var/lib/postgresql/data/${TASKMANAGEMENT_DB_CREATED} ]; then
  if pg_isready; then
    touch /var/lib/postgresql/data/${TASKMANAGEMENT_DB_CREATED}
    {
        /init/initdb.d/initdb.sh
    } &
  fi
fi

pg_isready


