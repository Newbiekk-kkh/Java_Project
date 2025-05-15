# 바로인턴 백엔드 개발 Java 과제

## 사용 기술
> Java 17, Spring, SpringBoot 3.4.5, Spring Security, JWT

## 프로젝트 기능

### 회원가입
> * username, password, nickname 을 입력해 간단한 회원가입 기능 구현

### 로그인
> * Spring Security 와 Jwt 를 활용한 로그인 기능 구현

### 관리자 권한 부여
> * 사용자 Role 을 기반으로 관리자 권한 부여 기능 구현

## API 명세
Swagger 링크 : http://52.78.48.78:8080/swagger-ui/index.html

![image](https://github.com/user-attachments/assets/d45a55aa-85f1-460b-91f2-b54b3f08261b)

## 배포 아키텍쳐
![image](https://github.com/user-attachments/assets/01852757-e5ba-45eb-b3ac-d326f9af39db)


**주요 특징:**
* EC2 인스턴스에서 직접 소스 코드를 클론합니다.
* EC2 인스턴스에서 Gradle을 사용하여 애플리케이션을 빌드합니다.
* 빌드된 JAR 파일을 EC2 인스턴스에서 `java -jar` 명령으로 직접 실행합니다.
* 데이터는 애플리케이션 내 인메모리 저장소(제공된 `ConcurrentHashMap` 기반 `UserRepository`)를 사용합니다.




