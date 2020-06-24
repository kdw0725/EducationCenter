/*
문서명 : DML-04
작성자 : 4조
작성일자 : 2020/06/02
프로젝트명 : Chick Education Center
프로그램명 : Chick Education Center
프로그램 설명 : 교육센터 시스템을 구현하기 위한 프로그램이다.

*/




---------------------------------------------------------------------------------------------------------
---------  관리자
---------------------------------------------------------------------------------------------------------
create or replace procedure proc_LoginAdmin (
    pid varchar2,
    ppw varchar2,
    presult out number
)
is
    pdel varchar2(3);
    accountcheck number;
begin
    select count(*) into accountcheck from tbl_admin where id = pid and pw = ppw;
    if accountcheck > 0 then
        select delflag into pdel from tbl_admin where id = pid and pw = ppw;
        if pdel = 'Y' then
            presult := 1;
        else
            presult := 0;
        end if;
    else
        presult := 0;
    end if;
end;

--1. 과목 추가

create or replace procedure proc_AddSubject(
    pname varchar2,
    pperiod number
)
is
begin
    insert into tbl_subject values (SUBJECT_SEQ.nextval, pname, pperiod, default);
end;



--2. 교재 추가

create or replace procedure proc_AddBook(
    pname varchar2,
    ppublisher varchar2,
    psubseq number
)
is
begin
    insert into tbl_book values (BOOK_SEQ.nextval, pname, ppublisher, psubseq, default);
end;



--3. 과정 추가

create or replace procedure proc_AddC(
    pname varchar2,     -- 과정 이름
    pperiod number       -- 과정 기간
)   
is
begin
    insert into tbl_course (seq, name, period, delflag) values (course_seq.nextVal, pname, pperiod, 'Y');
end proc_AddC;



--4. 과목 수정

create or replace procedure proc_UpdateSubject(
    pseq number,
    pname varchar2,
    pperiod number
)
is
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_subject where seq = pseq;
    if pdel = 'Y' then
        update tbl_subject set name = pname, period = pperiod where seq = pseq;
    else
        null;
    end if;
end;



--5. 교재 수정

create or replace procedure proc_UpdateBook(
    pseq number,
    pname varchar2,
    ppublisher varchar2
)
is
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_book where seq = pseq;
    if pdel = 'Y' then
        update tbl_book set name = pname, publisher = ppublisher where seq = pseq;
    else
        null;
    end if;
end;



--6. 과정 수정

create or replace procedure proc_updc(
    pname varchar2,
    pperiod number,
    pseq number
)
is 
begin
    update tbl_course set name = pname, period = pperiod where seq = pseq;
end;



--7. 과목 삭제

create or replace procedure proc_DeleteSubject(
    pseq number
)
is
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_subject where seq = pseq;
    if pdel = 'Y' then
        update tbl_subject set delflag = 'N' where seq = pseq;
        update tbl_course_subject set delflag = 'N' where subseq = pseq;
        update tbl_book set delflag = 'N' where subseq = pseq;
        update tbl_avail_subject set delflag = 'N' where subseq = pseq;
    else
        null;
    end if;
end;



--8. 교재 삭제

create or replace procedure proc_DeleteBook(
    pseq number
)
is
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_book where seq = pseq;
    if pdel = 'Y' then
        update tbl_book set delflag = 'N' where seq = pseq;
        update tbl_avail_subject set delflag = 'N' where bookseq = pseq;
    else
        null;
    end if;
end;



--9. 과정 삭제

create or replace procedure proc_DeleteCourse(
    pseq number
)
is
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_course where seq = pseq;
    if pdel = 'Y' then
        update tbl_course set delflag = 'N' where seq = pseq;
    else
        null;
    end if;

    select delflag into pdel from tbl_course_subject where crsseq = pseq;
    if pdel = 'Y' then
        update tbl_course_subject set delflag = 'N' where crsseq = pseq;
    else
        null;
    end if;
exception 
    when no_data_found then null;
end;



--1. 교사 추가

create or replace procedure proc_AddTeacher(
    pname varchar2, -- 교사 이름
    pid varchar2,   -- 교사 아이디
    pssn varchar2,  -- 교사 주민번호
    ptel varchar2  -- 교사 전화번호
)
is
begin
    insert into tbl_teacher values (TEACHER_SEQ.NEXTVAL, pname, pid, pssn, ptel, default);
end proc_AddTeacher;



--2. 교사 수정

create or replace procedure proc_UpdateTeacher (
    pseq number,    -- 교사 번호
    ptel varchar2  -- 교사 전화번호
)
is
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_teacher where seq = pseq;
    if pdel = 'Y' then
        update tbl_teacher set tel = ptel where seq = pseq;
    else
        null;
    end if;
end;



--3. 교사 삭제

create or replace procedure proc_DeleteTeacher(
    pseq number
)
is
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_teacher where seq = pseq;
    if pdel = 'Y' then
        update tbl_teacher set delflag = 'N' where seq = pseq;
    else
        null;
    end if;
end;



--4. 교사 강의 가능 과목 추가

create or replace procedure proc_AddAvailSubject(
    pteaseq number, -- 교사 번호
    psubseq number -- 과목 번호
)
is
    subcnt number;
    pbookseq number;
begin
    select count(subseq) into subcnt from tbl_avail_subject 
        where teaseq = pteaseq and subseq = psubseq;
    select seq into pbookseq from tbl_book where subseq = psubseq and rownum = 1 order by seq;
    if subcnt = 0 then
        insert into tbl_avail_subject values (AVAIL_SUBJECT_SEQ.NEXTVAL, pteaseq, psubseq, pbookseq, 'Y');
    else
        null;
    end if;
end proc_AddAvailSubject;


--1. 개설 과정 추가

create or replace procedure proc_AddCourseSubject(
    pcrsseq number,
    psubseq number
)
is
    pCdel varchar2(3);
    pSdel varchar2(3);
begin
    select delflag into pcdel from tbl_course where seq = pcrsseq;
    select delflag into psdel from tbl_subject where seq = psubseq;
    if pcdel = 'Y' and psdel = 'Y' then
        insert into tbl_course_subject values (COURSE_SUBJECT_SEQ.nextval, pcrsseq, psubseq, default);
    else
        null;
    end if;
end;



--2. 개설 과목 추가

create or replace procedure proc_AddOpenSubject (
    pocrsseq number,
    psubseq number,
    pstartdate date,
    penddate date,
    pbookseq number,
    pteaseq number
)
is
    pCdel varchar2(3);
    pSdel varchar2(3);
    pTdel varchar2(3);
    pBdel varchar2(3);
    passeq number;
begin
    select delflag into pcdel from tbl_open_course where seq = pocrsseq;
    select delflag into psdel from tbl_subject where seq = psubseq;
    select delflag into ptdel from tbl_teacher where seq = pteaseq;
    select delflag into pbdel from tbl_book where seq = pbookseq;
    if pcdel = 'Y' and psdel = 'Y' and ptdel = 'Y' and pbdel = 'Y' then
        passeq := AVAIL_SUBJECT_SEQ.nextval;
        insert into tbl_avail_subject values (passeq, pteaseq, psubseq, pbookseq, default);
        insert into tbl_open_subject values (OPEN_SUBJECT_SEQ.nextval, pstartdate, penddate, pocrsseq, passeq, default);
    else
        null;
    end if;

    commit;

exception
    when others then
        rollback;
end;

-----------------동적인 개설 과정 추가하기

create or replace procedure proc_AddOC(
    pseq number,            -- 해당 과정 번호
    pstartdate date,        --과정 시작날짜
    penddate date,          -- 과정 종료날짜
    pclassroom number        --과정 해당 교실
)
is
    pnum number := 0;
    pcourseseq number := 0;
begin
    select count(*) into pnum from tbl_course where seq = pseq;

    if pnum > 0 then
        select open_course_seq.nextVal into pcourseseq from dual;
        insert into tbl_open_course (seq, startdate, enddate, crsseq, delflag) values (pcourseseq, pstartdate, penddate, pseq, 'Y');
        insert into tbl_open_classroom values (open_classroom_seq.nextVal, pcourseseq, pclassroom, 'Y');
    else 
        dbms_output.put_line(pnum);
    end if;
end proc_AddOC;


--3. (교재 기초정보 확인 후) 과목에 대한 교재 추가

create or replace procedure proc_AddBook_inAvailSub(
    pteaseq number,
    pbookseq number
)
is
    psubseq number;
    ptdel number;
    pbdel number;
begin
    select subseq into psubseq from tbl_book where seq = pbookseq;
    select delflag into ptdel from tbl_teacher where seq = pteaseq;
    select delflag into pbdel from tbl_book where seq = pbookseq;
    if ptdel = 'Y' and pbdel = 'Y' then
        insert into tbl_avail_subject values (AVAIL_SUBJECT_SEQ.nextval, pteaseq, psubseq, pbookseq, default);
    else
        null;
    end if;

    commit;

exception
    when others then
        rollback;
end;


-- 정적인 강의실 정보 조회하기
create  or replace view vw_Seeclassroom
as
select seq  as CLASSNUM, maximum as CNT from tbl_classroom;


--------오픈 코스 조회하는 뷰
 create or replace view vw_SeeOC
as
select  a.* from (select distinct oc.seq as num,c.name as nameofcourse, c.period as period, 
        oc.startdate as "startdate", oc.enddate as "enddate", tblc.seq as "classroom",
        case
            when (select max(enddate) from tbl_open_subject where ocrsseq = oc.seq) > sysdate then '개설과목 등록 안됨'
            when (select max(enddate) from tbl_open_subject where ocrsseq = oc.seq)  <= sysdate then '개설과목 등록 완료'
            when (select max(enddate) from tbl_open_subject where ocrsseq = oc.seq) is null then '개설 과목 등록 안됨'
        end as "regcheck"
        ,
        (select count(*) from tbl_apply where ocrsseq = oc.seq) as stunum, oc.delflag
    
        from Tbl_open_course oc
            left outer join Tbl_course c on oc.crsseq = c.seq
                inner join tbl_open_classroom ocl on ocl.ocrsseq = oc.seq
                        inner join tbl_classroom tblc on ocl.roomseq = tblc.seq
                            left outer join  tbl_open_subject osj on oc.seq = osj.ocrsseq
                                left outer join tbl_avail_subject avails on osj.availseq = avails.seq
                                    left outer join tbl_subject ts on avails.subseq = ts.seq 
                                        where oc.delflag = 'Y'
                                        ORDER BY OC.seq ) a;


