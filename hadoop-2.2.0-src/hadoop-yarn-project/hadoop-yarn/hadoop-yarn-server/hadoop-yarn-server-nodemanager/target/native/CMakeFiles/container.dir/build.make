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
CMAKE_SOURCE_DIR = /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/target/native

# Include any dependencies generated for this target.
include CMakeFiles/container.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/container.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/container.dir/flags.make

CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o: CMakeFiles/container.dir/flags.make
CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o: /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/native/container-executor/impl/configuration.c
	$(CMAKE_COMMAND) -E cmake_progress_report /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/target/native/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o"
	/usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o   -c /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/native/container-executor/impl/configuration.c

CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.i"
	/usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/native/container-executor/impl/configuration.c > CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.i

CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.s"
	/usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/native/container-executor/impl/configuration.c -o CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.s

CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o.requires:
.PHONY : CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o.requires

CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o.provides: CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o.requires
	$(MAKE) -f CMakeFiles/container.dir/build.make CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o.provides.build
.PHONY : CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o.provides

CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o.provides.build: CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o

CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o: CMakeFiles/container.dir/flags.make
CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o: /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/native/container-executor/impl/container-executor.c
	$(CMAKE_COMMAND) -E cmake_progress_report /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/target/native/CMakeFiles $(CMAKE_PROGRESS_2)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o"
	/usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o   -c /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/native/container-executor/impl/container-executor.c

CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.i"
	/usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/native/container-executor/impl/container-executor.c > CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.i

CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.s"
	/usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src/main/native/container-executor/impl/container-executor.c -o CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.s

CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o.requires:
.PHONY : CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o.requires

CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o.provides: CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o.requires
	$(MAKE) -f CMakeFiles/container.dir/build.make CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o.provides.build
.PHONY : CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o.provides

CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o.provides.build: CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o

# Object files for target container
container_OBJECTS = \
"CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o" \
"CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o"

# External object files for target container
container_EXTERNAL_OBJECTS =

libcontainer.a: CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o
libcontainer.a: CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o
libcontainer.a: CMakeFiles/container.dir/build.make
libcontainer.a: CMakeFiles/container.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking C static library libcontainer.a"
	$(CMAKE_COMMAND) -P CMakeFiles/container.dir/cmake_clean_target.cmake
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/container.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/container.dir/build: libcontainer.a
.PHONY : CMakeFiles/container.dir/build

CMakeFiles/container.dir/requires: CMakeFiles/container.dir/main/native/container-executor/impl/configuration.c.o.requires
CMakeFiles/container.dir/requires: CMakeFiles/container.dir/main/native/container-executor/impl/container-executor.c.o.requires
.PHONY : CMakeFiles/container.dir/requires

CMakeFiles/container.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/container.dir/cmake_clean.cmake
.PHONY : CMakeFiles/container.dir/clean

CMakeFiles/container.dir/depend:
	cd /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/target/native && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/src /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/target/native /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/target/native /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-yarn-project/hadoop-yarn/hadoop-yarn-server/hadoop-yarn-server-nodemanager/target/native/CMakeFiles/container.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/container.dir/depend

