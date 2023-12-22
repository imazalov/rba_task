#!/bin/bash

DB_NAME="rba"
DB_USER="rba"
DB_PASS="rba"

# Check if the database exists
if psql -U "$DB_USER" -lqt | cut -d \| -f 1 | grep -qw $DB_NAME; then
    echo "Database $DB_NAME already exists."
else
    # Create the database
    createdb -U "$DB_USER" "$DB_NAME"
fi
