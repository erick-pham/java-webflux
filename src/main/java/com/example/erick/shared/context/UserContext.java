package com.example.erick.shared.context;

public final class UserContext {
    private UserContext() {} // Chống khởi tạo object

    private static final ThreadLocal<UserRequestDTO> USER_HOLDER = new ThreadLocal<>();

    public static void set(UserRequestDTO dto) {
        USER_HOLDER.set(dto);
    }

    public static UserRequestDTO get() {
        return USER_HOLDER.get();
    }

    public static void clear() {
        USER_HOLDER.remove(); // Giải phóng RAM và tránh leak data giữa các thread
    }

    // Helper để lấy nhanh userId mà không cần check null bên ngoài
    public static String getCurrentUserId() {
        UserRequestDTO user = get();
        return (user != null) ? user.getUserId() : "SYSTEM";
    }
}
