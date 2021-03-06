/*
문서명 : DDL-04
작성자 : 4조
작성일자 : 2020/06/02
프로젝트명 : Chick Education Center
프로그램명 : Chick Education Center
프로그램 설명 : 교육센터 시스템을 구현하기 위한 프로그램이다.

*/



-----------------------------기초 정보----------------------------------


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

-- 과정 정보
CREATE TABLE TBL_COURSE
(
    SEQ NUMBER PRIMARY KEY, -- 과정 번호
    NAME VARCHAR2(200) NOT NULL, -- 과정 이름
    PERIOD NUMBER NOT NULL  -- 과정 기간    
);


-- 교재 정보
CREATE TABLE TBL_BOOK
(
    SEQ NUMBER PRIMARY KEY, -- 교재 번호
    NAME VARCHAR2(100) NOT NULL, -- 교재 명
    PUBLISHER VARCHAR2(50) NOT NULL -- 출판사 명
);

-- 과목 정보
CREATE TABLE TBL_SUBJECT
(
    SEQ NUMBER PRIMARY KEY, -- 과목 번호
    NAME VARCHAR2(200) NOT NULL, -- 과목 이름
    PERIOD NUMBER NOT NULL,  -- 과목 기간    
    BOOKSEQ NUMBER NOT NULL REFERENCES TBL_BOOK(SEQ) -- 교재 번호
);


-- 강의실 정보
CREATE TABLE TBL_CLASSROOM
(
    SEQ NUMBER PRIMARY KEY, -- 강의실 번호
    MAXIMUM NUMBER NOT NULL -- 인원 수
);


----------------------------- 필수 FK ----------------------------------

-- 개설 과정 정보
CREATE TABLE TBL_OPEN_COURSE
(
    SEQ NUMBER PRIMARY KEY, -- 개설 과정 번호
    STARTDATE DATE NOT NULL, -- 개설 과정 시작 날짜
    ENDDATE DATE NOT NULL, -- 개설 과정 종료 날짜
    CRSSEQ NUMBER NOT NULL REFERENCES TBL_COURSE(SEQ) -- 과정 번호
);



-- 강의 가능 과목 정보
CREATE TABLE TBL_AVAIL_SUBJECT
(
    SEQ NUMBER PRIMARY KEY, -- 강의 가능 과목 번호
    TEASEQ NUMBER NOT NULL REFERENCES TBL_TEACHER(SEQ), -- 교사 번호
    SUBSEQ NUMBER NOT NULL REFERENCES TBL_SUBJECT(SEQ) -- 과목 번호
);


-- 개설 과목 정보
CREATE TABLE TBL_OPEN_SUBJECT
(
    SEQ NUMBER PRIMARY KEY, -- 개설 과목 번호
    STARTDATE DATE NOT NULL, -- 개설 과목 시작 날짜
    ENDDATE DATE NOT NULL, -- 개설 과목 종료 날짜
    OCRSSEQ NUMBER NOT NULL REFERENCES TBL_OPEN_COURSE(SEQ), -- 개설 과정 번호
    AVAILSEQ NUMBER NOT NULL REFERENCES TBL_AVAIL_SUBJECT(SEQ) -- 배정 정보    
);

-- 과정별 과목 정보
CREATE TABLE TBL_COURSE_SUBJECT
(
    SEQ NUMBER PRIMARY KEY, -- 과정별 과목 번호
    CRSSEQ NUMBER NOT NULL REFERENCES TBL_COURSE(SEQ), -- 과정 번호
    SUBSEQ NUMBER NOT NULL REFERENCES TBL_SUBJECT(SEQ)  -- 과목 번호
);


-- 수강신청 정보
CREATE TABLE TBL_APPLY
(
    SEQ NUMBER PRIMARY KEY, -- 수강 신청 번호
    STATUS DATE, --수강신청 상태
    STUSEQ NUMBER NOT NULL REFERENCES TBL_STUDENT(SEQ), -- 교육생 번호
    OCRSSEQ NUMBER NOT NULL REFERENCES TBL_OPEN_COURSE(SEQ) -- 개설 과정 번호
);

----------------------------- 그외 테이블  ----------------------------------


