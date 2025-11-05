package ServiceLayer;

// This class is designed to raise the error from the system to the client.

public class Response {
    public String msg;
    public boolean isError;
    public ResponseType type;

    public Response(boolean error, String message, ResponseType type){
        this.isError = error;
        this.msg = message;
        this.type = type;
    }

    @Override
    public String toString() {
        return (isError ? "[ERROR] " : "[OK] ") + msg + " (" + type + ")";
    }
}