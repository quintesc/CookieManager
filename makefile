default: run

run: FrontEnd.class
	java BackEnd

FrontEnd.class:
	javac Pair.java
	javac MapADT.java
	javac HashTableMap.java
	javac Order.java
	javac CookiePack.java
	javac Client.java
	javac FrontEnd.java
	javac BackEnd.java

clean:
	rm *.class