----------------------교육생 정보 확인


create or replace procedure proc_GetinfoOCS(
    pseq number,         --해당 과정 정보
    PRESULT OUT SYS_REFCURSOR 
)
is
begin
    open presult
    for
    select
                ts.name stuName,
                tc.name courseName,
                substr(ts.ssn, 8, 7) stuSsn,
                ts.tel stuTel,
                ts.regdate stuRegdate,
    case
        when ta.status is null then '과정진행중'
        when ta.status < ocr.enddate then '중도탈락'
        when ta.status >= ocr.enddate then '수료'
    end status,
    to_char(ta.status, 'yy-mm-dd') as statusdate
from tbl_student ts     -- 교육생 테이블
    inner join tbl_apply ta -- 수강신청 테이블
        on ts.seq = ta.stuseq
    inner join tbl_open_course ocr  -- 개설과정 테이블
        on ocr.seq = ta.ocrsseq
    inner join tbl_course tc    -- 과정 테이블
        on tc.seq = ocr.crsseq
    inner join tbl_open_classroom orm   -- 개설된 강의실 테이블
        on ocr.seq = orm.ocrsseq
    where tc.seq = pseq and tc.delflag = 'Y' 
    order by tc.name asc;
end proc_GetinfoOCS;
------------------------개설과목 정보


create or replace procedure proc_GetinfoOC(
    pseq number,         --해당 과정 정보
    PRESULT OUT SYS_REFCURSOR
    
)
is
begin
    OPEN PRESULT FOR
select rownum as num, ab.* from (select tc.name as coursename, ts.name as subjectname, os.startdate, os.enddate, bk.name as bkname, tt.name as teaname 
from tbl_open_course oc inner join tbl_open_subject os on oc.seq = os.ocrsseq
    inner join tbl_avail_subject avs on os.availseq = avs.seq
        inner join tbl_subject ts on avs.subseq = ts.seq
            inner join tbl_book bk on avs.bookseq = bk.seq
                inner join tbl_teacher tt on avs.teaseq = tt.seq
                    inner join tbl_course tc on tc.seq = oc.crsseq
                        where oc.seq = pseq AND OC.DELFLAG ='Y'
                            order by startdate) ab;
end proc_GetinfoOC;




--------------------------------개설 과정에 대한 수정
create or replace procedure proc_updOC(
    pseq number,
    pstartdate date,
    penddate date,
    pcrsseq number
) 
is
begin
    update tbl_open_course set startdate = pstartdate, enddate = penddate, crsseq = pcrsseq where seq = pseq; 

exception 
    when others then
        dbms_output.put_line('외래참조오류');
end proc_updOC;



-------------------개설 과정에 대한 삭제 + 개설된 강의실

create or replace procedure proc_DeleteOC(
    pseq number
)
is
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_OPEN_COURSE where seq = pseq;
    if pdel = 'Y' then
        update tbl_OPEN_COURSE set delflag = 'N' where seq = pseq;
    else
        null;
    end if;

    select delflag into pdel from tbl_OPEN_classroom where ocrsseq = pseq;
    if pdel = 'Y' then
        update tbl_OPEN_classroom set delflag = 'N' where ocrsseq = pseq;
    else
        null;
    end if;

end;






--1. 학생 추가

create or replace procedure proc_AddStudent(
    pname varchar2, -- 학생 이름
    pid varchar2,   -- 학생 아이디
    pssn varchar2,  -- 학생 주민번호
    ptel varchar2  -- 학생 전화번호
)
is
begin
    insert into tbl_student values (STUDENT_SEQ.NEXTVAL, pname, pid, pssn, ptel, null, default, default);
end proc_AddStudent;



--2. 학생 이름으로 검색

create or replace procedure proc_SearchStudent_fromName(
    pname varchar2, -- 학생 이름
    presult out sys_refcursor
)
is
begin
    open presult for
        select * from vw_ShowStudentInfo where stuName = pname;
end proc_SearchStudent_fromName;



--3. 학생 아이디로 검색

create or replace procedure proc_SearchStudent_fromID(
    pid varchar2, -- 학생 이름
    pstuName out varchar2,
    pcourseName out varchar2,
    pstartdate out date,
    penddate out date,
    proom out number,
    pstatus out varchar2,
    pstatusdate out date
)
is
begin
    select stuname, courseName, startdate, enddate, room, status, statusdate 
        into pstuName, pcourseName, pstartdate, penddate, proom, pstatus, pstatusdate
        from vw_ShowStudentInfo where stuid = pid;
end proc_SearchStudent_fromid;



--4. 학생 중도 탈락 처리

create or replace procedure proc_UpdateAppStatus(
    pstuseq in number
)
is
    scnt number;
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_apply where stuseq = pstuseq;
    if pdel = 'Y' then
        select count(status) into scnt from tbl_apply where stuseq = pstuseq;
        if scnt = 0 then
            update tbl_apply set status = sysdate where stuseq = pstuseq;
        else
            NULL;
        end if;
    else
        null;
    end if;
end proc_UpdateAppStatus;



--5. 학생 수정

create or replace procedure proc_UpdateStudent (
    pseq number,    -- 학생 번호
    ptel varchar2,  -- 학생 전화번호
    pacc varchar2   -- 학생 계좌
)
is
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_student where seq = pseq;
    if pdel = 'Y' then
        update tbl_student set tel = ptel, account = pacc where seq = pseq;
    else
        null;
    end if;
end;



11. 학생 삭제

create or replace procedure proc_DeleteStudent(
    pseq number
)
is
    pdel varchar2(3);
begin
    select delflag into pdel from tbl_student where seq = pseq;
    if pdel = 'Y' then
        update tbl_student set delflag = 'N' where seq = pseq;
    else
        null;
    end if;
end;



----------------------------국비지원 추가
create or replace procedure proc_addsupport(
    ptype varchar2,
    pdepdate date,
    pnumber number ---학생 번호 입력
)
is
    pseq number;
begin
   select seq into pseq from tbl_apply where stuseq = pnumber; 
    insert into tbl_support (seq, type, depdate, appseq, delflag) values (support_seq.nextVal, ptype, pdepdate, pseq, default);

    exception 
        when others then
        dbms_output.put_line('저장실패');

end proc_addsupport;



-----국비지원 특정 학생 번호 지정 조회
create or replace procedure proc_Seesp(
    pnum number,     --선택받을 학생 번호                      
    presult out sys_refcursor
)
is 
begin
    open presult 
    for 
    select * from vw_seesp where seq = pnum;
end;


-----국비지원 전체 보기 

create or replace view vw_SeeSp
as
select sup.seq as supseq, stu.seq, stu.name, stu.ssn, stu.account , sup.type, oc.startdate, oc.enddate, sup.depdate from tbl_support sup inner join tbl_apply appl on sup.appseq = appl.seq
    inner join tbl_student stu on stu.seq = appl.stuseq
        inner join tbl_open_course oc on oc.seq = appl.ocrsseq
 	WHERE SUP.DELFLAG = 'Y'
        		order by sup.appseq;


----------학생 코비드 기간 조회

create or replace procedure proc_findcovidstudate(
    pstartdays date,
    penddays date,
    presult out sys_refcursor
)
is
begin
        open presult 
        for 
        select * from vw_seecovidstu where days >= pstartdays and days <= penddays
        order by days desc;
end proc_findcovidstudate;


-----학생 코비드 보는 뷰

create or replace view vw_seecovidstu
as
select covids.seq, stu.name, stu.seq as stuseq, atts.days,covids.amtemp, covids.pmtemp  from tbl_covid19_student covids inner join tbl_attend_student atts on covids.attseq = atts.seq 
    inner join tbl_apply appl on atts.appseq = appl.seq 
        inner join tbl_student stu on appl.stuseq = stu.seq
	where appl.delflag = 'Y'            
		order by stu.name, days;


---특정 학생 코비드

create or replace procedure proc_findcovidstu(
    pseq number, --입력받을 특정 학생 번호
    presult out sys_refcursor
)
is
begin
        open presult 
        for 
        select * from vw_seecovidstu where stuseq = pseq;
end proc_findcovidstu;

-------학생 온도 저장
create or replace procedure proc_addcovstu(
    pamtemp number,
    ppmtemp number, 
    pstuseq number -- 학생 번호 받는 걸로 합시더
)
is
    pattseq number;
begin
    select max(att.seq) into pattseq from  tbl_attend_student att
        right outer join tbl_apply appl on att.appseq = appl.seq where appl.stuseq = pstuseq and att.delflag = 'Y';
    insert into tbl_covid19_student values (covid19_student_seq.nextVal, pamtemp, ppmtemp, pattseq, default);
end;


-------------교사 코비드 기간 조회
create or replace procedure proc_findcovidteadate(
    pstartdays date,
    penddays date,
    presult out sys_refcursor
)
is
begin
        open presult 
        for 
        select * from vw_seecovidtea where days >= pstartdays and days <= penddays;
end proc_findcovidteadate;

------------------특정 교사 코비드 조회

create or replace procedure proc_findcovidtea(
    pseq number, --입력받을 특정 교사 번호
    presult out sys_refcursor
)
is
begin
        open presult 
        for 
        select * from vw_seecovidtea where teaseq = pseq;
end proc_findcovidtea;
--------------교사 코비드 입력
create or replace procedure proc_addcovtea(
    pamtemp number,
    ppmtemp number, 
    pteaseq number, -- 교사 번호 받는 걸로 합시더
    presult out number
)
is
    pattseq number;
