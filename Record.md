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


## 0508

### 김승윤

**1. 채팅 기능 불완전하게나마 가능**

전문가 목록 조회 후 전문가에게만 채팅할 수 있게 함


## 0511

### 김수현

**1. post write할 때 이미지 삭제하기**<br>
java file : PostImage, WritePostActivity<br>
xml : post_write_image_view.xml<br>


**2. 댓글 수정**<br>
java file : SeePostActivity -> 댓글쓰면 댓글창 초기화, 0글자 입력 방지<br>
xml : menu_button_threedot_ripple, activity_see_post, comment_1_listview xml 수정<br>

## 0513
### 김수현
**1. SeePostActivity**
: 보는 게시글에 들어갈 image frame 넣어둠 (106~110 이용하시면 됨)


**2. see_post**
: 글에 댓글 recylerview와 댓글입력창 추가
| id | 내용 |
|---|:---:|
| see_post_comment_text | 작성 댓글 텍스트 |
| see_post_comment_send_button | 댓글 전송버튼 |

**3. 전반적인 action bar 손봄**
액션바 관련해서 애매하거나 궁금한거잇으면 카톡 ㄱ

**3. 그외자잘쓰**
댓글 날짜 추가
댓글 글쓰면 지워지면서 내려가는것도 추가
댓글 글씨 안쓰면 입력 안되게

## 0513
### 김승윤
**1. 마이인포 툴바 위치 문제 해결**

**2. 게시글 업로드 관련**

이미지 포함 글을 업로드 할 시, 이미지가 다 올라가야 업로드 완료되게 함.

이에 따라 게시판에 올라가는 글 역시 글과 이미지가 전부 포함되어 올라가게 됨.


## 0514
### 김승윤
**1. 회원가입 시 이메일 형식체크 업데이트**

Ex) aaa@aaa -> 이메일 체크 통과 못함

**2. 채팅기능 작동가능하게 업데이트**

더 다듬어야할 부분 Improvements에 썼음


### 김수현
**1. SeePostActivity &WritePostActivity**

: 수정끝~ => 후에 see에서 이미지 눌렀을 때 전체화면 되게 할거임


**승민좌가 해야할 것들**

1. Mathing_WritePostActivity
2. Mathing_expert_SeePostActivity
3. Mathing_user_SeePostActivity
 
각 파일 상단에 할일 적어둠


<br>
<br>
<br>
<br>
***Improvements***

화면 회전 아예 막아버리기

회원가입 창 글 입력칸의 글이 글씨체에 따라 잘리는 현상이 발생

이미지 포함 게시글 업로드 시 로딩 화면 추가하면 좋을 듯

게시글 미리보기 내용이 현재 15자 이상이면 더보기 추가하였지만 줄 단위로, 3줄 초과면 더보기 붙이기

채팅 메시지 보내면 메시지 박스 내용 초기화 더 빠르게 하기

채팅방에 있을 때나 들어갔을 때 새로운 메시지 있으면 스크롤 완벽하게 맨 아래로 이동하게 하기

채팅방 리스트에서 채팅방 내용 미리보기에 최근 메시지가 자동 업데이트 되게 하기

추가해야할 기능 - 홈 : 게시글 검색, 커뮤니티 : 게시글 검색, 매칭 : 게시글 검색, 매칭_전문가 리스트 : 닉네임 검색
