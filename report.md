# Error report

## 화면 회전시 RecyclerView 위치 기억 문제
### 문제
- 리사이클러 뷰가 포함된 프래그먼트에서 화면 회전시 위치를 기억하지 못함
### 수정
- 시도1
  - onSaveInstanceState에서 레이아웃 매니저의 findFirstVisibleItemPosition으로 위치 가져와 저장
  - onRestoreInstanceState에서 savedInstanceState 이용하여 위치 값(position) 복구
  - log 확인 결과 프래그먼트 인스턴스가 계속 생성되는 현상 파악
- 시도2
  - supportFragmentManager에서 findFragmentByTag로 기존 프래그먼트가 있는지 확인
  - 없는 경우에만 새로운 인스턴스 생성
  - 해결(화면 회전시 위치를 정상적으로 기억하는 점 확인)
### 원인
- 화면 회전시 기존 프래그먼트 인스턴스를 사용하지 않고 인스턴스를 계속 생성
### 결과
- 프래그먼트 매니저를 통해 프래그먼트 인스턴스 재사용
- 화면 회전시 스크롤 위치 정상 반영

## EditText 엔터키 동작 문제
### 문제
- EditText에서 한글 입력 후 엔터키 입력시 검색 동작 실행 안됨
### 수정
- 시도
  - setOnKeyListener로 keyEvent가 DOWN이고 keyCode가 ENTER일 때 검색 수행하도록 설정
  - 영어에서만 동작하여 로그 확인 -> 한글에서는 keyEvent가 UP만 인식됨
  - keyEvent를 UP으로 변경
### 원인
- 키보드 한글 입력 문제
### 결과
- EditText에서 한글 입력 후 엔터키 입력시에도 검색 정상 동작