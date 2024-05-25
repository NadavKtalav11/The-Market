package ServiceLayer;

public class Response<T> {
    private T result;
    private String description;
    private boolean success;

    public Response(T result, String description) {
        this.result = result;
        this.description = description;
        this.success = result != null;
    }

    public Response(T result) {
        this(result, "");
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
        this.success = result != null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSuccess() {
        return success;
    }

}

