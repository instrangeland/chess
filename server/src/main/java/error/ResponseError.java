package error;

public class ResponseError extends RuntimeException {
    public int getCode() {
        return code;
    }

    private final int code;
    public ResponseError(String message, int code) {
        super(message);
        this.code = code;
    }
}
