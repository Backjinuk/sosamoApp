if(NOT TARGET hermes-engine::libhermes)
add_library(hermes-engine::libhermes SHARED IMPORTED)
set_target_properties(hermes-engine::libhermes PROPERTIES
    IMPORTED_LOCATION "C:/Users/adm/.gradle/caches/8.12/transforms/9d0b7fef1f891c77146b1ae4749e40bb/transformed/hermes-android-0.78.0-debug/prefab/modules/libhermes/libs/android.x86_64/libhermes.so"
    INTERFACE_INCLUDE_DIRECTORIES "C:/Users/adm/.gradle/caches/8.12/transforms/9d0b7fef1f891c77146b1ae4749e40bb/transformed/hermes-android-0.78.0-debug/prefab/modules/libhermes/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

