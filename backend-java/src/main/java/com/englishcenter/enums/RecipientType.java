package com.englishcenter.enums;

public enum RecipientType {
    individual, // gửi cho cá nhân
    @SuppressWarnings("checkstyle:MemberName")
    class_type, // gửi cho cả lớp - dùng "class" trong JSON via @JsonProperty
    all          // gửi cho tất cả
    ;

    // Trong DB lưu: "individual", "class", "all"
    // Vì "class" là keyword Java nên enum value là class_type
    // nhưng khi serialize/deserialize sẽ map thành "class"
}