begin
    select max(att.seq) into pattseq  from tbl_attend_teacher att 
        where att.teaseq = pteaseq and att.delflag = 'Y';
    insert into tbl_covid19_teacher values (covid19_teacher_seq.nextVal, pamtemp, ppmtemp, pattseq, default);
    presult := 1;
exception
    when others then
    presult := 0;
end;

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

-- 교사 정보 출력    
SELECT * FROM VW_TEACHER_INFO WHERE SEQ = 1;
    
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










---------------------------------------------------------------------------------------------------------
---------  교사
---------------------------------------------------------------------------------------------------------
-- 교사 정보 vw

  CREATE OR REPLACE FORCE VIEW vw_showteacherinfo
  AS
  select
    tt.seq Tseq,    -- 선생님 번호
    tc.seq CSeq,    -- 과정 번호
    toc.seq OCSeq,  -- 개설 과정 번호
    ts.seq SSeq,    -- 과목 번호
    tos.seq OSSeq,  -- 개설 과목 번호
    tt.name teachername,   -- 선생님 이름
    ts.name nameSubject,    -- 과목 이름
    tos.startdate || ' ~ ' || tos.enddate termSubject,  -- 과목 기간
    tc.name nameCourse,     -- 과정 이름
    toc.startdate || ' ~ ' || toc.enddate termCourse,   -- 과정 기간
    tb.name bookname,       -- 책 이름
    tocr.roomseq roomNum,   -- 강의실 번호
    case
        when toc.startdate > sysdate then '강의예정'
        when toc.startdate < sysdate and toc.enddate > sysdate then '강의중'
        when toc.enddate < sysdate then '강의종료'
    end status              -- 강의 상태
from tbl_open_course toc -- 개설과정기간(+강의진행여부)
    inner join tbl_open_subject tos -- 개설과목 기간
        on toc.seq = tos.ocrsseq
    inner join tbl_course tc -- 과정명(개설된것만)
        on tc.seq = toc.crsseq
    inner join tbl_avail_subject tas
        on tas.seq = tos.availseq
    inner join tbl_subject ts -- 과목명(개설된것만)
        on ts.seq = tas.subseq
    inner join tbl_book tb -- 교재명
        on tb.seq = tas.bookseq
    inner join tbl_open_classroom tocr -- 강의실
        on tocr.ocrsseq = toc.seq
    right outer join tbl_teacher tt -- 교사명, 교사선택
        on tt.seq = tas.teaseq
where tt.delflag = 'Y'
order by toc.startdate, tos.startdate;

--------------------------------------------------------------------------------------------------------------------
-- 01. 교사  계정
--------------------------------------------------------------------------------------------------------------------
--T001 : 교사는 시스템의 일부 기능을 로그인 과정을 거친 후에 사용할 수 있다.
--T002 : 교사 계정은 사전에 관리자에 의해 데이터베이스에 등록된 것으로 간주한다.
-- 로그인
create or replace PROCEDURE PROC_TEACHER_LOGIN(
    PID IN VARCHAR2, -- ID
    PPW IN VARCHAR2, -- PW
    PRESULT OUT NUMBER, -- 1 OR 0
    PTEASEQ OUT NUMBER -- 교사번호
)
IS
BEGIN
    -- 
    SELECT COUNT(SEQ) INTO PRESULT FROM TBL_TEACHER WHERE ID = PID AND SUBSTR(SSN, 8) = PPW AND DELFLAG = 'Y';
    
    SELECT SEQ INTO PTEASEQ FROM TBL_TEACHER WHERE ID = PID AND SUBSTR(SSN, 8) = PPW AND DELFLAG = 'Y';
END PROC_TEACHER_LOGIN;

--------------------------------------------------------------------------------------------------------------------
-- 02. 강의 스케쥴 조회
--------------------------------------------------------------------------------------------------------------------
-- 메인 > 관리자로그인  > 관리자 메인 >강의 스케쥴 조회 > 과정 선택 > 과목 선택 > 학생 조회
--------------------------------------------------------------------------------------------------------------------
-- 메인
SELECT A.* 
FROM (SELECT DISTINCT TEACHERNAME,NAMECOURSE, TERMCOURSE, STATUS 
         FROM VW_SHOWTEACHERINFO 
         WHERE TSEQ = ?
         ORDER BY TERMCOURSE DESC) A 
WHERE ROWNUM = 1;

--T003 : 교사가 이용할 수 있는 기능에는 강의스케줄 조회, 배점 입출력, 성적 입출력 기능을 포함한다.            
--T004 : 교사는 자신의 강의 스케줄을 확인할 수 있어야 한다.
--T005 : 강의 스케줄은 강의 예정, 강의 중, 강의 종료로 구분해서 확인할 수 있어야 한다. 강의 진행 상태는 날짜를 기준으로 확인한다.

