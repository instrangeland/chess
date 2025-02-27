package error;

public class BadRequestError extends ResponseError {
  public BadRequestError() {
    super("Error: bad request", 400);
  }
}
