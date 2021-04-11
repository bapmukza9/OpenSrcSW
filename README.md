# 오픈소스 SW 실습
#### 컴퓨터공학부 201811211 이인우

오픈소스 SW 실습 파일들입니다.

## 실행방법
cmd창이나 git bash를 통해 src 디렉터리로 이동합니다.
그리고 아래의 명령어들을 순서대로 입력합니다.


컴파일 :
```sh
javac -cp "lib/*;" search_engine/kuir.java
```

2주차 프로그램:
```sh
java -cp "lib/*;" search_engine.kuir -c ./data/
```

3주차 프로그램:
```sh
java -cp "lib/*;" search_engine.kuir -k ./collection.xml
```

4주차 프로그램:
```sh
java -cp "lib/*;" search_engine.kuir -i ./index.xml
```

5~7주차 프로그램:
```sh
java -cp "lib/*;" search_engine.kuir -s ./index.post -q "질의어"
```
