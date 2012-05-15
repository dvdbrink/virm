PRJ_DIR			:= /home/daniel/dev/projects/virm/source/server/
LIB_DIR 		:= $(PRJ_DIR)lib/
JNI_DIR			:= $(PRJ_DIR)jni/

CXX 			:= g++-4.6
CFLAGS 			:= -fPIC -shared -std=c++0x
INCLUDES 		:= -I/usr/lib/jvm/java-7-openjdk-i386/include
SOURCE_CPU 		:= $(JNI_DIR)Recognize.cpp
SOURCE_GPU 		:= $(JNI_DIR)RecognizeGpu.cpp
LIBRARIES_CPU 	:= -L/usr/local/lib -lopencv_core -lopencv_features2d -lopencv_highgui
LIBRARIES_GPU 	:= $(LIBRARIES_CPU) -lopencv_gpu
OUT_CPU			:= $(LIB_DIR)librecognize.so
OUT_GPU 		:= $(LIB_DIR)librecognizegpu.so

all: cpu gpu

cpu:
	@echo -n compiling shared cpu library...
	@$(CXX) $(CFLAGS) $(INCLUDES) $(SOURCE_CPU) $(LIBRARIES_CPU) -o $(OUT_CPU)
	@echo done

gpu:
	@echo -n compiling shared gpu library...
	@$(CXX) $(CFLAGS) $(INCLUDES) $(SOURCE_GPU) $(LIBRARIES_GPU) -o $(OUT_GPU)
	@echo done

clean:
	@$(RM) $(OUT_CPU) $(OUT_GPU)
	@echo removed $(OUT_CPU)
	@echo removed $(OUT_GPU)