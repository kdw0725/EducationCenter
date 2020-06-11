SET SERVEROUTPUT ON;

-- 교사 로그인
CREATE OR REPLACE PROCEDURE PROC_TEACHER_LOGIN(
    PID VARCHAR2,
    PPW VARCHAR2,
    PRESULT OUT NUMBER
)
AS
BEGIN
    SELECT COUNT(SEQ) INTO PRESULT FROM TBL_TEACHER WHERE ID = PID AND SUBSTR(SSN, 8, 7) = PPW AND DELFLAG = 'Y';
END;



-- 교사 로그인 테스트
DECLARE
    PRESULT NUMBER;
BEGIN
    PROC_TEACHER_LOGIN('esema', '1421286', PRESULT);
    IF PRESULT = 1 THEN
        DBMS_OUTPUT.PUT_LINE('로그인 성공');    
    ELSIF PRESULT = 0 THEN
        DBMS_OUTPUT.PUT_LINE('로그인 실패');
    END IF;
END;


-- 교사 정보 등록 프로시저
CREATE OR REPLACE PROCEDURE PROC_INSERT_TEACHER(
    PNAME VARCHAR2,
    PID VARCHAR2,
    PSSN VARCHAR2,
    PTEL VARCHAR2,
    PSUBSEQ NUMBER,
    PBOOKSEQ NUMBER
)
IS
BEGIN
    INSERT INTO TBL_TEACHER(SEQ, NAME, ID, SSN, TEL) VALUES(TEACHER_SEQ.NEXTVAL, PNAME, PID, PSSN, PTEL);
    INSERT INTO TBL_AVAIL_SUBJECT(SEQ, TEASEQ, SUBSEQ, BOOKSEQ) VALUES(AVAIL_SUBJECT_SEQ.NEXTVAL, TEACHER_SEQ.CURRVAL, PSUBSEQ, PBOOKSEQ);
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
END;

DROP PROCEDURE PROC_INSERT_TEACHER;



-- 교사 전체 정보 출력
CREATE OR REPLACE VIEW VW_TEACHER_INFO_ALL
AS
SELECT 
    A.NAME AS NAME, SUBSTR(SSN, 8, 7) AS PW, A.TEL AS TEL, C.NAME AS SUBJECT
FROM TBL_TEACHER A
INNER JOIN TBL_AVAIL_SUBJECT B
ON A.SEQ = B.TEASEQ
INNER JOIN TBL_SUBJECT C
ON C.SEQ = B.SUBSEQ
WHERE A.DELFLAG = 'Y' AND B.DELFLAG = 'Y' AND C.DELFLAG = 'Y';

DROP VIEW VW_TEACHER_INFO_ALL; 
-- 교사별 정보 출력을 위한 뷰
CREATE OR REPLACE VIEW VW_TEACHER_INFO
AS
SELECT 
        TE.SEQ, TE.NAME AS NAME, SUB.NAME AS SUBJECT, OSUB.STARTDATE AS START_SUBJECT, OSUB.ENDDATE AS END_SUBJECT, CR.NAME AS COURSE_NAME, OCOR.STARTDATE AS START_COURSE, OCOR.ENDDATE AS END_COURSE, BOOK.NAME AS BOOK_NAME, OCLR.ROOMSEQ AS OPEN_ROOM,
        CASE
            WHEN SYSDATE < OSUB.STARTDATE THEN '강의 예정'
            WHEN SYSDATE BETWEEN OSUB.STARTDATE AND OSUB.ENDDATE THEN '강의중'
            WHEN SYSDATE > OSUB.ENDDATE THEN '강의 종료'
        END AS WORKING
    FROM TBL_OPEN_SUBJECT OSUB
    INNER JOIN TBL_AVAIL_SUBJECT ASUB
    ON ASUB.SEQ = OSUB.AVAILSEQ
    INNER JOIN TBL_SUBJECT SUB
    ON SUB.SEQ = ASUB.SUBSEQ
    INNER JOIN TBL_OPEN_COURSE OCOR
    ON OCOR.SEQ = OSUB.OCRSSEQ
    INNER JOIN TBL_BOOK BOOK
    ON ASUB.BOOKSEQ = BOOK.SEQ
    INNER JOIN TBL_OPEN_CLASSROOM OCLR
    ON OCOR.SEQ = OCLR.OCRSSEQ
    INNER JOIN TBL_COURSE CR
    ON OCOR.CRSSEQ = CR.SEQ
    INNER JOIN TBL_TEACHER TE
    ON ASUB.TEASEQ = TE.SEQ
    WHERE OSUB.DELFLAG = 'Y' AND ASUB.DELFLAG = 'Y' AND SUB.DELFLAG = 'Y' AND OCOR.DELFLAG = 'Y' AND BOOK.DELFLAG = 'Y' AND OCLR.DELFLAG = 'Y' AND CR.DELFLAG = 'Y' AND TE.DELFLAG = 'Y'
    ;