-- 개설된 강의실 정보
CREATE TABLE TBL_OPEN_CLASSROOM
(
    SEQ NUMBER PRIMARY KEY, -- 개설된 강의실 번호
    OCRSSEQ NUMBER NOT NULL REFERENCES TBL_OPEN_COURSE(SEQ), -- 개설 과정 번호
    ROOMSEQ NUMBER NOT NULL REFERENCES TBL_CLASSROOM(SEQ) -- 강의실 번호   
);


-- 성적
CREATE TABLE TBL_SCORE
(
    SEQ NUMBER PRIMARY KEY, -- 성적 번호
    WRITE NUMBER, -- 필기 성적
    PERFORMANCE NUMBER, -- 실기 성적
    ATTEND NUMBER, -- 출결 점수
    OSUBSEQ NUMBER NOT NULL REFERENCES TBL_OPEN_SUBJECT(SEQ), -- 개설 과목 번호
    APPSEQ NUMBER NOT NULL REFERENCES TBL_APPLY(SEQ) -- 수강신청 번호
);



-- 배점
CREATE TABLE TBL_POINT
(
    SEQ NUMBER PRIMARY KEY, -- 배점 번호
    WRITE NUMBER, -- 필기 배점
    PERFORMANCE NUMBER, -- 실기 배점
    ATTEND NUMBER DEFAULT 20 NOT NULL, -- 출결 배점
    OSUBSEQ NUMBER NOT NULL REFERENCES TBL_OPEN_SUBJECT(SEQ) --  개설 과목 번호
);



-- 시험
CREATE TABLE TBL_TEST
(
    SEQ NUMBER PRIMARY KEY, -- 시험 번호
    TESTFILE VARCHAR2(500), -- 시험 파일
    TESTDATE DATE, -- 시험 날짜
    OSUBSEQ NUMBER NOT NULL REFERENCES TBL_OPEN_SUBJECT(SEQ) -- 개설 과목 번호
);

---------------------------추가 요구사항  ----------------------------------

-- 국비 지원
CREATE TABLE TBL_SUPPORT
(
    SEQ NUMBER PRIMARY KEY, --  국비 지원 번호
    TYPE VARCHAR2(50) NOT NULL, -- 국비 지원 종류(취성패,내일배움,재직자,자비)
    DEPDATE DATE, -- 지급일
    APPSEQ NUMBER NOT NULL REFERENCES TBL_APPLY(SEQ) -- 수강 신청 번호
);

-- 취업
CREATE TABLE TBL_EMPLOYMENT
(
     SEQ NUMBER PRIMARY KEY, -- 취업 번호
     COMPANY VARCHAR2(30), -- 회사명
     SALARY NUMBER, -- 연봉
     APPSEQ NUMBER NOT NULL REFERENCES TBL_APPLY(SEQ) -- 수강 신청 번호
);


-- 교사 평가
CREATE TABLE TBL_TEACHER_SCORE
(
    SEQ NUMBER PRIMARY KEY, -- 교사 평가 번호
    SCORE NUMBER,  -- 평점
    OSUBSEQ NUMBER NOT NULL REFERENCES TBL_OPEN_SUBJECT(SEQ) -- 개설 과목 번호
);
SELECT * FROM TBL_COURSE;
DELETE  FROM TBL_COURSE;
COMMIT;


-- 관리자 근태
CREATE TABLE TBL_ATTEND_ADMIN
(
    SEQ NUMBER PRIMARY KEY, -- 관리자 근태 번호
    DAYS DATE NOT NULL, -- 날짜
    INTIME DATE, -- 출근 시간
    OUTTIME DATE, -- 퇴근 시간
    STATUS VARCHAR2(10), -- 상태(병가,외출,기타)
    ADMSEQ NUMBER NOT NULL REFERENCES TBL_TEACHER(SEQ) -- 관리자 번호
);


-- 교사 근태
CREATE TABLE TBL_ATTEND_TEACHER
(
    SEQ NUMBER PRIMARY KEY, -- 교사 근태 번호
    DAYS DATE NOT NULL, -- 날짜
    INTIME DATE, -- 출근 시간
    OUTTIME DATE, -- 퇴근 시간
    STATUS VARCHAR2(10), -- 상태(병가,외출,기타)
    TEASEQ NUMBER NOT NULL REFERENCES TBL_TEACHER(SEQ) -- 교사 번호
);

