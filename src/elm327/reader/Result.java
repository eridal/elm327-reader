package elm327.reader;

enum Result {
    
    OK("OK"),
    UNKNOWN("?"),
    NO_DATA("NO DATA");
    
    public final String message;
    
    Result(String message) {
        this.message = message;
    }
    
    public boolean matches(String response) {
        return response.endsWith(message);
    }
}
