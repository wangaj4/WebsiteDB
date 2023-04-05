CREATE DATABASE moviedb;
USE moviedb;
CREATE TABLE IF NOT EXISTS movies(
                id varchar(10) primary key,
                title varchar(100),
		year integer,
		director varchar(100)
               );

CREATE TABLE IF NOT EXISTS stars(
                id varchar(10) primary key,
                name varchar(100) not null,
                birthYear integer
               );

CREATE TABLE IF NOT EXISTS stars_in_movies(
                starId varchar(10),
		movieId varchar(10)
               );

CREATE TABLE IF NOT EXISTS genres(
                id integer primary key AUTO_INCREMENT,
                name varchar(32)
               );

CREATE TABLE IF NOT EXISTS genres_in_movies(
                genreId integer,
                movieId varchar(10)
               );

CREATE TABLE IF NOT EXISTS customers(
                id integer primary key AUTO_INCREMENT,
                firstName varchar(50),
                lastName varchar(50),
		ccId varchar(20),
		address varchar(200),
		email varchar(50),
		password varchar(20)
               );

CREATE TABLE IF NOT EXISTS sales(
                id integer primary key AUTO_INCREMENT,
                customerId integer,
		movieId varchar(10),
		saleDate date
               );

CREATE TABLE IF NOT EXISTS creditcards(
                id varchar(20) primary key,
                firstName varchar(50),
                lastName varchar(50),
		expiration date
               );

CREATE TABLE IF NOT EXISTS ratings(
                movieId varchar(10),
                rating float,
		numVotes integer
               );