-- 교육생 근태
CREATE TABLE TBL_ATTEND_STUDENT
(
    SEQ NUMBER PRIMARY KEY, -- 교육생 근태 번호
    DAYS DATE NOT NULL, -- 날짜
    INTIME DATE, -- 출근 시간
    OUTTIME DATE, -- 퇴근 시간
    STATUS VARCHAR2(10), -- 상태(병가,외출,기타)
    APPSEQ NUMBER NOT NULL REFERENCES TBL_APPLY(SEQ) -- 수강 신청 번호
);


-- 관리자 열 체크
CREATE TABLE TBL_COVID19_ADMIN
(
    SEQ NUMBER PRIMARY KEY, -- 관리자 열 체크 번호
    AMTEMP NUMBER, -- 오전 체온
    PMTEMP NUMBER, -- 오후 체온
    ATTSEQ NUMBER NOT NULL REFERENCES TBL_ATTEND_ADMIN(SEQ) -- 근태 번호
);

-- 교사 열 체크
CREATE TABLE TBL_COVID19_TEACHER
(
    SEQ NUMBER PRIMARY KEY, -- 교사 열 체크 번호
    AMTEMP NUMBER, -- 오전 체온
    PMTEMP NUMBER, -- 오후 체온
    ATTSEQ NUMBER NOT NULL REFERENCES TBL_ATTEND_TEACHER(SEQ) -- 근태 번호
);

-- 교육생 열 체크
CREATE TABLE TBL_COVID19_STUDENT
(
    SEQ NUMBER PRIMARY KEY, -- 교육생 열 체크 번호
    AMTEMP NUMBER, -- 오전 체온
    PMTEMP NUMBER, -- 오후 체온
    ATTSEQ NUMBER NOT NULL REFERENCES TBL_ATTEND_STUDENT(SEQ) -- 근태 번호
);

COMMIT;




/*

코멘트

*/

COMMENT ON TABLE TBL_ADMIN IS '관리자 계정 정보';
COMMENT ON COLUMN TBL_ADMIN.SEQ IS '관리자 번호';
COMMENT ON COLUMN TBL_ADMIN.ID IS '관리자 ID';
COMMENT ON COLUMN TBL_ADMIN.PW IS '관리자 PW';


COMMENT ON TABLE TBL_TEACHER IS '교사 계정 정보';
COMMENT ON COLUMN TBL_TEACHER.SEQ IS '교사 번호';
COMMENT ON COLUMN TBL_TEACHER.NAME IS '교사 이름';
COMMENT ON COLUMN TBL_TEACHER.ID IS '교사 ID';
COMMENT ON COLUMN TBL_TEACHER.SSN IS '교사 주민번호(PW로사용)';
COMMENT ON COLUMN TBL_TEACHER.TEL IS '교사 전화번호';


COMMENT ON TABLE TBL_STUDENT IS '교육생 계정 정보';
COMMENT ON COLUMN TBL_STUDENT.SEQ IS '학생 번호';
COMMENT ON COLUMN TBL_STUDENT.NAME IS '학생 이름';
COMMENT ON COLUMN TBL_STUDENT.ID IS '학생 ID';
COMMENT ON COLUMN TBL_STUDENT.SSN IS '학생 주민번호(PW로사용)';
COMMENT ON COLUMN TBL_STUDENT.TEL IS '학생 전화번호';
COMMENT ON COLUMN TBL_STUDENT.ACCOUNT IS '학생 계좌';
COMMENT ON COLUMN TBL_STUDENT.REGDATE IS '학생 등록일';


COMMENT ON TABLE TBL_COURSE IS '과정 정보';
COMMENT ON COLUMN TBL_COURSE.SEQ IS '과정 번호';
COMMENT ON COLUMN TBL_COURSE.NAME IS '과정 이름';
COMMENT ON COLUMN TBL_COURSE.PERIOD IS '과정 기간';


COMMENT ON TABLE TBL_SUBJECT IS '과목 정보';
COMMENT ON COLUMN TBL_SUBJECT.SEQ IS '과목 번호';
COMMENT ON COLUMN TBL_SUBJECT.NAME IS '과목 이름';
COMMENT ON COLUMN TBL_SUBJECT.PERIOD IS '과목 기간';
COMMENT ON COLUMN TBL_SUBJECT.BOOKSEQ IS '교재 번호';


