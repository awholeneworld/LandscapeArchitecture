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


## 0516
### 고지민
**1. 마이인포 글자부분**

글자 간격 안맞아서 그부분 수정함!

**2. 마이인포 포트폴리오 버튼 디자인**

**3. 회원가입 지역 선택부분**

세종특별자치시 추가

**4. setting activity**

setting 부분 디자인


## 0517
### 고지민
**1. setting Activity **

setting 부분 디자인 완료

**2. Setting Activity **

setting 알림 설정하는 부분 연결 완료


## 0519
### 김수현
**1. search **

일단 community search design만 구현해둠

file : CommunitySearchActivity, CommunityFragment, activity_search_community.xml, search.xml


**2. 수정(Rewrite) **

일단 post id 불러오는 문제가 해결되어야 더 진행 가능...

file : ReWritePostActivity, SeePostActivity (menu 선택 부분), activity_rewrite_post.xml, my_post_menu.xml


**3. Main home **

일단 community부분은 완성. 전문가 후기 부분은 see post 안되는 문제 해결되면 그 다음에 할 예정.

file : HomePostAdapter, MainHomeFragment


**4. matching 게시판 **

이거 올리고 작업할예정,,, 일단 id없어서 에러나서 주석처리해둠

file : MatchingUserViewFragment



## 0523
### 김수현
**한거**
<br>
<ui>

홈-전문가리뷰
 
매칭요청&전문가리스트 지역설정 => 일단 UI만 추가해둠
 
화면 회전 아예 막아버리기
 
게시글 업로드 시 로딩 화면 추가

 
<로직><br>
 
스와이프

## 0526
### 김승윤
**한거**

로그인 무한로딩 (사실 무한로딩이 아니라 코드 문제로 MainActivity로 넘어가지 못하는 것임) 해결 -> 로그인 시 자신이 작성한 게시물, 댓글 아이디 리스트 가져옴 (삭제할 수 있게)

PostContent를 그냥 게시판용 PostContent랑 매칭 게시판용 MatchinigPostContent로 나눴음 -> 로그인 했을 때 데이터 전달 용이하게 하기 위함

이에 따라 PostContent를 쓰던 파일에는 변동사항이 생김

설정에서 자신의 정보 바꾸면 게시글, 댓글, 매칭글, 채팅, 마이인포 정보 바뀌게 구현 (단 게시글은 새로고침 해야됨)

마이인포에 새로고침 기능 넣어서 프사, 닉네임, 지역 그리고 한줄 소개 업데이트 가능하게 함 (설정에서 자신의 정보 바꾸면 마이인포 새로고침 기능을 부름)

마이인포 게시물, 댓글 단 게시물은 아직 미완성 (불러오는 건 금방 끝낼 수 있지만 새 게시글 혹은 댓글 작성 시, 자동 업데이트를 어떻게 해야 효율적으로 할 수 있을지 로직 고민 중)


### 고지민
** 1. 내 정보 카드뷰 부분 **

내 정보 카드뷰 부분 사이 간격 조정

** 2. 내 정보 비밀번호 변경 부분 **

ChangePasswordActivity가 CheckPasswordActivity로 연결되어 있어 수정

** 3. 전문가 매칭 리스트 **

뒤로 가기 버튼 추가

 
 ## 0527
### 김수현
**한거**
<br> 
 포트폴리오 웹사이트 설정 로직 넣기<br> 
댓글 토글이벤트 추가 (리스트뷰 버튼이벤트) + 댓글 수동업데이트<br> 
채팅이 없습니다. => 채팅있을때 안보이게<br> 
후기 게시글 인터페이스 => see, write에서 전문가 닉네임 보이게<br> 

 
 
<br>
<br>
<br>
<br>
***Improvements***

<UI>
 
회원가입 창 글 입력칸의 글이 글씨체에 따라 잘리는 현상 해결하기 -> layout_height 높이면 됨

홈 전문가 리뷰  글 안들어가짐

매칭 지역설정 기능 추가 => UI만 구현됨
 
<Logic>
 
로그인 실패 에러메시지 Custom으로 만들기
 
커뮤니티 게시판 앨범형-리스트형 전환

커뮤니티 게시판 아래로 스크롤 하면 새 글 업데이트

커뮤니티 글, 댓글 옵션메뉴 프로필보기 이동

댓글 알림 기능

매칭 알림 기능

채팅 처음 시작해도 시작한 채팅 없다는 화면 뜨는 문제 해결하기

채팅방에 있을 때나 들어갔을 때 새로운 메시지 있으면 스크롤 완벽하게 맨 아래로 이동하게 하기

채팅방 알림 기능

마이인포 댓글 단 게시물 불러오기

앱 전반 검색 기능 추가 - 커뮤니티 : 게시글 검색, 매칭 : 게시글 검색, 매칭 전문가 리스트 탭 : 닉네임 검색

새로운 데이터 자동 업데이트 필요한 곳 : 매칭 게시판