DROP VIEW VW_TEACHER_INFO;
-- 교사 정보 출력    
SELECT * FROM VW_TEACHER_INFO WHERE SEQ = 1;
    

-- 교사별 정보 출력을 위한 프로시져
--CREATE OR REPLACE PROCEDURE PROC_TEACHER_DETAIL(
--    PNUM IN NUMBER,
--    PRESULT OUT SYS_REFCURSOR
--)
--IS
--BEGIN
--    OPEN PRESULT
--    FOR 
--    SELECT 
--        TE.NAME AS 교사명, SUB.NAME AS 과목명, OSUB.STARTDATE AS 과목시작, OSUB.ENDDATE AS 과목종료, CR.NAME AS 과정명, OCOR.STARTDATE AS 과정시작, OCOR.ENDDATE AS 과정종료, BOOK.NAME AS 교재명, OCLR.ROOMSEQ AS 개설강의실,
--        CASE
--            WHEN SYSDATE < OSUB.STARTDATE THEN '강의 예정'
--            WHEN SYSDATE BETWEEN OSUB.STARTDATE AND OSUB.ENDDATE THEN '강의중'
--            WHEN SYSDATE > OSUB.ENDDATE THEN '강의 종료'
--        END AS 강의진행여부
--    FROM TBL_OPEN_SUBJECT OSUB
--    INNER JOIN TBL_AVAIL_SUBJECT ASUB
--    ON ASUB.SEQ = OSUB.AVAILSEQ
--    INNER JOIN TBL_SUBJECT SUB
--    ON SUB.SEQ = ASUB.SUBSEQ
--    INNER JOIN TBL_OPEN_COURSE OCOR
--    ON OCOR.SEQ = OSUB.OCRSSEQ
--    INNER JOIN TBL_BOOK BOOK
--    ON ASUB.BOOKSEQ = BOOK.SEQ
--    INNER JOIN TBL_OPEN_CLASSROOM OCLR
--    ON OCOR.SEQ = OCLR.OCRSSEQ
--    INNER JOIN TBL_COURSE CR
--    ON OCOR.CRSSEQ = CR.SEQ
--    INNER JOIN TBL_TEACHER TE
--    ON ASUB.TEASEQ = TE.SEQ
--    WHERE ASUB.TEASEQ = PNUM
--    ORDER BY OSUB.STARTDATE DESC;
--END;
--
---- 교사 정보 출력 테스트
--DECLARE
--    PRESULT SYS_REFCURSOR;
--    VROW VW_TEACHER_INFO%ROWTYPE;
--BEGIN
--    PROC_TEACHER_DETAIL(1, PRESULT);
--    LOOP
--        FETCH PRESULT INTO VROW;
--        EXIT WHEN PRESULT%NOTFOUND;
--         DBMS_OUTPUT.PUT_LINE(VROW.NAME || ' ' || VROW.SUBJECT || ' ' || VROW.START_SUBJECT || ' ' || VROW.END_SUBJECT  || ' ' || VROW.COURSE_NAME || ' ' || VROW.START_COURSE || ' ' || VROW.END_COURSE || ' ' || VROW.BOOK_NAME || ' ' || VROW.OPEN_ROOM || ' ' || VROW.WORKING); 
--    END LOOP;
--END;

-- 교육생 로그인