COMMENT ON TABLE TBL_CLASSROOM IS '강의실 정보';
COMMENT ON COLUMN TBL_CLASSROOM.SEQ IS '강의실 번호';
COMMENT ON COLUMN TBL_CLASSROOM.MAXIMUM IS '인원 수';


COMMENT ON TABLE TBL_BOOK IS '교재 정보';
COMMENT ON COLUMN TBL_BOOK.SEQ IS '교재 번호';
COMMENT ON COLUMN TBL_BOOK.NAME IS '교재 명';
COMMENT ON COLUMN TBL_BOOK.PUBLISHER IS '출판사 명';


COMMENT ON TABLE TBL_OPEN_COURSE IS '개설 과정 정보';
COMMENT ON COLUMN TBL_OPEN_COURSE.SEQ IS '개설 과정 번호';
COMMENT ON COLUMN TBL_OPEN_COURSE.STARTDATE IS '개설 과정 시작 날짜';
COMMENT ON COLUMN TBL_OPEN_COURSE.ENDDATE IS '개설 과정 종료 날짜';
COMMENT ON COLUMN TBL_OPEN_COURSE.CRSSEQ IS '과정 번호';

COMMENT ON TABLE TBL_OPEN_SUBJECT IS '개설 과목 정보';
COMMENT ON COLUMN TBL_OPEN_SUBJECT.SEQ IS '개설 과목 번호';
COMMENT ON COLUMN TBL_OPEN_SUBJECT.STARTDATE IS '개설 과목 시작 날짜';
COMMENT ON COLUMN TBL_OPEN_SUBJECT.ENDDATE IS '개설 과목 종료 날짜';
COMMENT ON COLUMN TBL_OPEN_SUBJECT.OCRSSEQ IS '개설 과정 번호';
COMMENT ON COLUMN TBL_OPEN_SUBJECT.AVAILSEQ IS '배정 정보';

COMMENT ON TABLE TBL_APPLY IS '수강신청 정보';
COMMENT ON COLUMN TBL_APPLY.SEQ IS '수강 신청 번호';
COMMENT ON COLUMN TBL_APPLY.STATUS IS '수강신청 상태';
COMMENT ON COLUMN TBL_APPLY.STUSEQ IS '교육생 번호';
COMMENT ON COLUMN TBL_APPLY.OCRSSEQ IS '개설 과정 번호';

COMMENT ON TABLE TBL_COURSE_SUBJECT IS '과정별 과목 정보';
COMMENT ON COLUMN TBL_COURSE_SUBJECT.SEQ IS '과정별 과목 번호';
COMMENT ON COLUMN TBL_COURSE_SUBJECT.CRSSEQ IS '과정 번호';
COMMENT ON COLUMN TBL_COURSE_SUBJECT.SUBSEQ IS '과목 번호';

COMMENT ON TABLE TBL_AVAIL_SUBJECT IS '강의 가능 과목 정보';
COMMENT ON COLUMN TBL_AVAIL_SUBJECT.SEQ IS '강의 가능 과목 번호';
COMMENT ON COLUMN TBL_AVAIL_SUBJECT.TEASEQ IS '교사 번호';
COMMENT ON COLUMN TBL_AVAIL_SUBJECT.SUBSEQ IS '과목 번호';


COMMENT ON TABLE TBL_OPEN_CLASSROOM is '개설된 강의실 정보 테이블';
COMMENT ON COLUMN TBL_OPEN_CLASSROOM.SEQ is '개설된 강의실 정보 번호';
COMMENT ON COLUMN TBL_OPEN_CLASSROOM.OCRSSEQ is '개설 과정 번호 참조 키';
COMMENT ON COLUMN TBL_OPEN_CLASSROOM.ROOMSEQ is '강의실 번호 참조 키';

COMMENT ON TABLE TBL_SCORE is '성적 테이블';
COMMENT ON COLUMN TBL_SCORE.SEQ is '성적 번호';
COMMENT ON COLUMN TBL_SCORE.WRITE is '필기 성적';
COMMENT ON COLUMN TBL_SCORE.PERFORMANCE is '실기 성적';
COMMENT ON COLUMN TBL_SCORE.ATTEND is '출결 점수';
COMMENT ON COLUMN TBL_SCORE.OSUBSEQ is '개설 과목 번호 참조 키';
COMMENT ON COLUMN TBL_SCORE.STESEQ is '교육생 번호 참조 키';

