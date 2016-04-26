(ns tufdown.block
  (:require [instaparse.core :as insta]))

(def parse
  (insta/parser
   "문서         := (블럭 / 각주 / 문단)+
    <블럭>       := 구분줄 / 제목 / 목록 / 인용 / 원문 / 소스코드

    (* 제목 *)
    <제목>       := 일반제목 <LF> / 밑줄제목
    <일반제목>   := <'##' 공백> 작은제목 <공백? '#'+>? / <'#' 공백> 큰제목 <공백? '#'+>?
    <밑줄제목>   := 큰제목 <LF '='+ LF> / 작은제목 <LF '-'+ LF>
    큰제목       := 문장
    작은제목     := 문장

    (* 목록 *)
    <목록>       := 일반목록 / 숫자목록
    일반목록     := (일반마커 항목 <LF>)+ <LF>?
    숫자목록     := (숫자마커 항목 <LF>)+ <LF>?
    <일반마커>   := <공백? 공백? 공백? ('*' | '+' | '-') 공백+>
    <숫자마커>   := <공백? 공백? 공백? 숫자+ '.' 공백+>
    항목         := 문장 (<LF> !(빈줄 | 일반마커 | 숫자마커) <#'[ ]*'> 문장)*

    (* 인용 *)
    인용         := (<'>' 공백?> (빈줄 / 문장 <LF>))+

    (* 원문 *)
    원문         := (<공백4> ANY* LF)+

    (* 소스코드 *)
    소스코드     := <\"```\" 공백*> (<LF> 소스내용 / 소스언어 <LF> 소스내용) <\"```\" LF LF?>
    소스언어     := ANY+
    소스내용     := (ANY+ LF)*

    (* 구분줄 *)
    구분줄       := <구분줄표시 LF>
    구분줄표시   := #'(- ?){3,}' / #'(\\* ?){3,}' / #'(_ ?){3,}'

    (* 각주 *)
    <각주>       := 각주링크
    각주링크     := <'['> 각주이름 <']:' 공백+> 각주주소 링크타이틀? <LF>
    각주이름     := ANY+
    각주주소     := (!공백 ANY)+
    링크타이틀   := <공백+ '\"'> (!'\"' ANY)+ <'\"'>

    (* 문단 *)
    문단         := (문장 <LF> / 빈줄)* <LF>?
    빈줄         := 공백* <LF>
    문장         := ANY+
    <ANY>        := #'.'
    공백         := #' '
    공백4        := 공백 공백 공백 공백
    <LF>         := '\\n'
    <숫자>       := #'[0-9]'
   "))