create or replace PROCEDURE PROC_TEACHER_CLASS_SCHEDULE( -- 
    PTEASEQ NUMBER, -- 교사 번호
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT FOR
 select b.* from 
(

 SELECT 
    ROWNUM,A.* 
FROM (SELECT 
                DISTINCT  
                OCSEQ AS OCRSSEQ, -- 과정 번호
                NAMECOURSE AS CRSNAME, -- 과정명
                TERMCOURSE AS CRSTERM, -- 과정 기간
                STATUS AS CRSSTATUS -- 과정 상태
            FROM vw_ShowTeacherInfo WHERE TSEQ = pteaseq order by termcourse desc)  A ) b  ;
END PROC_TEACHER_CLASS_SCHEDULE;


--T006 : 강의 스케줄 정보 출력 시 과목번호, 과정명, 과정기간(시작 년월일, 끝 년월일), 강의실과 
--         과목명, 과목기간(시작 년월일, 끝 년월일), 교재명, 교육생 등록 인원을 확인할 수 있어야 한다.
create or replace PROCEDURE PROC_TEACHER_SCHEDULE_DETAIL(
    PTEASEQ NUMBER, -- 교사번호
    POCSEQ NUMBER, -- 과정번호 
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT FOR

 select b.* from 
(

 SELECT 
    ROWNUM,A.*  FROM (
SELECT 
                    DISTINCT 
                    OCSEQ AS OCRSSEQ, -- 개설 과정 번호
                    OSSEQ, -- 개설 과목 번호
                    NAMECOURSE AS CRSNAME, -- 과정명
                    TERMCOURSE AS  CRSTERM, -- 과정기간
                    ROOMNUM, -- 강의실
                    NAMESUBJECT AS SUBNAME, --과목명
                    TERMSUBJECT AS SUBTERM, -- 과목기간
                    BOOKNAME, -- 교재명
                    (SELECT COUNT(*) FROM TBL_APPLY WHERE OCRSSEQ = OCSEQ) AS CNTSTU -- 등록된 학생인원
            FROM vw_ShowTeacherInfo WHERE TSEQ = PTEASEQ  AND OCSEQ = POCSEQ ORDER BY TERMSUBJECT) A ) B ;
END PROC_TEACHER_SCHEDULE_DETAIL;


--T007 : 특정 과목을 과목번호로 선택 시 해당 과정에 등록된 
--         교육생 정보(교육생 이름, 전화번호, 등록일, 수료 또는 중도탈락)을 확인할 수 있어야 한다.
create or replace PROCEDURE PROC_SUBJECT_STUDENTINFO(
    PTEASEQ NUMBER, -- 교사번호
    POSUBSEQ NUMBER, -- 과목 번호
    PRESULT OUT SYS_REFCURSOR    
)
IS
BEGIN
    OPEN PRESULT FOR
    SELECT         
        (SELECT NAME FROM TBL_STUDENT WHERE SEQ = APP.STUSEQ) AS STUNAME,
        (SELECT TEL FROM TBL_STUDENT WHERE SEQ = APP.STUSEQ) AS STUTEL,
        to_char((SELECT REGDATE FROM TBL_STUDENT WHERE SEQ = APP.STUSEQ),'yyyy-mm-dd') AS STUREGDATE,
        CASE
            WHEN APP.STATUS - OC.ENDDATE > 0 THEN '수료'
            WHEN APP.STATUS IS NULL THEN '진행중'        
        END AS STUSTATUS
    FROM TBL_APPLY APP -- 수강 신청
            INNER JOIN TBL_OPEN_COURSE OC -- 개설 과정
                ON OC.SEQ = APP.OCRSSEQ
                    LEFT OUTER JOIN TBL_OPEN_SUBJECT OS -- 개설 과목
                        ON OS.OCRSSEQ = OC.SEQ
                            INNER JOIN TBL_AVAIL_SUBJECT AVAS-- 강의 가능 과목
                                ON OS.AVAILSEQ = AVAS.SEQ                                                                            
                                    INNER JOIN TBL_OPEN_CLASSROOM OCROOM-- 개설된 강의실
                                        ON OCROOM.OCRSSEQ = OC.SEQ
    WHERE (SELECT SEQ FROM TBL_TEACHER WHERE SEQ = AVAS.TEASEQ) = pteaseq
            AND os.seq = posubseq  AND APP.STATUS IS NULL  -- 입력받은 특정과목 번호
    ORDER BY OC.STARTDATE DESC;
END PROC_SUBJECT_STUDENTINFO;


--------------------------------------------------------------------------------------------------------------------
-- 03. 배점 및 시험 관리
--------------------------------------------------------------------------------------------------------------------
-- 메인 > 관리자로그인  > 관리자 메인 > 배점 및 시험 관리 > 과정 선택 > 과목 선택 > 배점 및 시험날짜,파일 입력
--------------------------------------------------------------------------------------------------------------------
--T008 : 교사가 강의를 마친 과목에 대한 성적 처리를 위해서 배점 입출력을 할 수 있어야 한다.
--T009 : 교사는 자신이 강의를 마친 과목의 목록 중에서 
--T012 : 배점 등록이 안 된 과목인 경우는 과목 정보가 출력될 때 배점 부분은 null 값으로 출력한다.

--PROC_POINT_COURSE_VW 추가 필요

create or replace PROCEDURE PROC_TEACHER_SUBJECT_VW(
    PTEASEQ NUMBER, -- 교사 번호
    PRESULT OUT SYS_REFCURSOR 
)
IS
BEGIN
    OPEN PRESULT FOR
    SELECT 
        (SELECT SEQ FROM TBL_SUBJECT WHERE SEQ = AVAS.SUBSEQ) AS 과목번호,
        (SELECT NAME FROM TBL_SUBJECT WHERE SEQ = AVAS.SUBSEQ) AS 과목명,
        OS.ENDDATE,
        PO.WRITE,
        PO.PERFORMANCE,
        PO.OSUBSEQ,
        AVAS.SEQ

    FROM TBL_OPEN_SUBJECT OS
    LEFT OUTER JOIN TBL_POINT PO
        ON OS.SEQ = PO.OSUBSEQ
            INNER JOIN TBL_AVAIL_SUBJECT AVAS
                ON AVAS.SEQ = OS.AVAILSEQ

            WHERE  AVAS.TEASEQ = PTEASEQ AND OS.AVAILSEQ = AVAS.SEQ
            ORDER BY ENDDATE DESC;

END;

--T011 : 과목 목록 출력 시 과목번호, 과정명, 과정기간(시작 년월일, 끝 년월일), 강의실, 과목명, 과목기간(시작 년월일, 끝 년월일), 교재명, 출결, 필기, 실기 배점 등이 출력되고, 
-- 특정 과목을 과목번호로 선택 시 출결 배점, 필기 배점, 실기 배점, 시험 날짜, 시험 문제를 입력할 수 있는 화면으로 연결되어야 한다.

create or replace PROCEDURE PROC_INSERT_POINT_TEST(
    POSUBSEQ IN NUMBER,
    PWRITE IN NUMBER,
    PPERFORMANCE IN NUMBER,
    PATTEND IN NUMBER,
    PTESTDATE IN VARCHAR2,
    PTESTFILE IN VARCHAR2,
    PRESULT out NUMBER -- 입력 유무
)
IS
    PSUBENDDATE DATE; -- 과목 종료 날짜

BEGIN
    SELECT ENDDATE INTO PSUBENDDATE FROM TBL_OPEN_SUBJECT WHERE SEQ = POSUBSEQ;
    IF (SYSDATE - PSUBENDDATE) >= 0 AND PATTEND >= 20 AND PWRITE+PPERFORMANCE+PATTEND = 100 THEN
        INSERT INTO TBL_POINT VALUES
        (POINT_SEQ.NEXTVAL,PWRITE,PPERFORMANCE,PATTEND,POSUBSEQ,DEFAULT);
        INSERT INTO TBL_TEST VALUES
        (TEST_SEQ.NEXTVAL,TO_DATE(PTESTFILE,'yyyy-mm-dd'),PTESTDATE,POSUBSEQ,DEFAULT);
        PRESULT := 1;
        commit;
    ELSE
        PRESULT :=0;    
        rollback;
    END IF; 

END PROC_INSERT_POINT_TEST;



--------------------------------------------------------------------------------------------------------------------
-- 1. 교사 추가기능
--------------------------------------------------------------------------------------------------------------------
-- 메인 > 관리자로그인  > 관리자 메인 > 출결 관리 > 조회/기록
--------------------------------------------------------------------------------------------------------------------

-- 1) 교사는 자신의 출결을 출/퇴근 별로 기록할 수 있다.
-- 출근용
create or replace PROCEDURE PROC_TEACHER_ATTEND_RECORD(
    pteaseq number
)
IS
pdel varchar2(3);
BEGIN
    select delflag into pdel from tbl_teacher where seq = pteaseq;
    if pdel = 'Y' then
        insert into tbl_attend_teacher values (ATTEND_teacher_SEQ.nextval, sysdate, sysdate, null, null, pteaseq, default);
    else
        null;
    end if;
exception 
    when no_data_found then null;

END;

-- 퇴근용
create or replace procedure PROC_TEACHER_ATTEND_UPDATE(
    pteaseq number
)
is
    pdel varchar2(3);
    pseq number;
begin
    select delflag into pdel from tbl_teacher where seq = pteaseq;
    select max(seq) into pseq from tbl_attend_teacher where teaseq = pteaseq;
    if pdel = 'Y' then
        update tbl_attend_teacher set outtime = sysdate where seq = pseq;
    else
        null;
    end if;
exception 
    when no_data_found then null;
end;

-- 2) 교사는 자신의 출결을 확인할 수 있다.
create or replace PROCEDURE PROC_TEACHER_ATTEND(
    PNUM IN NUMBER,
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT FOR
    SELECT
        TEA.NAME AS TEANAME,
        AT.DAYS AS days, 
        AT.INTIME AS intime,
        AT.OUTTIME AS outtime,
        CASE
            WHEN TO_CHAR(AT.INTIME, 'HH24:MI') > '09:10' THEN '지각'
            WHEN TO_CHAR(AT.OUTTIME, 'HH24:MI') < '17:50' THEN '반차'
            WHEN TO_CHAR(AT.INTIME, 'HH24:MI') < '09:10' AND TO_CHAR(AT.OUTTIME, 'HH24:MI') > '17:50' THEN '정상'
        END AS conatt,
        AT.STATUS AS status
    FROM TBL_TEACHER TEA -- 교사
        INNER JOIN TBL_ATTEND_TEACHER AT -- 교사근태
            ON TEA.SEQ = AT.TEASEQ             
                        WHERE TEA.SEQ = PNUM --현재 로그인 한 교사 번호 
                            ORDER BY DAYS; 
END;


--------------------------------------------------------------------------------------------------------------------
-- 2. 교사 추가기능
--------------------------------------------------------------------------------------------------------------------
-- 메인 > 관리자로그인  > 관리자 메인 > 체온 조회 
--------------------------------------------------------------------------------------------------------------------
-- 1) 교사는 자신의 체온을 확인할 수 있다.
create or replace PROCEDURE PROC_COVID_TEACHER(
    PTEASEQ NUMBER, -- 교사번호
    PRESULT OUT SYS_REFCURSOR
)   
IS
BEGIN
    OPEN presult FOR
SELECT 
    (SELECT SEQ FROM TBL_TEACHER WHERE SEQ = AT.TEASEQ) AS TEASEQ, -- 교사 번호
    to_char(DAYS, 'yyyy-mm-dd') DAYS, -- 날짜
    (SELECT AMTEMP FROM TBL_COVID19_TEACHER WHERE ATTSEQ =  AT.SEQ) AS AMTEMP, --오전체온
    (SELECT PMTEMP FROM TBL_COVID19_TEACHER WHERE ATTSEQ =  AT.SEQ) AS PMTEMP --오후체온
FROM TBL_ATTEND_TEACHER AT
    WHERE TEASEQ = PTEASEQ ORDER BY DAYS DESC;
END;




-----------<교사 4. 성적 관리>------------

--//1.성적 입력//
--
--=======과정 선택 뷰=======

CREATE OR REPLACE FORCE VIEW "PROJECT"."VW_TEACOURSE" ("NUM", "TEACHERNUM", "TEACHERNAME", "COURSENUM", "COURSENAME", "COURSEPER", "COURSEDATE") AS 
  select rownum as num, a."TEACHERNUM",a."TEACHERNAME",a."COURSENUM",a."COURSENAME",a."COURSEPER",a."COURSEDATE" from 
(select DISTINCT t.seq as TeacherNum, t.name as TeacherName,oc.seq as CourseNum,cou.name as CourseName,cou.period as CoursePer, 
         (oc.STARTDATE || '~' || oc.ENDDATE) as CourseDate
         from tbl_teacher t                             --교사 테이블
            inner join tbl_avail_subject asub           --강의가능과목 테이블
                on t.seq = asub.teaseq  
                inner join tbl_open_subject osub        --개설과목 테이블
                    on asub.seq = osub.availseq
                    inner join tbl_open_course oc       --개설과정 테이블
                        on oc.seq = osub.ocrsseq
                        inner join tbl_course cou       --과정 테이블
                            on cou.seq = oc.crsseq)a;

--======과목 선택 뷰======

CREATE OR REPLACE FORCE VIEW "PROJECT"."VW_SEESUBJECTSTU" ("NUM", "SUBSEQ", "COUNAME", "COURSENUM", "COURSE", "CLR", "SUBNAME", "OSUBNUM", "SUBPER", "BOOKNAME", "TEANUM", "ATTSCO", "WRISCO", "PERFORSCO", "SCOREG") AS 
  select rownum as num, a."SUBSEQ",a."COUNAME",a."COURSENUM",a."COURSE",a."CLR",a."SUBNAME",a."OSUBNUM",a."SUBPER",a."BOOKNAME",a."TEANUM",a."ATTSCO",a."WRISCO",a."PERFORSCO",a."SCOREG" from(SELECT distinct
    SUB.SEQ AS SubSeq, C.NAME AS CouName,oc.seq as CourseNum, (OC.STARTDATE || '~' || OC.ENDDATE) AS Course, cls.seq as Clr,
    SUB.NAME AS SubName,os.seq as OSubNum, (OS.STARTDATE || '~' || OS.ENDDATE) AS SubPer,
    BO.NAME AS BookName,avs.teaseq as TeaNum,
    PO.ATTEND || '%' AS AttSco, PO.WRITE || '%' AS WriSco, PO.PERFORMANCE || '%' AS PerforSco,
                 case
                    when PO.write is null and
                        PO.performance is null and
                        PO.attend is null then '성적등록X'
                    else '성적등록O'
                end as ScoReg --성적등록여부
FROM TBL_STUDENT ST
    INNER JOIN TBL_APPLY AP --수강신청 조인
        ON ST.SEQ = AP.STUSEQ
            INNER JOIN TBL_OPEN_COURSE OC --개설 과정 조인
                ON AP.OCRSSEQ = OC.SEQ
                    INNER JOIN TBL_OPEN_SUBJECT OS --개설 과목 조인
                        ON OC.SEQ = OS.OCRSSEQ
                            LEFT OUTER JOIN TBL_POINT PO --배점 조인
                                ON OS.SEQ = PO.OSUBSEQ
                                    INNER JOIN TBL_TEST TES --시험 조인
                                        ON OS.SEQ = TES.OSUBSEQ
                                            INNER JOIN TBL_AVAIL_SUBJECT AVS --강의가능 과목 조인
                                                ON OS.AVAILSEQ = AVS.SEQ
                                                    INNER JOIN TBL_BOOK BO --교재 조인
                                                        ON AVS.BOOKSEQ = BO.SEQ
                                                            INNER JOIN TBL_SUBJECT SUB --과목 조인
                                                                ON AVS.SUBSEQ = SUB.SEQ
                                                                    INNER JOIN TBL_COURSE C --과정 조인
                                                                        ON OC.CRSSEQ = C.SEQ
                                                                        inner join tbl_open_classroom opcls --개설된 강의실 조인
                                                                        on opcls.ocrsseq = oc.seq
                                                                        inner join tbl_classroom cls                --강의실 조인
                                                                                on cls.seq =  opcls.roomseq
                                                                            ORDER BY C.name) a;



