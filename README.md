# spring-vote-21st
ceos back-end 21st vote project
<br>
<br>

---
#  [프백 합동과제]

### (1) 프백 합동과제 요약
- 20기 파트장 및 데모데이 투표 서비스 만들기
- 배포 링크 : https://vote-dream.p-e.kr/swagger-ui/index.html#/
 
<br>

### (2) ERD 설계
![image](https://github.com/user-attachments/assets/af8dfbe1-67a3-450f-8a7e-6b3bfecf3a33)
> [**주요 고려사항**]<br> 
> 1. 같은 기능을 담당하는 api를 데모데이/파트장으로 따로 만들 것인가? -> X<br> 
> 2. 모아서 만들되, voteType의 enum타입으로 데모데이/파트장 투표를 구별하자!<br> 
> 3. voteType으로 Vote table에서 데모데이/파트장 투표 구별 -> Vote table의 vote_id로 voteRecord(투표 내역), voteItem(투표 항목)을 조회합니다.
<br>



### (3) 프론트엔드의 FIGMA 설계
![image](https://github.com/user-attachments/assets/2aff6a5f-5a50-4026-8ca2-abcf04a7f4f9)
![image](https://github.com/user-attachments/assets/5cb2389a-ec7f-49a1-9a90-859dc65b3763)
<br>
<br>



### (4) API 설계
![image](https://github.com/user-attachments/assets/a959ed60-c3d1-4f65-ad67-a2548824bd4a)
> [**고려사항/어려웠던 점/리팩토링사항**]<br>
> 1. api 간단 설명<br>  
> ```
> (1) vote/vote : 투표하는 api
> (2) vote/status : 개인 투표 여부 확인 api
> (3) vote/results : 전체 투표 결과를 반환하는 api
> (4) vote/items : 투표 항목 조회 api
> (5) vote/ping : 테스트용 동적 메서드 api (DEMODAY와 PARTLEADER enum 구분을 확인하기 위함..)
> (6) user/register : 회원가입 api
> (7) user/login : 로그인 api
> (8) user/check : 로그인 확인 api
> (9) user/reissue : 리프레쉬 토큰 관련 api
> ```
> 2. local로 테스트를 하다가 막날에 rds 데이터베이스 스키마를 만들며 연결 이슈가 있었지만 가볍게 해결하였고.. <br>

> 3. 토큰 포함한 api 테스트를 swagger에서 할 수 있다는 사실을 처음 알았습니다. **(여지껏 토큰을 열심히 넣어가며 postman에서 테스트 했는데)** 배포한 후 pr을 올리면 설정값에 따라 자동으로 swagger가 업데이트 되고, 테스트를 간편하게 해볼 수 있다는 점이 편리했습니다. <br>

> 4. rds에 더미 데이터가(프론트엔드/백엔드 팀원분들의 이름, 팀명 리스트) 들어가 있어야 해서 **CommandLineRunner**라는 메서드를 통해 기본 데이터 값을 데이터베이스에 넣어 보았습니다. 처음 실행될 때에만 넣어주고 이후에는 신경을 쓰지 않아도 되어서 좋았고.. 세상에는 편리한 메서드가 많은 것 같아요..<br> 

> 5. 리팩토링 1 : 업데이트가 너무 잘 되어서 테스트로 넣어둔 api도 같이 올라가서.. (/ping) 지우는 리팩토링을 할 계획입니다. <br> 

> 6. 리팩토링 2 : voteController와 voteService 간 타입을 바꿔주는 등의 잡다한 작업을 converter라는 폴더를 만들어 따로 빼려고 생각중입니다. **(controller - converter - service 구조가 되는 셈.)** 매번 계층 구조 분리의 중요성에 대해 각 과목에서 공부를 하게 되는데 막상 각 계층에 맞는 작업만을 잘 분리해서 넣는건 고민할만한 문제가 되는 것 같습니다. <br>

> 7. 리팩토링 3 : 이것저것 테스트 하다가 발견한 오류 : 팀원분들의 teamId와 Team table의 teamId가 매칭이 뭔가 안되더군요..뭔가 꼬인 것 같아 리팩토링 중에 있습니다. <br>

> 8. 리팩토링 4 : 로그인/회원가입에는 테스트 코드가 있고, vote에 테스트 코드를 추가하려 합니다.


<br>

### (5) 배포
1. EC2 route53으로 https 배포하려고 했습니다. 그러나 1개 등록 시 달마다 0.5달러를 내야했기에 nginx를 이용하여 무료로 바꿨습니다..
2. 도메인은 한글에서 무료로 발급받았는데 kro.~로 시작하는 도메인은 예뻐서 그런지 쓰는 사람이 많아서 ssl 인증이 잘 안됐습니다! ..그래서 못생긴 도메인으로 다시 바꿨습니다.
 
 
<br>


