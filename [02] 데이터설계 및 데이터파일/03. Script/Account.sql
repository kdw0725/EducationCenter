/*
문서명 : DDL-01-Account.sql (1/7)
작성자 : 4조
작성일자 : 2020.06.03.
프로젝트명 : 
프로그램명 : 교육센터 운영 프로그램(Education Manage Program)
프로그램 설명 : 교육센터 운영 시스템을 구현하기 위한 프로그램이다.
URL Link : 
*/

-- 관리자 계정 정보
CREATE TABLE TBL_ADMIN 
(
    SEQ NUMBER PRIMARY KEY, -- 관리자 번호
    ID VARCHAR2(50) NOT NULL, -- 관리자 ID
    PW VARCHAR2(50) NOT NULL -- 관리자 PW
);


-- 교사 계정 정보
CREATE TABLE TBL_TEACHER
(
    SEQ NUMBER PRIMARY KEY, -- 교사 번호
    NAME VARCHAR2(30) NOT NULL, -- 교사 이름
    ID VARCHAR2(50) NOT NULL, -- 교사  ID
    SSN VARCHAR2(14) NOT NULL, -- 교사 주민번호(PW로사용)
    TEL VARCHAR2(15) NOT NULL -- 교사 전화번호
);

-- 교육생 계정 정보
CREATE TABLE TBL_STUDENT
(
    SEQ NUMBER PRIMARY KEY, -- 학생 번호
    NAME VARCHAR2(30) NOT NULL, -- 학생 이름
    ID VARCHAR2(50) NOT NULL, -- 학생 ID
    SSN VARCHAR2(14) NOT NULL, -- 학생 주민번호(PW로사용)
    TEL VARCHAR2(15) NOT NULL, -- 학생 전화번호
    ACCOUNT VARCHAR2(50) NULL, -- 학생 계좌
    REGDATE DATE DEFAULT SYSDATE NOT NULL  -- 학생 등록일
);