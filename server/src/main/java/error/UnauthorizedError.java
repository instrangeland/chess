package error;

public class UnauthorizedError extends ResponseError {
  public UnauthorizedError() {
    super("Error: unauthorized", 401);
  }
}