-- 교육생 정보 입력 프로시저
CREATE OR REPLACE PROCEDURE PROC_INSERT_STUDENT(
    PNAME VARCHAR2, 
    PSSN VARCHAR2, 
    PTEL VARCHAR2,
    PACCOUNT VARCHAR2
)
IS
BEGIN
    INSERT INTO TBL_STUDENT(SEQ, NAME, SSN, TEL, ACCOUNT, REGDATE) VALUES(STUDENT_SEQ.NEXTVAL, PNAME, PSSN, PTEL, PTEL, DEFAULT);
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
END PROC_INSERT_STUDENT;

DROP PROCEDURE PROC_INSERT_STUDENT;

-- 학생 정보를 위한 뷰

CREATE OR REPLACE VIEW VW_STUDENT_INFO
AS
SELECT 
    ST.SEQ AS SEQ, ST.NAME AS  NAME, SUBSTR(ST.SSN, 8, 7) AS SSN, ST.TEL AS TEL, ST.REGDATE AS REGDATE, COUNT(AP.SEQ) AS COUNT
FROM TBL_STUDENT ST
INNER JOIN TBL_APPLY AP
ON ST.SEQ = AP.STUSEQ
WHERE ST.DELFLAG = 'Y' AND AP.DELFLAG = 'Y'
GROUP BY ST.SEQ, ST.NAME, SUBSTR(ST.SSN, 8, 7), ST.TEL, ST.REGDATE
;

DROP VIEW VW_STUDENT_INFO;

-- 학생 정보 출력 테스트
SELECT NAME, SSN, TEL, REGDATE, COUNT FROM VW_STUDENT_INFO WHERE SEQ = 1;

-- 교육생 정보 출력을 위한 뷰(상세)
CREATE OR REPLACE VIEW VW_STUDENT_INFO_DETAIL
AS
SELECT
    ST.NAME, CRS.NAME AS SUBJECT, OCRS.STARTDATE, OCRS.ENDDATE, OCLM.ROOMSEQ, 
    CASE
        WHEN AP.STATUS IS NULL THEN '수강중'
        WHEN AP.STATUS >= OCRS.ENDDATE THEN '수료'
        WHEN AP.STATUS > OCRS.STARTDATE AND AP.STATUS < OCRS.ENDDATE THEN '중도포기'
    END AS CURR_STATUS,
    AP.STATUS
FROM TBL_STUDENT ST
INNER JOIN TBL_APPLY AP
ON AP.STUSEQ = ST.SEQ
INNER JOIN TBL_OPEN_COURSE OCRS
ON OCRS.SEQ = AP.OCRSSEQ
INNER JOIN TBL_COURSE CRS
ON OCRS.CRSSEQ = CRS.SEQ
INNER JOIN TBL_OPEN_CLASSROOM OCLM
ON OCRS.SEQ = OCLM.OCRSSEQ
WHERE ST.DELFLAG = 'Y' AND AP.DELFLAG = 'Y' AND OCRS.DELFLAG = 'Y' AND CRS.DELFLAG = 'Y' AND OCLM.DELFLAG = 'Y'
;

DROP VIEW VW_STUDENT_INFO_DETAIL;

SELECT * FROM TBL_SUPPORT;
COMMIT;


ALTER TABLE TBL_ADMIN ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_APPLY ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_ATTEND_ADMIN ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_ATTEND_STUDENT ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_ATTEND_TEACHER ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_AVAIL_SUBJECT ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_BOOK ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_CLASSROOM ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_COURSE ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_COURSE_SUBJECT ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_COVID19_ADMIN ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_COVID19_STUDENT ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_COVID19_TEACHER ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_EMPLOYMENT ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_OPEN_CLASSROOM ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_OPEN_COURSE ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_OPEN_SUBJECT ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_POINT ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_SCORE ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_STUDENT ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_SUBJECT ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_SUPPORT ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_TEACHER ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_TEACHER_SCORE ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);
ALTER TABLE TBL_TEST ADD(DELFLAG VARCHAR2(3) DEFAULT 'Y' CHECK (DELFLAG IN('Y', 'N')) NOT NULL);

COMMIT;