COMMENT ON TABLE TBL_POINT is '배점 테이블';
COMMENT ON COLUMN TBL_POINT.SEQ is '배점 번호';
COMMENT ON COLUMN TBL_POINT.WRITE is '필기 배점';
COMMENT ON COLUMN TBL_POINT.PERFORMANCE is '실기 배점';
COMMENT ON COLUMN TBL_POINT.ATTEND is '출결 배점';
COMMENT ON COLUMN TBL_POINT.OSUBSEQ is '개설 과목 번호 참조 키';

COMMENT ON TABLE TBL_TEST is '시험 테이블';
COMMENT ON COLUMN TBL_TEST.SEQ is '시험 번호';
COMMENT ON COLUMN TBL_TEST.TESTFILE is '시험 파일';
COMMENT ON COLUMN TBL_TEST.TESTDATE is '시험 날짜';
COMMENT ON COLUMN TBL_TEST.OSUBSEQ is '개설 과목 번호 참조 키';

COMMENT ON TABLE TBL_SUPPORT IS '국비지원';
COMMENT ON COLUMN TBL_SUPPORT.SEQ IS '국비지원 번호';
COMMENT ON COLUMN TBL_SUPPORT.TYPE IS '국비지원 종류(취성패, 내일배움, 재직자, 자비)';
COMMENT ON COLUMN TBL_SUPPORT.DEPDATE IS '지급일';
COMMENT ON COLUMN TBL_SUPPORT.APPSEQ IS '수강신청 번호';

COMMENT ON TABLE TBL_EMPLOYMENT IS '취업';
COMMENT ON COLUMN TBL_EMPLOYMENT.SEQ IS '취업 번호';
COMMENT ON COLUMN TBL_EMPLOYMENT.COMPANY IS '회사명';
COMMENT ON COLUMN TBL_EMPLOYMENT.SALARY IS '연봉';
COMMENT ON COLUMN TBL_EMPLOYMENT.APPSEQ IS '수강신청 번호';

COMMENT ON TABLE TBL_TEACHER_SCORE IS '교사 평가';
COMMENT ON COLUMN TBL_TEACHER_SCORE.SEQ IS '평가 번호';
COMMENT ON COLUMN TBL_TEACHER_SCORE.SCORE IS '평점';
COMMENT ON COLUMN TBL_TEACHER_SCORE.OSUBSEQ IS '개설 과목 번호';

COMMENT ON TABLE TBL_ATTEND_ADMIN IS '관리자 근태';
COMMENT ON COLUMN TBL_ATTEND_ADMIN.SEQ IS  '관리자 근태 번호';
COMMENT ON COLUMN TBL_ATTEND_ADMIN.DAYS IS '날짜';
COMMENT ON COLUMN TBL_ATTEND_ADMIN.INTIME IS '출근시간';
COMMENT ON COLUMN TBL_ATTEND_ADMIN.OUTTIME IS '퇴근 시간';
COMMENT ON COLUMN TBL_ATTEND_ADMIN.STATUS IS '상태(병가,외출,기타)';
COMMENT ON COLUMN TBL_ATTEND_ADMIN.ADMSEQ IS '관리자 번호';

COMMENT ON TABLE TBL_ATTEND_TEACHER IS '교사 근태';
COMMENT ON COLUMN TBL_ATTEND_TEACHER.SEQ IS  '교사 근태 번호';
COMMENT ON COLUMN TBL_ATTEND_TEACHER.DAYS IS '날짜';
COMMENT ON COLUMN TBL_ATTEND_TEACHER.INTIME IS '출근시간';
COMMENT ON COLUMN TBL_ATTEND_TEACHER.OUTTIME IS '퇴근 시간';
COMMENT ON COLUMN TBL_ATTEND_TEACHER.STATUS IS '상태(병가,외출,기타)';
COMMENT ON COLUMN TBL_ATTEND_TEACHER.TEASEQ IS '교사 번호';

COMMENT ON TABLE TBL_ATTEND_STUDENT IS '교육생 근태';
COMMENT ON COLUMN TBL_ATTEND_STUDENT.SEQ IS  '교육생 근태 번호';
COMMENT ON COLUMN TBL_ATTEND_STUDENT.DAYS IS '날짜';
COMMENT ON COLUMN TBL_ATTEND_STUDENT.INTIME IS '출근시간';
COMMENT ON COLUMN TBL_ATTEND_STUDENT.OUTTIME IS '퇴근 시간';
COMMENT ON COLUMN TBL_ATTEND_STUDENT.STATUS IS '상태(병가,외출,기타)';
COMMENT ON COLUMN TBL_ATTEND_STUDENT.APPSEQ IS '수강신청 번호';

