# Record
이제 각자 수정한 부분을 여기에 기록해봅시다! <br>

## 기록 양식 <br>
"## 날짜"  <br>
"### 이름"  <br>
"수정파일 이름 : 수정 내역"  <br>
" <- 위에서 이건 제거하고 쓰시면 됩니다


## 0501
### 김수현
**1. comment_1_listview**
: 댓글 Frame
| id | 내용 |
|---|:---:|
| comment_img | 댓글 작성자 프로필 이미지 |
| comment_nickname | 댓글 작성자닉네임 |
| comment_content | 댓글내용 |
| comment_movevert | 댓글 점세개 id (나중에 메뉴 연결해야함) |


**2. see_post**
: 글에 댓글 recylerview와 댓글입력창 추가
| id | 내용 |
|---|:---:|
| see_post_comment_text | 작성 댓글 텍스트 |
| see_post_comment_send_button | 댓글 전송버튼 |

**3. 그외 자잘**
write_post 이미지 업로드 아이콘 변경


## 0502

### 김승윤

**1. LoginActivity.java**

로그인 할 시, 다음 앱 실행부터는 로그인 상태 유지

-임시 로그아웃 버튼 메인페이지(홈)에 생성-

**2. 파일 이름 변경 및 위치 정리**

역할에 맞고 일관적이게 파일 이름을 변경하였음

**3. CommunityFrame.java(+files related to the community)**

각 커뮤니티에 맞게 탭 이동할 수 있도록 수정

**4. DB**
 
게시물 업로드 시, 각 커뮤니티에 해당하는 경로에 저장


## 0503

### 김승윤

**1. Java files in the "activity" package**

SeePost.java 파일을 제외한 모든 파일에 주석 제대로 달고

코드 깔끔하게 다시 정리하였음

**2. 게시글 댓글 불러오기 구현**

구현함에 따라 데이터를 저장하는 자바 파일이 많아져서

container라는 package를 만들어 다 넣어버렸음

**3. UserInfo.java**

게시글, 댓글 불러올 시 프로필 사진, 닉네임 불러와야 하는데

DB의 비동기식 작동 방식 때문에 게시글, 댓글을 불러오는 동안에

프로필 사진과 닉네임 불러오는 것은 힘듦.

따라서 유저가 로그인시에 바로 유저 아이디, 프로필 사진 url, 닉네임을

불러와서 UserInfo.java에 저장하도록 했음.

그래서 프로필 사진, 닉네임 불러오고 싶으면 이 파일을 import해서 쓰면 됨.

예) 

import gachon.termproject.joker.UserInfo;

String nickname = UserInfo.nickname;

**4. DB**
 
댓글 업로드 시, 해당하는 글의 DB 경로에 저장


## 0507

### 김승윤

**1. ExpertList.java 관련된 파일들**

전문가 목록 조회 가능



## 0511

### 김수현

**1. post write할 때 이미지 삭제하기**
java file : PostImage, WritePostActivity
xml : post_write_image_view.xml







***Improvement***

댓글 작성 시간 표시할 수 있게 xml 만들어주면 좋겠음

게시글 내용이 3줄 초과면 리스트에 불러올 때 더보기 붙이기

이메일 .com 체크 로직이 작동을 안함

닉네임 중복아니면 바로 다음 단계 넘어갈 수 있게 손봐야함 (역시 DB 비동기식 작동 문제)
