package groupbee.employee;

public enum LoginStatusEnum {
    OK(200,"로그인 성공"),
    BAD_REQUEST(400,"BAD_REQUEST"),
    BAD_ID(401,"존재하지 않는 사용자입니다."),
    BAD_PASSWORD(402,"비밀번호가 일치하지 않습니다."),
    NOT_FOUND(403,"권한이 없습니다.");

    final int code;
    final String message;

    LoginStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