COMMENT ON TABLE TBL_COVID19_ADMIN IS '관리자 열 체크';
COMMENT ON COLUMN TBL_COVID19_ADMIN.SEQ IS '관리자 열 체크 번호';
COMMENT ON COLUMN TBL_COVID19_ADMIN.AMTEMP IS '오전 체온';
COMMENT ON COLUMN TBL_COVID19_ADMIN.PMTEMP IS '오후 체온';
COMMENT ON COLUMN TBL_COVID19_ADMIN.ATTSEQ IS '근태 번호';

COMMENT ON TABLE TBL_COVID19_TEACHER IS '교사 열 체크';
COMMENT ON COLUMN TBL_COVID19_TEACHER.SEQ IS '교사 열 체크 번호';
COMMENT ON COLUMN TBL_COVID19_TEACHER.AMTEMP IS '오전 체온';
COMMENT ON COLUMN TBL_COVID19_TEACHER.PMTEMP IS '오후 체온';
COMMENT ON COLUMN TBL_COVID19_TEACHER.ATTSEQ IS '근태 번호';

COMMENT ON TABLE TBL_COVID19_STUDENT IS '교육생 열 체크';
COMMENT ON COLUMN TBL_COVID19_STUDENT.SEQ IS '교육생 열 체크 번호';
COMMENT ON COLUMN TBL_COVID19_STUDENT.AMTEMP IS '오전 체온';
COMMENT ON COLUMN TBL_COVID19_STUDENT.PMTEMP IS '오후 체온';
COMMENT ON COLUMN TBL_COVID19_STUDENT.ATTSEQ IS '근태 번호';


COMMIT;


/*

시퀀스

*/
CREATE SEQUENCE ADMIN_SEQ; -- 관리자SEQ
CREATE SEQUENCE TEACHER_SEQ;  -- 교사SEQ
CREATE SEQUENCE STUDENT_SEQ; -- 교육생SEQ
CREATE SEQUENCE COURSE_SEQ; -- 과정SEQ
CREATE SEQUENCE BOOK_SEQ; -- 교재SEQ
CREATE SEQUENCE SUBJECT_SEQ; -- 과목SEQ
CREATE SEQUENCE CLASSROOM_SEQ; --강의실SEQ
CREATE SEQUENCE OPEN_COURSE_SEQ; --개설 과정SEQ
CREATE SEQUENCE AVAIL_SUBJECT_SEQ; -- 강의가능 과목SEQ
CREATE SEQUENCE OPEN_SUBJECT_SEQ; -- 개설 과목SEQ
CREATE SEQUENCE COURSE_SUBJECT_SEQ; -- 과정당 과목SEQ
CREATE SEQUENCE APPLY_SEQ; -- 수강신청SEQ
CREATE SEQUENCE OPEN_CLASSROOM_SEQ; -- 개설된 강의실SEQ
CREATE SEQUENCE SCORE_SEQ; -- 성적SEQ
CREATE SEQUENCE POINT_SEQ; -- 배점SEQ
CREATE SEQUENCE TEST_SEQ; -- 시험SEQ
CREATE SEQUENCE SUPPORT_SEQ; -- 국비지원SEQ
CREATE SEQUENCE EMPLOYMENT_SEQ; -- 취업SEQ
CREATE SEQUENCE TEACHER_SCORE_SEQ; -- 교사평가 SEQ
CREATE SEQUENCE ATTEND_ADMIN_SEQ; -- 관리자 근태SEQ
CREATE SEQUENCE ATTEND_TEACHER_SEQ; -- 교사 근태SEQ
CREATE SEQUENCE ATTEND_STUDENT_SEQ; -- 교육생 근태SEQ
CREATE SEQUENCE COVID19_ADMINSEQ; --관리자 열체크SEQ
CREATE SEQUENCE COVID19_TEACHERSEQ; -- 교사 열체크SEQ
CREATE SEQUENCE COVID19_STUDENTSEQ; -- 교육생 열체크SEQ

COMMIT;


