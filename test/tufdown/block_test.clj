(ns tufdown.block-test
  (:require [tufdown.block :refer :all]
            [clojure.test :refer :all]))

(deftest parse-test
  (testing "구분줄"
    (are [시도 결과] (= 시도 결과)
      (parse "---\n제목\n---\n")
      [:문서 [:구분줄] [:작은제목 [:문장 "제" "목"]]]

      (parse "- - -\n") [:문서 [:구분줄]]

      (parse "***\n") [:문서 [:구분줄]]

      (parse "* * *\n") [:문서 [:구분줄]]

      (parse "__________\n") [:문서 [:구분줄]]))

  (testing "각주"
    (are [시도 결과] (= 시도 결과)
      (parse "[각주링크]: http://test.com\n")
      [:문서 [:각주링크 [:각주이름 "각" "주" "링" "크"]
              [:각주주소 "h" "t" "t" "p" ":" "/" "/" "t" "e" "s" "t" "." "c" "o" "m"]]]

      (parse "[각주링크]: http://test.com?test=t#123   \"링크 설명\"\n")
      [:문서 [:각주링크
              [:각주이름 "각" "주" "링" "크"]
              [:각주주소 "h" "t" "t" "p" ":" "/" "/" "t" "e" "s" "t" "." "c" "o" "m" "?" "t" "e" "s" "t" "=" "t" "#" "1" "2" "3"]
              [:링크타이틀 "링" "크" " " "설" "명"]]]))

  (testing "소스코드"
    (are [시도 결과] (= 시도 결과)
      (parse "``` clojure\n(def a 1)\n```\n")
      [:문서 [:소스코드
              [:소스언어 "c" "l" "o" "j" "u" "r" "e"]
              [:소스내용 "(" "d" "e" "f" " " "a" " " "1" ")" "\n"]]]

      (parse "```\n(def a 1)\n```\n")
      [:문서 [:소스코드 [:소스내용 "(" "d" "e" "f" " " "a" " " "1" ")" "\n"]]]

      (parse "```clojure\n(def a 1)\n```\n그리고\n\n```\n한번더\n```\n")
      [:문서
       [:소스코드 [:소스언어 "c" "l" "o" "j" "u" "r" "e"]
        [:소스내용 "(" "d" "e" "f" " " "a" " " "1" ")" "\n"]]
       [:문단 [:문장 "그" "리" "고"]]
       [:소스코드 [:소스내용 "한" "번" "더" "\n"]]]))

  (testing "원문"
    (are [시도 결과] (= 시도 결과)
      (parse "    원문 텍스트는 줄넘김\n    문자도 유지\n")
      [:문서 [:원문 "원" "문" " " "텍" "스" "트" "는" " "
              "줄" "넘" "김" "\n" "문" "자" "도" " " "유" "지" "\n"]]))

  (testing "인용"
    (are [시도 결과] (= 시도 결과)
      (parse "> 짜라투스트라는 *이렇게* 말했다\n")
      [:문서 [:인용 [:문장 "짜" "라" "투" "스" "트" "라" "는" " " "*" "이" "렇" "게" "*" " " "말" "했" "다"]]]

      (parse "> 한줄\n> 두줄\n평문\n")
      [:문서 [:인용 [:문장 "한" "줄"] [:문장 "두" "줄"]] [:문단 [:문장 "평" "문"]]]

      (parse "> 첫줄\n>\n> 다음\n")
      [:문서 [:인용 [:문장 "첫" "줄"] [:빈줄] [:문장 "다" "음"]]]))

  (testing "목록"
    (are [시도 결과] (= 시도 결과)
      (parse "* 일반목록\n* 다음\n")
      [:문서 [:일반목록 [:항목 [:문장 "일" "반" "목" "록"]] [:항목 [:문장 "다" "음"]]]]

      (parse "* 첫번째\n  첫째이어서\n* 두번째\n")
      [:문서 [:일반목록
              [:항목 [:문장 "첫" "번" "째"] [:문장 "첫" "째" "이" "어" "서"]]
              [:항목 [:문장 "두" "번" "째"]]]]

      (parse "1. 첫번째\n2. 두번째\n")
      [:문서 [:숫자목록 [:항목 [:문장 "첫" "번" "째"]] [:항목 [:문장 "두" "번" "째"]]]]

      (parse "1. 첫번째\n  이어서\n2. 두번째\n")
      [:문서 [:숫자목록
              [:항목 [:문장 "첫" "번" "째"] [:문장 "이" "어" "서"]]
              [:항목 [:문장 "두" "번" "째"]]]]))

  (testing "제목"
    (are [시도 결과] (= 시도 결과)
      (parse "# 큰제목\n")
      [:문서 [:큰제목 [:문장 "큰" "제" "목"]]]

      (parse "# 큰제목 ####\n")
      [:문서 [:큰제목 [:문장 "큰" "제" "목"]]]

      (parse "## 작은제목\n")
      [:문서 [:작은제목 [:문장 "작" "은" "제" "목"]]]

      (parse "## 작은제목 #\n")
      [:문서 [:작은제목 [:문장 "작" "은" "제" "목"]]]

      (parse "# **강조**제목\n다음\n")
      [:문서
       [:큰제목 [:문장 "*" "*" "강" "조" "*" "*" "제" "목"]]
       [:문단 [:문장 "다" "음"]]]

      (parse "큰 제목\n=====\n일반문단\n")
      [:문서 [:큰제목 [:문장 "큰" " " "제" "목"]] [:문단 [:문장 "일" "반" "문" "단"]]]

      (parse "작은 제목\n---\n일반문단\n")
      [:문서 [:작은제목 [:문장 "작" "은" " " "제" "목"]] [:문단 [:문장 "일" "반" "문" "단"]]]))

  (testing "문단"
    (are [시도 결과] (= 시도 결과)
      (parse "\n")
      [:문서 [:문단]]

      (parse "문장 하나\n문장 둘.\n\n")
      [:문서 [:문단 [:문장 "문" "장" " " "하" "나"] [:문장 "문" "장" " " "둘" "."]]]

      (parse "문단 하나\n\n문단 둘.\n")
      [:문서 [:문단 [:문장 "문" "단" " " "하" "나"]] [:문단 [:문장 "문" "단" " " "둘" "."]]]))

  (testing "문서전체"
    (is (= (parse
            "# 큰제목
## 작은제목
첫문단

* 항목1
* 항목2 *강조*

``` 클로저
(defn a-func []
  true)
```
마지막 문단
")
           [:문서
            [:큰제목 [:문장 "큰" "제" "목"]]
            [:작은제목 [:문장 "작" "은" "제" "목"]]
            [:문단 [:문장 "첫" "문" "단"]]
            [:일반목록 [:항목 [:문장 "항" "목" "1"]]
             [:항목 [:문장 "항" "목" "2" " " "*" "강" "조" "*"]]]
            [:소스코드 [:소스언어 "클" "로" "저"]
             [:소스내용 "(" "d" "e" "f" "n" " " "a" "-" "f" "u" "n" "c" " " "[" "]" "\n" " " " " "t" "r" "u" "e" ")" "\n"]]
            [:문단 [:문장 "마" "지" "막" " " "문" "단"]]]))))