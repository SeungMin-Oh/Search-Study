
# Lucene Search Engine Project

### 프로젝트 개요
Apache Lucene을 기반으로 한 검색 엔진 스터디 자료입니다. Spring Boot 프레임워크를 사용하여 RESTful API 형태로 구현되었으며, 두 가지 검색 방식을 지원합니다:
1. **BM25 검색**: 문서 내 단어 빈도수에 기반한 일반적인 검색 방식.
2. **벡터 검색**: Python 서버와 통신하여 AI 모델을 활용한. (제작중)

### 기술 스택
- **Back-end**: Java 17, Spring Boot, Apache Lucene
- **Front-end**: HTML, JavaScript, jQuery, Bootstrap
- **Database**: MySQL
- **Dependency Management**: Maven

### 주요 기능
- **문서 색인(Indexing)**: 사용자가 입력한 제목과 내용을 색인하여 검색 가능하게 함.
- **BM25 검색**: BM25 알고리즘 검색.
- **벡터 검색**: 벡터 기반 검색. (제작중)

### 설정 방법

1. **클론 및 빌드**
   ```bash
   git clone <repository-url>
   cd waplsearch
   mvn clean install
   ```

2. **MySQL 설정**
   - MySQL을 기반으로 제작.
   - `src/main/resources/application.properties` 파일에서 데이터베이스 설정을 본인이 설치한 환경에 맞게 수정할 것.
   
3. **Python 서버 설정**
   - 벡터 검색은 3.x 버전 python을 필요로 함.
   - 'localhost:5000/embed' 의 엔드포인트를 가지도록 별도로 서버를 실행해야함
   - 그 외 내용은 requirements.txt를 살펴볼것
     
4. **애플리케이션 실행**
   ```bash
   mvn spring-boot:run
   ```

### 사용 방법

#### 1. 문서 색인
- **엔드포인트**: `/search/index`
- **메서드**: `POST`
- **파라미터**: 
  - `title`: 색인할 문서의 제목
  - `content`: 색인할 문서의 내용
- **사용 예시**:
  ```bash
  curl -X POST "http://localhost:8080/search/index" -d "title=Example Title&content=This is an example content."
  ```

#### 2. BM25 검색
- **엔드포인트**: `/search/bm25`
- **메서드**: `GET`
- **파라미터**: 
  - `query`: 검색할 쿼리 텍스트
- **사용 예시**:
  ```bash
  curl -X GET "http://localhost:8080/search/bm25?query=example"
  ```

#### 3. 벡터 검색
- **엔드포인트**: `/search/vector`
- **메서드**: `GET`
- **파라미터**: 
  - `query`: 검색할 쿼리 텍스트
- **사용 예시**:
  ```bash
  curl -X GET "http://localhost:8080/search/vector?query=example"
  ```

### 프론트엔드 사용
- `index.html` 을 브라우저에서 열어 직접 문서를 색인하고 검색할 수 있음.

### Maven 설정
`pom.xml` 파일 의존성:

```xml
<dependencies>
    <!-- Spring Boot 및 Lucene 라이브러리 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.lucene</groupId>
        <artifactId>lucene-core</artifactId>
        <version>9.10.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>org.apache.lucene</groupId>
        <artifactId>lucene-queryparser</artifactId>
        <version>9.10.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
    <!-- 기타 라이브러리 생략 -->
</dependencies>
```

