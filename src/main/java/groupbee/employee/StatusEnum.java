package groupbee.employee;

public enum StatusEnum {
    OK(200,"동기화 성공"),
    BAD_REQUEST(400,"로그인 필요");
    final int code;
    final String message;
    StatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