SELECT * FROM TBL_ATTEND_STUDENT;
-- 개설 과정별 교육생 출결(과정, 인원 포함)
CREATE OR REPLACE VIEW VW_COUNT_ATTEND_STUDENT
AS
SELECT 
    CRS.NAME AS COURSE, CRS.SEQ AS CRS_SEQ, ST.NAME AS STUDENT, ST.SEQ AS STU_SEQ,
    COUNT(CASE
        WHEN TO_CHAR(ATS.INTIME, 'HH24:MI')  < '09:10' AND TO_CHAR(ATS.OUTTIME, 'HH24:MI')  > '17:50' THEN 1
    END) AS NORMAL,
    COUNT(CASE
        WHEN TO_CHAR(ATS.INTIME, 'HH24:MI')  > '09:10' THEN 1
    END) AS LATE,
    COUNT(CASE
        WHEN TO_CHAR(ATS.OUTTIME, 'HH24:MI')  < '17:50' THEN 1
    END) AS EARLYHOME,
    COUNT(CASE
        WHEN ATS.STATUS = '외출' THEN 1
    END) AS OUTING,
    COUNT(CASE
        WHEN ATS.STATUS = '병가' THEN 1
    END) AS ILLNESS,
    COUNT(CASE
        WHEN ATS.STATUS = '기타' THEN 1
    END) AS EXTRA,
    COUNT(CASE
        WHEN ATS.STATUS IS NULL AND ATS.INTIME IS NULL THEN 1
    END) AS UNAUTHORIZED
FROM TBL_OPEN_COURSE OCRS
INNER JOIN TBL_APPLY AP
ON OCRS.SEQ = AP.OCRSSEQ
INNER JOIN TBL_STUDENT ST
ON AP.STUSEQ = ST.SEQ
INNER JOIN TBL_ATTEND_STUDENT ATS
ON ATS.APPSEQ = AP.SEQ
INNER JOIN TBL_COURSE CRS
ON OCRS.CRSSEQ = CRS.SEQ
WHERE OCRS.DELFLAG = 'Y' AND AP.DELFLAG = 'Y' AND ST.DELFLAG = 'Y' AND ATS.DELFLAG = 'Y' AND CRS.DELFLAG = 'Y'
GROUP BY CRS.NAME, ST.NAME, ST.SEQ, CRS.SEQ
ORDER BY COURSE
;

SELECT * FROM VW_COUNT_ATTEND_STUDENT;