--======학생 번호 선택 뷰======

CREATE OR REPLACE FORCE VIEW "PROJECT"."VW_SCOSTUDENT" ("STUNUM", "STUDENTNAME", "SUBNUM", "OSUBNUM", "SUBNAME", "TEANUM", "ATTSCO", "PERFORSCO", "WRISCO", "SCOREG") AS 
  select s.seq as StuNum, s.name as StudentName,sub.seq as SubNum,os.seq as OSubNum, sub.name as SubName,asub.teaseq as TeaNum,
        
         (SELECT ATTEND FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ ) AS AttSco,
        (SELECT WRITE  FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ ) AS PerforSco,
        (SELECT PERFORMANCE  FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ) AS WriSco,
                case
                when (SELECT WRITE  FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ ) is null and
                    (SELECT PERFORMANCE  FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ ) is null and
                    (SELECT ATTEND  FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ ) is null then '성적등록X'
                else '성적등록O'
            end as ScoReg
    from TBL_STUDENT s --학생정보 테이블 
        inner join TBL_APPLY apl  --수강신청 테이블
            on s.seq = apl.stuseq 
        inner join TBL_OPEN_COURSE oc --개설과정 테이블
            on oc.seq = apl.ocrsseq
        inner join TBL_OPEN_SUBJECT os  --개설과목 테이블
            on os.ocrsseq = oc.seq
        inner join TBL_AVAIL_SUBJECT asub   --강의가능과목 테이블
            on asub.seq =os.availseq
        inner join TBL_SUBJECT sub          --과목 테이블
            on sub.seq = asub.subseq;





--======실기,필기 점수 입력 프로시저======

create or replace PROCEDURE proc_FailStuSco( 
    possub number,                   --개설과목번호
    psnum number,                    --학생 번호
   
    pwrite number,                  --필기성적
    pperfor number,                 --실기성적
    presult out number              --결과 반환 1 or 0
)
is
    pappseq number;                 --수강신청SEQ 가져오기
    posend date;                     --과목종료일
    pappend date;                    --학생근태
begin 
     select seq into pappseq from tbl_apply where stuseq = psnum;  --학생SEQ 수강신청SEQ로 변환

    select enddate into posend from tbl_open_subject where seq = possub;
    select status into pappend from tbl_apply where stuseq = psnum;

    if pappend is null then
        presult := 1;
        --insert into tbl_score VALUES(score_seq.nextVal, possub,psnum,pwrite,pperfor,presult,default);
         update  tbl_score set write=pwrite, performance= pperfor 
                where appseq =pappseq and osubseq=possub ;
        dbms_output.put_line('성적입력가능');

    elsif pappend is not null and posend <= pappend then
        presult := 1;
        --insert into tbl_score VALUES(score_seq.nextVal, possub,psnum,pwrite,pperfor,presult,default);
         update  tbl_score set write=pwrite, performance= pperfor
                where appseq =pappseq and osubseq=possub ;
            dbms_output.put_line('성적입력가능');

    else 
        presult := 0;
        dbms_output.put_line('성적입력불가능');

    end if;
end;




--======출결 점수 자동입력 프로시저======

create or replace PROCEDURE PROC_STUDENT_ATTEND_AUTO(
    PSTUSEQ NUMBER,
    POSUBSEQ NUMBER
)
IS
VNORMAL NUMBER;
VLATE NUMBER;
VEARLYHOME NUMBER;
VOUTING NUMBER;
VILLNESS NUMBER;
VEXTRA NUMBER;
VUNAUTHORIZED NUMBER;
VATTENDSCORE NUMBER;
VAPPSEQ NUMBER;
VOSUBSEQ NUMBER;

BEGIN
    SELECT 
    NORMAL, LATE, EARLYHOME, OUTING, ILLNESS, EXTRA, UNAUTHORIZED, APPSEQ, OSUBSEQ
    INTO
    VNORMAL, VLATE, VEARLYHOME, VOUTING, VILLNESS, VEXTRA, VUNAUTHORIZED, VAPPSEQ, VOSUBSEQ
    FROM VW_COUNT_SUBJECT_ATTEND WHERE STUSEQ = PSTUSEQ AND OSUBSEQ = POSUBSEQ;

    UPDATE TBL_SCORE SET ATTEND =  FLOOR(100 - ((VILLNESS + VEXTRA + VUNAUTHORIZED + FLOOR((VLATE + VEARLYHOME)/3)) /(VNORMAL + VLATE + VEARLYHOME + VILLNESS + VEXTRA + VUNAUTHORIZED)))
    WHERE APPSEQ = VAPPSEQ AND OSUBSEQ = VOSUBSEQ;
END;



--//2. 성적 확인/출력//
--
--/2-1. 학생별 점수 확인/
--
--과정, 과목, 학생번호 선택 위와 동일한 뷰 사용
--
--
--======시험 점수 출력 프로시저======
create or replace procedure proc_Score_Out(
     pnum number ,     --선택받을 과목 번호                     
     psnum number ,      --선택할 학생 번호
    
    
    presult out sys_refcursor

)
is
begin
    open presult 
    for select rownum as num, a.* from(SELECT 
    ST.NAME AS StuName,
    OS.SEQ AS OSubNum, SUB.NAME AS SubName, OS.STARTDATE AS SubStart, OS.ENDDATE AS SubEnd,
    BO.NAME AS Book, TEA.NAME AS TeaName,
    (SELECT ATTEND || '점' FROM TBL_SCORE WHERE APPSEQ = AP.SEQ AND OSUBSEQ = OS.SEQ) AS AttSco,
    (SELECT WRITE || '점' FROM TBL_SCORE WHERE APPSEQ = AP.SEQ AND OSUBSEQ = OS.SEQ) AS WritrSco,
    (SELECT PERFORMANCE || '점' FROM TBL_SCORE WHERE APPSEQ = AP.SEQ AND OSUBSEQ = OS.SEQ) AS PerforSco
    FROM TBL_STUDENT ST
        INNER JOIN TBL_APPLY AP --수강신청 조인
            ON ST.SEQ = AP.STUSEQ
                INNER JOIN TBL_OPEN_COURSE OC --개설 과정 조인
                    ON AP.OCRSSEQ = OC.SEQ
                        INNER JOIN TBL_OPEN_SUBJECT OS --개설 과목 조인
                            ON OC.SEQ = OS.OCRSSEQ
                                LEFT OUTER JOIN TBL_POINT PO --배점 조인
                                    ON OS.SEQ = PO.OSUBSEQ
--                                        INNER JOIN TBL_SCORE SCO --성적 조인
--                                            ON AP.SEQ = SCO.APPSEQ
                                                INNER JOIN TBL_TEST TES --시험 조인
                                                    ON OS.SEQ = TES.OSUBSEQ
                                                        INNER JOIN TBL_AVAIL_SUBJECT AVS --강의가능 과목 조인
                                                            ON OS.AVAILSEQ = AVS.SEQ
                                                                INNER JOIN TBL_TEACHER TEA --교사 조인
                                                                    ON AVS.TEASEQ = TEA.SEQ
                                                                        INNER JOIN TBL_BOOK BO --교재 조인
                                                                            ON AVS.BOOKSEQ = BO.SEQ
                                                                                INNER JOIN TBL_SUBJECT SUB --과목 조인
                                                                                    ON AVS.SUBSEQ = SUB.SEQ
                                                                                WHERE ST.SEQ = psnum AND OS.SEQ = pnum) a;  --학생번호,과목번호 선택


end; 



--
--/2-2. 과목별 성적확인/
--
--과목 선택  뷰(vw_SeeSubjectStu) 사용
--
--======과목별 학생 목록 출력 프로시저======

create or replace procedure proc_SubStuSco (
    pnum number,            --과목번호 선택
    presult out sys_refcursor 
)
is
begin 
    open presult 
    for select rownum as num, a.* from(select DISTINCT sub.seq as SubNum,sub.name as SubName, (osub.STARTDATE || '~' || osub.ENDDATE) AS SubTerm,s.seq as StuNum,
    s.name as StudentName, s.tel as StudentTel,
        case
            when oc.enddate > apl.status then '수료완료'
            when oc.enddate < apl.status then '중도탈락'
            when apl.status is null then '수강중'
        end as subAndstu ,
    sco.write as WriteSco, sco.performance as PerforSco, sco.attend as AttSco
    from TBL_STUDENT s                  --학생 테이블
        inner join TBL_APPLY apl           --수강신청 테이블
            on s.seq = apl.stuseq
        inner join TBL_ATTEND_STUDENT ats   --학생근태 테이블
            on ats.appseq = apl.seq
        inner join TBL_SCORE sco            --성적 테이블
            on sco.appseq = apl.seq
        inner join TBL_OPEN_SUBJECT osub       --개설과목 테이블
            on osub.seq = sco.osubseq
        inner join  TBL_AVAIL_SUBJECT  asub     --강의가능과목 테이블
            on asub.seq = osub.availseq
        inner join TBL_SUBJECT sub              --과목 테이블
            on sub.seq = asub.subseq
        inner join TBL_COURSE_SUBJECT csub      --과정별과목 테이블
            on sub.seq = csub.subseq
        inner join TBL_COURSE cr                --과정 테이블
            on cr.seq = csub.crsseq
        inner join  TBL_OPEN_COURSE oc          --개설과정 테이블
            on oc.crsseq = cr.seq where sub.seq=pnum) a;

