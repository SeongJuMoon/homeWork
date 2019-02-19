# spring-boot-board-app 프로젝트 설명
## 본 레포지토리는 Spring Boot 2.0.5 RELEASE + SPRING - DATA - JPA + BootStrap를 이용하여 만든 간단한 익명 게시판입니다.
 
## 본 프로젝트에서는 MySQL을 이용하여 진행하였습니다.

# 1.스키마 및 테이블 설정
~~~
  CREATE SCHEMA `MYAPP`
  
  USE `MYAPP`
  
  CREATE TABLE `board` (
  `board_uuid` varchar(40) NOT NULL,
  `content` varchar(4000) NOT NULL,
  `password` varchar(200) DEFAULT NULL,
  `reg_dttm` datetime DEFAULT NULL,
  `title` varchar(2000) NOT NULL,
  `upt_dttm` datetime DEFAULT NULL,
  `user_name` varchar(100) NOT NULL,
  PRIMARY KEY (`board_uuid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8
~~~

# 1.2 테이블 명세 (TABLE SPEC)

~~~
board_uuid	varchar(40)	 : 게시판의 유니크 키로서 JAVA의 UUID 클래스를 이용하여 생성하고 있습니다.	
content	varchar(4000)		 : 게시판의 내용을 담는 값을 저장하는 컬럼 입니다.		
password	varchar(200)	 : 게시판의 수정 및 삭제를 할 때 요구하는 비밀번호의 값으로써 SHA-256 방식으로 암호화 하여 저장하고있습니다.		
reg_dttm	datetime			 : 게시판의 등록 타임스탬프 값을 저장하는 컬럼 입니다.
title	varchar(2000)			 : 게시판의 제목 값 저장하는 컬럼 입니다.
upt_dttm	datetime			 : 게시판의 수정 타임스탬프를 저장하는 컬럼입니다.
user_name	varchar(100)	 : 해당 게시물의 작성자의 이름을 저장하는 컬럼입니다.
~~~

# 2.프로젝트 빌드 JDK 버전
본 프로젝트는 openJDK 1.8 버전으로 빌드되었습니다.

# 2.1 Gradle 빌드 명령어
프로젝트 경로로 이동하여 cli로 접근 이후 아래의 명령어를 수행해주시면됩니다.
~~~
  gradlew bootjar --info (Windows)
  
  gradle bootjar --info (UNIX MAC LIUNX)
~~~
수행하고 나면 프로젝트의 bulid/libs 경로에 실행가능한 Jar 파일이 생성됩니다.

default-name = appName + SNAPSHOT + .jar

ex) Board-0.0.1-SNAPSHOT.jar

# 2.2상세 JVM SPEC
~~~
openjdk version "1.8.0_191-1-ojdkbuild"

OpenJDK Runtime Environment (build 1.8.0_191-1-ojdkbuild-b12)

OpenJDK 64-Bit Server VM (build 25.191-b12, mixed mode)
~~~

# 3.실행 커맨드 (Spring boot Applcation Running Command)
java -jar -server -Xms512m -Xmx1024m -Dfile.encodng=UTF-8 Board-0.0.1-SNAPSHOT.jar >> boot.log

# 4.접속 URL
http://localhost:10004

피드백이 있으시면 언제든지 PR 부탁드리겠습니다. 감사합니다.
