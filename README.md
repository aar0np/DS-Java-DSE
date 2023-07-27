<div class="top">
  <img class="scenario-academy-logo" src="https://datastax-academy.github.io/katapod-shared-assets/images/ds-academy-2023.svg" />
</div>

# Java Development with Apache Cassandra®

## Table of Contents

1. [Sign up for Astra free account](#1-sign-up-for-a-free-astra-account)
2. [Create Schema](#2-create-schema)
3. [Insert Data](#3-insert-data)
4. [GitPod](#4-gitpod)
5. [Code the DAL](#5-code-the-dal)
6. [Run](#6-run)

## 1. Sign up for a free Astra account

Go to [astra.datastax.com](https://astra.datastax.com) and sign up for a new account.  You can select to use Single Sign On with either a GitHub or Google account.
Click on the "Create a Vector Database" button.

Feel free to name your database whatever you like, but set the keyspace name to "bigbox."  Ensure that the "Provider" is set to "Google Cloud,"
and select "us-east1" (Monck's Corner, South Carolina) as the region.  Click the "Create Database" button.

Your new database should be spinning-up in the background, and you should see a pop-up with your
default access token details.  Copy and/or download these details, as we will need the "token" property later on.

## 2. Create schema

Once the database is available on the Astra dashboard, click on the database link, and select the "CQL Console" tab.

Move over to the new "bigbox" keyspace with the `USE` command:

```
use bigbox;
```

Next, create the tables and index that we will be using.

```
CREATE TABLE bigbox.product (
    product_id text PRIMARY KEY,
    brand text,
    images set<text>,
    name text,
    product_group text,
    short_desc text
);

CREATE TABLE bigbox.product_vector (
    product_id text PRIMARY KEY,
    name text,
    product_vector vector<float, 32>
);

CREATE CUSTOM INDEX product_vector_product_vector_idx ON bigbox.product_vector (product_vector) USING 'StorageAttachedIndex';
```

## 3. Insert Data

```
INSERT INTO product(product_id,product_group,name,brand,short_desc,images)
VALUES ('LS534L','LS534','Go Away Annotation T-Shirt','NerdShirts','Men''s Large "Go Away...Annotation" T-Shirt',{'ls534.png'});
INSERT INTO product(product_id,product_group,name,brand,short_desc,images)
VALUES ('LN355L','LN355','Your Face is an @Autowired @Bean T-Shirt','NerdShirts','Men''s Large "Your Face...Autowired" T-Shirt',{'ln355.png'});
INSERT INTO product(product_id,product_group,name,brand,short_desc,images)
VALUES ('DSA1121L','DSA1121','DataStax Astra "One Team" Long Sleeve Tee','DataStax','DataStax Astra "One Team" Long Sleeve Tee - Large',{'dsa1121.jpg'});
INSERT INTO product(product_id,product_group,name,brand,short_desc,images)
VALUES ('APC30L','APC30','Apache Cassandra 3.0 Contributor T-Shirt','Apache Foundation','Apache Cassandra 3.0 Contributor T-Shirt - Large',{'apc30.jpg'});
INSERT INTO product(product_id,product_group,name,brand,short_desc,images)
VALUES ('DSS821L','DSS821','DataStax Gray Track Jacket','DataStax','DataStax Gray Track Jacket - Large',{'dss821.jpg'});
INSERT INTO product(product_id,product_group,name,brand,short_desc,images)
VALUES ('DSH915L','DSH915','DataStax Vintage 2015 MVP Hoodie','DataStax','DataStax Vintage 2015 MVP Hoodie - Large',{'dsh915.jpg'});
INSERT INTO product(product_id,product_group,name,brand,short_desc,images)
VALUES ('DSH916L','DSH916','DataStax Black Hoodie','DataStax','DataStax Black Hoodie - Large',{'dsh916.jpg'});
```
```
INSERT INTO product_vector(product_id,name,product_vector)
VALUES ('LS534L','Go Away Annotation T-Shirt',[0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0]);
INSERT INTO product_vector(product_id,name,product_vector)
VALUES ('LN355L','Your Face is an @Autowired @Bean T-Shirt',[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0]);
INSERT INTO product_vector(product_id,name,product_vector)
VALUES ('DSA1121L','DataStax Astra "One Team" Long Sleeve Tee',[1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0]);
INSERT INTO product_vector(product_id,name,product_vector)
VALUES ('APC30L','Apache Cassandra 3.0 Contributor T-Shirt',[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0]);
INSERT INTO product_vector(product_id,name,product_vector)
VALUES ('DSS821L','DataStax Gray Track Jacket',[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0]);
INSERT INTO product_vector(product_id,name,product_vector)
VALUES ('DSH915L','DataStax Vintage 2015 MVP Hoodie',[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0]);
INSERT INTO product_vector(product_id,name,product_vector)
VALUES ('DSH916L','DataStax Black Hoodie',[1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0]);
```

## 4. GitPod

[![Open in GitPod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/aar0np/DS-Java-DSE)

Part of the GitPod image will be the Astra CLI tool.  You will be prompted to paste your token
at a terminal prompt.

You can now see your database with this command:

```
astra db list
```

Now, make note of your database name and download your Secure Connect Bundle:
```
astra db download-scb mydatabase
```

###Edit .env file

Rename the `.env` file, and add your token (starts with "AstraCS:"):

```
mv .env_example .env
```

Open and edit the `.env` file.  Right click on your Secure Connect Bundle Zip file in the file explorer, and select the
"Copy Path" option.  Paste the path to the `ASTRA_DB_SECURE_BUNDLE_PATH` entry in the `.env` file.  Also 
paste in your Astra token to the `ASTRA_DB_APP_TOKEN` variable.  Leave `ASTRA_DB_CLIENT_ID` and `ASTRA_DB_KEYSPACE` set as-is.

Then "source" in the .env file:

```
source .env
```

## 5. Code the DAL

Look in the `src/main/java/com/datastax/products/` directory for the `ProductDAL.java`.
Complete the empty methods.

## 6. Run

```
mvn spring-boot:run
```

Open up a new terminal in GitPod, and trigger your service with `curl`:

```
curl -XGET http://127.0.0.1:8080/productsvc/products/APC30L
```
```
curl -XGET http://127.0.0.1:8080/productsvc/promoproducts/APC30L
```