end;



--/2-3.전체 교육생 성적등록 여부 확인/
--
--
--======전체 교육생 성정등록여부 뷰======

create or replace VIEW vw_ScoStudent
as
select s.seq as StuNum, s.name as StudentName,sub.seq as SubNum,os.seq as OSubNum, sub.name as SubName,asub.teaseq as TeaNum,
        
         (SELECT ATTEND FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ ) AS AttSco,
        (SELECT WRITE  FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ ) AS PerforSco,
        (SELECT PERFORMANCE  FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ) AS WriSco,
                case
                when (SELECT WRITE  FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ ) is null and
                    (SELECT PERFORMANCE  FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ ) is null and
                    (SELECT ATTEND  FROM TBL_SCORE WHERE APPSEQ = apl.SEQ AND OSUBSEQ = OS.SEQ ) is null then '성적등록X'
                else '성적등록O'
            end as ScoReg
    from TBL_STUDENT s --학생정보 테이블 
        inner join TBL_APPLY apl  --수강신청 테이블
            on s.seq = apl.stuseq 
        inner join TBL_OPEN_COURSE oc --개설과정 테이블
            on oc.seq = apl.ocrsseq
        inner join TBL_OPEN_SUBJECT os  --개설과목 테이블
            on os.ocrsseq = oc.seq
        inner join TBL_AVAIL_SUBJECT asub   --강의가능과목 테이블
            on asub.seq =os.availseq
        inner join TBL_SUBJECT sub          --과목 테이블
            on sub.seq = asub.subseq; 







---------------<5. 학생 관리>--------------

--//1. 중도 탈락 확인 여부//
--
--/1-1. 중도 탈락 여부 확인/
--
--======전체 교육생 목록 출력 후 중도탈락 여부 확인 뷰======

create or REPLACE view vw_FailStuDate
as
select rownum as num, a.* from(select DISTINCT s.seq as StudentNum, s.name as StudentName, apl.status as StudentFail
    from TBL_STUDENT s                  --학생정보 테이블 
        inner join TBL_APPLY apl        --수강신청 테이블
            on s.seq = apl.stuseq 
        inner join TBL_OPEN_COURSE oc      --개성과정 테이블
            on oc.seq = apl.ocrsseq
        inner join TBL_OPEN_SUBJECT os      --개설과목 테이블
             on os.ocrsseq = oc.seq
--        inner join TBL_AVAIL_SUBJECT asub   --강의가능과목 테이블
--            on asub.seq = os.availseq
                 where oc.enddate > apl.status
             
        order by apl.status) a;



--/1-2. 중도 탈락일 확인/
--
--======t018 중도탈락 학생 목록과 탈락일 출력 뷰======
--
--위와 동일한 vw_FailStuDate 뷰 사용
--
--
--
--
--//2. 내 강의 교육생 출결 확인//
--
--/2-1. 모든 교육생 출결조회/
--
--과정 목록 위와 동일한 뷰 사용
--
--======강의 듣는 학생 출결 출력 뷰======

create or replace VIEW vw_CouStudent
as
select rownum as num, a.* from 
(select DISTINCT  t.seq as TeacherNum,oc.crsseq as CourseNum, cou.name as CourseName,s.seq as StuNum, s.name as StudentName, ats.days as Day, to_char(ats.intime, 'hh24:mi:ss') as InTime, 
                 to_char(ats.outtime, 'hh24:mi:ss') as OutTime,  t.seq as Tecnum,
                 case
                    when to_char(ats.intime, 'hh24:mi') > '09:10' then '지각'
                    when to_char(ats.outtime, 'hh24:mi') < '05:50' then '조퇴'
                    when to_char(ats.intime, 'hh24:mi') < '09:10' and
                         to_char(ats.outtime, 'hh24:mi') > '05:50' then '정상'
                 end as StuCondition ,
                 ats.STATUS as Status
    from TBL_STUDENT s                      --학생 테이블
        inner join TBL_APPLY apl           --수강신청 테이블
            on s.seq = apl.stuseq
        inner join TBL_ATTEND_STUDENT ats   --학생근태 테이블
            on ats.APPSEQ = apl.seq
        inner join TBL_OPEN_COURSE  oc      --개설과정 테이블
            on apl.ocrsseq = oc.seq
        inner join tbl_course cou           --과정 테이블
            on cou.seq = oc.crsseq
        inner join TBL_OPEN_SUBJECT os      --개설과목 테이블
            on oc.seq = os.ocrsseq
        inner join TBL_AVAIL_SUBJECT avs    --강의가능과목 테이블 
            on os.AVAILSEQ = avs.seq
        inner join TBL_TEACHER t            --교사 테이블
            on avs.teaseq = t.seq
                order by days) a;





--/2-2.출결 기간별 조회/
--
--과정 동일 뷰(vw_TeaCourse) 사용
--
--======출결 기간별 조회 뷰======