-- 과정별 학생의 출석을 보여주는 프로시저
CREATE OR REPLACE PROCEDURE PROC_COURSE_ATTEND(
    PCRSSEQ NUMBER,
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT
    FOR
    SELECT COURSE, CRS_SEQ, STUDENT, STU_SEQ, NORMAL, LATE, EARLYHOME, OUTING, ILLNESS, EXTRA, UNAUTHORIZED FROM VW_COUNT_ATTEND_STUDENT WHERE CRS_SEQ = PCRSSEQ;
END;

-- 과정별 학생 출석 프로시저 실행
DECLARE
    PRESULT SYS_REFCURSOR;
    VROW VW_COUNT_ATTEND_STUDENT%ROWTYPE;
BEGIN
    PROC_COURSE_ATTEND(3, PRESULT);
    LOOP
        FETCH PRESULT INTO VROW;
        EXIT WHEN PRESULT%NOTFOUND;
            DBMS_OUTPUT.PUT_LINE(VROW.COURSE || ' ' || VROW.STUDENT || ' ' || VROW.NORMAL || ' ' || VROW.LATE || ' ' || VROW.EARLYHOME || ' ' || VROW.OUTING || ' ' || VROW.ILLNESS || ' ' || VROW.EXTRA || ' ' || VROW.UNAUTHORIZED);
        
    END LOOP;
END;

-- 학생별 출결을 보여주는 프로시저
CREATE OR REPLACE PROCEDURE PROC_STUDENT_ATTEND(
    PSTUSEQ NUMBER,
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT
    FOR
    SELECT COURSE, CRS_SEQ, STUDENT, STU_SEQ, NORMAL, LATE, EARLYHOME, OUTING, ILLNESS, EXTRA, UNAUTHORIZED FROM VW_COUNT_ATTEND_STUDENT WHERE STU_SEQ = PSTUSEQ;
END;

-- 학생별 출결 보여주는 프로시저 실행
DECLARE
    PRESULT SYS_REFCURSOR;
    VROW VW_COUNT_ATTEND_STUDENT%ROWTYPE;
BEGIN
    PROC_STUDENT_ATTEND(1, PRESULT);
    LOOP
        FETCH PRESULT INTO VROW;
        EXIT WHEN PRESULT%NOTFOUND;
            DBMS_OUTPUT.PUT_LINE(VROW.COURSE || ' ' || VROW.STUDENT || ' ' || VROW.NORMAL || ' ' || VROW.LATE || ' ' || VROW.EARLYHOME || ' ' || VROW.OUTING || ' ' || VROW.ILLNESS || ' ' || VROW.EXTRA || ' ' || VROW.UNAUTHORIZED);
    END LOOP;
END;


-- 일자별 출결 현황
CREATE OR REPLACE VIEW VW_COUNT_DAYS
AS
SELECT 
    DAYS, 
     COUNT(CASE
        WHEN TO_CHAR(INTIME, 'HH24:MI')  < '09:10' AND TO_CHAR(OUTTIME, 'HH24:MI')  > '17:50' THEN 1
    END) AS NORMAL,
    COUNT(CASE
        WHEN TO_CHAR(INTIME, 'HH24:MI')  > '09:10' THEN 1
    END) AS LATE,
    COUNT(CASE
        WHEN TO_CHAR(OUTTIME, 'HH24:MI')  < '17:50' THEN 1
    END) AS EARLYHOME,
    COUNT(CASE
        WHEN STATUS = '외출' THEN 1
    END) AS OUTING,
    COUNT(CASE
        WHEN STATUS = '병가' THEN 1
    END) AS ILLNESS,
    COUNT(CASE
        WHEN STATUS = '기타' THEN 1
    END) AS EXTRA,
    COUNT(CASE
        WHEN STATUS IS NULL AND INTIME IS NULL THEN 1
    END) AS UNAUTHORIZED
FROM TBL_ATTEND_STUDENT
WHERE TBL_ATTEND_STUDENT.DELFLAG = 'Y'
GROUP BY DAYS
ORDER BY DAYS;

-- 일자별 출결 현황(기간 정해서)
CREATE OR REPLACE PROCEDURE PROC_ATTEND_DAYS(
    PSTARTDATE DATE,
    PENDDATE DATE,
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT
    FOR
    SELECT DAYS, NORMAL, LATE, EARLYHOME, OUTING, ILLNESS, EXTRA, UNAUTHORIZED FROM VW_COUNT_DAYS WHERE DAYS BETWEEN PSTARTDATE AND PENDDATE;
END;

DECLARE
    PRESULT SYS_REFCURSOR;
    VROW VW_COUNT_DAYS%ROWTYPE;
BEGIN
    PROC_ATTEND_DAYS('2020-03-10','2020-03-20', PRESULT);
    LOOP
        FETCH PRESULT INTO VROW;
        EXIT WHEN PRESULT%NOTFOUND;
         DBMS_OUTPUT.PUT_LINE(VROW.DAYS || ' '|| VROW.NORMAL || ' ' || VROW.LATE || ' '|| VROW.EARLYHOME || ' '|| VROW.OUTING || ' '|| VROW.ILLNESS || ' '|| VROW.EXTRA || ' '|| VROW.UNAUTHORIZED);
    END LOOP;
END;


SELECT * FROM VW_COUNT_DAYS;
-- 교사 개설 과목 출력을 위한 뷰
CREATE OR REPLACE VIEW VW_TEACHER_DETAIL
AS
SELECT 
    SUB.NAME AS SUBJECT, OSUB.STARTDATE AS SUBJECT_START, OSUB.ENDDATE AS SUBJECT_END,
    CRS.NAME AS COURSE, OCRS.STARTDATE AS COURSE_START, OCRS.ENDDATE AS COURSE_END,
    OCLM.ROOMSEQ AS ROOM_NUM, 
    BOOK.NAME AS BOOK, TCH.NAME AS TEACHER, TCH.SEQ AS TCH_SEQ, SUB.SEQ AS SUB_SEQ
FROM TBL_OPEN_COURSE OCRS
INNER JOIN TBL_COURSE CRS
ON CRS.SEQ = OCRS.CRSSEQ
INNER JOIN TBL_OPEN_CLASSROOM OCLM
ON OCRS.SEQ = OCLM.OCRSSEQ
INNER JOIN TBL_OPEN_SUBJECT OSUB
ON OCRS.SEQ = OSUB.OCRSSEQ
INNER JOIN TBL_AVAIL_SUBJECT ASUB
ON ASUB.SEQ = OSUB.AVAILSEQ
INNER JOIN TBL_SUBJECT SUB
ON SUB.SEQ = ASUB.SUBSEQ
INNER JOIN TBL_BOOK BOOK
ON BOOK.SEQ = ASUB.BOOKSEQ
INNER JOIN TBL_TEACHER TCH
ON TCH.SEQ = ASUB.TEASEQ
WHERE OCRS.DELFLAG = 'Y' AND CRS.DELFLAG = 'Y' AND OCLM.DELFLAG = 'Y' AND OSUB.DELFLAG = 'Y' AND ASUB.DELFLAG = 'Y'
AND SUB.DELFLAG = 'Y' AND BOOK.DELFLAG = 'Y' AND TCH.DELFLAG = 'Y' 
ORDER BY TCH.SEQ ASC, SUB.SEQ ASC
;
SELECT * FROM VW_TEACHER_DETAIL
WHERE TCH_SEQ = 1 AND SUB_SEQ = 2;


CREATE OR REPLACE PROCEDURE PROC_DELETE_OPEN_SUBJECT(
    PSEQ NUMBER
)
IS
BEGIN
    UPDATE TBL_OPEN_SUBJECT SET DELFLAG = 'N' WHERE SEQ = PSEQ;
    UPDATE TBL_SCORE SET DELFLAG = 'N' WHERE OSUBSEQ = PSEQ;
    UPDATE TBL_TEST SET DELFLAG = 'N' WHERE OSUBSEQ = PSEQ;
    UPDATE TBL_POINT SET DELFLAG = 'N' WHERE OSUBSEQ = PSEQ;
    UPDATE TBL_TEACHER_SCORE SET DELFLAG = 'N' WHERE OSUBSEQ = PSEQ;
END;


-- 교사별 코로나 조회 프로시저
CREATE OR REPLACE PROCEDURE PROC_COVID19_TEACHER_SHOW(
    PATTSEQ NUMBER,
    PRESULT OUT SYS_REFCURSOR 
)
IS
BEGIN
    OPEN PRESULT
    FOR
    SELECT SEQ, NAME, TEASEQ, DAYS, AMTEMP, PMTEMP FROM VW_TEACHER_COVID19 WHERE TEASEQ = PATTSEQ;
END;

-- 교사별 코로나 조회 프로시저 실행
DECLARE
    PRESULT SYS_REFCURSOR;
    VROW VW_TEACHER_COVID19%ROWTYPE;
BEGIN
    PROC_COVID19_TEACHER_SHOW(1, PRESULT);
    LOOP
        FETCH PRESULT INTO VROW;
        EXIT WHEN PRESULT%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(VROW.NAME || ' ' || VROW.DAYS || ' ' || VROW.AMTEMP || ' ' || VROW.PMTEMP);
    END LOOP;
END;


CREATE OR REPLACE PROCEDURE PROC_COVID19_DATE_TEACHER_SHOW(
    PSTARTDATE DATE,
    PLASTDATE DATE,
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT
    FOR
    SELECT SEQ, NAME, TEASEQ, DAYS, AMTEMP, PMTEMP FROM VW_TEACHER_COVID19 WHERE DAYS BETWEEN PSTARTDATE AND PLASTDATE ORDER BY TEASEQ ASC;
END;

DECLARE
    PRESULT SYS_REFCURSOR;
    VROW VW_TEACHER_COVID19%ROWTYPE;
BEGIN
    PROC_COVID19_DATE_TEACHER_SHOW('2020-03-10', '2020-03-20', PRESULT);
    LOOP
        FETCH PRESULT INTO VROW;
        EXIT WHEN PRESULT%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(VROW.NAME || ' ' || VROW.DAYS || ' ' || VROW.AMTEMP || ' ' || VROW.PMTEMP);
    END LOOP;
END;



SELECT * FROM VW_TEACHER_COVID19;






SET SERVEROUTPUT ON;