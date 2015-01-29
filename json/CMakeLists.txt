project(json)
set(LIBRARY_OUTPUT_PATH ${json_SOURCE_DIR}/../Common/Lib )
add_definitions(-g -D _LIB -D __LINUX__)
include_directories(
./include
./src
)
link_directories(
${json_SOURCE_DIR}/lib
)
file(GLOB ALL_HEADER_SRCS include/json/*.h )
file(GLOB ALL_CXX_SRCS src/lib_json/*.h src/lib_json/*.cpp src/lib_json/*.inl)
set(ALL_LIB pthread dl)
add_library(json STATIC ${ALL_HEADER_SRCS} ${ALL_CXX_SRCS})
target_link_libraries(json ${ALL_LIB})
