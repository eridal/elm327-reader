package elm327.reader;

public class Results {

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

        Answer(Command<T> command, T data) {
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

        Unknown(Command<T> command) {
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

        NoData(Command<T> command) {
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

        Error(Command<T> command, Exception error) {
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
            return true;
        }
    }

    interface RetryCommand {

    }
}
