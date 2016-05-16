(ns tufdown.span-test
  (:require [tufdown.span :refer [parse]]
            [clojure.test :refer :all]))

(deftest parse-test

  (testing "각주"
    (are [시도 결과] (= 시도 결과)
      (parse "측주를 달아보자.[>1]")
      [:문장 "측" "주" "를" " " "달" "아" "보" "자" "." [:측주 [:참조이름 "1"]]]))

  (testing "링크"
    (are [시도 결과] (= 시도 결과)
      (parse "[링크](주소)를 걸어보자")
      [:문장 [:일반링크 [:링크텍스트 "링" "크"] [:주소 "주" "소"]] "를" " " "걸" "어" "보" "자"]

      (parse "[[링크\\]](주소)걸기")
      [:문장 [:일반링크 [:링크텍스트 "[" "링" "크" [:ESC "]"]] [:주소 "주" "소"]] "걸" "기"]

      (parse "<http://자동.com/> 테스트>")
      [:문장 [:자동링크 "h" "t" "t" "p" "://" "자" "동" "." "c" "o" "m" "/"] " " "테" "스" "트" ">"]

      (parse "[참조 링크][1]")
      [:문장 [:참조링크 [:링크텍스트 "참" "조" " " "링" "크"] [:참조이름 "1"]]]

      (parse "[참조 링크][]")
      [:문장 [:참조링크 [:참조이름 "참" "조" " " "링" "크"]]]

      (parse "[참조 링크] [1]")
      (parse "[참조 링크][1]")))

  (testing "강조"
    (are [시도 결과] (= 시도 결과)
      (parse "*이탤릭* 시작")
      [:문장 [:기울임 "이" "탤" "릭"] " " "시" "작"]

      (parse "_이탤릭_으로 시작")
      [:문장 [:기울임 "이" "탤" "릭"] "으" "로" " " "시" "작"]

      (parse "**굵게*강조** *이탤릭*한 문장")
      [:문장 [:굵게 "굵" "게" "*" "강" "조"] " " [:기울임 "이" "탤" "릭"] "한" " " "문" "장"]

      (parse "이렇게 __굵게_강조__한 문장")
      [:문장 "이" "렇" "게" " " [:굵게 "굵" "게" "_" "강" "조"] "한" " " "문" "장"]

      (parse "이렇게 `코드` 인용한 문장")
      [:문장 "이" "렇" "게" " " [:코드 "코" "드"] " " "인" "용" "한" " " "문" "장"]

      (parse "이렇게 ``a`b`` 인용한 문장")
      [:문장 "이" "렇" "게" " " [:코드 "a" "`" "b"] " " "인" "용" "한" " " "문" "장"])))
