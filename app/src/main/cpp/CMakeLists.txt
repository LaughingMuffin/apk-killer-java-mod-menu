cmake_minimum_required(VERSION 3.8)

project("muffin")

add_subdirectory(thirdParty/whale)

set(CMAKE_CXX_STANDARD 17)

set(LINKER_FLAGS "-ffixed-x18 -Wl,--hash-style=both")
set(C_FLAGS "-Werror=format -fdata-sections -ffunction-sections -std=c2x -Wno-error=format-security -w -fno-rtti -fpermissive")
set(CXX_FLAGS "${CXX_FLAGS} -Wno-error=format-security -ffunction-sections -fdata-sections -w -Werror -s -std=c++20 -Wno-error=c++20-narrowing -fms-extensions -fno-rtti -fpermissive")

if (NOT CMAKE_BUILD_TYPE STREQUAL "Debug")
    set(C_FLAGS "${C_FLAGS} -fvisibility=hidden -fvisibility-inlines-hidden")
    set(LINKER_FLAGS "${LINKER_FLAGS} -Wl,-exclude-libs,ALL -Wl,--gc-sections -Wl,--strip-all")
endif ()

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${C_FLAGS} -O0")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${C_FLAGS} ${CXX_FLAGS} -O0")

set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} ${LINKER_FLAGS}")
set(CMAKE_MODULE_LINKER_FLAGS "${CMAKE_MODULE_LINKER_FLAGS} ${LINKER_FLAGS}")

include_directories(thirdParty/whale/include)

add_library(muffin SHARED
        Muffin.cpp
        thirdParty/elf/ElfImg.cpp)

target_link_libraries(muffin whale android log)