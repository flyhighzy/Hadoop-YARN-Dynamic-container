# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/src

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/target/native

# Include any dependencies generated for this target.
include CMakeFiles/test_libhdfs_threaded.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/test_libhdfs_threaded.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/test_libhdfs_threaded.dir/flags.make

CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o: CMakeFiles/test_libhdfs_threaded.dir/flags.make
CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o: /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/src/main/native/libhdfs/test_libhdfs_threaded.c
	$(CMAKE_COMMAND) -E cmake_progress_report /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/target/native/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o"
	/usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o   -c /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/src/main/native/libhdfs/test_libhdfs_threaded.c

CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.i"
	/usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/src/main/native/libhdfs/test_libhdfs_threaded.c > CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.i

CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.s"
	/usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/src/main/native/libhdfs/test_libhdfs_threaded.c -o CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.s

CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o.requires:
.PHONY : CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o.requires

CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o.provides: CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o.requires
	$(MAKE) -f CMakeFiles/test_libhdfs_threaded.dir/build.make CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o.provides.build
.PHONY : CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o.provides

CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o.provides.build: CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o

# Object files for target test_libhdfs_threaded
test_libhdfs_threaded_OBJECTS = \
"CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o"

# External object files for target test_libhdfs_threaded
test_libhdfs_threaded_EXTERNAL_OBJECTS =

test_libhdfs_threaded: CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o
test_libhdfs_threaded: CMakeFiles/test_libhdfs_threaded.dir/build.make
test_libhdfs_threaded: target/usr/local/lib/libhdfs.so.0.0.0
test_libhdfs_threaded: libnative_mini_dfs.a
test_libhdfs_threaded: target/usr/local/lib/libhdfs.so.0.0.0
test_libhdfs_threaded: /usr/lib/jdk/jdk1.6.0_35/jre/lib/amd64/server/libjvm.so
test_libhdfs_threaded: CMakeFiles/test_libhdfs_threaded.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking C executable test_libhdfs_threaded"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/test_libhdfs_threaded.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/test_libhdfs_threaded.dir/build: test_libhdfs_threaded
.PHONY : CMakeFiles/test_libhdfs_threaded.dir/build

CMakeFiles/test_libhdfs_threaded.dir/requires: CMakeFiles/test_libhdfs_threaded.dir/main/native/libhdfs/test_libhdfs_threaded.c.o.requires
.PHONY : CMakeFiles/test_libhdfs_threaded.dir/requires

CMakeFiles/test_libhdfs_threaded.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/test_libhdfs_threaded.dir/cmake_clean.cmake
.PHONY : CMakeFiles/test_libhdfs_threaded.dir/clean

CMakeFiles/test_libhdfs_threaded.dir/depend:
	cd /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/target/native && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/src /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/src /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/target/native /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/target/native /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-hdfs-project/hadoop-hdfs/target/native/CMakeFiles/test_libhdfs_threaded.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/test_libhdfs_threaded.dir/depend

