create table REPORT_CONFIGSQL
(
  reportName varchar(40),
  sqlName	 varchar(20),
  showId	 number(4),
  sql        CLOB,
  bak        VARCHAR2(20),
  name       VARCHAR2(20)
)


create table REPORT_INPUT
(
  id           NUMBER primary key,
  name         VARCHAR2(20),
  type         VARCHAR2(20),
  lable        VARCHAR2(20),
  multiselect  VARCHAR2(5),
  validateexp  VARCHAR2(100),
  validatemsg  VARCHAR2(50),
  allowblank   VARCHAR2(10),
  dependcolumn VARCHAR2(50),
  dependinput  VARCHAR2(50),
  disablemsg   VARCHAR2(30),
  datasql      VARCHAR2(200)
)

create table REPORT_TREE
(
  id       NUMBER primary key,
  parentid NUMBER,
  text     VARCHAR2(20),
  leaf     VARCHAR2(5),
  inputs   VARCHAR2(20),
  reportName	varchar(40),
  sqlName		varchar(20),
  columnIndex	varchar(50)
  
)

create table SHOW_RESULT
(
  id              NUMBER,
  caption         VARCHAR2(20),
  header          VARCHAR2(20),
  dataindex       VARCHAR2(20),
  width           NUMBER,
  isrender        CHAR(1),
  renderer        CLOB,
  hidden          CHAR(1),
  datafieldtype   VARCHAR2(20),
  seriesheader    CHAR(1),
  seriesdataindex CHAR(1),
  catedataindex   VARCHAR2(100),
  groupdataindex  VARCHAR2(20),
  groupheader     VARCHAR2(20),
  columnindex     NUMBER
)

create table report_authority_config(
	   id	number primary key,
       key_word varchar(30),
       param_word varchar(30),
       type     char(1),
       sql      varchar(500)
)