create or replace PROCEDURE PROC_TEACHER_STUATTEND(
    PNUM IN NUMBER,                         --개설과정 번호 
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT FOR
    SELECT distinct
        ST.NAME,
        TO_CHAR(AST.DAYS, 'YYYY-MM-DD') AS DAYS,
        TO_CHAR(AST.INTIME, 'HH24:MI') AS INTIME,
        TO_CHAR(AST.OUTTIME, 'HH24:MI') AS OUTTIME,
        CASE
            WHEN TO_CHAR(AST.INTIME, 'HH24:MI') > '09:10' THEN '지각'
            WHEN TO_CHAR(AST.OUTTIME, 'HH24:MI') < '17:50' THEN '조퇴'
            WHEN TO_CHAR(AST.INTIME, 'HH24:MI') <= '09:10' AND
                TO_CHAR(AST.OUTTIME, 'HH24:MI') >= '17:50' THEN '정상'
        END AS STATUS,
       AST.STATUS AS NOTE
    FROM TBL_STUDENT ST         --학생 테이블
        INNER JOIN TBL_APPLY AP --수강신청 조인
            ON ST.SEQ = AP.STUSEQ
                INNER JOIN TBL_ATTEND_STUDENT AST --학생 근태 조인
                    ON AP.SEQ = AST.APPSEQ
                    inner join tbl_open_course oc       --개설과정 조인
                        on oc.seq = ap.ocrsseq
                        inner join tbl_open_subject osub            --개설과목 조인
                         on osub.ocrsseq = oc.seq
                         inner join tbl_avail_subject asub          --강의가능과목 조인
                            on asub.seq = osub.availseq
                        WHERE oc.seq=pnum AND ST.DELFLAG = 'Y' AND AP.DELFLAG = 'Y' AND AST.DELFLAG = 'Y'
                            ORDER BY DAYS DESC;
END;






--/2-3.특정(과정/인원) 출결 조회/
--
--======특정 학생 출결 조회 뷰======
create or replace VIEW vw_CouStudent
as
select rownum as num, a.* from 
(select DISTINCT  t.seq as TeacherNum,oc.crsseq as CourseNum, cou.name as CourseName,s.seq as StuNum, s.name as StudentName, ats.days as Day, to_char(ats.intime, 'hh24:mi:ss') as InTime, 
                 to_char(ats.outtime, 'hh24:mi:ss') as OutTime,  t.seq as Tecnum,
                 case
                    when to_char(ats.intime, 'hh24:mi') > '09:10' then '지각'
                    when to_char(ats.outtime, 'hh24:mi') < '05:50' then '조퇴'
                    when to_char(ats.intime, 'hh24:mi') < '09:10' and
                         to_char(ats.outtime, 'hh24:mi') > '05:50' then '정상'
                 end as StuCondition ,
                 ats.STATUS as Status
    from TBL_STUDENT s                      --학생 테이블
        inner join TBL_APPLY apl           --수강신청 테이블
            on s.seq = apl.stuseq
        inner join TBL_ATTEND_STUDENT ats   --학생근태 테이블
            on ats.APPSEQ = apl.seq
        inner join TBL_OPEN_COURSE  oc      --개설과정 테이블
            on apl.ocrsseq = oc.seq
        inner join tbl_course cou           --과정 테이블
            on cou.seq = oc.crsseq
        inner join TBL_OPEN_SUBJECT os      --개설과목 테이블
            on oc.seq = os.ocrsseq
        inner join TBL_AVAIL_SUBJECT avs    --강의가능과목 테이블 
            on os.AVAILSEQ = avs.seq
        inner join TBL_TEACHER t            --교사 테이블
            on avs.teaseq = t.seq
                order by days) a;



--/2-4.학생 근태 상황 조회/
--
--======모든 학생 출결,근태 상황 조회 뷰======

create or replace VIEW vw_AllAtt
as
select rownum as num, a.* from 
(select DISTINCT s.seq as StuNum,s.name as StuName, days as Days, to_char(ats.intime, 'hh24:mi:ss') as InTime, 
                 to_char(ats.outtime, 'hh24:mi:ss') as OutTime,
                 case
                    when to_char(ats.intime, 'hh24:mi') > '09:10' then '지각'
                    when to_char(ats.outtime, 'hh24:mi') < '05:50' then '조퇴'
                    when to_char(ats.intime, 'hh24:mi') < '09:10' and
                         to_char(ats.outtime, 'hh24:mi') > '05:50' then '정상'
                 end as StuCondition,
                 ats.STATUS  as StuStatus
    from TBL_STUDENT s                      --학생 테이블
        inner join TBL_APPLY apl           --수강신청 테이블
            on s.seq = apl.stuseq
        inner join TBL_ATTEND_STUDENT ats   --학생근태 테이블
            on ats.APPSEQ = apl.seq
        inner join TBL_OPEN_COURSE  oc      --개설과정 테이블
            on apl.ocrsseq = oc.seq
        inner join TBL_OPEN_SUBJECT os      --개성과목 테이블
            on oc.seq = os.ocrsseq
        inner join TBL_AVAIL_SUBJECT avs    --강의가능과목 테이블 
            on os.AVAILSEQ = avs.seq
        inner join TBL_TEACHER t            --교사 테이블
            on avs.teaseq = t.seq
                order by days) a;



---------------------------------------------------------------------------------------------------------
---------  교육생
---------------------------------------------------------------------------------------------------------


--------------------------------------------------------------------------------------------------------------------
-- 01. 교육생 계정
--------------------------------------------------------------------------------------------------------------------

-- S001 : 교육생은 시스템의 일부 기능을 로그인 과정을 거친 후에 사용할 수 있다.
-- S002 : 교육생 계정은 사전에 관리자에 의해 데이터베이스에 등록된 것으로 간주한다.

-- 로그인
"SELECT SEQ, NAME, ID, SSN, TEL, ACCOUNT, REGDATE, DELFLAG FROM TBL_STUDENT WHERE ID = ? AND SUBSTR(SSN, 8, 7) = ?";

--------------------------------------------------------------------------------------------------------------------
-- 02. 성적 조회
--------------------------------------------------------------------------------------------------------------------
-- 메인 > 교육생 > 교육생 메뉴 > 성적 조회
--------------------------------------------------------------------------------------------------------------------

-- S005 : 교육생이 로그인에 성공하면 교육생 개인의 정보와 교육생이 수강한 과정명,
-- 과정기간(시작 년월일, 끝 년월일), 강의실이 타이틀로 출력된다.

-- 교육생 메인
-- 메인 PROCEDURE
CREATE OR REPLACE PROCEDURE PROC_STUDENT_MAIN(
    PNUM IN NUMBER,                     --학생번호
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT FOR
    SELECT
    DISTINCT
        ST.NAME, ST.ID, ST.SSN, ST.TEL, ST.ACCOUNT,
        C.NAME AS COURSENAME, TO_CHAR(OC.STARTDATE, 'YY/MM/DD') AS STARTDATE, TO_CHAR(OC.ENDDATE, 'YY/MM/DD') AS ENDDATE,
        OCL.ROOMSEQ AS ROOM, SU.TYPE
    FROM TBL_STUDENT ST
        INNER JOIN TBL_APPLY AP --수강신청 조인
            ON ST.SEQ = AP.STUSEQ
                INNER JOIN TBL_OPEN_COURSE OC --개설 과정 조인
                    ON AP.OCRSSEQ = OC.SEQ
                        INNER JOIN TBL_COURSE C --과정 조인
                            ON OC.CRSSEQ = C.SEQ
                                INNER JOIN TBL_OPEN_CLASSROOM OCL --개설된 강의실 조인
                                    ON OC.SEQ = OCL.OCRSSEQ
                                        INNER JOIN TBL_SUPPORT SU
                                            ON AP.SEQ = SU.APPSEQ
                                        WHERE ST.SEQ = PNUM; -- 현재 로그인 한 교육생 번호 변수
END;

-- S003 : 교육생이 이용할 수 있는 기능에는 성적 조회 기능을 포함한다.
-- S006 : 성적 정보는 과목별로 목록 형태로 출력된다.
-- S007 : 출력될 정보는 과목번호, 과목명, 과목기간(시작 년월일, 끝 년월일), 
-- 교재명, 교사명, 과목별 배점 정보(출결, 필기, 실기 배점), 과목별 성적 정보(출결, 필기, 실기 점수), 
-- 과목별 시험날짜, 시험문제가 출력되어야 한다.
-- S008 : 성적이 등록되지 않은 과목이 있는 경우 과목 정보는 출력되고 점수는 null 값으로 출력되도록 한다.

-- 교육생 성적 조회
-- 과목번호, 과목명, 과목기간(시작, 끝), 교재명, 교사명,
-- 과목별 배점정보(출결, 필기, 실기), 과목별 성적정보(출결, 필기, 실기), 과목별 시험날짜, 시험문제
-- 성적 PROCEDURE
CREATE OR REPLACE PROCEDURE PROC_STUDENT_SCORE_INFO(
    PNUM IN NUMBER,                     --학생번호
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT FOR
    SELECT 
        ST.NAME,
        SUB.SEQ, SUB.NAME AS SUBJECT, TO_CHAR(OS.STARTDATE, 'YY/MM/DD') AS STARTDATE, TO_CHAR(OS.ENDDATE, 'YY/MM/DD') AS ENDDATE,
        LPAD(BO.NAME, 40, ' ') AS BOOK, TEA.NAME AS TEACHER,
        CASE
        WHEN PO.ATTEND IS NOT NULL THEN PO.ATTEND || '%'
        WHEN PO.ATTEND IS NULL THEN NULL
    END AS ATPERCENT,
    CASE
        WHEN PO.WRITE IS NOT NULL THEN PO.WRITE || '%'
        WHEN PO.WRITE IS NULL THEN NULL
    END AS WRPERCENT,
    CASE
        WHEN PO.PERFORMANCE IS NOT NULL THEN PO.PERFORMANCE || '%'
        WHEN PO.PERFORMANCE IS NULL THEN NULL
    END AS PEPERCENT,
        (SELECT ATTEND FROM TBL_SCORE WHERE APPSEQ = AP.SEQ AND OSUBSEQ = OS.SEQ) AS ATSCORE,
        (SELECT WRITE FROM TBL_SCORE WHERE APPSEQ = AP.SEQ AND OSUBSEQ = OS.SEQ) AS WRSCORE,
        (SELECT PERFORMANCE FROM TBL_SCORE WHERE APPSEQ = AP.SEQ AND OSUBSEQ = OS.SEQ) AS PESCORE
    FROM TBL_STUDENT ST
        INNER JOIN TBL_APPLY AP --수강신청 조인
            ON ST.SEQ = AP.STUSEQ
                INNER JOIN TBL_OPEN_COURSE OC --개설 과정 조인
                    ON AP.OCRSSEQ = OC.SEQ
                        INNER JOIN TBL_OPEN_SUBJECT OS --개설 과목 조인
                            ON OC.SEQ = OS.OCRSSEQ
                                LEFT OUTER JOIN TBL_POINT PO --배점 조인
                                    ON OS.SEQ = PO.OSUBSEQ
--                                        INNER JOIN TBL_SCORE SCO --성적 조인
--                                            ON AP.SEQ = SCO.APPSEQ
                                                INNER JOIN TBL_TEST TES --시험 조인
                                                    ON OS.SEQ = TES.OSUBSEQ
                                                        INNER JOIN TBL_AVAIL_SUBJECT AVS --강의가능 과목 조인
                                                            ON OS.AVAILSEQ = AVS.SEQ
                                                                INNER JOIN TBL_TEACHER TEA --교사 조인
                                                                    ON AVS.TEASEQ = TEA.SEQ
                                                                        INNER JOIN TBL_BOOK BO --교재 조인
                                                                            ON AVS.BOOKSEQ = BO.SEQ
                                                                                INNER JOIN TBL_SUBJECT SUB --과목 조인
                                                                                    ON AVS.SUBSEQ = SUB.SEQ
                                                                                
    WHERE ST.SEQ = PNUM AND ST.DELFLAG = 'Y' AND AP.DELFLAG = 'Y' AND OC.DELFLAG = 'Y' AND OS.DELFLAG = 'Y'
        AND TES.DELFLAG = 'Y' AND AVS.DELFLAG = 'Y' AND TEA.DELFLAG = 'Y' AND BO.DELFLAG = 'Y' AND SUB.DELFLAG = 'Y'
            ORDER BY OS.STARTDATE;
END;

--------------------------------------------------------------------------------------------------------------------
-- 03. 출결 관리 및 출결 조회
--------------------------------------------------------------------------------------------------------------------

-- S010 : 본인의 출결 현황을 기간별(전체, 월, 일) 조회할 수 있어야 한다.
-- S011 : 다른 교육생의 현황은 조회할 수 없다.
-- S012 : 모든 출결 조회는 근태 상황을 구분할 수 있어야 한다.(정상, 지각, 조퇴, 외출, 병가, 기타)

-- 교육생 출결 조회
-- 출결 조회 PROCEDURE
CREATE OR REPLACE PROCEDURE PROC_STUDENT_ATTEND(
    PNUM IN NUMBER,
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT FOR
    SELECT
        ST.NAME,
        TO_CHAR(AST.DAYS, 'YYYY-MM-DD') AS DAYS,
        TO_CHAR(AST.INTIME, 'HH24:MI') AS INTIME,
        TO_CHAR(AST.OUTTIME, 'HH24:MI') AS OUTTIME,
        CASE
            WHEN TO_CHAR(AST.INTIME, 'HH24:MI') > '09:10' THEN '지각'
            WHEN TO_CHAR(AST.OUTTIME, 'HH24:MI') < '17:50' THEN '조퇴'
            WHEN TO_CHAR(AST.INTIME, 'HH24:MI') <= '09:10' AND
                TO_CHAR(AST.OUTTIME, 'HH24:MI') >= '17:50' THEN '정상'
        END AS STATUS,
        TO_CHAR(AST.INTIME, 'HH24:MI'),
        AST.STATUS AS NOTE
    FROM TBL_STUDENT ST
        INNER JOIN TBL_APPLY AP --수강신청 조인
            ON ST.SEQ = AP.STUSEQ
                INNER JOIN TBL_ATTEND_STUDENT AST --학생 근태 조인
                    ON AP.SEQ = AST.APPSEQ
                        WHERE ST.SEQ = PNUM AND ST.DELFLAG = 'Y' AND AP.DELFLAG = 'Y' AND AST.DELFLAG = 'Y'
                            ORDER BY DAYS DESC;
END;

-- S009 : 매일 근태 관리를 기록할 수 있어야 한다.(출근 1회, 퇴근 1회)

-- 교육생 출결 관리
-- 출/퇴근 PROCEDURE
CREATE OR REPLACE PROCEDURE PROC_STUDENT_ATTEND_INOUT(
    PNUM NUMBER, --PNUM
    PRESULT OUT NUMBER
)
IS
    PSEQ NUMBER;
    PINTIME DATE;
    POUTTIME DATE;
BEGIN
    SELECT MAX(SEQ) INTO PSEQ FROM TBL_ATTEND_STUDENT WHERE APPSEQ = (SELECT SEQ FROM TBL_APPLY WHERE STUSEQ = PNUM);
    SELECT INTIME, OUTTIME INTO PINTIME, POUTTIME FROM TBL_ATTEND_STUDENT WHERE SEQ = PSEQ;
    IF PINTIME IS NOT NULL AND POUTTIME IS NOT NULL THEN
        INSERT INTO TBL_ATTEND_STUDENT VALUES (ATTEND_STUDENT_SEQ.NEXTVAL, SYSDATE, SYSDATE, NULL, NULL, 
            (SELECT SEQ FROM TBL_APPLY WHERE STUSEQ = PNUM), DEFAULT);
        PRESULT := 0;
    ELSIF PINTIME IS NOT NULL AND POUTTIME IS NULL THEN
        UPDATE TBL_ATTEND_STUDENT SET OUTTIME = SYSDATE WHERE SEQ = PSEQ;
        PRESULT := 1;
    END IF;
END;

--------------------------------------------------------------------------------------------------------------------
-- 04. 기타 추가사항
--------------------------------------------------------------------------------------------------------------------

-- 관리자 열체크 VIEW
CREATE OR REPLACE VIEW VW_ADMIN_COVID19
AS
SELECT
    CAD.SEQ,
    AD.SEQ AS ADSEQ,
    AAD.DAYS,
    CAD.AMTEMP,
    CAD.PMTEMP
FROM TBL_ADMIN AD
    INNER JOIN TBL_ATTEND_ADMIN AAD
        ON AD.SEQ = AAD.ADMSEQ
            INNER JOIN TBL_COVID19_ADMIN CAD
                ON AAD.SEQ = CAD.ATTSEQ
                    WHERE AD.DELFLAG = 'Y' AND AAD.DELFLAG = 'Y' AND CAD.DELFLAG = 'Y'
                        ORDER BY AD.SEQ ASC, AAD.DAYS DESC;

-- 관리자 체온 추가 PROCEDURE
CREATE OR REPLACE PROCEDURE PROC_INSERT_ADMIN_COVID19(
    PAMTEMP NUMBER,
    PPMTEMP NUMBER,
    PADMSEQ NUMBER,
    PRESULT OUT NUMBER
)
IS
    PSEQ NUMBER;
    PATTSEQ NUMBER;
BEGIN    
    SELECT MAX(SEQ) INTO PSEQ FROM TBL_ATTEND_ADMIN WHERE ADMSEQ = PADMSEQ;
    SELECT MAX(ATTSEQ) INTO PATTSEQ FROM TBL_COVID19_ADMIN;
    IF PSEQ <> PATTSEQ THEN
        INSERT INTO TBL_COVID19_ADMIN
            VALUES (COVID19_ADMIN_SEQ.NEXTVAL, PAMTEMP, PPMTEMP, PSEQ, DEFAULT);
        PRESULT := 1;
    ELSE
        PRESULT := 0;
    END IF;
END;

-- 특정 관리자 체온만 보여주는 PROCEDURE
CREATE OR REPLACE PROCEDURE PROC_SEE_COVID19_ADMIN(
    PNUM NUMBER,
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN PRESULT FOR
        SELECT * FROM VW_ADMIN_COVID19 WHERE ADSEQ = PNUM ORDER BY DAYS DESC;
END;

-- 취업정보 VEIW
-- 학생이름, 수강상태, 수료날짜, 회사명, 연봉
CREATE OR REPLACE VIEW VW_EMPLOYMENT
AS
SELECT 
    STU.NAME,
    CASE
        WHEN OC.ENDDATE <= AP.STATUS THEN '수료'
        WHEN OC.ENDDATE > AP.STATUS THEN '중도탈락'
        WHEN AP.STATUS IS NULL THEN '수강중'
    END AS STATUS,
    EMP.COMPANY,
    CASE
        WHEN EMP.SALARY IS NOT NULL THEN TO_CHAR(EMP.SALARY, '999,999,999')
        WHEN EMP.SALARY IS NULL THEN NULL
    END AS SALARY,
    OC.SEQ AS COURSESEQ,
    C.NAME AS COURSENAME
FROM TBL_EMPLOYMENT EMP
    INNER JOIN TBL_APPLY AP
        ON EMP.APPSEQ = AP.SEQ
            INNER JOIN TBL_STUDENT STU
                ON AP.STUSEQ = STU.SEQ
                    INNER JOIN TBL_OPEN_COURSE OC
                        ON OC.SEQ = AP.OCRSSEQ
                            INNER JOIN TBL_COURSE C
                                ON OC.CRSSEQ = C.SEQ
                            WHERE EMP.DELFLAG = 'Y' AND AP.DELFLAG = 'Y' AND STU.DELFLAG = 'Y' AND OC.DELFLAG = 'Y';


-- 취업정보 UPDATE PROCEDURE
CREATE OR REPLACE PROCEDURE PROC_UPDATE_EMPLOYMENT(
    PCOMPANY VARCHAR2,
    PSALARY NUMBER,
    PSTUNAME VARCHAR2,
    PRESULT OUT NUMBER
)
IS
BEGIN

    UPDATE TBL_EMPLOYMENT SET COMPANY = PCOMPANY,
                          SALARY = PSALARY
                          WHERE APPSEQ = (SELECT SEQ FROM TBL_APPLY 
                            WHERE STUSEQ = (SELECT SEQ FROM TBL_STUDENT WHERE NAME = PSTUNAME));
    PRESULT := 1;

EXCEPTION
    WHEN OTHERS THEN
        PRESULT := 0;
END;


-- 취업정보 INSERT PROCEDURE
CREATE OR REPLACE PROCEDURE PROC_INSERT_EMPLOYMENT(
    PCOMPANY VARCHAR2,
    PSALARY NUMBER,
    PSTUNAME VARCHAR2,
    PRESULT OUT NUMBER,
    VRESULT OUT NUMBER
)
IS
BEGIN
    
    SELECT COUNT(SEQ) INTO PRESULT FROM TBL_EMPLOYMENT 
        WHERE APPSEQ = (SELECT SEQ FROM TBL_APPLY 
                WHERE STUSEQ = (SELECT SEQ FROM TBL_STUDENT WHERE NAME = PSTUNAME));
    
    IF PRESULT = 0 THEN    
        INSERT INTO TBL_EMPLOYMENT VALUES (EMPLOYMENT_SEQ.NEXTVAL, PCOMPANY, PSALARY, 
        (SELECT SEQ FROM TBL_APPLY WHERE STUSEQ = (SELECT SEQ FROM TBL_STUDENT WHERE NAME = PSTUNAME)), DEFAULT);
        VRESULT := 1;
    ELSE 
        VRESULT := 0;
    END IF;
    
END;

-- 학생이 교사 평가 시 과목 전체조회 PROCEDURE
CREATE OR REPLACE PROCEDURE PROC_STUDENT_SCORE_TEACHER(
    PNUM IN NUMBER,
    PRESULT OUT SYS_REFCURSOR
)
IS
BEGIN
OPEN PRESULT FOR
SELECT
    T.NAME,
    S.SEQ,
    S.NAME AS SUBJECT,
    OSU.STARTDATE,
    OSU.ENDDATE,
    TS.SCORE
FROM TBL_TEACHER_SCORE TS
    INNER JOIN TBL_OPEN_SUBJECT OSU
        ON TS.OSUBSEQ = OSU.SEQ
            INNER JOIN TBL_AVAIL_SUBJECT ASU
                ON OSU.AVAILSEQ = ASU.SEQ
                    INNER JOIN TBL_APPLY AP
                        ON TS.APPSEQ = AP.SEQ
                            INNER JOIN TBL_TEACHER T
                                ON ASU.TEASEQ = T.SEQ
                                    INNER JOIN TBL_SUBJECT S
                                        ON ASU.SUBSEQ = S.SEQ
WHERE TS.APPSEQ = (SELECT SEQ FROM TBL_APPLY WHERE STUSEQ = PNUM) AND TS.DELFLAG = 'Y' AND OSU.DELFLAG = 'Y'
    AND ASU.DELFLAG = 'Y' AND AP.DELFLAG = 'Y' AND T.DELFLAG = 'Y' AND S.DELFLAG = 'Y';
END;


-- 학생이 교사 평가 INSERT PROCEDURE
CREATE OR REPLACE PROCEDURE PROC_STUDENT_GRADETEACHER(
    PNUM IN NUMBER,
    PSUBNUM IN NUMBER,
    PSCORE IN NUMBER,
    PRESULT OUT NUMBER
)
IS
    TENDDATE DATE;
    OSEQ NUMBER;
    TSEQ NUMBER;
BEGIN
    SELECT 
        OS.ENDDATE, OS.SEQ, AP.SEQ INTO TENDDATE, OSEQ, TSEQ
    FROM TBL_OPEN_SUBJECT OS
        INNER JOIN TBL_OPEN_COURSE OC
            ON OS.OCRSSEQ = OC.SEQ
                INNER JOIN TBL_APPLY AP
                    ON OC.SEQ = AP.OCRSSEQ
                        INNER JOIN TBL_AVAIL_SUBJECT ASU
                            ON OS.AVAILSEQ = ASU.SEQ
                                INNER JOIN TBL_SUBJECT S
                                    ON ASU.SUBSEQ = S.SEQ
                    WHERE AP.STUSEQ = PNUM AND S.SEQ = PSUBNUM;
    IF SYSDATE - tenddate > 0 THEN
    UPDATE TBL_TEACHER_SCORE SET SCORE = PSCORE WHERE OSUBSEQ = OSEQ AND APPSEQ = TSEQ;
        PRESULT := 1;
    ELSE 
        PRESULT := 0;
    END IF;
END;

