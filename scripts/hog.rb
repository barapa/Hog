#!/usr/bin/env ruby

###################################################

# Hadoop Configuration
HADOOP_HOME="/Users/ktran/hadoop-1.0.1"
HADOOP_VERSION="1.0.1"
JAVA_HOME="/usr/libexec/java_home"

# Pascal to Java File Configuration
JFLAGS = "-g"
JAVAC ="javac -classpath .:../build/java_cup_v10k"
CUP = "java -classpath .:../build/java_cup_v10k java_cup.Main <"
JFLEX = "../build/jflex-1.4.3/bin/jflex"
JAVA = "java -classpath .:../build/java_cup_v10k"
CUP_FILE = "../compilation-example/pascal-tokens.cup"
JFLEX_FILE = "../compilation-example/pascal.jflex"
JAVAC_FILES = "../compilation-example/PascalLexer.java ../compilation-example/PascalLexerTest.java ../compilation-example/sym.java"
JAVA_EXECUTABLE = " ../compilation-example/PascalLexerTest ../compilation-example/lexer-test.p"

###################################################



#Parsing Hog Into Java

def java_cup_command
    system("#{CUP} #{CUP_FILE}")
end

def jflex_command
    system("#{JFLEX} #{JFLEX_FILE}")
end

def javac_command
    system("#{JAVAC} #{JAVAC_FILES}")
end

def java_lexer_command
    system("#{JAVA} #{JAVA_EXECUTABLE}")
end

java_cup_command
jflex_command
javac_command
java_lexer_command


# /Users/ktran/hadoop-1.0.1/bin/hadoop jar wordcount.jar WordCount input output
#
#


COMPILE_CMD = "javac -classpath .:/Users/ktran/hadoop-1.0.1/hadoop-core-1.0.1.jar:/Users/ktran/hadoop-1.0.1/lib/commons-cli-1.2.jar WordCount.java"
system("#{COMPILE_CMD} ")
system("jar cvf wordcount.jar *.class  ")
system("/Users/ktran/hadoop-1.0.1/bin/hadoop jar wordcount.jar WordCount input output")