package error;

public class TakenError extends ResponseError {
  public TakenError() {
    super("Error: already taken", 403);
  }
}
