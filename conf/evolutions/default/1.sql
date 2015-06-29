# Urls schema
 
# --- !Ups

CREATE TABLE "Urls" (
  "hashCol" VARCHAR(255) NOT NULL,
  "fullUrl" VARCHAR(255) NOT NULL
);

# --- !Downs

DROP TABLE "Urls";