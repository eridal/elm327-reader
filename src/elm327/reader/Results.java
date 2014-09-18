package elm327.reader;

public class Results {

    public static <T> Result<T> parse(String response, Command<T> command) {
        try {
            String data = extractData(response, command);

            for (Response r : Response.values()) {
                if (r.matches(data)) {
                    return r.createResult(command, data);
                }
            }

            T value = command.parse(data);
            return new Results.Answer<T>(command, value);

        } catch (ResultException e) {
            return new Results.Error<T>(command, e);
        }
    }

    enum Response {

        UNKNOWN("?") {
            @Override public <T> Result<T> createResult(Command<T> command, String data) {
                return new Results.Unknown<T>(command);
            }
        },

        NO_DATA("NO DATA") {
            @Override public <T> Result<T> createResult(Command<T> command, String data) {
                return new Results.NoData<T>(command);
            }
        };

        private final String result;

        Response(String result) {
            this.result = result;
        }

        public boolean matches(String data) {
            return result.equals(data);
        }

        abstract <T> Result<T> createResult(Command<T> command, String data);
    }

    private static <T> String extractData(String response, Command<T> command) throws ResultException {

        String[] fragments = split(response);

        if (fragments.length == 1) {
            return fragments[0];
        }

        if (fragments.length == 2) {
            Message message = command.message();
            if (message.hasCode(fragments[0])) {
                return fragments[1];
            }
        }

        throw new ResultException();
    }

    private static String[] split(String response) throws ResultException {

        if (null == response || response.isEmpty()) {
            throw new ResultException();
        }

        response = response.trim();

        if (null == response || response.isEmpty()) {
            throw new ResultException();
        }

        return response.split("\n+");
    }

    private static abstract class ResultAbstract<T> implements Result<T> {

        private final Command<T> command;

        private ResultAbstract(Command<T> command) {
            this.command = command;
        }

        @Override public Command<T> command() {
            return command;
        }
    }

    static class Answer<T> extends ResultAbstract<T> {

        private final T data;

        private Answer(Command<T> command, T data) {
            super(command);
            this.data = data;
        }

        @Override public T data() {
            return data;
        }

        @Override public boolean isError() {
            return false;
        }
    }

    static class Unknown<T> extends ResultAbstract<T> {

        private Unknown(Command<T> command) {
            super(command);
        }

        @Override public T data() {
            return null;
        }

        @Override public boolean isError() {
            return true;
        }
    };

    static class NoData<T> extends ResultAbstract<T> {

        private NoData(Command<T> command) {
            super(command);
        }

        @Override public T data() {
            return null;
        }

        @Override public boolean isError() {
            return false;
        }
    };

    static class Error<T> extends ResultAbstract<T> {

        public final Exception error;

        private Error(Command<T> command, Exception error) {
            super(command);
            this.error = error;
        }

        @Override public T data() {
            if (error instanceof RuntimeException) {
                throw (RuntimeException) error;
            } else {
                throw new RuntimeException(error);
            }
        }

        @Override public boolean isError() {
            return false;
        }
    }

    static <T> Result<T> createError(Command<T> command, Exception e) {
        return new Results.Error<T>(command, e);
    }
}
