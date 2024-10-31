# Paths
SRC_DIR = src
LIB_DIR = lib
BUILD_DIR = build

# Java and JVM settings
JAVAC = $(JAVA_HOME)/bin/javac
JAVA = $(JAVA_HOME)/bin/java

# Libraries
JAR_FILES = $(LIB_DIR)/core.jar:$(LIB_DIR)/gluegen-rt.jar:$(LIB_DIR)/jogl-all.jar
DYLIB_PATH = $(LIB_DIR)/macos-aarch64 # depend your env

# classes
MAIN_CLASS = main/Main
JAVA_FILES = $(shell find $(SRC_DIR) -name "*.java")

## Target for compiling Java files
target: $(JAVA_FILES)
	mkdir -p $(BUILD_DIR)
	$(JAVAC) -classpath $(JAR_FILES) -d $(BUILD_DIR) $(JAVA_FILES)

# Run the Java program
run: target
	$(JAVA) -Djava.library.path=$(DYLIB_PATH) -classpath $(BUILD_DIR):$(JAR_FILES) $(MAIN_CLASS)

# Clean the build directory
clean:
	rm -rf $(BUILD_DIR)/*
