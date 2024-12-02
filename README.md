### 개요
**1. 서비스 컨셉**
   - 책을 검색하고 대여할 수 있는 어플리케이션

**2. 개발 목적**
   - 자주 사용해보지 않은 AAC 연습(Navigation, Room)
   - 코루틴, flow 등 비동기 통신 연습
   - MVVM 연습

**3. 상세 화면 구상**
   - FavoriteFragment : 초기 진입 화면, 즐겨찾기 한 도서 목록
   - SearchFragment : 책 검색 화면
   - SearchBookFragment : 책 검색 결과 화면
   - BookDetailFragment : 책 상세 화면
   - BookRentalBottomFragment : 책 대여 화면
   - OrderFragment : 대여한 도서 정보가 노출되는 화면



### 필수 추가 내용 : local.properties 내 파라미터 선언
1. NAVER_OPEN_API_BASE_URL : Naver OpenApi url
2. NAVER_CLIENT_ID : 발급 받은 네이버 개발자 클라이언트 아이디
3. NAVER_CLIENT_SECRET : 발급 받은 네이버 개발자 시크릿 코드


### 실행 결과
<img width="980" alt="LibraryExample 이미지" src="https://github.com/user-attachments/assets/940edaa4-bd61-4872-aa80-11aef61cfe14">
